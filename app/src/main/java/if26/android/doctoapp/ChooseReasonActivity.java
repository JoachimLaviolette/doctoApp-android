package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseReasonActivity extends AppCompatActivity {
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
        this.FillReasonsList();
    }

    /**
     * Fill the reasons list with the reasons available
     */
    private void FillReasonsList() {
        // Fetch the reasons
        Serializable reasonsList = doctorService.GetDoctorReasons();

        // Build the doctors list view procedurally
        if (reasonsList instanceof List)
            this.BuildReasonsListView((ArrayList<String[]>) reasonsList);
    }

    /**
     * Fill the list view with the matching doctors
     * @param reasonsList The list of matching doctors
     */
    private void BuildReasonsListView(List<String[]> reasonsList) {
        ArrayList<Map<String,Object>> reasonsMapList = new ArrayList<>();

        // Keys
        String reasonKey = this.getResources().getString(R.string.doctor_service_doctor_reason);

        for(int i = 0; i < reasonsList.size(); ++i) {
            Map<String,Object> reasonMap = new HashMap<>();
            reasonMap.put(reasonKey, reasonsList.get(i)[0]);
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
}
