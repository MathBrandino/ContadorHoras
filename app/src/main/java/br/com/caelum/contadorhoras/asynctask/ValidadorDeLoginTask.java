package br.com.caelum.contadorhoras.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.MainActivity;
import br.com.caelum.contadorhoras.converter.CategoriaConverter;
import br.com.caelum.contadorhoras.dao.CategoriaDao;
import br.com.caelum.contadorhoras.modelo.Categoria;
import br.com.caelum.contadorhoras.modelo.Login;
import br.com.caelum.contadorhoras.servidor.LoginClient;

/**
 * Created by matheus on 14/12/15.
 */
public class ValidadorDeLoginTask extends AsyncTask<Void, Void, String> {

    private Login login;
    private Context ctx;
    private ProgressDialog progressDialog;

    public ValidadorDeLoginTask(Login login, Context ctx) {
        this.login = login;
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(ctx, "Aguarde", "Fazendo validação ", false, false);
    }

    @Override
    protected String doInBackground(Void... params) {

        /*LoginConverter loginConverter = new LoginConverter();
        String json = loginConverter.toJson(login);
*/
        LoginClient client = new LoginClient();
   //     String post = client.post(json);

        String lista = client.get(login.getLogin(), login.getSenha());

        return lista;
    }

    @Override
    protected void onPostExecute(String lista) {
        super.onPostExecute(lista);

        validaRequisicao(lista);

        progressDialog.dismiss();
    }

    private void validaRequisicao(String lista) {
        Log.i("lista", lista);
        if (lista != null && !lista.trim().isEmpty()) {
            List<Categoria> categorias = geraCategorias(lista);

            verificaCategoriaExistente(categorias);

            vaiParaMain();

        } else {
            mostraErro();
        }
    }

    private void mostraErro() {

        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.temp)
                .setTitle("Tivemos algum problema")
                .setMessage("Verifique sua conexão ou suas informações e tente novamente")
                .setCancelable(true)
                .show();
    }

    private void verificaCategoriaExistente(List<Categoria> categorias) {

        CategoriaDao dao = new CategoriaDao(ctx);
        dao.verificaCategorias(categorias);
        dao.close();

    }

    private List<Categoria> geraCategorias(String lista) {

        CategoriaConverter categoriaConverter = new CategoriaConverter();
        List<Categoria> categorias = categoriaConverter.fromJson(lista);

        return categorias;
    }

    private void vaiParaMain(){
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }
}
