package br.com.caelum.contadorhoras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;

import br.com.caelum.contadorhoras.R;

/**
 * Created by matheus on 26/11/15.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criaComponentes();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validaLogin()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    private boolean validaLogin() {
        if(validaUsuario() && validaSenha()){
            return true;
        }

        return false;

    }

    private boolean validaSenha() {
        TextInputLayout layout = (TextInputLayout) senha.getParent();
        if (senha.getText().toString().trim().isEmpty()){
            layout.setError("Senha inválida");
            return false;
        }

        return true;
    }

    private boolean validaUsuario() {
        if (login.getText().toString().trim().isEmpty() || !login.getText().toString().trim().contains(".")){
            TextInputLayout layout = (TextInputLayout) login.getParent();
            layout.setError("Usuario inválido");

            return false;
        }
        return true;
    }

    private void criaComponentes() {
        criaToolbar();

        login = (EditText) findViewById(R.id.login_usuario);
        senha = (EditText) findViewById(R.id.login_senha);

        fab = (FloatingActionButton) findViewById(R.id.fab_login);
    }

    private void criaToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
    }
}
