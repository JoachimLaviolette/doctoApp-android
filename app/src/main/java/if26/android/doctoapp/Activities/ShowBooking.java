package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.DatabaseHelpers.BookingDatabaseHelper;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.ImageService;

public class ShowBooking
        extends AppCompatActivity
        implements View.OnClickListener {
    private ConstraintLayout mainLayout;

    private Doctor doctor;
    private Patient patient;
    private Booking booking;
    private Resident loggedUser;

    private TextView appointmentDay;
    private TextView appointmentTime;

    private ImageView doctorPicture;
    private TextView doctorFullname;
    private TextView doctorSpeciality;
    private TextView doctorChevron;

    private TextView doctorContactNumber;
    private TextView doctorPaymentOptions;
    private TextView doctorPricesRefunds;

    private TextView warningMessage;
    private TextView reason;
    private TextView address;

    private ImageView patientPicture;
    private TextView patientFullname;
    private TextView patientBirthdate;

    private TextView updateBooking;
    private TextView cancelBooking;

    private PopupWindow currentCancelBookingPopup;
    private TextView popupClose;
    private RelativeLayout popupExternalBackground;
    private LinearLayout popupContentLayout;
    private Button discardBtn;
    private Button confirmBtn;
    private TextView popupDate;
    private TextView popupTime;
    private TextView popupDoctor;

    private ImageButton homeBtn;

    private static final int BOOKING_CANCELLED_SUCCESS = 0;
    private static final int BOOKING_CANCELLED_FAIL = 1;
    private static final int BOOKING_UPDATED_SUCCESS = 2;
    private static final int BOOKING_UPDATED_FAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.mainLayout = findViewById(R.id.show_booking_layout);
        this.homeBtn = findViewById(R.id.home);
        this.appointmentDay = findViewById(R.id.show_booking_appointment_fullday);
        this.appointmentTime = findViewById(R.id.show_booking_appointment_time);
        this.reason = findViewById(R.id.show_booking_appointment_reason);
        this.address = findViewById(R.id.show_booking_appointment_address_content);
        this.reason = findViewById(R.id.show_booking_appointment_reason);
        this.doctorPicture = findViewById(R.id.show_booking_appointment_doctor_picture);
        this.doctorFullname = findViewById(R.id.show_booking_appointment_doctor_fullname);
        this.doctorSpeciality = findViewById(R.id.show_booking_appointment_doctor_speciality);
        this.doctorChevron = findViewById(R.id.show_booking_appointment_chevron);
        this.doctorContactNumber = findViewById(R.id.show_booking_appointment_contact_number);
        this.doctorPaymentOptions = findViewById(R.id.show_booking_appointment_payment_options_content);
        this.doctorPricesRefunds = findViewById(R.id.show_booking_appointment_prices_refunds_content);
        this.warningMessage = findViewById(R.id.show_booking_appointment_warning_msg_content);
        this.patientPicture = findViewById(R.id.show_booking_appointment_patient_picture);
        this.patientFullname = findViewById(R.id.show_booking_appointment_patient_fullname);
        this.patientBirthdate = findViewById(R.id.show_booking_appointment_patient_birthdate);
        this.updateBooking = findViewById(R.id.show_booking_appointment_update_booking);
        this.cancelBooking = findViewById(R.id.show_booking_appointment_cancel_booking);

        // Popup
        this.currentCancelBookingPopup = null;
        this.popupClose = null;
        this.popupExternalBackground = null;
        this.popupContentLayout = null;
        this.discardBtn = null;
        this.confirmBtn = null;
        this.popupDate = null;
        this.popupTime = null;
        this.popupDoctor = null;
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        if (this.loggedUser instanceof Patient) {
            this.doctorChevron.setOnClickListener(this);
            this.updateBooking.setOnClickListener(this);
            this.cancelBooking.setOnClickListener(this);
        }
        this.homeBtn.setOnClickListener(this);
    }

    /**
     * Retrieve the params sent by the ChooseDateTime view
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

        // Get the booking
        key = this.getResources().getString(R.string.intent_booking);
        this.booking = (Booking) bundle.getSerializable(key);

        // Get the doctor
        this.doctor = this.booking.getDoctor();
        this.doctor = (Doctor) this.doctor.Update(this.getApplicationContext());

        // Get the patient
        this.patient = this.booking.getPatient();
        this.patient = (Patient) this.patient.Update(this.getApplicationContext());
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        // Set appointment full day
        this.appointmentDay.setText(this.booking.getFullDate());

        // Set appointment time
        this.appointmentTime.setText(this.booking.getTime());

        // Set the reason
        this.reason.setText(this.booking.getReason().getDescription());

        // Set the address
        this.address.setText(this.doctor.GetFullAddress());

        // Set the logged-user specific content
        if (this.loggedUser instanceof Patient) this.SetPatientContentContext();
        else this.SetDoctorContentContext();
    }

    /**
     * Set the content in case the logged user is a patient
     */
    private void SetPatientContentContext() {
        // Set doctor picture
        if (!this.doctor.getPicture().isEmpty()) this.doctorPicture.setImageURI(ImageService.GetURIFromPath(this.doctor.getPicture()));

        // Set doctor fullname
        this.doctorFullname.setText(this.doctor.getFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(this.doctor.getSpeciality());

        // Set doctor contact number
        this.doctorContactNumber.setText(this.doctor.getContactNumberAsString());

        // Set doctor payment options
        this.doctorPaymentOptions.setText(this.doctor.getPaymentOptionsAsString());

        // Set doctor prices and refunds
        this.doctorPricesRefunds.setText(this.doctor.getPricesAndRefundsAsString(this));

        // Set the warning message
        this.warningMessage.setText(this.doctor.getWarningMessage(this));

        // Hide patient-related sections
        (findViewById(R.id.show_booking_appointment_section_patient)).setVisibility(View.GONE);
    }

    /**
     * Set the content in case the logged user is a doctor
     */
    private void SetDoctorContentContext() {
        // Set patient picture
        if (!this.patient.getPicture().isEmpty()) this.patientPicture.setImageURI(ImageService.GetURIFromPath(this.patient.getPicture()));

        // Set patient fullname
        this.patientFullname.setText(this.patient.getFullname());

        // Set patient birthdate
        String birthdate = getString(R.string.show_booking_appointment_patient_birthdate_prefix) + this.patient.getBirthdate();
        this.patientBirthdate.setText(birthdate);

        // Hide doctor-related sections
        (findViewById(R.id.show_booking_appointment_section_doctor)).setVisibility(View.GONE);
        (findViewById(R.id.show_booking_appointment_section_manage_booking)).setVisibility(View.GONE);
        (findViewById(R.id.show_booking_appointment_section_warning_msg)).setVisibility(View.GONE);
        (findViewById(R.id.show_booking_appointment_section_contact)).setVisibility(View.GONE);
        (findViewById(R.id.show_booking_appointment_section_payment_options)).setVisibility(View.GONE);
        (findViewById(R.id.show_booking_appointment_section_prices_refunds)).setVisibility(View.GONE);
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override   
    public void onClick(View v) {
        if (this.loggedUser instanceof Patient) {
            if (v.getId() == R.id.show_booking_appointment_chevron) {
                this.ShowDoctorProfile();

                return;
            }

            if (v.getId() == R.id.show_booking_appointment_update_booking) {
                this.UpdateBooking();

                return;
            }

            if (v.getId() == R.id.show_booking_appointment_cancel_booking) {
                this.CancelBooking();

                return;
            }

            if (v.getId() == R.id.show_booking_appointment_cancel_appointment_popup_close
                    || v.getId() == R.id.show_booking_appointment_cancel_appointment_popup_discard_btn) {
                this.ClearCurrentCancelBookingPopupContext();

                return;
            }

            if (v.getId() == R.id.show_booking_appointment_cancel_appointment_popup_external_background
                    && v.getId() != R.id.show_booking_appointment_cancel_appointment_popup_content_layout) {
                    this.ClearCurrentCancelBookingPopupContext();

                return;
            }

            if (v.getId() == R.id.show_booking_appointment_cancel_appointment_popup_confirm_btn) {
                this.CancelBooking();

                return;
            }
        }

        if (v.getId() == R.id.home) {
            this.Home();
        }
    }

    /**
     * Start UpdateBooking activity
     */
    private void UpdateBooking() {
        // Create the intent
        Intent i = new Intent(ShowBooking.this, ChooseAvailabilityActivity.class);

        // Prepare the intent parameters
        // The patient
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // The booking
        key = this.getResources().getString(R.string.intent_booking);
        i.putExtra(key, this.booking);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Handle booking cancellation
     */
    private void CancelBooking() {
        if (this.currentCancelBookingPopup == null) {
            this.CreateCancelBookingPopup();

            return;
        }

        // Clear current cancel booking popup
        this.ClearCurrentCancelBookingPopupContext();

        // Try to cancel (remove) the booking
        // TODO: see later if not better to keep the booking and just mark it as cancelled (just to keep a track)
        boolean isCancelled = (new BookingDatabaseHelper(this)).DeleteBooking(this.booking);

        // If the cancellation was successful
        if (isCancelled) {
            this.MakeToast(BOOKING_CANCELLED_SUCCESS);
            this.MyBookings();

            return;
        }

        this.MakeToast(BOOKING_CANCELLED_FAIL);
    }

    /**
     * Open the cancel booking popup
     */
    private void CreateCancelBookingPopup() {
        // Clear the current cancel booking popup context if any
        this.ClearCurrentCancelBookingPopupContext();

        // Create the new popup context
        this.CreateNewCancelBookingPopupContext();

        // Attach the appropriate events to the current popup
        this.SubscribeEventsPopup();

        // Display the popup
        this.currentCancelBookingPopup.showAtLocation(this.mainLayout, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * Clear the current cancel booking popup context
     */
    private void ClearCurrentCancelBookingPopupContext() {
        if (this.currentCancelBookingPopup != null) {
            this.currentCancelBookingPopup.dismiss();
            this.currentCancelBookingPopup = null;
        }

        if (this.popupClose != null) {
            this.popupDate = null;
            this.popupTime = null;
            this.popupDoctor = null;
            this.popupClose = null;
            this.discardBtn = null;
            this.confirmBtn = null;
        }

        if (this.popupExternalBackground != null) this.popupExternalBackground = null;
        if (this.popupContentLayout != null) this.popupContentLayout = null;
    }

    /**
     * Create a new cancel booking popup context
     */
    private void CreateNewCancelBookingPopupContext() {
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = getLayoutInflater();
        int popupLayout = R.layout.popup_show_booking_appointment_cancel_appointment_layout;
        View popupSampleView = inflater.inflate(popupLayout, null);

        // Create the popup window
        this.currentCancelBookingPopup = new PopupWindow(popupSampleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.popupClose = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_close);
        this.popupExternalBackground = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_external_background);
        this.popupContentLayout = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_content_layout);
        this.discardBtn = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_discard_btn);
        this.confirmBtn = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_confirm_btn);
        this.popupDate = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_content_appointment_date);
        this.popupTime = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_content_appointment_time);
        this.popupDoctor = popupSampleView.findViewById(R.id.show_booking_appointment_cancel_appointment_popup_content_doctor_fullname);

        // Set popup dynamic content
        this.SetPopupContent();
    }

    /**
     * Attach the appropriate events to the current cancel booking popup components
     */
    private void SubscribeEventsPopup() {
        this.popupClose.setOnClickListener(this);
        this.popupExternalBackground.setOnClickListener(this);
        this.popupContentLayout.setOnClickListener(this);
        this.discardBtn.setOnClickListener(this);
        this.confirmBtn.setOnClickListener(this);
    }

    /**
     * Set popup dynamic content
     */
    private void SetPopupContent() {
        String fullDate = getString(R.string.show_booking_appointment_cancel_appointment_popup_content_appointment_date_prefix) + this.booking.getFullDate();
        this.popupDate.setText(fullDate);

        String time = getString(R.string.show_booking_appointment_cancel_appointment_popup_content_appointment_time_prefix) + this.booking.getTime();
        this.popupTime.setText(time);

        String doctor = getString(R.string.show_booking_appointment_cancel_appointment_popup_content_doctor_fullname_prefix) + this.booking.GetDoctorFullname();
        this.popupDoctor.setText(doctor);
    }

    /**
     * Show a toast after a specific action
     * @param action The action that was performed
     */
    private void MakeToast(int action) {
        Context context = getApplicationContext();
        CharSequence content;
        Toast toast;
        int duration = Toast.LENGTH_LONG;

        switch (action) {
            case BOOKING_CANCELLED_SUCCESS:
                content = this.getResources().getString(R.string.show_booking_toast_booking_cancelled_success_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case BOOKING_CANCELLED_FAIL:
                content = this.getResources().getString(R.string.show_booking_toast_booking_cancelled_fail_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case BOOKING_UPDATED_SUCCESS:
                content = this.getResources().getString(R.string.show_booking_toast_booking_updated_success_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case BOOKING_UPDATED_FAIL:
                content = this.getResources().getString(R.string.show_booking_toast_booking_updated_fail_content);
                toast = Toast.makeText(context, content, duration);
                toast.show();
        }
    }

    /**
     * Start MyBookings activity
     */
    private void MyBookings() {
        // Create the intent
        Intent i = new Intent(ShowBooking.this, MyBookingsActivity.class);

        // Put extra parameters
        // The search bar content
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Remove the booking from the extra params
        key = this.getResources().getString(R.string.intent_booking);
        i.removeExtra(key);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start Main activity
     */
    private void Home() {
        // Create the intent
        Intent i = new Intent(ShowBooking.this, MainActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Show the appointment doctor's profile
     */
    private void ShowDoctorProfile() {
        // Create the intent
        Intent i = new Intent(ShowBooking.this, DoctorProfileActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_doctor);
        i.putExtra(key, this.doctor);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
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
    }
}
