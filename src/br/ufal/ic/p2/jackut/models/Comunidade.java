package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;

import br.ufal.ic.p2.jackut.utils.UtilsString;


/**
 * <p> Classe que representa uma comunidade. </p>
 */

public class Comunidade {
    private final Usuario criador;
    private final String nome;
    private final String descricao;
    private final ArrayList<Usuario> membros = new ArrayList<>();

    /**
     * <p> Constrói uma nova {@code Comunidade} do Jackut. </p>
     * <p> Inicializa a lista de membros com o criador da comunidade. </p>
     *
     * @param criador    Criador da comunidade
     * @param nome       Nome da comunidade
     * @param descricao  Descrição da comunidade
     */

    public Comunidade(Usuario criador, String nome, String descricao) {
        this.criador = criador;
        this.nome = nome;
        this.descricao = descricao;
        this.membros.add(criador);
    }

    /**
     * <p> Retorna o nome da comunidade. </p>
     *
     * @return Nome da comunidade
     */

    public String getNome() {
        return nome;
    }

    /**
     * <p> Retorna a descrição da comunidade. </p>
     *
     * @return Descrição da comunidade
     */

    public String getDescricao() {
        return descricao;
    }

    /**
     * <p> Retorna o criador da comunidade. </p>
     *
     * @return Criador da comunidade
     */

    public Usuario getCriador() {
        return criador;
    }

    /**
     * <p> Retorna a lista de membros da comunidade. </p>
     *
     * @return Lista de membros da comunidade
     */

    public ArrayList<Usuario> getMembros() {
        return membros;
    }

    public String toString() {
        return this.getNome();
    }

    public String getMembrosString() {
        return UtilsString.formatArrayList(membros);
    }

    public void adicionarMembro(Usuario usuario) {
        this.membros.add(usuario);
    }

    public void setMembros(ArrayList<Usuario> membros) {
        this.membros.clear();
        this.membros.addAll(membros);
    }

    public void enviarMensagem(Mensagem mensagem) {
        for (Usuario membro : membros) {
            membro.receberMensagem(mensagem);
        }
    }
    
    public void removerMembro(Usuario membro) {
        this.membros.remove(membro);
    }
}
