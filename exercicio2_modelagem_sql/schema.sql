-- =========================================================
-- Sistema de Biblioteca - Modelagem PostgreSQL
--
-- Este arquivo é a versão "banco de dados" do sistema que fizemos em
-- Java no exercício 1. A ideia de fundo é a mesma (itens, usuários,
-- empréstimos), mas SQL não tem herança/polimorfismo como o Java —
-- então a modelagem usa outras técnicas para representar a mesma coisa.
--
-- ORDEM DE CRIAÇÃO IMPORTA: criamos "item" e "usuario" primeiro porque
-- a tabela "emprestimo" vai ter CHAVES ESTRANGEIRAS apontando para elas.
-- O banco não deixa criar uma referência para uma tabela que ainda
-- não existe.
-- =========================================================

-- ---------------------------------------------------------
-- Tabela: item
--
-- Note a diferença de abordagem em relação ao Java: lá tínhamos DUAS
-- classes (Livro e Revista) herdando de ItemBiblioteca. Aqui, em vez de
-- criar duas tabelas separadas, colocamos TUDO em uma tabela só e usamos
-- uma coluna "tipo" para diferenciar. Essa técnica se chama
-- "Single Table Inheritance" (herança em tabela única) — é mais simples
-- de implementar, mas tem a desvantagem de colunas que só fazem sentido
-- para um dos tipos (autor/edicao) ficarem vazias para o outro tipo.
-- ---------------------------------------------------------
CREATE TABLE item (
    -- GENERATED ALWAYS AS IDENTITY = auto-incremento (substitui o antigo
    -- SERIAL do PostgreSQL). PRIMARY KEY garante que id é único e cria
    -- automaticamente um índice para buscas rápidas por id.
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    -- UNIQUE garante que não existam dois itens com o mesmo código
    -- (equivale ao "codigo" do Java, que identificava cada item).
    codigo      VARCHAR(20)  NOT NULL UNIQUE,

    titulo      VARCHAR(200) NOT NULL,

    -- CHECK (tipo IN (...)) é uma "trava" no próprio banco: nenhuma
    -- linha pode ser inserida com um valor de tipo diferente desses dois.
    -- É como se fosse o equivalente, em banco de dados, de só existirem
    -- as classes Livro e Revista no Java.
    tipo        VARCHAR(10)  NOT NULL CHECK (tipo IN ('livro', 'revista')),

    -- Essas duas colunas são OPCIONAIS (aceitam NULL) porque cada uma só
    -- faz sentido para um dos tipos de item:
    autor       VARCHAR(150),   -- preenchido quando tipo = 'livro'
    edicao      VARCHAR(50),    -- preenchido quando tipo = 'revista'

    -- Equivalente ao atributo "disponivel" da classe ItemBiblioteca.
    disponivel  BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- Tabela: usuario
--
-- Mesma estratégia da tabela "item": uma tabela só para os dois tipos
-- de usuário (aluno e professor), diferenciados pela coluna "tipo".
-- ---------------------------------------------------------
CREATE TABLE usuario (
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    tipo          VARCHAR(10)  NOT NULL CHECK (tipo IN ('aluno', 'professor')),

    -- No Java, o limite vinha do método limiteItens() de cada subclasse
    -- (3 para Aluno, 5 para Professor). Aqui, como não existe polimorfismo
    -- em SQL, guardamos o valor diretamente como um dado na linha.
    limite_itens  INTEGER      NOT NULL
);

-- ---------------------------------------------------------
-- Tabela: emprestimo
-- Depende de item e usuario já existirem, por causa das FOREIGN KEYS.
--
-- Esta tabela é o equivalente, em banco de dados, do que a classe
-- Biblioteca fazia "na memória" no Java: registrar quem pegou o quê.
-- ---------------------------------------------------------
CREATE TABLE emprestimo (
    id                       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    -- REFERENCES cria uma CHAVE ESTRANGEIRA (foreign key): o banco
    -- garante que todo item_id inserido aqui precisa EXISTIR de fato
    -- na tabela item (mesma lógica para usuario_id/tabela usuario).
    -- Isso evita "empréstimos fantasmas" apontando para itens/usuários
    -- que não existem.
    item_id                  INTEGER NOT NULL REFERENCES item(id),
    usuario_id               INTEGER NOT NULL REFERENCES usuario(id),

    -- CURRENT_DATE como valor padrão: se não informarmos a data de
    -- retirada no INSERT, o banco assume "hoje" automaticamente.
    data_retirada            DATE    NOT NULL DEFAULT CURRENT_DATE,

    -- Data limite para devolver, calculada com base no prazo do item
    -- (14 dias para livro, 7 para revista) somado à data de retirada.
    -- Essa soma é feita pela aplicação/pelo usuário, não pelo banco.
    data_devolucao_prevista  DATE    NOT NULL,

    -- Fica NULL enquanto o item ainda está emprestado (não devolvido).
    -- É esse "NULL" que usamos depois, nas consultas, para identificar
    -- empréstimos "em aberto".
    data_devolucao           DATE,

    -- Valor acumulado de multa por atraso. Começa em 0 e só é atualizado
    -- quando o item é devolvido com atraso.
    valor_multa              NUMERIC(10,2) NOT NULL DEFAULT 0
);

-- =========================================================
-- Dados de teste
-- Servem para testar as consultas do exercício 3 com um cenário
-- conhecido — parecido com o cenário simulado na classe Main do Java.
-- =========================================================

-- 4 itens: 2 livros, 2 revistas.
-- Repare no padrão: quando tipo='livro', "autor" é preenchido e "edicao"
-- fica NULL; quando tipo='revista', é o contrário.
INSERT INTO item (codigo, titulo, tipo, autor, edicao, disponivel) VALUES
    ('L001', 'Dom Casmurro',      'livro',   'Machado de Assis', NULL,         TRUE),
    ('L002', 'O Cortiço',         'livro',   'Aluísio Azevedo',  NULL,         FALSE), -- já emprestado
    ('R001', 'Superinteressante', 'revista', NULL,               'Ed. 450',    TRUE),
    ('R002', 'Veja',              'revista', NULL,               'Ed. 2800',   FALSE); -- já emprestado

-- 2 usuários: um aluno (limite 3), um professor (limite 5) — mesmos
-- valores usados no exercício 1 em Java.
INSERT INTO usuario (nome, tipo, limite_itens) VALUES
    ('Maria Silva', 'aluno',     3),
    ('João Santos', 'professor', 5);

-- 2 empréstimos: um em aberto (para testar a consulta 2 e a 4),
-- e um já devolvido com multa (para testar a consulta 3).
INSERT INTO emprestimo (item_id, usuario_id, data_retirada, data_devolucao_prevista, data_devolucao, valor_multa) VALUES
    -- Empréstimo EM ABERTO: item_id=2 ("O Cortiço"), usuario_id=1 (Maria).
    -- data_devolucao é NULL porque ela ainda não devolveu.
    (2, 1, '2026-08-01', '2026-08-15', NULL,         0.00),

    -- Empréstimo DEVOLVIDO COM ATRASO: item_id=4 ("Veja"), usuario_id=2 (João).
    -- Prazo previsto: 2026-07-08. Devolveu em: 2026-07-10 -> 2 dias de atraso.
    -- Como revista multa R$1,00/dia, o valor da multa é 2 x R$1,00 = R$2,00.
    (4, 2, '2026-07-01', '2026-07-08', '2026-07-10', 2.00);
