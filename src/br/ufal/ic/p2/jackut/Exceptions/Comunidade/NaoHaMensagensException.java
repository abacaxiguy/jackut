package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

/**
 * <p> Excecao para quando nao ha mensagens em uma comunidade. </p>
 */

public class NaoHaMensagensException extends Exception {
    public NaoHaMensagensException() {
        super("Não há mensagens.");
    }
}
