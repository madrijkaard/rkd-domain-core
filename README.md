# RKD Domain Core

Serviço responsável pelo gerenciamento centralizado de domínios, subdomínios, atributos e opções configuráveis da plataforma RKD.

O projeto expõe uma API REST para criar, consultar, atualizar e excluir essas estruturas, mantendo seus relacionamentos e regras de negócio em um banco PostgreSQL.

## 1. Sobre este projeto

O `rkd-domain-core` funciona como o núcleo de definição de dados da plataforma.

Um **domínio** representa uma área ou entidade de negócio. Cada domínio pode possuir:

- atributos customizados;
- subdomínios hierárquicos;
- status ativo ou inativo;
- código e descrição;
- datas de criação e atualização.

Os atributos podem ser dos tipos:

`NUMBER`, `DECIMAL`, `TEXT`, `DATE`, `DATE_TIME`, `MONEY`, `OPTION` ou `JSON`.

Para atributos do tipo `OPTION`, a API permite associar uma lista de valores previamente cadastrada.

## 2. Tecnologias utilizadas

- **Java 25**
- **Quarkus 3.39.2**
- **Jakarta REST** para exposição dos endpoints
- **Hibernate ORM com Panache** para persistência
- **PostgreSQL 16** como banco de dados
- **Jackson** para serialização JSON
- **MapStruct** para conversão entre requests, entidades e responses
- **Maven** para gerenciamento e build
- **JUnit 5** para testes unitários
- **Mockito** para isolamento das regras de negócio
- **REST Assured** para testes de integração da API
- **Docker Compose** para inicialização do PostgreSQL

## 3. Estrutura do projeto

```text
src/
├── main/
│   ├── java/rkd/com/
│   │   ├── definition/   # Mapeadores de exceções HTTP
│   │   ├── dto/          # Objetos genéricos de resposta
│   │   ├── exception/    # Exceções de domínio
│   │   ├── mapper/       # Conversões com MapStruct
│   │   ├── message/      # Mensagens padronizadas
│   │   ├── model/        # Entidades JPA
│   │   ├── repository/   # Repositórios Panache
│   │   ├── request/      # Payloads de entrada
│   │   ├── resource/     # Endpoints REST
│   │   ├── response/     # Payloads de saída
│   │   ├── service/      # Regras de negócio
│   │   └── type/         # Enumeradores da aplicação
│   └── resources/
│       └── application.properties
├── test/
│   ├── java/rkd/com/
│   │   ├── resource/     # Testes REST com PostgreSQL
│   │   └── service/       # Testes unitários dos services
│   └── resources/
│       └── application.properties
├── docker-compose.yml     # PostgreSQL local
└── pom.xml
```

### Modelo de relacionamento

```text
Domain
├── parent              # domínio pai opcional
├── subdomains[]        # subdomínios filhos
├── attributes[]        # atributos configuráveis
└── status

Attribute
└── option              # utilizado quando type = OPTION
```

Um domínio pode possuir apenas um domínio pai, mas pode ter vários subdomínios. O sistema impede auto-referência e ciclos na hierarquia.

## 4. Funcionalidades

### Domínios

- Criar domínio raiz ou subdomínio.
- Consultar todos os domínios.
- Filtrar domínios por status:

  ```http
  GET /domain?status=true
  ```

- Consultar domínio por ID.
- Atualizar código, descrição, status e domínio pai.
- Excluir domínio sem subdomínios.
- Impedir domínio pai inexistente.
- Impedir ciclos na hierarquia.
- Retornar subdomínios de forma resumida, evitando recursão no JSON.

### Atributos

- Criar atributos vinculados a um domínio.
- Consultar todos os atributos.
- Filtrar por status.
- Consultar por ID.
- Atualizar atributos.
- Excluir atributos.
- Validar domínio associado.
- Exigir uma opção válida para atributos do tipo `OPTION`.

### Opções

- Criar listas de opções em JSONB.
- Exigir pelo menos duas opções.
- Consultar opções por ID.
- Filtrar por status.
- Atualizar valores e descrição.
- Excluir opções.

### Respostas de erro

A API padroniza erros com a seguinte estrutura:

```json
{
  "message": "Domínio pai não encontrado.",
  "type": "DOMAIN_NOT_FOUND",
  "timestamp": "2026-09-07T15:00:00"
}
```

Tipos principais:

- `DOMAIN_NOT_FOUND`
- `INVALID_DATA`
- `INVALID_ACTION`

## 5. Como inicializar a aplicação

### Pré-requisitos

- Java 25;
- Maven 3.9 ou superior;
- Docker Desktop com Docker Compose;
- PostgreSQL local, caso não utilize o Compose.

### Iniciar o PostgreSQL

Na raiz do projeto:

```bash
docker compose up -d
```

O Compose cria:

- banco: `domain_core`;
- usuário: `postgres`;
- senha: `postgres`;
- porta: `5432`.

Para verificar o container:

```bash
docker compose ps
```

Para interromper o banco:

```bash
docker compose down
```

### Variáveis de ambiente

Os valores padrão funcionam com o `docker-compose.yml`, mas podem ser substituídos:

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_USERNAME` | `postgres` | Usuário do PostgreSQL |
| `DB_PASSWORD` | `postgres` | Senha do PostgreSQL |
| `DB_URL` | `jdbc:postgresql://localhost:5432/domain_core` | URL JDBC |
| `QUARKUS_HIBERNATE_ORM_LOG_SQL` | `false` | Habilita logs SQL |

### Executar em modo desenvolvimento

```bash
mvn quarkus:dev
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

O Dev UI do Quarkus estará disponível em:

```text
http://localhost:8080/q/dev/
```

### Executar os testes

Com o PostgreSQL iniciado:

```bash
mvn test
```

Para executar o ciclo completo de verificação:

```bash
mvn verify
```

Os testes REST utilizam REST Assured e validam cabeçalhos, path params, query params, operações CRUD e persistência real no PostgreSQL.

### Gerar o pacote da aplicação

```bash
mvn package
```

Executar o artefato gerado:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

### Build nativo

Com GraalVM configurado:

```bash
mvn package -Dnative
```

Ou utilizando build nativo em container:

```bash
mvn package -Dnative -Dquarkus.native.container-build=true
```

## Licença

Este projeto está distribuído sob a licença definida em [LICENSE](LICENSE).
