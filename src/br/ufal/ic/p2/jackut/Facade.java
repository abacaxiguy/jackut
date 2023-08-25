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

import br.ufal.ic.p2.jackut.Exceptions.*;
import easyaccept.EasyAccept;


public class Facade {
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;
    private String sessao;

    public Facade() {
        // Inicializa o sistema com os dados mantidos em arquivo.
        // Se não houver arquivo, inicia o sistema sem usuários cadastrados.
        try{
            File arquivo = new File("usuarios.txt");
            if(arquivo.exists()){
                this.usuarios = new HashMap<>();
                this.usuarioLogado = null;
                this.sessao = null;

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
                    this.usuarios.put(dados[0], usuario);
                }
                br.close();
            }else{
                this.usuarios = new HashMap<>();
                this.usuarioLogado = null;
                this.sessao = null;
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
            this.usuarios = new HashMap<>();
            this.usuarioLogado = null;
            this.sessao = null;
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

        if (this.usuarios.containsKey(login)) {
            throw new ContaJaExisteException();
        }
        this.usuarios.put(login, new Usuario(login, senha, nome));
    }

    public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException {
        if (!this.usuarios.containsKey(login)) {
            throw new LoginSenhaInvalidosException("any");
        }

        Usuario usuario = this.usuarios.get(login);

        if (!usuario.getSenha().equals(senha)) {
            throw new LoginSenhaInvalidosException("any");
        }

        this.usuarioLogado = usuario;
        this.sessao = UUID.randomUUID().toString();

        return this.sessao;
    }

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        if (!this.usuarios.containsKey(login)) {
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = this.usuarios.get(login);

        if (atributo.equals("nome")) {
            return usuario.getNome();
        } else {
            return usuario.getPerfil().getAtributo(atributo);
        }
    }

    public void editarPerfil(String id, String atributo, String valor) throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        if (this.sessao == null || this.usuarioLogado == null) {
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = this.usuarioLogado;

        usuario.getPerfil().setAtributo(atributo, valor);
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

            for (Usuario usuario : this.usuarios.values()) {
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
