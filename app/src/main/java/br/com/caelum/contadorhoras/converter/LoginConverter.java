package br.com.caelum.contadorhoras.converter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.caelum.contadorhoras.modelo.Login;

/**
 * Created by matheus on 26/11/15.
 */
public class LoginConverter {

    public static final String NAME = "br.com.caelum.caelumweb2.modelo.pessoas.Usuario";

    public String toJson(Login login) {

        Gson gson = new Gson();

        String senha = login.getSenha();

        MessageDigest m = decodeMD5(senha);

        String senhaDecodificada = new BigInteger(1, m.digest()).toString(16);

        login.setSenha(senhaDecodificada);

        String json = gson.toJson(login);

        StringWriter writer = new StringWriter();

        Gson gsonFinal = new Gson();
        try {

            JsonWriter jsonWriter = gsonFinal.newJsonWriter(writer);
            jsonWriter.beginObject().name(NAME).jsonValue(json).endObject();
            jsonWriter.close();


            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    @NonNull
    private MessageDigest decodeMD5(String senha) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(senha.getBytes(), 0, senha.length());
        return m;
    }
}
