-- =========================================================
-- Exercício 3 - Consultas SQL
--
-- Estas consultas usam o schema e os dados de teste do exercício 2.
-- Cada uma exercita um conceito diferente de SQL: SELECT simples,
-- JOIN, agregação com GROUP BY, e LEFT JOIN para achar "quem não tem
-- correspondência".
-- =========================================================


-- ---------------------------------------------------------
-- 1) Listar todo o acervo, com código, título, tipo e disponibilidade
--
-- É a consulta mais simples: um SELECT direto na tabela "item",
-- sem JOIN nenhum, só ordenando o resultado por código.
-- ---------------------------------------------------------
SELECT
    codigo,
    titulo,
    tipo,
    disponivel
FROM item
ORDER BY codigo;


-- ---------------------------------------------------------
-- 2) Listar os empréstimos em aberto, com o nome do usuário e o
--    título do item
--
-- "Em aberto" significa que a coluna data_devolucao ainda é NULL
-- (ninguém devolveu esse item ainda).
--
-- IMPORTANTE: em SQL, nunca se compara com "= NULL" — essa comparação
-- SEMPRE dá "desconhecido" (equivalente a falso), mesmo se o valor
-- realmente for nulo. O jeito certo de testar isso é com "IS NULL"
-- (ou "IS NOT NULL" para o caso contrário).
-- ---------------------------------------------------------
SELECT
    u.nome  AS usuario,               -- "AS usuario" renomeia a coluna no resultado
    i.titulo AS item,
    e.data_retirada,
    e.data_devolucao_prevista
FROM emprestimo e                     -- "e" é um ALIAS (apelido) para "emprestimo"
JOIN usuario u ON u.id = e.usuario_id -- junta emprestimo com usuario pela chave usuario_id
JOIN item i    ON i.id = e.item_id    -- junta emprestimo com item pela chave item_id
WHERE e.data_devolucao IS NULL;       -- filtra só quem ainda não devolveu

-- Com os dados de teste, essa consulta retorna 1 linha: Maria Silva
-- com o item "O Cortiço" (o empréstimo que cadastramos sem data de
-- devolução).


-- ---------------------------------------------------------
-- 3) Calcular o total de multas acumuladas por usuário
--
-- Aqui usamos uma FUNÇÃO DE AGREGAÇÃO (SUM), que soma valores de
-- várias linhas em um só resultado. Sempre que uma consulta mistura
-- uma função de agregação com uma coluna "normal" (aqui, u.nome),
-- é obrigatório usar GROUP BY nessa coluna normal — é isso que diz
-- ao banco "quero uma soma para CADA nome diferente".
-- ---------------------------------------------------------
SELECT
    u.nome AS usuario,
    SUM(e.valor_multa) AS total_multas   -- soma todas as multas daquele usuário
FROM emprestimo e
JOIN usuario u ON u.id = e.usuario_id
GROUP BY u.nome                          -- agrupa uma linha de resultado por usuário
ORDER BY total_multas DESC;              -- quem deve mais aparece primeiro

-- Com os dados de teste, o resultado esperado é:
--   João Santos -> R$ 2,00 (a multa da revista "Veja")
--   Maria Silva -> R$ 0,00 (empréstimo em aberto, ainda sem multa registrada)


-- ---------------------------------------------------------
-- 4) Listar os itens que nunca foram emprestados
--
-- Esta é a consulta mais "conceitual" do exercício. A ideia:
--
-- Um JOIN comum (INNER JOIN) só traz linhas que TÊM correspondência
-- nas duas tabelas. Se um item nunca aparece em "emprestimo", ele
-- simplesmente SUMIRIA do resultado de um JOIN normal — o que é o
-- oposto do que queremos aqui.
--
-- Um LEFT JOIN resolve isso: ele mantém TODAS as linhas da tabela à
-- esquerda (item), e quando não existe correspondência na tabela à
-- direita (emprestimo), ele preenche as colunas de emprestimo com NULL
-- em vez de descartar a linha.
--
-- Então: se e.id vier NULL depois do LEFT JOIN, é porque aquele item
-- nunca teve nenhum empréstimo associado a ele.
-- ---------------------------------------------------------
SELECT
    i.codigo,
    i.titulo
FROM item i
LEFT JOIN emprestimo e ON e.item_id = i.id
WHERE e.id IS NULL;   -- pega só os itens que NÃO tiveram nenhuma linha correspondente

-- Com os dados de teste, retorna:
--   "Dom Casmurro" e "Superinteressante" — os dois únicos itens que
-- nunca aparecem como item_id em nenhuma linha de "emprestimo".
-- ("O Cortiço" e "Veja" ficam de fora porque cada um tem 1 empréstimo
-- registrado.)
