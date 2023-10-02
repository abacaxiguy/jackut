package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.NaoHaMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.NaoHaRecadosException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginSenhaInvalidosException;

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
     * <p> Adiciona o usuário à lista de solicitações enviadas e adiciona o usuário atual à lista de solicitações recebidas do usuário passado como parâmetro. </p>
     *
     * @param usuario Usuário para o qual a solicitação será enviada.
     */

    public void enviarSolicitacao(Usuario usuario) {
        this.solicitacoesEnviadas.add(usuario);
        usuario.solicitacoesRecebidas.add(this);
    }

    /**
     * <p> Aceita uma solicitação de amizade. </p>
     * <p> Adiciona o usuário à lista de amigos e remove o usuário da lista de solicitações recebidas. </p>
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
     *
     * @return Lista de amigos do usuário formatada em uma String
     *
     * @see UtilsString
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
     * @throws NaoHaRecadosException Exceção lançada caso o usuário não tenha recados na fila.
     */

    public Recado lerRecado() throws NaoHaRecadosException {
        if (this.recados.isEmpty()) {
            throw new NaoHaRecadosException();
        }

        return this.recados.poll();
    }

    /**
     * <p> Adiciona um recado à fila de recados do usuário. </p>
     *
     * @param recado Recado a ser adicionado.
     */

    public void receberRecado(Recado recado) {
        this.recados.add(recado);
    }

    /**
     * <p> Adiciona um amigo ao usuário. </p>
     * <p><b>AVISO:</b> Método utilizado apenas para carregar os dados do arquivo. </p>
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

    /**
     * <p> Adiciona uma comunidade à lista de comunidades em que o usuário é o dono. </p>
     *
     * @param comunidade Comunidade a ser adicionada.
     */

    public void setDonoComunidade(Comunidade comunidade) {
        if (this.comunidadesProprietarias.contains(comunidade)) {
            return;
        }

        this.comunidadesProprietarias.add(comunidade);
    }

    /**
     * <p> Adiciona uma comunidade à lista de comunidades em que o usuário é participante. </p>
     *
     * @param comunidade Comunidade a ser adicionada.
     */

    public void setParticipanteComunidade(Comunidade comunidade) {
        if (this.comunidadesParticipantes.contains(comunidade)) {
            return;
        }

        this.comunidadesParticipantes.add(comunidade);
    }

    /**
     * <p> Retorna a lista de comunidades das quais o usuário é dono. </p>
     *
     * @return Lista de comunidades das quais o usuário é dono.
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

    public Mensagem lerMensagem() throws NaoHaMensagensException {
        if (this.mensagens.isEmpty()) {
            throw new NaoHaMensagensException();
        }

        return this.mensagens.poll();
    }

    /**
     * <p> Retorna a fila de mensagens do usuário. </p>
     *
     * @return Fila de mensagens do usuário.
     *
     * @see Mensagem
     */

    public Queue<Mensagem> getMensagens() {
        return this.mensagens;
    }

    /**
     * <p> Adiciona um usuário à lista de ídolos do usuário. </p>
     *
     * @param idolo Ídolo a ser adicionado.
     */

    public void setIdolo(Usuario idolo) {
        this.idolos.add(idolo);
    }

    /**
     * <p> Adiciona um usuário à lista de fãs do usuário. </p>
     *
     * @param fa Fã a ser adicionado.
     */

    public void setFa(Usuario fa) {
        this.fas.add(fa);
    }

    /**
     * <p> Retorna a lista de ídolos do usuário. </p>
     *
     * @return Lista de ídolos do usuário.
     */

    public ArrayList<Usuario> getIdolos() {
        return this.idolos;
    }

    /**
     * <p> Retorna a lista de fãs do usuário. </p>
     *
     * @return Lista de fãs do usuário.
     */

    public ArrayList<Usuario> getFas() {
        return this.fas;
    }

    /**
     * <p> Retorna a lista de ídolos do usuário formatada como uma String. </p>
     *
     * @return Lista de ídolos do usuário formatada em uma String
     *
     * @see UtilsString
     */

    public String getFasString() {
        return UtilsString.formatArrayList(this.fas);
    }

    /**
     * <p> Adiciona um usuário à lista de paqueras do usuário. </p>
     *
     * @param paquera Paquera a ser adicionado.
     */

    public void setPaquera(Usuario paquera) {
        this.paqueras.add(paquera);
    }

    /**
     * <p> Adiciona um usuário à lista de paqueras recebidas do usuário. </p>
     *
     * @param usuario Usuário a ser adicionado.
     */

    public void setPaquerasRecebidas(Usuario usuario) {
        this.paquerasRecebidas.add(usuario);
    }

    /**
     * <p> Retorna a lista de paqueras do usuário. </p>
     *
     * @return Lista de paqueras do usuário.
     */

    public ArrayList<Usuario> getPaqueras() {
        return this.paqueras;
    }

    /**
     * <p> Retorna a lista de paqueras recebidas do usuário. </p>
     *
     * @return Lista de paqueras recebidas do usuário.
     */

    public ArrayList<Usuario> getPaquerasRecebidas() {
        return this.paquerasRecebidas;
    }

    /**
     * <p> Retorna a lista de paqueras do usuário formatada como uma String. </p>
     *
     * @return Lista de paqueras do usuário formatada em uma String
     *
     * @see UtilsString
     */

    public String getPaquerasString() {
        return UtilsString.formatArrayList(this.paqueras);
    }

    /**
     * <p> Adiciona um usuário à lista de inimigos do usuário. </p>
     *
     * @param inimigo Inimigo a ser adicionado.
     */

    public void setInimigo(Usuario inimigo) {
        this.inimigos.add(inimigo);
    }

    /**
     * <p> Retorna a lista de inimigos do usuário. </p>
     *
     * @return Lista de inimigos do usuário.
     */

    public ArrayList<Usuario> getInimigos() {
        return this.inimigos;
    }

    /**
     * <p> Remove uma comunidade da lista de comunidades em que o usuário participa. </p>
     *
     * @param comunidade Comunidade em que o usuário não participa mais.
     */

    public void sairComunidade(Comunidade comunidade) {
        this.comunidadesParticipantes.remove(comunidade);
    }

    /**
     * <p> Remove um recado da fila de recados do usuário. </p>
     *
     * @param recado Recado a ser removido.
     */

    public void removerRecado(Recado recado) {
        this.recados.remove(recado);
    }

    /**
     * <p> Remove um amigo da lista de amigos do usuário. </p>
     *
     * @param usuario Usuário que não é mais amigo.
     */

    public void removerAmigo(Usuario usuario) {
        this.amigos.remove(usuario);
    }

    /**
     * <p> Remove um fã da lista de fãs do usuário. </p>
     *
     * @param usuario Usuário que não é mais ídolo.
     */

    public void removerFa(Usuario usuario) {
        this.fas.remove(usuario);
    }

    /**
     * <p> Remove um ídolo da lista de ídolos do usuário. </p>
     *
     * @param usuario Usuário que não é mais ídolo.
     */

    public void removerIdolo(Usuario usuario) {
        this.idolos.remove(usuario);
    }

    /**
     * <p> Remove uma paquera da lista de paqueras do usuário. </p>
     *
     * @param usuario Usuário que não é mais paquera.
     */

    public void removerPaquera(Usuario usuario) {
        this.paqueras.remove(usuario);
    }

    /**
     * <p> Remove uma paquera da lista de paqueras recebidas do usuário. </p>
     *
     * @param usuario Usuário que não é mais paquera.
     */

    public void removerPaqueraRecebida(Usuario usuario) {
        this.paquerasRecebidas.remove(usuario);
    }

    /**
     * <p> Remove um inimigo da lista de inimigos do usuário. </p>
     *
     * @param usuario Usuário que não é mais inimigo.
     */

    public void removerInimigo(Usuario usuario) {
        this.inimigos.remove(usuario);
    }

    /**
     * <p> Remove uma solicitação de amizade da lista de solicitações enviadas do usuário. </p>
     *
     * @param usuario Usuário que não receberá mais a solicitação.
     */

    public void removerSolicitacaoEnviada(Usuario usuario) {
        this.solicitacoesEnviadas.remove(usuario);
    }

    /**
     * <p> Remove uma solicitação de amizade da lista de solicitações recebidas do usuário. </p>
     *
     * @param usuario Usuário que não receberá mais a solicitação.
     */

    public void removerSolicitacaoRecebida(Usuario usuario) {
        this.solicitacoesRecebidas.remove(usuario);
    }

    /**
     * <p> Retorna uma {@code String} que representa o usuário. </p>
     * <p> A representação segue o formato: {@code login} </p>
     *
     * @return String que representa o usuário.
     */

    @Override
    public String toString() {
        return this.getLogin();
    }
}