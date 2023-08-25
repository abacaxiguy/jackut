package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioAutoAdicaoAmigoException extends RuntimeException {
    public UsuarioAutoAdicaoAmigoException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
    
}
