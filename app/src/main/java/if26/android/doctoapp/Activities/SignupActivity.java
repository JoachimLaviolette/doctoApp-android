package if26.android.doctoapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import if26.android.doctoapp.BuildConfig;
import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;
import if26.android.doctoapp.Models.Address;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.EncryptionService;
import if26.android.doctoapp.Services.ImageService;
import if26.android.doctoapp.Services.StringFormatterService;

public class SignupActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

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

    private static final String PREFIX_PROFILE_PICTURE = "profile_picture_";
    private static final String FILE_EXT = ".png";

    private static final int SCALE_AMOUNT_PICTURE = 400;

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
        this.loggedUser = bundle.containsKey(key) ?
                        bundle.getSerializable(key) instanceof Patient ?
                            (Patient) bundle.getSerializable(key) :
                                (Doctor) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_take_picture_from_camera:
                this.CheckPermission("CAMERA");

                return;
            case R.id.signup_select_picture_from_gallery:
                this.CheckPermission("READ_EXTERNAL_STORAGE");

                return;
            case R.id.signup_btn:
                this.Signup();

                return;
            case R.id.signup_login_link:
                this.Login();

                return;
            case R.id.signup_pro_account_link:
                this.SignupPro();
        }
    }

    /**
     * Set Sign-up context
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
     * Check if we have the permission to access the camera and the gallery
     */
    private void CheckPermission(String PermissionAction){
        switch (PermissionAction) {
            case "READ_EXTERNAL_STORAGE":
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // Permission has already been granted
                    this.SelectPictureFromGallery();
                }

                break;
            case "CAMERA":
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    //Request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCode.PERMISSIONS_REQUEST_CAMERA);
                } else {
                    // Permission has already been granted
                    this.TakePictureFromCamera();
                }

                break;
        }
    }

    /**
     * Do an action depending on the result of the permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RequestCode.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.TakePictureFromCamera();
                }
                break;
            case RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.SelectPictureFromGallery();
                }
                break;
        }
    }

    /**
     * Ask the user to take a picture from the camera
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
     * @return The created file
     */
    private File CreatePictureFile() {
        File pictureFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pictureFileName = PREFIX_PROFILE_PICTURE + timeStamp + "_";

        /*
         * To store internally, use : getFilesDir()
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            pictureFile = File.createTempFile(pictureFileName, FILE_EXT, storageDir);

            if (this.picturePath != null) if (!this.picturePath.isEmpty()) this.RemoveCurrentPictureFile();

            this.picturePath = pictureFile.getAbsolutePath();
            this.pictureURI = ImageService.GetURIFromFile(pictureFile);
        }
        catch (Exception e) { e.printStackTrace(); }

        return pictureFile;
    }

    /**
     * Remove the current picture file saved on the phone
     */
    private void RemoveCurrentPictureFile() throws IOException {
        File currentPictureFile = new File(this.picturePath);

        if (currentPictureFile.getPath().isEmpty()) throw new FileNotFoundException();
        if (! currentPictureFile.delete()) throw new IOException("Impossible to delete the picture file.");
    }

    /**
     * Save to uploaded picture to app folder
     */
    private boolean SavePictureBitmapToAppFolder(Intent data) {
        Bitmap pictureBmp = (new ImageService(this)).GetBmpFromURI(data.getData());
        int scaleFactor = Math.min(pictureBmp.getHeight() / SCALE_AMOUNT_PICTURE, pictureBmp.getWidth() / SCALE_AMOUNT_PICTURE);

        pictureBmp = Bitmap.createScaledBitmap(
                pictureBmp,
                pictureBmp.getWidth() / scaleFactor,
                pictureBmp.getHeight() / scaleFactor,
                true
        );

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
                    if (!this.SavePictureBitmapToAppFolder(data)) return;

                    this.picture.setImageURI(this.pictureURI);
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

        Address address = new Address(
                -1,
                StringFormatterService.CapitalizeOnly(this.street1Input.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.street2Input.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.cityInput.getText().toString().trim()),
                this.zipInput.getText().toString().trim(),
                StringFormatterService.Capitalize(this.countryInput.getText().toString().trim())
        );

        Patient patient = new Patient(
                -1,
                StringFormatterService.CapitalizeOnly(this.lastnameInput.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.firstnameInput.getText().toString().trim()),
                this.birthdateInput.getText().toString().trim(),
                this.emailInput.getText().toString().trim(),
                pwd,
                pwdSalt,
                this.insuranceNumberInput.getText().toString().trim(),
                address,
                lastLogin,
                this.picturePath == null ? "" : this.picturePath
        );

        if (patientDbHelper.CreatePatient(patient)) {
            this.DisplaySuccessMsg();
            this.MakeToast();
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
        boolean isOneFieldEmpty =
                        this.lastnameInput.getText().toString().trim().isEmpty()
                        || this.firstnameInput.getText().toString().trim().isEmpty()
                        || this.birthdateInput.getText().toString().trim().isEmpty()
                        || this.emailInput.getText().toString().trim().isEmpty()
                        || this.passwordInput.getText().toString().trim().isEmpty()
                        || this.confirmPasswordInput.getText().toString().trim().isEmpty()
                        || this.insuranceNumberInput.getText().toString().trim().isEmpty()
                        || this.street1Input.getText().toString().trim().isEmpty()
                        // || this.street2Input.getText().toString().trim().isEmpty() // Street 2 is not a required field
                        || this.cityInput.getText().toString().trim().isEmpty()
                        || this.zipInput.getText().toString().trim().isEmpty()
                        || this.countryInput.getText().toString().trim().isEmpty();
                        // || this.picturePath.trim().isEmpty(); // Picture is not a required field

        // One of the fields is empty
        if (isOneFieldEmpty) return false;

        boolean bothPwdEqual = this.passwordInput.getText().toString().trim().
                equals(this.confirmPasswordInput.getText().toString().trim());

        // Both passwords do not correspond
        if (!bothPwdEqual) return false;

        boolean isBirthDateCorrectFormat = this.birthdateInput.getText().toString().trim().matches("\\d{4}-(01|02|03|04|05|06|07|08|09|10|11|12)-\\d{2}");

        // The provided date not in the correct format (ie. 1996-01-27)
        return isBirthDateCorrectFormat;
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
     * Start SignupPro activity
     */
    private void SignupPro() {
        // Create the intent
        Intent i = new Intent(SignupActivity.this, SignupProActivity.class);

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
     * Show a toast after SIGN UP action
     */
    private void MakeToast() {
        Context context = getApplicationContext();
        CharSequence content = this.getResources().getString(R.string.signup_toast_signup_content);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, content, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        try {
            if (this.picturePath != null) if (!this.picturePath.isEmpty()) this.RemoveCurrentPictureFile();
            finish();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
