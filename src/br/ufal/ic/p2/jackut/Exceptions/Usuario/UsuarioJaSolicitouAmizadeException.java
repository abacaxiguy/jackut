package br.ufal.ic.p2.jackut.Exceptions.Usuario;

/**
 * <p> Exceção que indica que o usuário já solicitou amizade para o outro usuário. </p>
 */

public class UsuarioJaSolicitouAmizadeException extends RuntimeException {
    public UsuarioJaSolicitouAmizadeException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
    
}
