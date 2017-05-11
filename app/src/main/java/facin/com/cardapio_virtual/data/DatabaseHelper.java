package facin.com.cardapio_virtual.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import facin.com.cardapio_virtual.data.DatabaseContract.*;

/**
 * Created by 13108306 on 03/01/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Alterar a medida que o banco de dados for modificado
    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "cardapio-virtual.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /* Tabelas principais */
        final String SQL_CREATE_RESTAURANTES_TABLE = "CREATE TABLE " + RestaurantesEntry.TABLE_NAME + " (" +
                RestaurantesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RestaurantesEntry.COLUMN_NOME + " VARCHAR(200) NOT NULL, " +
                RestaurantesEntry.COLUMN_EMAIL + " VARCHAR(254) UNIQUE NOT NULL, " +
                RestaurantesEntry.COLUMN_TELEFONE + " VARCHAR(15) NULL, " +
                RestaurantesEntry.COLUMN_ENDERECO + " VARCHAR(500) NOT NULL, " +
                RestaurantesEntry.COLUMN_LAT + " REAL NOT NULL, " +
                RestaurantesEntry.COLUMN_LNG + " REAL NOT NULL, " +
                RestaurantesEntry.COLUMN_DESCRICAO + " VARCHAR(1000) NOT NULL, " +
                RestaurantesEntry.COLUMN_FAVORITO + " BOOLEAN NOT NULL" +
                ");";

        final String SQL_CREATE_LOG_TABLE = "CREATE TABLE " + LogsEntry.TABLE_NAME + " (" +
                LogsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LogsEntry.COLUMN_PRODUTO + " VARCHAR(100) UNIQUE NOT NULL, " +
                LogsEntry.COLUMN_ACESSOS + " INTEGER NOT NULL" +
                ");";

        final String SQL_CREATE_MAESFILHAS_TABLE = "CREATE TABLE " + MaesFilhasEntry.TABLE_NAME + " (" +
                MaesFilhasEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MaesFilhasEntry.COLUMN_MAE + " VARCHAR(100) NOT NULL, " +
                MaesFilhasEntry.COLUMN_FILHA + " INTEGER NOT NULL" +
                ");";

//        final String SQL_CREATE_USUARIOS_TABLE = "CREATE TABLE " + UsuariosEntry.TABLE_NAME + " (" +
//                UsuariosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                UsuariosEntry.COLUMN_NOME + " VARCHAR(200) UNIQUE NOT NULL" +
//                ");";
//        /* Tabelas intermediárias */
//        final String SQL_CREATE_FAVORITOS_TABLE = "CREATE TABLE " + FavoritosEntry.TABLE_NAME + " (" +
//                FavoritosEntry.COLUMN_ID_RESTAURANTE + " INTEGER NOT NULL, " +
//                FavoritosEntry.COLUMN_ID_USUARIO + " INTEGER NOT NULL, " +
//                "PRIMARY KEY(" + FavoritosEntry.COLUMN_ID_RESTAURANTE + ", "+
//                FavoritosEntry.COLUMN_ID_USUARIO + "), " +
//                "FOREIGN KEY(" + FavoritosEntry.COLUMN_ID_RESTAURANTE + ") REFERENCES " +
//                RestaurantesEntry.TABLE_NAME + "(" + RestaurantesEntry._ID + "), " +
//                "FOREIGN KEY(" + FavoritosEntry.COLUMN_ID_USUARIO + ") REFERENCES " +
//                UsuariosEntry.TABLE_NAME + "(" + UsuariosEntry._ID + ") " +
//                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_RESTAURANTES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LOG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MAESFILHAS_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_USUARIOS_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RestaurantesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LogsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MaesFilhasEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
