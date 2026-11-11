# Como o banco de dados foi montado

> Documento de referência: o que foi feito à mão, o que o programa faz sozinho, e como reproduzir tudo em outro computador.
> As perguntas de prova sobre banco estão em [`guia-de-defesa-oral.md`](guia-de-defesa-oral.md).

---

## A regra que explica tudo

O banco foi montado em **duas partes separadas**, e saber distinguir as duas é o ponto central:

| | Quem fez | Quando |
|---|---|---|
| **O banco** (schema `faculdade`) | **à mão**, um comando no terminal | uma única vez |
| **As tabelas** (`cursos`, `disciplinas`, `alunos`) | **o próprio programa Java** | toda vez que o `Main` roda |

### Por que o banco é criado à mão?

Por causa da string de conexão em [`DatabaseConnection.java`](../src/database/DatabaseConnection.java):

```java
private static final String HOST = "jdbc:mysql://localhost/";
private static final String DATABASE = "faculdade";
...
connection = DriverManager.getConnection(HOST + DATABASE, DB_USER, DB_PASS);
```

A URL já aponta **para dentro** do schema `faculdade`. Então o banco precisa existir *antes* de a conexão abrir — se não existir, o `DriverManager` falha e o programa nem chega a executar uma linha de SQL. É o problema do ovo e da galinha.

> Daria para o programa criar o banco sozinho, conectando primeiro em `jdbc:mysql://localhost/` sem schema, rodando o `CREATE DATABASE` e depois reconectando. Mantive o formato do exemplo da aula, que conecta direto no schema.

---

## Passo a passo (para reproduzir em outra máquina)

### 1. Instalar o MySQL

```bash
brew install mysql
```

### 2. Subir o serviço

```bash
brew services start mysql
```

Confira que subiu — tem que responder `mysqld is alive`:

```bash
/opt/homebrew/opt/mysql/bin/mysqladmin ping
```

### 3. Criar o banco (o único passo manual)

```bash
/opt/homebrew/opt/mysql/bin/mysql -u root -e "CREATE DATABASE IF NOT EXISTS faculdade CHARACTER SET utf8mb4;"
```

### 4. Rodar o programa — as tabelas nascem sozinhas

```bash
cd "/Users/luisotavio/Faculdade Cotemig/POO - Trabalho" && ./compilar-e-rodar.sh
```

Pronto. Não existe passo 5: o `Main` cria as três tabelas na primeira execução.

> **Usuário e senha:** `root` sem senha. É o padrão do MySQL instalado pelo Homebrew e está em `DatabaseConnection.java`. Em um sistema real isso jamais ficaria no código — iria para variável de ambiente ou arquivo de configuração fora do versionamento.

---

## Como as tabelas são criadas

Cada DAO tem um método `CreateTable()`, e o [`Main`](../src/Main.java) chama os três ao iniciar:

```java
CursoDAO.CreateTable();
DisciplinaDAO.CreateTable();
AlunoDAO.CreateTable();
```

**A ordem não é aleatória.** `disciplinas` e `alunos` têm chave estrangeira apontando para `cursos`. Se `cursos` não existisse ainda, o MySQL recusaria a criação com erro de chave estrangeira.

O `IF NOT EXISTS` faz com que rodar o programa 10 vezes não cause erro nenhum: na primeira execução ele cria, nas seguintes ignora.

### O SQL que está no código

**`cursos`** — [`CursoDAO.java`](../src/dao/CursoDAO.java)
```sql
CREATE TABLE IF NOT EXISTS cursos (
    codigo INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    turno VARCHAR(20) NOT NULL
)
```

**`disciplinas`** — [`DisciplinaDAO.java`](../src/dao/DisciplinaDAO.java)
```sql
CREATE TABLE IF NOT EXISTS disciplinas (
    codigo INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL,
    codigo_curso INT NOT NULL,
    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
)
```

**`alunos`** — [`AlunoDAO.java`](../src/dao/AlunoDAO.java)
```sql
CREATE TABLE IF NOT EXISTS alunos (
    matricula INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    codigo_curso INT NOT NULL,
    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
)
```

### Por que `Statement` e não `PreparedStatement` aqui?

`CreateTable()` usa `Statement` comum. O SQL é **fixo**, escrito por mim, sem nenhum valor vindo do usuário — não há o que injetar. O `PreparedStatement` com `?` é usado em todos os outros métodos (INSERT, SELECT, UPDATE, DELETE), que são exatamente os que recebem dados digitados.

---

## O que o MySQL guardou de fato

Rodando `SHOW CREATE TABLE disciplinas`, aparecem coisas que eu não escrevi:

```sql
CREATE TABLE `disciplinas` (
  `codigo` int NOT NULL,
  `nome` varchar(100) NOT NULL,
  `carga_horaria` int NOT NULL,
  `codigo_curso` int NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `codigo_curso` (`codigo_curso`),
  CONSTRAINT `disciplinas_ibfk_1` FOREIGN KEY (`codigo_curso`) REFERENCES `cursos` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
```

Duas observações que valem ouro se o professor perguntar:

- **`ENGINE=InnoDB`** — é a engine padrão e a única que respeita chave estrangeira de verdade. Com MyISAM, o `FOREIGN KEY` é aceito na sintaxe mas **ignorado** na prática.
- **`KEY codigo_curso`** — o MySQL criou esse índice **sozinho**. Ele precisa consultar essa coluna rapidamente toda vez que valida a chave estrangeira, então cria o índice automaticamente.

---

## Comandos úteis

**Ver o conteúdo das tabelas:**
```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "SELECT * FROM cursos; SELECT * FROM disciplinas; SELECT * FROM alunos;"
```

**Provar a relação entre as tabelas (bom para mostrar na apresentação):**
```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "SELECT a.matricula, a.nome AS aluno, c.nome AS curso, c.turno FROM alunos a JOIN cursos c ON a.codigo_curso = c.codigo;"
```

**Zerar os dados sem apagar as tabelas:**
```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "DELETE FROM alunos; DELETE FROM disciplinas; DELETE FROM cursos;"
```

> A ordem do `DELETE` também importa, e pelo motivo inverso do `CREATE`: as tabelas filhas (`alunos`, `disciplinas`) saem primeiro, porque não dá para apagar um curso que ainda tem registros apontando para ele.

**Recomeçar do zero, apagando até as tabelas** (o programa recria na próxima execução):
```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "SET FOREIGN_KEY_CHECKS=0; DROP TABLE IF EXISTS alunos, disciplinas, cursos; SET FOREIGN_KEY_CHECKS=1;"
```

---

## Arquivo `schema.sql`

O arquivo [`schema.sql`](schema.sql) nesta pasta é a estrutura do banco exportada com `mysqldump`. **O sistema não precisa dele para funcionar** — as tabelas são criadas pelo Java. Ele serve para entregar junto com o trabalho e para mostrar a estrutura sem precisar abrir o MySQL.

Foi gerado com:

```bash
/opt/homebrew/opt/mysql/bin/mysqldump -u root --no-data --skip-comments faculdade > docs/schema.sql
```
