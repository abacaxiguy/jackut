package br.ufal.ic.p2.jackut.models;

import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;

/**
 * <p> Classe que representa um perfil de usuário. </p>
 */

public class Perfil {
    private final Map<String, String> atributos = new HashMap<>();

    /**
     * <p> Adiciona um atributo ao perfil. </p>
     *
     * @param chave Nome do atributo.
     * @param valor Valor do atributo.
     */

    public void setAtributo(String chave, String valor) {
        this.atributos.put(chave, valor);
    }

    /**
     * <p> Retorna o valor de um atributo do perfil. </p>
     *
     * @param chave  Chave do atributo.
     * @return       Valor do atributo.
     *
     * @throws AtributoNaoPreenchidoException Exceção que indica que o atributo não foi preenchido.
     */

    public String getAtributo(String chave) throws AtributoNaoPreenchidoException {
        if (this.atributos.containsKey(chave)) {
            return this.atributos.get(chave);
        } else {
            throw new AtributoNaoPreenchidoException();
        }
    }

    /**
     * <p> Retorna todos os atributos do perfil. </p>
     *
     * @return Map com todos os atributos do perfil.
     */

    public Map<String, String> getAtributos() {
        return this.atributos;
    }
}
