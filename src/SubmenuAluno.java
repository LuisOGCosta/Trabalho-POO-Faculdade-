import controller.AlunoController;
import view.Teclado;

public class SubmenuAluno {

    public static void Show() {
        int opcao;

        do {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("  SUBMENU - ALUNOS     ");
            System.out.println("-----------------------");
            System.out.println("1 - Cadastrar ALUNO");
            System.out.println("2 - Consultar ALUNO");
            System.out.println("3 - Remover ALUNO");
            System.out.println("4 - Atualizar ALUNO");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("-----------------------");

            opcao = Teclado.lerInt("Opcao: ");

            switch (opcao) {
                case 1:
                    AlunoController.Create();
                    break;
                case 2:
                    AlunoController.Read();
                    break;
                case 3:
                    AlunoController.Delete();
                    break;
                case 4:
                    AlunoController.Update();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opcao invalida! Escolha entre 1 e 5.");
            }

        } while (opcao != 5);
    }
}
