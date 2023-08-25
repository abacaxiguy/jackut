package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioNaoCadastradoException extends RuntimeException {
    public UsuarioNaoCadastradoException() {
        super("Usuário não cadastrado.");
    }
}
