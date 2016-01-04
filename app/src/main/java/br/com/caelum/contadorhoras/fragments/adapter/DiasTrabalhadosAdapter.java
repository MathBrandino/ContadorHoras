package br.com.caelum.contadorhoras.fragments.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.modelo.Dia;
import br.com.caelum.contadorhoras.modelo.Tarefa;


/**
 * Created by matheus on 11/11/15.
 */
public class DiasTrabalhadosAdapter extends BaseAdapter {
    private final List<Dia> dias;
    private final FragmentActivity activity;
    private TextView semana;
    private TextView data;
    private Dia dia;
    private ImageView sinal;
    private TextView quantidadeTarefas;

    public DiasTrabalhadosAdapter(FragmentActivity activity, List<Dia> dias) {
        this.activity = activity;
        this.dias = dias;
    }

    @Override
    public int getCount() {
        return dias.size();
    }

    @Override
    public Object getItem(int position) {
        return dias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dias.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = criaView(convertView, parent);

        buscaViews(view);

        colocaDia(position);

        populaImageView();

        colocaDiaDaSemana();

        colocaQuantidadeDeTarefas();


        return view;
    }

    private View criaView(View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.dias_trabalhados_item, parent, false);

        } else {
            view = convertView;
        }
        return view;
    }

    private void colocaQuantidadeDeTarefas() {
        TarefaDao dao = new TarefaDao(activity);
        List<Tarefa> tarefas = dao.pegaTarefasDoDia(dia);
        dao.close();

        quantidadeTarefas.setText(String.valueOf(tarefas.size()));
    }

    private void colocaDia(int position) {
        dia = dias.get(position);

        data.setText(dia.getData());
    }

    private void colocaDiaDaSemana() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date parse = dateFormat.parse(dia.getData());
            long time = parse.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            semana.setText("" + geraNomeDia(calendar.get(calendar.DAY_OF_WEEK)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void buscaViews(View view) {
        data = (TextView) view.findViewById(R.id.data_dia);
        semana = (TextView) view.findViewById(R.id.nome_dia);
        sinal = (ImageView) view.findViewById(R.id.sinal);
        quantidadeTarefas = (TextView) view.findViewById(R.id.tarefas_dia);
    }

    private void populaImageView() {
        Bitmap bm;

        if (hasTarefa()) {
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.positivo);
        } else {
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.negativo);
        }


        bm = Bitmap.createScaledBitmap(bm, 50, 50, true);
        sinal.setImageBitmap(bm);
    }

    private boolean hasTarefa() {
        TarefaDao dao = new TarefaDao(activity);
        boolean hasTarefa = dao.hasTarefa(dia.getData());
        dao.close();
        return hasTarefa;
    }

    private String geraNomeDia(int i) {

        switch (i) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terça";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sábado";
            default:
                return "";
        }
    }
}