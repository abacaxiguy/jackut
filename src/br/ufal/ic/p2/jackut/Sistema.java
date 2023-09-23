package br.ufal.ic.p2.jackut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import br.ufal.ic.p2.jackut.Exceptions.Recado.UsuarioAutoEnvioRecadoException;

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
        try {
            UtilsFileHandler.criarPasta();

            Map<String, String[]> comunidades = new HashMap<>();

            File arquivo = new File("./database/usuarios.txt");
            if (arquivo.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");

                    String login = dados[0];
                    String senha = dados[1];
                    String nome = "";
                    if (dados.length > 2) {
                        nome = dados[2];
                    }

                    Usuario usuario = new Usuario(login, senha, nome);
                    for (int i = 3; i < dados.length - 1; i++) {
                        String[] atributo = dados[i].split(":");
                        usuario.getPerfil().setAtributo(atributo[0], atributo[1]);
                    }

                    String[] comunidadesUsuario = dados[dados.length - 1]
                            .substring(1, dados[dados.length - 1].length() - 1)
                            .split(",");

                    comunidades.put(login, comunidadesUsuario);

                    this.setUsuario(usuario);
                }
                br.close();
            }

            File arquivo2 = new File("./database/amigos.txt");
            if (arquivo2.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo2));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario usuario = this.getUsuario(dados[0]);

                    if (dados[1].length() <= 2) {
                        continue;
                    }

                    String[] amigos = dados[1].substring(1, dados[1].length() - 1).split(",");

                    for (String amigo : amigos) {
                        usuario.setAmigo(this.getUsuario(amigo));
                    }
                }
                br.close();
            }

            File arquivo3 = new File("./database/recados.txt");
            if (arquivo3.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo3));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario usuario = this.getUsuario(dados[0]);
                    Usuario amigo = this.getUsuario(dados[1]);
                    String recado = dados[2];
                    try {
                        this.enviarRecado(amigo, usuario, recado);
                    } catch (UsuarioEhInimigoException e) {
                    }
                }
                br.close();
            }

            File arquivo4 = new File("./database/comunidades.txt");
            if (arquivo4.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo4));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario dono = this.getUsuario(dados[0]);
                    String nome = dados[1];
                    String descricao = dados[2];

                    Comunidade comunidade = new Comunidade(dono, nome, descricao);

                    dono.setCriadorComunidade(comunidade);

                    String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");

                    for (String membro : membros) {
                        if (membro.equals(dono.getLogin())) {
                            continue;
                        }
                        comunidade.adicionarMembro(this.getUsuario(membro));
                    }

                    this.comunidades.put(nome, comunidade);
                }

                try {
                    for (String login : comunidades.keySet()) {
                        Usuario usuario = this.getUsuario(login);
                        for (String comunidade : comunidades.get(login)) {
                            usuario.setParticipanteComunidade(this.getComunidade(comunidade));
                        }
                    }
                } catch (ComunidadeNaoExisteException e) {
                }

                br.close();
            }

            File arquivo5 = new File("./database/mensagens.txt");
            if (arquivo5.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo5));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario usuario = this.getUsuario(dados[0]);
                    String mensagem = dados[1];
                    Mensagem msg = new Mensagem(mensagem);
                    usuario.receberMensagem(msg);
                }
                br.close();
            }

            File arquivo6 = new File("./database/relacoes.txt");
            if (arquivo6.exists()) {
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo6));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario usuario = this.getUsuario(dados[0]);
                    Usuario amigo = this.getUsuario(dados[1]);
                    String tipo = dados[2];

                    switch (tipo) {
                        case "idolo":
                            usuario.setIdolo(amigo);
                            break;
                        case "fa":
                            usuario.setFa(amigo);
                            break;
                        case "paquera":
                            usuario.setPaquera(amigo);
                            break;
                        case "paqueraRecebida":
                            usuario.setPaquerasRecebidas(amigo);
                            break;
                        case "inimigo":
                            usuario.setInimigo(amigo);
                            amigo.setInimigo(usuario);
                            break;
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * <p> Retorna todos os usuários do sistema. </p>
     *
     * @return Map com todos os usuários do sistema.
     *
     * @see Usuario
     */

    public Map<String, Usuario> getUsuarios() {
        return this.usuarios;
    }

    /**
     * <p>
     * Adiciona um amigo ao usuário, caso ele não seja amigo, já tenha solicitado
     * amizade ou já tenha recebido uma solicitação de amizade do usuário.
     * </p>
     *
     * @param amigo Login do amigo a ser adicionado
     *
     * @throws UsuarioJaTemRelacaoException       Exceção lançada caso o usuário já
     *                                            seja amigo do usuário aberto na
     *                                            sessão
     * @throws UsuarioAutoRelacaoException        Exceção lançada caso o usuário
     *                                            tente adicionar a si mesmo como
     *                                            amigo
     * @throws UsuarioJaSolicitouAmizadeException Exceção lançada caso o usuário já
     *                                            tenha solicitado amizade ao amigo
     * 
     * @throws UsuarioEhInimigoException          Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão
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
     * @param dono       Usuario dono da comunidade
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

        dono.setCriadorComunidade(comunidade);
        dono.setParticipanteComunidade(comunidade);
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

        return comunidade.getCriador().getLogin();
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
     * <p> Retorna as comunidades das quais o usuário é membro formatado em uma string. </p>
     *
     * @param usuario  Usuário.
     * @return         Comunidades das quais o usuário é membro.
     */

    public String getComunidades(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getComunidadesParticipantes());
    }

    /**
     * <p> Adiciona um usuário a uma comunidade. </p>
     *
     * @param usuario  Usuário.
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

    public String lerMensagem(Usuario usuario) throws NaoHaMensagensException {
        return usuario.lerMensagem();
    }

    public void enviarMensagem(Comunidade comunidade, String msg) {
        Mensagem mensagem = new Mensagem(msg);

        comunidade.enviarMensagem(mensagem);
    }

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

    public String getFas(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getFas());
    }

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

    public String getPaqueras(Usuario usuario) {
        return UtilsString.formatArrayList(usuario.getPaqueras());
    }

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

    public void verificarInimigo(Usuario usuario, Usuario inimigo) throws UsuarioEhInimigoException {
        if (usuario.getInimigos().contains(inimigo)) {
            throw new UsuarioEhInimigoException(inimigo.getNome());
        }
    }

//     # User Story 9 - Remoção de conta - Permita a um usuário encerrar sua conta no Jackut. Todas as suas informações devem sumir do sistema: relacionamentos, mensagens enviadas, perfil.

// zerarSistema

// criarUsuario login=jpsauve senha=sauvejp nome="Jacques Sauve"
// s1=abrirSessao login=jpsauve senha=sauvejp

// criarUsuario login=oabath senha=abatho nome="Osorio Abath"
// s2=abrirSessao login=oabath senha=abatho

// enviarRecado id=${s1} destinatario=oabath recado="Ola"
// criarComunidade id=${s1} nome="UFCG" descricao="Comunidade para professores, alunos e funcionários da UFCG"
// adicionarComunidade id=${s2} nome="UFCG"
// adicionarAmigo id=${s2} amigo=jpsauve

// removerUsuario id=${s1}

// expectError "Usuário não cadastrado." getAtributoUsuario login=jpsauve atributo=nome
// expectError "Comunidade não existe." getDescricaoComunidade nome="UFCG"
// expect {} getComunidades login=oabath
// expect {} getAmigos login=oabath
// expectError "Não há recados." lerRecado id=${s2}

// # tratamento de erros

// expectError "Usuário não cadastrado." removerUsuario id=${s1}

// encerrarSistema
// quit
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
            if (comunidade.getCriador().equals(usuario)) {
                for (Usuario membro : comunidade.getMembros()) {
                    membro.removerComunidade(comunidade);
                }
                this.comunidades.remove(comunidade.getNome());
            }
        }

        for (Usuario destinatario : this.usuarios.values()) {
            for (Recado recado : destinatario.getRecados()) { // O(n^2)
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
        try {
            UtilsFileHandler.limparArquivos();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            UtilsFileHandler.criarPasta();

            UtilsFileHandler.persistirDados(this.usuarios, this.comunidades);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
