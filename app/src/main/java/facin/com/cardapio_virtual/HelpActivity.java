package facin.com.cardapio_virtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class HelpActivity extends AppCompatActivity
    implements QuestionFragment.OnListFragmentInteractionListener {

    // Intent Extras
    protected static final String EXTRA_RESPOSTA = "facin.com.cardapio_virtual.EXTRA_RESPOSTA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_help,
                            QuestionFragment.newInstance(1))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(Question item) {
        Intent intent = new Intent(getApplicationContext(), QuestionInfoActivity.class);
        intent.putExtra(EXTRA_RESPOSTA, item.getAnswer());
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        startActivityForResult(intent, -1);
    }
}
