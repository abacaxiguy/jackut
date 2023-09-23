package br.ufal.ic.p2.jackut.Exceptions.Recado;

/**
 * <p> Exceção que indica que não há recados para serem lidos. </p>
 */

public class NaoHaRecadosExcpetion extends RuntimeException {
    public NaoHaRecadosExcpetion() {
        super("Não há recados.");
    }
}
