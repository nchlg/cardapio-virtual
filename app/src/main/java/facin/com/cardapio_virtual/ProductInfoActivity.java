package facin.com.cardapio_virtual;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import facin.com.cardapio_virtual.data.DatabaseContract;

public class ProductInfoActivity extends AppCompatActivity {

    private TextView mIngredientesText;
    private TextView mPrecoText;
    private TextView mQuantidadeText;
    private Intent intent;

    private String intentOntClassURI;
    private String intentProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        // Pega Intent
        intent = getIntent();
        if(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME) != null) {
            intentProduct = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME);
            if (getActionBar() != null)
                getActionBar().setTitle(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME));
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME));
        }
        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI) != null ) {
            intentOntClassURI = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI);
        }

        // TextViews
        setTextViewContent(intent);
        new FetchLogsTask().execute((Void) null);
    }

    protected void setTextViewContent(Intent intent) {
        String intentIngredientes;
        String intentPreco;
        String intentQuantidade;
        String auxIngredientesText;
        String auxPrecoText;
        String auxQuantidadeText;

        mIngredientesText = (TextView) findViewById(R.id.ingredientes_text);
        mPrecoText = (TextView) findViewById(R.id.preco_text);
        mQuantidadeText = (TextView) findViewById(R.id.quantidade_text);
        // Popula os campos de texto com os dados do restuarante
        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_INGREDIENTES) != null &&
                !intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_INGREDIENTES).equals("")) {
            intentIngredientes = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_INGREDIENTES);
            auxIngredientesText = getResources().getString(R.string.pre_ingredients) + ": " +
                    intentIngredientes + ".";
        } else {
            intentIngredientes = getResources().getString(R.string.indisp_ingredients);
            auxIngredientesText = intentIngredientes;
        }
        mIngredientesText.setText(auxIngredientesText);

        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_PRECO) != null) {
            intentPreco = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_PRECO);
            auxPrecoText = getResources().getString(R.string.pre_price) + ": " +
                    intentPreco + ".";
        } else {
            intentPreco = getResources().getString(R.string.indisp_price);
            auxPrecoText = intentPreco;
        }
        mPrecoText.setText(auxPrecoText);

        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_QUANTIDADE) != null) {
            intentQuantidade = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_QUANTIDADE);
            auxQuantidadeText = getResources().getString(R.string.pre_quantity) + ": " +
                    intentQuantidade + ".";
        } else {
            intentQuantidade = getResources().getString(R.string.indisp_quantity);
            auxQuantidadeText = intentQuantidade;
        }
        mQuantidadeText.setText(auxQuantidadeText);
    }

    @Override
    public void onBackPressed() {
        setIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {
                setIntent();
                return true;
            }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    protected void setIntent() {
        Intent intent = new Intent();
        intent.putExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI, intentOntClassURI);
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13) {
            if (resultCode == RESULT_OK)
                setTextViewContent(data);
        }
    }

    public class FetchLogsTask extends AsyncTask<Void, Void, Boolean> {

        Cursor logsCursor;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (intentProduct != null) {
                    // Busca número de acessos na tabela
                    logsCursor = getContentResolver().query(
                            DatabaseContract.LogsEntry.CONTENT_URI,
                            new String[]{DatabaseContract.LogsEntry.COLUMN_ACESSOS},
                            DatabaseContract.LogsEntry.COLUMN_PRODUTO + " = ?",
                            new String[]{intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME)},
                            null
                    );
                    if (logsCursor != null) {
                        logsCursor.moveToFirst();
                        ContentValues log = new ContentValues();
//                        log.put("_id", (byte[]) null);
//                        log.put("produto", intentProduct);
                          log.put("acessos", String.valueOf(Integer.valueOf(logsCursor.getString(0)) + 1));

                        getContentResolver().update(
                                DatabaseContract.LogsEntry.CONTENT_URI,
                                log,
                                DatabaseContract.LogsEntry.COLUMN_PRODUTO + "= ?",
                                new String[]{intentProduct});
                        return true;
                    }
//                    logsCursor = getContentResolver().query(
//                            DatabaseContract.LogsEntry.CONTENT_URI,
//                            null,
//                            null,
//                            null,
//                            null
//                    );
//                    if (logsCursor != null) {
//                        logsCursor.moveToFirst();
//                        do {
//                            Log.d("Logs", logsCursor.getString(0) + "/" + logsCursor.getString(1) + "/" + logsCursor.getString(2));
//                        } while (logsCursor.moveToNext());
//                        return true;
//                    }
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
    }
}
