package dao;

import database.DatabaseConnection;
import model.Aluno;
import model.Curso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO {

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS alunos (
                    matricula INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    idade INT NOT NULL,
                    codigo_curso INT NOT NULL,
                    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
                )
                """;
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela alunos", e);
        }
    }

    public static boolean Create(Aluno aluno) {
        String sql = "INSERT INTO alunos (matricula, nome, idade, codigo_curso) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, aluno.getMatricula());
            stmt.setString(2, aluno.getNome());
            stmt.setInt(3, aluno.getIdade());
            stmt.setInt(4, aluno.getCurso().getCodigo());
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar aluno", e);
        }
    }

    // Monta a cadeia Aluno -> Curso -> List<Disciplina> em um objeto so.
    public static Optional<Aluno> Read(int matricula) {
        String sql = "SELECT matricula, nome, idade, codigo_curso FROM alunos WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curso curso = CursoDAO.Read(rs.getInt("codigo_curso")).orElse(null);
                    return Optional.of(new Aluno(
                            rs.getInt("matricula"),
                            rs.getString("nome"),
                            rs.getInt("idade"),
                            curso
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar aluno", e);
        }
        return Optional.empty();
    }

    public static List<Aluno> ReadAll() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = """
                SELECT a.matricula, a.nome, a.idade,
                       c.codigo AS curso_codigo, c.nome AS curso_nome, c.turno AS curso_turno
                FROM alunos a
                INNER JOIN cursos c ON a.codigo_curso = c.codigo
                ORDER BY a.matricula
                """;

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("curso_codigo"),
                        rs.getString("curso_nome"),
                        rs.getString("curso_turno")
                );
                alunos.add(new Aluno(
                        rs.getInt("matricula"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        curso
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos", e);
        }
        return alunos;
    }

    public static boolean Update(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, idade = ?, codigo_curso = ? WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setInt(2, aluno.getIdade());
            stmt.setInt(3, aluno.getCurso().getCodigo());
            stmt.setInt(4, aluno.getMatricula());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno", e);
        }
    }

    public static boolean Delete(int matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover aluno", e);
        }
    }

    public static int CountByCurso(int codigoCurso) {
        String sql = "SELECT COUNT(*) AS total FROM alunos WHERE codigo_curso = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar alunos do curso", e);
        }
        return 0;
    }
}
