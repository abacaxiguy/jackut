package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioJaSolicitouAmizadeException extends RuntimeException {
    public UsuarioJaSolicitouAmizadeException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
    
}
