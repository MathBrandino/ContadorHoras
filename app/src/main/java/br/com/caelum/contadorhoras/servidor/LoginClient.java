package br.com.caelum.contadorhoras.servidor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by matheus on 26/11/15.
 */
public class LoginClient {

    private URL url;
    private String lista;

    public String post(String json) {
        try {
            url = new URL("https://caelumweb.caelum.com.br/caelumweb/android/login");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintStream stream = new PrintStream(connection.getOutputStream());
            stream.println(json);

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNext())
                lista += scanner.next();

            return lista;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    public String get(String login, String senha) {

        try {
/*
            MessageDigest m = decodeMD5(senha);

            String string = new BigInteger(1, m.digest()).toString(16);*/

            url = new URL("https://sistema.caelum.com.br:8443/android/projetosDoUsuario?login="+login+"&senha="+senha);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");

            connection.setRequestMethod("GET");

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNext())
                lista += scanner.next();

            String substring = lista.substring(4);
            return substring;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    // metodo para converter a senha para md5
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
