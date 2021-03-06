package br.com.caelum.contadorhoras.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import br.com.caelum.contadorhoras.activity.ListaDiasActivity;
import br.com.caelum.contadorhoras.activity.MainActivity;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.fragments.adapter.TarefasAdapter;
import br.com.caelum.contadorhoras.modelo.Dia;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 10/11/15.
 */
public class TarefaFragment extends Fragment {

    private ListView listaAtividades;
    private FloatingActionButton addTarefas;
    private Tarefa tarefa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tarefa_fragment, container, false);

        listaAtividades = (ListView) view.findViewById(R.id.lista_tarefas);

        addTarefas = (FloatingActionButton) view.findViewById(R.id.tarefa_fragment_add);

        addTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaDao dao = new DiaDao(getContext());
                List<Dia> dias = dao.pegaDias();
                dao.close();

                if (dias.size() > 0) {
                    Intent intent = new Intent(getContext(), ListaDiasActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(addTarefas, "Não tem nenhum dia para adicionar",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        registerForContextMenu(listaAtividades);

        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tarefa = (Tarefa) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), CadastraTarefaActivity.class);
                intent.putExtra("tarefa", tarefa);
                startActivity(intent);

            }
        });

        return view;
    }

    protected void carregaLista() {
        TarefaDao dao = new TarefaDao(getContext());
        List<Tarefa> tarefas = dao.pegaTarefas();
        dao.close();


        listaAtividades.setAdapter(new TarefasAdapter(getActivity(), tarefas));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.tarefa_menu_context, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        tarefa = (Tarefa) listaAtividades.getItemAtPosition(info.position);

        MenuItem deletar = menu.findItem(R.id.menu_deletar);

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(getContext()).setMessage("Deseja excluir tarefa ?").setTitle("Atenção")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                TarefaDao dao = new TarefaDao(getContext());
                                dao.deleta(tarefa);
                                dao.close();

                                carregaLista();

                                carregaListaDias();

                            }
                        }).setNegativeButton("Não", null).show();

                return true;
            }
        });
    }

    private void carregaListaDias() {
        MainActivity activity = (MainActivity) getActivity();
        activity.getDiaFragment().carregaLista();
    }

    @Override
    public void onResume() {
        super.onResume();
        carregaLista();
    }
}
