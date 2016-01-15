package br.com.caelum.contadorhoras.asynctask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.servidor.HorasClient;

/**
 * Created by matheus on 17/11/15.
 */
public class UploadTarefasTask extends AsyncTask<Void, Void, Integer> {

    private String json;
    private ListaTarefasUploadActivity activity;
    private ProgressDialog alertDialog;

    public UploadTarefasTask(String json, ListaTarefasUploadActivity activity) {
        this.json = json;
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        HorasClient client = new HorasClient();

        int code = client.post(json);

        return code;
    }

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);
        if (code == HttpsURLConnection.HTTP_ACCEPTED) {

            finalizaRequest();

        } else {
            mostraErro();
        }
        fechaAlerta();

    }

    private void fechaAlerta() {
        alertDialog.dismiss();
    }

    private void finalizaRequest() {
        removeDia();
        saiDaTela();
        avisaUsuarioSucesso();
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

    private void avisaUsuarioSucesso() {
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
