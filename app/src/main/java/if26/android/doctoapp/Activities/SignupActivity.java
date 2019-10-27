package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import if26.android.doctoapp.BuildConfig;
import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;
import if26.android.doctoapp.Models.Address;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.EncryptionService;
import if26.android.doctoapp.Services.ImageService;

public class SignupActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Patient loggedUser;

    private LinearLayout signupMsg;
    private TextView signupMsgTitle;
    private TextView signupMsgContent;

    private CircleImageView picture;
    private Button takePictureFromCamera;
    private Button selectPictureFromGallery;
    private String picturePath;
    private Uri pictureURI;

    private EditText lastnameInput;
    private EditText firstnameInput;
    private EditText birthdateInput;
    private EditText emailInput;
    private EditText insuranceNumberInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private EditText street1Input;
    private EditText street2Input;
    private EditText cityInput;
    private EditText zipInput;
    private EditText countryInput;

    private LinearLayout patientProfileSection, patientAddressSection;

    private Button signupBtn;

    private TextView loginLink;

    private TextView proAccountLink;
    private LinearLayout professionalSection;

    private static final int LOGOUT = 0;
    private static final int LOGIN = 1;

    private static final String KEY_TAKE_PICTURE_FROM_CAMERA = "data";
    private static final String PREFIX_PROFILE_PICTURE = "profile_picture_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        this.signupMsg = findViewById(R.id.signup_msg);
        this.signupMsgTitle = findViewById(R.id.signup_msg_title);
        this.signupMsgContent = findViewById(R.id.signup_msg_content);

        this.picture = findViewById(R.id.signup_patient_picture);
        this.takePictureFromCamera = findViewById(R.id.signup_take_picture_from_camera);
        this.selectPictureFromGallery = findViewById(R.id.signup_select_picture_from_gallery);
        this.picturePath = null;
        this.pictureURI = null;

        this.lastnameInput = findViewById(R.id.signup_lastname);
        this.firstnameInput = findViewById(R.id.signup_firstname);
        this.birthdateInput = findViewById(R.id.signup_birthdate);
        this.emailInput = findViewById(R.id.signup_email);
        this.insuranceNumberInput = findViewById(R.id.signup_insurance_number);
        this.passwordInput = findViewById(R.id.signup_pwd);
        this.confirmPasswordInput = findViewById(R.id.signup_confirm_pwd);

        this.street1Input = findViewById(R.id.signup_street1);
        this.street2Input = findViewById(R.id.signup_street2);
        this.cityInput = findViewById(R.id.signup_city);
        this.zipInput = findViewById(R.id.signup_zip);
        this.countryInput = findViewById(R.id.signup_country);

        this.patientProfileSection = findViewById(R.id.signup_patient_profile_section);
        this.patientAddressSection = findViewById(R.id.signup_patient_address_section);

        this.signupBtn = findViewById(R.id.signup_btn);

        this.loginLink = findViewById(R.id.signup_login_link);

        this.proAccountLink = findViewById(R.id.signup_pro_account_link);
        this.professionalSection = findViewById(R.id.signup_pro_account_section);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.takePictureFromCamera.setOnClickListener(this);
        this.selectPictureFromGallery.setOnClickListener(this);
        this.signupBtn.setOnClickListener(this);
        this.loginLink.setOnClickListener(this);
        this.proAccountLink.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(R.string.title_signup);
        this.SetSignupContext();
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
            case R.id.signup_take_picture_from_camera:
                this.TakePictureFromCamera();

                return;
            case R.id.signup_select_picture_from_gallery:
                this.SelectPictureFromGallery();

                return;
            case R.id.signup_btn:
                this.Signup();

                return;
            case R.id.signup_login_link:
                this.Login();

                return;
        }
    }

    /**
     * Set Sing-up context
     */
    private void SetSignupContext() {
        this.patientProfileSection.setVisibility(View.VISIBLE);
        this.patientAddressSection.setVisibility(View.VISIBLE);
        this.signupBtn.setVisibility(View.VISIBLE);
        this.professionalSection.setVisibility(View.VISIBLE);
        this.signupMsg.setVisibility(View.GONE);
        this.lastnameInput.setText("");
        this.firstnameInput.setText("");
        this.birthdateInput.setText("");
        this.emailInput.setText("");
        this.insuranceNumberInput.setText("");
        this.passwordInput.setText("");
        this.confirmPasswordInput.setText("");
        this.street1Input.setText("");
        this.street2Input.setText("");
        this.cityInput.setText("");
        this.zipInput.setText("");
        this.countryInput.setText("");
    }

    /**
     * Ask the user to load a picture
     */
    private void TakePictureFromCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getPackageManager()) == null) return;

        File pictureFile = this.CreatePictureFile();

        if (pictureFile == null) return;

        String authorities = BuildConfig.APPLICATION_ID + ".fileprovider";
        Uri pictureURI = FileProvider.getUriForFile(this, authorities, pictureFile);
        i.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
        startActivityForResult(i, RequestCode.TAKE_PICTURE_FROM_CAMERA);
    }

    /**
     * Ask the user to load a picture
     */
    private void SelectPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (i.resolveActivity(getPackageManager()) == null) return;

        startActivityForResult(i, RequestCode.SELECT_PICTURE_FROM_GALLERY);
    }

    /**
     * Create a file using the uploaded picture
     * @return The create file
     */
    private File CreatePictureFile() {
        File pictureFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pictureFileName = PREFIX_PROFILE_PICTURE + timeStamp + "_";

        /**
         * To store internally, use : getFilesDir()
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            pictureFile = File.createTempFile(pictureFileName, ".jpg", storageDir);
            this.picturePath = pictureFile.getAbsolutePath();
            this.pictureURI = ImageService.GetURIFromFile(pictureFile);
        }
        catch (Exception e) { e.printStackTrace(); }
        finally { return pictureFile; }
    }

    /**
     * Save to uploaded image to app folder
     */
    private boolean SaveBitmapToAppFolder(Intent data) {
        Bitmap pictureBmp = (new ImageService(this)).GetBmpFromURI(data.getData());
        File pictureFile = this.CreatePictureFile();

        if (pictureFile == null) return false;

        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            pictureBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.TAKE_PICTURE_FROM_CAMERA:
                    this.picture.setImageURI(this.pictureURI);

                    return;
                case RequestCode.SELECT_PICTURE_FROM_GALLERY:
                    if (!this.SaveBitmapToAppFolder(data)) return;

                    this.picture.setImageURI(this.pictureURI);

                    return;
            }
        }
    }

    /**
     * Execute signup process
     */
    private void Signup() {
        if (!this.AllFieldsCorrect()) {
            this.DisplayErrorMsg();

            return;
        }

        PatientDatabaseHelper patientDbHelper = new PatientDatabaseHelper(this.getApplicationContext());

        String lastLogin = DateTimeService.GetCurrentDateTime();

        String inputPwd = this.passwordInput.getText().toString().trim();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1(inputPwd + pwdSalt);

        String picture = this.picturePath;

        Address address = new Address(
                -1,
                this.street1Input.getText().toString().trim(),
                this.street2Input.getText().toString().trim(),
                this.cityInput.getText().toString().trim(),
                this.zipInput.getText().toString().trim(),
                this.countryInput.getText().toString().trim()
        );

        Patient patient = new Patient(
                -1,
                this.lastnameInput.getText().toString().trim(),
                this.firstnameInput.getText().toString().trim(),
                this.birthdateInput.getText().toString().trim(),
                this.emailInput.getText().toString().trim(),
                pwd,
                pwdSalt,
                this.insuranceNumberInput.getText().toString().trim(),
                address,
                lastLogin,
                picture
        );

        if (patientDbHelper.CreatePatient(patient)) {
            this.DisplaySuccessMsg();
            this.MakeToast(this.LOGIN);
            Intent i = new Intent();
            String key = this.getResources().getString(R.string.intent_logged_user);
            i.putExtra(key, this.loggedUser);
            setResult(RESULT_OK, i);
            finish();

            return;
        }

        this.DisplayErrorMsg();
    }

    /**
     * Check that all the fields are filled properly
     * @return If all the fields are correctly filled
     */
    private boolean AllFieldsCorrect() {
        boolean allFieldsFilled =
                        this.lastnameInput.getText().toString().trim().isEmpty()
                        || this.firstnameInput.getText().toString().trim().isEmpty()
                        || this.birthdateInput.getText().toString().trim().isEmpty()
                        || this.emailInput.getText().toString().trim().isEmpty()
                        || this.passwordInput.getText().toString().trim().isEmpty()
                        || this.confirmPasswordInput.getText().toString().trim().isEmpty()
                        || this.insuranceNumberInput.getText().toString().trim().isEmpty()
                        || this.street1Input.getText().toString().trim().isEmpty()
                        || this.street2Input.getText().toString().trim().isEmpty()
                        || this.cityInput.getText().toString().trim().isEmpty()
                        || this.zipInput.getText().toString().trim().isEmpty()
                        || this.countryInput.getText().toString().trim().isEmpty()
                        || this.picturePath.trim().isEmpty();

        // One of the fields is empty
        if (allFieldsFilled) return false;

        boolean bothPwdEqual = this.passwordInput.getText().toString().trim().
                equals(this.confirmPasswordInput.getText().toString().trim());

        // Both passwords do not correspond
        if (!bothPwdEqual) return false;

        boolean isBirthDateCorrectFormat = this.birthdateInput.getText().toString().trim().matches("\\d{4}-(01|02|03|04|05|06|07|08|09|10|11|12)-\\d{2}");

        // The provided date not in the correct format (ie. 1996-01-27)
        if (!isBirthDateCorrectFormat) return false;

        return true;
    }

    /**
     * Start Login activity
     */
    private void Login() {
        // Create the intent
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Display a success message to notify the registration succeeded
     */
    private void DisplaySuccessMsg() {
        this.signupMsg.setVisibility(View.VISIBLE);
        this.signupMsg.setBackgroundResource(R.color.signup_success_msg);
        this.signupMsgTitle.setText(this.getResources().getString(R.string.signup_success_msg_title));
        this.signupMsgContent.setText(this.getResources().getString(R.string.signup_success_msg_content));
        this.patientProfileSection.setVisibility(View.GONE);
        this.patientAddressSection.setVisibility(View.GONE);
        this.signupBtn.setVisibility(View.GONE);
        this.professionalSection.setVisibility(View.GONE);
    }

    /**
     * Display an error message to notify the registration failed
     */
    private void DisplayErrorMsg() {
        this.signupMsg.setVisibility(View.VISIBLE);
        this.signupMsg.setBackgroundResource(R.color.signup_error_msg);
        this.signupMsgTitle.setText(this.getResources().getString(R.string.signup_error_msg_title));
        this.signupMsgContent.setText(this.getResources().getString(R.string.signup_error_msg_content));
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
}
