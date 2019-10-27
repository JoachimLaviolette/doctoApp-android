package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.DatabaseHelpers.BookingDatabaseHelper;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.RequestCode;

public class ConfirmAppointmentActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Doctor doctor;
    private Patient patient;
    private Booking booking;
    private Patient loggedUser;

    private LinearLayout confirmAppointmentSummary;
    private LinearLayout confirmAppointmentMsg;

    private TextView confirmAppointmentMsgTitle;
    private TextView confirmAppointmentMsgContent;

    private TextView appointmentDay;
    private TextView appointmentTime;

    private ImageView doctorPicture;
    private TextView doctorFullname;
    private TextView doctorSpeciality;
    private TextView doctorChevron;

    private TextView appointmentReason;

    private ImageView patientPicture;
    private TextView patientFullname;

    private TextView warningMessage;

    private TextView doctorContactNumber;

    private TextView doctorAddress;

    private Button seeMyBookings;

    private static DateTimeService dateTimeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        dateTimeService = new DateTimeService(this);
        this.confirmAppointmentSummary = findViewById(R.id.confirm_appointment_summary);
        this.confirmAppointmentMsg = findViewById(R.id.confirm_appointment_msg);
        this.confirmAppointmentMsgTitle = findViewById(R.id.confirm_appointment_msg_title);
        this.confirmAppointmentMsgContent = findViewById(R.id.confirm_appointment_msg_content);
        this.appointmentDay = findViewById(R.id.appointment_summary_fullday);
        this.appointmentTime = findViewById(R.id.appointment_summary_time);
        this.doctorPicture = findViewById(R.id.appointment_summary_doctor_picture);
        this.doctorFullname = findViewById(R.id.appointment_summary_doctor_fullname);
        this.doctorSpeciality = findViewById(R.id.appointment_summary_doctor_speciality);
        this.doctorChevron = findViewById(R.id.appointment_summary_chevron);
        this.appointmentReason = findViewById(R.id.appointment_summary_reason);
        this.patientPicture = findViewById(R.id.appointment_summary_patient_picture);
        this.patientFullname = findViewById(R.id.appointment_summary_patient_fullname);
        this.doctorContactNumber = findViewById(R.id.appointment_summary_contact_number);
        this.doctorAddress = findViewById(R.id.appointment_summary_address_content);
        this.seeMyBookings = findViewById(R.id.confirm_appointment_see_my_bookings);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.doctorChevron.setOnClickListener(this);
        this.seeMyBookings.setOnClickListener(this);
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
        this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());

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
        if ((new BookingDatabaseHelper(this.getApplicationContext())).InsertAppointment(this.booking))
            this.SetSuccessContent();
        else
            this.SetErrorContent();
    }

    /**
     * Set the content in case the booking has successfully been created
     */
    private void SetSuccessContent() {
        // Set appointment full day
        this.appointmentDay.setText(this.booking.getDate());

        // Set appointment time
        this.appointmentTime.setText(this.booking.getTime());

        // Set doctor picture
        //this.doctorPicture.setImageResource(doctorService.GetDoctorPicture());

        // Set doctor fullname
        this.doctorFullname.setText(this.doctor.getFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(this.doctor.getSpeciality());

        // Set the reason
        this.appointmentReason.setText(this.booking.getReason().getDescription());

        // Set doctor contact number
        this.doctorContactNumber.setText(this.doctor.getContactNumberAsString());

        // Set doctor address
        this.doctorAddress.setText(this.doctor.GetFullAddress());

        // Set patient fullname
        this.patientFullname.setText(this.patient.getFullname());
    }

    /**
     * Set the content in case something went wrong with the booking
     */
    private void SetErrorContent() {
        this.confirmAppointmentSummary.setVisibility(View.GONE);
        this.confirmAppointmentMsg.setBackgroundResource(R.color.confirm_appointment_error_msg);
        this.confirmAppointmentMsgTitle.setText(this.getResources().getString(R.string.confirm_appointment_error_msg_title));
        this.confirmAppointmentMsgContent.setText(this.getResources().getString(R.string.confirm_appointment_error_msg_content));
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_summary_chevron:
                this.ShowDoctorProfile();

                return;
            case R.id.confirm_appointment_see_my_bookings:
                this.MyBookings();

                return;
        }
    }

    /**
     * Show the appointment doctor's profile
     */
    private void ShowDoctorProfile() {
        // Create the intent
        Intent i = new Intent(ConfirmAppointmentActivity.this, DoctorProfileActivity.class);

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

    /**
     * Start MyBookings activity
     */
    private void MyBookings() {
        // Create the intent
        Intent i = new Intent(ConfirmAppointmentActivity.this, MyBookingsActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_logged_user);
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
                this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
                if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());
            }
        }
    }
}
