package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProductInfoActivity extends AppCompatActivity {

    private TextView mIngredientesText;
    private TextView mPrecoText;
    private TextView mQuantidadeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        // Pega Intent
        Intent intent = getIntent();
        if(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME) != null) {
            if (getActionBar() != null)
                getActionBar().setTitle(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME));
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_NOME));
        }

        // TextViews
        setTextViewContent(intent);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13) {
            if (resultCode == RESULT_OK)
                setTextViewContent(data);
        }
    }
}
