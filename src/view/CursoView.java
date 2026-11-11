package view;

import model.Curso;
import model.Disciplina;

import java.util.List;

// CAMADA VIEW: so imprime na tela e le o teclado. Nao acessa banco de dados.
public class CursoView {

    public static void Message(String message) {
        System.out.println(message);
    }

    public static void Create(Curso curso) {
        System.out.println();
        System.out.println("--- CADASTRAR CURSO ---");
        int codigo = Teclado.lerInt("Informe o codigo do curso: ");
        String nome = Teclado.lerTexto("Informe o nome do curso: ");
        String turno = Teclado.lerTexto("Informe o turno (Manha/Tarde/Noite): ");

        curso.setCodigo(codigo);
        curso.setNome(nome);
        curso.setTurno(turno);
    }

    public static int Read() {
        return Teclado.lerInt("Informe o codigo do curso: ");
    }

    public static void Show(Curso curso) {
        System.out.println();
        System.out.println("--- DADOS DO CURSO ---");
        System.out.println("Codigo: " + curso.getCodigo());
        System.out.println("Nome..: " + curso.getNome());
        System.out.println("Turno.: " + curso.getTurno());

        List<Disciplina> disciplinas = curso.getDisciplinas();
        System.out.println("Disciplinas (" + disciplinas.size() + "):");
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

    public static void ShowList(List<Curso> cursos) {
        System.out.println();
        System.out.println("--- CURSOS CADASTRADOS (" + cursos.size() + ") ---");
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
            return;
        }
        for (Curso curso : cursos) {
            System.out.println(curso);
        }
    }

    public static void Update(Curso curso) {
        System.out.println();
        System.out.println("--- ATUALIZAR CURSO ---");
        System.out.println("Codigo: " + curso.getCodigo() + " (identificador, nao pode ser alterado)");
        String nome = Teclado.lerTexto("Novo nome (" + curso.getNome() + "): ");
        String turno = Teclado.lerTexto("Novo turno (" + curso.getTurno() + "): ");

        curso.setNome(nome);
        curso.setTurno(turno);
    }

    public static Curso Escolher(List<Curso> cursos, String mensagem) {
        System.out.println();
        System.out.println("Cursos disponiveis:");
        for (Curso curso : cursos) {
            System.out.println("   " + curso);
        }
        while (true) {
            int codigo = Teclado.lerInt(mensagem);
            for (Curso curso : cursos) {
                if (curso.getCodigo() == codigo) {
                    return curso;
                }
            }
            System.out.println("Curso inexistente! Escolha um dos codigos listados acima.");
        }
    }
}
