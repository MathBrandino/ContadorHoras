package br.com.caelum.contadorhoras.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import br.com.caelum.contadorhoras.R;
import br.com.caelum.contadorhoras.asynctask.ValidadorDeLoginTask;
import br.com.caelum.contadorhoras.modelo.Login;

/**
 * Created by matheus on 26/11/15.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText senha;

    private FloatingActionButton fab;
    private Login login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criaComponentes();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaLogin()) {
                    criaLogin();

                    //new AlertDialog.Builder(LoginActivity.this).setMessage(s).show();

                    new ValidadorDeLoginTask(login, LoginActivity.this).execute();
                }

            }
        });

    }

    private void criaLogin() {
        login = new Login(usuario.getText().toString(), senha.getText().toString());
    }

    private boolean validaLogin() {
        if (validaUsuario() && validaSenha()) {
            return true;
        }

        return false;

    }

    private boolean validaSenha() {
        TextInputLayout layout = (TextInputLayout) senha.getParent();
        if (senha.getText().toString().trim().isEmpty()) {
            layout.setError("Senha inválida");
            return false;
        }

        return true;
    }

    private boolean validaUsuario() {
        if (usuario.getText().toString().trim().isEmpty()) {
            TextInputLayout layout = (TextInputLayout) usuario.getParent();
            layout.setError("Usuario inválido");

            return false;
        }
        return true;
    }

    private void criaComponentes() {
        criaToolbar();

        usuario = (EditText) findViewById(R.id.login_usuario);
        senha = (EditText) findViewById(R.id.login_senha);

        fab = (FloatingActionButton) findViewById(R.id.fab_login);
    }

    private void criaToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
    }
}
