package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.jgabrielfreitas.core.BlurImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import if26.android.doctoapp.BuildConfig;
import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.DatabaseHelpers.DoctorDatabaseHelper;
import if26.android.doctoapp.Models.Address;
import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Education;
import if26.android.doctoapp.Models.Experience;
import if26.android.doctoapp.Models.Language;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.PaymentOption;
import if26.android.doctoapp.Models.Reason;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.AvailabilityService;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.EducationService;
import if26.android.doctoapp.Services.EncryptionService;
import if26.android.doctoapp.Services.ExperienceService;
import if26.android.doctoapp.Services.ImageService;
import if26.android.doctoapp.Services.ReasonService;
import if26.android.doctoapp.Services.StringFormatterService;

public class SignupProActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

    private LinearLayout signupMsg;
    private TextView signupMsgTitle;
    private TextView signupMsgContent;

    private CircleImageView picture;
    private BlurImageView header;

    private Button takePictureFromCamera;
    private Button selectPictureFromGallery;
    private String picturePath;
    private Uri pictureURI;

    private Button takeHeaderFromCamera;
    private Button selectHeaderFromGallery;
    private String headerPath;
    private Uri headerURI;

    private EditText lastnameInput;
    private EditText firstnameInput;
    private EditText specialityInput;
    private EditText emailInput;
    private EditText descriptionInput;
    private EditText contactNumberInput;
    private CheckBox isUnderAgreement;
    private CheckBox isHealthInsuranceCard;
    private CheckBox isThirdPartyPayment;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private EditText street1Input;
    private EditText street2Input;
    private EditText cityInput;
    private EditText zipInput;
    private EditText countryInput;

    private LinearLayout
            doctorProfileSection,
            doctorAddressSection;

    private Button
            addAvailabilityBtn,
            addReasonBtn,
            addExperience,
            addTrainingBtn,
            addLanguageBtn,
            addPaymentOptionBtn;

    private Spinner
            daysList,
            languagesList,
            paymentOptionsList;

    private TextView
            availabilityTime,
            reasonDescription,
            experienceYear,
            experienceDescription,
            trainingYear,
            trainingDescription;

    private List<Availability> availabilities;
    private List<Reason> reasons;
    private List<Experience> experiences;
    private List<Education> trainings;
    private Set<Language> languages;
    private Set<PaymentOption> paymentOptions;

    private TextView
            availabilityErrorMsg,
            reasonErrorMsg,
            experienceErrorMsg,
            trainingErrorMsg,
            languageErrorMsg,
            paymentOptionErrorMsg;

    private Button signupBtn;

    private TextView loginLink;

    private TextView privateAccountLink;
    private LinearLayout privateSection;

    private static final String PREFIX_PROFILE_PICTURE = "profile_picture_";
    private static final String PREFIX_PROFILE_HEADER = "profile_header_";

    private static final int ADD_AVAILABILITY = 0;
    private static final int ADD_REASON = 1;
    private static final int ADD_EXPERIENCE = 2;
    private static final int ADD_TRAINING = 3;
    private static final int ADD_LANGUAGE = 4;
    private static final int ADD_PAYMENT_OPTION = 5;
    private static final int SIGNUP = 6;

    private static final int BLUR_AMOUNT = 2;
    private static final int SCALE_AMOUNT_PICTURE = 400;
    private static final int SCALE_AMOUNT_HEADER = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_pro);

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

        this.signupMsg = findViewById(R.id.signup_pro_msg);
        this.signupMsgTitle = findViewById(R.id.signup_pro_msg_title);
        this.signupMsgContent = findViewById(R.id.signup_pro_msg_content);

        this.picture = findViewById(R.id.signup_pro_doctor_picture);
        this.header = findViewById(R.id.signup_pro_doctor_header);

        this.takePictureFromCamera = findViewById(R.id.signup_pro_take_picture_from_camera);
        this.selectPictureFromGallery = findViewById(R.id.signup_pro_select_picture_from_gallery);
        this.picturePath = null;
        this.pictureURI = null;

        this.takeHeaderFromCamera = findViewById(R.id.signup_pro_take_header_from_camera);
        this.selectHeaderFromGallery = findViewById(R.id.signup_pro_select_header_from_gallery);
        this.headerPath = null;
        this.headerURI = null;

        this.lastnameInput = findViewById(R.id.signup_pro_lastname);
        this.firstnameInput = findViewById(R.id.signup_pro_firstname);
        this.specialityInput = findViewById(R.id.signup_pro_speciality);
        this.emailInput = findViewById(R.id.signup_pro_email);
        this.descriptionInput = findViewById(R.id.signup_pro_description);
        this.contactNumberInput = findViewById(R.id.signup_pro_contact_number);
        this.isUnderAgreement = findViewById(R.id.signup_pro_is_under_agreement);
        this.isHealthInsuranceCard = findViewById(R.id.signup_pro_is_health_insurance_card);
        this.isThirdPartyPayment = findViewById(R.id.signup_pro_is_third_party_payment);
        this.passwordInput = findViewById(R.id.signup_pro_pwd);
        this.confirmPasswordInput = findViewById(R.id.signup_pro_confirm_pwd);

        this.street1Input = findViewById(R.id.signup_pro_street1);
        this.street2Input = findViewById(R.id.signup_pro_street2);
        this.cityInput = findViewById(R.id.signup_pro_city);
        this.zipInput = findViewById(R.id.signup_pro_zip);
        this.countryInput = findViewById(R.id.signup_pro_country);

        this.doctorProfileSection = findViewById(R.id.signup_pro_doctor_profile_section);
        this.doctorAddressSection = findViewById(R.id.signup_pro_doctor_address_section);

        this.addAvailabilityBtn = findViewById(R.id.signup_pro_add_availability_btn);
        this.addReasonBtn = findViewById(R.id.signup_pro_add_reason_btn);
        this.addExperience = findViewById(R.id.signup_pro_add_experience_btn);
        this.addTrainingBtn = findViewById(R.id.signup_pro_add_training_btn);
        this.addLanguageBtn = findViewById(R.id.signup_pro_add_language_btn);
        this.addPaymentOptionBtn = findViewById(R.id.signup_pro_add_payment_option_btn);

        this.daysList = findViewById(R.id.signup_pro_days_list);
        this.availabilityTime = findViewById(R.id.signup_pro_availability_time_input);
        this.reasonDescription = findViewById(R.id.signup_pro_reason_description_input);
        this.experienceYear = findViewById(R.id.signup_pro_experience_year_input);
        this.experienceDescription = findViewById(R.id.signup_pro_experience_description_input);
        this.trainingYear = findViewById(R.id.signup_pro_training_year_input);
        this.trainingDescription = findViewById(R.id.signup_pro_training_description_input);
        this.languagesList = findViewById(R.id.signup_pro_languages_list);
        this.paymentOptionsList = findViewById(R.id.signup_pro_payment_options_list);

        this.availabilities = new LinkedList<>();
        this.reasons = new LinkedList<>();
        this.experiences = new LinkedList<>();
        this.trainings = new LinkedList<>();
        this.languages = new LinkedHashSet<>();
        this.paymentOptions = new LinkedHashSet<>();

        this.availabilityErrorMsg = findViewById(R.id.signup_pro_availability_error);
        this.reasonErrorMsg = findViewById(R.id.signup_pro_reason_error);
        this.experienceErrorMsg = findViewById(R.id.signup_pro_experience_error);
        this.trainingErrorMsg = findViewById(R.id.signup_pro_training_error);
        this.languageErrorMsg = findViewById(R.id.signup_pro_language_error);
        this.paymentOptionErrorMsg = findViewById(R.id.signup_pro_payment_option_error);

        this.signupBtn = findViewById(R.id.signup_pro_btn);

        this.loginLink = findViewById(R.id.signup_pro_login_link);

        this.privateAccountLink = findViewById(R.id.signup_pro_private_account_link);
        this.privateSection = findViewById(R.id.signup_pro_private_section);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.takePictureFromCamera.setOnClickListener(this);
        this.selectPictureFromGallery.setOnClickListener(this);
        this.takeHeaderFromCamera.setOnClickListener(this);
        this.selectHeaderFromGallery.setOnClickListener(this);
        this.addAvailabilityBtn.setOnClickListener(this);
        this.addReasonBtn.setOnClickListener(this);
        this.addExperience.setOnClickListener(this);
        this.addTrainingBtn.setOnClickListener(this);
        this.addLanguageBtn.setOnClickListener(this);
        this.addPaymentOptionBtn.setOnClickListener(this);
        this.signupBtn.setOnClickListener(this);
        this.loginLink.setOnClickListener(this);
        this.privateAccountLink.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(R.string.title_signup);
        this.SetSignupContext();
        this.SetLanguagesList();
        this.SetPaymentOptionsList();
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
     * Set the languages list options
     */
    private void SetLanguagesList() {
        List<String> languages = new LinkedList<>();
        for (Language l : Language.values()) languages.add(l.toString());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                languages
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.languagesList.setAdapter(dataAdapter);
    }

    /**
     * Set the payment options list options
     */
    private void SetPaymentOptionsList() {
        List<String> paymentOptions = new LinkedList<>();
        for (PaymentOption po : PaymentOption.values()) paymentOptions.add(po.toString());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                paymentOptions
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.paymentOptionsList.setAdapter(dataAdapter);
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_pro_take_picture_from_camera:
                this.TakePictureFromCamera();

                return;
            case R.id.signup_pro_select_picture_from_gallery:
                this.SelectPictureFromGallery();

                return;
            case R.id.signup_pro_take_header_from_camera:
                this.TakeHeaderFromCamera();

                return;
            case R.id.signup_pro_select_header_from_gallery:
                this.SelectHeaderFromGallery();

                return;
            case R.id.signup_pro_add_availability_btn:
                this.AddAvailability();

                return;
            case R.id.signup_pro_add_reason_btn:
                this.AddReason();

                return;
            case R.id.signup_pro_add_experience_btn:
                this.AddExperience();

                return;
            case R.id.signup_pro_add_training_btn:
                this.AddTraining();

                return;
            case R.id.signup_pro_add_language_btn:
                this.AddLanguage();

                return;
            case R.id.signup_pro_add_payment_option_btn:
                this.AddPaymentOption();

                return;
            case R.id.signup_pro_btn:
                this.SignupPro();

                return;
            case R.id.signup_pro_login_link:
                this.Login();

                return;
            case R.id.signup_pro_private_account_link:
                this.Signup();

                return;
        }
    }

    /**
     * Set Sign-up context
     */
    private void SetSignupContext() {
        this.header.setBlur(BLUR_AMOUNT);
        this.doctorProfileSection.setVisibility(View.VISIBLE);
        this.doctorAddressSection.setVisibility(View.VISIBLE);
        this.signupBtn.setVisibility(View.VISIBLE);
        this.privateSection.setVisibility(View.VISIBLE);
        this.signupMsg.setVisibility(View.GONE);
        this.lastnameInput.setText("");
        this.firstnameInput.setText("");
        this.specialityInput.setText("");
        this.emailInput.setText("");
        this.descriptionInput.setText("");
        this.contactNumberInput.setText("");
        this.passwordInput.setText("");
        this.confirmPasswordInput.setText("");
        this.street1Input.setText("");
        this.street2Input.setText("");
        this.cityInput.setText("");
        this.zipInput.setText("");
        this.countryInput.setText("");
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
     * Ask the user to take a picture from the camera
     */
    private void TakeHeaderFromCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getPackageManager()) == null) return;

        File headerFile = this.CreateHeaderFile();

        if (headerFile == null) return;

        String authorities = BuildConfig.APPLICATION_ID + ".fileprovider";
        Uri headerURI = FileProvider.getUriForFile(this, authorities, headerFile);
        i.putExtra(MediaStore.EXTRA_OUTPUT, headerURI);
        startActivityForResult(i, RequestCode.TAKE_HEADER_FROM_CAMERA);
    }

    /**
     * Ask the user to load a header
     */
    private void SelectHeaderFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (i.resolveActivity(getPackageManager()) == null) return;

        startActivityForResult(i, RequestCode.SELECT_HEADER_FROM_GALLERY);
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
            pictureFile = File.createTempFile(pictureFileName, ".jpg", storageDir);
            this.picturePath = pictureFile.getAbsolutePath();
            this.pictureURI = ImageService.GetURIFromFile(pictureFile);
        }
        catch (Exception e) { e.printStackTrace(); }
        finally { return pictureFile; }
    }

    /**
     * Create a file using the uploaded header
     * @return The created file
     */
    private File CreateHeaderFile() {
        File headerFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String headerFileName = PREFIX_PROFILE_HEADER + timeStamp + "_";

        /**
         * To store internally, use : getFilesDir()
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            headerFile = File.createTempFile(headerFileName, ".jpg", storageDir);
            this.headerPath = headerFile.getAbsolutePath();
            this.headerURI = ImageService.GetURIFromFile(headerFile);
        }
        catch (Exception e) { e.printStackTrace(); }
        finally { return headerFile; }
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

    /**
     * Save to uploaded header to app folder
     */
    private boolean SaveHeaderBitmapToAppFolder(Intent data) {
        Bitmap headerBmp = (new ImageService(this)).GetBmpFromURI(data.getData());
        int scaleFactor = Math.min(headerBmp.getHeight() / SCALE_AMOUNT_HEADER, headerBmp.getWidth() / SCALE_AMOUNT_HEADER);

        headerBmp = Bitmap.createScaledBitmap(
                headerBmp,
                headerBmp.getWidth() / scaleFactor,
                headerBmp.getHeight() / scaleFactor,
                true
        );

        File headerFile = this.CreateHeaderFile();

        if (headerFile == null) return false;

        try {
            FileOutputStream out = new FileOutputStream(headerFile);
            headerBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
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

                    return;
                case RequestCode.TAKE_HEADER_FROM_CAMERA:
                    this.header.setImageURI(this.headerURI);

                    return;
                case RequestCode.SELECT_HEADER_FROM_GALLERY:
                    if (!this.SaveHeaderBitmapToAppFolder(data)) return;

                    this.header.setImageURI(this.headerURI);

                    return;
            }
        }
    }

    /**
     * Add the availability chosen by the doctor
     */
    private void AddAvailability() {
        if (!this.CheckAvailabilityFields()) {
            this.availabilityErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        String day = this.daysList.getSelectedItem().toString().trim();
        String time = this.availabilityTime.getText().toString().trim();

        // Create a new Availability object
        Availability a = new Availability(null, day, time);

        // If already added
        if (this.availabilities.contains(a)) {
            // Reference comparison is not enough to say if the list actually contains the same object
            // So we need to use a personalize method using the appropriate service
            if (AvailabilityService.Contains(this.availabilities, a)) {
                this.availabilityErrorMsg.setVisibility(View.VISIBLE);

                return;
            }
        }

        this.availabilityErrorMsg.setVisibility(View.GONE);

        // Add it to the list of availabilities
        this.availabilities.add(a);

        // Display a toast msg
        this.MakeToast(ADD_AVAILABILITY);
    }

    /**
     * Check if the availability fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckAvailabilityFields() {
        return this.availabilityTime.getText().toString().trim().matches("(0|1)(0|1|2|3|4|5|6|7|8|9):(0|1|2|3|4|5)(0|1|2|3|4|5|6|7|8|9)");
    }

    /**
     * Add the reason chosen by the doctor
     */
    private void AddReason() {
        if (!this.CheckReasonFields()) {
            this.availabilityErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        // Create a new Reason object
        Reason r = new Reason(-1, null, this.reasonDescription.getText().toString().trim());

        // If already added
        if (this.reasons.contains(r)) {
            // Reference comparison is not enough to say if the list actually contains the same object
            // So we need to use a personalize method using the appropriate service
            if (ReasonService.Contains(this.reasons, r)) {
                this.reasonErrorMsg.setVisibility(View.VISIBLE);

                return;
            }
        }

        this.reasonErrorMsg.setVisibility(View.GONE);

        // Add it to the list of reasons
        this.reasons.add(r);

        // Display a toast msg
        this.MakeToast(ADD_REASON);
    }

    /**
     * Check if the reason fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckReasonFields() {
        return !this.reasonDescription.getText().toString().trim().isEmpty();
    }

    /**
     * Add the experience chosen by the doctor
     */
    private void AddExperience() {
        if (!this.CheckExperienceFields()) {
            this.experienceErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        String year = this.experienceYear.getText().toString().trim();
        String desc = this.experienceDescription.getText().toString().trim();

        // Create a new Experience object
        Experience e = new Experience(null, year, desc);

        // If already added
        if (this.experiences.contains(e)) {
            // Reference comparison is not enough to say if the list actually contains the same object
            // So we need to use a personalize method using the appropriate service
            if (ExperienceService.Contains(this.experiences, e)) {
                this.experienceErrorMsg.setVisibility(View.VISIBLE);

                return;
            }
        }

        this.experienceErrorMsg.setVisibility(View.GONE);

        // Add it to the list of experiences
        this.experiences.add(e);

        // Display a toast msg
        this.MakeToast(ADD_EXPERIENCE);
    }

    /**
     * Check if the experience fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckExperienceFields() {
        return this.experienceYear.getText().toString().trim().matches("\\d{4}");
    }

    /**
     * Add the training chosen by the doctor
     */
    private void AddTraining() {
        if (!this.CheckTrainingFields()) {
            this.trainingErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        String year = this.trainingYear.getText().toString().trim();
        String desc = this.trainingDescription.getText().toString().trim();

        // Create a new Education object
        Education e = new Education(null, year, desc);

        // If already added
        if (this.trainings.contains(e)) {
            // Reference comparison is not enough to say if the list actually contains the same object
            // So we need to use a personalize method using the appropriate service
            if (EducationService.Contains(this.trainings, e)) {
                this.trainingErrorMsg.setVisibility(View.VISIBLE);

                return;
            }
        }

        this.trainingErrorMsg.setVisibility(View.GONE);

        // Add it to the list of trainings
        this.trainings.add(e);

        // Display a toast msg
        this.MakeToast(ADD_TRAINING);
    }

    /**
     * Check if the training fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckTrainingFields() {
        return this.trainingYear.getText().toString().trim().matches("\\d{4}");
    }

    /**
     * Add the language chosen by the doctor
     */
    private void AddLanguage() {
        // Create a new Language object
        Language l = Language.valueOf(this.languagesList.getSelectedItem().toString().trim());

        // If already added
        if (this.languages.contains(l)) {
            this.languageErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        this.languageErrorMsg.setVisibility(View.GONE);

        // Add it to the list of languages
        this.languages.add(l);

        // Display a toast msg
        this.MakeToast(ADD_LANGUAGE);
    }

    /**
     * Add the payment option chosen by the doctor
     */
    private void AddPaymentOption() {
        // Create a new PaymentOption object
        PaymentOption po = PaymentOption.valueOf(this.paymentOptionsList.getSelectedItem().toString().trim());

        // If already added
        if (this.paymentOptions.contains(po)) {
            this.paymentOptionErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        this.paymentOptionErrorMsg.setVisibility(View.GONE);

        // Add it to the list of payment options
        this.paymentOptions.add(po);

        // Display a toast msg
        this.MakeToast(ADD_PAYMENT_OPTION);
    }

    /**
     * Execute signup process
     */
    private void SignupPro() {
        if (!this.AllFieldsCorrect()) {
            this.DisplayErrorMsg();

            return;
        }

        DoctorDatabaseHelper doctorDbHelper = new DoctorDatabaseHelper(this.getApplicationContext());

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

        Doctor doctor = new Doctor(
                -1,
                StringFormatterService.CapitalizeOnly(this.lastnameInput.getText().toString().trim()),
                StringFormatterService.CapitalizeOnly(this.firstnameInput.getText().toString().trim()),
                StringFormatterService.Capitalize(this.specialityInput.getText().toString().trim()),
                this.emailInput.getText().toString().trim().toLowerCase(),
                this.descriptionInput.getText().toString().trim(),
                this.contactNumberInput.getText().toString().trim(),
                pwd,
                pwdSalt,
                this.isUnderAgreement.isChecked(),
                this.isHealthInsuranceCard.isChecked(),
                this.isThirdPartyPayment.isChecked(),
                address,
                lastLogin,
                this.picturePath == null ? "" : this.picturePath,
                this.headerPath == null ? "" : this.headerPath,
                this.availabilities,
                this.languages,
                this.paymentOptions,
                this.reasons,
                this.trainings,
                this.experiences

        );

        if (doctorDbHelper.CreateDoctor(doctor)) {
            this.DisplaySuccessMsg();
            this.MakeToast(SIGNUP);
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
                        || this.specialityInput.getText().toString().trim().isEmpty()
                        || this.emailInput.getText().toString().trim().isEmpty()
                        || this.passwordInput.getText().toString().trim().isEmpty()
                        || this.confirmPasswordInput.getText().toString().trim().isEmpty()
                        // || this.descriptionInput.getText().toString().trim().isEmpty() // Description is not a required field
                        || this.contactNumberInput.getText().toString().trim().isEmpty()
                        || this.street1Input.getText().toString().trim().isEmpty()
                        // || this.street2Input.getText().toString().trim().isEmpty() // Street 2 is not a required fields
                        || this.cityInput.getText().toString().trim().isEmpty()
                        || this.zipInput.getText().toString().trim().isEmpty()
                        || this.countryInput.getText().toString().trim().isEmpty();
                        // || this.picturePath.trim().isEmpty() // Picture is not a required field
                        // || this.headerPath.trim().isEmpty(); // Header is not a required field

        // One of the fields is empty
        if (allFieldsFilled) return false;

        boolean bothPwdEqual = this.passwordInput.getText().toString().trim().
                equals(this.confirmPasswordInput.getText().toString().trim());

        // Both passwords do not correspond
        if (!bothPwdEqual) return false;

        return true;
    }

    /**
     * Start Signup activity
     */
    private void Signup() {
        // Create the intent
        Intent i = new Intent(SignupProActivity.this, SignupActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start Login activity
     */
    private void Login() {
        // Create the intent
        Intent i = new Intent(SignupProActivity.this, LoginActivity.class);

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
        this.signupMsg.setBackgroundResource(R.color.signup_pro_success_msg);
        this.signupMsgTitle.setText(this.getResources().getString(R.string.signup_pro_success_msg_title));
        this.signupMsgContent.setText(this.getResources().getString(R.string.signup_pro_success_msg_content));
        this.doctorProfileSection.setVisibility(View.GONE);
        this.doctorAddressSection.setVisibility(View.GONE);
        this.signupBtn.setVisibility(View.GONE);
        this.privateSection.setVisibility(View.GONE);
    }

    /**
     * Display an error message to notify the registration failed
     */
    private void DisplayErrorMsg() {
        this.signupMsg.setVisibility(View.VISIBLE);
        this.signupMsg.setBackgroundResource(R.color.signup_pro_error_msg);
        this.signupMsgTitle.setText(this.getResources().getString(R.string.signup_pro_error_msg_title));
        this.signupMsgContent.setText(this.getResources().getString(R.string.signup_pro_error_msg_content));
    }

    /**
     * Show a toast after one action
     * @param action The action performed
     */
    private void MakeToast(int action) {
        Context context = getApplicationContext();
        CharSequence content;
        Toast toast;
        int duration = Toast.LENGTH_LONG;

        switch (action) {
            case SIGNUP:
                content = this.getResources().getString(R.string.signup_toast_signup_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_AVAILABILITY:
                content = this.getResources().getString(R.string.signup_toast_availability_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_REASON:
                content = this.getResources().getString(R.string.signup_toast_reason_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_EXPERIENCE:
                content = this.getResources().getString(R.string.signup_toast_experience_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_TRAINING:
                content = this.getResources().getString(R.string.signup_toast_training_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_LANGUAGE:
                content = this.getResources().getString(R.string.signup_toast_language_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_PAYMENT_OPTION:
                content = this.getResources().getString(R.string.signup_toast_payment_option_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
        }
    }
}
