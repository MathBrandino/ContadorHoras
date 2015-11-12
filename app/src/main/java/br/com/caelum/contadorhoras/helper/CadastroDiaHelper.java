package br.com.caelum.contadorhoras.helper;

import android.support.v7.widget.Toolbar;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_cadastro_dia_trabalhado);
        this.activity.setSupportActionBar(toolbar);
        this.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendario = (CalendarView) activity.findViewById(R.id.calendario);
        data = (TextView) activity.findViewById(R.id.data_inserida);
        dia = new Dia();
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
        return !data.getText().toString().contains("Selecione");
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
}
