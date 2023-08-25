package br.ufal.ic.p2.jackut;

public class Usuario {
    private String login;
    private String senha;
    private String nome;
    private Perfil perfil;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = new Perfil();
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
}