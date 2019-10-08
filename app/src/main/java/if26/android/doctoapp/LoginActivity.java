package if26.android.doctoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.Instantiate();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        setTitle(R.string.title_login);
    }
}
