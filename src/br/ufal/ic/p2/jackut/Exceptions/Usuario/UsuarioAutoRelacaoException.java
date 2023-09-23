package br.ufal.ic.p2.jackut.Exceptions.Usuario;

public class UsuarioAutoRelacaoException extends RuntimeException {
    /**
     * <p>
     * Retorna uma exceção de auto adição de relação dependendo do tipo de relação.
     * </p>
     * <p>
     * O parâmetro <code>relacao</code> deve ser uma das seguintes relações:
     * <ul>
     * <li> Amizade </li>
     * <li> Fã </li>
     * <li> Paquera </li>
     * <li> Inimigo </li>
     * </ul>
     * </p>
     *
     * @param relacao Relação que está sendo adicionada.
     */
    public UsuarioAutoRelacaoException(String relacao) {
        super(
                relacao.toLowerCase().equals("amizade")
                        ? 
                "Usuário não pode adicionar a si mesmo como amigo."
                        : 
                "Usuário não pode ser " + relacao.toLowerCase() + " de si mesmo."
        );
    }
}
