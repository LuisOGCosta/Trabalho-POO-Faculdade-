package dao;

import database.DatabaseConnection;
import model.Curso;
import model.Disciplina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// PADRAO DAO: unica camada que conhece SQL. Trocar de banco mexe so aqui.
public class CursoDAO {

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cursos (
                    codigo INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    turno VARCHAR(20) NOT NULL
                )
                """;
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela cursos", e);
        }
    }

    public static boolean Create(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nome, turno) VALUES (?, ?, ?)";

        // PreparedStatement com "?": evita SQL Injection e trata os tipos.
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, curso.getCodigo());
            stmt.setString(2, curso.getNome());
            stmt.setString(3, curso.getTurno());
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar curso", e);
        }
    }

    public static Optional<Curso> Read(int codigo) {
        String sql = "SELECT codigo, nome, turno FROM cursos WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curso curso = new Curso(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("turno")
                    );
                    List<Disciplina> disciplinas = DisciplinaDAO.ReadByCurso(curso.getCodigo());
                    curso.setDisciplinas(disciplinas);
                    return Optional.of(curso);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar curso", e);
        }
        return Optional.empty();
    }

    public static List<Curso> ReadAll() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT codigo, nome, turno FROM cursos ORDER BY codigo";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cursos.add(new Curso(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("turno")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cursos", e);
        }
        return cursos;
    }

    public static boolean Update(Curso curso) {
        String sql = "UPDATE cursos SET nome = ?, turno = ? WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getTurno());
            stmt.setInt(3, curso.getCodigo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar curso", e);
        }
    }

    public static boolean Delete(int codigo) {
        String sql = "DELETE FROM cursos WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover curso", e);
        }
    }

    public static boolean Exists(int codigo) {
        return Read(codigo).isPresent();
    }
}
