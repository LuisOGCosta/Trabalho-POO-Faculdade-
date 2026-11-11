package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// PADRAO SINGLETON: uma unica conexao para todo o sistema (abrir conexao e caro).
public class DatabaseConnection {

    private static Connection connection = null;

    private static final String HOST = "jdbc:mysql://localhost/";
    private static final String DATABASE = "faculdade";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                System.out.println("Conectando ao banco de dados...");
                connection = DriverManager.getConnection(HOST + DATABASE, DB_USER, DB_PASS);
                System.out.println("Conexao bem-sucedida!");
            } catch (SQLException e) {
                System.out.println("Erro ao conectar: " + e.getMessage());
                throw new RuntimeException("Falha ao conectar ao banco de dados", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexao fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexao: " + e.getMessage());
            }
        }
    }
}
