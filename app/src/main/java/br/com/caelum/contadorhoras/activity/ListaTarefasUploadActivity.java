package br.com.caelum.contadorhoras.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import br.com.caelum.contadorhoras.asynctask.UploadTarefasTask;
import br.com.caelum.contadorhoras.converter.TarefaConverter;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.modelo.Dia;
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
    private FloatingActionButton sobeHoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tarefas_upload);

        criaTela();

        pegaDia();

        pegaListaTarefas();

        vaiParaTarefaSelecionada();

        fazUploadDasHoras();

    }

    private void pegaDia() {
        dia = (Dia) getIntent().getSerializableExtra("dia");
    }

    private void fazUploadDasHoras() {
        sobeHoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaSeDesejaSubirHoras();
            }
        });
    }

    private void verificaSeDesejaSubirHoras() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Deseja fazer upload das horas ?")
                .setPositiveButton("Sim ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertaComJson();
                        //enviaJsonParaServidor(geraJson());

                    }
                })
                .setNegativeButton("Ainda não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void vaiParaTarefaSelecionada() {
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
        sobeHoras = (FloatingActionButton) findViewById(R.id.upload);
    }

    private String devolveQuantidadeDeHoras(List<Tarefa> tarefas, Long contador) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Tarefa tarefa : tarefas) {

            DiaDao dao = new DiaDao(this);
            String dia = dao.getData(tarefa.getDataDia());
            dao.close();
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


    private String geraJson() {

        return new TarefaConverter().toJson(tarefas);
    }

    private AlertDialog alertaComJson() {
        return new AlertDialog.Builder(this).setMessage(geraJson()).show();
    }

    private void enviaJsonParaServidor(String json) {

        new UploadTarefasTask(json, this).execute();

    }
}
