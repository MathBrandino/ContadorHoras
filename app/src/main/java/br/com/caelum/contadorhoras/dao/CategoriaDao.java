package br.com.caelum.contadorhoras.dao;

import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Categoria;

/**
 * Created by matheus on 08/12/15.
 */
public class CategoriaDao implements Closeable {

    private final DatabaseHelperDao dao;
    private Context context;

    public CategoriaDao(Context context) {
        this.context = context;
        dao = new DatabaseHelperDao(context);

    }

    public List<Categoria> getCategorias(){

        List<Categoria> categorias = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from categoria", null);

        while (cursor.moveToNext()){
            categorias.add(popula(cursor));
        }


        return categorias;
    }

    private Categoria popula(Cursor cursor) {
        Categoria categoria = new Categoria();

        categoria.setId(cursor.getLong(cursor.getColumnIndex("id")));
        categoria.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));

        return categoria;
    }

    @Override
    public void close() {
        dao.close();
    }
}
