# Atividade 1 — Diagrama de Classes

> **Para imprimir/entregar:** `diagrama-de-classes.svg` e `diagrama-de-camadas.svg` (nesta pasta),
> ou a versão em Word em `word/1 - Diagrama de Classes.docx`, que já traz as duas imagens.
> O código Mermaid abaixo é a versão editável, caso você queira ajustar algo.

---

## 1. Modelo de domínio (as classes do negócio)

```mermaid
classDiagram
    class Pessoa {
        <<abstract>>
        -String nome
        -int idade
        +Pessoa()
        +Pessoa(String nome, int idade)
        +getIdentificacao() String
        +getNome() String
        +setNome(String nome) void
        +getIdade() int
        +setIdade(int idade) void
    }

    class Aluno {
        -int matricula
        -Curso curso
        +Aluno()
        +Aluno(int matricula, String nome, int idade, Curso curso)
        +getIdentificacao() String
        +getMatricula() int
        +setMatricula(int matricula) void
        +getCurso() Curso
        +setCurso(Curso curso) void
        +toString() String
    }

    class Curso {
        -int codigo
        -String nome
        -String turno
        -List~Disciplina~ disciplinas
        +Curso()
        +Curso(int codigo, String nome, String turno)
        +getCodigo() int
        +setCodigo(int codigo) void
        +getNome() String
        +setNome(String nome) void
        +getTurno() String
        +setTurno(String turno) void
        +getDisciplinas() List~Disciplina~
        +setDisciplinas(List~Disciplina~ disciplinas) void
        +adicionarDisciplina(Disciplina disciplina) void
        +toString() String
    }

    class Disciplina {
        -int codigo
        -String nome
        -int cargaHoraria
        -int codigoCurso
        +Disciplina()
        +Disciplina(int codigo, String nome, int cargaHoraria, int codigoCurso)
        +getCodigo() int
        +setCodigo(int codigo) void
        +getNome() String
        +setNome(String nome) void
        +getCargaHoraria() int
        +setCargaHoraria(int cargaHoraria) void
        +getCodigoCurso() int
        +setCodigoCurso(int codigoCurso) void
        +toString() String
    }

    Pessoa <|-- Aluno : herança (extends)
    Aluno "1" --> "1" Curso : possui
    Curso "1" *-- "1..*" Disciplina : composição
```

### Como ler os três relacionamentos

| Notação | Relacionamento | O que significa no trabalho |
|---|---|---|
| Triângulo vazado `<\|--` | **Herança** | `Aluno extends Pessoa`. O aluno herda `nome` e `idade`. |
| Seta simples `-->` | **Associação** | Todo aluno possui exatamente 1 curso (item c do enunciado). |
| Losango preenchido `*--` | **Composição** | O curso é dono das disciplinas: tem de 1 a N, e se o curso é removido elas vão junto (item d). |

---

## 2. Arquitetura em camadas (como as classes se organizam)

```mermaid
flowchart TD
    Main[Main] --> Menu[Menu]
    Menu --> SA[SubmenuAluno]
    Menu --> SD[SubmenuDisciplina]
    Menu --> SC[SubmenuCurso]

    SA --> AC[AlunoController]
    SD --> DC[DisciplinaController]
    SC --> CC[CursoController]

    AC --> AV[AlunoView]
    DC --> DV[DisciplinaView]
    CC --> CV[CursoView]
    AV --> T[Teclado]
    DV --> T
    CV --> T

    AC --> ADAO[AlunoDAO]
    DC --> DDAO[DisciplinaDAO]
    CC --> CDAO[CursoDAO]

    ADAO --> DB[(DatabaseConnection<br/>MySQL)]
    DDAO --> DB
    CDAO --> DB

    AC -.usa.-> MAluno[model.Aluno]
    CC -.usa.-> MCurso[model.Curso]
    DC -.usa.-> MDisc[model.Disciplina]
```

**Regra que vale para todo o sistema:**

- **Menu/Submenu** só encaminham a opção escolhida. Não sabem o que é banco de dados.
- **Controller** aplica a regra de negócio. Não tem `System.out.println` nem SQL.
- **View** só fala com o usuário (imprime e lê teclado). Não acessa banco.
- **DAO** é a única camada que conhece SQL.
- **Model** são os objetos do negócio, usados por todas as camadas.

---

## 3. Modelo do banco de dados

```mermaid
erDiagram
    CURSOS ||--o{ DISCIPLINAS : "possui"
    CURSOS ||--o{ ALUNOS : "matricula"

    CURSOS {
        int codigo PK
        varchar nome
        varchar turno
    }
    DISCIPLINAS {
        int codigo PK
        varchar nome
        int carga_horaria
        int codigo_curso FK
    }
    ALUNOS {
        int matricula PK
        varchar nome
        int idade
        int codigo_curso FK
    }
```

As chaves estrangeiras `codigo_curso` são o que o banco usa para garantir, por conta própria, que nenhum aluno e nenhuma disciplina fiquem sem curso.
