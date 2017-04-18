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
            intentEndereco = getResources().getString(R.string.pre_address) + ": " +
                    intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_ENDERECO) + ".";
        } else {
            intentEndereco = getResources().getString(R.string.indisp_address);
        }
        mEnderecoText.setText(intentEndereco);

        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) != null) {
            intentEmail = getResources().getString(R.string.pre_email) + ": " +
                    intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_EMAIL) + ".";
        } else {
            intentEmail = getResources().getString(R.string.indisp_email);
        }
        mEmailText.setText(intentEmail);

        if (intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) != null) {
            intentTelefone = getResources().getString(R.string.pre_phone_number) + ": " +
                    intent.getStringExtra(MainActivity.EXTRA_RESTAURANT_TELEFONE) + ".";
        } else {
            intentTelefone = getResources().getString(R.string.indisp_phone_number);
        }
        mTelefoneText.setText(intentTelefone);
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
