package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;

import easyaccept.EasyAccept;

// # User Story 1 - Criação de conta
// # Permita a um usuário criar uma conta no Jackut . Deve ser fornecido um login, uma senha e um nome com o qual o usuário será conhecido na rede.

// zerarSistema

// # Obs.: usar codificação de caracteres ISO 8859
// expectError "Usuário não cadastrado." getAtributoUsuario login=jpsauve atributo=nome

// criarUsuario login=jpsauve senha=sauvejp nome="Jacques Sauve"

// expect "Jacques Sauve" getAtributoUsuario login=jpsauve atributo=nome

// # O comando abrirSessao retorna uma id que pode ser atribuída a uma variável do EasyAccept para referência posterior no script, como você
// # poderá ver, por exemplo, na user story 2. A gente inventou esse comando por questão de segurança, para simular um usuário entrando a
// # senha e iniciando um modo privado de uso do sistema. Nesse caso da us1_1, ele está sendo usado meramente para testar a
// # senha (porque não podemos usar "expect getAtributoUsuario atributo=senha", justamente pelas questões de segurança). Se a senha
// # estiver correta, a sessão é aberta e o teste passa sem erros. Se a senha não estiver implementada corretamente (ou estiver errada), o
// # programa vai lançar uma exceção e o EasyAccept vai acusar erro nessa linha.
// abrirSessao login=jpsauve senha=sauvejp

// ###################
// # testes de todas as outras combinacoes importantes para o comando criarUsuario
// ###################
// # cada usuario esta associado a um unico login, com o qual eh identificado no sistema; dois usuarios nao podem ter o mesmo login

// expectError "Conta com esse nome já existe." criarUsuario login=jpsauve senha=##$@!# nome="Jacques Sauve II"

// # entretanto, se o login for diferente, dois usuários podem ter o mesmo nome

// criarUsuario login=jpsauve2 senha=sauvejp nome="Jacques Sauve"

// expect "Jacques Sauve" getAtributoUsuario login=jpsauve2 atributo=nome
// abrirSessao login=jpsauve2 senha=sauvejp

// expectError "Login inválido." criarUsuario login= senha=fasdh nome="Jacques Sauve"
// expectError "Senha inválida." criarUsuario login=jpsauve3 senha= nome="Jacques Sauve"
// # nome vazio pode
// criarUsuario login=jpsauve3 senha=3sauvejp nome=""

// ###################
// # testes de todas as outras combinacoes importantes para o comando abrirSessao
// ###################
// # observe que ao dar erro, não queremos dizer se o erro foi login ou senha
// # Isso é a forma padrao de tratar o assunto pois, desta forma, o hacker tem que advinhar ambos ao mesmo tempo
// # em vez de quebrar login e senha separadamente

// expectError "Login ou senha inválidos." abrirSessao login=jpsauve senha=x
// expectError "Login ou senha inválidos." abrirSessao login=x senha=x
// expectError "Login ou senha inválidos." abrirSessao login= senha=x
// expectError "Login ou senha inválidos." abrirSessao login=jpsauve senha=

// encerrarSistema
// quit


public class Facade {
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

    public Facade() {
        this.usuarios = new HashMap<>();
        this.usuarioLogado = null;
    }

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.usuarioLogado = null;
    }

    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.equals("")) {
            throw new RuntimeException("Login inválido.");
        }

        if (senha == null || senha.equals("")) {
            throw new RuntimeException("Senha inválida.");
        }

        if (this.usuarios.containsKey(login)) {
            throw new RuntimeException("Conta com esse nome já existe.");
        }
        this.usuarios.put(login, new Usuario(login, senha, nome));
    }

    public String abrirSessao(String login, String senha) {
        String idSessao = null;

        if (!this.usuarios.containsKey(login)) {
            throw new RuntimeException("Login ou senha inválidos.");
        }

        Usuario usuario = this.usuarios.get(login);

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Login ou senha inválidos.");
        }
        
        this.usuarioLogado = usuario;
        idSessao = login;
    
        return idSessao;
    }

    public String getAtributoUsuario(String login, String atributo) {
        if (!this.usuarios.containsKey(login)) {
            throw new RuntimeException("Usuário não cadastrado.");
        }

        Usuario usuario = this.usuarios.get(login);

        if (atributo.equals("nome")) {
            return usuario.getNome();
        } else {
            throw new RuntimeException("Atributo desconhecido.");
        }
    }

    public void encerrarSistema() {
        
    }

}
