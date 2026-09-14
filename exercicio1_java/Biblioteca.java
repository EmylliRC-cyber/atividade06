/**
 * Biblioteca é a classe que ORQUESTRA o sistema inteiro. Ela não herda
 * de nada — é uma classe "de serviço", que conhece ItemBiblioteca e
 * Usuario apenas pelo tipo mais genérico possível (a classe abstrata),
 * nunca por Livro/Revista/Aluno/Professor especificamente.
 *
 * Essa é uma ideia importante de POO: "programar para a abstração,
 * não para a implementação". Se amanhã alguém criar uma classe "DVD"
 * que também extends ItemBiblioteca, ESTA classe não precisa mudar
 * NADA — é só implementar prazoEmprestimo()/multaPorDia() no DVD e
 * tudo aqui continua funcionando.
 */
public class Biblioteca {

    // Um array de tamanho FIXO (definido lá no construtor) — por isso
    // precisamos controlar manualmente quantas posições já usamos.
    private ItemBiblioteca[] acervo;
    private int totalItens; // quantas posições do array já estão preenchidas de fato

    public Biblioteca(int capacidade) {
        this.acervo = new ItemBiblioteca[capacidade]; // array vazio (todo null por enquanto)
        this.totalItens = 0;
    }

    /**
     * Adiciona um item na próxima posição livre do array.
     * Repare que o parâmetro é do tipo "ItemBiblioteca" — funciona
     * tanto para um Livro quanto para uma Revista, sem distinção
     * nenhuma no código (de novo, polimorfismo).
     */
    public void cadastrarItem(ItemBiblioteca item) {
        if (totalItens < acervo.length) {
            acervo[totalItens] = item;
            totalItens++;
        } else {
            // Proteção simples: evita "ArrayIndexOutOfBoundsException"
            // se tentarem cadastrar mais itens do que a capacidade permite.
            System.out.println("Acervo cheio. Não foi possível cadastrar: " + item.getTitulo());
        }
    }

    /**
     * Tenta realizar um empréstimo. Retorna true/false para quem chamou
     * saber se deu certo (útil, por exemplo, se quiséssemos tomar alguma
     * ação diferente em caso de recusa).
     */
    public boolean emprestar(Usuario usuario, ItemBiblioteca item) {
        // 1ª validação: o item precisa estar livre
        if (!item.isDisponivel()) {
            System.out.println("RECUSADO: item indisponível -> " + item.getTitulo());
            return false;
        }
        // 2ª validação: o usuário não pode ter estourado o limite dele
        if (!usuario.podeEmprestar()) {
            System.out.println("RECUSADO: limite de itens atingido -> " + usuario.getNome());
            return false;
        }

        // Se passou pelas duas validações, o empréstimo é efetivado:
        item.marcarEmprestado();         // muda o estado do item
        usuario.incrementarEmprestimo(); // atualiza o contador do usuário
        System.out.println("OK: " + usuario.getNome() + " pegou emprestado \"" + item.getTitulo() + "\"");
        return true;
    }

    /**
     * Processo espelhado ao de emprestar(): valida, depois atualiza
     * o estado do item e do usuário.
     */
    public boolean devolver(Usuario usuario, ItemBiblioteca item) {
        if (item.isDisponivel()) {
            // Se já está disponível, é porque nunca foi emprestado
            // (ou já foi devolvido antes) — não faz sentido devolver de novo.
            System.out.println("AVISO: item já está disponível -> " + item.getTitulo());
            return false;
        }

        item.marcarDevolvido();
        usuario.decrementarEmprestimo();
        System.out.println("OK: " + usuario.getNome() + " devolveu \"" + item.getTitulo() + "\"");
        return true;
    }

    /**
     * ESTE É O MÉTODO MAIS IMPORTANTE PARA ENTENDER POLIMORFISMO
     * NESTE EXERCÍCIO.
     *
     * Percorremos o array de ItemBiblioteca com um ÚNICO laço for,
     * sem NENHUM "if (item instanceof Livro)" ou parecido.
     *
     * Quando fazemos "System.out.println(acervo[i])", o Java chama
     * automaticamente o toString() do objeto. E mesmo a variável
     * sendo declarada como "ItemBiblioteca", o objeto DE VERDADE
     * guardado ali dentro pode ser um Livro ou uma Revista — e é o
     * toString() (e prazoEmprestimo()/multaPorDia() chamados por ele)
     * DESSE TIPO REAL que é executado. Essa decisão "em tempo de
     * execução" de qual método rodar é o que chamamos de polimorfismo
     * dinâmico (dynamic dispatch).
     */
    public void listarAcervo() {
        System.out.println("--- Acervo da Biblioteca ---");
        // percorremos só até totalItens (não até acervo.length),
        // para não imprimir posições ainda vazias (null) do array
        for (int i = 0; i < totalItens; i++) {
            System.out.println(acervo[i]);
        }
    }
}
