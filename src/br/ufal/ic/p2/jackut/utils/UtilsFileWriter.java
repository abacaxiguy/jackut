package br.ufal.ic.p2.jackut.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Mensagem;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.Usuario;

import static br.ufal.ic.p2.jackut.types.RelacoesTypes.*;
import br.ufal.ic.p2.jackut.types.RelacoesTypes;


/**
 * <p> Classe com métodos utilitários para a escrita de arquivos. </p>
 */

public class UtilsFileWriter {

    /**
     * <p> Cria a pasta {@code database} caso ela não exista. </p>
     */

    public static void criarPasta() {
        String caminho = "./database";

        File diretorio = new File(caminho);

        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }

    /**
     * <p> Escreve um arquivo genérico com o nome e conteúdo passados. </p>
     *
     * @param arquivo  Nome do arquivo.
     * @param conteudo     Conteúdo do arquivo.
     */

    public static void escreverArquivo(String arquivo, String conteudo) {
        try{
            File file = new File("./database/" + arquivo);
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo " + arquivo);
        }
    }

    /**
     * <p> Salva os usuários no arquivo "usuarios.txt". </p>
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarUsuarios(Map<String, Usuario> usuarios) {
        StringBuilder usuariosData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            usuariosData.append(usuario.getLogin()).append(";")
                    .append(usuario.getSenha()).append(";")
                    .append(usuario.getNome()).append(";");

            for (String atributo : usuario.getPerfil().getAtributos().keySet()) {
                usuariosData.append(atributo).append(":")
                        .append(usuario.getPerfil().getAtributo(atributo)).append(";");
            }

            usuariosData.append(UtilsString.formatArrayList(usuario.getComunidadesParticipantes())).append("\n");
        }

        escreverArquivo("usuarios.txt", usuariosData.toString());
    }

    /**
     * <p> Salva os amigos dos usuários no arquivo "amigos.txt". </p>
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarAmigos(Map<String, Usuario> usuarios) {
        StringBuilder amigosData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            String amigos = usuario.getAmigosString();
            amigosData.append(usuario.getLogin()).append(";").append(amigos).append("\n");
        }

        escreverArquivo("amigos.txt", amigosData.toString());
    }

    /**
     * <p> Salva os recados dos usuários no arquivo "recados.txt". </p>
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarRecados(Map<String, Usuario> usuarios) {
        StringBuilder recadosData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            for (Recado recado : usuario.getRecados()) {
                recadosData.append(usuario.getLogin()).append(";")
                        .append(recado.getRemetente().getLogin()).append(";")
                        .append(recado.getRecado()).append("\n");
            }
        }

        escreverArquivo("recados.txt", recadosData.toString());
    }

    /**
     * <p> Salva as comunidades dos usuários no arquivo "comunidades.txt". </p>
     *
     * @param comunidades  Map com as comunidades.
     */

    public static void salvarComunidades(Map<String, Comunidade> comunidades) {
        StringBuilder comunidadesData = new StringBuilder();
        for (Comunidade comunidade : comunidades.values()) {
            String membros = comunidade.getMembrosString();
            comunidadesData.append(comunidade.getDono().getLogin()).append(";")
                    .append(comunidade.getNome()).append(";")
                    .append(comunidade.getDescricao()).append(";")
                    .append(membros).append("\n");
        }

        escreverArquivo("comunidades.txt", comunidadesData.toString());
    }

    /**
     * <p> Salva as mensagens dos usuários no arquivo "mensagens.txt". </p>
     *
     * @param usuarios   Map com os usuários.
     */

    public static void salvarMensagens(Map<String, Usuario> usuarios) {
        StringBuilder mensagensData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            for (Mensagem mensagem : usuario.getMensagens()) {
                mensagensData.append(usuario.getLogin()).append(";")
                        .append(mensagem.getMensagem()).append("\n");
            }
        }

        escreverArquivo("mensagens.txt", mensagensData.toString());
    }

    /**
     * <p> Salva as relações dos usuários no arquivo "relacoes.txt". </p>
     *
     * @param usuarios  Map com os usuários.
     *
     * @see RelacoesTypes
     */

    public static void salvarRelacoes(Map<String, Usuario> usuarios) {
        StringBuilder relacoesData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            for (Usuario idolo : usuario.getIdolos()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(idolo.getLogin()).append(";")
                        .append(IDOLO).append("\n");
            }

            for (Usuario fa : usuario.getFas()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(fa.getLogin()).append(";")
                        .append(FA).append("\n");
            }

            for (Usuario paquera : usuario.getPaqueras()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(paquera.getLogin()).append(";")
                        .append(PAQUERA).append("\n");
            }

            for (Usuario paquerasRecebidas : usuario.getPaquerasRecebidas()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(paquerasRecebidas.getLogin()).append(";")
                        .append(PAQUERARECEBIDA).append("\n");
            }

            for (Usuario inimigos : usuario.getInimigos()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(inimigos.getLogin()).append(";")
                        .append(INIMIGO).append("\n");
            }
        }

        escreverArquivo("relacoes.txt", relacoesData.toString());
    }

    /**
     * <p> Persiste os dados, salvando-os em arquivos. </p>
     *
     * @param usuarios      Map com os usuários.
     * @param comunidades   Map com as comunidades.
     */

    public static void persistirDados(Map<String, Usuario> usuarios, Map<String, Comunidade> comunidades) {
        salvarUsuarios(usuarios);
        salvarAmigos(usuarios);
        salvarRecados(usuarios);
        salvarComunidades(comunidades);
        salvarMensagens(usuarios);
        salvarRelacoes(usuarios);
    }

    /**
     * <p> Carrega os usuários do arquivo "usuarios.txt". </p>
     */

    public static void limparArquivos() {
        escreverArquivo("usuarios.txt", "");
        escreverArquivo("amigos.txt", "");
        escreverArquivo("recados.txt", "");
        escreverArquivo("comunidades.txt", "");
        escreverArquivo("mensagens.txt", "");
        escreverArquivo("relacoes.txt", "");
    }
}
