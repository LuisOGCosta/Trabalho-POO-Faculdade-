import view.Teclado;

public class Menu {

    public static void Show() {
        int opcao;

        // do/while: o menu se repete e o programa SO encerra na opcao 4 (SAIR).
        do {
            System.out.println();
            System.out.println("=======================");
            System.out.println("         MENU          ");
            System.out.println("=======================");
            System.out.println("1 - Gerenciar ALUNOS");
            System.out.println("2 - Gerenciar DISCIPLINAS");
            System.out.println("3 - Gerenciar CURSOS");
            System.out.println("4 - SAIR");
            System.out.println("=======================");

            opcao = Teclado.lerInt("Opcao: ");

            switch (opcao) {
                case 1:
                    SubmenuAluno.Show();
                    break;
                case 2:
                    SubmenuDisciplina.Show();
                    break;
                case 3:
                    SubmenuCurso.Show();
                    break;
                case 4:
                    System.out.println("Encerrando o sistema. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida! Escolha entre 1 e 4.");
            }

        } while (opcao != 4);
    }
}
