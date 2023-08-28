package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p> Classe que representa o sistema. </p>
 */

public class Sistema {
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> sessoes;

    /**
     * <p> Constrói um novo {@code Sistema} responsável por
     * gerenciar os usuários e as sessões. </p>
     *
     * <p> Inicializa as listas de usuários e sessões. </p>
     *
     * @see Usuario
     */

    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
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
     * <p> Cria uma sessão para um usuário logado. </p>
     *
     * @param usuario Usuário a ser adicionado.
     *
     * @see Usuario
     */

    public void setSessaoUsuario(Usuario usuario) {
        String id = UUID.randomUUID().toString();
        this.sessoes.put(id, usuario);
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
     * <p> Retorna um usuário logado do sistema pelo id da sua sessão. </p>
     *
     * @param id ID da sessão do usuário.
     *
     * @see Usuario
     */

    public Usuario getSessaoUsuario(String id) {
        return this.sessoes.get(id);
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
        for (Map.Entry<String, Usuario> entry : this.sessoes.entrySet()) {
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
        this.sessoes = new HashMap<>();
    }
}
