package br.com.caelum.contadorhoras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Dia;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 10/11/15.
 */
public class TarefaDao implements Closeable {

    private static final String TABELA = "Tarefa";
    private static final String DESC = "desc";
    private static final String HORA_INICIAL = "horaInicial";
    private static final String HORA_FINAL = "horaFinal";
    private static final String MINUTO_INICIAL = "minutoInicial";
    private static final String MINUTO_FINAL = "minutoFinal";
    private static final String ID_DIA = "idDia";
    private DatabaseHelperDao helperDao;

    public TarefaDao(Context context) {
        helperDao = new DatabaseHelperDao(context);
    }

    public void insere(Tarefa tarefa) {

        ContentValues dados = new ContentValues();

        dados.put(ID_DIA, tarefa.getIdDia());
        dados.put(DESC, tarefa.getDesc());
        dados.put(HORA_INICIAL, tarefa.getHoraInicial());
        dados.put(HORA_FINAL, tarefa.getHoraFinal());
        dados.put(MINUTO_INICIAL, tarefa.getMinutoInicial());
        dados.put(MINUTO_FINAL, tarefa.getHoraFinal());

        helperDao.getWritableDatabase().insert(TABELA, null, dados);
    }

    public List<Tarefa> pegaTarefas() {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "Select * from " + TABELA + " order by " + ID_DIA + " ;";
        Cursor cursor = helperDao.getReadableDatabase().rawQuery(sql, null);

        while (cursor.moveToNext()) {
            tarefas.add(populaTarefa(cursor));
        }

        return tarefas;
    }

    public void altera(Tarefa tarefa) {

        ContentValues dados = new ContentValues();

        dados.put(ID_DIA, tarefa.getIdDia());
        dados.put(DESC, tarefa.getDesc());
        dados.put(HORA_FINAL, tarefa.getHoraFinal());
        dados.put(HORA_INICIAL, tarefa.getHoraInicial());
        dados.put(MINUTO_INICIAL, tarefa.getMinutoInicial());
        dados.put(MINUTO_FINAL, tarefa.getMinutoFinal());


        helperDao.getWritableDatabase().update(TABELA, dados, " id  = ? ", new String[]{String.valueOf(tarefa.getId())});
    }

    public void deleta(Tarefa tarefa) {

        helperDao.getWritableDatabase().delete(TABELA, "id = ? ", new String[]{String.valueOf(tarefa.getId())});

    }

    private Tarefa populaTarefa(Cursor cursor) {

        Tarefa tarefa = new Tarefa();

        tarefa.setId(cursor.getLong(cursor.getColumnIndex("id")));
        tarefa.setIdDia(cursor.getLong(cursor.getColumnIndex(ID_DIA)));
        tarefa.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
        tarefa.setHoraFinal(cursor.getInt(cursor.getColumnIndex(HORA_FINAL)));
        tarefa.setMinutoFinal(cursor.getInt(cursor.getColumnIndex(MINUTO_FINAL)));
        tarefa.setHoraInicial(cursor.getInt(cursor.getColumnIndex(HORA_INICIAL)));
        tarefa.setMinutoInicial(cursor.getInt(cursor.getColumnIndex(MINUTO_INICIAL)));

        return tarefa;
    }


    @Override
    public void close() {
        helperDao.close();
    }

    public void deletaPorDia(Long id) {

        helperDao.getWritableDatabase().delete(TABELA, ID_DIA + " = ?", new String[]{id.toString()});
    }

    public boolean hasTarefa(Long id) {
        Cursor cursor = helperDao.getReadableDatabase().rawQuery("Select * from " + TABELA + " where " + ID_DIA + "  =  ?", new String[]{id.toString()});

        return cursor.moveToNext();
    }

    public List<Tarefa> pegaTarefasDoDia(Dia dia) {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "Select * from " + TABELA + " where " + ID_DIA + " = ? ;";
        Cursor cursor = helperDao.getReadableDatabase().rawQuery(sql, new String[]{dia.getId().toString()});

        while (cursor.moveToNext()) {
            tarefas.add(populaTarefa(cursor));
        }


        return tarefas;
    }
}
