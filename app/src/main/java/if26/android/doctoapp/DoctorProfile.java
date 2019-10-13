package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DoctorProfile
        extends AppCompatActivity
        implements View.OnClickListener {
    private Bundle doctor;
    private ConstraintLayout mainLayout;
    private Button bookAppointmentBtn;
    private ImageView doctorPicture;
    private TextView
                doctorFullname,
                doctorSpeciality;
    private TextView
                doctorAddressContent,
                doctorPricesRefundsContent,
                doctorPaymentOptionsContent,
                doctorDescriptionContent,
                doctorHoursContactsContent,
                doctorEducationContent,
                doctorLanguagesContent,
                doctorExperiencesContent;
    private LinearLayout
            doctorAddressSection,
            doctorPricesRefundsSection,
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
        this.doctorDescriptionContent = findViewById(R.id.doctor_profile_description_content);
        this.doctorHoursContactsContent = findViewById(R.id.doctor_profile_hours_contacts_content);
        this.doctorEducationContent = findViewById(R.id.doctor_profile_education_content);
        this.doctorLanguagesContent = findViewById(R.id.doctor_profile_languages_content);
        this.doctorExperiencesContent = findViewById(R.id.doctor_profile_experiences_content);

        // Popup
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
        this.doctor = i.getExtras().getBundle(this.getResources().getString(R.string.search_intent_doctor));
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        DoctorService doctorService = new DoctorService(this, this.doctor);

        // Set doctor picture
        this.doctorPicture.setImageResource(doctorService.GetDoctorPicture());

        // Set doctor fullname
        this.doctorFullname.setText(doctorService.GetDoctorFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(doctorService.GetDoctorSpeciality());

        // Set doctor address
        this.doctorAddressContent.setText(doctorService.GetDoctorAddress());

        // Set doctor prices and refunds
        this.doctorPricesRefundsContent.setText(doctorService.GetDoctorPricesRefunds());

        // Set doctor payment options
        this.doctorPaymentOptionsContent.setText(doctorService.GetDoctorPaymentOptions());

        // Set doctor description
        this.doctorDescriptionContent.setText(doctorService.GetDoctorDescription());

        // Set doctor hours and contacts
        //this.doctorHoursContactsContent.setText(doctorService.GetDoctorHoursContacts());

        // Set doctor education
        this.doctorEducationContent.setText(doctorService.GetDoctorEducation());

        // Set doctor languages
        this.doctorLanguagesContent.setText(doctorService.GetDoctorLanguages());

        // Set doctor experiences
        this.doctorExperiencesContent.setText(doctorService.GetDoctorExperiences());
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
     * @param v DoctorProfile view
     */
    @Override
    public void onClick(View v) {
        int titleKey = R.string.doctor_profile_popup_title;
        int contentKey = R.string.doctor_profile_popup_content;

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
            case R.id.doctor_profile_description_section:
                titleKey = R.string.doctor_profile_description;
                contentKey = R.id.doctor_profile_description_content;
                break;
            case R.id.doctor_profile_education_section:
                titleKey = R.string.doctor_profile_education;
                contentKey = R.id.doctor_profile_education_content;
                break;
            case R.id.doctor_profile_experiences_section:
                titleKey = R.string.doctor_profile_experiences;
                contentKey = R.id.doctor_profile_experiences_content;
                break;
            case R.id.doctor_profile_hours_contacts_section:
                titleKey = R.string.doctor_profile_hours_contacts;
                contentKey = R.id.doctor_profile_hours_contacts_content;
                break;
            case R.id.doctor_profile_languages_section:
                titleKey = R.string.doctor_profile_languages;
                contentKey = R.id.doctor_profile_languages_content;
                break;
            case R.id.doctor_profile_payment_options_section:
                titleKey = R.string.doctor_profile_payment_options;
                contentKey = R.id.doctor_profile_payment_options_content;
                break;
            case R.id.doctor_profile_prices_refunds_section:
                titleKey = R.string.doctor_profile_prices_refunds;
                contentKey = R.id.doctor_profile_prices_refunds_content;
                break;
        }

        this.CreatePopup(
                this.getResources().getString(titleKey),
                ((TextView) findViewById(contentKey)).getText().toString()
        );
    }

    /**
     * Start BookAppointment activity
     */
    private void BookAppointment() {
        // Create the intent
        Intent i = new Intent(DoctorProfile.this, ChooseReasonActivity.class);

        // Prepare the intent parameters
        String key = this.getResources().getString(R.string.search_intent_doctor);
        i.putExtra(key, this.doctor);

        // Start the activity
        startActivity(i);
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
        View popupSampleView = this.CreateNewPopupContext();

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
    private View CreateNewPopupContext() {
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the popup layout
        View popupSampleView = inflater.inflate(R.layout.doctor_profile_popup_layout, null);

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
        TextView popupContent = popupSampleView.findViewById(R.id.doctor_profile_popup_content);

        // Set the popup title
        popupTitle.setText(title);
        popupContent.setText(content);
    }
}
