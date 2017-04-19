package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hp.hpl.jena.iri.impl.Main;

public class RestaurantInfoActivity extends AppCompatActivity {

    private TextView mDescricaoText;
    private TextView mEnderecoText;
    private TextView mEmailText;
    private TextView mTelefoneText;
    private Button mBtnMenu;

    // Intent
    private String intentDescricao;
    private String intentEndereco;
    private String intentEmail;
    private String intentTelefone;
    private String auxEnderecoText;
    private String auxEmailText;
    private String auxTelefoneText;

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
        setTextViewContent(intent);

        // Buttons
        mBtnMenu = (Button) findViewById(R.id.btn_cardapio);
        mBtnMenu.setText(getResources().getString(R.string.btn_menu));
        mBtnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantInfoActivity.this, MenuActivity.class);
                intent.putExtra(MainActivity.EXTRA_RESTAURANT_DESCRICAO, intentDescricao);
                intent.putExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO, intentEndereco);
                intent.putExtra(MainActivity.EXTRA_RESTAURANT_EMAIL, intentEmail);
                intent.putExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE, intentTelefone);
                startActivityForResult(intent, 13);
            }
        });

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13) {
            if (resultCode == RESULT_OK)
                setTextViewContent(data);
        }
    }
}
