package br.com.caelum.contadorhoras.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
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

        View view = criaView(convertView, parent);

        populaView(position, view);

        return view;
    }

    private View criaView(View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.tarefa_upload_item, parent, false);
        } else {
            view = convertView;
        }
        return view;
    }

    private void populaView(int position, View view) {
        TextView descricao = (TextView) view.findViewById(R.id.descricao_tarefa_upload);
        descricao.setText(tarefas.get(position).getDesc());

        TextView horas = (TextView) view.findViewById(R.id.quantidade_horas);
        horas.setText(quantidadeHoras(tarefas.get(position)));
    }

    private String quantidadeHoras(Tarefa tarefa) {
        String horasTrabalhadas;
        Long contador = 0L;

        DiaDao dao = new DiaDao(activity);
        String dia = dao.getData(tarefa.getDataDia());
        dao.close();
        int horaInicial = tarefa.getHoraInicial();
        int minutoInicial = tarefa.getMinutoInicial();
        int horaFinal = tarefa.getHoraFinal();
        int minutoFinal = tarefa.getMinutoFinal();
        String tempoInicial = dia + " " + horaInicial + ":" + minutoInicial;
        String tempoFinal = dia + " " + horaFinal + ":" + minutoFinal;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date dateFinal = null;
        Date dateInicial = null;
        try {
            dateFinal = dateFormat.parse(tempoFinal);
            dateInicial = dateFormat.parse(tempoInicial);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diferenca = dateFinal.getTime() - dateInicial.getTime();
        contador += diferenca;

        long hora = contador / (3600 * 1000);
        long minutos = (contador % (3600 * 1000)) / (1000 * 60);

        horasTrabalhadas = hora + ":" + minutos;

        return horasTrabalhadas;

    }
}
