package br.ufal.ic.p2.jackut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.p2.jackut.Exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.LoginSenhaInvalidosException;
import br.ufal.ic.p2.jackut.Exceptions.NaoHaRecadosExcpetion;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioAutoAdicaoAmigoException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioAutoEnvioRecadoException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaAmigoException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaSolicitouAmizadeException;

/**
 * <p> Classe que representa um usuário. </p>
 */

public class Usuario {
    private final String login;
    private final String senha;
    private final String nome;
    private final Perfil perfil;
    private final ArrayList<Usuario> amigos;
    private final ArrayList<Usuario> solicitacoesEnviadas;
    private final ArrayList<Usuario> solicitacoesRecebidas;
    private final Queue<Recado> recados;

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
        this.perfil = new Perfil();
        this.amigos = new ArrayList<>();
        this.solicitacoesEnviadas = new ArrayList<>();
        this.solicitacoesRecebidas = new ArrayList<>();
        this.recados = new LinkedList<>();
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
     * <p> Adiciona um amigo ao usuário, caso ele não seja amigo, já tenha solicitado amizade ou já tenha recebido uma solicitação de amizade do usuário. </p>
     *
     * @param amigo  Login do amigo a ser adicionado
     *
     * @throws UsuarioJaAmigoException             Exceção lançada caso o usuário já seja amigo do usuário aberto na sessão
     * @throws UsuarioAutoAdicaoAmigoException     Exceção lançada caso o usuário tente adicionar a si mesmo como amigo
     * @throws UsuarioJaSolicitouAmizadeException  Exceção lançada caso o usuário já tenha solicitado amizade ao amigo
     */
    public void adicionarAmigo(Usuario amigo) throws UsuarioJaAmigoException, UsuarioAutoAdicaoAmigoException, UsuarioJaSolicitouAmizadeException {
        if (this.getLogin().equals(amigo.getLogin())) {
            throw new UsuarioAutoAdicaoAmigoException();
        }

        if (this.getAmigos().contains(amigo) || amigo.getAmigos().contains(this)) {
            throw new UsuarioJaAmigoException();
        }

        if (this.getSolicitacoesEnviadas().contains(amigo)) {
            throw new UsuarioJaSolicitouAmizadeException();
        } else if (this.getSolicitacoesRecebidas().contains(amigo)) {
            this.aceitarSolicitacao(amigo);
        } else {
            this.enviarSolicitacao(amigo);
        }
    }

    /**
     * <p> Retorna a lista de amigos do usuário especificado. </p>
     * <p> O retorno é formatado como uma String no formato: <b>{amigo1,amigo2,amigo3,...}</b> </p>
     *
     * @return Lista de amigos do usuário formatada em uma String
     */

    public String getAmigosString() {
        String amigos = "{";
        for (int i = 0; i < this.amigos.size(); i++) {
            Usuario amigo = this.amigos.get(i);
            amigos += amigo.getLogin();
            if (i < this.amigos.size() - 1) {
                amigos += ",";
            }
        }

        amigos += "}";
        return amigos;
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
     * <p> Envia um recado para o usuário passado como parâmetro. </p>
     *
     * @param destinatario  Usuário para o qual o recado será enviado.
     * @param recado        Recado a ser enviado.
     *
     * @throws UsuarioAutoEnvioRecadoException Exceção lançada caso o usuário tente enviar um recado para si mesmo.
     */

    public void enviarRecado(Usuario destinatario, String recado) throws UsuarioAutoEnvioRecadoException {
        if (this.getLogin().equals(destinatario.getLogin())) {
            throw new UsuarioAutoEnvioRecadoException();
        }

        Recado r = new Recado(this, destinatario, recado);
        destinatario.recados.add(r);
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
}