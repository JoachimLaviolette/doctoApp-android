package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Codes.RequestCode;

public class MyBookingsActivity
        extends AppCompatActivity {
    private Patient loggedUser;

    private GridLayout appointmentList;
    private LinearLayout noBookingMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        this.Instantiate();
        this.RetrieveExtraParams();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.loggedUser = null;

        this.appointmentList = findViewById(R.id.appointment_list);
        this.noBookingMsg = findViewById(R.id.my_bookings_no_booking_msg);
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
        this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());
    }

    /**
     * Set view default content
     */
    private void SetContent() {
        if (this.loggedUser.getAppointments().size() > 0) {
            this.noBookingMsg.setVisibility(View.GONE);
            this.DisplayAppointments();
        }
    }

    /**
     * Display procedurally all the appointments of the logged patient
     */
    private void DisplayAppointments() {
        LayoutInflater inflater = this.getLayoutInflater();

        for (final Booking a: this.loggedUser.getAppointments()) {
            View appointmentItemLayout = inflater.inflate(R.layout.appointment_item, this.appointmentList, false);
            TextView fullDay = appointmentItemLayout.findViewById(R.id.appointment_item_fullday);
            TextView time = appointmentItemLayout.findViewById(R.id.appointment_item_time);
            TextView doctorFullname = appointmentItemLayout.findViewById(R.id.appointment_item_doctor_fullname);
            TextView doctorSpeciality = appointmentItemLayout.findViewById(R.id.appointment_item_doctor_speciality);
            TextView doctorChevron = appointmentItemLayout.findViewById(R.id.appointment_item_chevron);
            TextView reason = appointmentItemLayout.findViewById(R.id.appointment_item_reason);
            TextView doctorContactNumber = appointmentItemLayout.findViewById(R.id.appointment_item_contact_number);
            TextView doctorAddress = appointmentItemLayout.findViewById(R.id.appointment_item_address_content);

            // Set data
            fullDay.setText(a.getDate());
            time.setText(a.getTime());
            doctorFullname.setText(a.getDoctor().getFullname());
            doctorSpeciality.setText(a.getDoctor().getSpeciality());
            reason.setText(a.getReason().getDescription());
            doctorContactNumber.setText(a.getDoctor().getContactNumberAsString());
            doctorAddress.setText(a.getDoctor().GetFullAddress());

            // Set click event
            doctorChevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDoctorProfile(a.getDoctor());
                }
            });

            // Add the appointment item to the list of appointments
            this.appointmentList.addView(appointmentItemLayout);
        }
    }

    /**
     * Show the appointment doctor's profile
     */
    private void ShowDoctorProfile(Doctor doctor) {
        // Create the intent
        Intent i = new Intent(MyBookingsActivity.this, DoctorProfileActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_doctor);
        i.putExtra(key, doctor);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }
}
