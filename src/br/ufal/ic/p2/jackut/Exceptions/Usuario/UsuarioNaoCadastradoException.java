package br.ufal.ic.p2.jackut.Exceptions.Usuario;

/**
 * <p> Exceção que indica que o usuário não está cadastrado no sistema. </p>
 */

public class UsuarioNaoCadastradoException extends RuntimeException {
    public UsuarioNaoCadastradoException() {
        super("Usuário não cadastrado.");
    }
}
