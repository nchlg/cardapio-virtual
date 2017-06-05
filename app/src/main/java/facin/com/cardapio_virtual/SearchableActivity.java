package facin.com.cardapio_virtual;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import facin.com.cardapio_virtual.data.DatabaseContract;

public class SearchableActivity extends AppCompatActivity
    implements RestaurantsFragment.OnListFragmentInteractionListener {

    private Cursor searchCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,
                            RestaurantsFragment.newInstance(handleIntent(getIntent())))
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private String handleIntent(Intent intent) {
        String query = "";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //new FetchSearchTask().execute(query);
            //use the query to search your data somehow
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String data = intent.getDataString();
            intent =  new Intent(getApplicationContext(), RestaurantInfoActivity.class);
            intent.putExtra(MainActivity.EXTRA_RESTAURANT_PK, data);
        }
        return query;
    }

    // Métodos relacionado aos fragmentos
    public void onListFragmentInteraction(Restaurant item) {
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_PK, Integer.toString(item.getPrimaryKey()));
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_NOME, item.getNome());
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, item.getDescricao());
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, item.getEndereco());
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, item.getEmail());
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, item.getTelefone());
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        startActivityForResult(intent, -1);
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
                //recyclerView.setAdapter(new FavouriteRecyclerViewAdapter(favourites, mListener));
            }
        }
    }
}
