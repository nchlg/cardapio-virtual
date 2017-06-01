package facin.com.cardapio_virtual;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import facin.com.cardapio_virtual.auxiliares.Utilitarios;
import facin.com.cardapio_virtual.data.SuggestionProvider;
import facin.com.cardapio_virtual.owlModels.OntologyInitializer;

public class MainActivity extends AppCompatActivity
        implements FavouritesFragment.OnListFragmentInteractionListener,
        RestaurantsFragment.OnListFragmentInteractionListener,
        MyMapFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private GoogleMap map;
    // Localização
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected static Location mLastLocation = null;
    protected double mLatitude = 0.0;
    protected double mLongitude = 0.0;
    // Constant used in the location settings dialog.
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // Intent EXTRA
    protected static final String EXTRA_RESTAURANT_PK = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_PK";
    protected static final String EXTRA_RESTAURANT_NOME = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_NOME";
    protected static final String EXTRA_RESTAURANT_DESCRICAO = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_DESCRICAO";
    protected static final String EXTRA_RESTAURANT_ENDERECO = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_ENDERECO";
    protected static final String EXTRA_RESTAURANT_EMAIL = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_EMAIL";
    protected static final String EXTRA_RESTAURANT_TELEFONE = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_TELEFONE";
    protected static final String EXTRA_RESTAURANT_FAVORITO = "facin.com.cardapio_virtual.EXTRA_RESTAURANT_FAVORITO";

    protected static final String RESTAURANTS_FRAGMENT_TAG = "facin.com.cardapio_virtual.RESTAURANTS_FRAGMENT_TAG";
    protected static final String FAVOURITES_FRAGMENT_TAG = "facin.com.cardapio_virtual.FAVOURITES_FRAGMENT_TAG";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mSlidingTabLayout;

    // Fragmentos
    RestaurantsFragment restaurantsFragment;
    FavouritesFragment favouritesFragment;
    List<Fragment> fragmentos = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Cria barra de abas
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fragmentos
        fragmentos.add(favouritesFragment = FavouritesFragment.newInstance(1));
        fragmentos.add(RestaurantsFragment.newInstance(""));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentos);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (TabLayout) findViewById(R.id.tab_examinador);
        mSlidingTabLayout.setTabGravity(mSlidingTabLayout.GRAVITY_FILL);
        mSlidingTabLayout.setupWithViewPager(mViewPager);
        // Tab/Aba 0
        mSlidingTabLayout.getTabAt(0).setText(R.string.tab_fav);
        mSlidingTabLayout.getTabAt(0).setContentDescription(
                getResources().getString(R.string.tab_fav) +
                        ": " +
                getResources().getString(R.string.tab_description) + " " +
                        (0 + 1) +
                        " de "
                        + mSlidingTabLayout.getTabCount());
        // Tab/Aba 1
        mSlidingTabLayout.getTabAt(1).setText(R.string.tab_restaurants);
        mSlidingTabLayout.getTabAt(1).setContentDescription(
                getResources().getString(R.string.tab_restaurants) +
                        ": " +
                        getResources().getString(R.string.tab_description) + " " +
                        (1 + 1) +
                        " de "
                        + mSlidingTabLayout.getTabCount());
        // Tab/Aba 2
        //mSlidingTabLayout.getTabAt(2).setText(R.string.tab_map);
        // Google API
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.main_content,
//                            RestaurantsFragment.newInstance(""), RESTAURANTS_FRAGMENT_TAG)
//                    .commit();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.main_content,
//                            FavouritesFragment.newInstance(1), FAVOURITES_FRAGMENT_TAG)
//                    .commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor sugestoes = getContentResolver().query(
                        SuggestionProvider.CONTENT_URI,
                        null,
                        null,
                        new String[]{newText},
                        null
                );
                searchView.setSuggestionsAdapter(new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.fragment_question,
                        sugestoes,
                        null,
                        null,
                        0));
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help: {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivityForResult(intent, -1);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Métodos relacionado aos fragmentos
    public void onListFragmentInteraction(Restaurant item) {
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_PK, Integer.toString(item.getPrimaryKey()));
        intent.putExtra(EXTRA_RESTAURANT_NOME, item.getNome());
        intent.putExtra(EXTRA_RESTAURANT_DESCRICAO, item.getDescricao());
        intent.putExtra(EXTRA_RESTAURANT_ENDERECO, item.getEndereco());
        intent.putExtra(EXTRA_RESTAURANT_EMAIL, item.getEmail());
        intent.putExtra(EXTRA_RESTAURANT_TELEFONE, item.getTelefone());
        intent.putExtra(EXTRA_RESTAURANT_FAVORITO, item.isFavorito());
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        startActivityForResult(intent, -1);
    }

    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        /* You can get the current location settings of a user's device.
        To do this, create a LocationSettingsRequest.Builder, and add one or more location requests
         */
        LocationSettingsRequest.Builder locationSettingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        /* Sempre perguntar */
        locationSettingsBuilder.setAlwaysShow(true);

        /* Next check whether the current location settings are satisfied. */
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        locationSettingsBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            break;
                        }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Utilitarios.ordenaRestaurantes(((FavouritesFragment) fragmentos.get(0)).getFavoritos(), mLastLocation);
        ((FavouritesFragment) fragmentos.get(0)).atualizaRecyclerView();

        Utilitarios.ordenaRestaurantes(((RestaurantsFragment) fragmentos.get(1)).getRestaurantes(), mLastLocation);
        ((RestaurantsFragment) fragmentos.get(1)).atualizaRecyclerView();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        protected List<Fragment> fragmentosAdapter;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentosAdapter) {
            super(fm);
            this.fragmentosAdapter = fragmentosAdapter;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1)
                // Restaurantes
                return fragmentosAdapter.get(1);
//            else if (position == 2) {
//                /*map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//                        .getMap();
//                if (map!=null) {
//                    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
//                            .title("Hamburg"));
//                    Marker kiel = map.addMarker(new MarkerOptions()
//                            .position(KIEL)
//                            .title("Kiel")
//                            .snippet("Kiel is cool")
//                            .icon(BitmapDescriptorFactory
//                                    .fromResource(R.drawable.ic_launcher)));
//                }*/
//                return new MyMapFragment();
//            }
            else
                // Favoritos
                return fragmentosAdapter.get(0);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return fragmentosAdapter.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_fav);
                case 1:
                    return getResources().getString(R.string.tab_restaurants);
//                case 2:
//                    return getResources().getString(R.string.tab_map);
            }
            return null;
        }
    }
}
