package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.models.*;

import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;

import br.ufal.ic.p2.jackut.utils.*;


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
     *
     * @param login  Login do usuário
     * @return       Lista de amigos do usuário formatada em uma String
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilsString
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
     * @throws NaoHaRecadosException          Exceção lançada caso o usuário não tenha recados na fila
     */

    public String lerRecado(String id) throws UsuarioNaoCadastradoException, NaoHaRecadosException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        return this.sistema.lerRecado(usuario);
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

    /**
     * <p> Retorna a descrição da comunidade especificada. </p>
     *
     * @param nome  Nome da comunidade
     * @return      Descrição da comunidade
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     */

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getDescricaoComunidade(nome);
    }

    /**
     * <p> Retorna o dono da comunidade especificada. </p>
     *
     * @param nome  Nome da comunidade
     * @return      Dono da comunidade
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     */

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getDonoComunidade(nome);
    }

    /**
     * <p> Retorna a lista de membros da comunidade especificada. </p>
     *
     * @param nome  Nome da comunidade
     * @return      Lista de membros da comunidade formatada em uma String
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     *
     * @see UtilsString
     */

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sistema.getMembrosComunidade(nome);
    }

    /**
     * <p> Retorna a lista de comunidades do usuário especificado. </p>
     *
     * @param login  Login do usuário
     * @return       Lista de comunidades do usuário formatada em uma String
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilsString
     */

    public String getComunidades(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return this.sistema.getComunidades(usuario);
    }

    /**
     * <p> Adiciona o usuário com a sessão aberta identificada por id à comunidade especificada. </p>
     *
     * @param id    ID da sessão
     * @param nome  Nome da comunidade
     *
     * @throws UsuarioNaoCadastradoException       Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeNaoExisteException        Exceção lançada caso a comunidade não exista
     * @throws UsuarioJaEstaNaComunidadeException  Exceção lançada caso o usuário já esteja na comunidade
     */

    public void adicionarComunidade(String id, String nome)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException, UsuarioJaEstaNaComunidadeException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        this.sistema.adicionarComunidade(usuario, nome);
    }

    /**
     * <p> Lê a primeira mensagem da fila de mensagens do usuário com a sessão aberta. </p>
     *
     * @param id  ID da sessão
     * @return    Primeira mensagem da fila de mensagens do usuário
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws NaoHaMensagensException        Exceção lançada caso o usuário não tenha mensagens na fila
     */

    public String lerMensagem(String id) throws UsuarioNaoCadastradoException, NaoHaMensagensException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        return this.sistema.lerMensagem(usuario);
    }

    /**
     * <p> Envia uma mensagem de um usuário com sessão aberta à comunidade especificada. </p>
     *
     * @param id          ID da sessão
     * @param comunidade  Nome da comunidade
     * @param mensagem    Mensagem a ser enviada
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeNaoExisteException   Exceção lançada caso a comunidade não exista
     */

    public void enviarMensagem(String id, String comunidade, String mensagem)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException {
        this.sistema.getSessaoUsuario(id);
        Comunidade comunidadeAlvo = this.sistema.getComunidade(comunidade);

        this.sistema.enviarMensagem(comunidadeAlvo, mensagem);
    }

    /**
     * <p> Retorna true se o usuário com a sessão aberta identificada por id é fã do usuário especificado. </p>
     *
     * @param login       Login do usuário
     * @param loginIdolo  Login do ídolo
     * @return            Booleano indicando se o usuário é fã do ídolo
     */
    public boolean ehFa(String login, String loginIdolo) {
        Usuario usuario = this.sistema.getUsuario(login);
        Usuario idolo = this.sistema.getUsuario(loginIdolo);

        return idolo.getFas().contains(usuario);
    }

    /**
     * <p> Adiciona um ídolo ao usuário com a sessão aberta identificada por id. </p>
     *
     * @param id          ID da sessão
     * @param loginIdolo  Login do ídolo
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário ou o ídolo não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja ídolo do ídolo
     * @throws UsuarioAutoRelacaoException    Exceção lançada caso o usuário tente adicionar a si mesmo como ídolo
     */

    public void adicionarIdolo (String id, String loginIdolo)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario idolo = this.sistema.getUsuario(loginIdolo);

        this.sistema.adicionarIdolo(usuario, idolo);
    }

    /**
     * <p> Retorna a lista de fãs do usuário com a sessão aberta identificada por id. </p>
     *
     * @param login  ID da sessão
     * @return       Lista de ídolos do usuário formatada em uma {@code String}
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilsString
     */

    public String getFas(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        return this.sistema.getFas(usuario);
    }

    /**
     * <p> Retorna true se o usuário com a sessão aberta identificada por id paquera o usuário especificado. </p>
     *
     * @param id            ID da sessão
     * @param loginPaquera  Login do usuário paquerado
     * @return         Booleano indicando se o usuário paquera o usuário especificado
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário ou o paquera não estejam cadastrados
     */

    public boolean ehPaquera(String id, String loginPaquera) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario paquera = this.sistema.getUsuario(loginPaquera);

        return usuario.getPaqueras().contains(paquera);
    }

    /**
     * <p> Adiciona um paquera ao usuário com a sessão aberta identificada por id. </p>
     *
     * @param id            ID da sessão
     * @param loginPaquera  Login do usuário paquerado
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário ou o paquera não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja paquera do paquera
     * @throws UsuarioAutoRelacaoException    Exceção lançada caso o usuário tente adicionar a si mesmo como paquera
     * @throws UsuarioEhInimigoException      Exceção lançada caso o usuário tente adicionar um inimigo como paquera
     */

    public void adicionarPaquera (String id, String loginPaquera)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException, UsuarioEhInimigoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario paquera = this.sistema.getUsuario(loginPaquera);

        this.sistema.adicionarPaquera(usuario, paquera);
    }

    /**
     * <p> Retorna a lista de paqueras do usuário com a sessão aberta identificada por id. </p>
     *
     * @param id  ID da sessão
     * @return    Lista de paqueras do usuário formatada em uma {@code String}
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilsString
     */

    public String getPaqueras(String id) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);

        return this.sistema.getPaqueras(usuario);
    }

    /**
     * <p> Adiciona um inimigo ao usuário com a sessão aberta identificada por id. </p>
     *
     * @param id            ID da sessão
     * @param loginInimigo  Login do usuário inimigo
     *
     * @throws UsuarioNaoCadastradoException  Exceção lançada caso o usuário ou o inimigo não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja inimigo do inimigo
     * @throws UsuarioAutoRelacaoException    Exceção lançada caso o usuário tente adicionar a si mesmo como inimigo
     */

    public void adicionarInimigo(String id, String loginInimigo)
            throws UsuarioNaoCadastradoException, UsuarioJaTemRelacaoException, UsuarioAutoRelacaoException {
        Usuario usuario = this.sistema.getSessaoUsuario(id);
        Usuario inimigo = this.sistema.getUsuario(loginInimigo);

        this.sistema.adicionarInimigo(usuario, inimigo);
    }

    /**
     * <p> Remove um usuário do sistema. </p>
     *
     * @param id ID da sessão
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado
     */

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
