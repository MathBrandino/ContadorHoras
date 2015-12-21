package br.com.caelum.contadorhoras.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by matheus on 17/11/15.
 */
public class HorasClient {

    private URL url;

    public int post(String json) {

        try {
            url = new URL("https://192.168.84.115/externo/android/salvaHoras");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            connection.setRequestProperty("Content-type", "application/json");
            PrintStream saida = new PrintStream(connection.getOutputStream());
            saida.println(json);

            connection.connect();

            return connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return HttpsURLConnection.HTTP_FORBIDDEN;
    }
}
