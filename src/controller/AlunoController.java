package controller;

import dao.AlunoDAO;
import dao.CursoDAO;
import model.Aluno;
import model.Curso;
import view.AlunoView;
import view.CursoView;

import java.util.List;
import java.util.Optional;

public class AlunoController {

    public static void Create() {
        List<Curso> cursos = CursoDAO.ReadAll();
        if (cursos.isEmpty()) {
            AlunoView.Message("Nenhum curso cadastrado! Cadastre um CURSO antes de cadastrar alunos.");
            return;
        }

        Curso curso = CursoView.Escolher(cursos, "Informe o codigo do curso do aluno: ");

        Aluno aluno = new Aluno();
        AlunoView.Create(aluno, curso);

        if (AlunoDAO.Create(aluno)) {
            AlunoView.Message("Aluno cadastrado com sucesso!");
        } else {
            AlunoView.Message("ERRO: ja existe um aluno com a matricula "
                    + aluno.getMatricula() + "!");
        }
    }

    public static void Read() {
        List<Aluno> alunos = AlunoDAO.ReadAll();
        AlunoView.ShowList(alunos);
        if (alunos.isEmpty()) {
            return;
        }

        int matricula = AlunoView.Read();
        Optional<Aluno> aluno = AlunoDAO.Read(matricula);

        if (aluno.isPresent()) {
            AlunoView.Show(aluno.get());
        } else {
            AlunoView.Message("Aluno nao encontrado!");
        }
    }

    public static void Update() {
        List<Aluno> alunos = AlunoDAO.ReadAll();
        AlunoView.ShowList(alunos);
        if (alunos.isEmpty()) {
            return;
        }

        int matricula = AlunoView.Read();
        Optional<Aluno> alunoEncontrado = AlunoDAO.Read(matricula);

        if (alunoEncontrado.isEmpty()) {
            AlunoView.Message("Aluno nao encontrado!");
            return;
        }

        Aluno aluno = alunoEncontrado.get();
        List<Curso> cursos = CursoDAO.ReadAll();

        AlunoView.Message("Curso atual: " + aluno.getCurso().getNome());
        Curso curso = CursoView.Escolher(cursos, "Informe o codigo do curso do aluno: ");

        AlunoView.Update(aluno, curso);

        if (AlunoDAO.Update(aluno)) {
            AlunoView.Message("Aluno atualizado com sucesso!");
        } else {
            AlunoView.Message("Nao foi possivel atualizar o aluno!");
        }
    }

    public static void Delete() {
        List<Aluno> alunos = AlunoDAO.ReadAll();
        AlunoView.ShowList(alunos);
        if (alunos.isEmpty()) {
            return;
        }

        int matricula = AlunoView.Read();

        if (AlunoDAO.Delete(matricula)) {
            AlunoView.Message("Aluno removido com sucesso!");
        } else {
            AlunoView.Message("Aluno nao encontrado!");
        }
    }
}
