package facin.com.cardapio_virtual;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

import facin.com.cardapio_virtual.owlModels.OntologyInitializer;

public class MenuActivity extends AppCompatActivity
        implements ProductFragment.OnListFragmentInteractionListener {

    public File lanchesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public void onListFragmentInteraction(Product product) {

    }


//    public class FetchOntologyTask extends AsyncTask<Void, Boolean, Void> {
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            try {
//
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean result) {
//
//        }
//    }
}
