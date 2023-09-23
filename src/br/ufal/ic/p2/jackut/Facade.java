package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;

/**
 * <p> Classe fachada que implementa a interface do sistema Jackut. </p>
 */

public class Facade {
    private final Sistema sistema = new Sistema();

    /**
     * <p> Apaga todos os dados mantidos no sistema. </p>
     *
     * @see Sistema
     */

    public void zerarSistema() {
        this.sistema.zerarSistema();
    }

    /**
     * <p> Cria um usuário com os dados da conta fornecidos. </p>
     *
     * @param login  Login do usuário
     * @param senha  Senha do usuário
     * @param nome   Nome do usuário
     *
     * @throws LoginSenhaInvalidosException  Exceção lançada caso o login ou a senha sejam inválidos
     * @throws ContaJaExisteException        Exceção lançada caso o login já esteja cadastrado
     * 
     * @see Usuario
     */

    public void criarUsuario(String login, String senha, String nome) throws LoginSenhaInvalidosException, ContaJaExisteException {
        Usuario usuario = new Usuario(login, senha, nome);

        this.sistema.setUsuario(usuario);
    }

    /**
     * <p> Abre uma sessão para um usuário com o login e a senha fornecidos,
     * e retorna uma id para esta sessão. </p>
     *
     * @param login  Login do usuário
     * @param senha  Senha do usuário
     * @return       ID da sessão
     *
     * @throws LoginSenhaInvalidosException   Exceção lançada caso o login ou a senha sejam inválidos
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário não esteja cadastrado
     */

    public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException, UsuarioNaoCadastradoException {
        return this.sistema.abrirSessao(login, senha);
    }

    /**
     * <p> Retorna o valor do atributo de um usuário, armazenado em seu perfil. </p>
     *
     * @param login     Login do usuário
     * @param atributo  Atributo a ser retornado
     * @return          Valor do atributo
     *
     * @throws UsuarioNaoCadastradoException   Exceção lançada caso o usuário não esteja cadastrado
     * @throws AtributoNaoPreenchidoException  Exceção lançada caso o atributo não esteja preenchido
     */

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return usuario.getAtributo(atributo);
    }

    /**
     * <p> Modifica o valor de um atributo do perfil de um usuário para o valor especificado. </p>
     * <p> Uma sessão válida <b>(identificada por id)</b> deve estar aberta para o usuário
     * cujo perfil se quer editar </p>
     *
     * @param id        ID da sessão
     * @param atributo  Atributo a ser modificado
     * @param valor     Novo valor do atributo
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     */

    public void editarPerfil(String id, String atributo, String valor)
            throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        usuario.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * <p> Adiciona um amigo ao usuário aberto na sessão especificada através de id. </p>
     *
     * @param id     ID da sessão
     * @param amigo  Login do amigo a ser adicionado
     *
     * @throws UsuarioJaTemRelacaoException        Exceção lançada caso o usuário já seja amigo do usuário aberto na sessão
     * @throws UsuarioNaoCadastradoException       Exceção lançada caso o usuário ou o amigo não estejam cadastrados
     * @throws UsuarioAutoRelacaoException         Exceção lançada caso o usuário tente adicionar a si mesmo como amigo
     * @throws UsuarioJaSolicitouAmizadeException  Exceção lançada caso o usuário já tenha solicitado amizade ao amigo
     */

    public void adicionarAmigo(String id, String amigo) throws UsuarioJaTemRelacaoException, UsuarioNaoCadastradoException,
            UsuarioAutoRelacaoException, UsuarioJaSolicitouAmizadeException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario amigoUsuario = this.sistema.getUsuario(amigo);

        this.sistema.adicionarAmigo(usuario, amigoUsuario);
    }

    /**
     * <p> Retorna true se os dois usuários são amigos. </p>
     *
     * @param login   Login do primeiro usuário
     * @param amigo   Login do segundo usuário
     * @return        Booleano indicando se os usuários são amigos
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso um dos usuários não esteja cadastrado
     */

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);
        Usuario amigoUsuario = this.sistema.getUsuario(amigo);

        return usuario.getAmigos().contains(amigoUsuario);
    }

    /**
     * <p> Retorna a lista de amigos do usuário especificado. </p>
     * <p> O retorno é formatado como uma String no formato: <b>{amigo1,amigo2,amigo3,...}</b> </p>
     *
     * @param login  Login do usuário
     * @return       Lista de amigos do usuário formatada em uma String
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     */

    public String getAmigos(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return usuario.getAmigosString();
    }

    /**
     * <p> Envia o recado especificado ao destinatário especificado. </p>
     * <p> Uma sessão válida <b>(identificada por id)</b> deve estar aberta 
     * para o usuário que deseja enviar o recado. </p>
     *
     * @param id            ID da sessão
     * @param destinatario  Login do destinatário
     * @param recado        Recado a ser enviado
     *
     * @throws UsuarioNaoCadastradoException    Exceção lançada caso o usuário ou o destinatário não estejam cadastrados
     * @throws UsuarioAutoEnvioRecadoException  Exceção lançada caso o usuário tente enviar um recado para si mesmo
     */

    public void enviarRecado(String id, String destinatario, String recado) throws UsuarioNaoCadastradoException, UsuarioAutoEnvioRecadoException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario destinatarioUsuario = this.sistema.getUsuario(destinatario);

        this.sistema.enviarRecado(usuario, destinatarioUsuario, recado);
    }

    /**
     * <p> Retorna o primeiro recado da fila de recados do usuário com a sessão aberta
     * identificada por id. </p>
     *
     * @param id  ID da sessão
     * @return    Primeiro recado da fila de recados do usuário
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws NaoHaRecadosExcpetion          Exceção lançada caso o usuário não tenha recados na fila
     */

    public String lerRecado(String id) throws UsuarioNaoCadastradoException, NaoHaRecadosExcpetion {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        Recado recado = usuario.getRecado();
        return recado.getRecado();
    }

    /**
     * <p> Cria uma comunidade com os dados fornecidos. </p>
     *
     * @param id         ID da sessão
     * @param nome       Nome da comunidade
     * @param descricao  Descrição da comunidade
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeJaExisteException    Exceção lançada caso a comunidade já exista
     */

    public void criarComunidade(String id, String nome, String descricao)
            throws UsuarioNaoCadastradoException, ComunidadeJaExisteException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        sistema.criarComunidade(usuario, nome, descricao);
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getDescricaoComunidade(nome);
    }

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getDonoComunidade(nome);
    }

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getMembrosComunidade(nome);
    }

    public String getComunidades(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return this.sistema.getComunidades(usuario);
    }

    public void adicionarComunidade(String id, String nome)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException, UsuarioJaEstaNaComunidadeException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        this.sistema.adicionarComunidade(usuario, nome);
    }
    
    public String lerMensagem(String id) throws UsuarioNaoCadastradoException, NaoHaMensagensException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        return this.sistema.lerMensagem(usuario);
    }

    public void enviarMensagem(String id, String comunidade, String mensagem)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException {
        this.sistema.getSessaoUsuario(id);
        Comunidade comunidadeAlvo = this.sistema.getComunidade(comunidade);

        this.sistema.enviarMensagem(comunidadeAlvo, mensagem);
    }

    public boolean ehFa(String login, String idolo) {
        Usuario usuario = this.sistema.getUsuario(login);
        Usuario idoloUsuario = this.sistema.getUsuario(idolo);

        return idoloUsuario.getFas().contains(usuario);
    }

    public void adicionarIdolo (String id, String idolo)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario idoloUsuario = this.sistema.getUsuario(idolo);

        this.sistema.adicionarIdolo(usuario, idoloUsuario);
    }

    public String getFas(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return this.sistema.getFas(usuario);
    }

    public boolean ehPaquera(String id, String paquera) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario paqueraUsuario = this.sistema.getUsuario(paquera);

        return usuario.getPaqueras().contains(paqueraUsuario);
    }

    public void adicionarPaquera (String id, String paquera)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario paqueraUsuario = this.sistema.getUsuario(paquera);

        this.sistema.adicionarPaquera(usuario, paqueraUsuario);
    }

    public String getPaqueras(String id) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        return this.sistema.getPaqueras(usuario);
    }

    public void adicionarInimigo(String id, String inimigo)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario inimigoUsuario = this.sistema.getUsuario(inimigo);

        this.sistema.adicionarInimigo(usuario, inimigoUsuario);
    }

    public void removerUsuario(String id) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        this.sistema.removerUsuario(usuario, id);
    }

    /**
     * <p> Grava o cadastro em arquivo e encerra o programa.</p>
     * <p> Atingir o final de um script (final de arquivo) é equivalente 
     * a encontrar este comando. </p>
     * <p> Neste caso, o comando não tem parâmetros, e salvará nesse momento
     * apenas os usuários cadastrados. </p>
     *
     * @throws AtributoNaoPreenchidoException Exceção lançada caso algum atributo não esteja preenchido
     */

    public void encerrarSistema() throws AtributoNaoPreenchidoException {
        this.sistema.encerrarSistema();
    }
}
