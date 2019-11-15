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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.jgabrielfreitas.core.BlurImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class MyProfileProActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

    private LinearLayout myProfileProMsg;
    private TextView myProfileProMsgTitle;
    private TextView myProfileProMsgContent;

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
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;

    private EditText street1Input;
    private EditText street2Input;
    private EditText cityInput;
    private EditText zipInput;
    private EditText countryInput;

    private LinearLayout
            doctorProfileSection,
            doctorAddressSection;

    private LinearLayout
            availabilityMyProfileProList,
            reasonMyProfileProList,
            experienceMyProfileProList,
            educationMyProfileProList,
            paymentOptionMyProfileProList,
            languageMyProfileProList;

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
            experienceDegree,
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

    private Button myProfileProUpdateBtn;

    private static final String PREFIX_PROFILE_PICTURE = "profile_picture_";
    private static final String PREFIX_PROFILE_HEADER = "profile_header_";
    private static final String FILE_EXT = ".png";

    private static final int ADD_AVAILABILITY = 0;
    private static final int ADD_REASON = 1;
    private static final int ADD_EXPERIENCE = 2;
    private static final int ADD_TRAINING = 3;
    private static final int ADD_LANGUAGE = 4;
    private static final int ADD_PAYMENT_OPTION = 5;

    private static final int REMOVE_AVAILABILITY = 6;
    private static final int REMOVE_REASON = 7;
    private static final int REMOVE_EXPERIENCE = 8;
    private static final int REMOVE_TRAINING = 9;
    private static final int REMOVE_LANGUAGE = 10;
    private static final int REMOVE_PAYMENT_OPTION = 11;

    private static final int UPDATE = 12;

    private static final int BLUR_AMOUNT = 2;
    private static final int SCALE_AMOUNT_PICTURE = 400;
    private static final int SCALE_AMOUNT_HEADER = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_pro);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.myProfileProMsg = findViewById(R.id.my_profile_pro_msg);
        this.myProfileProMsgTitle = findViewById(R.id.my_profile_pro_msg_title);
        this.myProfileProMsgContent = findViewById(R.id.my_profile_pro_msg_content);

        this.picture = findViewById(R.id.my_profile_pro_doctor_picture);
        this.header = findViewById(R.id.my_profile_pro_doctor_header);

        this.takePictureFromCamera = findViewById(R.id.my_profile_pro_take_picture_from_camera);
        this.selectPictureFromGallery = findViewById(R.id.my_profile_pro_select_picture_from_gallery);
        this.picturePath = this.loggedUser.getPicture();
        this.pictureURI = ImageService.GetURIFromPath(this.picturePath);

        this.takeHeaderFromCamera = findViewById(R.id.my_profile_pro_take_header_from_camera);
        this.selectHeaderFromGallery = findViewById(R.id.my_profile_pro_select_header_from_gallery);
        this.headerPath = ((Doctor) this.loggedUser).getHeader();
        this.headerURI = ImageService.GetURIFromPath(this.headerPath);

        this.lastnameInput = findViewById(R.id.my_profile_pro_lastname);
        this.firstnameInput = findViewById(R.id.my_profile_pro_firstname);
        this.specialityInput = findViewById(R.id.my_profile_pro_speciality);
        this.emailInput = findViewById(R.id.my_profile_pro_email);
        this.descriptionInput = findViewById(R.id.my_profile_pro_description);
        this.contactNumberInput = findViewById(R.id.my_profile_pro_contact_number);
        this.isUnderAgreement = findViewById(R.id.my_profile_pro_is_under_agreement);
        this.isHealthInsuranceCard = findViewById(R.id.my_profile_pro_is_health_insurance_card);
        this.isThirdPartyPayment = findViewById(R.id.my_profile_pro_is_third_party_payment);
        this.oldPasswordInput = findViewById(R.id.my_profile_pro_old_pwd);
        this.newPasswordInput = findViewById(R.id.my_profile_pro_new_pwd);
        this.confirmPasswordInput = findViewById(R.id.my_profile_pro_confirm_pwd);

        this.street1Input = findViewById(R.id.my_profile_pro_street1);
        this.street2Input = findViewById(R.id.my_profile_pro_street2);
        this.cityInput = findViewById(R.id.my_profile_pro_city);
        this.zipInput = findViewById(R.id.my_profile_pro_zip);
        this.countryInput = findViewById(R.id.my_profile_pro_country);

        this.doctorProfileSection = findViewById(R.id.my_profile_pro_doctor_profile_section);
        this.doctorAddressSection = findViewById(R.id.my_profile_pro_doctor_address_section);

        this.availabilityMyProfileProList = findViewById(R.id.availability_my_profile_pro_list);
        this.reasonMyProfileProList = findViewById(R.id.reason_my_profile_pro_list);
        this.experienceMyProfileProList = findViewById(R.id.experience_my_profile_pro_list);
        this.educationMyProfileProList = findViewById(R.id.education_my_profile_pro_list);
        this.languageMyProfileProList = findViewById(R.id.language_my_profile_pro_list);
        this.paymentOptionMyProfileProList = findViewById(R.id.payment_option_my_profile_pro_list);

        this.addAvailabilityBtn = findViewById(R.id.my_profile_pro_add_availability_btn);
        this.addReasonBtn = findViewById(R.id.my_profile_pro_add_reason_btn);
        this.addExperience = findViewById(R.id.my_profile_pro_add_experience_btn);
        this.addTrainingBtn = findViewById(R.id.my_profile_pro_add_training_btn);
        this.addLanguageBtn = findViewById(R.id.my_profile_pro_add_language_btn);
        this.addPaymentOptionBtn = findViewById(R.id.my_profile_pro_add_payment_option_btn);

        this.daysList = findViewById(R.id.my_profile_pro_days_list);
        this.availabilityTime = findViewById(R.id.my_profile_pro_availability_time_input);
        this.reasonDescription = findViewById(R.id.my_profile_pro_reason_description_input);
        this.experienceYear = findViewById(R.id.my_profile_pro_experience_year_input);
        this.experienceDegree = findViewById(R.id.my_profile_pro_experience_degree_input);
        this.trainingYear = findViewById(R.id.my_profile_pro_training_year_input);
        this.trainingDescription = findViewById(R.id.my_profile_pro_training_description_input);
        this.languagesList = findViewById(R.id.my_profile_pro_languages_list);
        this.paymentOptionsList = findViewById(R.id.my_profile_pro_payment_options_list);

        this.availabilities = new LinkedList<>();
        this.reasons = new LinkedList<>();
        this.experiences = new LinkedList<>();
        this.trainings = new LinkedList<>();
        this.languages = new LinkedHashSet<>();
        this.paymentOptions = new LinkedHashSet<>();

        this.availabilityErrorMsg = findViewById(R.id.my_profile_pro_availability_error);
        this.reasonErrorMsg = findViewById(R.id.my_profile_pro_reason_error);
        this.experienceErrorMsg = findViewById(R.id.my_profile_pro_experience_error);
        this.trainingErrorMsg = findViewById(R.id.my_profile_pro_training_error);
        this.languageErrorMsg = findViewById(R.id.my_profile_pro_language_error);
        this.paymentOptionErrorMsg = findViewById(R.id.my_profile_pro_payment_option_error);

        this.myProfileProUpdateBtn = findViewById(R.id.my_profile_pro_update_btn);
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
        this.takeHeaderFromCamera.setOnClickListener(this);
        this.selectHeaderFromGallery.setOnClickListener(this);
        this.addAvailabilityBtn.setOnClickListener(this);
        this.addReasonBtn.setOnClickListener(this);
        this.addExperience.setOnClickListener(this);
        this.addTrainingBtn.setOnClickListener(this);
        this.addLanguageBtn.setOnClickListener(this);
        this.addPaymentOptionBtn.setOnClickListener(this);
        this.myProfileProUpdateBtn.setOnClickListener(this);
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        setTitle(getString(R.string.title_my_profile));
        this.SetMyProfileProContext();
        this.SetLanguagesList();
        this.SetPaymentOptionsList();
        this.SetAvailabilitySection();
        this.SetReasonSection();
        this.SetEducationSection();
        this.SetExperienceSection();
        this.SetLanguageSection();
        this.SetPaymentOptionSection();
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
     * Set the section of availabilities
     */
    private void SetAvailabilitySection() {
        for (Availability a: ((Doctor) this.loggedUser).getAvailabilities()) {
            // Add it to the list of availabilities
            this.availabilities.add(a);

            // Add it to the view's list of availabilities
            this.FeedAvailabilityViewList(a);
        }
    }

    /**
     * Set the section of reasons
     */
    private void SetReasonSection() {
        for (Reason r: ((Doctor) this.loggedUser).getReasons()) {
            // Add it to the list of reasons
            this.reasons.add(r);

            // Add it to the view's list of reasons
            this.FeedReasonViewList(r);
        }
    }

    /**
     * Set the section of trainings
     */
    private void SetEducationSection() {
        for (Education e: ((Doctor) this.loggedUser).getTrainings()) {
            // Add it to the list of trainings
            this.trainings.add(e);

            // Add it to the view's list of trainings
            this.FeedEducationViewList(e);
        }
    }

    /**
     * Set the section of experiences
     */
    private void SetExperienceSection() {
        for (Experience e: ((Doctor) this.loggedUser).getExperiences()) {
            // Add it to the list of experiences
            this.experiences.add(e);

            // Add it to the view's list of experiences
            this.FeedExperienceViewList(e);
        }
    }

    /**
     * Set the section of languages
     */
    private void SetLanguageSection() {
        for (Language l: ((Doctor) this.loggedUser).getLanguages()) {
            // Add it to the list of languages
            this.languages.add(l);

            // Add it to the view's list of languages
            this.FeedLanguageViewList(l);
        }
    }

    /**
     * Set the section of payment options
     */
    private void SetPaymentOptionSection() {
        for (PaymentOption po: ((Doctor) this.loggedUser).getPaymentOptions()) {
            // Add it to the list of payment options
            this.paymentOptions.add(po);

            // Add it to the view's list of payment options
            this.FeedPaymentOptionViewList(po);
        }
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_profile_pro_take_picture_from_camera:
                this.CheckPermission("CAMERA");

                return;
            case R.id.my_profile_pro_select_picture_from_gallery:
                this.CheckPermission("READ_EXTERNAL_STORAGE");

                return;
            case R.id.my_profile_pro_take_header_from_camera:
                this.CheckPermission("CAMERA_HEADER");

                return;
            case R.id.my_profile_pro_select_header_from_gallery:
                this.CheckPermission("READ_EXTERNAL_STORAGE_HEADER");

                return;
            case R.id.my_profile_pro_add_availability_btn:
                this.AddAvailability();

                return;
            case R.id.my_profile_pro_add_reason_btn:
                this.AddReason();

                return;
            case R.id.my_profile_pro_add_experience_btn:
                this.AddExperience();

                return;
            case R.id.my_profile_pro_add_training_btn:
                this.AddTraining();

                return;
            case R.id.my_profile_pro_add_language_btn:
                this.AddLanguage();

                return;
            case R.id.my_profile_pro_add_payment_option_btn:
                this.AddPaymentOption();

                return;
            case R.id.my_profile_pro_update_btn:
                this.UpdateProfile();
        }
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
            case "READ_EXTERNAL_STORAGE_HEADER":
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_HEADER);
                } else {
                    // Permission has already been granted
                    this.SelectHeaderFromGallery();
                }

                break;
            case "CAMERA_HEADER":
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    //Request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCode.PERMISSIONS_REQUEST_CAMERA_HEADER);
                } else {
                    // Permission has already been granted
                    this.TakeHeaderFromCamera();
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
            case RequestCode.PERMISSIONS_REQUEST_CAMERA_HEADER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.TakeHeaderFromCamera();
                }
                break;
            case RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_HEADER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.SelectHeaderFromGallery();
                }
                break;
        }
    }

    /**
     * Set Sign-up context
     */
    private void SetMyProfileProContext() {
        this.header.setBlur(BLUR_AMOUNT);
        this.doctorProfileSection.setVisibility(View.VISIBLE);
        this.doctorAddressSection.setVisibility(View.VISIBLE);
        this.myProfileProUpdateBtn.setVisibility(View.VISIBLE);
        this.myProfileProMsg.setVisibility(View.GONE);

        if (this.loggedUser.getPicture() != null) {
            if (!this.loggedUser.getPicture().isEmpty()) {
                Uri uri = ImageService.GetURIFromPath(this.loggedUser.getPicture());

                if (uri != null) {
                    this.picture.setImageURI(uri);
                    this.picturePath = this.loggedUser.getPicture();
                    this.pictureURI = uri;
                }
            }
        }

        if (((Doctor) this.loggedUser).getHeader() != null) {
            if (!((Doctor) this.loggedUser).getHeader().isEmpty()) {
                Uri uri = ImageService.GetURIFromPath(((Doctor) this.loggedUser).getHeader());

                if (uri != null) {
                    this.header.setImageURI(uri);
                    this.headerPath = ((Doctor) this.loggedUser).getHeader();
                    this.headerURI = uri;
                }
            }
        }

        this.lastnameInput.setText(this.loggedUser.getLastname());
        this.firstnameInput.setText(this.loggedUser.getFirstname());
        this.specialityInput.setText(((Doctor) this.loggedUser).getSpeciality());
        this.emailInput.setText(this.loggedUser.getEmail());
        this.descriptionInput.setText(((Doctor) this.loggedUser).getDescription());
        this.contactNumberInput.setText(((Doctor) this.loggedUser).getContactNumber());
        this.oldPasswordInput.setText("");
        this.newPasswordInput.setText("");
        this.confirmPasswordInput.setText("");
        this.isUnderAgreement.setChecked(((Doctor) this.loggedUser).isUnderAgreement());
        this.isHealthInsuranceCard.setChecked(((Doctor) this.loggedUser).isHealthInsuranceCard());
        this.isThirdPartyPayment.setChecked(((Doctor) this.loggedUser).isThirdPartyPayment());
        this.street1Input.setText(this.loggedUser.GetStreet1());
        this.street2Input.setText(this.loggedUser.GetStreet2());
        this.cityInput.setText(this.loggedUser.GetCity());
        this.zipInput.setText(this.loggedUser.GetZip());
        this.countryInput.setText(this.loggedUser.GetCountry());
    }
    
    private void UpdateProfile() {
        if (!this.AllFieldsCorrect()) {
            this.DisplayErrorMsg();

            return;
        }

        DoctorDatabaseHelper doctorDbHelper = new DoctorDatabaseHelper(this.getApplicationContext());

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

        Doctor doctor = new Doctor(
                this.loggedUser.getId(),
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

        if (doctorDbHelper.UpdateDoctor(doctor)) {
            try {
                if (this.picturePath != null)
                    if (!this.picturePath.isEmpty())
                        if (!this.picturePath.equals(this.loggedUser.getPicture()))
                            this.RemoveOldPictureFile();

                if (this.headerPath != null)
                    if (!this.headerPath.isEmpty())
                        if (!this.headerPath.equals(((Doctor) this.loggedUser).getHeader()))
                            this.RemoveOldHeaderFile();
            }
            catch (Exception e) { e.printStackTrace(); }
            
            this.DisplaySuccessMsg();
            this.MakeToast(UPDATE);
            Intent i = new Intent();
            String key = this.getResources().getString(R.string.intent_logged_user);
            i.putExtra(key, this.loggedUser);
            setResult(RESULT_OK, i);
            // To refresh the activity
            finish();
            startActivity(getIntent());

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
                        || this.specialityInput.getText().toString().trim().isEmpty()
                        || this.emailInput.getText().toString().trim().isEmpty()
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
            return oldPwdEqual && bothNewPwdEqual;
        }

        return true;
    }

    /**
     * Display a success message to notify the update succeeded
     */
    private void DisplaySuccessMsg() {
        this.myProfileProMsg.setVisibility(View.VISIBLE);
        this.myProfileProMsg.setBackgroundResource(R.color.my_profile_pro_success_msg);
        this.myProfileProMsgTitle.setText(this.getResources().getString(R.string.my_profile_pro_success_msg_title));
        this.myProfileProMsgContent.setText(this.getResources().getString(R.string.my_profile_pro_success_msg_content));
        this.doctorProfileSection.setVisibility(View.VISIBLE);
        this.doctorAddressSection.setVisibility(View.VISIBLE);
        this.myProfileProUpdateBtn.setVisibility(View.VISIBLE);
    }

    /**
     * Display an error message to notify the update failed
     */
    private void DisplayErrorMsg() {
        this.myProfileProMsg.setVisibility(View.VISIBLE);
        this.myProfileProMsg.setBackgroundResource(R.color.my_profile_pro_error_msg);
        this.myProfileProMsgTitle.setText(this.getResources().getString(R.string.my_profile_pro_error_msg_title));
        this.myProfileProMsgContent.setText(this.getResources().getString(R.string.my_profile_pro_error_msg_content));
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

        /*
         * To store internally, use : getFilesDir()
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            pictureFile = File.createTempFile(pictureFileName, FILE_EXT, storageDir);

            if (this.picturePath != null)
                if (!this.picturePath.isEmpty())
                    if (!this.picturePath.equals(this.loggedUser.getPicture()))
                        this.RemoveCurrentPictureFile();

            this.picturePath = pictureFile.getAbsolutePath();
            this.pictureURI = ImageService.GetURIFromFile(pictureFile);
        }
        catch (Exception e) { e.printStackTrace(); }

        return pictureFile;
    }

    /**
     * Create a file using the uploaded header
     * @return The created file
     */
    private File CreateHeaderFile() {
        File headerFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String headerFileName = PREFIX_PROFILE_HEADER + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            headerFile = File.createTempFile(headerFileName, FILE_EXT, storageDir);

            if (this.headerPath != null)
                if (!this.headerPath.isEmpty())
                    if (!this.headerPath.equals(((Doctor) this.loggedUser).getHeader()))
                        this.RemoveCurrentHeaderFile();

            this.headerPath = headerFile.getAbsolutePath();
            this.headerURI = ImageService.GetURIFromFile(headerFile);
        }
        catch (Exception e) { e.printStackTrace(); }

        return headerFile;
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
     * Remove the current patient header file saved on the phone
     */
    private void RemoveCurrentHeaderFile() throws IOException {
        File currentHeaderFile = new File(this.headerPath);

        if (currentHeaderFile.getPath().isEmpty()) throw new FileNotFoundException();
        if (! currentHeaderFile.delete()) throw new IOException("Impossible to delete the header file.");
    }

    /**
     * Remove the current header file saved on the phone
     */
    private void RemoveOldHeaderFile() throws IOException {
        File currentHeaderFile = new File(((Doctor) this.loggedUser).getHeader());

        if (currentHeaderFile.getPath().isEmpty()) throw new FileNotFoundException();
        if (! currentHeaderFile.delete()) throw new IOException("Impossible to delete the old header file.");
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

        // Add it to the view's list of availabilities
        this.FeedAvailabilityViewList(a);

        // Display a toast msg
        this.MakeToast(ADD_AVAILABILITY);
    }

    /**
     * Check if the availability fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckAvailabilityFields() {
        return this.availabilityTime.getText().toString().trim().matches("([01])([0123456789]):([012345])([0123456789])");
    }

    /**
     * Add the provided availability to the availability view list
     * @param a The availability to add to the availability view list
     */
    private void FeedAvailabilityViewList(final Availability a) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View availabilityListItemLayout = inflater.inflate(R.layout.template_availability_my_profile_pro_list_item_layout, this.availabilityMyProfileProList, false);

        View day = inflater.inflate(R.layout.template_availability_my_profile_pro_list_item_day, (LinearLayout) availabilityListItemLayout, false);
        ((TextView) day).setText(a.getDate());

        View pipe = inflater.inflate(R.layout.template_my_profile_pro_list_item_pipe, (LinearLayout) availabilityListItemLayout, false);

        View time = inflater.inflate(R.layout.template_availability_my_profile_pro_list_item_time, (LinearLayout) availabilityListItemLayout, false);
        ((TextView) time).setText(a.getTime());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) availabilityListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) availabilityListItemLayout.getParent()).removeView(availabilityListItemLayout);
                availabilities.remove(a);
                MakeToast(REMOVE_AVAILABILITY);
            }
        });

        ((LinearLayout) availabilityListItemLayout).addView(day);
        ((LinearLayout) availabilityListItemLayout).addView(pipe);
        ((LinearLayout) availabilityListItemLayout).addView(time);
        ((LinearLayout) availabilityListItemLayout).addView(remove);

        this.availabilityMyProfileProList.addView(availabilityListItemLayout);
    }

    /**
     * Add the reason chosen by the doctor
     */
    private void AddReason() {
        if (!this.CheckReasonFields()) {
            this.reasonErrorMsg.setVisibility(View.VISIBLE);

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

        // Add it to the view's list of reasons
        this.FeedReasonViewList(r);

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
     * Add the provided reason to the reason view list
     * @param r The reason to add to the reason view list
     */
    private void FeedReasonViewList(final Reason r) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View reasonListItemLayout = inflater.inflate(R.layout.template_reason_my_profile_pro_list_item_layout, this.reasonMyProfileProList, false);

        View description = inflater.inflate(R.layout.template_reason_my_profile_pro_list_item_description, (LinearLayout) reasonListItemLayout, false);
        ((TextView) description).setText(r.getDescription());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) reasonListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) reasonListItemLayout.getParent()).removeView(reasonListItemLayout);
                reasons.remove(r);
                MakeToast(REMOVE_REASON);
            }
        });

        ((LinearLayout) reasonListItemLayout).addView(description);
        ((LinearLayout) reasonListItemLayout).addView(remove);

        this.reasonMyProfileProList.addView(reasonListItemLayout);
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

        // Add it to the view's list of trainings
        this.FeedEducationViewList(e);

        // Display a toast msg
        this.MakeToast(ADD_TRAINING);
    }

    /**
     * Check if the training fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckTrainingFields() {
        return this.trainingYear.getText().toString().trim().matches("\\d{4}")
                && !this.trainingDescription.getText().toString().trim().isEmpty();
    }

    /**
     * Add the provided training to the education view list
     * @param e The training to add to the education view list
     */
    private void FeedEducationViewList(final Education e) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View educationListItemLayout = inflater.inflate(R.layout.template_education_my_profile_pro_list_item_layout, this.educationMyProfileProList, false);

        View year = inflater.inflate(R.layout.template_education_my_profile_pro_list_item_year, (LinearLayout) educationListItemLayout, false);
        ((TextView) year).setText(e.getYear());

        View pipe = inflater.inflate(R.layout.template_my_profile_pro_list_item_pipe, (LinearLayout) educationListItemLayout, false);

        View degree = inflater.inflate(R.layout.template_education_my_profile_pro_list_item_degree, (LinearLayout) educationListItemLayout, false);
        ((TextView) degree).setText(e.getDegree());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) educationListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) educationListItemLayout.getParent()).removeView(educationListItemLayout);
                reasons.remove(e);
                MakeToast(REMOVE_TRAINING);
            }
        });

        ((LinearLayout) educationListItemLayout).addView(year);
        ((LinearLayout) educationListItemLayout).addView(pipe);
        ((LinearLayout) educationListItemLayout).addView(degree);
        ((LinearLayout) educationListItemLayout).addView(remove);

        this.educationMyProfileProList.addView(educationListItemLayout);
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
        String desc = this.experienceDegree.getText().toString().trim();

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

        // Add it to the view's list of experiences
        this.FeedExperienceViewList(e);

        // Display a toast msg
        this.MakeToast(ADD_EXPERIENCE);
    }

    /**
     * Check if the experience fields are correctly filled
     * @return If the fields are correctly filled
     */
    private boolean CheckExperienceFields() {
        return this.experienceYear.getText().toString().trim().matches("\\d{4}")
                && !this.experienceDegree.getText().toString().trim().isEmpty();
    }

    /**
     * Add the provided experience to the experience view list
     * @param e The experience to add to the experience view list
     */
    private void FeedExperienceViewList(final Experience e) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View experienceListItemLayout = inflater.inflate(R.layout.template_experience_my_profile_pro_list_item_layout, this.experienceMyProfileProList, false);

        View year = inflater.inflate(R.layout.template_experience_my_profile_pro_list_item_year, (LinearLayout) experienceListItemLayout, false);
        ((TextView) year).setText(e.getYear());

        View pipe = inflater.inflate(R.layout.template_my_profile_pro_list_item_pipe, (LinearLayout) experienceListItemLayout, false);

        View description = inflater.inflate(R.layout.template_experience_my_profile_pro_list_item_description, (LinearLayout) experienceListItemLayout, false);
        ((TextView) description).setText(e.getDescription());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) experienceListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) experienceListItemLayout.getParent()).removeView(experienceListItemLayout);
                experiences.remove(e);
                MakeToast(REMOVE_EXPERIENCE);
            }
        });

        ((LinearLayout) experienceListItemLayout).addView(year);
        ((LinearLayout) experienceListItemLayout).addView(pipe);
        ((LinearLayout) experienceListItemLayout).addView(description);
        ((LinearLayout) experienceListItemLayout).addView(remove);

        this.experienceMyProfileProList.addView(experienceListItemLayout);
    }

    /**
     * Add the language chosen by the doctor
     */
    private void AddLanguage() {
        // Create a new Language object
        Language l = Language.GetValueOf(this.languagesList.getSelectedItem().toString());

        // If already added
        if (this.languages.contains(l)) {
            this.languageErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        this.languageErrorMsg.setVisibility(View.GONE);

        // Add it to the list of languages
        this.languages.add(l);

        // Add it to the view's list of languages
        this.FeedLanguageViewList(l);

        // Display a toast msg
        this.MakeToast(ADD_LANGUAGE);
    }

    /**
     * Add the provided language to the language view list
     * @param l The language to add to the language view list
     */
    private void FeedLanguageViewList(final Language l) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View languageListItemLayout = inflater.inflate(R.layout.template_language_my_profile_pro_list_item_layout, this.languageMyProfileProList, false);

        View description = inflater.inflate(R.layout.template_language_my_profile_pro_list_item_description, (LinearLayout) languageListItemLayout, false);
        ((TextView) description).setText(l.toString());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) languageListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) languageListItemLayout.getParent()).removeView(languageListItemLayout);
                languages.remove(l);
                MakeToast(REMOVE_LANGUAGE);
            }
        });

        ((LinearLayout) languageListItemLayout).addView(description);
        ((LinearLayout) languageListItemLayout).addView(remove);

        this.languageMyProfileProList.addView(languageListItemLayout);
    }

    /**
     * Add the payment option chosen by the doctor
     */
    private void AddPaymentOption() {
        // Create a new PaymentOption object
        PaymentOption po = PaymentOption.GetValueOf(this.paymentOptionsList.getSelectedItem().toString());

        // If already added
        if (this.paymentOptions.contains(po)) {
            this.paymentOptionErrorMsg.setVisibility(View.VISIBLE);

            return;
        }

        this.paymentOptionErrorMsg.setVisibility(View.GONE);

        // Add it to the list of payment options
        this.paymentOptions.add(po);

        // Add it to the view's list of payment options
        this.FeedPaymentOptionViewList(po);

        // Display a toast msg
        this.MakeToast(ADD_PAYMENT_OPTION);
    }

    /**
     * Add the provided payment option to the payment option view list
     * @param po The payment option to add to the payment option view list
     */
    private void FeedPaymentOptionViewList(final PaymentOption po) {
        LayoutInflater inflater = this.getLayoutInflater();

        final View paymentOptionListItemLayout = inflater.inflate(R.layout.template_payment_option_my_profile_pro_list_item_layout, this.paymentOptionMyProfileProList, false);

        View description = inflater.inflate(R.layout.template_payment_option_my_profile_pro_list_item_description, (LinearLayout) paymentOptionListItemLayout, false);
        ((TextView) description).setText(po.toString());

        View remove = inflater.inflate(R.layout.template_my_profile_pro_list_item_remove, (LinearLayout) paymentOptionListItemLayout, false);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup) paymentOptionListItemLayout.getParent()).removeView(paymentOptionListItemLayout);
                paymentOptions.remove(po);
                MakeToast(REMOVE_PAYMENT_OPTION);
            }
        });

        ((LinearLayout) paymentOptionListItemLayout).addView(description);
        ((LinearLayout) paymentOptionListItemLayout).addView(remove);

        this.paymentOptionMyProfileProList.addView(paymentOptionListItemLayout);
    }

    /**
     * Show a toast after an action
     * @param action The action performed
     */
    private void MakeToast(int action) {
        Context context = getApplicationContext();
        CharSequence content;
        Toast toast;
        int duration = Toast.LENGTH_LONG;

        switch (action) {
            case UPDATE:
                content = this.getResources().getString(R.string.my_profile_pro_toast_update_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_AVAILABILITY:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_availability_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_REASON:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_reason_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_EXPERIENCE:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_experience_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_TRAINING:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_training_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_LANGUAGE:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_language_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case ADD_PAYMENT_OPTION:
                content = this.getResources().getString(R.string.my_profile_pro_toast_add_payment_option_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_AVAILABILITY:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_availability_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_REASON:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_reason_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_EXPERIENCE:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_experience_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_TRAINING:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_training_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_LANGUAGE:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_language_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case REMOVE_PAYMENT_OPTION:
                content = this.getResources().getString(R.string.my_profile_pro_toast_remove_payment_option_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (this.picturePath != null)
                if (!this.picturePath.isEmpty())
                    if (!this.picturePath.equals(this.loggedUser.getPicture()))
                        this.RemoveCurrentPictureFile();

            if (this.headerPath != null)
                if (!this.headerPath.isEmpty())
                    if (!this.headerPath.equals(((Doctor) this.loggedUser).getHeader()))
                        this.RemoveCurrentHeaderFile();

            finish();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
