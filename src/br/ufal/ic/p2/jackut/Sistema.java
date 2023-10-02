package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.ufal.ic.p2.jackut.utils.*;

import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Mensagem;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.Usuario;

import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;


/**
 * <p> Classe que representa o sistema. </p>
 */

public class Sistema {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Usuario> sessoes = new HashMap<>();
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /**
     * <p> Constrói um novo {@code Sistema} responsável por
     * gerenciar os usuários e as sessões. </p>
     *
     * <p> Inicializa as listas de usuários e sessões. </p>
     *
     * @see Usuario
     */

    public Sistema() {
        UtilsFileWriter.criarPasta();

        UtilsFileReader.lerArquivos(this);
    }

    /**
     * <p> Adiciona um usuário ao sistema. </p>
     *
     * @param usuario Usuário a ser adicionado.
     *
     * @throws ContaJaExisteException Exceção lançada caso o usuário já esteja cadastrado.
     *
     * @see Usuario
     */

    public void setUsuario(Usuario usuario) throws ContaJaExisteException {
        String login = usuario.getLogin();

        if (this.usuarios.containsKey(login)) {
            throw new ContaJaExisteException();
        }

        this.usuarios.put(login, usuario);
    }

    /**
     * <p> Loga um usuário no sistema, criando seu id de sessão e retornando-o. </p>
     *
     * @param login  Login do usuário.
     * @param senha  Senha do usuário.
     * @return       ID da sessão do usuário.
     *
     * @throws LoginSenhaInvalidosException Exceção lançada caso o login ou a senha sejam inválidos.
     *
     * @see Usuario
     */

    public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException {
        try{
            Usuario usuario = this.getUsuario(login);

            if (!usuario.verificarSenha(senha)) {
                throw new LoginSenhaInvalidosException("any");
            }

            String id = UUID.randomUUID().toString();

            this.sessoes.put(id, usuario);

            return id;
        } catch (UsuarioNaoCadastradoException e) {
            throw new LoginSenhaInvalidosException("any");
        }
    }

    /**
     * <p> Retorna um usuário do sistema pelo login. </p>
     *
     * @param login Login do usuário.
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado.
     *
     * @see Usuario
     */

    public Usuario getUsuario(String login) throws UsuarioNaoCadastradoException {
        if (!this.usuarios.containsKey(login)) {
            throw new UsuarioNaoCadastradoException();
        }

        return this.usuarios.get(login);
    }

    /**
     * <p> Retorna um usuário logado do sistema pelo id da sua sessão. </p>
     *
     * @param id ID da sessão do usuário.
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado.
     *
     * @see Usuario
     */

    public Usuario getSessaoUsuario(String id) throws UsuarioNaoCadastradoException {
        if (!this.sessoes.containsKey(id)) {
            throw new UsuarioNaoCadastradoException();
        }

        return this.sessoes.get(id);
    }

    /**
     * <p> Adiciona um amigo ao usuário </p>
     * <p> O amigo não será adicionado caso ele não seja amigo, já tenha solicitado amizade ou já tenha recebido uma solicitação de amizade do usuário. </p>
     *
     * @param amigo Login do amigo a ser adicionado
     *
     * @throws UsuarioJaTemRelacaoException        Exceção lançada caso o usuário já seja amigo do usuário aberto na sessão
     * @throws UsuarioAutoRelacaoException         Exceção lançada caso o usuário tente adicionar a si mesmo como amigo
     * @throws UsuarioJaSolicitouAmizadeException  Exceção lançada caso o usuário já tenha solicitado amizade ao amigo
     * @throws UsuarioEhInimigoException           Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão
     */

    public void adicionarAmigo(Usuario usuario, Usuario amigo)
            throws UsuarioJaTemRelacaoException, UsuarioJaSolicitouAmizadeException, UsuarioAutoRelacaoException,
            UsuarioEhInimigoException {
        if (usuario.equals(amigo)) {
            throw new UsuarioAutoRelacaoException("amizade");
        }

        if (usuario.getAmigos().contains(amigo) || amigo.getAmigos().contains(usuario)) {
            throw new UsuarioJaTemRelacaoException("amigo");
        }

        this.verificarInimigo(usuario, amigo);

        if (usuario.getSolicitacoesEnviadas().contains(amigo)) {
            throw new UsuarioJaSolicitouAmizadeException();
        } else if (usuario.getSolicitacoesRecebidas().contains(amigo)) {
            usuario.aceitarSolicitacao(amigo);
        } else {
            usuario.enviarSolicitacao(amigo);
        }
    }

    /**
     * <p> Envia um recado para o usuário passado como parâmetro. </p>
     *
     * @param destinatario  Usuário para o qual o recado será enviado.
     * @param recado        Recado a ser enviado.
     *
     * @throws UsuarioAutoEnvioRecadoException Exceção lançada caso o usuário tente enviar um recado para si mesmo.
     */

    public void enviarRecado(Usuario remetente, Usuario destinatario, String recado) throws UsuarioAutoEnvioRecadoException, UsuarioEhInimigoException {
        if (remetente.getLogin().equals(destinatario.getLogin())) {
            throw new UsuarioAutoEnvioRecadoException();
        }

        this.verificarInimigo(remetente, destinatario);

        Recado r = new Recado(remetente, destinatario, recado);
        destinatario.receberRecado(r);
    }

    /**
     * <p> Retorna o último recado não lido do usuário. </p>
     *
     * @param usuario  O usuário do qual o recado será lido.
     * @return         Recado do usuário.
     *
     * @throws NaoHaRecadosException Exceção lançada se não houver recados para ler.
     */

    public String lerRecado(Usuario usuario) throws NaoHaRecadosException {
        Recado recado = usuario.lerRecado();
        return recado.getRecado();
    }

    /**
     * <p> Retorna a comunidade do sistema pelo nome. </p>
     *
     * @param nome  Nome da comunidade.
     * @return      Comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public Comunidade getComunidade(String nome) throws ComunidadeNaoExisteException {
        if (!this.comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }

        return this.comunidades.get(nome);
    }

    /**
     * <p> Cria uma comunidade no sistema. </p>
     *
     * @param dono    Usuario dono da comunidade
     * @param nome       Nome da comunidade
     * @param descricao  Descricao da comunidade
     *
     * @throws ComunidadeJaExisteException Exceção lançada caso a comunidade já exista com base no nome.
     *
     * @see Comunidade
     */

    public void criarComunidade(Usuario dono, String nome, String descricao) throws ComunidadeJaExisteException {
        if (this.comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }

        Comunidade comunidade = new Comunidade(dono, nome, descricao);
        this.comunidades.put(nome, comunidade);

        dono.setDonoComunidade(comunidade);
        dono.setParticipanteComunidade(comunidade);
    }

    /**
     * <p> Adiciona uma comunidade ao sistema. </p>
     * <p><b>AVISO:</b> Método utilizado apenas para carregar os dados do arquivo.</p>
     *
     * @param nome        Nome da comunidade.
     * @param comunidade  Comunidade a ser adicionada.
     *
     * @see Comunidade
     */

    public void setComunidade(String nome, Comunidade comunidade) {
        this.comunidades.put(nome, comunidade);
    }

    /**
     * <p> Retorna uma comunidade do sistema pelo nome. </p>
     *
     * @param nome  Nome da comunidade.
     * @return      Comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getDescricao();
    }

    /**
     * <p> Retorna o dono da comunidade do sistema pelo nome. </p>
     *
     * @param nome  Nome da comunidade.
     * @return      Dono da comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getDono().getLogin();
    }

    /**
     * <p> Retorna os membros da comunidade formatado em uma string. </p>
     *
     * @param nome  Nome da comunidade.
     * @return      Membros da comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getMembrosString();
    }

    /**
     * <p> Retorna as comunidades das quais o usuário é membro formatado em uma {@code String}. </p>
     *
     * @param usuario  O usuário do qual as comunidades serão retornadas.
     * @return         Comunidades das quais o usuário é membro.
     */

    public String getComunidades(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getComunidadesParticipantes());
    }

    /**
     * <p> Adiciona um usuário a uma comunidade. </p>
     *
     * @param usuario  O usuário a ser adicionado.
     * @param nome     Nome da comunidade.
     *
     * @throws ComunidadeNaoExisteException        Exceção lançada caso a comunidade não exista.
     * @throws UsuarioJaEstaNaComunidadeException  Exceção lançada caso o usuário já esteja na comunidade.
     */

    public void adicionarComunidade(Usuario usuario, String nome)
            throws ComunidadeNaoExisteException, UsuarioJaEstaNaComunidadeException {
        Comunidade comunidade = this.getComunidade(nome);

        if (usuario.getComunidadesParticipantes().contains(comunidade)) {
            throw new UsuarioJaEstaNaComunidadeException();
        }

        comunidade.adicionarMembro(usuario);
        usuario.setParticipanteComunidade(comunidade);
    }

    /**
     * <p> Lê a mensagem de um usuário. </p>
     *
     * @param usuario  O usuário do qual a mensagem será lida.
     * @return         Mensagem do usuário.
     *
     * @throws NaoHaMensagensException  Exceção lançada se não houver mensagens para ler.
     */

    public String lerMensagem(Usuario usuario) throws NaoHaMensagensException {
        Mensagem mensagem = usuario.lerMensagem();
        return mensagem.getMensagem();
    }

    /**
     * <p> Envia uma mensagem para uma comunidade. </p>
     * <p> A mensagem será enviada para todos os membros da comunidade. </p>
     *
     * @param comunidade  A comunidade para a qual a mensagem será enviada.
     * @param msg         A mensagem a ser enviada.
     */

    public void enviarMensagem(Comunidade comunidade, String msg) {
        Mensagem mensagem = new Mensagem(msg);

        comunidade.enviarMensagem(mensagem);
    }

    /**
     * <p> Adiciona um usuário como fã de outro. </p>
     *
     * @param usuario  Usuário que será fã.
     * @param idolo    Usuário que será ídolo.
     *
     * @throws UsuarioAutoRelacaoException   Exceção lançada caso o usuário tente adicionar a si mesmo como fã.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja fã do usuário aberto na sessão.
     * @throws UsuarioEhInimigoException     Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão.
     */

    public void adicionarIdolo(Usuario usuario, Usuario idolo)
            throws UsuarioAutoRelacaoException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (usuario.equals(idolo)) {
            throw new UsuarioAutoRelacaoException("fã");
        }

        if (usuario.getIdolos().contains(idolo)) {
            throw new UsuarioJaTemRelacaoException("ídolo");
        }

        this.verificarInimigo(usuario, idolo);

        usuario.setIdolo(idolo);
        idolo.setFa(usuario);
    }

    /**
     * <p> Retorna os fãs do usuário formatados em uma {@code String}. </p>
     *
     * @param usuario  O usuário do qual os fãs serão retornados.
     * @return         Fãs do usuário.
     *
     * @see UtilsString
     */

    public String getFas(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getFas());
    }

    /**
     * <p> Adiciona um usuário como paquera de outro. </p>
     *
     * @param usuario Usuário que irá paquerar.
     * @param paquera Usuário que será paquerado.
     *
     * @throws UsuarioAutoRelacaoException   Exceção lançada caso o usuário tente adicionar a si mesmo como paquera.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja paquera do usuário aberto na sessão.
     * @throws UsuarioEhInimigoException     Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão.
     */

    public void adicionarPaquera(Usuario usuario, Usuario paquera)
            throws UsuarioAutoRelacaoException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (usuario.equals(paquera)) {
            throw new UsuarioAutoRelacaoException("paquera");
        }

        if (usuario.getPaqueras().contains(paquera)) {
            throw new UsuarioJaTemRelacaoException("paquera");
        }

        this.verificarInimigo(usuario, paquera);

        if (usuario.getPaquerasRecebidas().contains(paquera) || paquera.getPaquerasRecebidas().contains(usuario)) {
            this.enviarRecado(paquera, usuario, paquera.getNome() + " é seu paquera - Recado do Jackut.");
            this.enviarRecado(usuario, paquera, usuario.getNome() + " é seu paquera - Recado do Jackut.");
        }

        usuario.setPaquera(paquera);
        paquera.setPaquerasRecebidas(usuario);
    }

    /**
     * <p> Retorna os paqueras do usuário formatados como uma {@code String}. </p>
     *
     * @param usuario  O usuário do qual os paqueras serão retornados.
     * @return         Paqueras do usuário.
     *
     * @see UtilsString
     */

    public String getPaqueras(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getPaqueras());
    }

    /**
     * <p> Adiciona um usuário como inimigo de outro. </p>
     *
     * @param usuario Usuário que irá adicionar o inimigo.
     * @param inimigo Usuário que será inimigo.
     *
     * @throws UsuarioAutoRelacaoException   Exceção lançada caso o usuário tente adicionar a si mesmo como inimigo.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja inimigo do usuário aberto na sessão.
     */

    public void adicionarInimigo(Usuario usuario, Usuario inimigo)
            throws UsuarioAutoRelacaoException, UsuarioJaTemRelacaoException {
        if (usuario.equals(inimigo)) {
            throw new UsuarioAutoRelacaoException("inimigo");
        }

        if (usuario.getInimigos().contains(inimigo)) {
            throw new UsuarioJaTemRelacaoException("inimigo");
        }

        usuario.setInimigo(inimigo);
        inimigo.setInimigo(usuario);
    }

    /**
     * <p> Verifica se o usuário é inimigo do usuário aberto na sessão. </p>
     * <p> Caso seja, lança uma exceção. </p>
     *
     * @param usuario Usuário que irá verificar.
     * @param inimigo O inimigo que será verificado.
     *
     * @throws UsuarioEhInimigoException Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão.
     */

    public void verificarInimigo(Usuario usuario, Usuario inimigo) throws UsuarioEhInimigoException {
        if (usuario.getInimigos().contains(inimigo)) {
            throw new UsuarioEhInimigoException(inimigo.getNome());
        }
    }

    /**
     * <p> Remove um usuário do sistema. </p>
     * <p> Remove o usuário de todas as comunidades, remove os recados enviados e recebidos pelo usuário e remove o usuário do sistema, junto de todos os seus relacionamentos </p>
     *
     * @param usuario  Usuário a ser removido.
     * @param id       ID da sessão do usuário.
     *
     * @throws UsuarioNaoCadastradoException Exceção lançada caso o usuário não esteja cadastrado.
     */

    public void removerUsuario(Usuario usuario, String id) throws UsuarioNaoCadastradoException {
        for (Usuario amigo : usuario.getAmigos()) {
            amigo.removerAmigo(usuario);
        }

        for (Usuario fa : usuario.getIdolos()) {
            fa.removerFa(usuario);
        }

        for (Usuario paquera : usuario.getPaqueras()) {
            paquera.removerPaquera(usuario);
        }

        for (Usuario paqueraRecebida : usuario.getPaquerasRecebidas()) {
            paqueraRecebida.removerPaqueraRecebida(usuario);
        }

        for (Usuario inimigo : usuario.getInimigos()) {
            inimigo.removerInimigo(usuario);
        }

        for (Usuario solicitacaoEnviada : usuario.getSolicitacoesEnviadas()) {
            solicitacaoEnviada.removerSolicitacaoEnviada(usuario);
        }

        for (Usuario solicitacaoRecebida : usuario.getSolicitacoesRecebidas()) {
            solicitacaoRecebida.removerSolicitacaoRecebida(usuario);
        }

        for (Comunidade comunidade : this.comunidades.values()) {
            if (comunidade.getDono().equals(usuario)) {
                for (Usuario membro : comunidade.getMembros()) {
                    membro.sairComunidade(comunidade);
                }
                this.comunidades.remove(comunidade.getNome());
            }
        }

        for (Usuario destinatario : this.usuarios.values()) {
            for (Recado recado : destinatario.getRecados()) {
                if (recado.getRemetente().equals(usuario)) {
                    destinatario.removerRecado(recado);
                }
            }
        }

        this.usuarios.remove(usuario.getLogin());
        this.sessoes.remove(id);
    }

    /**
     * <p> Zera as informações do sistema. </p>
     */

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.comunidades = new HashMap<>();

        UtilsFileWriter.limparArquivos();
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
        UtilsFileWriter.criarPasta();
        UtilsFileWriter.persistirDados(this.usuarios, this.comunidades);
    }
}
