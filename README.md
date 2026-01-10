# Spring Batch - Laboratório de Importação de Usuários

## 📋 Descrição

Projeto de laboratório desenvolvido com **Spring Boot** e **Spring Batch** para demonstrar o processamento em lote (batch) de dados. O projeto realiza a importação de um arquivo CSV com dados de usuários para um banco de dados MySQL.

## 🎯 Objetivo

Criar um pipeline de processamento em lote que:
- Leia dados de um arquivo CSV (`users.csv`)
- Processe os registros em chunks de 10 itens
- Escreva os dados no banco de dados MySQL
- Gerencie o estado da execução através do Spring Batch

## 🛠️ Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 4.0.1**
- **Spring Batch** - Framework para processamento em lote
- **Spring Data JPA** - Persistência de dados
- **MySQL** - Banco de dados relacional
- **Maven** - Gerenciador de dependências
- **H2 Database** - Console H2 para testes

## 📦 Estrutura do Projeto

```
lab-spring-batch/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/victorhugolgr/lab/
│   │   │       ├── SpringBatchApplication.java       # Classe principal
│   │   │       ├── dto/
│   │   │       │   └── User.java                     # Record de usuário
│   │   │       ├── config/                           # Configurações
│   │   │       └── jobs/
│   │   │           └── importuser/
│   │   │               ├── ImportUsersJobConfig.java # Job de importação
│   │   │               ├── UserReaderConfig.java     # Leitor CSV
│   │   │               └── UserWriterConfig.java     # Escritor banco de dados
│   │   └── resources/
│   │       ├── application.properties                # Configurações da aplicação
│   │       ├── schema.sql                            # Script de criação de tabelas
│   │       └── users.csv                             # Arquivo com 1000 registros
│   └── test/
│       └── java/...                                  # Testes
└── pom.xml                                           # Configuração Maven
```

## 📊 Modelo de Dados

### User (Record)
```java
public record User(Long id, String name, String email) {}
```

**Campos:**
- `id`: Identificador único do usuário
- `name`: Nome do usuário
- `email`: Email do usuário

## 🔧 Configuração do Banco de Dados

O projeto está configurado para usar **MySQL** com as seguintes credenciais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/USER_DB
spring.datasource.username=root
spring.datasource.password=123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Requisitos:**
- MySQL Server instalado e rodando na porta 3306
- Banco de dados `USER_DB` será criado automaticamente

## 📝 Dados de Teste

O arquivo `users.csv` contém **1000 registros** de usuários no seguinte formato:

```csv
id,name,email
1,User0001,user1@example.com
2,User0002,user2@example.com
...
1000,User1000,user1000@example.com
```

## ⚙️ Configuração do Job

### Componentes Principais

#### 1. **ImportUsersJobConfig**
Define o job de importação de usuários com:
- **Step:** `csv-to-db-step`
- **Chunk Size:** 10 registros por chunk
- **Reader:** `FlatFileItemReader` (lê do CSV)
- **Writer:** `JdbcBatchItemWriter` (escreve no banco)

#### 2. **UserReaderConfig**
Configura o leitor de arquivo CSV:
- Recurso: `users.csv`
- Delimitador: Vírgula
- Campos: `id`, `name`, `email`
- Tipo alvo: `User.class`

#### 3. **UserWriterConfig**
Configura o escritor no banco de dados usando JDBC

## 🚀 Como Executar

### Pré-requisitos
- Java 25 instalado
- Maven instalado
- MySQL Server rodando

### Passos

1. **Criar o banco de dados** (opcional, será criado automaticamente):
```sql
CREATE DATABASE USER_DB;
```

2. **Compilar o projeto:**
```bash
cd lab-spring-batch/lab/lab
mvn clean install
```

3. **Executar a aplicação:**
```bash
mvn spring-boot:run
```

Ou:
```bash
java -jar target/lab-0.0.1-SNAPSHOT.jar
```

## 📊 Fluxo de Execução

```
users.csv (1000 registros)
    ↓
FlatFileItemReader (Lê CSV)
    ↓
Chunk Processing (10 por chunk = 100 chunks)
    ↓
JdbcBatchItemWriter (Escreve em batches)
    ↓
MySQL Database (Tabela users)
```

## 🔍 Características do Spring Batch

- ✅ **Processamento em Chunks**: Os dados são processados em lotes de 10 registros
- ✅ **Rastreamento de Execução**: Mantém histórico de execuções do job
- ✅ **Recuperação de Falhas**: Suporta reinicialização de jobs após falhas
- ✅ **Escalabilidade**: Preparado para processar grandes volumes de dados
- ✅ **Transações**: Cada chunk é uma transação separada

## 📈 Monitoramento

O Spring Batch mantém tabelas de metadados:
- `BATCH_JOB_INSTANCE` - Instâncias dos jobs
- `BATCH_JOB_EXECUTION` - Execuções do job
- `BATCH_STEP_EXECUTION` - Execuções de steps
- `BATCH_STEP_EXECUTION_CONTEXT` - Contexto de execução

## 🐛 Troubleshooting

### Erro de conexão com MySQL
```
Verifique se o MySQL está rodando e as credenciais estão corretas
```

### Job não processa dados
```
Certifique-se que o arquivo users.csv existe em src/main/resources/
```

### Erro ao criar tabelas
```
Execute o schema.sql manualmente ou verifique as permissões do usuário MySQL
```

## 📚 Referências

- [Spring Batch Documentation](https://spring.io/projects/spring-batch)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/en/)

## 👨‍💻 Autor

Victor Hugo LGR

## 📄 Licença

Projeto de laboratório para fins educacionais

---

**Última atualização:** Janeiro 2026
