package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements ProductFragment.OnListFragmentInteractionListener {

    // Intent
    private String intentNome;
    private String intentDescricao;
    private String intentEndereco;
    private String intentEmail;
    private String intentTelefone;
    private String intentOntClassURI;
    protected static final String SOURCE = "http://www.semanticweb.org/priscila/ontologies/2017/3/untitled-ontology-3";
    protected static final String NS = SOURCE + "#";
    protected static final String EXTRA_PRODUCT_NOME = "facin.com.cardapio_virtual.EXTRA_PRODUCT_NOME";
    protected static final String EXTRA_PRODUCT_INGREDIENTES = "facin.com.cardapio_virtual.EXTRA_PRODUCT_INGREDIENTES";
    protected static final String EXTRA_PRODUCT_PRECO = "facin.com.cardapio_virtual.EXTRA_PRODUCT_PRECO";
    protected static final String EXTRA_PRODUCT_QUANTIDADE = "facin.com.cardapio_virtual.EXTRA_PRODUCT_QUANTIDADE";
    protected static final String EXTRA_PRODUCT_ONTCLASS_URI = "facin.com.cardapio_virtual.EXTRA_PRODUCT_ONTCLASS_URI";

    protected static final String PRODUCT_FRAGMENT_TAG = "facin.com.cardapio_virtual.PRODUCT_FRAGMENT_TAG";

    private final static int REQUEST_RESTAURANT_INFO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_menu,
                            ProductFragment.newInstance(1), PRODUCT_FRAGMENT_TAG)
                    .commit();
        }

        // Intent
        Intent intent = getIntent();
        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME) != null) {
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
        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI) != null) {
            intentOntClassURI = intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI);
        } else {
            intentOntClassURI = NS + "Produto";
        }

    }

    @Override
    public void onListFragmentInteraction(Product product) {
        if (product.getOntClass().listSubClasses().toList().isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
            intent.putExtra(EXTRA_PRODUCT_NOME, product.getNome());
            intent.putExtra(EXTRA_PRODUCT_INGREDIENTES, product.getIngredientesAsString());
            intent.putExtra(EXTRA_PRODUCT_PRECO, product.getPrecoAsString());
            if (product.isContavel())
                intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, Integer.toString(product.getQuantidade()));
            else
                intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, (String) null);
            intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, intentOntClassURI);
            // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
            startActivityForResult(intent, -1);
        } else {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, product.getOntClass().getURI());
            startActivityForResult(intent, -1);
        }
    }

    @Override
    public void onBackPressed() {
        setIntent();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case android.R.id.home: {
//                setIntent();
//                return true;
//            }
//            default:
//                return super.onOptionsItemSelected(menuItem);
//        }
//    }

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

    public String getIntentOntClassURI() {
        return intentOntClassURI;
    }

    public void setIntentOntClassURI(String intentOntClassLocalName) {
        this.intentOntClassURI = intentOntClassLocalName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_filter: {
                new MaterialDialog.Builder(MenuActivity.this)
                        .title(R.string.action_filter)
                        .items(R.array.media_filters)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                ProductFragment productFragment =
                                        (ProductFragment) getSupportFragmentManager().findFragmentById(R.id.activity_menu);
                                productFragment.setFiltros(setFiltros(which));
                                return true;
                            }
                        })
                        .positiveText(R.string.dialog_choose)
                        .widgetColorRes(R.color.colorPrimary)
                        .show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<FiltroInterface> setFiltros(Integer[] which) {
        List<FiltroInterface> filtros = new ArrayList<>();
        for (int i : which) {
            switch (i) {
                case 0:
                    filtros.add(Filtros.getFiltroGluten());
                    break;
                case 1:
                    filtros.add(Filtros.getFiltroLactose());
                    break;
                case 2:
                    filtros.add(Filtros.getFiltroVegetariano());
                    break;
                default:
                    break;
            }
        }
        return filtros;
    }
}
