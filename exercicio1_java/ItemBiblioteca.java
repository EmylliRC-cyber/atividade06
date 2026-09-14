/**
 * Classe ABSTRATA que representa qualquer item que a biblioteca empresta.
 *
 * Por que "abstract"?
 * Porque não faz sentido existir um "ItemBiblioteca" genérico no mundo real —
 * só existem LIVROS ou REVISTAS de verdade. Marcar a classe como abstrata
 * IMPEDE que alguém escreva "new ItemBiblioteca(...)" em outro lugar do
 * código. Ela serve só como um "contrato" / "molde" para as subclasses.
 *
 * Essa classe concentra tudo o que é IGUAL entre livro e revista
 * (código, título, disponibilidade) e deixa em aberto ("abstract") o que é
 * DIFERENTE entre eles (prazo de empréstimo e valor da multa).
 */
public abstract class ItemBiblioteca {

    // Atributos privados: só esta classe pode acessá-los diretamente.
    // Isso é ENCAPSULAMENTO — o mundo externo só enxerga os getters abaixo.
    private String codigo;      // identificador único do item, ex: "L001"
    private String titulo;      // ex: "Dom Casmurro"
    private boolean disponivel; // true = pode ser emprestado, false = já está com alguém

    /**
     * Construtor da classe base.
     * Mesmo sendo abstrata, ItemBiblioteca TEM construtor — ele não cria
     * um ItemBiblioteca sozinho, mas é chamado pelas subclasses via super(...)
     * para inicializar a parte "comum" do objeto.
     */
    public ItemBiblioteca(String codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.disponivel = true; // toda vez que um item é criado, ele começa disponível
    }

    // ---------------------------------------------------------------
    // MÉTODOS ABSTRATOS
    // Não têm corpo { } — são apenas uma "assinatura", uma promessa.
    // Toda classe concreta que herdar de ItemBiblioteca (Livro, Revista)
    // é OBRIGADA pelo compilador a implementar os dois métodos abaixo.
    // Isso é a base do POLIMORFISMO: cada subtipo decide seu próprio
    // comportamento, mas todo mundo é chamado da mesma forma.
    // ---------------------------------------------------------------
    public abstract int prazoEmprestimo();   // quantos dias o item pode ficar emprestado
    public abstract double multaPorDia();    // quanto custa cada dia de atraso

    // ---------------------------------------------------------------
    // GETTERS: forma pública e controlada de LER os atributos privados.
    // Repare que não existe "setCodigo" ou "setTitulo" — uma vez criado,
    // código e título de um item não deveriam mudar. Isso é uma escolha
    // de design: menos "setters" = objetos mais previsíveis.
    // ---------------------------------------------------------------
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        // Convenção do Java: getters de boolean começam com "is", não "get"
        return disponivel;
    }

    // ---------------------------------------------------------------
    // Estes dois métodos NÃO têm modificador de acesso (nem public, nem
    // private) — isso é chamado de visibilidade "package-private".
    // Na prática: só classes DO MESMO PACOTE podem chamá-los.
    //
    // Por que isso importa? Porque não queremos que qualquer código externo
    // faça "item.marcarEmprestado()" livremente e "engane" o sistema.
    // Só a classe Biblioteca (que mora no mesmo pacote) deve poder mudar
    // a disponibilidade, e sempre como CONSEQUÊNCIA de emprestar()/devolver().
    // Isso é um exemplo de encapsulamento bem pensado: a regra de negócio
    // fica centralizada em um único lugar (a Biblioteca).
    // ---------------------------------------------------------------
    void marcarEmprestado() {
        this.disponivel = false;
    }

    void marcarDevolvido() {
        this.disponivel = true;
    }

    /**
     * Sobrescreve o toString() padrão do Java (que por padrão imprimiria
     * algo tipo "Livro@1b6d3586", que não ajuda ninguém).
     *
     * O pulo do gato aqui: mesmo esse código estando na classe ABSTRATA,
     * quando ele é executado, "getClass().getSimpleName()" retorna o nome
     * da classe REAL do objeto em tempo de execução ("Livro" ou "Revista"),
     * e as chamadas a prazoEmprestimo()/multaPorDia() executam a versão
     * IMPLEMENTADA pela subclasse concreta. Isso é polimorfismo puro:
     * o mesmo código serve para qualquer tipo de item, sem nenhum "if".
     */
    @Override
    public String toString() {
        return String.format(
                // %-25s = string alinhada à esquerda, ocupando 25 posições
                // %2d    = inteiro ocupando 2 posições
                // %.2f   = número decimal com 2 casas depois da vírgula
                "[%s] %-25s (%-8s) - %-12s - prazo: %2d dias - multa: R$ %.2f/dia",
                codigo,
                titulo,
                getClass().getSimpleName(),               // "Livro" ou "Revista"
                disponivel ? "disponível" : "emprestado",  // operador ternário: condição ? seVerdadeiro : seFalso
                prazoEmprestimo(),                         // chama a versão da subclasse
                multaPorDia());                            // idem
    }
}
