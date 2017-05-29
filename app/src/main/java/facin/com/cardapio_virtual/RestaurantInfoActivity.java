package facin.com.cardapio_virtual;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.Normalizer;

import facin.com.cardapio_virtual.data.DatabaseContract;

public class RestaurantInfoActivity extends AppCompatActivity {

    private TextView mDescricaoText;
    private TextView mEnderecoText;
    private TextView mEmailText;
    private TextView mTelefoneText;
    private Button mBtnMenu;
    private Button mBtnFavoritar;
    private boolean favorito;

    // ProgressDialog
    private ProgressDialog progressDialog;

    // Intent
    private String intentNome;
    private String intentDescricao;
    private String intentEndereco;
    private String intentEmail;
    private String intentTelefone;
    private String auxEnderecoText;
    private String auxEmailText;
    private String auxTelefoneText;

    // Intent
    protected final static String EXTRA_NOME_RESTAURANTE_ASSET = "facin.com.cardapio_virtual.EXTRA_NOME_RESTAURANTE_ASSET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        // Pega Intent
        Intent intent = getIntent();
        intentNome = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME);
        if(intentNome != null) {
            if (getActionBar() != null)
                getActionBar().setTitle(intentNome);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(intentNome);

            getWindow().getDecorView()
                    .sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
        }

        // TextViews
        setTextViewContent(intent);

        // Buttons
        mBtnMenu = (Button) findViewById(R.id.btn_cardapio);
        mBtnMenu.setText(getResources().getString(R.string.btn_menu));
        mBtnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                    boolean temCardapio = false;
                    String restaurantNome = Normalizer
                            .normalize(intentNome.toLowerCase().replace(" ", ""), Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
                    for (String asset : getAssets().list("")) {
                        if (asset.contains(restaurantNome)) {
                            temCardapio = true;
                            intent.putExtra(EXTRA_NOME_RESTAURANTE_ASSET, restaurantNome);
                        }
                    }
                    if (!temCardapio) {
                        Toast.makeText(getApplicationContext(), R.string.toast_menu_unavailable, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intent.putExtra(MainActivity.EXTRA_RESTAURANT_NOME, intentNome);
                        intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, intentDescricao);
                        intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, intentEndereco);
                        intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, intentEmail);
                        intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, intentTelefone);
                        startActivityForResult(intent, 13);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mBtnFavoritar = (Button) findViewById(R.id.btn_favoritar);
        favorito = intent.getBooleanExtra(MainActivity.EXTRA_RESTAURANT_FAVORITO, false);
        alteraEstadoDoBotaoFavorito();
        mBtnFavoritar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (favorito) {
                    progressDialog = ProgressDialog.show(RestaurantInfoActivity.this,
                            null,
                            getResources().getText(R.string.progress_dialog_favoriting_unfavoriting),
                            true,
                            false);
                } else {
                    progressDialog = ProgressDialog.show(RestaurantInfoActivity.this,
                            null,
                            getResources().getText(R.string.progress_dialog_favoriting),
                            true,
                            false);
                }
                new FetchFavoritarTask().execute((Void) null);
            }
        });
    }

    protected void alteraEstadoDoBotaoFavorito() {
        if (favorito) {
            mBtnFavoritar.setText(R.string.btn_favorite_unfavorite);
            mBtnFavoritar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.lightBackground));
            mBtnFavoritar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        } else {
            mBtnFavoritar.setText(R.string.btn_favorite);
            mBtnFavoritar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            mBtnFavoritar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent2));
        }
    }

    protected void setTextViewContent(Intent intent) {
        mDescricaoText = (TextView) findViewById(R.id.descricao_text);
        mEnderecoText = (TextView) findViewById(R.id.endereco_text);
        mEmailText = (TextView) findViewById(R.id.email_text);
        mTelefoneText = (TextView) findViewById(R.id.telefone_text);
        // Popula os campos de texto com os dados do restuarante
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO) != null) {
            intentDescricao = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO);
        } else {
            intentDescricao = getResources().getString(R.string.indisp_description);
        }
        mDescricaoText.setText(intentDescricao);

        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO) != null) {
            intentEndereco = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO);
            auxEnderecoText = getResources().getString(R.string.pre_address) + ": " +
                     intentEndereco + ".";
        } else {
            intentEndereco = getResources().getString(R.string.indisp_address);
            auxEnderecoText = intentEndereco;
        }
        mEnderecoText.setText(auxEnderecoText);

        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) != null) {
            intentEmail = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL);
            auxEmailText = getResources().getString(R.string.pre_email) + ": " +
                     intentEmail + ".";
        } else {
            intentEmail = getResources().getString(R.string.indisp_email);
            auxEmailText = intentEmail;
        }
        mEmailText.setText(auxEmailText);

        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) != null) {
            intentTelefone = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE);
            auxTelefoneText = getResources().getString(R.string.pre_phone_number) + ": " +
                     intentTelefone + ".";
        } else {
            intentTelefone = getResources().getString(R.string.indisp_phone_number);
            auxTelefoneText = intentTelefone;
        }
        mTelefoneText.setText(auxTelefoneText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 13) {
            if (resultCode == RESULT_OK)
                setTextViewContent(data);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class FetchFavoritarTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... params) {
            try {
                ContentValues cv = new ContentValues();
                cv.put("favorito", favorito ? 0 : 1);
                getContentResolver().update(
                        DatabaseContract.RestaurantesEntry.CONTENT_URI,
                        cv,
                        DatabaseContract.RestaurantesEntry.COLUMN_NOME + "= ?",
                        new String[]{intentNome}
                );
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            favorito = !favorito;
            alteraEstadoDoBotaoFavorito();
            Log.d("Favorito", intentNome + "/" + String.valueOf(favorito));
            progressDialog.dismiss();
            if (favorito) {
                Toast favoritoToast = Toast.makeText(getApplicationContext(), R.string.toast_favorite, Toast.LENGTH_SHORT);
                favoritoToast.show();
            } else {
                Toast favoritoDesfavoritadoToast = Toast.makeText(getApplicationContext(), R.string.toast_favorite_unfavorite, Toast.LENGTH_SHORT);
                favoritoDesfavoritadoToast.show();
            }
        }
    }
}
