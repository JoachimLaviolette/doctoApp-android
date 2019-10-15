package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Services.DoctorService;

public class ChooseReasonActivity
        extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private Bundle doctor;
    private TextView doctorFullname;
    private ListView reasonsList;
    private static DoctorService doctorService;

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
        doctorService = new DoctorService(this, this.doctor);
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
        this.doctor = i.getExtras().getBundle(this.getResources().getString(R.string.search_intent_doctor));
    }

    /**
     * Set content procedurally
     */
    private void SetContent() {
        this.doctorFullname.setText(doctorService.GetDoctorFullnameAsTitle());
        this.FillReasonsList();
    }

    /**
     * Fill the reasons list with the reasons available
     */
    private void FillReasonsList() {
        // Fetch the reasons
        Serializable reasonsList = doctorService.GetDoctorReasons();

        // Build the reasons list view procedurally
        if (reasonsList instanceof ArrayList)
            this.BuildReasonsListView((ArrayList<Object>) reasonsList);
    }

    /**
     * Fill the list view with the reasons
     * @param reasonsList The list of reasons
     */
    private void BuildReasonsListView(List<Object> reasonsList) {
        ArrayList<Map<String,Object>> reasonsMapList = new ArrayList<>();

        // Keys
        String reasonKey = this.getResources().getString(R.string.doctor_service_doctor_reason);

        for(int i = 0; i < reasonsList.size(); ++i) {
            Map<String,Object> reasonMap = new HashMap<>();
            reasonMap.put(reasonKey, reasonsList.get(i));
            reasonsMapList.add(reasonMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
            this,
            reasonsMapList,
            R.layout.reason_list,
            new String[] { reasonKey },
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
        Intent i = new Intent(ChooseReasonActivity.this, ChooseDateTimeActivity.class);

        // Prepare the intent parameters
        String key = this.getResources().getString(R.string.search_intent_doctor);
        Map<String, Object> doctorData = (HashMap<String,Object>) parent.getAdapter().getItem(position);

        // Complete the doctor bundle adding reason data
        this.doctor = doctorService.GetDoctorAsBundle(doctorData);
        i.putExtra(key, this.doctor);

        // Start the activity
        startActivity(i);
    }
}
