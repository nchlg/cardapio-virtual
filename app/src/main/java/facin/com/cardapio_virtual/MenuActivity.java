package facin.com.cardapio_virtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import facin.com.cardapio_virtual.auxiliares.FiltroInterface;
import facin.com.cardapio_virtual.auxiliares.Filtros;
import facin.com.cardapio_virtual.auxiliares.Restricao;

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
    protected static String nomeRestauranteArquivo;

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
        if (intent.getStringExtra(RestaurantInfoActivity.EXTRA_NOME_RESTAURANTE_ASSET) != null) {
            nomeRestauranteArquivo = intent.getStringExtra(RestaurantInfoActivity.EXTRA_NOME_RESTAURANTE_ASSET);
        } else {
            nomeRestauranteArquivo = null;
        }
    }

    @Override
    public void onListFragmentInteraction(Product product) {
        try {
            Intent intent;
            if (product.getOntClass().listSubClasses().toList().isEmpty()) {
                intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
                intent.putExtra(EXTRA_PRODUCT_NOME, product.getNome());
                Log.d("Prod.", product.getNome());
                Log.d("Prod.", product.getIngredientes().toString());
                intent.putExtra(EXTRA_PRODUCT_INGREDIENTES, product.getIngredientesAsString());
                intent.putExtra(EXTRA_PRODUCT_PRECO, product.getPrecoAsString());
                if (product.getMapaRestricoes().get(Restricao.CONTAVEL))
                    intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, Integer.toString(product.getQuantidade()));
                else
                    intent.putExtra(EXTRA_PRODUCT_QUANTIDADE, (String) null);
                intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, intentOntClassURI);
            } else {
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, product.getOntClass().getURI());
            }
            configuraIntent(intent);
            startActivityForResult(intent, -1);
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), R.string.toast_product_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_NOME, intentNome);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, intentDescricao);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, intentEndereco);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, intentEmail);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, intentTelefone);
        intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, intentOntClassURI);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
    protected void configuraIntent(Intent intentDaAtividade) {
        Intent intent;
        if (intentDaAtividade == null)
            intent = new Intent();
        else
            intent = intentDaAtividade;
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_NOME, intentNome);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, intentDescricao);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, intentEndereco);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, intentEmail);
        intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, intentTelefone);
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
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
                final ProductFragment productFragment =
                        (ProductFragment) getSupportFragmentManager().findFragmentById(R.id.activity_menu);
                new MaterialDialog.Builder(MenuActivity.this)
                        .title(R.string.action_filter)
                        .items(R.array.media_filters)
                        .itemsCallbackMultiChoice(getFiltros(ProductFragment.getFiltros()), new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                ProductFragment.setFiltros(setFiltros(which));
                                productFragment.configuraProdutosAExibir();
                                productFragment.atualizaListaDeProdutos();
                                return true;
                            }
                        })
                        .positiveText(R.string.dialog_choose)
                        .contentColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .positiveColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .negativeText(R.string.dialog_cancel)
                        .negativeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .widgetColorRes(R.color.colorPrimary)
                        .buttonRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .show();
                return true;
            }
            case R.id.action_order: {
                final ProductFragment productFragment =
                        (ProductFragment) getSupportFragmentManager().findFragmentById(R.id.activity_menu);
                new MaterialDialog.Builder(this)
                        .title(R.string.action_order)
                        .items(R.array.media_orders)
                        .itemsCallbackSingleChoice(ProductFragment.getOrdem(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                ProductFragment.setOrdem(which);
                                productFragment.configuraProdutosAExibir();
                                productFragment.atualizaListaDeProdutos();
                                return true;
                            }
                        })
                        .positiveText(R.string.dialog_choose).positiveColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .negativeText(R.string.dialog_cancel)
                        .contentColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .negativeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .widgetColorRes(R.color.colorPrimary)
                        .buttonRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .show();
                return true;
            }
            case android.R.id.home: {
                Intent intent = new Intent();
                configuraIntent(intent);
                intent.putExtra(EXTRA_PRODUCT_ONTCLASS_URI, intentOntClassURI);
                setResult(RESULT_OK, intent);
                super.onBackPressed();
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
                case 3:
                    filtros.add(Filtros.getFiltroGordura());
                    break;
                case 4:
                    filtros.add(Filtros.getFiltroSal());
                    break;
                default:
                    break;
            }
        }
        return filtros;
    }

    private Integer[] getFiltros(List<FiltroInterface> filtros) {
        List<Integer> filtrosSelecionados = new ArrayList<>();

        for (FiltroInterface filtro : filtros) {
            if(filtro.getClass() == Filtros.getFiltroGluten().getClass()) {
                filtrosSelecionados.add(0);
            }
            else if(filtro.getClass() == Filtros.getFiltroLactose().getClass()) {
                filtrosSelecionados.add(1);
            }
            else if(filtro.getClass() == Filtros.getFiltroVegetariano().getClass()) {
                filtrosSelecionados.add(2);
            }
            else if(filtro.getClass() == Filtros.getFiltroGordura().getClass()) {
                filtrosSelecionados.add(3);
            }
            else if(filtro.getClass() == Filtros.getFiltroSal().getClass()) {
                filtrosSelecionados.add(4);
            }
        }

        return filtrosSelecionados.toArray(new Integer[0]);
    }
}
