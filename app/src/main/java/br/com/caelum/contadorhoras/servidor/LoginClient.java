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

    public static final String HTTPS_SISTEMA_CAELUM_COM_BR_8443_ANDROID_PROJETOS_DO_USUARIO = "https://sistema.caelum.com.br/android/projetosDoUsuario";
    private URL url;
    private String lista;

    public String post(String json) {
        try {
            url = new URL(HTTPS_SISTEMA_CAELUM_COM_BR_8443_ANDROID_PROJETOS_DO_USUARIO);
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
