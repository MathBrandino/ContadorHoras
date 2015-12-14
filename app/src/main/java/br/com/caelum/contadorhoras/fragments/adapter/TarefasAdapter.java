package br.com.caelum.contadorhoras.fragments.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 10/11/15.
 */
public class TarefasAdapter extends BaseAdapter {


    private final Activity activity;
    private final List<Tarefa> tarefas;

    public TarefasAdapter(Activity activity, List<Tarefa> tarefas) {
        this.activity = activity;
        this.tarefas = tarefas;
    }

    @Override
    public int getCount() {
        return tarefas.size();
    }

    @Override
    public Object getItem(int position) {
        return tarefas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tarefas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.tarefa_item, parent, false);
        } else {
            view = convertView;
        }

        Tarefa tarefa = tarefas.get(position);

        TextView tarefaDia = (TextView) view.findViewById(R.id.tarefa_item_dia);
        TextView tempoInicial = (TextView) view.findViewById(R.id.tempo_inicial);
        TextView tempoFinal = (TextView) view.findViewById(R.id.tempo_final);
        TextView descricao = (TextView) view.findViewById(R.id.descricao_tarefa);

        DiaDao dao = new DiaDao(activity);
        tarefaDia.setText(dao.getData(tarefa.getDataDia()));
        dao.close();

        tempoInicial.setText("" + tarefa.getHoraInicial() + " : " + tarefa.getMinutoInicial());
        tempoFinal.setText("" + tarefa.getHoraFinal() + " : " + tarefa.getMinutoFinal());

        descricao.setText(tarefa.getDescricao());

        return view;
    }
}
