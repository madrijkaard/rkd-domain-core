#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/../docker-compose.yml"
DB_NAME="${DB_NAME:-domain_core}"
DB_USER="${DB_USER:-postgres}"

echo "WARNING: resetting the PostgreSQL container and volume defined by ${COMPOSE_FILE}."
echo "All existing data in this project database will be permanently removed."

echo "Stopping and removing the current PostgreSQL container and volume..."
docker compose -f "${COMPOSE_FILE}" down --volumes --remove-orphans

echo "Starting a fresh PostgreSQL instance from ${COMPOSE_FILE}..."
docker compose -f "${COMPOSE_FILE}" up -d postgres

echo "Waiting for PostgreSQL to become ready..."
until docker compose -f "${COMPOSE_FILE}" exec -T postgres pg_isready -U "${DB_USER}" -d "${DB_NAME}" >/dev/null 2>&1; do
  sleep 2
done

echo "Resetting schema and loading domains, attributes and options..."
docker compose -f "${COMPOSE_FILE}" exec -T postgres \
  psql -v ON_ERROR_STOP=1 -U "${DB_USER}" -d "${DB_NAME}" \
  < "${SCRIPT_DIR}/domain_load.sql"

echo "Load completed successfully."
