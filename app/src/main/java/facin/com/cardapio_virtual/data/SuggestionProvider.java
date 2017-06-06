package facin.com.cardapio_virtual.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SuggestionProvider extends ContentProvider {

    public static final String AUTHORITY = "facin.com.cardapio_virtual.suggestions";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY);

    // UriMatcher constant for search suggestions
    private static final int SEARCH_SUGGEST = 1;

    private static final UriMatcher uriMatcher;

    private static final String[] SEARCH_SUGGEST_COLUMNS = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
//            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA
    };

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }

    @Override
    public int delete(Uri uri, String arg1, String[] arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        MatrixCursor cursor = null;
        // Use the UriMatcher to see what kind of query we have
        if (uri.getLastPathSegment().equals(SearchManager.SUGGEST_URI_PATH_QUERY)) {
            // String query = uri.getLastPathSegment().toLowerCase();
            Cursor suggestions = getContext().getContentResolver().query(
                    DatabaseContract.RestaurantesEntry.CONTENT_URI,
                    new String[]{DatabaseContract.RestaurantesEntry._ID,
                            DatabaseContract.RestaurantesEntry.COLUMN_NOME},
                    "(" + DatabaseContract.RestaurantesEntry.COLUMN_NOME + " LIKE ?) OR (" +
                            DatabaseContract.RestaurantesEntry.COLUMN_ENDERECO + " LIKE ?)",
                    new String[]{"%" + selectionArgs[0] + "%", "%" + selectionArgs[0] + "%"},
                    null
            );
            cursor = new MatrixCursor(SEARCH_SUGGEST_COLUMNS, 1);
            while(suggestions.moveToNext()) {
                cursor.addRow(new String[]{
                    suggestions.getString(0),
                        suggestions.getString(1).toLowerCase(),
                        null,
                        suggestions.getString(0)
                });
            }
            suggestions.close();
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException();
    }
}