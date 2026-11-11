package dao;

import database.DatabaseConnection;
import model.Disciplina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DisciplinaDAO {

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS disciplinas (
                    codigo INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    carga_horaria INT NOT NULL,
                    codigo_curso INT NOT NULL,
                    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
                )
                """;
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela disciplinas", e);
        }
    }

    public static boolean Create(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria, codigo_curso) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getCargaHoraria());
            stmt.setInt(4, disciplina.getCodigoCurso());
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar disciplina", e);
        }
    }

    public static Optional<Disciplina> Read(int codigo) {
        String sql = "SELECT codigo, nome, carga_horaria, codigo_curso FROM disciplinas WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(montarDisciplina(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar disciplina", e);
        }
        return Optional.empty();
    }

    public static List<Disciplina> ReadAll() {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT codigo, nome, carga_horaria, codigo_curso FROM disciplinas ORDER BY codigo_curso, codigo";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                disciplinas.add(montarDisciplina(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disciplinas", e);
        }
        return disciplinas;
    }

    public static List<Disciplina> ReadByCurso(int codigoCurso) {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT codigo, nome, carga_horaria, codigo_curso FROM disciplinas WHERE codigo_curso = ? ORDER BY codigo";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplinas.add(montarDisciplina(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disciplinas do curso", e);
        }
        return disciplinas;
    }

    public static boolean Update(Disciplina disciplina) {
        String sql = "UPDATE disciplinas SET nome = ?, carga_horaria = ?, codigo_curso = ? WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getCargaHoraria());
            stmt.setInt(3, disciplina.getCodigoCurso());
            stmt.setInt(4, disciplina.getCodigo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disciplina", e);
        }
    }

    public static boolean Delete(int codigo) {
        String sql = "DELETE FROM disciplinas WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover disciplina", e);
        }
    }

    public static int DeleteByCurso(int codigoCurso) {
        String sql = "DELETE FROM disciplinas WHERE codigo_curso = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover disciplinas do curso", e);
        }
    }

    public static int CountByCurso(int codigoCurso) {
        String sql = "SELECT COUNT(*) AS total FROM disciplinas WHERE codigo_curso = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar disciplinas do curso", e);
        }
        return 0;
    }

    private static Disciplina montarDisciplina(ResultSet rs) throws SQLException {
        return new Disciplina(
                rs.getInt("codigo"),
                rs.getString("nome"),
                rs.getInt("carga_horaria"),
                rs.getInt("codigo_curso")
        );
    }
}
