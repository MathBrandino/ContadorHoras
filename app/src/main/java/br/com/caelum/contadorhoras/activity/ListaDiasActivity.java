package br.com.caelum.contadorhoras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.modelo.Dia;

/**
 * Created by matheus on 12/11/15.
 */
public class ListaDiasActivity extends AppCompatActivity {

    private ListView listaDeDias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dias);

        criaTela();

        carregaLista();

        final ListaDiasActivity that = this;
        listaDeDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dia dia = (Dia) parent.getItemAtPosition(position);

                Intent intent = new Intent(that, CadastraTarefaActivity.class);
                intent.putExtra("dia", dia);
                startActivity(intent);

                finish();

            }
        });
    }

    private void criaTela() {
        colocaToolbarNaTela();

        listaDeDias = (ListView) findViewById(R.id.lista_dias_cadastrados);
    }

    private void carregaLista() {
        DiaDao dao = new DiaDao(this);
        List<Dia> dias = dao.pegaDias();
        dao.close();
        ArrayAdapter<Dia> adapter = new ArrayAdapter<Dia>(this, android.R.layout.simple_list_item_1, dias);

        listaDeDias.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void colocaToolbarNaTela() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cadastro_dias_cadastrados);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
