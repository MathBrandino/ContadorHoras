package br.com.caelum.contadorhoras.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.helper.CadastraTarefaHelper;
import br.com.caelum.contadorhoras.modelo.Dia;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 11/11/15.
 */
public class CadastraTarefaActivity extends AppCompatActivity {

    private CadastraTarefaHelper helper;
    private Dia dia;
    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tarefa);


        helper = new CadastraTarefaHelper(this);

        verificaChegadaIntent();

    }

    private void verificaChegadaIntent() {
        if (getIntent().hasExtra("dia")) {

            dia = (Dia) getIntent().getSerializableExtra("dia");

        } else if (getIntent().hasExtra("tarefa")) {
            tarefa = (Tarefa) getIntent().getSerializableExtra("tarefa");
            helper.colocaTarefaNoFormulario(tarefa);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem salvar = menu.add("Salvar");
        salvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        salvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                if (helper.validaFormulario()) {
                    TarefaDao dao = new TarefaDao(CadastraTarefaActivity.this);

                    Tarefa tarefa = helper.pegaTarefaFormulario();

                    if (tarefa.getId() == null) {
                        tarefa.setDataDia(dia.getData());
                        dao.insere(tarefa);
                        Toast.makeText(CadastraTarefaActivity.this, "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        dao.altera(tarefa);
                        Toast.makeText(CadastraTarefaActivity.this, "Tarefa alterada com sucesso", Toast.LENGTH_SHORT).show();
                    }

                    dao.close();
                    finish();
                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
