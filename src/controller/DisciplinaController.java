package controller;

import dao.CursoDAO;
import dao.DisciplinaDAO;
import model.Curso;
import model.Disciplina;
import view.CursoView;
import view.DisciplinaView;

import java.util.List;
import java.util.Optional;

public class DisciplinaController {

    public static void Create() {
        List<Curso> cursos = CursoDAO.ReadAll();
        if (cursos.isEmpty()) {
            DisciplinaView.Message("Nenhum curso cadastrado! Cadastre um CURSO antes de cadastrar disciplinas.");
            return;
        }

        Curso curso = CursoView.Escolher(cursos, "Informe o codigo do curso da disciplina: ");

        Disciplina disciplina = new Disciplina();
        DisciplinaView.Create(disciplina, curso);

        if (DisciplinaDAO.Create(disciplina)) {
            DisciplinaView.Message("Disciplina cadastrada com sucesso!");
        } else {
            DisciplinaView.Message("ERRO: ja existe uma disciplina com o codigo "
                    + disciplina.getCodigo() + "!");
        }
    }

    public static void Read() {
        List<Disciplina> disciplinas = DisciplinaDAO.ReadAll();
        DisciplinaView.ShowList(disciplinas);
        if (disciplinas.isEmpty()) {
            return;
        }

        int codigo = DisciplinaView.Read();
        Optional<Disciplina> disciplinaEncontrada = DisciplinaDAO.Read(codigo);

        if (disciplinaEncontrada.isEmpty()) {
            DisciplinaView.Message("Disciplina nao encontrada!");
            return;
        }

        Disciplina disciplina = disciplinaEncontrada.get();
        Curso curso = CursoDAO.Read(disciplina.getCodigoCurso()).orElse(null);
        DisciplinaView.Show(disciplina, curso);
    }

    public static void Update() {
        List<Disciplina> disciplinas = DisciplinaDAO.ReadAll();
        DisciplinaView.ShowList(disciplinas);
        if (disciplinas.isEmpty()) {
            return;
        }

        int codigo = DisciplinaView.Read();
        Optional<Disciplina> disciplinaEncontrada = DisciplinaDAO.Read(codigo);

        if (disciplinaEncontrada.isEmpty()) {
            DisciplinaView.Message("Disciplina nao encontrada!");
            return;
        }

        Disciplina disciplina = disciplinaEncontrada.get();
        List<Curso> cursos = CursoDAO.ReadAll();

        DisciplinaView.Message("Curso atual: codigo " + disciplina.getCodigoCurso());
        Curso curso = CursoView.Escolher(cursos, "Informe o codigo do curso da disciplina: ");

        DisciplinaView.Update(disciplina, curso);

        if (DisciplinaDAO.Update(disciplina)) {
            DisciplinaView.Message("Disciplina atualizada com sucesso!");
        } else {
            DisciplinaView.Message("Nao foi possivel atualizar a disciplina!");
        }
    }

    public static void Delete() {
        List<Disciplina> disciplinas = DisciplinaDAO.ReadAll();
        DisciplinaView.ShowList(disciplinas);
        if (disciplinas.isEmpty()) {
            return;
        }

        int codigo = DisciplinaView.Read();
        Optional<Disciplina> disciplinaEncontrada = DisciplinaDAO.Read(codigo);

        if (disciplinaEncontrada.isEmpty()) {
            DisciplinaView.Message("Disciplina nao encontrada!");
            return;
        }

        Disciplina disciplina = disciplinaEncontrada.get();

        if (DisciplinaDAO.CountByCurso(disciplina.getCodigoCurso()) <= 1) {
            DisciplinaView.Message("NAO E POSSIVEL REMOVER: esta e a unica disciplina do curso "
                    + disciplina.getCodigoCurso() + ".");
            DisciplinaView.Message("Todo curso deve possuir pelo menos 1 disciplina.");
            return;
        }

        if (DisciplinaDAO.Delete(codigo)) {
            DisciplinaView.Message("Disciplina removida com sucesso!");
        } else {
            DisciplinaView.Message("Nao foi possivel remover a disciplina!");
        }
    }
}
