package facin.com.cardapio_virtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class QuestionInfoActivity extends AppCompatActivity {

    private Intent intent;
    private TextView mRespostaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_info);
        mRespostaText = (TextView) findViewById(R.id.answer_text);

        // Pega Intent
        intent = getIntent();
        if (intent.getStringExtra(HelpActivity.EXTRA_RESPOSTA) != null ) {
            mRespostaText.setText(intent.getStringExtra(HelpActivity.EXTRA_RESPOSTA));
        }
    }
}
