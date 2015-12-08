package br.com.caelum.contadorhoras.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matheus on 10/11/15.
 */
class DatabaseHelperDao extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String DATABASE = "ContadorCaelum";

    public DatabaseHelperDao(Context ctx) {
        super(ctx, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table Dia ( " +
                "id integer primary key , " +
                "data text not null ) ;";

        String query = "Create table Tarefa ( " +
                "id integer primary key , " +
                "idCategoria integer not null," +
                "dataDia text not null, " +
                "desc text not null, " +
                "horaInicial integer not null, " +
                "minutoInicial integer not null, " +
                "horaFinal integer not null , " +
                "minutoFinal integer not null ," +
                "FOREIGN KEY(dataDia) REFERENCES Dia (data) ," +
                "FOREIGN KEY(idCategoria) REFERENCES Categoria (id) ) ;";

        String ddl = "Create table Categoria (" +
                "id integer primary key , " +
                "tipo text not null ) ;";


        String insert = "insert into Categoria (tipo) values ('Aula') ;" ;
        String insert1 = "insert into Categoria (tipo) values ('Video') ; ";
        String insert2 = "insert into Categoria (tipo) values ('Livro') ;";


        db.execSQL(sql);
        db.execSQL(ddl);
        db.execSQL(insert);
        db.execSQL(insert1);
        db.execSQL(insert2);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
