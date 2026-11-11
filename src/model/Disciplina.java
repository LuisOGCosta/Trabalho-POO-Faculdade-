package model;

public class Disciplina {

    private int codigo;
    private String nome;
    private int cargaHoraria;

    private int codigoCurso;

    public Disciplina() {
    }

    public Disciplina(int codigo, String nome, int cargaHoraria, int codigoCurso) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.codigoCurso = codigoCurso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(int codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | " + nome + " | Carga horaria: " + cargaHoraria + "h";
    }
}
