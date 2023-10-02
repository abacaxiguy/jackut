package br.ufal.ic.p2.jackut.Exceptions.Usuario;


/**
 * <p> Exceção para quando o usuário é inimigo do usuário logado. </p>
 */

public class UsuarioEhInimigoException extends Exception {

    /**
     * <p> Exceção para quando o usuário é inimigo do usuário logado. </p>
     * 
     * @param usuario Usuario que é inimigo do usuário logado.
     */

    public UsuarioEhInimigoException(String usuario) {
        super("Função inválida: " + usuario + " é seu inimigo.");
    }
}
