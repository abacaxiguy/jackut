package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.NaoHaMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.NaoHaRecadosExcpetion;
import br.ufal.ic.p2.jackut.Exceptions.Recado.UsuarioAutoEnvioRecadoException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginSenhaInvalidosException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.utils.UtilsString;

/**
 * <p> Classe que representa um usuário. </p>
 */

public class Usuario {
    private final String login;
    private final String senha;
    private final String nome;

    private final Perfil perfil = new Perfil();

    private final ArrayList<Usuario> amigos = new ArrayList<>();
    private final ArrayList<Usuario> solicitacoesEnviadas = new ArrayList<>();
    private final ArrayList<Usuario> solicitacoesRecebidas = new ArrayList<>();

    private final Queue<Recado> recados = new LinkedList<>();

    private final ArrayList<Comunidade> comunidadesProprietarias = new ArrayList<>();
    private final ArrayList<Comunidade> comunidadesParticipantes = new ArrayList<>();
    private final Queue<Mensagem> mensagens = new LinkedList<>();

    private final ArrayList<Usuario> idolos = new ArrayList<>();
    private final ArrayList<Usuario> fas = new ArrayList<>();

    private final ArrayList<Usuario> paqueras = new ArrayList<>();
    private final ArrayList<Usuario> paquerasRecebidas = new ArrayList<>();

    private final ArrayList<Usuario> inimigos = new ArrayList<>();

    /**
     * <p> Constrói um novo {@code Usuario} do Jackut. </p>
     * <p> Inicializa um {@code Perfil} para o usuário, as listas de amigos, solicitações enviadas e solicitações recebidas. </p>
     *
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @param nome Nome do usuário.
     *
     * @throws LoginSenhaInvalidosException Exceção lançada caso o login ou a senha sejam inválidos.
     *
     * @see Perfil
     */

    public Usuario(String login, String senha, String nome) throws LoginSenhaInvalidosException {
        if (login == null) {
            throw new LoginSenhaInvalidosException("login");
        }

        if (senha == null) {
            throw new LoginSenhaInvalidosException("senha");
        }

        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    /**
     * <p> Retorna o login do usuário. </p>
     *
     * @return Login do usuário.
     */

    public String getLogin() {
        return this.login;
    }

    public String toString() {
        return this.getLogin();
    }

    /**
     * <p> Verifica se a senha passada como parâmetro é igual à senha do usuário. </p>
     *
     * @param senha  Senha a ser verificada.
     * @return       True se a senha for igual, false caso contrário.
     */

    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * <p> Retorna a senha do usuário. </p>
     *
     * @return Senha do usuário.
     */

    public String getSenha() {
        return this.senha;
    }

    /**
     * <p> Retorna o nome do usuário. </p>
     *
     * @return Nome do usuário.
     */

    public String getNome() {
        return this.nome;
    }

    /**
     * <p> Retorna o perfil do usuário. </p>
     *
     * @return Perfil do usuário.
     *
     * @see Perfil
     */

    public Perfil getPerfil() {
        return this.perfil;
    }

    /**
     * <p> Retorna o valor de um atributo do usuário. </p>
     * <p> Caso o atributo seja "nome", retorna o nome do usuário. </p>
     *
     * @param atributo  Atributo a ser retornado.
     * @return          Valor do atributo.
     *
     * @throws AtributoNaoPreenchidoException Exceção lançada caso o atributo não tenha sido preenchido.
     */

    public String getAtributo(String atributo) throws AtributoNaoPreenchidoException {
        if (atributo.equals("nome")) {
            return this.getNome();
        } else {
            return this.getPerfil().getAtributo(atributo);
        }
    }

    /**
     * <p> Envia uma solicitação de amizade para o usuário passado como parâmetro. </p>
     *
     * @param usuario Usuário para o qual a solicitação será enviada.
     */

    public void enviarSolicitacao(Usuario usuario) {
        this.solicitacoesEnviadas.add(usuario);
        usuario.solicitacoesRecebidas.add(this);
    }

    /**
     * <p> Aceita uma solicitação de amizade. </p>
     *
     * @param usuario Usuário que enviou a solicitação.
     */

    public void aceitarSolicitacao(Usuario usuario) {
        this.amigos.add(usuario);
        this.solicitacoesRecebidas.remove(usuario);
        usuario.amigos.add(this);
        usuario.solicitacoesEnviadas.remove(this);
    }

    /**
     * <p> Retornar a lista de amigos do usuário. </p>
     *
     * @return Lista de amigos do usuário.
     */

    public ArrayList<Usuario> getAmigos() {
        return this.amigos;
    }

    /**
     * <p> Retorna a lista de amigos do usuário especificado. </p>
     * <p> O retorno é formatado como uma String no formato: <b>{amigo1,amigo2,amigo3,...}</b> </p>
     *
     * @return Lista de amigos do usuário formatada em uma String
     */

    public String getAmigosString() {
        return UtilsString.formatArrayList(this.amigos);
    }

    /**
     * <p> Retorna a lista de solicitações de amizade enviadas pelo usuário. </p>
     *
     * @return Lista de solicitações de amizade enviadas pelo usuário
     */

    public ArrayList<Usuario> getSolicitacoesEnviadas() {
        return this.solicitacoesEnviadas;
    }

    /**
     * <p> Retorna a lista de solicitações de amizade recebidas pelo usuário. </p>
     *
     * @return Lista de solicitações de amizade recebidas pelo usuário.
     */

    public ArrayList<Usuario> getSolicitacoesRecebidas() {
        return this.solicitacoesRecebidas;
    }

    /**
     * <p> Retorna o recado mais antigo do usuário. </p>
     *
     * @return Recado mais antigo do usuário.
     *
     * @throws NaoHaRecadosExcpetion Exceção lançada caso o usuário não tenha recados na fila.
     */

    public Recado getRecado() throws NaoHaRecadosExcpetion {
        if (this.recados.isEmpty()) {
            throw new NaoHaRecadosExcpetion();
        }

        return this.recados.poll();
    }

    public void receberRecado(Recado recado) {
        this.recados.add(recado);
    }

    /**
     * <p> Adiciona um amigo ao usuário. </p>
     * <p> Método utilizado para carregar os dados do arquivo. Não deve ser utilizado para adicionar amigos. </p>
     *
     * @param amigo Amigo a ser adicionado.
     */

    public void setAmigo(Usuario amigo) {
        if (!this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
        }
    }

    /**
     * <p> Retorna a lista de recados não lidos do usuário. </p>
     *
     * @return Lista de recados não lidos do usuário.
     *
     * @see Recado
     */

    public Queue<Recado> getRecados() {
        return this.recados;
    }

    public void setCriadorComunidade(Comunidade comunidade) {
        this.comunidadesProprietarias.add(comunidade);
    }

    public void setParticipanteComunidade(Comunidade comunidade) {
        this.comunidadesParticipantes.add(comunidade);
    }

    /**
     * <p> Retorna a lista de comunidades das quais o usuário é criador. </p>
     *
     * @return Lista de comunidades das quais o usuário é criador.
     *
     * @see Comunidade
     */
    public ArrayList<Comunidade> getComunidadesProprietarias() {
        return this.comunidadesProprietarias;
    }

    /**
     * <p> Retorna a lista de comunidades das quais o usuário é participante. </p>
     *
     * @return Lista de comunidades das quais o usuário é participante.
     *
     * @see Comunidade
     */
    public ArrayList<Comunidade> getComunidadesParticipantes() {
        return this.comunidadesParticipantes;
    }

    /**
     * <p> Recebe uma mensagem de uma comunidade. </p>
     *
     * @param mensagem Mensagem a ser recebida.
     */

    public void receberMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }

    /**
     * <p> Le uma mensagem da fila de mensagens do usuário. </p>
     *
     * @return Mensagem lida.
     *
     * @throws NaoHaMensagensException Exceção lançada caso o usuário não tenha mensagens na fila.
     */

    public String lerMensagem() throws NaoHaMensagensException {
        if (this.mensagens.isEmpty()) {
            throw new NaoHaMensagensException();
        }

        return this.mensagens.poll().getMensagem();
    }

    public Queue<Mensagem> getMensagens() {
        return this.mensagens;
    }

    public void setIdolo(Usuario usuario) {
        this.idolos.add(usuario);
    }

    public void setFa(Usuario usuario) {
        this.fas.add(usuario);
    }

    public ArrayList<Usuario> getIdolos() {
        return this.idolos;
    }

    public ArrayList<Usuario> getFas() {
        return this.fas;
    }

    public String getFasString() {
        return UtilsString.formatArrayList(this.fas);
    }

    public void setPaquera(Usuario usuario) {
        this.paqueras.add(usuario);
    }

    public void setPaquerasRecebidas(Usuario usuario) {
        this.paquerasRecebidas.add(usuario);
    }

    public ArrayList<Usuario> getPaqueras() {
        return this.paqueras;
    }

    public ArrayList<Usuario> getPaquerasRecebidas() {
        return this.paquerasRecebidas;
    }

    public String getPaquerasString() {
        return UtilsString.formatArrayList(this.paqueras);
    }

    public void setInimigo(Usuario usuario) {
        this.inimigos.add(usuario);
    }

    public ArrayList<Usuario> getInimigos() {
        return this.inimigos;
    }

    public void removerAmigo(Usuario usuario) {
        this.amigos.remove(usuario);
    }

    public void removerSolicitacaoEnviada(Usuario usuario) {
        this.solicitacoesEnviadas.remove(usuario);
    }

    public void removerSolicitacaoRecebida(Usuario usuario) {
        this.solicitacoesRecebidas.remove(usuario);
    }

    public void removerFa(Usuario usuario) {
        this.fas.remove(usuario);
    }

    public void removerPaquera(Usuario usuario) {
        this.paqueras.remove(usuario);
    }

    public void removerPaqueraRecebida(Usuario usuario) {
        this.paquerasRecebidas.remove(usuario);
    }

    public void removerInimigo(Usuario usuario) {
        this.inimigos.remove(usuario);
    }

    public void removerIdolo(Usuario usuario) {
        this.idolos.remove(usuario);
    }

    public void removerComunidade(Comunidade comunidade) {
        this.comunidadesParticipantes.remove(comunidade);
    }

    public void removerRecado(Recado recado) {
        this.recados.remove(recado);
    }
}