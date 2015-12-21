package br.com.caelum.contadorhoras.helper;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.CadastroDiaTrabalhadoActivity;
import br.com.caelum.contadorhoras.modelo.Dia;

/**
 * Created by matheus on 11/11/15.
 */
public class CadastroDiaHelper {


    private final CadastroDiaTrabalhadoActivity activity;
    private TextView data;
    private CalendarView calendario;
    private Toolbar toolbar;
    private Dia dia;

    public CadastroDiaHelper(CadastroDiaTrabalhadoActivity activity) {

        this.activity = activity;

        preparaViews();

        dia = new Dia();
    }

    private void preparaViews() {
        preparaToolbar();

        calendario = (CalendarView) activity.findViewById(R.id.calendario);
        data = (TextView) activity.findViewById(R.id.data_inserida);
    }

    private void preparaToolbar() {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_cadastro_dia_trabalhado);
        this.activity.setSupportActionBar(toolbar);
        this.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Dia geraDiaFormulario() {

        dia.setData(data.getText().toString());
        return dia;
    }

    public CalendarView getCalendario() {
        return calendario;
    }

    public TextView getData() {
        return data;
    }

    public boolean validaData() {

        Date dataAtual = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataSelecionada = null;
        try {
            dataSelecionada = format.parse(dia.getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendarAtual = Calendar.getInstance();
        calendarAtual.setTime(dataAtual);

        Calendar calendarSelecionado = Calendar.getInstance();
        calendarSelecionado.setTime(dataSelecionada);

        int diaAtual = calendarAtual.get(Calendar.DAY_OF_YEAR);
        int diaSelecionado = calendarSelecionado.get(Calendar.DAY_OF_YEAR);


        if (isTomorrow(diaAtual, diaSelecionado)) {

            erroDataAdiantada();

            return false;
        }

        if (hasTwoWeeks(diaAtual, diaSelecionado)) {

            erroDataLimiteUltrapassada();

            return false;
        }

        return true;
    }

    private void erroDataLimiteUltrapassada() {
        Snackbar.make(data, "Você não pode marcar essa data, limite de 14 dias ultrapassado.", Snackbar.LENGTH_SHORT).show();
    }

    private void erroDataAdiantada() {
        Snackbar.make(data, "Você não pode marcar um dia posterior ao de hoje, selecione um dia valido", Snackbar.LENGTH_LONG).show();
    }

    private boolean hasTwoWeeks(int diaAtual, int diaSelecionado) {


        int diaMinimo = (diaAtual - 14);
        if (diaMinimo >= diaSelecionado) {
            return true;
        }

        return false;
    }

    private boolean isTomorrow(int diaAtual, int diaSelecionado) {
        if (diaSelecionado > diaAtual) {
            return true;
        }

        return false;
    }

    public void colocaDiaNoFormulario(Dia dia) {
        this.dia = dia;
        data.setText(dia.getData());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date parse = format.parse(dia.getData());
            calendario.setDate(parse.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void mostraErroDataJaExiste() {
        Snackbar.make(getData(), "Você já possui um registro com essa data", Snackbar.LENGTH_SHORT).show();


    }
}
