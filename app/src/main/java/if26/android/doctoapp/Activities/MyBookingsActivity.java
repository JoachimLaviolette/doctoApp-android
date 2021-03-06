package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.ImageService;

public class MyBookingsActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Resident loggedUser;

    private TextView title;
    private GridLayout appointmentList;
    private LinearLayout noBookingMsg;
    private ImageButton homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.title = findViewById(R.id.my_bookings_title);
        this.appointmentList = findViewById(R.id.appointment_list);
        this.noBookingMsg = findViewById(R.id.my_bookings_no_booking_msg);
        this.homeBtn = findViewById(R.id.home);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.homeBtn.setOnClickListener(this);
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
     * Set view default content
     */
    private void SetContent() {
        if (this.loggedUser.getAppointments().size() > 0) {
            this.noBookingMsg.setVisibility(View.GONE);
            this.DisplayAppointments();
        }

        if (this.loggedUser instanceof Doctor) {
            this.title.setText(getString(R.string.my_bookings_title_doctor));
        }
    }

    /**
     * Display procedurally all the appointments of the logged user
     */
    private void DisplayAppointments() {
        if (this.loggedUser instanceof Doctor) this.DisplayAppointmentsForDoctor();
        else this.DisplayAppointmentsForPatient();
    }

    /**
     * Display appointments for doctor
     */
    private void DisplayAppointmentsForDoctor() {
        LayoutInflater inflater = this.getLayoutInflater();

        for (final Booking a: this.loggedUser.getAppointments()) {
            View appointmentItemLayout = inflater.inflate(R.layout.template_doctor_appointment_item, this.appointmentList, false);
            TextView fullDate = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_fulldate);
            TextView time = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_time);
            CircleImageView patientPicture = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_patient_picture);
            TextView patientFullname = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_patient_fullname);
            TextView patientBirthdate = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_patient_birthdate);
            TextView patientChevron = appointmentItemLayout.findViewById(R.id.doctor_appointment_item_chevron);

            // Set data
            fullDate.setText(a.getFullDate());
            time.setText(a.getTime());
            if (!a.getPatient().getPicture().isEmpty()) patientPicture.setImageURI(ImageService.GetURIFromPath(a.getPatient().getPicture()));
            patientFullname.setText(a.getPatient().getFullname());
            String birthdate = getString(R.string.show_booking_appointment_patient_birthdate_prefix) + a.getPatient().getBirthdate();
            patientBirthdate.setText(birthdate);

            // Set click event
            patientChevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowBooking(a);
                }
            });

            // Add the appointment item to the list of appointments
            this.appointmentList.addView(appointmentItemLayout);
        }
    }

    /**
     * Display appointments for patient
     */
    private void DisplayAppointmentsForPatient() {
        LayoutInflater inflater = this.getLayoutInflater();

        for (final Booking a: this.loggedUser.getAppointments()) {
            View appointmentItemLayout = inflater.inflate(R.layout.template_patient_appointment_item_compact , this.appointmentList, false);
            TextView fullDate = appointmentItemLayout.findViewById(R.id.patient_appointment_item_fulldate);
            TextView time = appointmentItemLayout.findViewById(R.id.patient_appointment_item_time);
            CircleImageView doctorPicture = appointmentItemLayout.findViewById(R.id.patient_appointment_item_doctor_picture);
            TextView doctorFullname = appointmentItemLayout.findViewById(R.id.patient_appointment_item_doctor_fullname);
            TextView doctorSpeciality = appointmentItemLayout.findViewById(R.id.patient_appointment_item_doctor_speciality);
            TextView doctorChevron = appointmentItemLayout.findViewById(R.id.patient_appointment_item_chevron);

            // Set data
            fullDate.setText(a.getFullDate());
            time.setText(a.getTime());
            if (!a.getDoctor().getPicture().isEmpty()) doctorPicture.setImageURI(ImageService.GetURIFromPath(a.getDoctor().getPicture()));
            doctorFullname.setText(a.getDoctor().getFullname());
            doctorSpeciality.setText(a.getDoctor().getSpeciality());

            // Set click event
            doctorChevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowBooking(a);
                }
            });

            // Add the appointment item to the list of appointments
            this.appointmentList.addView(appointmentItemLayout);
        }
    }

    /**
     * Show the details of the given booking
     * @param booking The booking to show the details of
     */
    private void ShowBooking(Booking booking) {
        // Create the intent
        Intent i = new Intent(MyBookingsActivity.this, ShowBooking.class);

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
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.home) {
            this.Home();
        }
    }

    /**
     * Start Main activity
     */
    private void Home() {
        // Create the intent
        Intent i = new Intent(MyBookingsActivity.this, MainActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }
}
