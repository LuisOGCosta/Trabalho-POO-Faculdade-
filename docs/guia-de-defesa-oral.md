# Guia de Defesa Oral — Trabalho de POO

> O item (j) do enunciado diz: *"Você deverá apresentar seu sistema e ser capaz de explicar trechos de códigos, princípios de desenvolvimento e padrões utilizados. Questionamentos sobre estratégias de POO serão realizadas durante a apresentação."*
>
> Este documento tem a resposta curta de cada pergunta provável **e o arquivo/linha que prova**. Se você souber só as frases em **negrito**, já se defende.

---

## Mapa rápido: conceito → onde está no código

| Conceito | Arquivo | Linha |
|---|---|---|
| Classe abstrata | `src/model/Pessoa.java` | 4 |
| Método abstrato | `src/model/Pessoa.java` | 19 |
| Herança (`extends`) | `src/model/Aluno.java` | 4 |
| `super(...)` | `src/model/Aluno.java` | 16 |
| Polimorfismo (sobrescrita) | `src/model/Aluno.java` | 23 e 44 |
| Encapsulamento | `src/model/Pessoa.java` | 7-8 |
| Composição (Aluno→Curso) | `src/model/Aluno.java` | 9 |
| Composição + Lista (Curso→Disciplinas) | `src/model/Curso.java` | 13 |
| Singleton | `src/database/DatabaseConnection.java` | 10, 17, 21 |
| DAO + PreparedStatement | `src/dao/AlunoDAO.java` | 38 |
| Listas vindas do banco | `src/dao/AlunoDAO.java` | 74 |
| Cadeia Aluno→Curso→Disciplinas | `src/dao/AlunoDAO.java` | 59 |
| Tela que prova o item (c.ii) | `src/view/AlunoView.java` | 53 |
| Regra "curso tem ≥1 disciplina" | `src/controller/CursoController.java` | 42 |
| Regra "não remover curso com alunos" | `src/controller/CursoController.java` | 102 |
| Só sai no SAIR (item f) | `src/Menu.java` | 39 |
| Criação das tabelas | `src/Main.java` | 13-15 |

---

## Os 4 pilares da POO neste trabalho

### 1. "Onde está o ENCAPSULAMENTO?"

**Todos os atributos das classes de modelo são `private`, e o acesso é feito só por getters e setters.**

> "Em `Pessoa.java`, linha 7, `nome` é `private`. Nenhuma outra classe consegue fazer `pessoa.nome = ...`. Quem quiser mudar passa pelo `setNome()`. Isso protege o objeto: se amanhã eu precisar validar que o nome não pode ser vazio, eu coloco a validação dentro do setter e o sistema inteiro passa a ser validado, sem mudar mais nada."

### 2. "Onde está a HERANÇA?"

**`Aluno extends Pessoa`** (`src/model/Aluno.java`, linha 4).

> "Criei a classe `Pessoa` com o que qualquer pessoa da faculdade tem: nome e idade. O `Aluno` herda isso e acrescenta o que é só dele: matrícula e curso. No construtor, linha 16, eu chamo `super(nome, idade)` — quem sabe guardar nome e idade é a superclasse, eu não repito esse código."

**Se perguntarem "e qual a vantagem?":**
> "Se amanhã a faculdade pedir para cadastrar Professor, eu crio `Professor extends Pessoa` e já ganho nome e idade prontos."

### 3. "Onde está o POLIMORFISMO?"

Tem **dois tipos** e os dois estão no trabalho:

**Polimorfismo de sobrescrita (tempo de execução)** — `Aluno.java`, linhas 23 e 44:
> "`Pessoa` declara o método abstrato `getIdentificacao()` sem corpo. O `Aluno` é obrigado a implementar e devolve a matrícula. Um Professor devolveria outro código. É o mesmo nome de método com comportamentos diferentes dependendo do objeto. Também sobrescrevi `toString()`, que é um método da classe `Object`."

**Polimorfismo de sobrecarga (tempo de compilação)** — todas as classes de modelo:
> "Cada classe tem dois construtores com o mesmo nome e parâmetros diferentes: um vazio e um completo. O compilador escolhe qual usar pela assinatura."

### 4. "Onde está a ABSTRAÇÃO?"

Duas respostas, pode usar qualquer uma:

> "Na classe `Pessoa`, que é `abstract`: ela define o que toda pessoa tem, mas não pode ser instanciada — não existe 'pessoa solta' no sistema, só aluno."

> "E na divisão em camadas: quando o Controller chama `AlunoDAO.Create(aluno)`, ele não sabe nem quer saber se por trás tem MySQL, arquivo ou lista. Ele só conhece o que o DAO faz, não como faz."

---

## Perguntas sobre os padrões de projeto

### "Que padrões de projeto você usou?"

**Três: MVC, DAO e Singleton.**

### "Explique o MVC."

> "Separei o sistema em três responsabilidades:
> - **Model** (`model/`) são os objetos do negócio: Aluno, Curso, Disciplina.
> - **View** (`view/`) é a única que imprime na tela e lê o teclado.
> - **Controller** (`controller/`) é quem decide: recebe o comando do submenu, pede os dados pra View, aplica a regra e manda o DAO salvar.
>
> A prova é que **não existe um único `System.out.println` dentro dos Controllers, e nem uma linha de SQL.** Quando o Controller quer falar com o usuário, ele chama `AlunoView.Message(...)`."

### "Por que usar o padrão DAO? Não dava pra colocar o SQL direto no Controller?"

> "Dava, mas o sistema ficaria amarrado ao MySQL. **O DAO é a única camada que conhece SQL** — nenhuma outra classe do projeto importa `java.sql`. Se eu precisar trocar o banco, ou voltar a salvar em lista na memória como na primeira versão, eu mexo só nos três arquivos da pasta `dao/` e o resto do sistema nem fica sabendo."

### "O que é Singleton e por que você usou?"

> "Singleton é um padrão que garante que exista **uma única instância** de uma classe durante toda a execução. Em `DatabaseConnection.java` eu fiz três coisas: o construtor é `private` (linha 17), então ninguém consegue dar `new`; a conexão é um atributo `static` (linha 10); e o `getConnection()` só cria a conexão se ela ainda não existir (linha 21) — nas chamadas seguintes devolve a mesma.
>
> Usei porque abrir conexão com banco é caro. Se cada método do DAO abrisse a sua, o sistema ficaria lento e poderia estourar o limite de conexões do MySQL."

---

## As perguntas difíceis

### "Por que você usou LISTAS se já tem banco de dados?" ⚠️

Essa é a pergunta mais provável, porque os itens (g) e (i) do enunciado parecem se contradizer. **Use as duas justificativas:**

> "As listas estão em dois lugares, e os dois são necessários:
>
> **1.** Todo método de consulta do DAO devolve uma `List`. Por exemplo, `AlunoDAO.ReadAll()` na linha 74 faz a consulta no MySQL e transforma cada linha do `ResultSet` em um objeto `Aluno` dentro de uma `ArrayList`. **O banco guarda linhas e colunas; o programa Java trabalha com objetos dentro de listas.** A lista é a ponte entre os dois.
>
> **2.** A classe `Curso` tem o atributo `private List<Disciplina> disciplinas` (linha 13). É a lista que representa a relação 'um curso tem N disciplinas' dentro do objeto."

### "O que acontece se você tentar remover um curso que tem aluno?" ⚠️

> "O sistema bloqueia. Em `CursoController.java`, linha 102, antes de remover eu chamo `AlunoDAO.CountByCurso()`. Se tiver aluno, mostro uma mensagem e não removo — porque o item (c) do enunciado exige que todo aluno tenha um curso, então eu deixaria um aluno órfão.
>
> **Já as disciplinas são removidas junto**, na linha 110. Isso é proposital: a relação entre curso e disciplina é de **composição** — a disciplina só existe por causa do curso, sozinha ela não faz sentido. Além disso, o banco também me protege: existe uma `FOREIGN KEY` nas duas tabelas."

### "Qual a diferença entre herança e composição? Por que você usou cada uma?"

> "Herança é uma relação **'é um'**: o Aluno **é uma** Pessoa. Composição é uma relação **'tem um'**: o Aluno **tem um** Curso.
>
> Eu não poderia fazer `Aluno extends Curso`, porque aluno não é um curso — ele tem um. Por isso o curso entrou como atributo (`Aluno.java`, linha 9), e a Pessoa entrou como superclasse."

### "Por que `PreparedStatement` e não montar a String do SQL?"

> "Por dois motivos. O primeiro é **segurança**: concatenar String abre brecha para SQL Injection — se alguém digitasse um nome com aspas e um comando dentro, ele seria executado. Com `?`, o driver trata o valor como dado, nunca como comando. O segundo é que o `PreparedStatement` já resolve a conversão de tipos e o escape de caracteres pra mim."

### "Por que os métodos são `static`?"

Responda com honestidade — é o estilo do material da disciplina:

> "Segui o mesmo padrão do exemplo trabalhado em aula. Como as classes de DAO, Controller e View não guardam estado próprio — elas só executam uma operação e devolvem o resultado — não é necessário criar objeto delas. O estado do sistema fica nos objetos de modelo e no banco de dados.
>
> Se o sistema crescesse, eu transformaria os DAOs em classes instanciáveis implementando uma interface comum, para poder trocar a implementação sem mexer no Controller."

### "Como você garante que todo curso tem pelo menos 1 disciplina?" (item d)

> "Em `CursoController.java`, logo depois de salvar o curso, eu entro em um laço que obriga o cadastro da primeira disciplina. A condição está na linha 42: `while (cadastradas == 0 || DisciplinaView.PerguntarOutra())`. Enquanto nenhuma disciplina foi cadastrada, ele nem pergunta se quer continuar — ele repete. Só depois da primeira é que aparece 'deseja cadastrar outra?'.
>
> E para não furar a regra por outro caminho, o `DisciplinaController` (linha 102) também impede remover a **última** disciplina de um curso."

### "Como o sistema mostra o curso e as disciplinas do aluno?" (item c.ii)

Essa é a melhor hora para mostrar a tela rodando:

> "Tudo sai de **um único objeto**, navegando pela composição. Em `AlunoDAO.Read()`, linha 59, depois de montar o aluno eu chamo `CursoDAO.Read()`, que por sua vez já carrega as disciplinas daquele curso. Quando o objeto chega na View, basta fazer `aluno.getCurso().getDisciplinas()` — está em `AlunoView.java`, linha 53."

### "Por que o programa só sai no SAIR?" (item f)

> "Os menus usam `do/while`. Em `Menu.java`, linha 39, a condição é `while (opcao != 4)`. O laço se repete indefinidamente e só encerra na opção 4. Os submenus usam a mesma lógica com a opção 5, mas ela só volta para o menu principal, não encerra o programa."

### "Para que serve essa classe `Teclado`?"

> "Ela centraliza toda a leitura do teclado, aplicando o princípio **DRY** — não repetir código. Resolve três problemas de uma vez:
>
> **1.** Tem **um único `Scanner`** para todo o sistema (linha 9). Criar um `Scanner` novo dentro de cada método faz vários objetos disputarem a mesma entrada e o programa começa a pular leituras.
>
> **2.** Eu uso **só `nextLine()`**. O `nextInt()` lê o número mas deixa o ENTER no buffer, e a próxima leitura de texto vem vazia. Lendo a linha inteira e convertendo com `Integer.parseInt()` (linha 19), esse problema não existe — e o nome do aluno pode ter espaço.
>
> **3.** Se o usuário digitar letra onde era número, o sistema avisa e pede de novo, em vez de encerrar com `InputMismatchException`."

---

## Perguntas sobre o banco de dados

> O passo a passo completo de como o banco foi montado está em [`banco-de-dados.md`](banco-de-dados.md), e a estrutura em SQL está em [`schema.sql`](schema.sql).

### "Como você montou o banco de dados?" ⚠️

Comece pela separação em duas partes — é o que o professor quer ouvir:

> "Foi em duas partes. **O banco** em si, o schema `faculdade`, eu criei à mão, um comando só, uma vez. **As tabelas** são criadas pelo próprio programa: cada DAO tem um método `CreateTable()` com `CREATE TABLE IF NOT EXISTS`, e o `Main` chama os três quando o sistema inicia."

### "Por que o banco é criado à mão e as tabelas não?"

> "Por causa da string de conexão. Ela é `jdbc:mysql://localhost/faculdade` — já aponta para dentro do schema. Então o banco precisa existir antes de a conexão abrir; se não existir, o `DriverManager` falha e o programa nem chega a rodar uma linha de SQL. É o problema do ovo e da galinha.
>
> Daria para resolver conectando primeiro sem schema, rodando o `CREATE DATABASE` e reconectando — mas mantive o formato do exemplo visto em aula."

### "Por que essa ordem de criação das tabelas?" ⚠️

> "`cursos` primeiro, depois `disciplinas` e `alunos`. As duas últimas têm `FOREIGN KEY` apontando para `cursos` — se `cursos` não existisse ainda, o MySQL recusaria a criação.
>
> No `DELETE` é o contrário: as filhas saem primeiro, porque não dá para apagar um curso que ainda tem registros apontando para ele."

### "Qual a relação entre as tabelas?"

> "Um para muitos nos dois casos: um curso tem muitas disciplinas e muitos alunos. As duas tabelas filhas guardam `codigo_curso` como `FOREIGN KEY`."

### "Para que servem as chaves estrangeiras?"

> "Elas fazem o **banco** garantir a regra, não só o Java. Mesmo que alguém altere o programa e tente cadastrar um aluno no curso 999, que não existe, o MySQL recusa. É uma segunda camada de proteção além da validação que já faço no Controller."

### "Por que o código do curso é a chave primária e não um ID auto-incremento?"

> "Porque o código do curso é um dado do negócio — a faculdade já identifica o curso por ele, e o enunciado pede 'código do curso' como atributo. O mesmo vale para a matrícula do aluno, que é o identificador natural dele."

### "Por que `CreateTable()` usa `Statement` e o resto usa `PreparedStatement`?"

> "Porque no `CreateTable()` o SQL é fixo, escrito por mim, sem nenhum valor vindo do usuário — não há o que injetar. O `PreparedStatement` com `?` é usado em todos os métodos que recebem dados digitados: INSERT, SELECT, UPDATE e DELETE."

### Se ele pedir para ver o banco por fora

> "O MySQL guardou as tabelas como **InnoDB**, que é a única engine que respeita chave estrangeira de verdade — em MyISAM o `FOREIGN KEY` é aceito na sintaxe mas ignorado. E ele criou **sozinho** um índice em cima de cada chave estrangeira, porque precisa consultar essa coluna rápido toda vez que valida a relação."

Comando para mostrar a relação funcionando:

```bash
/opt/homebrew/opt/mysql/bin/mysql -u root faculdade -e "SELECT a.matricula, a.nome AS aluno, c.nome AS curso FROM alunos a JOIN cursos c ON a.codigo_curso = c.codigo;"
```

### "Por que `Optional` nos métodos de consulta?"

> "Porque a busca pode não encontrar nada. O `Optional` deixa isso explícito na assinatura do método e obriga quem chama a tratar o caso, em vez de devolver `null` e arriscar um `NullPointerException`. No Controller eu uso `isPresent()` ou `isEmpty()` antes de usar o objeto."

---

## Se o professor pedir para mudar algo na hora

| Pedido | O que abrir |
|---|---|
| "Adicione um campo e-mail no aluno" | `model/Aluno.java` (atributo + getter/setter), `view/AlunoView.java` (ler e mostrar), `dao/AlunoDAO.java` (INSERT, SELECT, UPDATE e o CREATE TABLE) |
| "Mostre quantas horas tem o curso inteiro" | `view/CursoView.java` — somar `getCargaHoraria()` percorrendo a lista de disciplinas |
| "Crie uma classe Professor" | `model/Professor.java` com `extends Pessoa` e implementando `getIdentificacao()` |
| "Liste os alunos de um curso" | `dao/AlunoDAO.java` — já existe `CountByCurso`, bastaria um `ReadByCurso` no mesmo molde |

---

## Honestidade

Se o professor apontar algo que você não sabe, a melhor resposta é:

> "Não implementei isso, mas o lugar onde eu mexeria seria [camada]. Eu organizei o sistema justamente para que essa mudança ficasse em um arquivo só."

Isso mostra que você entende a arquitetura, que é exatamente o que o item (j) está avaliando.
