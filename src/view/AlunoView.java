package view;

import model.Aluno;
import model.Curso;
import model.Disciplina;

import java.util.List;

public class AlunoView {

    public static void Message(String message) {
        System.out.println(message);
    }

    public static void Create(Aluno aluno, Curso curso) {
        System.out.println();
        System.out.println("--- CADASTRAR ALUNO ---");
        int matricula = Teclado.lerInt("Informe a matricula: ");
        String nome = Teclado.lerTexto("Informe o nome: ");
        int idade = Teclado.lerInt("Informe a idade: ");

        aluno.setMatricula(matricula);
        aluno.setNome(nome);
        aluno.setIdade(idade);
        aluno.setCurso(curso);
    }

    public static int Read() {
        return Teclado.lerInt("Informe o numero da matricula: ");
    }

    // TELA PRINCIPAL: mostra aluno, curso e disciplinas navegando pela
    // COMPOSICAO -> aluno.getCurso().getDisciplinas().
    public static void Show(Aluno aluno) {
        System.out.println();
        System.out.println("--- DADOS DO ALUNO ---");
        System.out.println("Matricula: " + aluno.getMatricula());
        System.out.println("Nome.....: " + aluno.getNome());
        System.out.println("Idade....: " + aluno.getIdade() + " anos");

        Curso curso = aluno.getCurso();
        if (curso == null) {
            System.out.println("Curso....: (nao encontrado)");
            return;
        }

        System.out.println();
        System.out.println("CURSO DO ALUNO");
        System.out.println("   Codigo: " + curso.getCodigo());
        System.out.println("   Nome..: " + curso.getNome());
        System.out.println("   Turno.: " + curso.getTurno());

        List<Disciplina> disciplinas = curso.getDisciplinas();
        System.out.println();
        System.out.println("DISCIPLINAS DO CURSO (" + disciplinas.size() + ")");
        if (disciplinas.isEmpty()) {
            System.out.println("   (nenhuma disciplina cadastrada)");
        } else {
            for (Disciplina disciplina : disciplinas) {
                System.out.println("   - " + disciplina.getNome()
                        + " (codigo " + disciplina.getCodigo()
                        + ", " + disciplina.getCargaHoraria() + "h)");
            }
        }
    }

    public static void ShowList(List<Aluno> alunos) {
        System.out.println();
        System.out.println("--- ALUNOS CADASTRADOS (" + alunos.size() + ") ---");
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        for (Aluno aluno : alunos) {
            System.out.println(aluno + " | Curso: " + aluno.getCurso().getNome());
        }
    }

    public static void Update(Aluno aluno, Curso curso) {
        System.out.println();
        System.out.println("--- ATUALIZAR ALUNO ---");
        System.out.println("Matricula: " + aluno.getMatricula() + " (identificador, nao pode ser alterado)");
        String nome = Teclado.lerTexto("Novo nome (" + aluno.getNome() + "): ");
        int idade = Teclado.lerInt("Nova idade (" + aluno.getIdade() + "): ");

        aluno.setNome(nome);
        aluno.setIdade(idade);
        aluno.setCurso(curso);
    }
}
