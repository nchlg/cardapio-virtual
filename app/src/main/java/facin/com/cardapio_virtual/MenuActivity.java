package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.io.File;

import facin.com.cardapio_virtual.owlModels.OntologyInitializer;

public class MenuActivity extends AppCompatActivity
        implements ProductFragment.OnListFragmentInteractionListener {

    public File lanchesFile;

    // Intent
    private String intentNome;
    private String intentDescricao;
    private String intentEndereco;
    private String intentEmail;
    private String intentTelefone;
    protected static final String EXTRA_PRODUCT_NOME = "acin.com.cardapio_virtual.EXTRA_PRODUCT_NOME";
    protected static final String EXTRA_PRODUCT_INGREDIENTES = "acin.com.cardapio_virtual.EXTRA_PRODUCT_INGREDIENTES";
    protected static final String EXTRA_PRODUCT_PRECO = "acin.com.cardapio_virtual.EXTRA_PRODUCT_PRECO";
    protected static final String EXTRA_PRODUCT_QUANTIDADE = "acin.com.cardapio_virtual.EXTRA_PRODUCT_QUANTIDADE";

    private final static int REQUEST_RESTAURANT_INFO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_menu,
                            ProductFragment.newInstance(1))
                    .commit();
        }

        // Intent
        Intent intent = getIntent();
        if(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME) != null) {
            intentNome = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME);
        }
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO) != null) {
            intentDescricao = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO);
        } else {
            intentDescricao = getResources().getString(R.string.indisp_description);
        }
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO) != null) {
            intentEndereco = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO);
        } else {
            intentEndereco = getResources().getString(R.string.indisp_address);
        }
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) != null) {
            intentEmail = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL);
        } else {
            intentEmail = getResources().getString(R.string.indisp_email);
        }
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) != null) {
            intentTelefone = intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE);
        } else {
            intentTelefone = getResources().getString(R.string.indisp_phone_number);
        }
    }

    @Override
    public void onListFragmentInteraction(Product product) {
        Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
        intent.putExtra(EXTRA_PRODUCT_NOME, product.getNome());
        intent.putExtra(EXTRA_PRODUCT_INGREDIENTES, product.getIngredientesAsString());
        intent.putExtra(EXTRA_PRODUCT_PRECO, product.getPrecoAsString());
        if (product.isContavel())
            intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, Integer.toString(product.getQuantidade()));
        else
            intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, (String) null);
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        startActivityForResult(intent, -1);
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
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_NOME, intentNome);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, intentDescricao);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, intentEndereco);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, intentEmail);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, intentTelefone);
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        setResult(RESULT_OK, intent);
        finish();
    }
}
