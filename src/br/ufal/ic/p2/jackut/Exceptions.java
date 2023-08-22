package br.ufal.ic.p2.jackut;

public class Exceptions {
    public class UsuarioNaoCadastradoException extends RuntimeException {
        public UsuarioNaoCadastradoException() {
            super("Usuário não cadastrado");
        }
    }
}
