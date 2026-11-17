import dao.AlunoDAO;
import dao.CursoDAO;
import dao.DisciplinaDAO;
import database.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.getConnection();

        CursoDAO.CreateTable();
        DisciplinaDAO.CreateTable();
        AlunoDAO.CreateTable();

        Menu.Show();

        DatabaseConnection.closeConnection();
    }
}
