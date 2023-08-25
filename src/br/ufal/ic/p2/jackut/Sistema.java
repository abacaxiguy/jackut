package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sistema {
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> usuariosLogados;

    public Sistema() {
        this.usuarios = new HashMap<>();
        this.usuariosLogados = new HashMap<>();
    }

    public void setUsuario(Usuario usuario) {
        this.usuarios.put(usuario.getLogin(), usuario);
    }

    public void setUsuarioLogado(Usuario usuario) {
        String id = UUID.randomUUID().toString();
        this.usuariosLogados.put(id, usuario);
    }

    public Usuario getUsuario(String login) {
        return this.usuarios.get(login);
    }

    public Usuario getUsuarioLogado(String id) {
        return this.usuariosLogados.get(id);
    }

    public Map<String, Usuario> getUsuarios() {
        return this.usuarios;
    }

    public String getSessao(Usuario usuario) {
        for (Map.Entry<String, Usuario> entry : this.usuariosLogados.entrySet()) {
            if (entry.getValue().equals(usuario)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.usuariosLogados = new HashMap<>();
    }
}
