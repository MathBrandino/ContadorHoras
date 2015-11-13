package br.com.caelum.contadorhoras.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.adapter.TarefasUploadAdapter;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.modelo.Dia;
import br.com.caelum.contadorhoras.modelo.Login;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 12/11/15.
 */
public class ListaTarefasUploadActivity extends AppCompatActivity {

    private ListView listaTarefas;
    private Toolbar toolbar;
    private Dia dia;
    private TextView mensagemQuantidadeHoras;
    private List<Tarefa> tarefas;
    private long contador = 0;
    private String horasFinal;
    private FloatingActionButton fab;
    private TextView login;
    private TextView senha;
    private AlertDialog alert;
    private Login caelumWebLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tarefas_upload);

        criaTela();

        dia = (Dia) getIntent().getSerializableExtra("dia");

        pegaListaTarefas();

        listaTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarefa tarefa = (Tarefa) parent.getItemAtPosition(position);

                Intent intent = new Intent(ListaTarefasUploadActivity.this, CadastraTarefaActivity.class);
                intent.putExtra("tarefa", tarefa);
                startActivity(intent);

            }
        });

    }

    private void pegaListaTarefas() {
        TarefaDao dao = new TarefaDao(this);
        tarefas = dao.pegaTarefasDoDia(dia);
        dao.close();

        TarefasUploadAdapter adapter = new TarefasUploadAdapter(tarefas, this);
        listaTarefas.setAdapter(adapter);

        recarregaHoras();

    }

    private void criaTela() {
        colocarToolbarNaTela();
        listaTarefas = (ListView) findViewById(R.id.lista_tarefas_upload);
        mensagemQuantidadeHoras = (TextView) findViewById(R.id.mostra_horas);
    }

    private String devolveQuantidadeDeHoras(List<Tarefa> tarefas, Long contador) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Tarefa tarefa : tarefas) {

            DiaDao dao = new DiaDao(this);
            String dia = dao.getData(tarefa.getIdDia());
            int horaInicial = tarefa.getHoraInicial();
            int minutoInicial = tarefa.getMinutoInicial();
            int horaFinal = tarefa.getHoraFinal();
            int minutoFinal = tarefa.getMinutoFinal();
            String tempoInicial = dia + " " + horaInicial + ":" + minutoInicial;
            String tempoFinal = dia + " " + horaFinal + ":" + minutoFinal;

            try {
                Date dateInicial = dateFormat.parse(tempoInicial);

                Date dateFinal = dateFormat.parse(tempoFinal);

                long diferenca = dateFinal.getTime() - dateInicial.getTime();
                contador += diferenca;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        long hora = contador / (3600 * 1000);
        long minutos = (contador % (3600 * 1000)) / (1000 * 60);

        horasFinal = hora + ":" + minutos;
        return horasFinal;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pegaListaTarefas();
    }

    private void recarregaHoras() {
        mensagemQuantidadeHoras.setText("Quantidade de horas :  " + devolveQuantidadeDeHoras(tarefas, contador));
    }

    private void colocarToolbarNaTela() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_lista_tarefas_upload);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem subirHoras = menu.add("Subir horas");
        subirHoras.setIcon(android.R.drawable.ic_menu_upload);
        subirHoras.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        subirHoras.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final View caelumWebView = getLayoutInflater().inflate(R.layout.mensagem_caelum_web, null);

                populaTelaDeLoginCaelumWeb(caelumWebView);

                alert = criaAlertView(caelumWebView);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (validaFormulario()) {
                            caelumWebLogin = geraLogin();

                            alert.dismiss();
                        }
                    }
                });


                return true;
            }
        });

        return true;
    }

    private boolean validaFormulario() {
        if (verificaLogin() && verificaSenha()) {
            return true;
        }
        return false;
    }

    private boolean verificaLogin() {
        TextInputLayout loginLayout = (TextInputLayout) login.getParent();
        if (login.getText().toString().trim().isEmpty()) {
            loginLayout.setError("Login Invalido");
            return false;
        }
        if (loginLayout.getError() != null && loginLayout.getError().length() > 2) {
            loginLayout.setError("");
        }
        return true;
    }

    private boolean verificaSenha() {
        TextInputLayout loginLayout = (TextInputLayout) senha.getParent();

        if (senha.getText().toString().trim().isEmpty()) {
            loginLayout.setError("Senha Invalida");
            return false;
        }

        if (loginLayout.getError() != null && loginLayout.getError().length() > 2) {
            loginLayout.setError("");
        }
        return true;
    }

    private Login geraLogin() {
        String loginCaelumWeb = login.getText().toString();
        String senhaCaelumWeb = senha.getText().toString();
        return new Login(loginCaelumWeb, senhaCaelumWeb);
    }

    private void populaTelaDeLoginCaelumWeb(View caelumWebView) {
        fab = (FloatingActionButton) caelumWebView.findViewById(R.id.botao_caelum_web);
        login = (TextView) caelumWebView.findViewById(R.id.login_caelum_web);
        senha = (TextView) caelumWebView.findViewById(R.id.senha_caelum_web);
    }

    private AlertDialog criaAlertView(View caelumWebView) {
        return new AlertDialog.Builder(ListaTarefasUploadActivity.this).setView(caelumWebView)
                .setTitle("CaelumWeb")
                .show();
    }
}