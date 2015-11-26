package br.com.caelum.contadorhoras.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by matheus on 26/11/15.
 */
public class ReceptorDeToken {

    private URL url;
    private String token;

    public String post(String json){
        try {
            url = new URL("https://caelumweb.caelum.com.br/caelumweb");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintStream stream = new PrintStream(connection.getOutputStream());
            stream.println(json);

            connection.connect();

            token = new Scanner(connection.getInputStream()).next();

            return token;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }
}
