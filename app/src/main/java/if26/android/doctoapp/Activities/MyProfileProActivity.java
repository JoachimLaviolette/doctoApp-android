package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;

public class MyProfileProActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_pro);

        this.Instantiate();
        this.RetrieveExtraParams();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
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
        this.loggedUser = bundle.containsKey(key) ?
                bundle.getSerializable(key) instanceof Patient ?
                        (Patient) bundle.getSerializable(key) :
                        (Doctor) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(getString(R.string.title_my_profile));
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: return;
        }
    }

    /**
     * Show a toast after update action was performed
     */
    private void MakeToast() {
        Context context = getApplicationContext();
        CharSequence content = this.getResources().getString(R.string.my_profile_toast_update_content);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, content, duration);
        toast.show();
    }
}
