package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioAutoEnvioRecadoException extends RuntimeException {
    public UsuarioAutoEnvioRecadoException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
    
}
