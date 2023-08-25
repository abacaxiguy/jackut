package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;
import br.ufal.ic.p2.jackut.Exceptions.AtributoNaoPreenchidoException;

public class Perfil {
    private Map<String, String> atributos = new HashMap<>();

    public Perfil() {
    }

    public void setAtributo(String chave, String valor) {
        this.atributos.put(chave, valor);
    }

    public String getAtributo(String chave) throws AtributoNaoPreenchidoException {
        if (this.atributos.containsKey(chave)) {
            return this.atributos.get(chave);
        } else {
            throw new AtributoNaoPreenchidoException();
        }
    }

    protected Map<String, String> getAtributos() {
        return this.atributos;
    }
}
