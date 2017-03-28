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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RestaurantFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Cursor restaurantsCursor;
    private ArrayList<Restaurant> restaurants;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RestaurantFragment newInstance(int columnCount) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
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
            new FetchRestaurantTask().execute((Void) null);
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

    public class FetchRestaurantTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // insereDummies();
                restaurantsCursor = getActivity().getContentResolver().query(
                        DatabaseContract.RestaurantesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                if (restaurantsCursor != null) {
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
                restaurants = populaLista(restaurantsCursor);
                recyclerView.setAdapter(new MyRestaurantRecyclerViewAdapter(restaurants, mListener));
            }
        }

        public ArrayList<Restaurant> populaLista(Cursor cursor) {
            restaurants = new ArrayList<>();
            DatabaseUtils.dumpCursor(restaurantsCursor);
            /* Cria restaurantes */
            while(restaurantsCursor.moveToNext()) {
                Restaurant restaurant = new Restaurant(
                        Integer.parseInt(restaurantsCursor.getString(0)),
                        restaurantsCursor.getString(1),
                        restaurantsCursor.getString(2),
                        restaurantsCursor.getString(3),
                        restaurantsCursor.getString(4),
                        Double.parseDouble(restaurantsCursor.getString(5).trim()),
                        Double.parseDouble(restaurantsCursor.getString(6).trim()),
                        restaurantsCursor.getString(7)
                );
                restaurants.add(restaurant);
            }
            return restaurants;
        }

        public void insereDummies() {
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
            getActivity().getContentResolver().insert(DatabaseContract.RestaurantesEntry.CONTENT_URI, rest1);
        }
    }
}
