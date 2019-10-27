package if26.android.doctoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Codes.RequestCode;

public class ChooseAvailabilityActivity
        extends AppCompatActivity
        implements AdapterView.OnClickListener {
    private Doctor doctor;
    private Booking booking;
    private Patient loggedUser;

    private TextView doctorFullname;
    private GridLayout dateTimeListGlobalLayout;
    private static DateTimeService dateTimeService;

    private static int WEEKS_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_availability);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        dateTimeService = new DateTimeService(this);
        this.doctorFullname = findViewById(R.id.choose_date_time_doctor_fullname);
        this.dateTimeListGlobalLayout = findViewById(R.id.date_time_list_global_layout);
    }

    /**
     * Retrieve the params sent by the DoctorProfileActivity view
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
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        this.doctorFullname.setText(this.doctor.getFullname());
        this.FillDateTimesList();
    }

    /**
     * Fill the date times list with the date times available
     */
    private void FillDateTimesList() {
        // Fetch the availabilities
        Map<String, List<Availability>> availabilitiesPerDay = this.doctor.getAvailabilitiesPerDay(this.WEEKS_NUMBER);

        // Build the date times list view procedurally
        this.BuildDateTimesListView(availabilitiesPerDay);
    }

    /**
     * Fill the list view with the availabilities
     * @param availabilitiesPerDay The list of availabilities per day
     */
    private void BuildDateTimesListView(Map<String, List<Availability>> availabilitiesPerDay) {
        LayoutInflater inflater = this.getLayoutInflater();

        for (String d: availabilitiesPerDay.keySet()) {
            View dateTimeLayout = inflater.inflate(R.layout.date_time_datetime_layout, this.dateTimeListGlobalLayout, false);

            View day = inflater.inflate(R.layout.date_time_day, (LinearLayout) dateTimeLayout, false);
            ((TextView) day).setText(d);

            View timeLayout = inflater.inflate(R.layout.date_time_time_layout, (LinearLayout) dateTimeLayout, false);

            ((LinearLayout) dateTimeLayout).addView(day);

            List<Availability> availabilities = availabilitiesPerDay.get(d);

            for (Availability a: availabilities) {
                String hour = a.getTime();
                View time = inflater.inflate(R.layout.date_time_time, (GridLayout) timeLayout, false);
                ((Button) time).setText(hour);
                time.setTag(dateTimeService.CreateTimeTag(hour, a.getDate()));
                time.setOnClickListener(this);
                ((GridLayout) timeLayout).addView(time);
            }

            ((LinearLayout) dateTimeLayout).addView(timeLayout);
            this.dateTimeListGlobalLayout.addView(dateTimeLayout);
        }
    }

    /**
     * Handle click events
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        if (v instanceof TextView) this.ConfirmAppointment((TextView) v);
    }

    /**
     * Start ConfirmAppointment activity
     * @param time The time that was chosen
     */
    public void ConfirmAppointment(TextView time) {
        // Prepare date time data
        String timeTag = time.getTag().toString();

        // For some reason, a number is added at the end of the previously-built time tag
        // So we need to clean it ...
        timeTag = this.CleanTimeTag(timeTag);

        // Get datetime data from the time tag
        Map<String,String> dateData = dateTimeService.GetDateTimeDataFromTimeTag(timeTag);

        // Create the intent
        Intent i = new Intent(ChooseAvailabilityActivity.this, ConfirmAppointmentActivity.class);

        // Prepare the intent parameters
        // The completed booking
        String key = this.getResources().getString(R.string.intent_booking);
        this.booking.setDate((new DateTimeService(this.getApplicationContext()).GetFullDayFromData(dateData)));
        this.booking.setTime(dateData.get(this.getResources().getString(R.string.date_service_time)));
        this.booking.setBookingDate(DateTimeService.GetCurrentDateTime());
        i.putExtra(key, this.booking);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Clean the tag of a text view displaying a time
     * @param timeTag The time tag to clean
     * @return The time tag cleaned
     */
    private String CleanTimeTag(String timeTag) {
        if (!timeTag.contains(" ")) return timeTag;

        return timeTag.substring(0, timeTag.indexOf(" "));
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
