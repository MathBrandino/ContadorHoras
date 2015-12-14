package br.com.caelum.contadorhoras.dao;

import android.content.ContentValues;
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

    public static final String ID = "id";
    public static final String TIPO = "tipo";
    private static final String TABELA = "Categoria";
    private final DatabaseHelperDao dao;
    private Context context;

    public CategoriaDao(Context context) {
        this.context = context;
        dao = new DatabaseHelperDao(context);

    }

    public List<Categoria> getCategorias() {

        List<Categoria> categorias = new ArrayList<>();

        Cursor cursor = dao.getReadableDatabase().rawQuery("Select * from "+ TABELA, null);

        while (cursor.moveToNext()) {
            categorias.add(popula(cursor));
        }


        return categorias;
    }

    private Categoria popula(Cursor cursor) {
        Categoria categoria = new Categoria();

        categoria.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        categoria.setTipo(cursor.getString(cursor.getColumnIndex(TIPO)));

        return categoria;
    }

    @Override
    public void close() {
        dao.close();
    }


    private void insere(Categoria categoria) {
        ContentValues dados = new ContentValues();

        populaContentValues(categoria, dados);

        dao.getWritableDatabase().insert(TABELA, null, dados);
    }

    private void populaContentValues(Categoria categoria, ContentValues dados) {
        dados.put(ID, categoria.getId());
        dados.put(TIPO, categoria.getTipo());
    }

    private void altera(Categoria categoria) {

        ContentValues dados = new ContentValues();

        populaContentValues(categoria, dados);

        String[] id = {categoria.getId().toString()};
        dao.getWritableDatabase().update(TABELA, dados, ID +" = ? ", id);
    }


    public void verificaCategorias(List<Categoria> categorias) {

        for (Categoria categoria : categorias) {
            if (hasCategoria(categoria)) {
                if (precisaAlteracao(categoria)) {
                    altera(categoria);
                }
            } else {
                insere(categoria);
            }

        }
    }


    private boolean precisaAlteracao(Categoria categoria) {

        String sql = "Select * from "+ TABELA + " where "+ ID +" = ? ,"+ TIPO +" = ? ";
        String[] where = {categoria.getId().toString(), categoria.getTipo()};
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, where);
        return !cursor.moveToNext();
    }

    private boolean hasCategoria(Categoria categoria) {

        String sql = "Select * from " + TABELA + " where "+ ID +" = ?  ";
        String[] where = {categoria.getId().toString()};
        Cursor cursor = dao.getReadableDatabase().rawQuery(sql, where);
        return cursor.moveToNext();

    }
}
