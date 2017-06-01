package facin.com.cardapio_virtual.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by 13108306 on 03/01/2017.
 */
public class DatabaseProvider extends ContentProvider {
    // UriMatcher usada por este provedor de conteúdo
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    /* Tabelas */
    static final int RESTAURANTES = 100;
    static final int LOGS = 200;
    static final int JOIN_MAESFILHAS_LOGS = 250;
    static final int MAES_FILHAS = 300;
    static final int DUVIDAS = 400;

    static UriMatcher buildUriMatcher() {
        /* Todos caminhos (paths) adicionados à UriMatcher têm um código correspondente que será
        retornado quando seu par for encontrado. O código passado dentro do construtor representa o códigio
        a ser retornado para a URI raiz. É comum usar NO_MATCH como o código nesse caso.*/
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        /* Para cada tipo de URI que você deseja adicionar, crie um código correspondente.*/
        matcher.addURI(authority, DatabaseContract.PATH_RESTAURANTES, RESTAURANTES);
        matcher.addURI(authority, DatabaseContract.PATH_LOGS, LOGS);
        matcher.addURI(authority, DatabaseContract.PATH_MAES_FILHAS, MAES_FILHAS);
        matcher.addURI(authority, DatabaseContract.PATH_JOIN_MAESFILHAS_LOGS, JOIN_MAESFILHAS_LOGS);
        matcher.addURI(authority, DatabaseContract.PATH_DUVIDAS, DUVIDAS);
//        matcher.addURI(authority, DatabaseContract.PATH_USUARIOS, USUARIOS);
//        matcher.addURI(authority, DatabaseContract.PATH_FAVORITOS, FAVORITOS);
//        /* Joins */
//        matcher.addURI(authority, DatabaseContract.PATH_RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS + "/",
//                RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        /* Use a Uri Matcher para determinar qual o tipo de URI que está sendo recebido. */
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANTES:
                return DatabaseContract.RestaurantesEntry.CONTENT_TYPE;
            case LOGS:
                return DatabaseContract.LogsEntry.CONTENT_TYPE;
            case MAES_FILHAS:
                return DatabaseContract.MaesFilhasEntry.CONTENT_TYPE;
            case DUVIDAS:
                return DatabaseContract.DuvidasEntry.CONTENT_TYPE;
//            case USUARIOS:
//                return DatabaseContract.UsuariosEntry.CONTENT_TYPE;
//            case FAVORITOS:
//                return DatabaseContract.FavoritosEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case RESTAURANTES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.RestaurantesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LOGS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.LogsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MAES_FILHAS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.MaesFilhasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case JOIN_MAESFILHAS_LOGS:
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                        "SELECT " +
                                DatabaseContract.MaesFilhasEntry.TABLE_NAME + "." + DatabaseContract.MaesFilhasEntry.COLUMN_MAE + ", " +
                                " SUM (" + DatabaseContract.LogsEntry.TABLE_NAME + "." + DatabaseContract.LogsEntry.COLUMN_ACESSOS + ")" +
                                " FROM " + DatabaseContract.MaesFilhasEntry.TABLE_NAME +
                                " INNER JOIN " +  DatabaseContract.LogsEntry.TABLE_NAME +
                                " ON " + DatabaseContract.MaesFilhasEntry.TABLE_NAME + "." + DatabaseContract.MaesFilhasEntry.COLUMN_FILHA +
                                " = " + DatabaseContract.LogsEntry.TABLE_NAME + "." + DatabaseContract.LogsEntry._ID +
                                " WHERE " + DatabaseContract.MaesFilhasEntry.TABLE_NAME + "." + DatabaseContract.MaesFilhasEntry.COLUMN_MAE + " = ?" +
                                " GROUP BY " + DatabaseContract.MaesFilhasEntry.COLUMN_MAE,
                        selectionArgs
                );
                break;
            case DUVIDAS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DatabaseContract.DuvidasEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
//            case USUARIOS: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        DatabaseContract.UsuariosEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//            case FAVORITOS: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        DatabaseContract.FavoritosEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//            case RELATIONSHIP_JOIN_RESTAURANTESFAVORITOS: {
//                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
//                      " SELECT " +
//                        DatabaseContract.RestaurantesEntry.TABLE_NAME + "." + DatabaseContract.RestaurantesEntry._ID + ", " +
//                        DatabaseContract.RestaurantesEntry.TABLE_NAME + "." + DatabaseContract.RestaurantesEntry.COLUMN_NOME + "AS RESTAURANTE_NOME, " +
//                        DatabaseContract.RestaurantesEntry.TABLE_NAME + "." + DatabaseContract.RestaurantesEntry.COLUMN_ENDERECO + ", " +
//                        DatabaseContract.FavoritosEntry.TABLE_NAME + "." + DatabaseContract.FavoritosEntry._ID +
//                        " FROM " + DatabaseContract.RestaurantesEntry.TABLE_NAME +
//                        " INNER JOIN " + DatabaseContract.FavoritosEntry.TABLE_NAME +
//                        " ON " + DatabaseContract.RestaurantesEntry.TABLE_NAME + "." + DatabaseContract.RestaurantesEntry._ID +
//                        " = " + DatabaseContract.FavoritosEntry.TABLE_NAME + "." + DatabaseContract.FavoritosEntry.COLUMN_ID_RESTAURANTE,
//                        selectionArgs
//                );
//                break;
//            }
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RESTAURANTES: {
                long _id = db.insert(DatabaseContract.RestaurantesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DatabaseContract.RestaurantesEntry.buildAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case LOGS: {
                long _id = db.insert(DatabaseContract.LogsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DatabaseContract.RestaurantesEntry.buildAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case MAES_FILHAS: {
                long _id = db.insert(DatabaseContract.MaesFilhasEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DatabaseContract.RestaurantesEntry.buildAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
            case DUVIDAS: {
                long _id = db.insert(DatabaseContract.DuvidasEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DatabaseContract.DuvidasEntry.buildAvaliacoesUri(_id);
                else
                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
                break;
            }
//            case USUARIOS: {
//                long _id = db.insert(DatabaseContract.UsuariosEntry.TABLE_NAME, null, values);
//                if (_id > 0)
//                    returnUri = DatabaseContract.UsuariosEntry.buildAvaliacoesUri(_id);
//                else
//                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
//                break;
//            }
//            case FAVORITOS: {
//                long _id = db.insert(DatabaseContract.FavoritosEntry.TABLE_NAME, null, values);
//                if (_id > 0)
//                    returnUri = DatabaseContract.FavoritosEntry.buildAvaliacoesUri(_id);
//                else
//                    throw new android.database.SQLException("Falha ao inserir linha em " + uri);
//                break;
//            }
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        /* Isso faz com que a deleção de todas as linhas retorne o número de linhas deletadas. */
        if (null == selection) selection = "1";
        switch (match) {
            case RESTAURANTES: {
                rowsDeleted = db.delete(
                        DatabaseContract.RestaurantesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case LOGS: {
                rowsDeleted = db.delete(
                        DatabaseContract.LogsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MAES_FILHAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.MaesFilhasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case DUVIDAS: {
                rowsDeleted = db.delete(
                        DatabaseContract.DuvidasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

//            case USUARIOS: {
//                rowsDeleted = db.delete(
//                        DatabaseContract.UsuariosEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            }
//            case FAVORITOS: {
//                rowsDeleted = db.delete(
//                        DatabaseContract.FavoritosEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            }
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case RESTAURANTES: {
                rowsUpdated = db.update(DatabaseContract.RestaurantesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case LOGS: {
//                rowsUpdated = insertOrUpdate(uri, values, selection, selectionArgs, db);
                rowsUpdated = db.update(DatabaseContract.LogsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case MAES_FILHAS: {
                rowsUpdated = db.update(DatabaseContract.MaesFilhasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case DUVIDAS: {
                rowsUpdated = db.update(DatabaseContract.DuvidasEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
//            case USUARIOS:
//                rowsUpdated = db.update(DatabaseContract.UsuariosEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            case FAVORITOS:
//                rowsUpdated = db.update(DatabaseContract.FavoritosEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
            default:
                throw new UnsupportedOperationException("URI desconhecida: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private int insertOrUpdate(Uri uri, ContentValues values,
                                String selection, String[] selectionArgs, SQLiteDatabase db) throws SQLException {
        int rowsUpdated = 0;
        try {
            long _id = db.insert(DatabaseContract.LogsEntry.TABLE_NAME, null, values);
            if (_id > 0)
                rowsUpdated = 1;
            else
                throw new android.database.SQLException("Falha ao inserir linha em " + uri);
        } catch (SQLiteConstraintException e) {
            rowsUpdated = db.update(DatabaseContract.LogsEntry.TABLE_NAME, values, selection,
                    selectionArgs);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case RESTAURANTES: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.RestaurantesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case LOGS: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.LogsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MAES_FILHAS: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.MaesFilhasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case DUVIDAS: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.DuvidasEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
//            case USUARIOS:
//                db.beginTransaction();
//                returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        long _id = db.insert(DatabaseContract.UsuariosEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            case FAVORITOS:
//                db.beginTransaction();
//                returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        long _id = db.insert(DatabaseContract.FavoritosEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    /* You do not need to call this method. This is a method specifically to assist the testing
     * framework in running smoothly. You can read more at:
     * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown() */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
