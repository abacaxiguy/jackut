package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioJaAmigoException extends RuntimeException {
    public UsuarioJaAmigoException() {
        super("Usuário já está adicionado como amigo.");
    }
    
}
