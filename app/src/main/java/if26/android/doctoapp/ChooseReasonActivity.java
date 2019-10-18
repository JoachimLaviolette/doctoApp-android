package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Reason;

public class ChooseReasonActivity
        extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private Doctor doctor;
    private Booking booking;

    private TextView doctorFullname;
    private ListView reasonsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reason);

        this.RetrieveExtraParams();
        this.Instantiate();
        this.SubscribeEvents();
        this.SetContent();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        this.doctorFullname = findViewById(R.id.choose_reason_doctor_fullname);
        this.reasonsList = findViewById(R.id.reasons_list);
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.reasonsList.setOnItemClickListener(this);
    }

    /**
     * Retrieve the params sent by the DoctorProfileActivity view
     */
    private void RetrieveExtraParams() {
        Intent i = getIntent();
        String key = this.getResources().getString(R.string.intent_booking);

        // Get the booking
        this.booking = (Booking) i.getExtras().getSerializable(key);

        // Get the doctor
        this.doctor = this.booking.getDoctor();
        this.doctor = (Doctor) this.doctor.Update(this.getApplicationContext());
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        this.doctorFullname.setText(this.doctor.getFullname());
        this.FillReasonsList();
    }

    /**
     * Fill the reasons list with the reasons available
     */
    private void FillReasonsList() {
        // Fetch the reasons
        List<Reason> reasonsList = this.doctor.getReasons();

        // Build the reasons list view procedurally
        this.BuildReasonsListView(reasonsList);
    }

    /**
     * Fill the list view with the reasons
     * @param reasonsList The list of reasons
     */
    private void BuildReasonsListView(List<Reason> reasonsList) {
        ArrayList<Map<String,Object>> reasonsMapList = new ArrayList<>();

        // Keys
        String reasonItemKey = this.getResources().getString(R.string.doctor_service_doctor_reason);
        String reasonKey = this.getResources().getString(R.string.intent_reason);

        for (Reason r: reasonsList) {
            Map<String,Object> reasonMap = new LinkedHashMap<>();
            reasonMap.put(reasonItemKey, r.getDescription());
            reasonMap.put(reasonKey, r);
            reasonsMapList.add(reasonMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
            this,
            reasonsMapList,
            R.layout.reason_list,
            new String[] { reasonItemKey },
            new int[] { R.id.reason_list_item_reason }
        );

        this.reasonsList.setAdapter(simpleAdapter);
    }

    /**
     * Handle action when clicking on one of the reasons of the list
     * @param parent
     * @param view Choose reason view
     * @param position Clicked item position
     * @param id Clicked item id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create the intent
        Intent i = new Intent(ChooseReasonActivity.this, ChooseAvailabilityActivity.class);

        // Prepare the intent parameters
        String key = this.getResources().getString(R.string.intent_reason);
        Reason r = (Reason) ((LinkedHashMap<String, Object>) parent.getAdapter().getItem(position)).get(key);
        this.booking.setReason(r);
        this.booking.setDoctor(this.doctor);
        key = this.getResources().getString(R.string.intent_booking);
        i.putExtra(key, this.booking);

        // Start the activity
        startActivity(i);
    }
}