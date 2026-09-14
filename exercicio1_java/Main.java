/**
 * Classe de entrada do programa (contém o método main). Sua função é
 * apenas MONTAR UM CENÁRIO DE TESTE: criar objetos, chamar os métodos
 * da Biblioteca em uma ordem que exercite as regras de negócio
 * (empréstimo permitido, empréstimo recusado por limite, devolução)
 * e imprimir os resultados no console para conferência visual.
 */
public class Main {

    public static void main(String[] args) {

        // Cria uma biblioteca com capacidade para 10 itens no acervo.
        Biblioteca biblioteca = new Biblioteca(10);

        // -----------------------------------------------------------
        // Cadastro do acervo: 3 livros e 1 revista.
        // Cada "new Livro(...)" chama o construtor de Livro, que por sua
        // vez chama super(codigo, titulo) lá em ItemBiblioteca.
        // -----------------------------------------------------------
        Livro livro1 = new Livro("L001", "O Horizonte Mora Em Um Dia Cinza");
        Livro livro2 = new Livro("L002", "Uma Aventura A Dois");
        Livro livro3 = new Livro("L003", "Harry Potter: A Ordem Da Fenix");
        Revista revista1 = new Revista("R001", "Superinteressante - Ed. 450");

        biblioteca.cadastrarItem(livro1);
        biblioteca.cadastrarItem(livro2);
        biblioteca.cadastrarItem(livro3);
        biblioteca.cadastrarItem(revista1);

        // -----------------------------------------------------------
        // Cadastro de usuários: um aluno (limite 3) e um professor (limite 5).
        // -----------------------------------------------------------
        Aluno aluno = new Aluno("Maria Silva");
        Professor professor = new Professor("João Santos");

        System.out.println("=== Acervo inicial ===");
        biblioteca.listarAcervo(); // tudo deve aparecer como "disponível"

        System.out.println("\n=== Cenário de empréstimos ===");

        // Maria (aluno, limite 3) pega os 3 livros — isso EXATAMENTE
        // atinge o limite dela. As três chamadas devem retornar "OK".
        biblioteca.emprestar(aluno, livro1);
        biblioteca.emprestar(aluno, livro2);
        biblioteca.emprestar(aluno, livro3);

        // Maria tenta pegar um 4º item (a revista). Isso deve ser
        // RECUSADO, porque podeEmprestar() agora retorna false
        // (quantidadeEmprestada == limiteItens()).
        biblioteca.emprestar(aluno, revista1);

        // João (professor, limite 5) ainda não pegou nada, então
        // consegue pegar a mesma revista sem problema.
        biblioteca.emprestar(professor, revista1);

        System.out.println("\n=== Situação dos usuários ===");
        // Aqui o toString() de Usuario é chamado implicitamente por println.
        System.out.println(aluno);      // deve mostrar 3/3 itens emprestados
        System.out.println(professor);  // deve mostrar 1/5 itens emprestados

        System.out.println("\n=== Acervo após empréstimos ===");
        // Agora os 4 itens do acervo devem aparecer como "emprestado".
        biblioteca.listarAcervo();

        System.out.println("\n=== Devolução ===");
        // Maria devolve o primeiro livro.
        biblioteca.devolver(aluno, livro1);

        System.out.println("\n=== Acervo final ===");
        // Só "Dom Casmurro" deve voltar a aparecer como "disponível";
        // os outros 3 continuam emprestados.
        biblioteca.listarAcervo();
    }
}
