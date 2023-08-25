package br.ufal.ic.p2.jackut;

import java.util.ArrayList;

public class Usuario {
    private String login;
    private String senha;
    private String nome;
    private Perfil perfil;
    private ArrayList<Usuario> amigos;
    private ArrayList<Usuario> solicitacoesEnviadas;
    private ArrayList<Usuario> solicitacoesRecebidas;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = new Perfil();
        this.amigos = new ArrayList<>();
        this.solicitacoesEnviadas = new ArrayList<>();
        this.solicitacoesRecebidas = new ArrayList<>();
    }

    public String getLogin() {
        return this.login;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getNome() {
        return this.nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void enviarSolicitacao(Usuario usuario) {
        this.solicitacoesEnviadas.add(usuario);
        usuario.solicitacoesRecebidas.add(this);
    }

    public void aceitarSolicitacao(Usuario usuario) {
        this.amigos.add(usuario);
        this.solicitacoesRecebidas.remove(usuario);
        usuario.amigos.add(this);
        usuario.solicitacoesEnviadas.remove(this);
    }

    public ArrayList<Usuario> getAmigos() {
        return this.amigos;
    }

    public ArrayList<Usuario> getSolicitacoesEnviadas() {
        return this.solicitacoesEnviadas;
    }

    public ArrayList<Usuario> getSolicitacoesRecebidas() {
        return this.solicitacoesRecebidas;
    }
}