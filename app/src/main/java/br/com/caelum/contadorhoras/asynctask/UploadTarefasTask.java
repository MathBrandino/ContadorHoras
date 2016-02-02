package br.com.caelum.contadorhoras.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import br.com.caelum.contadorhoras.delegate.LancaHorasDelegate;
import br.com.caelum.contadorhoras.servidor.HorasClient;

/**
 * Created by matheus on 17/11/15.
 */
public class UploadTarefasTask extends AsyncTask<Void, Void, Integer> {

    private String json;
    private LancaHorasDelegate delegate;
    private ProgressDialog alertDialog;
    private Exception erro;

    public UploadTarefasTask(String json, LancaHorasDelegate delegate) {
        this.json = json;
        this.delegate = delegate;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        try {
            HorasClient client = new HorasClient();

            Log.i("json", json);

            int code = client.post(json);

            return code;
        } catch (Exception erro){
            this.erro = erro;
            return null;
        }

    }

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);


        if (code != null){
            delegate.lidaComRetorno(code);
        } else {
            delegate.lidaComErro(erro);
        }

        fechaAlerta();
    }

    private void fechaAlerta() {
        alertDialog.dismiss();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        geraAlerta();
    }

    private void geraAlerta() {
        alertDialog = ProgressDialog.show(delegate.getContext(), "Aguarde ...", "Enviando dados para o Caelum Web", false, false);
    }
}
