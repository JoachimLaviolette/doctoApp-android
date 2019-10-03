package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton loginBtn;
    private EditText searchEditText;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Instantiate();
        this.SubscribeEvents();
    }

    private void Instantiate() {
        this.loginBtn = findViewById(R.id.main_login);
        this.searchEditText = findViewById(R.id.main_search);
        this.searchBtn = findViewById(R.id.main_search_btn);
    }

    private void SubscribeEvents() {
        this.loginBtn.setOnClickListener(this);
        this.searchEditText.setOnClickListener(this);
        this.searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_login:
                this.Login();
                break;
            case R.id.main_search:
            case R.id.main_search_btn:
                this.Search();
                break;
            default:
                return;
        }
    }

    private void Login() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    private void Search() {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(i);
    }
}
