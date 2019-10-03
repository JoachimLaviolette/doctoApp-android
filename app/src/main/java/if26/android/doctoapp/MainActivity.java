package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Instantiate();
        this.SubscribeEvents();

    }

    private void Instantiate() {
        this.loginBtn = findViewById(R.id.main_login);
    }

    private void SubscribeEvents() {
        this.loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_login:
                this.Login();
                break;
            default:
                return;
        }
    }

    private void Login() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
