package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jgabrielfreitas.core.BlurImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.ImageService;

public class DoctorProfileActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Doctor doctor;
    private Resident loggedUser;

    private ConstraintLayout mainLayout;
    private Button bookAppointmentBtn;
    private CircleImageView doctorPicture;
    private BlurImageView doctorHeader;
    private TextView
                doctorFullname,
                doctorSpeciality;
    private TextView
                doctorAddressContent,
                doctorPricesRefundsContent,
                doctorPaymentOptionsContent,
                isUnderAgreementPopupContent,
                isThirdPartyPaymentPopupContent,
                isHealthInsuranceCardPopupContent;
    private LinearLayout
            doctorAddressSection,
            doctorPricesRefundsSection,
            isUnderAgreementPopupSection,
            isThirdPartyPaymentPopupSection,
            isHealthInsuranceCardPopupSection,
            doctorPaymentOptionsSection,
            doctorDescriptionSection,
            doctorHoursContactsSection,
            doctorEducationSection,
            doctorLanguagesSection,
            doctorExperiencesSection;
    private PopupWindow currentPopup;
    private TextView popupClose;
    private RelativeLayout popupExternalBackground;
    private LinearLayout popupContentLayout;

    private static final int BLUR_AMOUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        this.Instantiate();
        this.RetrieveExtraParams();
        this.SetContent();
        this.SubscribeEvents();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.mainLayout = findViewById(R.id.doctor_profile_layout);
        this.bookAppointmentBtn = findViewById(R.id.doctor_profile_book_appointment);
        this.doctorPicture = findViewById(R.id.doctor_profile_picture);
        this.doctorHeader = findViewById(R.id.doctor_profile_header);
        this.doctorFullname = findViewById(R.id.doctor_profile_fullname);
        this.doctorSpeciality = findViewById(R.id.doctor_profile_speciality);

        // Sections
        this.doctorAddressSection = findViewById(R.id.doctor_profile_address_section);
        this.doctorPricesRefundsSection = findViewById(R.id.doctor_profile_prices_refunds_section);
        this.doctorPaymentOptionsSection = findViewById(R.id.doctor_profile_payment_options_section);
        this.doctorDescriptionSection = findViewById(R.id.doctor_profile_description_section);
        this.doctorHoursContactsSection = findViewById(R.id.doctor_profile_hours_contacts_section);
        this.doctorEducationSection = findViewById(R.id.doctor_profile_education_section);
        this.doctorLanguagesSection = findViewById(R.id.doctor_profile_languages_section);
        this.doctorExperiencesSection = findViewById(R.id.doctor_profile_experiences_section);

        // Content
        this.doctorAddressContent = findViewById(R.id.doctor_profile_address_content);
        this.doctorPricesRefundsContent = findViewById(R.id.doctor_profile_prices_refunds_content);
        this.doctorPaymentOptionsContent = findViewById(R.id.doctor_profile_payment_options_content);

        // Popups
        this.currentPopup = null;
        this.popupClose = null;
        this.popupExternalBackground = null;
        this.popupContentLayout = null;
    }

    /**
     * Retrieve the extra params sent by the Search view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        // Retrieve the params
        // Get the logged user
        String key = this.getResources().getString(R.string.intent_logged_user);
        this.loggedUser = bundle.containsKey(key) ?
                        bundle.getSerializable(key) instanceof Patient ?
                            (Patient) bundle.getSerializable(key) :
                                (Doctor) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());

        // Get the doctor
        key = this.getResources().getString(R.string.intent_doctor);
        this.doctor = (Doctor) i.getExtras().getSerializable(key);
        this.doctor = (Doctor) this.doctor.Update(this.getApplicationContext());
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        // Set doctor picture
        if (!this.doctor.getPicture().isEmpty()) this.doctorPicture.setImageURI(ImageService.GetURIFromPath(this.doctor.getPicture()));

        // Set doctor header
        if (!this.doctor.getHeader().isEmpty()) this.doctorHeader.setImageURI(ImageService.GetURIFromPath(this.doctor.getHeader()));
        else this.doctorHeader.setImageResource(R.drawable.beach_2);
        this.doctorHeader.setBlur(BLUR_AMOUNT);

        // Set doctor fullname
        this.doctorFullname.setText(this.doctor.getFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(this.doctor.getSpeciality());

        // Set doctor address
        this.doctorAddressContent.setText(this.doctor.GetFullAddress());

        // Set doctor prices and refunds
        this.doctorPricesRefundsContent.setText(doctor.getPricesAndRefundsAsString(this.getApplicationContext()));

        // Set doctor payment options
        this.doctorPaymentOptionsContent.setText(doctor.getPaymentOptionsAsString());
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.bookAppointmentBtn.setOnClickListener(this);
        this.doctorAddressSection.setOnClickListener(this);
        this.doctorPricesRefundsSection.setOnClickListener(this);
        this.doctorPaymentOptionsSection.setOnClickListener(this);
        this.doctorDescriptionSection.setOnClickListener(this);
        this.doctorHoursContactsSection.setOnClickListener(this);
        this.doctorEducationSection.setOnClickListener(this);
        this.doctorLanguagesSection.setOnClickListener(this);
        this.doctorExperiencesSection.setOnClickListener(this);
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        int titleKey = R.string.doctor_profile_popup_title;
        int contentKey = R.string.doctor_profile_popup_content;
        boolean popup = true;

        switch (v.getId()) {
            case R.id.doctor_profile_book_appointment:
                this.BookAppointment();
                return;
            case R.id.doctor_profile_popup_close:
                this.ClearCurrentPopupContext();
                return;
            case R.id.doctor_profile_popup_external_background:
                if (v.getId() != R.id.doctor_profile_popup_content_layout) {
                    this.ClearCurrentPopupContext();
                    return;
                }
            case R.id.doctor_profile_address_section:
                titleKey = R.string.doctor_profile_address;
                contentKey = R.id.doctor_profile_address_content;
                break;
            default:
                popup = false;
        }

        if (popup) {
            this.CreatePopup(
                    this.getResources().getString(titleKey),
                    ((TextView) findViewById(contentKey)).getText().toString()
            );

            return;
        }

        String content = this.getResources().getString(contentKey);

        switch (v.getId()) {
            case R.id.doctor_profile_prices_refunds_section:
                titleKey = R.string.doctor_profile_prices_refunds;
                content = "";
                break;
            case R.id.doctor_profile_description_section:
                titleKey = R.string.doctor_profile_description;
                content = this.doctor.getDescription();
                break;
            case R.id.doctor_profile_education_section:
                titleKey = R.string.doctor_profile_education;
                content = this.doctor.getTrainingsAsString();
                break;
            case R.id.doctor_profile_experiences_section:
                titleKey = R.string.doctor_profile_experiences;
                content = this.doctor.getExperiencesAString();
                break;
            case R.id.doctor_profile_hours_contacts_section:
                titleKey = R.string.doctor_profile_hours_contacts;
                // TODO: content = this.doctor.GetFullAddress();
                break;
            case R.id.doctor_profile_languages_section:
                titleKey = R.string.doctor_profile_languages;
                content = this.doctor.getLanguagesAsString();
                break;
            default:
                return;
        }

        this.CreatePopup(
                this.getResources().getString(titleKey),
                content
        );
    }

    /**
     * Start BookAppointment activity
     */
    private void BookAppointment() {
        if(this.loggedUser == null) {
            startActivityForResult(new Intent(DoctorProfileActivity.this, LoginActivity.class), RequestCode.CONNECT_PATIENT);

            return;
        }

        // Create the intent
        Intent i = new Intent(DoctorProfileActivity.this, ChooseReasonActivity.class);

        // Create the appointment object
        Booking booking = new Booking(
                (Patient) this.loggedUser,
                this.doctor,
                null,
                "",
                "",
                ""
        );

        // Prepare the intent parameters
        // The booking
        String key = this.getResources().getString(R.string.intent_booking);
        i.putExtra(key, booking);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Open a new popup
     * @param title The title of the popup
     * @param content The content of the popup
     */
    private void CreatePopup(String title, String content) {
        // Clear the current popup context if any
        this.ClearCurrentPopupContext();

        // Create the new popup context
        View popupSampleView = this.CreateNewPopupContext(title);

        // Attach the appropriate events to the current popup
        this.SubscribeEventsPopup();

        // Set the attributes of the popup
        this.SetPopupAttributes(title, content, popupSampleView);

        // Display the popup
        this.currentPopup.showAtLocation(this.mainLayout, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * Clear the current popup context
     */
    private void ClearCurrentPopupContext() {
        if (this.currentPopup != null) this.currentPopup.dismiss();
        if (this.popupClose != null) {
            this.popupClose = null;
        }
        if (this.popupExternalBackground != null) this.popupExternalBackground = null;
        if (this.popupContentLayout != null) this.popupContentLayout = null;
    }

    /**
     * Create a new popup context
     * @return The popup sample view
     */
    private View CreateNewPopupContext(String title) {
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = getLayoutInflater();
        int popupLayout = R.layout.popup_doctor_profile_layout;

        // Inflate the popup layout
        if (title == this.getResources().getString(R.string.doctor_profile_prices_refunds))
            popupLayout = R.layout.popup_doctor_profile_prices_refunds_layout;

        View popupSampleView = inflater.inflate(popupLayout, null);

        // Create the popup window
        this.currentPopup = new PopupWindow(popupSampleView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.popupClose = popupSampleView.findViewById(R.id.doctor_profile_popup_close);
        this.popupExternalBackground = popupSampleView.findViewById(R.id.doctor_profile_popup_external_background);
        this.popupContentLayout = popupSampleView.findViewById(R.id.doctor_profile_popup_content_layout);

        return popupSampleView;
    }

    /**
     * Attach the appropriate events to the current popup components
     */
    private void SubscribeEventsPopup() {
        this.popupClose.setOnClickListener(this);
        this.popupExternalBackground.setOnClickListener(this);
        this.popupContentLayout.setOnClickListener(this);
    }

    /**
     * Set the attributes of the current popup
     * @param title The title of the popup
     * @param content THe content of the popup
     * @param popupSampleView The sample view used to draw the popup
     */
    private void SetPopupAttributes(String title, String content, View popupSampleView) {
        // Retrieve popup components references
        TextView popupTitle = popupSampleView.findViewById(R.id.doctor_profile_popup_title);

        // Set the popup title
        popupTitle.setText(title);

        if (title != this.getResources().getString(R.string.doctor_profile_prices_refunds)) {
            TextView popupContent = popupSampleView.findViewById(R.id.doctor_profile_popup_content);
            popupContent.setText(content);

            return;
        }

        this.FormatPricesRefundsPopupViewData(popupSampleView);
    }

    /**
     * Format prices and refunds popup view data
     * @param popupSampleView
     */
    private void FormatPricesRefundsPopupViewData(View popupSampleView) {
        this.isUnderAgreementPopupSection = popupSampleView.findViewById(R.id.doctor_profile_popup_section_is_under_agreement);
        this.isThirdPartyPaymentPopupSection = popupSampleView.findViewById(R.id.doctor_profile_section_is_third_party_payment);
        this.isHealthInsuranceCardPopupSection = popupSampleView.findViewById(R.id.doctor_profile_section_is_health_insurance_card);
        this.isUnderAgreementPopupContent = popupSampleView.findViewById(R.id.doctor_profile_popup_is_under_agreement_content);
        this.isThirdPartyPaymentPopupContent = popupSampleView.findViewById(R.id.doctor_profile_popup_is_third_party_payment_content);
        this.isHealthInsuranceCardPopupContent = popupSampleView.findViewById(R.id.doctor_profile_popup_is_health_insurance_card_content);

        if (this.doctor.isUnderAgreement()) {
            this.isUnderAgreementPopupContent.setText(this.getResources().getString(R.string.doctor_profile_popup_is_under_agreement_content));
        } else {
            this.isUnderAgreementPopupSection.setVisibility(View.GONE);
        }

        if (this.doctor.isThirdPartyPayment()) {
            this.isThirdPartyPaymentPopupContent.setText(this.getResources().getString(R.string.doctor_profile_popup_is_third_party_payment_content));
        } else {
            this.isThirdPartyPaymentPopupSection.setVisibility(View.GONE);
        }

        if (this.doctor.isHealthInsuranceCard()) {
            this.isHealthInsuranceCardPopupContent.setText(this.getResources().getString(R.string.doctor_profile_popup_is_health_insurance_card_content));
        } else {
            this.isHealthInsuranceCardPopupSection.setVisibility(View.GONE);
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
                this.loggedUser = bundle.containsKey(key) ?
                        bundle.getSerializable(key) instanceof Patient ?
                            (Patient) bundle.getSerializable(key) :
                                (Doctor) bundle.getSerializable(key) : null;
                if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());

            }
        }

        if (requestCode == RequestCode.CONNECT_PATIENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the logged user
                Bundle bundle = intent.getExtras();
                String key = this.getResources().getString(R.string.intent_logged_user);
                this.loggedUser = bundle.containsKey(key) ?
                        bundle.getSerializable(key) instanceof Patient ?
                            (Patient) bundle.getSerializable(key) :
                                (Doctor) bundle.getSerializable(key) : null;
                if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());
                this.BookAppointment();
            }
        }
    }
}
