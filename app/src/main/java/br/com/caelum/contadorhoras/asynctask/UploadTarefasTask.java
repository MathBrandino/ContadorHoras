package br.com.caelum.contadorhoras.asynctask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.servidor.HorasClient;

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

        HorasClient client = new HorasClient();

        String post = client.post(json);

        return post;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null && !s.trim().isEmpty()) {

            finalizaRequest();

        } else {
            mostraErro();
        }
        alertDialog.dismiss();

    }

    private void finalizaRequest() {
        removeDia();
        saiDaTela();
        avisaUsuario();
    }

    private void saiDaTela() {
        activity.finish();
    }

    private void mostraErro() {
        new AlertDialog.Builder(activity)
                .setIcon(R.drawable.temp)
                .setMessage("Ocorreu algum erro ")
                .setTitle("Atenção")
                .setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new UploadTarefasTask(json, activity).execute();
                    }
                })
                .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void avisaUsuario() {
        Toast.makeText(activity, "Upload realizado com sucesso !", Toast.LENGTH_LONG).show();
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
        alertDialog = ProgressDialog.show(activity, "Aguarde ...", "Enviando dados para o Caelum Web", false, false);
    }
}
