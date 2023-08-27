package br.ufal.ic.p2.jackut.Exceptions;

/**
 * <p> Exceção que indica que o usuário não pode adicionar a si mesmo como amigo. </p>
 */

public class UsuarioAutoAdicaoAmigoException extends RuntimeException {
    public UsuarioAutoAdicaoAmigoException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
    
}
