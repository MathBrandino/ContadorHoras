package br.com.caelum.contadorhoras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.adapter.ContadorPagerAdapter;
import br.com.caelum.contadorhoras.fragments.DiaFragment;
import br.com.caelum.contadorhoras.fragments.TarefaFragment;

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

        setSupportActionBar(toolbar);

        preparaVisualicaoDaTela();
        populaListaFragment();


    }

    private void preparaComponentes() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
    }

    private void populaListaFragment() {
        diaFragment = new DiaFragment();
        tarefaFragment = new TarefaFragment();

        fragments = new ArrayList<>();

        fragments.add(diaFragment);
        fragments.add(tarefaFragment);

        ContadorPagerAdapter adapter = new ContadorPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);
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

        MenuItem add = menu.findItem(R.id.adicionar);
        if (viewPager.getCurrentItem() == 0) {
            add.setTitle("Adiciona Dia");
        } else if (viewPager.getCurrentItem() == 1) {
            add.setTitle("Adiciona Tarefa");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.adicionar:
                if (viewPager.getCurrentItem() == 1) {
                    Intent intent = new Intent(this, ListaDiasActivity.class);
                    startActivity(intent);
                } else if (viewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(this, CadastroDiaTrabalhadoActivity.class);
                    startActivity(intent);
                }
        }


        return true;

    }
}
