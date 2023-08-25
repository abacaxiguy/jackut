package br.ufal.ic.p2.jackut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.ufal.ic.p2.jackut.Exceptions.*;


public class Facade {
    private Sistema sistema;

    public Facade() {
        this.sistema = new Sistema();

        try{
            File arquivo = new File("usuarios.txt");
            if(arquivo.exists()){
                String[] dados;
                String linha;

                BufferedReader br = new BufferedReader(new FileReader(arquivo));

                while ((linha = br.readLine()) != null) {
                    dados = linha.split(";");
                    Usuario usuario = new Usuario(dados[0], dados[1], dados[2]);
                    for(int i = 3; i < dados.length; i++){
                        String[] atributo = dados[i].split(":");
                        usuario.getPerfil().setAtributo(atributo[0], atributo[1]);
                    }
                    this.sistema.setUsuario(usuario);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zerarSistema() {
        // Apaga todos os dados mantidos no sistema.

        try {
            File arquivo = new File("usuarios.txt");
            arquivo.delete();
            this.sistema.zerarSistema();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criarUsuario(String login, String senha, String nome) throws LoginSenhaInvalidosException, ContaJaExisteException {
        // Cria um usuário com os dados da conta fornecidos.

        if (login == null || login.equals("")) {
            throw new LoginSenhaInvalidosException("login");
        }

        if (senha == null || senha.equals("")) {
            throw new LoginSenhaInvalidosException("senha");
        }

        if (this.sistema.getUsuario(login) != null) {
            throw new ContaJaExisteException();
        }
        Usuario usuario = new Usuario(login, senha, nome);
        this.sistema.setUsuario(usuario);
    }

    public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException {
        // Abre uma sessão para um usuário com o login e a senha fornecidos, e retorna
        // uma id para esta sessão.

        if (this.sistema.getUsuario(login) == null) {
            throw new LoginSenhaInvalidosException("any");
        }

        Usuario usuario = this.sistema.getUsuario(login);

        if (!usuario.getSenha().equals(senha)) {
            throw new LoginSenhaInvalidosException("any");
        }

        this.sistema.setUsuarioLogado(usuario);

        return this.sistema.getSessao(usuario);
    }

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        // Retorna o valor do atributo de um usuário, armazenado em seu perfil

        if (this.sistema.getUsuario(login) == null) {
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = this.sistema.getUsuario(login);

        if (atributo.equals("nome")) {
            return usuario.getNome();
        } else {
            return usuario.getPerfil().getAtributo(atributo);
        }
    }

    public void editarPerfil(String id, String atributo, String valor)
            throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        // Modifica o valor de um atributo do perfil de um usuário para o valor
        // especificado. Uma sessão válida (identificada por id) deve estar aberta para
        // o usuário cujo perfil se quer editar.

        if (this.sistema.getUsuarioLogado(id) == null) {
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = this.sistema.getUsuarioLogado(id);

        usuario.getPerfil().setAtributo(atributo, valor);
    }

    public void adicionarAmigo(String id, String amigo) throws UsuarioJaAmigoException, UsuarioNaoCadastradoException,
            UsuarioAutoAdicaoAmigoException, UsuarioJaSolicitouAmizadeException {
        Usuario usuario = this.sistema.getUsuarioLogado(id);
        Usuario amigoUsuario = this.sistema.getUsuario(amigo);

        if (amigoUsuario == null || usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (usuario.getLogin().equals(amigoUsuario.getLogin())) {
            throw new UsuarioAutoAdicaoAmigoException();
        }

        if (usuario.getAmigos().contains(amigoUsuario) || amigoUsuario.getAmigos().contains(usuario)) {
            throw new UsuarioJaAmigoException();
        }

        if (usuario.getSolicitacoesEnviadas().contains(amigoUsuario)) {
            throw new UsuarioJaSolicitouAmizadeException();
        }

        else if (usuario.getSolicitacoesRecebidas().contains(amigoUsuario)) {
            usuario.aceitarSolicitacao(amigoUsuario);
        }

        else {
            usuario.enviarSolicitacao(amigoUsuario);
        }
    }

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);
        Usuario amigoUsuario = this.sistema.getUsuario(amigo);

        if (usuario == null || amigoUsuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.getAmigos().contains(amigoUsuario);
    }

    public String getAmigos(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = this.sistema.getUsuario(login);

        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        String amigos = "{";
        for (int i = 0; i < usuario.getAmigos().size(); i++) {
            Usuario amigo = usuario.getAmigos().get(i);
            amigos += amigo.getLogin();
            if (i < usuario.getAmigos().size() - 1) {
                amigos += ",";
            }
        }

        amigos += "}";
        return amigos;
    }

    public void encerrarSistema() throws AtributoNaoPreenchidoException {
        // Grava o cadastro em arquivo e encerra o programa. Atingir o final de um
        // script (final de arquivo) é equivalente a encontrar este comando.
        // Neste caso, o comando não tem parâmetros, e salvará nesse momento apenas os usuários cadastrados.

        try {
            File arquivo = new File("usuarios.txt");
            arquivo.createNewFile();

            FileWriter fw = new FileWriter(arquivo);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Usuario usuario : this.sistema.getUsuarios().values()) {
                bw.write(usuario.getLogin() + ";" + usuario.getSenha() + ";" + usuario.getNome());
                for (String atributo : usuario.getPerfil().getAtributos().keySet()) {
                    bw.write(";" + atributo + ":" + usuario.getPerfil().getAtributo(atributo));
                }
                bw.newLine();
            }

            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
