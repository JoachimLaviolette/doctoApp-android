package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ConfirmAppointmentActivity
        extends AppCompatActivity {
    private Bundle doctor;

    private TextView appointmentDay;
    private TextView appointmentTime;

    private ImageView doctorPicture;
    private TextView doctorFullname;
    private TextView doctorSpeciality;

    private ImageView patientPicture;
    private TextView patientFullname;

    private TextView warningMessage;
    private TextView contactNumber;

    private TextView doctorAddress;

    private static DoctorService doctorService;
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
        doctorService = new DoctorService(this, this.doctor);
        dateTimeService = new DateTimeService(this);
        this.appointmentDay = findViewById(R.id.appointment_summary_fullday);
        this.appointmentTime = findViewById(R.id.appointment_summary_time);
        this.doctorPicture = findViewById(R.id.appointment_summary_doctor_picture);
        this.doctorFullname = findViewById(R.id.appointment_summary_doctor_fullname);
        this.doctorSpeciality = findViewById(R.id.appointment_summary_doctor_speciality);
        this.patientPicture = findViewById(R.id.appointment_summary_patient_picture);
        this.patientFullname = findViewById(R.id.appointment_summary_patient_fullname);
        this.doctorAddress = findViewById(R.id.appointment_summary_address_content);

    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {

    }

    /**
     * Retrieve the params sent by the ChooseDateTime view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        this.doctor = i.getExtras().getBundle(this.getResources().getString(R.string.search_intent_doctor));
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        Map<String,String> appointmentData = (HashMap) doctorService.GetAppointmentData();

        // Set appointment full day
        this.appointmentDay.setText(dateTimeService.GetFullDayFromData(appointmentData));

        // Set appointment time
        this.appointmentTime.setText(dateTimeService.GetTimeFromData(appointmentData));

        // Set doctor picture
        this.doctorPicture.setImageResource(doctorService.GetDoctorPicture());

        // Set doctor fullname
        this.doctorFullname.setText(doctorService.GetDoctorFullname());

        // Set doctor speciality
        this.doctorSpeciality.setText(doctorService.GetDoctorSpeciality());

        // Set doctor address
        this.doctorAddress.setText(doctorService.GetDoctorAddress());
    }
}
