package br.com.caelum.contadorhoras.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by matheus on 17/11/15.
 */
public class EnviadorDeJson {

    private URL url;

    public String post(String json) {

        try {
            url = new URL("https://caelumweb.caelum.com.br/caelumweb");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            PrintStream saida = new PrintStream(connection.getOutputStream());
            saida.println(json);

            connection.connect();
            String resposta = new Scanner(connection.getInputStream()).next();

            return resposta;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
