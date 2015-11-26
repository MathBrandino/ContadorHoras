package br.com.caelum.contadorhoras.converter;

import com.google.gson.Gson;

import br.com.caelum.contadorhoras.modelo.Login;

/**
 * Created by matheus on 26/11/15.
 */
public class LoginConverter {

    public String toJson(Login login){

        Gson gson = new Gson();
        return gson.toJson(login);

    }
}
