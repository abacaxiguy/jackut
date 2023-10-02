package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

/**
 * <p> Exceção que indica que o usuário já faz parte da comunidade. </p>
 */

public class UsuarioJaEstaNaComunidadeException extends RuntimeException {
    public UsuarioJaEstaNaComunidadeException() {
        super("Usuario já faz parte dessa comunidade.");
    }
    
}
