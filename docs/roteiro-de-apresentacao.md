# Roteiro de Apresentação

---

## PARTE 1 — Antes de apresentar (faça 10 minutos antes)

### 1. Ligar o MySQL

```bash
brew services start mysql
```

### 2. Conferir que o banco responde

```bash
/opt/homebrew/opt/mysql/bin/mysqladmin ping
```

Tem que responder `mysqld is alive`. Se não responder, rode o comando do passo 1 de novo e espere uns 10 segundos.

### 3. Zerar o banco para a demonstração começar do zero

```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "DELETE FROM alunos; DELETE FROM disciplinas; DELETE FROM cursos;"
```

> Se o banco `faculdade` não existir (por exemplo, em outro computador), crie antes com:
> `/opt/homebrew/opt/mysql/bin/mysql -u root -e "CREATE DATABASE IF NOT EXISTS faculdade;"`
> As **tabelas** não precisam ser criadas à mão — o próprio programa cria ao iniciar.
> O passo a passo completo está em [`banco-de-dados.md`](banco-de-dados.md).

### 4. Abrir o projeto no IntelliJ e rodar uma vez

Abra a pasta `POO - Trabalho` no IntelliJ e rode a configuração **Main** (botão verde ▶).
Tem que aparecer:

```
Conectando ao banco de dados...
Conexao bem-sucedida!
```

Saia com a opção `4`. **Se chegou até aqui, está tudo pronto.**

### Plano B (se o IntelliJ der problema na hora)

```bash
cd "/Users/luisotavio/Faculdade Cotemig/POO - Trabalho" && ./compilar-e-rodar.sh
```

Compila e roda pelo terminal, sem depender do IntelliJ.

---

## PARTE 2 — A demonstração ao vivo

> **Siga esta ordem.** Ela foi montada para que cada tela prove um item do enunciado, terminando na tela mais forte do trabalho.

### Passo 1 — Mostrar o MENU principal (itens a, f)

Rode o programa. Com o menu na tela, diga:

> "Este é o menu principal pedido no item 01. Ele usa um `do/while` que só encerra quando eu escolher a opção 4. Qualquer outra opção me traz de volta pra cá."

### Passo 2 — Cadastrar um CURSO com disciplinas (itens d, e)

Escolha `3` (Gerenciar CURSOS) → `1` (Cadastrar CURSO) e digite:

| Campo | Valor |
|---|---|
| Código do curso | `101` |
| Nome do curso | `Sistemas de Informacao` |
| Turno | `Noite` |

O sistema vai pedir a disciplina automaticamente. Diga:

> "Repare que ele **me obrigou** a cadastrar uma disciplina. O item (d) do enunciado diz que um curso deve possuir pelo menos 1 disciplina, então eu coloquei essa regra no `CursoController`: enquanto não houver nenhuma disciplina, ele nem pergunta se quero continuar."

| Campo | Valor |
|---|---|
| Código da disciplina | `201` |
| Nome | `Programacao Orientada a Objetos` |
| Carga horária | `80` |

Responda `s` para cadastrar outra:

| Campo | Valor |
|---|---|
| Código da disciplina | `202` |
| Nome | `Banco de Dados` |
| Carga horária | `60` |

Responda `n`. Volte ao menu com `5`.

### Passo 3 — Cadastrar um ALUNO escolhendo o curso (item c.i)

`1` (Gerenciar ALUNOS) → `1` (Cadastrar ALUNO).

**Pare um segundo quando a lista de cursos aparecer** e diga:

> "O item (c.i) diz que ao cadastrar um aluno deve ser escolhido um dos cursos **existentes**. Por isso o sistema lista os cursos do banco e só aceita um código que esteja nessa lista."

| Campo | Valor |
|---|---|
| Código do curso | `101` |
| Matrícula | `2026001` |
| Nome | `Luis Otavio Martins` |
| Idade | `20` |

> 💡 Digite o nome **completo, com espaço**. Isso prova que a leitura de texto está correta.

### Passo 4 — ⭐ A TELA MAIS IMPORTANTE (item c.ii)

Ainda no submenu de alunos: `2` (Consultar ALUNO) → matrícula `2026001`.

Vai aparecer o aluno, o curso e as duas disciplinas. **Esta é a hora de explicar a composição:**

> "Esta tela atende o item (c.ii): mostrar o aluno com seu curso e todas as disciplinas. E tudo isso sai de **um único objeto**. O `AlunoDAO` monta o aluno, busca o curso dele, e o `CursoDAO` já traz as disciplinas daquele curso dentro de uma `List`. Na View eu só faço `aluno.getCurso().getDisciplinas()`. Isso é **composição**: o aluno tem um curso, e o curso tem uma lista de disciplinas."

Se ele quiser ver o código, abra `src/view/AlunoView.java` na linha 65.

### Passo 5 — Mostrar que atualiza de verdade

`4` (Atualizar ALUNO) → matrícula `2026001` → curso `101` → nome `Luis Otavio Martins Silva` → idade `21`.

Depois faça `2` (Consultar) de novo com a mesma matrícula.

> "Os dados mudaram porque o `UPDATE` foi executado no banco. Se eu fechar o programa agora e abrir de novo, eles continuam lá."

### Passo 6 — Mostrar as regras de negócio (opcional, se sobrar tempo)

Volte ao menu, `3` (Gerenciar CURSOS) → `3` (Remover CURSO) → `101`.

> "Ele bloqueou. Antes de remover, o Controller verifica se existe aluno matriculado — se eu removesse, o aluno ficaria sem curso, e o item (c) exige que todo aluno tenha um."

### Passo 7 — Sair pela opção correta (item f)

`5` para voltar e `4` para sair.

> "O programa só encerra aqui, na opção SAIR, como pede o item (f). E ele fecha a conexão com o banco antes de terminar."

---

## PARTE 3 — Se der tempo de mostrar o código

Abra nesta ordem, são 3 arquivos e 2 minutos:

**1. `src/model/Pessoa.java`** — herança e abstração
> "Classe abstrata com o que toda pessoa tem. O método `getIdentificacao()` é abstrato: obriga quem herdar a dizer como se identifica."

**2. `src/model/Aluno.java`** — herança, composição e polimorfismo
> "`extends Pessoa` na linha 10, `super(nome, idade)` na linha 25, e o `getIdentificacao()` implementado na linha 35. O atributo `curso` na linha 16 é a composição."

**3. `src/dao/AlunoDAO.java`** — DAO, PreparedStatement e listas
> "Esta é a única camada que conhece SQL. Uso `PreparedStatement` com `?` para evitar SQL Injection. E o `ReadAll()` na linha 88 transforma o resultado do banco em uma `List` de objetos."

---

## Colinha de emergência

Se travar em alguma pergunta, estas três frases salvam quase tudo:

1. **"Separei em camadas: o Menu encaminha, o Controller decide, a View fala com o usuário e o DAO fala com o banco."**
2. **"O Aluno é uma Pessoa — por isso herança. O Aluno tem um Curso — por isso composição."**
3. **"Se precisar trocar o banco de dados, eu mexo só na pasta `dao/`."**

O documento `guia-de-defesa-oral.md` tem a resposta completa de cada pergunta provável.
