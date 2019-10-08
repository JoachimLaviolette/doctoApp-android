package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorProfile
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button bookAppointmentBtn;
    private ImageView doctorPicture;
    private TextView
            doctorFullname,
            doctorSpeciality,
            doctorAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        this.Instantiate();
        this.SetContent();
        this.SubscribeEvents();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.bookAppointmentBtn = findViewById(R.id.doctor_profile_book_appointment);
        this.doctorPicture = findViewById(R.id.doctor_profile_picture);
        this.doctorFullname = findViewById(R.id.doctor_profile_fullname);
        this.doctorSpeciality = findViewById(R.id.doctor_profile_speciality);
        this.doctorAddress = findViewById(R.id.doctor_profile_address_content);
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        Intent i = getIntent();
        Bundle doctor = i.getExtras();
        DoctorService doctorService = new DoctorService(this);

        // Set doctor picture
        this.doctorPicture.setImageResource(doctorService.GetDoctorPicture(doctor));

        // Set doctor fullname
        this.doctorFullname.setText(doctorService.GetDoctorFullname(doctor));

        // Set doctor speciality
        this.doctorSpeciality.setText(doctorService.GetDoctorSpeciality(doctor));

        // Set doctor address
        this.doctorAddress.setText(doctorService.GetDoctorAddress(doctor));
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.bookAppointmentBtn.setOnClickListener(this);
    }

    /**
     * Handle click events
     * @param v The DoctorProfile view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doctor_profile_book_appointment:
                this.BookAppointment();

                return;
        }
    }

    /**
     * Start BookAppointment activity
     */
    private void BookAppointment() {

    }
}
