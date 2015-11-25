package br.com.caelum.contadorhoras.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.adapter.ContadorPagerAdapter;
import br.com.caelum.contadorhoras.dao.DiaDao;
import br.com.caelum.contadorhoras.dao.TarefaDao;
import br.com.caelum.contadorhoras.fragments.DiaFragment;
import br.com.caelum.contadorhoras.fragments.TarefaFragment;
import br.com.caelum.contadorhoras.modelo.Dia;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private List<Fragment> fragments;
    private DiaFragment diaFragment;
    private TarefaFragment tarefaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preparaComponentes();

        preparaVisualicaoDaTela();

        populaListaFragment();

    }

    private void preparaComponentes() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void populaListaFragment() {
        preparaListaDeFragments();

        adaptaViewPager();

        viewPager.setCurrentItem(0);
    }

    private void adaptaViewPager() {
        ContadorPagerAdapter adapter = new ContadorPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    private void preparaListaDeFragments() {
        diaFragment = new DiaFragment();
        tarefaFragment = new TarefaFragment();

        fragments = new ArrayList<>();

        fragments.add(diaFragment);
        fragments.add(tarefaFragment);
    }

    private void preparaVisualicaoDaTela() {

        preparaTabLayout();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void preparaTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Dias"));
        tabLayout.addTab(tabLayout.newTab().setText("Tarefas"));
    }

    public TarefaFragment getTarefaFragment() {
        return tarefaFragment;
    }

    public DiaFragment getDiaFragment() {
        return diaFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        criaMenu(menu);

        return true;
    }

    private void criaMenu(Menu menu) {

        MenuItem add = menu.findItem(R.id.adicionar);
        MenuItem subirHoras = menu.findItem(R.id.subir);
        if (viewPager.getCurrentItem() == 1) {
            add.setVisible(true);
            add.setTitle("Adiciona Tarefa");
        } else {
            add.setVisible(false);
        }

        if (viewPager.getCurrentItem() == 0) {
            subirHoras.setVisible(true);

        } else {
            subirHoras.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DiaDao dao = new DiaDao(this);
        switch (item.getItemId()) {
            case R.id.adicionar:

                if (dao.pegaDias().size() >= 1) {
                    Intent intent = new Intent(this, ListaDiasActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(viewPager, "Você ainda não possui nenhum dia", Snackbar.LENGTH_SHORT).show();
                }

                dao.close();

                return true;

            case R.id.subir:

                List<Dia> dias = dao.pegaDias();
                dao.close();

                if (dias.size() > 0) {

                    View view = View.inflate(this, R.layout.dias_subir_item, null);

                    ListView list = criaListView(dias, view);

                    final AlertDialog alertDialog = criaAlertComDias(view);

                    eventoClickDaLista(list, alertDialog);

                } else {
                    Snackbar.make(getDiaFragment().getFab(), "Você não tem nenhum dia ", Snackbar.LENGTH_SHORT)
                            .setAction("Adicionar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vaiParaCadastroDeDia();
                                }
                            })
                            .show();
                }
                return true;
        }
        return true;
    }

    private void vaiParaCadastroDeDia() {
        Intent intent = new Intent(this, CadastroDiaTrabalhadoActivity.class);
        startActivity(intent);
    }

    @NonNull
    private ListView criaListView(List<Dia> dias, View view) {
        ListView list = (ListView) view.findViewById(R.id.lista_dias_cadastrados);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dias));
        return list;
    }

    @NonNull
    private AlertDialog criaAlertComDias(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Escolha um dia").setView(view).create();
        alertDialog.show();
        return alertDialog;
    }

    private void eventoClickDaLista(ListView list, final AlertDialog alertDialog) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dia dia = (Dia) parent.getItemAtPosition(position);

                TarefaDao tarefaDao = new TarefaDao(MainActivity.this);

                if (tarefaDao.pegaTarefasDoDia(dia).size() >= 1) {
                    vaiParaListaDeTarefasUpload(dia);
                    alertDialog.dismiss();

                } else {
                    alertDialog.dismiss();
                    Snackbar.make(getDiaFragment().getFab(), "Esse dia ainda não tem nenhuma tarefa", Snackbar.LENGTH_SHORT)
                            .setAction("Adicionar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vaiParaCadastroDeTarefas(dia);
                                }
                            }).show();
                }
            }
        });
    }

    private void vaiParaCadastroDeTarefas(Dia dia) {

        Intent intent = new Intent(this, CadastraTarefaActivity.class);
        intent.putExtra("dia", dia);
        startActivity(intent);

    }

    private void vaiParaListaDeTarefasUpload(Dia dia) {
        Intent intent = new Intent(this, ListaTarefasUploadActivity.class);
        intent.putExtra("dia", dia);
        startActivity(intent);
    }
}