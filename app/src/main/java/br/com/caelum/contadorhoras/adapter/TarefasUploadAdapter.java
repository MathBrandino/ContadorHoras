package br.com.caelum.contadorhoras.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 12/11/15.
 */
public class TarefasUploadAdapter extends BaseAdapter {

    private final ListaTarefasUploadActivity activity;
    private final List<Tarefa> tarefas;

    public TarefasUploadAdapter(List<Tarefa> tarefas, ListaTarefasUploadActivity activity) {
        this.tarefas = tarefas;
        this.activity = activity;
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
            view = activity.getLayoutInflater().inflate(R.layout.tarefa_upload_item, parent, false);
        } else {
            view = convertView;
        }

        TextView descricao = (TextView) view.findViewById(R.id.descricao_tarefa_upload);
        descricao.setText(tarefas.get(position).getDesc());

        return view;
    }
}
