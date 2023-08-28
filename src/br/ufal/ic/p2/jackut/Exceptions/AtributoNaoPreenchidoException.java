package br.ufal.ic.p2.jackut.Exceptions;

/**
 * <p> Exceção que indica que um atributo não foi preenchido. </p>
 */

public class AtributoNaoPreenchidoException extends RuntimeException {
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}
