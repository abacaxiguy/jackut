package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

/**
 * <p> Exceção que indica que a comunidade não existe. </p>
 */

public class ComunidadeNaoExisteException extends RuntimeException {
    public ComunidadeNaoExisteException() {
        super("Comunidade não existe.");
    }
}
