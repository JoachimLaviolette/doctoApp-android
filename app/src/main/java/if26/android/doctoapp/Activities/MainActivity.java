package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Codes.RequestCode;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {
    private Patient loggedUser;

    private ImageButton loginBtn;
    private EditText searchBar;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Instantiate();
        this.SubscribeEvents();
        this.RetrieveExtraParams();
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
     * Retrieve the extra params sent by the former view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();

        // Create a bundle containing the extra params
        Bundle bundle = i.getExtras();

        if (bundle == null) return;

        // Retrieve the params
        // Get the logged user
        String key = this.getResources().getString(R.string.intent_logged_user);
        this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
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
     * @param v The view that has been touched
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // This is to copy the exact behaviour of Doctolib's Main view
        if (v.getId() == R.id.main_search_bar
            &&  event.getAction() == MotionEvent.ACTION_DOWN) {
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

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start Search activity
     */
    private void Search() {
        // Create the intent
        Intent i = new Intent(MainActivity.this, SearchActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.main_intent_search);
        String value = this.searchBar.getText().toString().trim();
        i.putExtra(key, value);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        // Put extra parameters
        // The logged user
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check which request we're responding to
        if (requestCode == RequestCode.LOGGED_PATIENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the logged user
                Bundle bundle = intent.getExtras();
                String key = this.getResources().getString(R.string.intent_logged_user);
                this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
                if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());
            }
        }
    }
}
