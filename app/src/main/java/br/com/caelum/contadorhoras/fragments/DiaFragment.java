package br.com.caelum.contadorhoras.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.activity.CadastraTarefaActivity;
import br.com.caelum.contadorhoras.activity.CadastroDiaTrabalhadoActivity;
import br.com.caelum.contadorhoras.activity.ListaTarefasUploadActivity;
import br.com.caelum.contadorhoras.activity.MainActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.fragments.adapter.DiasTrabalhadosAdapter;
import br.com.caelum.contadorhoras.modelo.Dia;

/**
 * Created by matheus on 10/11/15.
 */
public class DiaFragment extends Fragment {

    private ListView listaDiasTrabalhados;
    private DiasTrabalhadosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dias_fragment, container, false);

        listaDiasTrabalhados = (ListView) view.findViewById(R.id.lista_dias_trabalhados);

        vaiParaDiaSelecionado();

        registerForContextMenu(listaDiasTrabalhados);

        return view;
    }

    private void vaiParaDiaSelecionado() {
        listaDiasTrabalhados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dia dia = (Dia) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), CadastroDiaTrabalhadoActivity.class);
                intent.putExtra("dia", dia);
                startActivity(intent);

            }
        });
    }

    protected void carregaLista() {
        DiaDao dao = new DiaDao(getContext());
        List<Dia> dias = dao.pegaDias();
        dao.close();

        adapter = new DiasTrabalhadosAdapter(getActivity(), dias);
        listaDiasTrabalhados.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Dia dia = (Dia) listaDiasTrabalhados.getItemAtPosition(info.position);

        getActivity().getMenuInflater().inflate(R.menu.dia_context_menu, menu);

        MenuItem deletar = menu.findItem(R.id.menu_deletar);
        MenuItem upload = menu.findItem(R.id.menu_upload);
        MenuItem tarefa = menu.findItem(R.id.menu_cadastra_tarefa);

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DiaDao dao = new DiaDao(getActivity());
                dao.deleta(dia);
                dao.close();

                carregaLista();

                MainActivity activity = (MainActivity) getActivity();
                activity.getTarefaFragment().carregaLista();
                return true;
            }
        });

        upload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final TarefaDao dao = new TarefaDao(getContext());
                if (dao.pegaTarefasDoDia(dia).size() >= 1) {

                    Intent intent = new Intent(getActivity(), ListaTarefasUploadActivity.class);
                    intent.putExtra("dia", dia);
                    startActivity(intent);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Atenção !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Você não possui nenhuma hora para esse dia !")
                            .setPositiveButton("Cadastrar hora  ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), CadastraTarefaActivity.class);
                                    intent.putExtra("dia", dia);
                                    startActivity(intent);
                                }
                            })
                            .show();
                    dao.close();
                }
                return true;
            }
        });

        tarefa.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), CadastraTarefaActivity.class);
                intent.putExtra("dia", dia);
                startActivity(intent);

                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        carregaLista();

    }
}
