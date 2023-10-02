package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;

import br.ufal.ic.p2.jackut.utils.UtilsString;


/**
 * <p> Classe que representa uma comunidade. </p>
 */

public class Comunidade {
    private final Usuario dono;
    private final String nome;
    private final String descricao;
    private final ArrayList<Usuario> membros = new ArrayList<>();

    /**
     * <p> Constrói uma nova {@code Comunidade} do Jackut. </p>
     * <p> Inicializa a lista de membros com o dono da comunidade. </p>
     *
     * @param dono    Dono da comunidade
     * @param nome       Nome da comunidade
     * @param descricao  Descrição da comunidade
     */

    public Comunidade(Usuario dono, String nome, String descricao) {
        this.dono = dono;
        this.nome = nome;
        this.descricao = descricao;
        this.membros.add(dono);
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
     * <p> Retorna o dono da comunidade. </p>
     *
     * @return Dono da comunidade
     */

    public Usuario getDono() {
        return dono;
    }

    /**
     * <p> Retorna a lista de membros da comunidade. </p>
     *
     * @return Lista de membros da comunidade
     */

    public ArrayList<Usuario> getMembros() {
        return membros;
    }

    /**
     * <p> Retorna a lista de membros do usuário formatada como uma {@code String}. </p>
     *
     * @return Lista de ídolos do usuário formatada em uma string
     *
     * @see UtilsString
     */

    public String getMembrosString() {
        return UtilsString.formatArrayList(membros);
    }

    /**
     * <p> Adiciona um membro à comunidade. </p>
     *
     * @param usuario Usuário a ser adicionado
     */

    public void adicionarMembro(Usuario usuario) {
        this.membros.add(usuario);
    }

    /**
     * <p> Adiciona uma lista de membros à comunidade. </p>
     * <p> A lista de membros atual é substituída pela nova lista. </p>
     * <p><b>AVISO:</b> Método utilizado apenas para carregar os dados do arquivo. </p>
     *
     * @param membros Lista de membros a serem adicionados
     */

    public void setMembros(ArrayList<Usuario> membros) {
        this.membros.clear();
        this.membros.addAll(membros);
    }

    /**
     * <p> Envia uma mensagem para todos os membros da comunidade. </p>
     *
     * @param mensagem Mensagem a ser enviada
     */

    public void enviarMensagem(Mensagem mensagem) {
        for (Usuario membro : membros) {
            membro.receberMensagem(mensagem);
        }
    }

    /**
     * <p> Retorna uma {@code String} que representa a comunidade. </p>
     * <p> A representação segue o formato: {@code nome}. </p>
     *
     * @return String que representa a comunidade.
     */

    @Override
    public String toString() {
        return this.getNome();
    }
}
