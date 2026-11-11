import dao.AlunoDAO;
import dao.CursoDAO;
import dao.DisciplinaDAO;
import database.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.getConnection();

        // A ORDEM IMPORTA: "disciplinas" e "alunos" tem chave estrangeira
        // apontando para "cursos", entao "cursos" precisa existir primeiro.
        CursoDAO.CreateTable();
        DisciplinaDAO.CreateTable();
        AlunoDAO.CreateTable();

        Menu.Show();

        DatabaseConnection.closeConnection();
    }
}
