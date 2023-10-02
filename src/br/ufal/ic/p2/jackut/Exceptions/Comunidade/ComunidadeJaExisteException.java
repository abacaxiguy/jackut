package br.ufal.ic.p2.jackut.Exceptions.Comunidade;


/**
 * <p> Exceção que indica que a comunidade não existe. </p>
 */

public class ComunidadeJaExisteException extends RuntimeException {
    public ComunidadeJaExisteException() {
        super("Comunidade com esse nome já existe.");
    }
}
