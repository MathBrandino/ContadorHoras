package br.com.caelum.contadorhoras.modelo;

/**
 * Created by matheus on 26/11/15.
 */
public class Login {

    private String senha;
    private String usuario;

    public Login(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
