/**
 * Livro é uma classe CONCRETA (pode ser instanciada com "new Livro(...)").
 *
 * "extends ItemBiblioteca" significa que Livro HERDA tudo o que
 * ItemBiblioteca já definiu: os atributos código/título/disponível,
 * os getters, os métodos marcarEmprestado()/marcarDevolvido() e o toString().
 *
 * O que Livro precisa fazer é só preencher a parte que faltava:
 * implementar os dois métodos que eram "abstract" na classe pai.
 */
public class Livro extends ItemBiblioteca {

    /**
     * "super(codigo, titulo)" chama o construtor da CLASSE PAI
     * (ItemBiblioteca) para inicializar código, título e disponibilidade.
     * Toda subclasse deve chamar super(...) — se você não escrever,
     * o Java tenta chamar um construtor vazio da classe pai, e como
     * ItemBiblioteca não tem um, o código nem compilaria.
     */
    public Livro(String codigo, String titulo) {
        super(codigo, titulo);
    }

    /**
     * @Override avisa o compilador (e quem lê o código) que este método
     * está substituindo/implementando um método da classe pai. Se você
     * escrever o nome errado por engano, o compilador acusa erro —
     * é uma rede de segurança.
     *
     * Regra de negócio: livro pode ficar emprestado por 14 dias.
     */
    @Override
    public int prazoEmprestimo() {
        return 14;
    }

    /**
     * Regra de negócio: cada dia de atraso na devolução de um livro
     * custa R$ 0,50.
     */
    @Override
    public double multaPorDia() {
        return 0.50;
    }
}
