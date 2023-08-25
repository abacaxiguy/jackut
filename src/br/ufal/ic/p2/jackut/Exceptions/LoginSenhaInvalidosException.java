package br.ufal.ic.p2.jackut.Exceptions;

public class LoginSenhaInvalidosException extends RuntimeException {
    public LoginSenhaInvalidosException(String message) {
        super(message.equals("login") ? "Login inválido." : message.equals("senha") ? "Senha inválida." : "Login ou senha inválidos.");
    }
    
}
