#!/usr/bin/env node

import fs from 'node:fs';
import path from 'node:path';

const migrationsDir = path.resolve(process.argv[2] ?? '../sinan-core/apps/domain/migrations');
const outputFile = path.resolve(process.argv[3] ?? './load/domain_load.sql');

function matchingClose(text, start, opening, closing) {
  let depth = 0;
  let quote = null;
  let escaped = false;
  for (let i = start; i < text.length; i += 1) {
    const char = text[i];
    if (quote) {
      if (escaped) escaped = false;
      else if (char === '\\') escaped = true;
      else if (char === quote) quote = null;
      continue;
    }
    if (char === "'" || char === '"') {
      quote = char;
      continue;
    }
    if (char === opening) depth += 1;
    if (char === closing && --depth === 0) return i;
  }
  throw new Error(`Unclosed ${opening} in migration`);
}

function decodeString(value) {
  return value.replace(/\\(['"\\])/g, '$1').replace(/\\n/g, '\n');
}

function quotedValue(text, key) {
  const match = text.match(new RegExp(`${key}=(['"])((?:\\\\.|(?!\\1).)*)\\1`));
  return match ? decodeString(match[2]) : null;
}

function keywordValue(text, key) {
  const match = text.match(new RegExp(`\\b${key}\\s*=\\s*(True|False)`));
  return match ? match[1] === 'True' : false;
}

function choicesValue(text) {
  const marker = text.indexOf('choices=');
  if (marker < 0) return null;
  const start = text.indexOf('[', marker);
  if (start < 0) return null;
  const end = matchingClose(text, start, '[', ']');
  const choices = [];
  const expression = text.slice(start + 1, end);
  const regex = /\(\s*(['"])((?:\\.|(?!\1).)*)\1\s*,\s*(['"])((?:\\.|(?!\3).)*)\3\s*\)/g;
  for (const match of expression.matchAll(regex)) {
    choices.push([decodeString(match[2]), decodeString(match[4])]);
  }
  return choices;
}

function humanize(value) {
  return value.replaceAll('_', ' ').replace(/([a-z0-9])([A-Z])/g, '$1 $2').trim().toLowerCase()
    .replace(/^./, (char) => char.toUpperCase());
}

function parseMigration(file) {
  const text = fs.readFileSync(file, 'utf8');
  const nameMatch = text.match(/migrations\.CreateModel\(\s*name=['"]([^'"]+)['"]/);
  const fieldsStart = text.indexOf('fields=[', nameMatch?.index ?? 0);
  if (!nameMatch || fieldsStart < 0) throw new Error(`Invalid migration: ${file}`);
  const listStart = text.indexOf('[', fieldsStart);
  const listEnd = matchingClose(text, listStart, '[', ']');
  const fieldsText = text.slice(listStart + 1, listEnd);
  const fields = [];
  let index = 0;
  while (index < fieldsText.length) {
    const start = fieldsText.indexOf('(', index);
    if (start < 0) break;
    const end = matchingClose(fieldsText, start, '(', ')');
    const tuple = fieldsText.slice(start + 1, end);
    const fieldMatch = tuple.match(/^\s*(['"])((?:\\.|(?!\1).)*)\1\s*,\s*models\.([A-Za-z0-9_]+)\s*\(/);
    if (fieldMatch) {
      const fieldName = decodeString(fieldMatch[2]);
      const djangoType = fieldMatch[3];
      const argsStart = tuple.indexOf('(', fieldMatch.index + fieldMatch[0].length - 1);
      const argsEnd = matchingClose(tuple, argsStart, '(', ')');
      const args = tuple.slice(argsStart + 1, argsEnd);
      const choices = choicesValue(args);
      let type = 'TEXT';
      if (djangoType === 'CharField' && choices?.length) type = 'OPTION';
      else if (['BigAutoField', 'AutoField', 'IntegerField', 'PositiveIntegerField', 'SmallIntegerField'].includes(djangoType)) type = 'NUMBER';
      else if (djangoType === 'DecimalField') type = 'DECIMAL';
      else if (djangoType === 'DateField') type = 'DATE';
      else if (djangoType === 'DateTimeField') type = 'DATE_TIME';
      else if (djangoType === 'OneToOneField') type = 'JSON';
      fields.push({
        code: fieldName,
        description: quotedValue(args, 'verbose_name') ?? humanize(fieldName),
        type,
        mandatory: !(keywordValue(args, 'blank') || keywordValue(args, 'null')),
        choices,
      });
    }
    index = end + 1;
  }
  const slug = path.basename(file, '.py').split('_create_')[1];
  return { code: slug.toUpperCase(), description: humanize(nameMatch[1]), migration: path.basename(file), fields };
}

function sql(value) {
  return `'${String(value).replaceAll("'", "''")}'`;
}

const files = fs.readdirSync(migrationsDir)
  .filter((file) => /^\d+_create_.*\.py$/.test(file))
  .sort()
  .map((file) => parseMigration(path.join(migrationsDir, file)));
const options = new Map();
for (const domain of files) {
  for (const field of domain.fields) {
    if (field.type === 'OPTION' && field.choices?.length) options.set(`${domain.code}__${field.code.toUpperCase()}`, field.choices);
  }
}

const lines = [
  '-- Generated from sinan-core/apps/domain/migrations.',
  '-- Idempotent seed: rows already present are not duplicated.',
  '-- Mapping notes: OneToOneField -> JSON; TimeField -> TEXT (no TIME enum exists).',
  '', 'BEGIN;', '', '-- Domains',
];
for (const domain of files) {
  lines.push(`-- Source: ${domain.migration}`,
    'INSERT INTO domain (code, description, status, created_at, updated_at)',
    `SELECT ${sql(domain.code)}, ${sql(domain.description)}, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP`,
    `WHERE NOT EXISTS (SELECT 1 FROM domain WHERE code = ${sql(domain.code)});`, '');
}
lines.push('-- Options');
for (const [code, choices] of options) {
  const value = JSON.stringify({ options: choices.map(([choice, label]) => ({ value: choice, label })) });
  lines.push('INSERT INTO "option" (code, description, "values", status, created_at, updated_at)',
    `SELECT ${sql(code)}, ${sql(`Options for ${code}`)}, ${sql(value)}::jsonb, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP`,
    `WHERE NOT EXISTS (SELECT 1 FROM "option" WHERE code = ${sql(code)});`, '');
}
lines.push('-- Attributes');
for (const domain of files) {
  lines.push(`-- Source: ${domain.migration}`);
  for (const field of domain.fields) {
    const optionCode = field.type === 'OPTION' ? `${domain.code}__${field.code.toUpperCase()}` : null;
    const optionId = optionCode ? `(SELECT o.id FROM "option" o WHERE o.code = ${sql(optionCode)} LIMIT 1)` : 'NULL';
    lines.push('INSERT INTO attribute (code, description, type, mandatory, domain_id, option_id, status, created_at, updated_at)',
      `SELECT ${sql(field.code)}, ${sql(field.description)}, ${sql(field.type)}, ${String(field.mandatory).toUpperCase()}, d.id, ${optionId}, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP`,
      `FROM domain d WHERE d.code = ${sql(domain.code)}`,
      `  AND NOT EXISTS (SELECT 1 FROM attribute a WHERE a.domain_id = d.id AND a.code = ${sql(field.code)});`, '');
  }
}
lines.push('COMMIT;', '');
fs.mkdirSync(path.dirname(outputFile), { recursive: true });
fs.writeFileSync(outputFile, lines.join('\n'), 'utf8');
console.log(JSON.stringify({ migrations: files.length, domains: files.length, attributes: files.reduce((total, domain) => total + domain.fields.length, 0), options: options.size }));
