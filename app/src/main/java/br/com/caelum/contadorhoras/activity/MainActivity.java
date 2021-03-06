package br.com.caelum.contadorhoras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DiaDao dao = new DiaDao(this);
        switch (item.getItemId()) {

            case R.id.subir:

                List<Dia> dias = dao.pegaDias();
                dao.close();

                List<Dia> diasValidos = new ArrayList<>();

                Calendar dataAtual = pegaDiaAtual();


                for (Dia dia : dias) {

                    Date date = transformaDiaEmDate(dia);

                    Calendar dataDaLista = transformaDateEmCalendar(date);

                    colocaDatasValidasNaLista(diasValidos, dataAtual, dia, dataDaLista);

                }

                if (diasValidos.size() > 0) {

                    View view = View.inflate(this, R.layout.dias_subir_item, null);

                    ListView list = criaListView(diasValidos, view);

                    final AlertDialog alertDialog = criaAlertComDias(view);

                    eventoClickDaLista(list, alertDialog);

                } else {
                    Snackbar.make(viewPager, "Você não tem nenhum dia ", Snackbar.LENGTH_SHORT)
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

    private void colocaDatasValidasNaLista(List<Dia> diasValidos, Calendar dataAtual, Dia dia, Calendar dataDaLista) {
        if (dataDaLista.get(Calendar.DAY_OF_YEAR) > dataAtual.get(Calendar.DAY_OF_YEAR) - 14) {

            diasValidos.add(dia);

        }
    }

    @NonNull
    private Calendar transformaDateEmCalendar(Date date) {
        Calendar dataDaLista = Calendar.getInstance();
        dataDaLista.setTimeInMillis(date.getTime());
        return dataDaLista;
    }

    @Nullable
    private Date transformaDiaEmDate(Dia dia) {
        String data = dia.getData();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(data);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @NonNull
    private Calendar pegaDiaAtual() {
        long currentTimeMillis = System.currentTimeMillis();

        Calendar dataAtual = Calendar.getInstance();

        dataAtual.setTimeInMillis(currentTimeMillis);
        return dataAtual;
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

                } else {
                    Snackbar.make(getDiaFragment().getFab(), "Esse dia ainda não tem nenhuma tarefa", Snackbar.LENGTH_SHORT)
                            .setAction("Adicionar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vaiParaCadastroDeTarefas(dia);
                                }
                            }).show();
                }

                alertDialog.dismiss();
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