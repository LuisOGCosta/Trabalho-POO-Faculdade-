package view;

import model.Curso;
import model.Disciplina;

import java.util.List;

public class DisciplinaView {

    public static void Message(String message) {
        System.out.println(message);
    }

    public static void Create(Disciplina disciplina, Curso curso) {
        System.out.println();
        System.out.println("--- CADASTRAR DISCIPLINA ---");
        System.out.println("Curso: " + curso.getNome() + " (codigo " + curso.getCodigo() + ")");
        int codigo = Teclado.lerInt("Informe o codigo da disciplina: ");
        String nome = Teclado.lerTexto("Informe o nome da disciplina: ");
        int cargaHoraria = Teclado.lerInt("Informe a carga horaria (em horas): ");

        disciplina.setCodigo(codigo);
        disciplina.setNome(nome);
        disciplina.setCargaHoraria(cargaHoraria);
        disciplina.setCodigoCurso(curso.getCodigo());
    }

    public static int Read() {
        return Teclado.lerInt("Informe o codigo da disciplina: ");
    }

    public static void Show(Disciplina disciplina, Curso curso) {
        System.out.println();
        System.out.println("--- DADOS DA DISCIPLINA ---");
        System.out.println("Codigo........: " + disciplina.getCodigo());
        System.out.println("Nome..........: " + disciplina.getNome());
        System.out.println("Carga horaria.: " + disciplina.getCargaHoraria() + "h");
        if (curso != null) {
            System.out.println("Curso.........: " + curso.getNome()
                    + " (codigo " + curso.getCodigo() + ", turno " + curso.getTurno() + ")");
        }
    }

    public static void ShowList(List<Disciplina> disciplinas) {
        System.out.println();
        System.out.println("--- DISCIPLINAS CADASTRADAS (" + disciplinas.size() + ") ---");
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina cadastrada.");
            return;
        }
        for (Disciplina disciplina : disciplinas) {
            System.out.println(disciplina + " | Curso: " + disciplina.getCodigoCurso());
        }
    }

    public static void Update(Disciplina disciplina, Curso curso) {
        System.out.println();
        System.out.println("--- ATUALIZAR DISCIPLINA ---");
        System.out.println("Codigo: " + disciplina.getCodigo() + " (identificador, nao pode ser alterado)");
        String nome = Teclado.lerTexto("Novo nome (" + disciplina.getNome() + "): ");
        int cargaHoraria = Teclado.lerInt("Nova carga horaria (" + disciplina.getCargaHoraria() + "): ");

        disciplina.setNome(nome);
        disciplina.setCargaHoraria(cargaHoraria);
        disciplina.setCodigoCurso(curso.getCodigo());
    }

    public static boolean PerguntarOutra() {
        return Teclado.lerSimNao("Deseja cadastrar outra disciplina para este curso? (S/N): ");
    }
}
