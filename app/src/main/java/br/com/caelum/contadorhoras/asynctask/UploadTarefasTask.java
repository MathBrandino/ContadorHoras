package br.com.caelum.contadorhoras.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.servidor.EnviadorDeJson;

/**
 * Created by matheus on 17/11/15.
 */
public class UploadTarefasTask extends AsyncTask<Void, Void, String> {

    private String json;
    private ListaTarefasUploadActivity activity;
    private ProgressDialog alertDialog;

    public UploadTarefasTask(String json, ListaTarefasUploadActivity activity) {
        this.json = json;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {

        EnviadorDeJson enviadorDeJson = new EnviadorDeJson();

        String post = enviadorDeJson.post(json);

        return post;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if ( s != null && !s.trim().isEmpty()){

            removeDia();
            activity.finish();
            avisaUsuario();
        }
        alertDialog.dismiss();

    }

    private void avisaUsuario() {
        Toast.makeText(activity , "Upload realizado com sucesso !", Toast.LENGTH_LONG).show();
    }

    private void removeDia() {
        DiaDao dao = new DiaDao(activity);
        dao.deleta(activity.pegaDia());
        dao.close();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        geraAlerta();
    }

    private void geraAlerta() {
        alertDialog = ProgressDialog.show(activity, "Por favor aguarde", "Enviando dados para o Caelum Web", false, false);
    }
}
