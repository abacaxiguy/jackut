package br.ufal.ic.p2.jackut.models;


/**
 * <p> Classe que representa uma mensagem. </p>
 */

public class Mensagem {
    private final String mensagem;

    /**
     * <p> Constrói uma nova {@code Mensagem} do Jackut. </p>
     *
     * @param mensagem Mensagem
     */

    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * <p> Retorna a mensagem. </p>
     *
     * @return Mensagem
     */

    public String getMensagem() {
        return this.mensagem;
    }
}
