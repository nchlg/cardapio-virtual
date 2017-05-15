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

import com.hp.hpl.jena.assembler.Content;

import facin.com.cardapio_virtual.data.DatabaseContract;

import java.util.ArrayList;
import java.util.List;

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
    private static boolean restaurantesInicializados = false;

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
                    if (!restaurantesInicializados) {
                        insereRestaurantes();
                        restaurantesInicializados = true;
                    }
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
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Double.parseDouble(cursor.getString(5).trim()),
                        Double.parseDouble(cursor.getString(6).trim()),
                        cursor.getString(7),
                        !cursor.getString(8).equals("0")
                );
                restaurants.add(restaurant);
            }
            return restaurants;
        }

    }
    boolean insereRestaurantes() {
        List<ContentValues> restaurantes = new ArrayList<>();

        ContentValues rest1 = new ContentValues();
        rest1.put("_id", (byte[]) null);
        rest1.put("nome", "Bar do 5");
        rest1.put("email", "bardo5@mail.com");
        rest1.put("telefone", "(51) 3354-1999");
        rest1.put("endereco", "Av. Ipiranga, 6681");
        rest1.put("latitude", -30.059947);
        rest1.put("longitude", -51.174464);
        rest1.put("descricao","Bar do prédio de História que oferece deliciosas opções vegetarianas e um ambiente aconchegante.");
        rest1.put("favorito", 0);
        restaurantes.add(rest1);

        ContentValues rest2 = new ContentValues();
        rest2.put("_id", (byte[]) null);
        rest2.put("nome", "Espaço 32");
        rest2.put("email", "espaco32@gmail.com");
        rest2.put("telefone", "(51) 3093-3256");
        rest2.put("endereco", "Av. Bento Gonçalves, 4314 (PUCRS - Prédio 32)");
        rest2.put("latitude", -30.061238);
        rest2.put("longitude", -51.173792);
        rest2.put("descricao", "Almoço com um delicioso buffet; lanches, cafés, coffee break, eventos corporativos e festas.");
        rest2.put("favorito", 0);
        restaurantes.add(rest2);

        try {
            Cursor restaurantesCursor = getActivity().getContentResolver().query(
                    DatabaseContract.RestaurantesEntry.CONTENT_URI,
                    new String[]{DatabaseContract.RestaurantesEntry.COLUMN_NOME},
                    null,
                    null,
                    null
            );

            if (restaurantesCursor != null) {
                restaurantesCursor.moveToFirst();
                do {
                    for (ContentValues cv : restaurantes) {
                        if (cv.get("nome").equals(restaurantesCursor.getString(0))) {

                        }
                    }
                }
            }
        }



        getActivity().getContentResolver().insert(DatabaseContract.RestaurantesEntry.CONTENT_URI, rest1);
        getActivity().getContentResolver().insert(DatabaseContract.RestaurantesEntry.CONTENT_URI, rest2);

        return true;
    }
}
