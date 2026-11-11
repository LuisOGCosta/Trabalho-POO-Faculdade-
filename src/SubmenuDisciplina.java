import controller.DisciplinaController;
import view.Teclado;

public class SubmenuDisciplina {

    public static void Show() {
        int opcao;

        do {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println(" SUBMENU - DISCIPLINAS ");
            System.out.println("-----------------------");
            System.out.println("1 - Cadastrar DISCIPLINA");
            System.out.println("2 - Consultar DISCIPLINA");
            System.out.println("3 - Remover DISCIPLINA");
            System.out.println("4 - Atualizar DISCIPLINA");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("-----------------------");

            opcao = Teclado.lerInt("Opcao: ");

            switch (opcao) {
                case 1:
                    DisciplinaController.Create();
                    break;
                case 2:
                    DisciplinaController.Read();
                    break;
                case 3:
                    DisciplinaController.Delete();
                    break;
                case 4:
                    DisciplinaController.Update();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opcao invalida! Escolha entre 1 e 5.");
            }

        } while (opcao != 5);
    }
}
