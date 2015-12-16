package br.com.caelum.contadorhoras.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
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
            url = new URL("https://sistema.caelum.com.br:8443/android/projetosDoUsuario");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintStream saida = new PrintStream(connection.getOutputStream());
            saida.println(json);

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());


            while (scanner.hasNext())
                lista += scanner.next();

            lista = lista.substring(4);
            return lista;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
