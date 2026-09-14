/**
 * Aluno é um tipo concreto de Usuario.
 * A única coisa que ele precisa definir é o próprio limite de itens.
 */
public class Aluno extends Usuario {

    public Aluno(String nome) {
        super(nome); // inicializa o nome e zera o contador de empréstimos na classe pai
    }

    /**
     * Regra de negócio: aluno pode ter no máximo 3 itens emprestados
     * ao mesmo tempo.
     */
    @Override
    public int limiteItens() {
        return 3;
    }
}
