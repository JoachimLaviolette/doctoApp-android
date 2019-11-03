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

public class MyProfileActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

    private LinearLayout myProfileMsg;
    private TextView myProfileMsgTitle;
    private TextView myProfileMsgContent;

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
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;

    private EditText street1Input;
    private EditText street2Input;
    private EditText cityInput;
    private EditText zipInput;
    private EditText countryInput;

    private LinearLayout patientProfileSection, patientAddressSection;

    private Button myProfileUpdateBtn;

    private static final String PREFIX_PROFILE_PICTURE = "profile_picture_";
    private static final String FILE_EXT = ".png";

    private static final int SCALE_AMOUNT_PICTURE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.myProfileMsg = findViewById(R.id.my_profile_msg);
        this.myProfileMsgTitle = findViewById(R.id.my_profile_msg_title);
        this.myProfileMsgContent = findViewById(R.id.my_profile_msg_content);

        this.picture = findViewById(R.id.my_profile_patient_picture);
        this.picturePath = this.loggedUser.getPicture();
        this.pictureURI = ImageService.GetURIFromPath(this.picturePath);

        this.takePictureFromCamera = findViewById(R.id.my_profile_take_picture_from_camera);
        this.selectPictureFromGallery = findViewById(R.id.my_profile_select_picture_from_gallery);
        this.picturePath = null;
        this.pictureURI = null;

        this.lastnameInput = findViewById(R.id.my_profile_lastname);
        this.firstnameInput = findViewById(R.id.my_profile_firstname);
        this.birthdateInput = findViewById(R.id.my_profile_birthdate);
        this.emailInput = findViewById(R.id.my_profile_email);
        this.insuranceNumberInput = findViewById(R.id.my_profile_insurance_number);
        this.oldPasswordInput = findViewById(R.id.my_profile_old_pwd);
        this.newPasswordInput = findViewById(R.id.my_profile_new_pwd);
        this.confirmPasswordInput = findViewById(R.id.my_profile_confirm_pwd);

        this.street1Input = findViewById(R.id.my_profile_street1);
        this.street2Input = findViewById(R.id.my_profile_street2);
        this.cityInput = findViewById(R.id.my_profile_city);
        this.zipInput = findViewById(R.id.my_profile_zip);
        this.countryInput = findViewById(R.id.my_profile_country);

        this.patientProfileSection = findViewById(R.id.my_profile_patient_profile_section);
        this.patientAddressSection = findViewById(R.id.my_profile_patient_address_section);

        this.myProfileUpdateBtn = findViewById(R.id.my_profile_update_btn);
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
        this.takePictureFromCamera.setOnClickListener(this);
        this.selectPictureFromGallery.setOnClickListener(this);
        this.myProfileUpdateBtn.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(getString(R.string.title_my_profile));
        this.SetMyProfileContext();
    }

    /**
     * Set MyProfile context
     */
    private void SetMyProfileContext() {
        this.patientProfileSection.setVisibility(View.VISIBLE);
        this.patientAddressSection.setVisibility(View.VISIBLE);
        this.myProfileUpdateBtn.setVisibility(View.VISIBLE);
        this.myProfileMsg.setVisibility(View.GONE);
        if (this.loggedUser.getPicture() != null) if (!this.loggedUser.getPicture().isEmpty()) {
            Uri uri = ImageService.GetURIFromPath(this.loggedUser.getPicture());
            if (uri != null) {
                this.picture.setImageURI(uri);
                this.picturePath = this.loggedUser.getPicture();
                this.pictureURI = uri;
            }
        }
        this.lastnameInput.setText(this.loggedUser.getLastname());
        this.firstnameInput.setText(this.loggedUser.getFirstname());
        this.birthdateInput.setText(((Patient) this.loggedUser).getBirthdate());
        this.emailInput.setText(this.loggedUser.getEmail());
        this.insuranceNumberInput.setText(((Patient) this.loggedUser).getInsuranceNumber());
        this.oldPasswordInput.setText("");
        this.newPasswordInput.setText("");
        this.confirmPasswordInput.setText("");
        this.street1Input.setText(this.loggedUser.GetStreet1());
        this.street2Input.setText(this.loggedUser.GetStreet2());
        this.cityInput.setText(this.loggedUser.GetCity());
        this.zipInput.setText(this.loggedUser.GetZip());
        this.countryInput.setText(this.loggedUser.GetCountry());
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_profile_take_picture_from_camera:
                this.TakePictureFromCamera();

                return;
            case R.id.my_profile_select_picture_from_gallery:
                this.SelectPictureFromGallery();

                return;
            case R.id.my_profile_update_btn:
                this.UpdateProfile();
        }
    }

    /**
     * Execute update profile process
     */
    private void UpdateProfile() {
        if (!this.AllFieldsCorrect()) {
            this.DisplayErrorMsg();

            return;
        }

        PatientDatabaseHelper patientDbHelper = new PatientDatabaseHelper(this.getApplicationContext());

        String lastLogin = DateTimeService.GetCurrentDateTime();

        String pwd = this.loggedUser.getPwd();
        String pwdSalt = this.loggedUser.getPwdSalt();

        if (!this.oldPasswordInput.getText().toString().trim().isEmpty()
            && !this.newPasswordInput.getText().toString().trim().isEmpty()
            && !this.confirmPasswordInput.getText().toString().trim().isEmpty()) {
            String inputPwd = this.newPasswordInput.getText().toString().trim();
            pwdSalt = EncryptionService.SALT();
            pwd = EncryptionService.SHA1(inputPwd + pwdSalt);
        }

        Address address = new Address(
                this.loggedUser.GetAddressId(),
                StringFormatterService.CapitalizeOnly(this.street1Input.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.street2Input.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.cityInput.getText().toString().trim()),
                this.zipInput.getText().toString().trim(),
                StringFormatterService.Capitalize(this.countryInput.getText().toString().trim())
        );

        Patient patient = new Patient(
                this.loggedUser.getId(),
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

        if (patientDbHelper.UpdatePatient(patient)) {
            try { if (this.picturePath != null) if (!this.picturePath.isEmpty()) if (!this.picturePath.equals(this.loggedUser.getPicture())) this.RemoveOldPictureFile(); }
            catch (Exception e) { e.printStackTrace(); }

            this.DisplaySuccessMsg();
            this.MakeToast();
            Intent i = new Intent();
            String key = this.getResources().getString(R.string.intent_logged_user);
            i.putExtra(key, this.loggedUser);
            setResult(RESULT_OK, i);
            // To refresh the activity
            //finish();
            //startActivity(getIntent());

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
                        || this.insuranceNumberInput.getText().toString().trim().isEmpty()
                        || this.street1Input.getText().toString().trim().isEmpty()
                        // || this.street2Input.getText().toString().trim().isEmpty() // Street 2 is not a required field
                        || this.cityInput.getText().toString().trim().isEmpty()
                        || this.zipInput.getText().toString().trim().isEmpty()
                        || this.countryInput.getText().toString().trim().isEmpty();
                        // || this.picturePath.trim().isEmpty(); // Picture is not a required field

        // One of the fields is empty
        if (isOneFieldEmpty) return false;

        // Check if a new password was provided
        if (!this.oldPasswordInput.getText().toString().trim().isEmpty()
                || !this.newPasswordInput.getText().toString().trim().isEmpty()
                || !this.confirmPasswordInput.getText().toString().trim().isEmpty()) {
            String sha1Pwd = EncryptionService.SHA1(this.oldPasswordInput.getText().toString().trim() + this.loggedUser.getPwdSalt());

            if (sha1Pwd == null) return false;

            // Check if the current password verification is ok
            boolean oldPwdEqual = sha1Pwd.equals(this.loggedUser.getPwd());

            // Check if both new password do correspond
            boolean bothNewPwdEqual = this.newPasswordInput.getText().toString().trim().
                    equals(this.confirmPasswordInput.getText().toString().trim());

            // Whether the current password verification is wrong or both new password do not correspond
            if (!(oldPwdEqual && bothNewPwdEqual)) return false;
        }

        // The provided date not in the correct format (ie. 1996-01-27)
        return this.birthdateInput.getText().toString().trim().matches("\\d{4}-(01|02|03|04|05|06|07|08|09|10|11|12)-\\d{2}");
    }

    /**
     * Display a success message to notify the update succeeded
     */
    private void DisplaySuccessMsg() {
        this.myProfileMsg.setVisibility(View.VISIBLE);
        this.myProfileMsg.setBackgroundResource(R.color.my_profile_success_msg);
        this.myProfileMsgTitle.setText(this.getResources().getString(R.string.my_profile_success_msg_title));
        this.myProfileMsgContent.setText(this.getResources().getString(R.string.my_profile_success_msg_content));
        this.patientProfileSection.setVisibility(View.VISIBLE);
        this.patientAddressSection.setVisibility(View.VISIBLE);
        this.myProfileUpdateBtn.setVisibility(View.VISIBLE);
    }

    /**
     * Display an error message to notify the update failed
     */
    private void DisplayErrorMsg() {
        this.myProfileMsg.setVisibility(View.VISIBLE);
        this.myProfileMsg.setBackgroundResource(R.color.my_profile_error_msg);
        this.myProfileMsgTitle.setText(this.getResources().getString(R.string.my_profile_error_msg_title));
        this.myProfileMsgContent.setText(this.getResources().getString(R.string.my_profile_error_msg_content));
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

        /**
         * To store internally, use : getFilesDir()
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            pictureFile = File.createTempFile(pictureFileName, FILE_EXT, storageDir);

            if (this.picturePath != null) if (!this.picturePath.isEmpty()) if (!this.picturePath.equals(this.loggedUser.getPicture())) this.RemoveCurrentPictureFile();

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
     * Remove the current picture file saved on the phone
     */
    private void RemoveOldPictureFile() throws IOException {
        File currentPictureFile = new File(this.loggedUser.getPicture());

        if (currentPictureFile.getPath().isEmpty()) throw new FileNotFoundException();
        if (! currentPictureFile.delete()) throw new IOException("Impossible to delete the old picture file.");
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
     * Show a toast after update action was performed
     */
    private void MakeToast() {
        Context context = getApplicationContext();
        CharSequence content = this.getResources().getString(R.string.my_profile_toast_update_content);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, content, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        try {
            if (this.picturePath != null) if (!this.picturePath.isEmpty()) if (!this.picturePath.equals(this.loggedUser.getPicture())) this.RemoveCurrentPictureFile();
            finish();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
