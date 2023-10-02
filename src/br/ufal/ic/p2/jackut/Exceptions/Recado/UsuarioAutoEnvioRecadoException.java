package br.ufal.ic.p2.jackut.Exceptions.Recado;


/**
 * <p> Exceção que indica que o usuário não pode enviar recado para si mesmo. </p>
 */

public class UsuarioAutoEnvioRecadoException extends RuntimeException {
    public UsuarioAutoEnvioRecadoException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
    
}
