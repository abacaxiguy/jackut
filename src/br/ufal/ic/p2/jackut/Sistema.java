package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p> Classe que representa o sistema. </p>
 */

public class Sistema {
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> usuariosLogados;

    /**
     * <p> Constrói um novo {@code Sistema} responsável por
     * gerenciar os usuários e as sessões. </p>
     *
     * <p> Inicializa as listas de usuários e usuários logados. </p>
     *
     * @see Usuario
     */
    public Sistema() {
        this.usuarios = new HashMap<>();
        this.usuariosLogados = new HashMap<>();
    }

    /**
     * <p> Adiciona um usuário ao sistema. </p>
     *
     * @param usuario Usuário a ser adicionado.
     *
     * @see Usuario
     */

    public void setUsuario(Usuario usuario) {
        this.usuarios.put(usuario.getLogin(), usuario);
    }

    /**
     * <p> Adiciona um usuário logado ao sistema. </p>
     *
     * @param usuario Usuário a ser adicionado.
     *
     * @see Usuario
     */

    public void setUsuarioLogado(Usuario usuario) {
        String id = UUID.randomUUID().toString();
        this.usuariosLogados.put(id, usuario);
    }

    /**
     * <p> Retorna um usuário do sistema pelo login. </p>
     *
     * @param login Login do usuário.
     *
     * @see Usuario
     */

    public Usuario getUsuario(String login) {
        return this.usuarios.get(login);
    }

    /**
     * <p> Retorna um usuário logado do sistema pelo id. </p>
     *
     * @param id ID da sessão do usuário.
     *
     * @see Usuario
     */

    public Usuario getUsuarioLogado(String id) {
        return this.usuariosLogados.get(id);
    }

    /**
     * <p> Retorna todos os usuários do sistema. </p>
     *
     * @return Map com todos os usuários do sistema.
     *
     * @see Usuario
     */

    public Map<String, Usuario> getUsuarios() {
        return this.usuarios;
    }

    /**
     * <p> Retorna o ID da sessão de um usuário. </p>
     *
     * @param usuario  Usuário a ser buscado.
     * @return         ID da sessão do usuário.
     */

    public String getSessao(Usuario usuario) {
        for (Map.Entry<String, Usuario> entry : this.usuariosLogados.entrySet()) {
            if (entry.getValue().equals(usuario)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * <p> Zera as informações do sistema. </p>
     */

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.usuariosLogados = new HashMap<>();
    }
}
