package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.EncryptionService;
import if26.android.doctoapp.Codes.RequestCode;

public class LoginActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Patient loggedUser;

    private LinearLayout loginMsg;
    private TextView loginMsgTitle;
    private TextView loginMsgContent;
    private EditText emailInput;
    private EditText passwordInput;
    private CheckBox stayLogged;
    private TextView forgotPwd;
    private LinearLayout optionsSection;
    private Button loginBtn;
    private Button logoutBtn;
    private TextView signupLink;
    private LinearLayout signupSection;
    private TextView proAccountLink;
    private LinearLayout professionalSection;

    private static final int LOGOUT = 0;
    private static final int LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.Instantiate();
        this.SubscribeEvents();
        this.RetrieveExtraParams();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.loggedUser = null;
        this.loginMsg = findViewById(R.id.login_msg);
        this.loginMsgTitle = findViewById(R.id.login_msg_title);
        this.loginMsgContent = findViewById(R.id.login_msg_content);
        this.emailInput = findViewById(R.id.login_email);
        this.passwordInput = findViewById(R.id.login_pwd);
        this.stayLogged = findViewById(R.id.login_stay_logged);
        this.forgotPwd = findViewById(R.id.login_forgot_pwd);
        this.optionsSection = findViewById(R.id.login_options_section);
        this.loginBtn = findViewById(R.id.login_btn);
        this.logoutBtn = findViewById(R.id.login_logout_btn);
        this.signupLink = findViewById(R.id.login_signup_link);
        this.signupSection = findViewById(R.id.login_signup_section);
        this.proAccountLink = findViewById(R.id.login_pro_account_link);
        this.professionalSection = findViewById(R.id.login_pro_account_section);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.loginMsg.setOnClickListener(this);
        this.forgotPwd.setOnClickListener(this);
        this.loginBtn.setOnClickListener(this);
        this.logoutBtn.setOnClickListener(this);
        this.signupLink.setOnClickListener(this);
        this.proAccountLink.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(R.string.title_login);
        this.SetLoginContext();

        if (this.loggedUser != null) {
            this.DisplaySuccessMsg();
        }
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
            case R.id.login_forgot_pwd:
                this.ForgotPwd();

                return;
            case R.id.login_btn:
                this.Login();

                return;
            case R.id.login_logout_btn:
                this.Logout();

                return;
            case R.id.login_signup_link:
                this.Signup();

                return;
            case R.id.login_pro_account_link:
                this.LoginProAccount();

                return;
            case R.id.login_msg:
                this.MyBookings();

                return;
        }
    }

    /**
     * Set Login context
     */
    private void SetLoginContext() {
        this.emailInput.setVisibility(View.VISIBLE);
        this.passwordInput.setVisibility(View.VISIBLE);
        this.optionsSection.setVisibility(View.VISIBLE);
        this.loginBtn.setVisibility(View.VISIBLE);
        this.logoutBtn.setVisibility(View.GONE);
        this.signupSection.setVisibility(View.VISIBLE);
        this.professionalSection.setVisibility(View.VISIBLE);
        this.loginMsg.setVisibility(View.GONE);
        this.emailInput.setText("");
        this.passwordInput.setText("");
        this.stayLogged.setChecked(false);
    }

    /**
     * Execute login process
     */
    private void Login() {
        String inputEmail = this.emailInput.getText().toString();

        // Try to get a patient using the provided username
        Patient patient = (new PatientDatabaseHelper(this.getApplicationContext())).GetPatientByEmail(inputEmail);

        if (patient != null) {
            String inputPwd = this.passwordInput.getText().toString().trim();
            String salt = patient.getPwdSalt();
            String hashedInputPwd = EncryptionService.SHA1(inputPwd + salt);
            String patientPwd = patient.getPwd();

            if (patientPwd.equals(hashedInputPwd)) {
                // Logged-in
                this.DisplaySuccessMsg();
                this.MakeToast(this.LOGIN);
                this.loggedUser = patient;
                Intent i = new Intent();
                String key = this.getResources().getString(R.string.intent_logged_user);
                i.putExtra(key, this.loggedUser);
                setResult(RESULT_OK, i);
                finish();

                return;
            }
        }

        // Display error message
        this.DisplayErrorMsg();
    }

    /**
     * Display a success message to notify the connexion succeeded
     */
    private void DisplaySuccessMsg() {
        this.loginMsg.setVisibility(View.VISIBLE);
        this.loginMsg.setBackgroundResource(R.color.login_success_msg);
        this.loginMsgTitle.setText(this.getResources().getString(R.string.login_success_msg_title));
        this.loginMsgContent.setText(this.getResources().getString(R.string.login_success_msg_content));
        this.emailInput.setVisibility(View.GONE);
        this.passwordInput.setVisibility(View.GONE);
        this.optionsSection.setVisibility(View.GONE);
        this.loginBtn.setVisibility(View.GONE);
        this.logoutBtn.setVisibility(View.VISIBLE);
        this.signupSection.setVisibility(View.GONE);
        this.professionalSection.setVisibility(View.GONE);
    }

    /**
     * Display an error message to notify the connexion failed
     */
    private void DisplayErrorMsg() {
        this.loginMsg.setVisibility(View.VISIBLE);
        this.loginMsg.setBackgroundResource(R.color.login_error_msg);
        this.loginMsgTitle.setText(this.getResources().getString(R.string.login_error_msg_title));
        this.loginMsgContent.setText(this.getResources().getString(R.string.login_error_msg_content));
    }

    /**
     * Disconnect the logged-in patient
     */
    private void Logout() {
        this.loggedUser = null;
        this.SetLoginContext();
        this.MakeToast(this.LOGOUT);
    }

    /**
     * Start Signup activity
     */
    private void Signup() {
        // Create the intent
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start ForgotPwd activity
     */
    private void ForgotPwd() {

    }

    /**
     * Start LoginProAccount activity
     */
    private void LoginProAccount() {

    }

    /**
     * Start MyBookings activity
     */
    private void MyBookings() {
        // Create the intent
        Intent i = new Intent(LoginActivity.this, MyBookingsActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Show a toast after LOGIN or LOGOUT action
     * @param action LOGIN or LOGOUT action
     */
    private void MakeToast(int action) {
        Context context = getApplicationContext();
        CharSequence content;
        Toast toast;
        int duration = Toast.LENGTH_LONG;

        switch (action) {
            case LOGOUT:
                content = this.getResources().getString(R.string.login_toast_logout_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case LOGIN:
                content = this.getResources().getString(R.string.login_toast_login_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
        }
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
