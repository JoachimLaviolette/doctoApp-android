package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Services.DateTimeService;

public class ConfirmAppointmentActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Doctor doctor;
    private Patient patient;
    private Booking booking;

    private TextView appointmentDay;
    private TextView appointmentTime;

    private ImageView doctorPicture;
    private TextView doctorFullname;
    private TextView doctorSpeciality;
    private TextView doctorChevron;

    private ImageView patientPicture;
    private TextView patientFullname;

    private TextView warningMessage;
    private TextView contactNumber;

    private TextView doctorAddress;

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
        this.appointmentDay = findViewById(R.id.appointment_summary_fullday);
        this.appointmentTime = findViewById(R.id.appointment_summary_time);
        this.doctorPicture = findViewById(R.id.appointment_summary_doctor_picture);
        this.doctorFullname = findViewById(R.id.appointment_summary_doctor_fullname);
        this.doctorSpeciality = findViewById(R.id.appointment_summary_doctor_speciality);
        this.doctorChevron = findViewById(R.id.appointment_summary_chevron);
        this.patientPicture = findViewById(R.id.appointment_summary_patient_picture);
        this.patientFullname = findViewById(R.id.appointment_summary_patient_fullname);
        this.doctorAddress = findViewById(R.id.appointment_summary_address_content);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.doctorChevron.setOnClickListener(this);
    }

    /**
     * Retrieve the params sent by the ChooseDateTime view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        String key = this.getResources().getString(R.string.intent_booking);

        // Get the booking
        this.booking = (Booking) i.getExtras().getSerializable(key);

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
        this.appointmentDay.setText(this.booking.getDate());

        // Set appointment time
        this.appointmentTime.setText(this.booking.getTime());

        // Set doctor picture
        //this.doctorPicture.setImageResource(doctorService.GetDoctorPicture());

        // Set doctor fullname
        this.doctorFullname.setText(this.doctor.getFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(this.doctor.getSpeciality());

        // Set doctor address
        this.doctorAddress.setText(this.doctor.GetFullAddress());

        // Set patient fullname
        this.patientFullname.setText(this.patient.getFullname());
    }

    /**
     * Handle click events
     * @param v ConfirmAppointment view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.appointment_summary_chevron)
            this.ShowDoctorProfile();
    }

    /**
     * Show the appointment doctor's profile
     */
    private void ShowDoctorProfile() {
        // Create the intent
        Intent i = new Intent(ConfirmAppointmentActivity.this, DoctorProfileActivity.class);

        // Prepare the intent parameters
        String key = this.getResources().getString(R.string.intent_doctor);
        i.putExtra(key, this.doctor);

        // Start the activity
        startActivity(i);
    }
}
