package br.com.caelum.contadorhoras.fragments;

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
import br.com.caelum.contadorhoras.activity.MainActivity;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.fragments.adapter.TarefasAdapter;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 10/11/15.
 */
public class TarefaFragment extends Fragment {

    private ListView listaAtividades;
    private Tarefa tarefa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tarefa_fragment, container, false);

        listaAtividades = (ListView) view.findViewById(R.id.lista_tarefas);

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

                TarefaDao dao = new TarefaDao(getContext());
                dao.deleta(tarefa);
                dao.close();

                carregaLista();

                carregaListaDias();

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
