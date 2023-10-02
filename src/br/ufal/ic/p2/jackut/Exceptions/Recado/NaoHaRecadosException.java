package br.ufal.ic.p2.jackut.Exceptions.Recado;


/**
 * <p> Exceção que indica que não há recados para serem lidos. </p>
 */

public class NaoHaRecadosException extends RuntimeException {
    public NaoHaRecadosException() {
        super("Não há recados.");
    }
}
