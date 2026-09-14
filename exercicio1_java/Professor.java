/**
 * Professor é a outra especialização de Usuario. Estruturalmente é
 * idêntico a Aluno — a única diferença de verdade é o valor do limite.
 * Isso reforça por que vale a pena ter uma classe abstrata Usuario:
 * evita duplicar nome, contador, podeEmprestar(), etc. em cada tipo.
 */
public class Professor extends Usuario {

    public Professor(String nome) {
        super(nome);
    }

    /**
     * Regra de negócio: professor tem um limite maior que aluno
     * (5 itens em vez de 3) — reflete um "privilégio" do perfil.
     */
    @Override
    public int limiteItens() {
        return 5;
    }
}
