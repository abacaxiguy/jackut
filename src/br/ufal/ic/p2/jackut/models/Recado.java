package br.ufal.ic.p2.jackut.models;

/**
 * <p> Classe que representa um recado. </p>
 */

public class Recado {
    private final Usuario remetente;
    private final Usuario destinatario;
    private final String recado;

    /**
     * <p> Constrói um novo {@code Recado} enviado de um {@code Usuario} para outro. </p>
     *
     * @param remetente     Remetente do recado.
     * @param destinatario  Destinatário do recado.
     * @param recado        Recado.
     *
     * @see Usuario
     */

    public Recado(Usuario remetente, Usuario destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    /**
     * <p> Retorna o remetente do recado. </p>
     *
     * @return Remetente do recado.
     * 
     * @see Usuario
     */

    public Usuario getRemetente() {
        return this.remetente;
    }

    /**
     * <p> Retorna o destinatário do recado. </p>
     *
     * @return Destinatário do recado.
     *
     * @see Usuario
     */

    public Usuario getDestinatario() {
        return this.destinatario;
    }

    /**
     * <p> Retorna o recado. </p>
     *
     * @return Recado.
     */

    public String getRecado() {
        return this.recado;
    }
}
