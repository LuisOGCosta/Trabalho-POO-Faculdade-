# Sistema de Faculdade — Trabalho de POO

Trabalho de **Programação Orientada a Objetos** — Cotemig.
Sistema de console em Java que gerencia **Alunos, Cursos e Disciplinas**, com persistência em MySQL.

---

## Como rodar

### 1. Pré-requisitos

- Java 17 ou superior
- MySQL

### 2. Subir o banco

```bash
brew services start mysql
mysql -u root -e "CREATE DATABASE IF NOT EXISTS faculdade CHARACTER SET utf8mb4;"
```

As **tabelas não precisam ser criadas à mão** — o próprio programa cria ao iniciar, no método `CreateTable()` de cada DAO.

### 3. Executar

Pelo IntelliJ: abra a pasta e rode a configuração **Main**.

Pelo terminal:

```bash
./compilar-e-rodar.sh
```

> A conexão está em `src/database/DatabaseConnection.java` (`localhost`, usuário `root`, sem senha — padrão do MySQL instalado pelo Homebrew). Ajuste lá se o seu ambiente for diferente.

---

## Estrutura

```
src/
├── Main.java                     cria as tabelas e chama o Menu
├── Menu.java                     menu principal
├── SubmenuAluno/Disciplina/Curso.java
├── model/        Aluno, Curso, Disciplina
├── view/         AlunoView, CursoView, DisciplinaView, Teclado
├── controller/   AlunoController, CursoController, DisciplinaController
├── dao/          AlunoDAO, CursoDAO, DisciplinaDAO
├── database/     DatabaseConnection (Singleton)
└── libs/         mysql-connector-j-9.1.0.jar
```

**Separação de responsabilidades:** o Submenu só encaminha, o Controller decide, a View fala com o usuário e o DAO fala com o banco. Nenhum Controller tem `System.out.println`, e nenhuma classe fora de `dao/` importa `java.sql`.

---

## Conceitos de POO aplicados

| Conceito | Onde |
|---|---|
| Polimorfismo | `toString()` sobrescrito nos models |
| Encapsulamento | atributos `private` + getters/setters |
| Composição | `Aluno` tem um `Curso`; `Curso` tem `List<Disciplina>` |
| Padrão MVC | pacotes `model`, `view`, `controller` |
| Padrão DAO | `dao/` concentra todo o SQL |
| Padrão Singleton | `DatabaseConnection` |

---

## Modelo de dados

```
cursos      (codigo PK, nome, turno)
disciplinas (codigo PK, nome, carga_horaria, codigo_curso FK -> cursos)
alunos      (matricula PK, nome, idade, codigo_curso FK -> cursos)
```

Um curso tem N disciplinas e N alunos. As chaves estrangeiras garantem, no próprio banco, que nenhum aluno e nenhuma disciplina fiquem sem curso.
