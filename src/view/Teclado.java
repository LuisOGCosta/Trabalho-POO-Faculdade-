package view;

import java.util.Scanner;

// Centraliza a leitura do teclado (DRY): UM UNICO Scanner para todo o sistema
// e so nextLine(), para o nome do aluno poder ter espaco.
public class Teclado {

    private static final Scanner scan = new Scanner(System.in);

    private Teclado() {
    }

    public static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = lerLinha();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido! Digite apenas numeros.");
            }
        }
    }

    public static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = lerLinha();
            if (!linha.isEmpty()) {
                return linha;
            }
            System.out.println("O campo nao pode ficar vazio!");
        }
    }

    public static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = lerLinha().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Responda apenas S (sim) ou N (nao).");
        }
    }

    private static String lerLinha() {
        if (!scan.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada encerrada. Finalizando o programa.");
            System.exit(0);
        }
        return scan.nextLine().trim();
    }
}
