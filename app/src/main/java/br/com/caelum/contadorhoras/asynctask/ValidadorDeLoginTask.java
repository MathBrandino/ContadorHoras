package br.com.caelum.contadorhoras.asynctask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.LoginActivity;
import br.com.caelum.contadorhoras.activity.MainActivity;
import br.com.caelum.contadorhoras.converter.CategoriaConverter;
import br.com.caelum.contadorhoras.converter.LoginConverter;
import br.com.caelum.contadorhoras.dao.CategoriaDao;
import br.com.caelum.contadorhoras.dao.LoginDao;
import br.com.caelum.contadorhoras.modelo.Categoria;
import br.com.caelum.contadorhoras.modelo.Login;
import br.com.caelum.contadorhoras.servidor.LoginClient;

/**
 * Created by matheus on 14/12/15.
 */
public class ValidadorDeLoginTask extends AsyncTask<Void, Void, String> {

    private Login login;
    private LoginActivity activity;
    private ProgressDialog progressDialog;

    public ValidadorDeLoginTask(Login login, LoginActivity activity) {
        this.login = login;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        criaAlerta();
    }


    @Override
    protected String doInBackground(Void... params) {

        LoginConverter loginConverter = new LoginConverter();
        String json = loginConverter.toJson(login);

        LoginClient client = new LoginClient();

        String lista = client.post(json);

        return lista;
    }

    @Override
    protected void onPostExecute(String lista) {
        super.onPostExecute(lista);

        validaRequisicao(lista);

        fechaDialog();
    }

    private void fechaDialog() {
        progressDialog.dismiss();
    }


    private void criaAlerta() {
        progressDialog = ProgressDialog.show(activity, "Aguarde", "Fazendo validação ", false, false);
    }

    private void validaRequisicao(String lista) {

        if (lista != null && !lista.trim().isEmpty()) {
            List<Categoria> categorias = geraCategorias(lista);

            verificaCategoriaExistente(categorias);

            validaLoginBanco();

            vaiParaMain();

            finalizaFormulario();

        } else {
            mostraErro();
        }
    }

    private void finalizaFormulario() {

        activity.finish();

    }

    private void validaLoginBanco() {

        LoginDao dao = new LoginDao(activity);
        dao.valida(login);
        dao.close();
    }

    private void mostraErro() {

        new AlertDialog.Builder(activity)
                .setIcon(R.drawable.temp)
                .setTitle("Tivemos algum problema")
                .setMessage("Verifique sua conexão ou suas informações e tente novamente")
                .setCancelable(true)
                .show();
    }

    private void verificaCategoriaExistente(List<Categoria> categorias) {

        CategoriaDao dao = new CategoriaDao(activity);
        dao.verificaCategorias(categorias);
        dao.close();

    }

    private List<Categoria> geraCategorias(String lista) {

        CategoriaConverter categoriaConverter = new CategoriaConverter();
        List<Categoria> categorias = categoriaConverter.fromJson(lista);

        return categorias;
    }

    private void vaiParaMain() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);

    }
}
