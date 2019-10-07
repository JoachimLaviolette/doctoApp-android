package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {

    private ImageButton loginBtn;
    private EditText searchBar;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Instantiate();
        this.SubscribeEvents();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.loginBtn = findViewById(R.id.main_login);
        this.searchBar = findViewById(R.id.main_search_bar);
        this.searchBtn = findViewById(R.id.main_search_btn);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.loginBtn.setOnClickListener(this);
        this.searchBar.setOnClickListener(this);
        this.searchBtn.setOnClickListener(this);
        this.searchBar.setOnTouchListener(this);
    }

    /**
     * Handle click events
     * @param v The Main view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_login:
                this.Login();
                break;
            case R.id.main_search_bar:
            case R.id.main_search_btn:
                this.Search();
        }
    }

    /**
     * Handle touch events
     * @param v The Main view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.main_search_bar) {
            this.Search();

            return true;
        }

        return false;
    }

    /**
     * Start Login activity
     */
    private void Login() {
        // Create the intent
        Intent i = new Intent(MainActivity.this, LoginActivity.class);

        // Start the activity
        startActivity(i);
    }

    /**
     * Start Search activity
     */
    private void Search() {
        // Create the intent
        Intent i = new Intent(MainActivity.this, SearchActivity.class);

        // Put extra parameters
        String key = this.getResources().getString(R.string.main_intent_search);
        String value = this.searchBar.getText().toString();
        i.putExtra(key, value);

        // Start the activity
        startActivity(i);
    }
}
