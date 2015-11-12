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


/**
 * Created by matheus on 11/11/15.
 */
public class DiasTrabalhadosAdapter extends BaseAdapter {
    private final List<Dia> dias;
    private final FragmentActivity activity;

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

        View view;

        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.dias_trabalhados_item, parent, false);

        } else {
            view = convertView;
        }

        TextView data = (TextView) view.findViewById(R.id.data_dia);
        TextView semana = (TextView) view.findViewById(R.id.nome_dia);

        Dia dia = dias.get(position);

        data.setText(dia.getData());

        populaImageView(view, dia);

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

        return view;
    }

    private void populaImageView(View view, Dia dia) {
        Bitmap bm;
        TarefaDao dao = new TarefaDao(activity);
        boolean hasTarefa = dao.hasTarefa(dia.getId());
        if (hasTarefa) {
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.positivo);
        } else {
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.negativo);
        }
        ImageView sinal = (ImageView) view.findViewById(R.id.sinal);

        bm = Bitmap.createScaledBitmap(bm, 50, 50, true);
        sinal.setImageBitmap(bm);
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