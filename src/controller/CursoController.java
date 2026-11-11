package controller;

import dao.AlunoDAO;
import dao.CursoDAO;
import dao.DisciplinaDAO;
import model.Curso;
import model.Disciplina;
import view.CursoView;
import view.DisciplinaView;

import java.util.List;
import java.util.Optional;

// CAMADA CONTROLLER: aplica a regra de negocio.
public class CursoController {

    public static void Create() {
        Curso curso = new Curso();
        CursoView.Create(curso);

        if (!CursoDAO.Create(curso)) {
            CursoView.Message("ERRO: ja existe um curso com o codigo " + curso.getCodigo() + "!");
            return;
        }
        CursoView.Message("Curso cadastrado com sucesso!");
        CursoView.Message("Todo curso precisa de pelo menos 1 disciplina. Cadastre a primeira:");

        int cadastradas = 0;
        do {
            Disciplina disciplina = new Disciplina();
            DisciplinaView.Create(disciplina, curso);

            if (DisciplinaDAO.Create(disciplina)) {
                cadastradas++;
                curso.adicionarDisciplina(disciplina);
                DisciplinaView.Message("Disciplina cadastrada com sucesso!");
            } else {
                DisciplinaView.Message("ERRO: ja existe uma disciplina com o codigo "
                        + disciplina.getCodigo() + "! Informe outro codigo.");
            }
            // REGRA: enquanto nao houver nenhuma disciplina, repete sem perguntar.
        } while (cadastradas == 0 || DisciplinaView.PerguntarOutra());

        CursoView.Message("Curso finalizado com " + cadastradas + " disciplina(s).");
    }

    public static void Read() {
        List<Curso> cursos = CursoDAO.ReadAll();
        CursoView.ShowList(cursos);
        if (cursos.isEmpty()) {
            return;
        }

        int codigo = CursoView.Read();
        Optional<Curso> curso = CursoDAO.Read(codigo);

        if (curso.isPresent()) {
            CursoView.Show(curso.get());
        } else {
            CursoView.Message("Curso nao encontrado!");
        }
    }

    public static void Update() {
        List<Curso> cursos = CursoDAO.ReadAll();
        CursoView.ShowList(cursos);
        if (cursos.isEmpty()) {
            return;
        }

        int codigo = CursoView.Read();
        Optional<Curso> cursoEncontrado = CursoDAO.Read(codigo);

        if (cursoEncontrado.isEmpty()) {
            CursoView.Message("Curso nao encontrado!");
            return;
        }

        Curso curso = cursoEncontrado.get();
        CursoView.Update(curso);

        if (CursoDAO.Update(curso)) {
            CursoView.Message("Curso atualizado com sucesso!");
        } else {
            CursoView.Message("Nao foi possivel atualizar o curso!");
        }
    }

    public static void Delete() {
        List<Curso> cursos = CursoDAO.ReadAll();
        CursoView.ShowList(cursos);
        if (cursos.isEmpty()) {
            return;
        }

        int codigo = CursoView.Read();
        if (!CursoDAO.Exists(codigo)) {
            CursoView.Message("Curso nao encontrado!");
            return;
        }

        int alunosMatriculados = AlunoDAO.CountByCurso(codigo);
        if (alunosMatriculados > 0) {
            CursoView.Message("NAO E POSSIVEL REMOVER: existem " + alunosMatriculados
                    + " aluno(s) matriculado(s) neste curso.");
            CursoView.Message("Remova ou transfira os alunos antes de excluir o curso.");
            return;
        }

        int disciplinasRemovidas = DisciplinaDAO.DeleteByCurso(codigo);

        if (CursoDAO.Delete(codigo)) {
            CursoView.Message("Curso removido com sucesso! ("
                    + disciplinasRemovidas + " disciplina(s) removida(s) junto)");
        } else {
            CursoView.Message("Nao foi possivel remover o curso!");
        }
    }
}
