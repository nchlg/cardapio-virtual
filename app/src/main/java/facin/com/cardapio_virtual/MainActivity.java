package facin.com.cardapio_virtual;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity
        implements FavouritesFragment.OnListFragmentInteractionListener,
        RestaurantsFragment.OnListFragmentInteractionListener,
        MyMapFragment.OnFragmentInteractionListener {

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
    // Constant used in the location settings dialog.
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Cria barra de abas
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (TabLayout)findViewById(R.id.tab_examinador);
        mSlidingTabLayout.setTabGravity(mSlidingTabLayout.GRAVITY_FILL);
        mSlidingTabLayout.setupWithViewPager(mViewPager);
        // Tab/Aba 0
        mSlidingTabLayout.getTabAt(0).setText(R.string.tab_fav);
        // Tab/Aba 1
        mSlidingTabLayout.getTabAt(1).setText(R.string.tab_restaurants);
        // Tab/Aba 2
        mSlidingTabLayout.getTabAt(2).setText(R.string.tab_map);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(this);

        return true;
    }



    /*@Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Métodos relacionado aos fragmentos
    public void onListFragmentInteraction(Restaurant item) {

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


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1)
                return new RestaurantsFragment();
            else if (position == 2) {
                /*map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                if (map!=null) {
                    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                            .title("Hamburg"));
                    Marker kiel = map.addMarker(new MarkerOptions()
                            .position(KIEL)
                            .title("Kiel")
                            .snippet("Kiel is cool")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.ic_launcher)));
                }*/
                return new MyMapFragment();
            }
            else
                return new FavouritesFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    // TODO: mudar descrição para algo como "Aba X de Y".
                    return getResources().getString(R.string.tab_fav);
                case 1:
                    return getResources().getString(R.string.tab_restaurants);
                case 2:
                    return getResources().getString(R.string.tab_map);
            }
            return null;
        }
    }
}
