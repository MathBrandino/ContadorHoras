package br.com.caelum.contadorhoras.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matheus on 10/11/15.
 */
class DatabaseHelperDao extends SQLiteOpenHelper {

    public static final String ID_DIA = "id";
    public static final String DATA = "data";
    public static final String TABELA_DIA = "Dia";
    public static final String TABELA_TAREFA = "Tarefa";
    public static final String ID_TAREFA = "id";
    public static final String ID_CATEGORIA_TAREFA = "idCategoria";
    public static final String DATA_DIA = "dataDia";
    public static final String DESC = "desc";
    public static final String HORA_INICIAL = "horaInicial";
    public static final String MINUTO_INICIAL = "minutoInicial";
    public static final String HORA_FINAL = "horaFinal";
    public static final String MINUTO_FINAL = "minutoFinal";
    public static final String TABELA_CATEGORIA = "Categoria";
    public static final String ID_CATEGORIA = "id";
    public static final String TIPO = "tipo";
    public static final String ID_LOGIN = "id";
    public static final String USUARIO = "usuario";
    public static final String SENHA = "senha";
    private static final int VERSAO = 1;
    private static final String DATABASE = "ContadorCaelum";
    private static final String TABELA_LOGIN = "Login";

    public DatabaseHelperDao(Context ctx) {
        super(ctx, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table " + TABELA_DIA + " ( " +
                ID_DIA + " integer primary key , " +
                DATA + " text not null ) ;";

        String query = "Create table " + TABELA_TAREFA + " ( " +
                ID_TAREFA + " integer primary key , " +
                ID_CATEGORIA_TAREFA + " integer not null," +
                DATA_DIA + " text not null, " +
                DESC + " text not null, " +
                HORA_INICIAL + " integer not null, " +
                MINUTO_INICIAL + " integer not null, " +
                HORA_FINAL + " integer not null , " +
                MINUTO_FINAL + " integer not null ," +
                "FOREIGN KEY(" + DATA_DIA + ") REFERENCES Dia (" + DATA + ") ," +
                "FOREIGN KEY(" + ID_CATEGORIA_TAREFA + ") REFERENCES Categoria (" + ID_CATEGORIA + ") ) ;";

        String ddl = "Create table " + TABELA_CATEGORIA + " (" +
                ID_CATEGORIA + " integer primary key , " +
                TIPO + " text not null ) ;";

        String create = "Create table " + TABELA_LOGIN + " ( " +
                ID_LOGIN + " integer primary key, " +
                USUARIO + " text not null , " +
                SENHA + " text not null ) ;";


        db.execSQL(sql);
        db.execSQL(ddl);
        db.execSQL(query);
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
