package if26.android.doctoapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.DatabaseHelpers.DoctorDatabaseHelper;
import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.EncryptionService;

public class ForgotPasswordActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private LinearLayout updatedPasswordMsg;
    private TextView updatedPasswordMsgTitle;
    private TextView updatedPasswordMsgContent;

    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private Button setNewPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.emailInput = findViewById(R.id.forgot_password_email);
        this.passwordInput = findViewById(R.id.forgot_password_pwd);
        this.confirmPasswordInput = findViewById(R.id.forgot_password_confirm_pwd);
        this.updatedPasswordMsg = findViewById(R.id.forgot_password_password_updated_msg);
        this.updatedPasswordMsgTitle = findViewById(R.id.forgot_password_password_updated_msg_title);
        this.updatedPasswordMsgContent = findViewById(R.id.forgot_password_password_updated_msg_content);
        this.setNewPasswordBtn = findViewById(R.id.forgot_password_btn);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.setNewPasswordBtn.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(R.string.title_forgot_password);
        this.updatedPasswordMsg.setVisibility(View.GONE);
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forgot_password_btn) this.SetNewPassword();
    }

    /**
     * Try to set the password associated to the given email address
     */
    private void SetNewPassword() {
        if (!this.AllFieldsCorrect()) {
            this.DisplayErrorMsg();

            return;
        }

        Resident user;

        if ((user = this.TryLoginAsDoctor()) == null
            && (user = this.TryLoginAsPatient()) == null) {
            this.DisplayErrorMsg();

            return;
        }

        String inputPwd = this.passwordInput.getText().toString().trim();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1(inputPwd + pwdSalt);

        user.setPwd(pwd);
        user.setPwdSalt(pwdSalt);

        if (user instanceof Doctor) (new DoctorDatabaseHelper(this)).UpdateDoctor((Doctor) user);
        else (new PatientDatabaseHelper(this)).UpdatePatient((Patient) user);

        this.DisplaySuccessMsg();
    }

    /**
     * Try to login as a patient
     * @return patient The patient matched if any or null
     */
    private Patient TryLoginAsPatient() {
        String inputEmail = this.emailInput.getText().toString();

        // Try to get a patient using the provided email and return it
        return (new PatientDatabaseHelper(this.getApplicationContext())).GetPatientByEmail(inputEmail);
    }

    /**
     * Try to login as a doctor
     * @return doctor The doctor matched if any or null
     */
    private Doctor TryLoginAsDoctor() {
        String inputEmail = this.emailInput.getText().toString();

        // Try to get a doctor using the provided email and return it
        return (new DoctorDatabaseHelper(this.getApplicationContext())).GetDoctorByEmail(inputEmail);
    }

    /**
     * Check that all the fields are filled properly
     * @return If all the fields are correctly filled
     */
    private boolean AllFieldsCorrect() {
        boolean isOneFieldEmpty =
                this.emailInput.getText().toString().trim().isEmpty()
                        || this.passwordInput.getText().toString().trim().isEmpty()
                        || this.confirmPasswordInput.getText().toString().trim().isEmpty();

        // One of the fields is empty
        if (isOneFieldEmpty) return false;

        // Both passwords do not correspond
        return this.passwordInput.getText().toString().trim().
                equals(this.confirmPasswordInput.getText().toString().trim());
    }

    /**
     * Display a success message to notify the password update succeeded
     */
    private void DisplaySuccessMsg() {
        this.updatedPasswordMsg.setVisibility(View.VISIBLE);
        this.updatedPasswordMsg.setBackgroundResource(R.color.forgot_password_success_msg);
        this.updatedPasswordMsgTitle.setText(this.getResources().getString(R.string.forgot_password_success_msg_title));
        this.updatedPasswordMsgContent.setText(this.getResources().getString(R.string.forgot_password_success_msg_content));
    }

    /**
     * Display an error message to notify the password update failed
     */
    private void DisplayErrorMsg() {
        this.updatedPasswordMsg.setVisibility(View.VISIBLE);
        this.updatedPasswordMsg.setBackgroundResource(R.color.forgot_password_error_msg);
        this.updatedPasswordMsgTitle.setText(this.getResources().getString(R.string.forgot_password_error_msg_title));
        this.updatedPasswordMsgContent.setText(this.getResources().getString(R.string.forgot_password_error_msg_content));
    }
}
