package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Codes.RequestCode;
import if26.android.doctoapp.DatabaseHelpers.BookingDatabaseHelper;
import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;

public class ChooseAvailabilityActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    private Doctor doctor;
    private Booking booking;
    private Resident loggedUser;

    private ImageButton homeBtn;
    private ImageButton dashboardBtn;

    private TextView doctorFullname;
    private GridLayout dateTimeListGlobalLayout;

    private static DateTimeService dateTimeService;

    private boolean isBookingUpdate;

    private static int WEEKS_NUMBER = 1;

    private static final int BOOKING_UPDATED_SUCCESS = 0;
    private static final int BOOKING_UPDATED_ERROR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_availability);

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
        this.homeBtn = findViewById(R.id.home);
        this.dashboardBtn = findViewById(R.id.dashboard);
        this.doctorFullname = findViewById(R.id.choose_date_time_doctor_fullname);
        this.dateTimeListGlobalLayout = findViewById(R.id.date_time_list_global_layout);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.homeBtn.setOnClickListener(this);
        this.dashboardBtn.setOnClickListener(this);
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
        this.loggedUser = bundle.containsKey(key) ?
                        bundle.getSerializable(key) instanceof Patient ?
                            (Patient) bundle.getSerializable(key) :
                                (Doctor) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = this.loggedUser.Update(this.getApplicationContext());

        // Get the booking
        key = this.getResources().getString(R.string.intent_booking);
        this.booking = (Booking) bundle.getSerializable(key);
        this.isBookingUpdate = this.booking.getDate() != null;

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
        Map<String, List<Availability>> availabilitiesPerDay;

        if (!this.isBookingUpdate)
            // Fetch the availabilities
            availabilitiesPerDay = this.doctor.getAvailabilitiesPerDay(this.WEEKS_NUMBER);
        else
            // Fetch the availabilities for the booking day
            availabilitiesPerDay = this.doctor.getAvailabilitiesForDay(this.booking.getFullDate());

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
            View dateTimeLayout = inflater.inflate(R.layout.template_date_time_datetime_layout, this.dateTimeListGlobalLayout, false);

            View day = inflater.inflate(R.layout.template_date_time_day, (LinearLayout) dateTimeLayout, false);
            ((TextView) day).setText(d);

            View timeLayout = inflater.inflate(R.layout.template_date_time_time_layout, (LinearLayout) dateTimeLayout, false);

            ((LinearLayout) dateTimeLayout).addView(day);

            List<Availability> availabilities = availabilitiesPerDay.get(d);

            for (Availability a: availabilities) {
                String hour = a.getTime();
                View time = inflater.inflate(R.layout.template_date_time_time, (GridLayout) timeLayout, false);
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
        if (v.getId() == R.id.home) { this.Home();return; }
        if (v.getId() == R.id.dashboard) { this.Dashboard();return; }

        if (v instanceof TextView) {
            if (!this.isBookingUpdate) this.ConfirmAppointment((TextView) v);
            else this.ConfirmBookingUpdate((TextView) v);
        }
    }

    /**
     * Start Main activity
     */
    private void Home() {
        // Create the intent
        Intent i = new Intent(ChooseAvailabilityActivity.this, MainActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start Dashboard activity
     */
    private void Dashboard() {
        // Create the intent
        Intent i = new Intent(ChooseAvailabilityActivity.this, LoginActivity.class);

        // Prepare the intent parameters
        // The doctor
        String key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Start the activity
        startActivityForResult(i, RequestCode.LOGGED_PATIENT);
    }

    /**
     * Start ConfirmAppointment activity
     * @param time The time that was chosen
     */
    public void ConfirmAppointment(TextView time) {
        // Prepare date time data
        String timeTag = time.getTag().toString();

        // Get datetime data from the time tag
        Map<String,String> dateData = dateTimeService.GetDateTimeDataFromTimeTag(timeTag);

        // Create the intent
        Intent i = new Intent(ChooseAvailabilityActivity.this, ConfirmAppointmentActivity.class);

        // Prepare the intent parameters
        // The completed booking
        String key = this.getResources().getString(R.string.intent_booking);
        this.booking.setFullDate((new DateTimeService(this).GetFullDateFromData(dateData)));
        this.booking.setDate((new DateTimeService(this).GetDateFromData(dateData)));
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
     * Confirm booking update
     * @param time The time that was chosen
     */
    public void ConfirmBookingUpdate(TextView time) {
        // Prepare date time data
        String timeTag = time.getTag().toString();

        // Get datetime data from the time tag
        Map<String,String> dateData = dateTimeService.GetDateTimeDataFromTimeTag(timeTag);

        // Create the intent
        Intent i = new Intent(ChooseAvailabilityActivity.this, MyBookingsActivity.class);

        // Prepare the intent parameters
        // The completed booking
        String key = this.getResources().getString(R.string.intent_booking);
        this.booking.setTime(dateData.get(this.getResources().getString(R.string.date_service_time)));
        String oldBookingDate = this.booking.getBookingDate(); // Todo : we should use a booking id for update rather than processing like this
        this.booking.setBookingDate(DateTimeService.GetCurrentDateTime());
        i.putExtra(key, this.booking);

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

        // Notify the user the update has been done
        if ((new BookingDatabaseHelper(this).UpdateBooking(this.booking, oldBookingDate))) this.MakeToast(BOOKING_UPDATED_SUCCESS);
        else this.MakeToast(BOOKING_UPDATED_ERROR);

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

    /**
     * Show a toast after one action
     * @param action The action performed
     */
    private void MakeToast(int action) {
        Context context = getApplicationContext();
        CharSequence content;
        Toast toast;
        int duration = Toast.LENGTH_LONG;

        switch (action) {
            case BOOKING_UPDATED_SUCCESS:
                content = this.getResources().getString(R.string.choose_date_time_toast_booking_updated_success);
                toast = Toast.makeText(context, content, duration);
                toast.show();

                return;
            case BOOKING_UPDATED_ERROR:
                content = this.getResources().getString(R.string.choose_date_time_toast_booking_updated_error);
                toast = Toast.makeText(context, content, duration);
                toast.show();
        }
    }
}
