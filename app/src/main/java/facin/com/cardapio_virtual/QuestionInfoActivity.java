package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

public class QuestionInfoActivity extends AppCompatActivity {

    private Intent intent;
    private TextView mRespostaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_info);

        // Pega Intent
        intent = getIntent();
        if (intent.getStringExtra(MenuActivity.EXTRA_PRODUCT_ONTCLASS_URI) != null ) {
            mRespostaText.setText(intent.getStringExtra(HelpActivity.EXTRA_RESPOSTA));
        }
    }
}
