package br.ufal.ic.p2.jackut.Exceptions.Usuario;

public class UsuarioJaTemRelacaoException extends Exception {
    /**
     * <p>
     * Retorna uma exceção quando o usuário já tem uma relação com outro usuário.
     * </p>
     * <p>
     * O parâmetro <code>relacao</code> deve ser uma das seguintes relações:
     * <ul>
     * <li> Amigo </li>
     * <li> Ídolo </li>
     * <li> Paquera </li>
     * <li> Inimigo </li>
     * </ul>
     * </p>
     *
     * @param relacao Relação que está sendo adicionada.
     */
    public UsuarioJaTemRelacaoException(String relacao) {
        super("Usuário já está adicionado como " + relacao.toLowerCase() + ".");
    }
}
