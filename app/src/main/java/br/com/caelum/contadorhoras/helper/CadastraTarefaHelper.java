package br.com.caelum.contadorhoras.helper;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.CadastraTarefaActivity;
import br.com.caelum.contadorhoras.dao.CategoriaDao;
import br.com.caelum.contadorhoras.modelo.Categoria;
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

    private Spinner atividades;
    private Tarefa tarefa;
    private CadastraTarefaActivity activity;

    public CadastraTarefaHelper(CadastraTarefaActivity activity) {
        this.activity = activity;

        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_cadastro_tarefa);
        tempoInicial = (TimePicker) activity.findViewById(R.id.cadastro_tempo_inicial);
        tempoFinal = (TimePicker) activity.findViewById(R.id.cadastro_tempo_final);
        inputLayout = (TextInputLayout) activity.findViewById(R.id.cadastro_tarefa_text_layout);
        descricao = (EditText) activity.findViewById(R.id.cadastro_descricao_tarefa);

        atividades = (Spinner) activity.findViewById(R.id.cadastro_tarefa_tipo);
        populaAtividades();

        tempoInicial.setIs24HourView(true);
        tempoFinal.setIs24HourView(true);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tarefa = new Tarefa();
    }

    private void populaAtividades() {

        CategoriaDao dao = new CategoriaDao(activity);
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, dao.getCategorias());
        dao.close();
        atividades.setAdapter(adapter);

    }

    public Tarefa pegaTarefaFormulario() {

        tarefa.setDescricao(descricao.getText().toString());
        tarefa.setIdCategoria(Long.valueOf(atividades.getSelectedItemPosition()));
        setaHoras();


        return tarefa;

    }

    private void setaHoras() {

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
    }

    private boolean validaDescricao() {
        if (descricao.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Não se esqueça da descrição !");
            return false;
        } else {
            return true;
        }
    }

    private boolean validaHoras() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (tempoInicial.getHour() > tempoFinal.getHour()) {

                return false;
            }
        } else {
            if (tempoInicial.getCurrentHour() > tempoFinal.getCurrentHour()) {
                return false;
            }
        }

        return true;
    }


    private boolean validaMinutos() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (tempoFinal.getHour() == tempoInicial.getHour() && tempoInicial.getMinute() > tempoFinal.getMinute()) {
                return false;
            }
        } else {
            if (tempoFinal.getCurrentHour() == tempoInicial.getCurrentHour() && tempoInicial.getCurrentMinute() > tempoFinal.getCurrentMinute()) {
                return false;
            }
        }

        return true;
    }

    private boolean validaQuantidadeHoras() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (tempoFinal.getMinute() == tempoInicial.getMinute() && tempoFinal.getHour() == tempoInicial.getHour()) {
                return false;
            }
        } else {
            if (tempoFinal.getCurrentMinute() == tempoInicial.getCurrentMinute() && tempoFinal.getCurrentHour() == tempoInicial.getCurrentHour()) {
                return false;
            }
        }

        return true;
    }

    private boolean validaTempo() {
        if (validaHoras() && validaMinutos() && validaQuantidadeHoras()) {
            return true;
        }
        Snackbar.make(descricao, "Por favor verifique as horas", Snackbar.LENGTH_LONG).show();
        return false;
    }

    public boolean validaFormulario() {
        if (validaDescricao() && validaTempo()) {
            return true;
        }
        return false;

    }

    public void colocaTarefaNoFormulario(Tarefa tarefa) {
        descricao.setText(tarefa.getDescricao());
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

        atividades.setSelection(tarefa.getIdCategoria().intValue());

        this.tarefa = tarefa;

    }
}
