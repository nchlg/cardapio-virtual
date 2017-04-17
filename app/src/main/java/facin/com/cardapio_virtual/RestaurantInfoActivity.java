package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class RestaurantInfoActivity extends AppCompatActivity {

    private TextView mDescricaoText;
    private TextView mEnderecoText;
    private TextView mEmailText;
    private TextView mTelefoneText;
    private Button mBtnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        // Pega Intent
        Intent intent = getIntent();
        if(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME) != null) {
            if (getActionBar() != null)
                getActionBar().setTitle(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME));
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_NOME));
        }

        // TextViews
        mDescricaoText = (TextView) findViewById(R.id.descricao_text);
        mEnderecoText = (TextView) findViewById(R.id.endereco_text);
        mEmailText = (TextView) findViewById(R.id.email_text);
        mTelefoneText = (TextView) findViewById(R.id.telefone_text);
        // Popula os campos de texto com os dados do restuarante
        mDescricaoText.setText(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO) != null ?
                intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO) :
                getResources().getString(R.string.indisp_description));

        mEnderecoText.setText(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO) != null ?
                intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO) :
                getResources().getString(R.string.indisp_address));

        mEmailText.setText(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) != null ?
                intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) :
                getResources().getString(R.string.indisp_email));

        mTelefoneText.setText(intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) != null ?
                intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) :
                getResources().getString(R.string.indisp_phone_number));

        // Buttons
        mBtnMenu = (Button) findViewById(R.id.btn_cardapio);
        mBtnMenu.setText(getResources().getString(R.string.btn_menu));
    }
}
