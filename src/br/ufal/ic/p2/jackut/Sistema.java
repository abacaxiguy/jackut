package br.ufal.ic.p2.jackut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import br.ufal.ic.p2.jackut.Exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.ContaJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.LoginSenhaInvalidosException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioNaoCadastradoException;

/**
 * <p> Classe que representa o sistema. </p>
 */

public class Sistema {
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> sessoes;

    /**
     * <p> Constrói um novo {@code Sistema} responsável por
     * gerenciar os usuários e as sessões. </p>
     *
     * <p> Inicializa as listas de usuários e sessões. </p>
     *
     * @see Usuario
     */

    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();

                try {
            String caminho = "./database";

            File diretorio = new File(caminho);

            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

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
                    for (int i = 3; i < dados.length; i++) {
                        String[] atributo = dados[i].split(":");
                        usuario.getPerfil().setAtributo(atributo[0], atributo[1]);
                    }
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
                    amigo.enviarRecado(usuario, recado);
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
     * <p> Zera as informações do sistema. </p>
     */

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        try {
            File arquivo = new File("./database/usuarios.txt");
            arquivo.delete();
            File arquivo2 = new File("./database/amigos.txt");
            arquivo2.delete();
            File arquivo3 = new File("./database/recados.txt");
            arquivo3.delete();
        } catch (

        Exception e) {
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
            String caminho = "./database";

            File diretorio = new File(caminho);

            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            File arquivoUsuarios = new File("./database/usuarios.txt");
            arquivoUsuarios.createNewFile();

            FileWriter fw = new FileWriter(arquivoUsuarios);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Usuario usuario : this.getUsuarios().values()) {
                bw.write(usuario.getLogin() + ";" + usuario.getSenha() + ";" + usuario.getNome());
                for (String atributo : usuario.getPerfil().getAtributos().keySet()) {
                    bw.write(";" + atributo + ":" + usuario.getPerfil().getAtributo(atributo));
                }
                bw.newLine();
            }

            bw.flush();
            bw.close();
            fw.close();

            File arquivoAmigos = new File("./database/amigos.txt");
            arquivoAmigos.createNewFile();

            FileWriter fw2 = new FileWriter(arquivoAmigos);
            BufferedWriter bw2 = new BufferedWriter(fw2);

            for (Usuario usuario : this.getUsuarios().values()) {
                String amigos = usuario.getAmigosString();
                bw2.write(usuario.getLogin() + ";" + amigos);
                bw2.newLine();
            }

            bw2.flush();
            bw2.close();
            fw2.close();

            File arquivoRecados = new File("./database/recados.txt");
            arquivoRecados.createNewFile();

            FileWriter fw3 = new FileWriter(arquivoRecados);
            BufferedWriter bw3 = new BufferedWriter(fw3);

            for (Usuario usuario : this.getUsuarios().values()) {
                for (Recado recado : usuario.getRecados()) {
                    bw3.write(usuario.getLogin() + ";" + recado.getRemetente().getLogin() + ";" + recado.getRecado());
                    bw3.newLine();
                }
            }

            bw3.flush();
            bw3.close();
            fw3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
