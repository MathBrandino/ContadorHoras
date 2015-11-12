package br.com.caelum.contadorhoras.helper;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TimePicker;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.CadastraTarefaActivity;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 11/11/15.
 */
public class CadastraTarefaHelper {

    private Toolbar toolbar;

    private TimePicker tempoInicial;
    private TimePicker tempoFinal;

    private TextInputLayout inputLayout;
    private EditText descricao;

    private Tarefa tarefa;

    public CadastraTarefaHelper(CadastraTarefaActivity activity) {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_cadastro_tarefa);
        tempoInicial = (TimePicker) activity.findViewById(R.id.cadastro_tempo_inicial);
        tempoFinal = (TimePicker) activity.findViewById(R.id.cadastro_tempo_final);

        tempoInicial.setIs24HourView(true);
        tempoFinal.setIs24HourView(true);

        inputLayout = (TextInputLayout) activity.findViewById(R.id.cadastro_tarefa_text_layout);
        descricao = (EditText) activity.findViewById(R.id.cadastro_descricao_tarefa);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tarefa = new Tarefa();
    }

    public Tarefa pegaTarefaFormulario() {

        tarefa.setDesc(descricao.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tarefa.setHoraInicial(tempoInicial.getHour());
            tarefa.setMinutoInicial(tempoInicial.getMinute());
            tarefa.setHoraFinal(tempoFinal.getHour());
            tarefa.setMinutoFinal(tempoFinal.getMinute());
        } else {
            tarefa.setHoraInicial(tempoInicial.getCurrentHour());
            tarefa.setMinutoInicial(tempoInicial.getCurrentMinute());
            tarefa.setHoraFinal(tempoFinal.getCurrentHour());
            tarefa.setMinutoFinal(tempoFinal.getCurrentMinute());
        }

        return tarefa;

    }

    public boolean validaDescricao() {
        if (descricao.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Não se esqueça da descrição !");
            return false;
        } else {
            return true;
        }
    }


    public void colocaTarefaNoFormulario(Tarefa tarefa) {
        descricao.setText(tarefa.getDesc());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tempoFinal.setHour(tarefa.getHoraFinal());
            tempoFinal.setMinute(tarefa.getMinutoFinal());
            tempoInicial.setHour(tarefa.getHoraInicial());
            tempoInicial.setMinute(tarefa.getMinutoInicial());
        } else {
            tempoFinal.setCurrentHour(tarefa.getHoraFinal());
            tempoFinal.setCurrentMinute(tarefa.getMinutoFinal());
            tempoInicial.setCurrentHour(tarefa.getHoraInicial());
            tempoInicial.setCurrentMinute(tarefa.getMinutoInicial());
        }
        this.tarefa = tarefa;

    }
}
