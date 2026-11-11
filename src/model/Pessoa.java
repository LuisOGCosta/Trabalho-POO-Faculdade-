package model;

// CLASSE ABSTRATA: nao existe "pessoa solta" no sistema, so Aluno.
public abstract class Pessoa {

    // ENCAPSULAMENTO: atributos private, acessados so por getters/setters.
    private String nome;
    private int idade;

    public Pessoa() {
    }

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    // METODO ABSTRATO: obriga toda classe filha a dizer como se identifica.
    public abstract String getIdentificacao();

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
}
