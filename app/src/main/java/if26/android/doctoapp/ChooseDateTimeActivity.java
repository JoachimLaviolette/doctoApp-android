package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChooseDateTimeActivity extends AppCompatActivity {
    private Bundle doctor;
    private TextView doctorFullname;
    private static DoctorService doctorService;
    private GridLayout dateTimeListGlobalLayout;

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
        this.doctorFullname = findViewById(R.id.choose_date_time_doctor_fullname);
        this.dateTimeListGlobalLayout = findViewById(R.id.date_time_list_global_layout);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {

    }

    /**
     * Retrieve the params sent by the DoctorProfile view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        this.doctor = i.getExtras().getBundle(this.getResources().getString(R.string.doctor_service_bundle_key_doctor));
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
                    ((GridLayout) timeLayout).addView(time);
                }
            }

            ((LinearLayout) dateTimeLayout).addView(timeLayout);
            this.dateTimeListGlobalLayout.addView(dateTimeLayout);
        }
    }
}
