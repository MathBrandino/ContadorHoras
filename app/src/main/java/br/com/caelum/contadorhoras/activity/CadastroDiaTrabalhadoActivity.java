package br.com.caelum.contadorhoras.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.helper.CadastroDiaHelper;
import br.com.caelum.contadorhoras.modelo.Dia;

/**
 * Created by matheus on 11/11/15.
 */
public class CadastroDiaTrabalhadoActivity extends AppCompatActivity {

    private CadastroDiaHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_dia_trabalhado);

        helper = new CadastroDiaHelper(this);

        verificaIntentRecebida();

        selecionaDataAtual();

        pegaDataCalendario();
    }

    private void selecionaDataAtual() {
        long date = helper.getCalendario().getDate();
        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(date);
        String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(data.getTime());
        helper.getData().setText(dataFormatada);
    }

    private void verificaIntentRecebida() {
        if (getIntent().hasExtra("dia")) {
            Dia dia = (Dia) getIntent().getSerializableExtra("dia");
            helper.colocaDiaNoFormulario(dia);
        }
    }

    private void pegaDataCalendario() {
        helper.getCalendario().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date date = new Date(calendar.getTimeInMillis());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                helper.getData().setText(dateFormat.format(date));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem salvar = menu.add("Salvar");
        salvar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        salvar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Dia dia = helper.geraDiaFormulario();
                DiaDao dao = new DiaDao(CadastroDiaTrabalhadoActivity.this);
                if (helper.validaData()) {
                    if (dia.getId() == null) {
                        if(dao.verificaDiaJaExiste(dia.getData())) {
                            fazInsercao(dia, dao);
                        } else {
                            Snackbar.make(helper.getData(), "Você já possui um registro com essa data", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        fazAlteracao(dia, dao);
                    }

                    dao.close();

                } else {
                    Snackbar.make(helper.getData(), "Selecione uma data, por gentileza", Snackbar.LENGTH_LONG).show();
                }

                return true;
            }
        });

        return true;
    }

    private void fazInsercao(Dia dia, DiaDao dao) {
        dao.insere(dia);
        Toast.makeText(CadastroDiaTrabalhadoActivity.this, "Dia salvo com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void fazAlteracao(Dia dia, DiaDao dao) {
        dao.altera(dia);
        Toast.makeText(CadastroDiaTrabalhadoActivity.this, "Dia alterado com sucesso", Toast.LENGTH_SHORT).show();
        finish();
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
