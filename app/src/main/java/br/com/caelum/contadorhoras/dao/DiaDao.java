package br.com.caelum.contadorhoras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Dia;

/**
 * Created by matheus on 10/11/15.
 */
public class DiaDao implements Closeable {

    private static final String DATA = "data";
    private static final String TABELA = "Dia";
    private static final String ID = "id";
    private final Context ctx;
    private DatabaseHelperDao helperDao;

    public DiaDao(Context ctx) {
        helperDao = new DatabaseHelperDao(ctx);
        this.ctx = ctx;
    }

    public void deleta(Dia dia) {
        helperDao.getWritableDatabase().delete(TABELA, ID + " = ?", new String[]{String.valueOf(dia.getId())});
        TarefaDao dao = new TarefaDao(ctx);
        dao.deletaPorDia(dia.getData());
        dao.close();
    }

    public void insere(Dia dia) {
        ContentValues values = new ContentValues();
        values.put(DATA, dia.getData());

        helperDao.getWritableDatabase().insert(TABELA, null, values);

    }

    public void altera(Dia dia) {

        ContentValues values = new ContentValues();
        values.put(DATA, dia.getData());

        helperDao.getWritableDatabase().update(TABELA, values, ID + " = ? ", new String[]{String.valueOf(dia.getId())});

    }

    public List<Dia> pegaDias() {
        List<Dia> dias = new ArrayList<>();

        String sql = "Select * from " + TABELA + " order by " + DATA + " ;";
        Cursor cursor = helperDao.getReadableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            dias.add(geraDiaDaBusca(cursor));
        }

        return dias;
    }

    @NonNull
    private Dia geraDiaDaBusca(Cursor cursor) {
        Dia dia = new Dia();
        dia.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        dia.setData(cursor.getString(cursor.getColumnIndex(DATA)));
        return dia;
    }

    @Override
    public void close() {
        helperDao.close();
    }

    public String getData(String dataDia) {
        Cursor cursor = helperDao.getReadableDatabase().rawQuery("Select * from " + TABELA + " where " + DATA + " = ?", new String[]{dataDia});

        String data = null;
        if (cursor.moveToNext())
            data = cursor.getString(cursor.getColumnIndex(DATA));

        return data;
    }
}
