package facin.com.cardapio_virtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RestaurantInfoActivity extends AppCompatActivity {

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

    }
}
