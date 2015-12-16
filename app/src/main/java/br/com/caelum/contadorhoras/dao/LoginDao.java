package br.com.caelum.contadorhoras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;

import br.com.caelum.contadorhoras.modelo.Login;

/**
 * Created by matheus on 15/12/15.
 */
public class LoginDao implements Closeable {

    public static final String USUARIO = "usuario";
    public static final String SENHA = "senha";
    private static final String TABELA = "Login";
    private DatabaseHelperDao dao;

    public LoginDao(Context context) {
        dao = new DatabaseHelperDao(context);
    }


    private void insere(Login login) {
        ContentValues values = new ContentValues();
        values.put(USUARIO, login.getLogin());
        values.put(SENHA, login.getSenha());
        dao.getWritableDatabase().insert(TABELA, null, values);
    }


    private boolean hasLogin(Login login) {
        String sql = "select * from " + TABELA + " where " + USUARIO + " = ?  ;";
        String strings[] = {login.getLogin()};
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, strings);

        if (cursor.moveToNext())
            return true;
        else
            return false;
    }

    public Login pegaLoginValido() {

        String usuario = null;
        String senha = null;

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from " + TABELA, null);
        if (cursor.moveToLast()) {
            usuario = cursor.getString(cursor.getColumnIndex(USUARIO));
            senha = cursor.getString(cursor.getColumnIndex(SENHA));
        }

        Login login = new Login(usuario, senha);


        return login;
    }

    @Override
    public void close() {

        dao.close();
    }

    public void valida(Login login) {
        if (hasLogin(login)) {
            if (hasChaged(login)) {
                altera(login);
            }
        } else {
            insere(login);
        }
    }

    private void altera(Login login) {

        ContentValues dados = new ContentValues();

        dados.put(SENHA, login.getSenha());

        String[] usuario = {login.getLogin()};
        dao.getWritableDatabase().update(TABELA, dados, USUARIO + " = ?", usuario);
    }

    private boolean hasChaged(Login login) {
        String sql = " select  * from " + TABELA + " where " + USUARIO + " = ? and " + SENHA + " = ? ;";
        String[] strings = {login.getLogin(), login.getSenha()};
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, strings);
        if (cursor.moveToNext())
            return false;
        return true;
    }
}
