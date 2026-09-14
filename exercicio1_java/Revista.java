/**
 * Revista segue exatamente a mesma estrutura de Livro — a diferença
 * está só nos VALORES retornados pelos métodos abstratos.
 *
 * Isso mostra bem o propósito da herança: o "esqueleto" (o que é um item,
 * como ele se comporta, como é exibido) é escrito UMA VEZ na classe pai;
 * cada subclasse só varia o que realmente é específico dela.
 */
public class Revista extends ItemBiblioteca {

    public Revista(String codigo, String titulo) {
        super(codigo, titulo);
    }

    /**
     * Regra de negócio: revista tem prazo mais curto que livro (7 dias
     * contra 14) — faz sentido, já que revistas costumam ser mais
     * "descartáveis"/atuais e a biblioteca quer que circulem mais rápido.
     */
    @Override
    public int prazoEmprestimo() {
        return 7;
    }

    /**
     * Regra de negócio: em compensação, a multa por atraso é o dobro
     * da do livro (R$ 1,00/dia) — um incentivo a devolver rápido.
     */
    @Override
    public double multaPorDia() {
        return 1.00;
    }
}
