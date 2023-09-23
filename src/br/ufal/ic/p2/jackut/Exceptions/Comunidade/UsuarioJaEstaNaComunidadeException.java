package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class UsuarioJaEstaNaComunidadeException extends RuntimeException {
    public UsuarioJaEstaNaComunidadeException() {
        super("Usuario já faz parte dessa comunidade.");
    }
    
}
