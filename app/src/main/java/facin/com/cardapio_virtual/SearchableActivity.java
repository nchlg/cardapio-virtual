package facin.com.cardapio_virtual;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import facin.com.cardapio_virtual.data.DatabaseContract;

public class SearchableActivity extends AppCompatActivity {

    private Cursor searchCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new FetchSearchTask().execute(query);
            //use the query to search your data somehow
        }
    }

    public class FetchSearchTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            String searchQuery = params[0];

            try {
                searchCursor = getContentResolver().query(
                        DatabaseContract.RestaurantesEntry.CONTENT_URI,
                        null,
                        DatabaseContract.RestaurantesEntry.COLUMN_NOME + " = ?",
                        new String[]{searchQuery},
                        null
                );
                if (searchCursor != null) {
                    return true;
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                // TODO: Criar lista com os restaurantes que voltaram na Query. A classe SearchableActivity deve ter uma lista.
                //favourites = populaLista(restaurantsCursor);
                //recyclerView.setAdapter(new MyFavouriteRecyclerViewAdapter(favourites, mListener));
            }
        }
    }
}
