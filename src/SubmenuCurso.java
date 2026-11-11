import controller.CursoController;
import view.Teclado;

public class SubmenuCurso {

    public static void Show() {
        int opcao;

        do {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("  SUBMENU - CURSOS     ");
            System.out.println("-----------------------");
            System.out.println("1 - Cadastrar CURSO");
            System.out.println("2 - Consultar CURSO");
            System.out.println("3 - Remover CURSO");
            System.out.println("4 - Atualizar CURSO");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("-----------------------");

            opcao = Teclado.lerInt("Opcao: ");

            switch (opcao) {
                case 1:
                    CursoController.Create();
                    break;
                case 2:
                    CursoController.Read();
                    break;
                case 3:
                    CursoController.Delete();
                    break;
                case 4:
                    CursoController.Update();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opcao invalida! Escolha entre 1 e 5.");
            }

        } while (opcao != 5);
    }
}
