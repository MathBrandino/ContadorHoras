package br.com.caelum.contadorhoras.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by matheus on 17/11/15.
 */
public class HorasClient {

    public static final String HTTPS_SISTEMA_CAELUM_COM_BR_8443_ANDROID_SALVA_HORAS = "https://sistema.caelum.com.br:8443/android/salvaHoras";
    private URL url;

    public int post(String json) {

        try {
            url = new URL(HTTPS_SISTEMA_CAELUM_COM_BR_8443_ANDROID_SALVA_HORAS);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

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
