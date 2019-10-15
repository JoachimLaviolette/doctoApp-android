package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.DoctorService;

public class ChooseDateTimeActivity
        extends AppCompatActivity
        implements AdapterView.OnClickListener {
    private Bundle doctor;
    private TextView doctorFullname;
    private GridLayout dateTimeListGlobalLayout;
    private static DoctorService doctorService;
    private static DateTimeService dateTimeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);

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
        this.doctorFullname = findViewById(R.id.choose_date_time_doctor_fullname);
        this.dateTimeListGlobalLayout = findViewById(R.id.date_time_list_global_layout);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {

    }

    /**
     * Retrieve the params sent by the DoctorProfileActivity view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        this.doctor = i.getExtras().getBundle(this.getResources().getString(R.string.search_intent_doctor));
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        this.doctorFullname.setText(doctorService.GetDoctorFullnameAsTitle());
        this.FillDateTimesList();
    }

    /**
     * Fill the date times list with the date times available
     */
    private void FillDateTimesList() {
        // Fetch the reasons
        Serializable dateTimesList = doctorService.GetDoctorHoursContacts();

        // Build the date times list view procedurally
        if (dateTimesList instanceof HashMap)
            this.BuildDateTimesListView((HashMap<String,Object>) dateTimesList);
    }

    /**
     * Fill the list view with the date times
     * @param dateTimesList The list of date times
     */
    private void BuildDateTimesListView(Map<String,Object> dateTimesList) {
        LayoutInflater inflater = getLayoutInflater();

        for (String d: dateTimesList.keySet()) {
            View dateTimeLayout = inflater.inflate(R.layout.date_time_datetime_layout, this.dateTimeListGlobalLayout, false);

            View day = inflater.inflate(R.layout.date_time_day, (LinearLayout) dateTimeLayout, false);
            ((TextView) day).setText(d);

            View timeLayout = inflater.inflate(R.layout.date_time_time_layout, (LinearLayout) dateTimeLayout, false);

            ((LinearLayout) dateTimeLayout).addView(day);

            Object hours = dateTimesList.get(d);

            if (hours instanceof String[]) {
                for (String hour : (String[]) hours) {
                    View time = inflater.inflate(R.layout.date_time_time, (GridLayout) timeLayout, false);
                    ((TextView) time).setText(hour);
                    time.setTag(dateTimeService.CreateTimeTag(hour, d));
                    time.setOnClickListener(this);
                    ((GridLayout) timeLayout).addView(time);
                }
            }

            ((LinearLayout) dateTimeLayout).addView(timeLayout);
            this.dateTimeListGlobalLayout.addView(dateTimeLayout);
        }
    }

    /**
     * Handle click events
     * @param v ChooseDataTime The view item that wa clicked
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
        // Create the intent
        Intent i = new Intent(ChooseDateTimeActivity.this, ConfirmAppointmentActivity.class);

        // Prepare date time data
        String timeTag = time.getTag().toString();

        // For some reason, a number is added at the end of the previously-built time tag
        // So we need to clean it ...
        timeTag = this.CleanTimeTag(timeTag);

        // Get datetime data from the time tag
        Map<String,String> dateData = dateTimeService.GetDateTimeDataFromTimeTag(timeTag);

        // Prepare doctor data with appointment date time information
        Map<String, Object> doctorData = new HashMap<>();
        doctorData.put(this.getResources().getString(R.string.doctor_service_doctor_hours_contacts), dateData);

        // Complete the doctor bundle adding appointment date time data
        this.doctor = doctorService.GetDoctorAsBundle(doctorData);
        i.putExtra(this.getResources().getString(R.string.search_intent_doctor), this.doctor);

        // Start the activity
        startActivity(i);
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
}
