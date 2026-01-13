# Spring Batch - Laboratório de Importação de Usuários

## 📋 Descrição

Projeto de laboratório desenvolvido com **Spring Boot** e **Spring Batch** para demonstrar o processamento em lote (batch) de dados. O projeto realiza a importação de um arquivo CSV com dados de usuários para um banco de dados MySQL.

## 🎯 Objetivo

Criar um pipeline de processamento em lote que:
- **Leia múltiplos arquivos CSV** de uma pasta parametrizada ✨
- Obtenha o caminho da pasta da **tabela PROPERTIES** do banco ✨
- Processe os registros em chunks de 10 itens
- Filtre registros de acordo com critérios definidos
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
│   │   │   ├── br/com/victorhugolgr/lab/
│   │   │   │   ├── SpringBatchApplication.java           # Classe principal
│   │   │   │   ├── dto/
│   │   │   │   │   └── User.java                         # Record de usuário
│   │   │   │   └── jobs/
│   │   │   │       └── importuser/
│   │   │   │           ├── ImportUsersJobConfig.java     # Job de importação
│   │   │   │           ├── UserReaderConfig.java         # Leitor multi-arquivo parametrizado ✨
│   │   │   │           ├── UserFieldSetMapper.java       # Mapper para records ✨
│   │   │   │           ├── UserWriterConfig.java         # Escritor banco de dados
│   │   │   │           └── UserItemProcessor.java        # Processador/Filtro
│   │   │   │
│   │   │   ├── br/com/victorhugolgr/domain/             # Entidades
│   │   │   │   └── Property.java                         # Entidade JPA para properties ✨
│   │   │   │
│   │   │   ├── br/com/victorhugolgr/repository/         # Repositórios
│   │   │   │   └── PropertyRepository.java               # Repositório JPA ✨
│   │   │   │
│   │   │   └── br/com/victorhugolgr/service/            # Serviços
│   │   │       └── PropertyService.java                  # Serviço de properties ✨
│   │   │
│   │   └── resources/
│   │       ├── application.properties                    # Configurações da aplicação
│   │       ├── schema.sql                                # Script de criação de tabelas
│   │       └── csv/                                      # Pasta com arquivos CSV ✨
│   │           ├── users1.csv
│   │           ├── users2.csv
│   │           └── ...
│   └── test/
│       └── java/...                                      # Testes
└── pom.xml                                               # Configuração Maven
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

Os arquivos CSV na pasta configurada em PROPERTIES com ID `PATH_CSV` contêm registros de usuários:

```csv
id,name,email
1,User0001,user1@example.com
2,User0002,user2@example.com
...
```

### Configurar PATH_CSV

1. **Via SQL** (ao criar o banco):
```sql
INSERT INTO properties (id, value, description) 
VALUES ('PATH_CSV', '/home/victorhugolgr/git/lab-spring-batch/data/csv', 'Caminho dos arquivos CSV');
```

2. **Criar a pasta com arquivos CSV**:
```bash
mkdir -p /home/victorhugolgr/git/lab-spring-batch/data/csv
cp users.csv /home/victorhugolgr/git/lab-spring-batch/data/csv/
```

## ⚙️ Configuração do Job

### Componentes Principais

#### 1. **Property & PropertyRepository** ✨
Gerencia configurações parametrizadas no banco de dados:
```java
@Entity
@Table(name = "properties")
public class Property {
    @Id
    private String id;        // Ex: "PATH_CSV"
    private String value;     // Ex: "/home/.../csv"
    private String description;
}
```

#### 2. **PropertyService** ✨
Serviço para leitura de properties do banco:
```java
@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository repository;
    
    public String getPropertyValueById(String id) {
        return repository.findById(id)
            .map(property -> property.getValue())
            .orElseThrow(() -> new RuntimeException("Property not found"));
    }
}
```

#### 3. **UserReaderConfig** ✨
Configura o leitor de **múltiplos arquivos CSV** com caminho parametrizado:
- **MultiResourceItemReader**: Processa vários arquivos em sequência ✨
- **Caminho:** Lido dinamicamente de `PropertyService` (tabela PROPERTIES)
- **Delimitador:** Vírgula
- **Campos:** `id`, `name`, `email`
- **Skip Header:** Ignora primeira linha

#### 4. **UserFieldSetMapper** ✨
Mapper customizado para mapear CSV para **records** (Java 14+):
```java
public class UserFieldSetMapper implements FieldSetMapper<User> {
    @Override
    public User mapFieldSet(FieldSet fieldSet) throws BindException {
        return new User(
            fieldSet.readLong("id"),
            fieldSet.readString("name"),
            fieldSet.readString("email")
        );
    }
}
```

#### 5. **UserWriterConfig**
Configura o escritor no banco de dados usando JDBC

#### 6. **UserItemProcessor**
Implementa o processamento e filtragem de registros:
- Filtra apenas usuários com **ID par**
- Descarta automaticamente registros com ID ímpar
- Retorna `null` para descartar items

## 🚀 Como Executar

### Pré-requisitos
- Java 25 instalado
- Maven instalado
- MySQL Server rodando

### Passos

1. **Criar a pasta de CSV:**
```bash
mkdir -p /home/victorhugolgr/git/lab-spring-batch/data/csv
```

2. **Copiar arquivos CSV:**
```bash
cp /home/victorhugolgr/git/lab-spring-batch/lab/lab/src/main/resources/users.csv \
   /home/victorhugolgr/git/lab-spring-batch/data/csv/
```

3. **Compilar o projeto:**
```bash
cd /home/victorhugolgr/git/lab-spring-batch/lab/lab
mvn clean install
```

4. **Executar a aplicação:**
```bash
mvn spring-boot:run
```

Ou:
```bash
java -jar target/lab-0.0.1-SNAPSHOT.jar
```

### Verificar Execução

Após a execução, consulte o banco:
```sql
-- Ver quanto foi lido
SELECT COUNT(*) FROM batch_step_execution WHERE step_name = 'csv-to-db-step';

-- Ver registros salvos (apenas pares)
SELECT COUNT(*) FROM users;
SELECT * FROM users LIMIT 5;
```

## 📊 Fluxo de Execução

```
Propriedades do Banco (TABLE: properties)
    ↓
PATH_CSV = "/home/.../data/csv"
    ↓
Listar arquivos CSV da pasta ✨
    ↓
Para cada arquivo CSV:
  ├── MultiResourceItemReader (Lê arquivo) ✨
  ├── UserFieldSetMapper (Mapeia para record) ✨
  ├── UserItemProcessor (Filtra IDs pares)
  ├── Chunk Processing (10 por chunk)
  └── JdbcBatchItemWriter (Escreve em batch)
    ↓
MySQL Database (users - apenas pares)
```

**Exemplo de resultado com 3 arquivos de 1000 registros cada:**
- **Registros lidos:** 3000
- **Registros processados:** 1500 (apenas IDs pares)
- **Registros salvos:** 1500

## 🔍 Características do Spring Batch

- ✅ **MultiResourceItemReader**: Processa múltiplos arquivos em sequência ✨
- ✅ **Properties Parametrizadas**: Configurações no banco de dados ✨
- ✅ **Record Mapping**: Suporte a Java Records com FieldSetMapper customizado ✨
- ✅ **Processamento em Chunks**: Os dados são processados em lotes de 10 registros
- ✅ **Filtragem com ItemProcessor**: Implementa lógica de negócio e filtra registros
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

### Monitorar Resultados da Filtragem

Você pode verificar o número de registros processados e salvos:

```sql
-- Total de registros na tabela users (apenas pares)
SELECT COUNT(*) FROM users;

-- Verificar alguns registros salvos
SELECT * FROM users LIMIT 10;

-- Verificar que todos os IDs são pares
SELECT id, name, email FROM users WHERE id % 2 != 0;
-- Resultado: nenhum registro
```

## 🔧 Personalizando o ItemProcessor

Para implementar diferentes filtros, edite a classe `UserItemProcessor.java`:

```java
// Exemplo 1: Filtrar apenas IDs maiores que 500
public User process(User user) throws Exception {
    if (user.getId() > 500) {
        return user;
    }
    return null;
}

// Exemplo 2: Filtrar por padrão de email
public User process(User user) throws Exception {
    if (user.getEmail().contains("@example.com")) {
        return user;
    }
    return null;
}

// Exemplo 3: Múltiplos critérios
public User process(User user) throws Exception {
    if (user.getId() % 2 == 0 && user.getId() > 100) {
        return user;
    }
    return null;
}
```

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
