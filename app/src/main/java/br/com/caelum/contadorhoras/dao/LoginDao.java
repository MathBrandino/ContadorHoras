package br.com.caelum.contadorhoras.dao;

import android.content.Context;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by matheus on 15/12/15.
 */
public class LoginDao implements Closeable{

    private Context context;

    public LoginDao(Context context) {
        this.context = context;
    }

    @Override
    public void close() throws IOException {

    }
}
