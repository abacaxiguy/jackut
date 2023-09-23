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

public class UtilsFileHandler {
    public static void criarPasta() {
        String caminho = "./database";

        File diretorio = new File(caminho);

        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }

    public static void escreverArquivo(String nomeArquivo, String conteudo) throws IOException {
        File arquivo = new File("./database/" + nomeArquivo);
        arquivo.createNewFile();

        FileWriter fw = new FileWriter(arquivo);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(conteudo);
        bw.flush();
        bw.close();
        fw.close();
    }

    public static void salvarUsuarios(Map<String, Usuario> usuarios) throws IOException {
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

    public static void salvarAmigos(Map<String, Usuario> usuarios) throws IOException {
        StringBuilder amigosData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            String amigos = usuario.getAmigosString();
            amigosData.append(usuario.getLogin()).append(";").append(amigos).append("\n");
        }

        escreverArquivo("amigos.txt", amigosData.toString());
    }

    public static void salvarRecados(Map<String, Usuario> usuarios) throws IOException {
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

    public static void salvarComunidades(Map<String, Comunidade> comunidades) throws IOException {
        StringBuilder comunidadesData = new StringBuilder();
        for (Comunidade comunidade : comunidades.values()) {
            String membros = comunidade.getMembrosString();
            comunidadesData.append(comunidade.getCriador().getLogin()).append(";")
                    .append(comunidade.getNome()).append(";")
                    .append(comunidade.getDescricao()).append(";")
                    .append(membros).append("\n");
        }

        escreverArquivo("comunidades.txt", comunidadesData.toString());
    }

    public static void salvarMensagens(Map<String, Usuario> usuarios) throws IOException {
        StringBuilder mensagensData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            for (Mensagem mensagem : usuario.getMensagens()) {
                mensagensData.append(usuario.getLogin()).append(";")
                        .append(mensagem.getMensagem()).append("\n");
            }
        }

        escreverArquivo("mensagens.txt", mensagensData.toString());
    }

    public static void salvarRelacoes(Map<String, Usuario> usuarios) throws IOException {
        StringBuilder relacoesData = new StringBuilder();
        for (Usuario usuario : usuarios.values()) {
            for (Usuario idolo : usuario.getIdolos()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(idolo.getLogin()).append(";")
                        .append("idolo").append("\n");
            }

            for (Usuario fa : usuario.getFas()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(fa.getLogin()).append(";")
                        .append("fa").append("\n");
            }

            for (Usuario paquera : usuario.getPaqueras()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(paquera.getLogin()).append(";")
                        .append("paquera").append("\n");
            }

            for (Usuario paquerasRecebidas : usuario.getPaquerasRecebidas()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(paquerasRecebidas.getLogin()).append(";")
                        .append("paqueraRecebida").append("\n");
            }

            for (Usuario inimigos : usuario.getInimigos()) {
                relacoesData.append(usuario.getLogin()).append(";")
                        .append(inimigos.getLogin()).append(";")
                        .append("inimigo").append("\n");
            }
        }

        escreverArquivo("relacoes.txt", relacoesData.toString());
    }

    public static void persistirDados(Map<String, Usuario> usuarios, Map<String, Comunidade> comunidades) throws IOException {
        salvarUsuarios(usuarios);
        salvarAmigos(usuarios);
        salvarRecados(usuarios);
        salvarComunidades(comunidades);
        salvarMensagens(usuarios);
        salvarRelacoes(usuarios);
    }

    public static void limparArquivos() throws IOException {
        escreverArquivo("usuarios.txt", "");
        escreverArquivo("amigos.txt", "");
        escreverArquivo("recados.txt", "");
        escreverArquivo("comunidades.txt", "");
        escreverArquivo("mensagens.txt", "");
        // escreverArquivo("relacoes.txt", "");
    }
}
