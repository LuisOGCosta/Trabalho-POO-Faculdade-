package model;

// HERANCA: Aluno E UMA Pessoa -> herda nome e idade.
public class Aluno extends Pessoa {

    private int matricula;

    // COMPOSICAO: Aluno TEM UM Curso (o objeto inteiro, nao so o codigo).
    private Curso curso;

    public Aluno() {
        super();
    }

    public Aluno(int matricula, String nome, int idade, Curso curso) {
        super(nome, idade);
        this.matricula = matricula;
        this.curso = curso;
    }

    // POLIMORFISMO: implementacao obrigatoria do metodo abstrato de Pessoa.
    @Override
    public String getIdentificacao() {
        return "Matricula: " + matricula;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return getIdentificacao() + " | " + getNome() + " | " + getIdade() + " anos";
    }
}
