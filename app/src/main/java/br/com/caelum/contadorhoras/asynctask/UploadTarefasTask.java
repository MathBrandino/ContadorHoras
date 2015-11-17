package br.com.caelum.contadorhoras.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import br.com.caelum.contadorhoras.servidor.WebClient;

/**
 * Created by matheus on 17/11/15.
 */
public class UploadTarefasTask extends AsyncTask<Void, Void, String> {

    private String json;
    private Activity activity;
    private ProgressDialog alertDialog;

    public UploadTarefasTask(String json, Activity activity) {
        this.json = json;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {

        WebClient webClient = new WebClient();

        String post = webClient.post(json);

        return post;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        alertDialog.dismiss();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = ProgressDialog.show(activity, "Por favor aguarde", "Enviando dados para o Caelum Web", false, false);
    }
}
