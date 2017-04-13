package facin.com.cardapio_virtual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import facin.com.cardapio_virtual.data.DatabaseContract;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RestaurantsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_QUERY = "search-query";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mSearchQuery = null;
    private OnListFragmentInteractionListener mListener;
    private Cursor restaurantsCursor;
    private Cursor searchesCursor;
    private ArrayList<Restaurant> restaurants;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RestaurantsFragment newInstance(String query) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mSearchQuery = getArguments().getString(ARG_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // Pode-se checar que a Atividade é instanceof SearchableActivity para garantia........
            new FetchRestaurantTask().execute(mSearchQuery);
            // recyclerView.setAdapter(new MyRestaurantRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Restaurant item);
    }

    public class FetchRestaurantTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Se mSearchQuery for nula, significa que a atividade é a MainActivity. Senão, é a SearchableActivity.
            if (params[0] == null) {
                try {
                    insereRestauranteTeste();
                    restaurantsCursor = getActivity().getContentResolver().query(
                            DatabaseContract.RestaurantesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                    if (restaurantsCursor != null) {
                        return "restaurantsCursor";
                    }
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                String searchQuery = params[0];
                try {
                    searchesCursor = getActivity().getContentResolver().query(
                            DatabaseContract.RestaurantesEntry.CONTENT_URI,
                            null,
                            DatabaseContract.RestaurantesEntry.COLUMN_NOME + " LIKE ?",
                            new String[]{"%" + searchQuery + "%"},
                            null
                    );
                    if (searchesCursor != null) {
                        return "searchesCursor";
                    }
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("restaurantsCursor")) {
                restaurants = populaLista(restaurantsCursor);
                recyclerView.setAdapter(new MyRestaurantRecyclerViewAdapter(restaurants, mListener));
            } else {
                if (result.equals("searchesCursor")) {
                    restaurants = populaLista(searchesCursor);
                    recyclerView.setAdapter(new MyRestaurantRecyclerViewAdapter(restaurants, mListener));
                }
            }
        }

        public ArrayList<Restaurant> populaLista(Cursor cursor) {
            restaurants = new ArrayList<>();
            DatabaseUtils.dumpCursor(cursor);
            /* Cria restaurantes */
            while(cursor.moveToNext()) {
                Restaurant restaurant = new Restaurant(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Double.parseDouble(cursor.getString(5).trim()),
                        Double.parseDouble(cursor.getString(6).trim()),
                        cursor.getString(7)
                );
                restaurants.add(restaurant);
            }
            return restaurants;
        }

        boolean insereRestauranteTeste() {
            Cursor testCursor = getActivity().getContentResolver().query(
                    DatabaseContract.RestaurantesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if (testCursor != null) {
                while(testCursor.moveToNext()) {
                    if (testCursor.getString(testCursor.getColumnIndex("nome")).equals("Bar do 5")) {
                        testCursor.close();
                        return false;
                    }
                }
            }

            Restaurant restaurant = new Restaurant(true);
            ContentValues rest1 = new ContentValues();
            rest1.put("_id", (byte[]) null);
            rest1.put("nome", restaurant.getNome());
            rest1.put("email",restaurant.getEmail());
            rest1.put("telefone",restaurant.getTelefone());
            rest1.put("endereco",restaurant.getEndereco());
            rest1.put("latitude",restaurant.getLatitude());
            rest1.put("longitude",restaurant.getLongitude());
            rest1.put("descricao",restaurant.getDescricao());
            rest1.put("favorito", restaurant.isFavorito() ? 1 : 0);
            getActivity().getContentResolver().insert(DatabaseContract.RestaurantesEntry.CONTENT_URI, rest1);

            if (testCursor != null ) {
                testCursor.close();
            }
            return true;
        }
    }
}
