package br.ufal.ic.p2.jackut.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.jackut.Sistema;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;

import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Mensagem;
import br.ufal.ic.p2.jackut.models.Usuario;

import br.ufal.ic.p2.jackut.types.RelacoesTypes;

/**
 * <p> Classe com métodos utilitários para a leitura de arquivos. </p>
 */

public class UtilsFileReader {

    /**
     * <p> Lê todos os arquivos do banco de dados e os carrega no sistema. </p>
     *
     * @param sistema Sistema a ser carregado.
     */

    public static void lerArquivos(Sistema sistema) {
        Map<String, String[]> comunidades = new HashMap<>();

        lerArquivo("usuarios", sistema, comunidades);
        lerArquivo("amigos", sistema, null);
        lerArquivo("recados", sistema, null);
        lerArquivo("comunidades", sistema, comunidades);
        lerArquivo("mensagens", sistema, null);
        lerArquivo("relacoes", sistema, null);
    }

    /**
     * <p> Lê um arquivo genérico com o nome passado e o carrega no sistema. </p>
     *
     * @param arquivo      Nome do arquivo.
     * @param sistema      Sistema a ser carregado.
     * @param comunidades  Mapa de comunidades.
     */

    public static void lerArquivo(String arquivo, Sistema sistema, Map<String, String[]> comunidades) {
        File file = new File("./database/" + arquivo + ".txt");

        if (!file.exists()) return;

        String[] dados;
        String linha;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((linha = br.readLine()) != null) {
                dados = linha.split(";");

                if(arquivo.equals("usuarios")) lerUsuarios(sistema, dados, comunidades);
                else if(arquivo.equals("amigos")) lerAmigos(sistema, dados);
                else if(arquivo.equals("recados")) lerRecados(sistema, dados);
                else if(arquivo.equals("comunidades")) lerComunidades(sistema, dados, comunidades);
                else if(arquivo.equals("mensagens")) lerMensagens(sistema, dados);
                else if(arquivo.equals("relacoes")) lerRelacoes(sistema, dados);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo);
        }
    }

    /**
     * <p> Lê os usuários do arquivo "usuarios.txt". </p>
     *
     * @param sistema      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

    private static void lerUsuarios(Sistema sistema, String[] dados, Map<String, String[]> comunidades) {
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
        sistema.setUsuario(usuario);
    }

    /**
     * <p> Lê os amigos dos usuários do arquivo "amigos.txt". </p>
     *
     * @param sistema  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerAmigos(Sistema sistema, String[] dados) {
        Usuario usuario = sistema.getUsuario(dados[0]);

        if (dados[1].length() <= 2) {
            return;
        }

        String[] amigos = dados[1].substring(1, dados[1].length() - 1).split(",");

        for (String amigo : amigos) {
            usuario.setAmigo(sistema.getUsuario(amigo));
        }
    }

    /**
     * <p> Lê os recados dos usuários do arquivo "recados.txt". </p>
     *
     * @param sistema  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerRecados(Sistema sistema, String[] dados) {
        Usuario usuario = sistema.getUsuario(dados[0]);
        Usuario amigo = sistema.getUsuario(dados[1]);
        String recado = dados[2];

        try {
            sistema.enviarRecado(amigo, usuario, recado);
        } catch (UsuarioEhInimigoException e) {}
    }

    /**
     * <p> Lê as comunidades dos usuários do arquivo "comunidades.txt". </p>
     *
     * @param sistema      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

    private static void lerComunidades(Sistema sistema, String[] dados, Map<String, String[]> comunidades) {
        Usuario dono = sistema.getUsuario(dados[0]);
        String nome = dados[1];
        String descricao = dados[2];

        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);

        dono.setDonoComunidade(novaComunidade);

        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");

        for (String membro : membros) {
            if (membro.equals(dono.getLogin())) {
                continue;
            }

            novaComunidade.adicionarMembro(sistema.getUsuario(membro));
        }

        sistema.setComunidade(nome, novaComunidade);

        try {
            for (String login : comunidades.keySet()) {
                Usuario usuario = sistema.getUsuario(login);
                for (String comunidade : comunidades.get(login)) {
                    usuario.setParticipanteComunidade(sistema.getComunidade(comunidade));
                }
            }
        } catch (ComunidadeNaoExisteException e) {}
    }

    /**
     * <p> Lê as mensagens dos usuários do arquivo "mensagens.txt". </p>
     *
     * @param sistema  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerMensagens(Sistema sistema, String[] dados) {
        Usuario usuario = sistema.getUsuario(dados[0]);
        String mensagem = dados[1];

        Mensagem msg = new Mensagem(mensagem);

        usuario.receberMensagem(msg);
    }

    /**
     * <p> Lê as relações dos usuários do arquivo "relacoes.txt". </p>
     *
     * @param sistema  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     *
     * @see RelacoesTypes
     */

    private static void lerRelacoes(Sistema sistema, String[] dados) {
        Usuario usuario = sistema.getUsuario(dados[0]);
        Usuario usuarioAlvo = sistema.getUsuario(dados[1]);
        RelacoesTypes tipo = RelacoesTypes.valueOf(dados[2]);

        switch (tipo) {
            case IDOLO:
                usuario.setIdolo(usuarioAlvo);
                break;
            case FA:
                usuario.setFa(usuarioAlvo);
                break;
            case PAQUERA:
                usuario.setPaquera(usuarioAlvo);
                break;
            case PAQUERARECEBIDA:
                usuario.setPaquerasRecebidas(usuarioAlvo);
                break;
            case INIMIGO:
                usuario.setInimigo(usuarioAlvo);
                usuarioAlvo.setInimigo(usuario);
                break;
        }
    }
}
