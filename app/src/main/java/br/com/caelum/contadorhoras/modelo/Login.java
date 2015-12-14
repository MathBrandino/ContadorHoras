package br.com.caelum.contadorhoras.modelo;

/**
 * Created by matheus on 26/11/15.
 */
public class Login {

    private String senha;
    private String login;

    public Login(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
