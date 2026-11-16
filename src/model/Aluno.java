package model;

public class Aluno {

    // ENCAPSULAMENTO: atributos private, acessados so por getters/setters.
    private int matricula;
    private String nome;
    private int idade;

    // COMPOSICAO: Aluno TEM UM Curso (o objeto inteiro, nao so o codigo).
    private Curso curso;

    public Aluno() {
    }

    public Aluno(int matricula, String nome, int idade, Curso curso) {
        this.matricula = matricula;
        this.nome = nome;
        this.idade = idade;
        this.curso = curso;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "Matricula: " + matricula + " | " + nome + " | " + idade + " anos";
    }
}
