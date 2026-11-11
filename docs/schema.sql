-- ============================================================
--  Trabalho de POO - Sistema de Faculdade
--  Estrutura do banco de dados
-- ============================================================
--  OBSERVACAO IMPORTANTE:
--  O sistema NAO precisa deste arquivo para funcionar. As tabelas
--  sao criadas pelo proprio programa, no metodo CreateTable() de
--  cada DAO, chamado pelo Main na inicializacao.
--  Este arquivo existe para documentar a estrutura e para entregar
--  junto com o trabalho.
-- ============================================================

CREATE DATABASE IF NOT EXISTS faculdade CHARACTER SET utf8mb4;
USE faculdade;

-- ------------------------------------------------------------
--  A ORDEM DE CRIACAO IMPORTA:
--  "disciplinas" e "alunos" possuem chave estrangeira apontando
--  para "cursos", entao "cursos" precisa ser criada primeiro.
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS cursos (
    codigo INT PRIMARY KEY,
    nome   VARCHAR(100) NOT NULL,
    turno  VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplinas (
    codigo        INT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL,
    codigo_curso  INT NOT NULL,
    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
);

CREATE TABLE IF NOT EXISTS alunos (
    matricula    INT PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    idade        INT NOT NULL,
    codigo_curso INT NOT NULL,
    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo)
);

-- ------------------------------------------------------------
--  Dados de exemplo (opcional - descomente se quiser popular)
-- ------------------------------------------------------------
-- INSERT INTO cursos VALUES (101, 'Sistemas de Informacao', 'Noite');
-- INSERT INTO disciplinas VALUES (201, 'Programacao Orientada a Objetos', 80, 101);
-- INSERT INTO disciplinas VALUES (202, 'Banco de Dados', 60, 101);
-- INSERT INTO alunos VALUES (2026001, 'Luis Otavio Martins', 20, 101);
