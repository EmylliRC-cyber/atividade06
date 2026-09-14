/**
 * Classe ABSTRATA que representa qualquer pessoa que pega itens
 * emprestados na biblioteca.
 *
 * É a "irmã gêmea" de ItemBiblioteca: mesma ideia, mesma estrutura,
 * só que modelando usuários em vez de itens. Repare no padrão que se
 * repete no exercício inteiro:
 *   1. atributos privados comuns a todos os subtipos
 *   2. construtor que inicializa esses atributos comuns
 *   3. um ou mais métodos "abstract" para o que varia por subtipo
 *   4. getters públicos
 *   5. toString() que usa os métodos abstratos (polimorfismo)
 */
public abstract class Usuario {

    private String nome;
    private int quantidadeEmprestada; // quantos itens esse usuário tem em mãos agora

    public Usuario(String nome) {
        this.nome = nome;
        this.quantidadeEmprestada = 0; // todo usuário começa sem nenhum item emprestado
    }

    /**
     * Método abstrato: cada tipo de usuário tem um limite diferente
     * de quantos itens pode ter emprestados ao mesmo tempo.
     * Quem decide esse número é a subclasse (Aluno ou Professor).
     */
    public abstract int limiteItens();

    public String getNome() {
        return nome;
    }

    public int getQuantidadeEmprestada() {
        return quantidadeEmprestada;
    }

    /**
     * Regra de negócio central deste exercício: um usuário só pode pegar
     * mais um item emprestado se ainda não tiver atingido o limite dele.
     *
     * Note que este método é "package-private" (sem public/private) —
     * só a Biblioteca (mesmo pacote) deveria consultar isso antes de
     * liberar um empréstimo. Não é uma informação de uso livre por
     * qualquer parte do sistema.
     */
    boolean podeEmprestar() {
        return quantidadeEmprestada < limiteItens();
    }

    /**
     * Chamado pela Biblioteca sempre que um empréstimo é CONCLUÍDO
     * com sucesso, para atualizar o contador do usuário.
     */
    void incrementarEmprestimo() {
        quantidadeEmprestada++;
    }

    /**
     * Chamado pela Biblioteca quando o usuário devolve um item.
     * O "if" é uma proteção defensiva: mesmo que, por algum bug,
     * decrementarEmprestimo() fosse chamado a mais vezes do que deveria,
     * o contador nunca fica negativo (o que não faria sentido no mundo real).
     */
    void decrementarEmprestimo() {
        if (quantidadeEmprestada > 0) {
            quantidadeEmprestada--;
        }
    }

    /**
     * Assim como em ItemBiblioteca, este toString() usa
     * getClass().getSimpleName() e limiteItens() — que são resolvidos
     * de forma diferente dependendo se o objeto real é um Aluno ou
     * um Professor. É polimorfismo de novo, agora do lado dos usuários.
     */
    @Override
    public String toString() {
        return String.format("%-15s (%-10s) - %d/%d itens emprestados",
                nome, getClass().getSimpleName(), quantidadeEmprestada, limiteItens());
    }
}
