package if26.android.doctoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity
        extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText searchBar;
    private Button searchBtn;
    private ListView searchList;
    private String searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.Instantiate();
        this.SubscribeEvents();
        this.RetrieveExtraParams();
        this.SyncSearchBar();
        this.Search();
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        setTitle(R.string.title_search);
        this.searchBar = findViewById(R.id.search_bar);
        this.searchBtn = findViewById(R.id.search_btn);
        this.searchList = findViewById(R.id.search_list);
        this.searchContent = "";
    }

    /**
     * Listen to the events
     */
    private void SubscribeEvents() {
        this.searchBtn.setOnClickListener(this);
        this.searchList.setOnItemClickListener(this);
        this.ObserveSearchBar();
    }

    /**
     * Retrieve the extra params sent by the Main view
     */
    private void RetrieveExtraParams() {
        // Get the intent from Main activity
        Intent i = getIntent();

        // Create a bundle containing the extra params
        Bundle bundle = i.getExtras();

        // Retrieve the params
        String key = this.getResources().getString(R.string.main_intent_search);
        String searchContent = bundle.getString(key).trim();

        // Save the search content
        this.searchContent = searchContent;
    }

    /**
     * Synchronize the search bar content with the last search content
     */
    private void SyncSearchBar() {
        // Insert the param in the search bar
        this.searchBar.setText(searchContent);
    }

    /**
     * Synchronize the search content with the last content of the search bar
     * @param searchBarContent The search bar content
     */
    private void SyncSearchContent(String searchBarContent) {
        // Save the search content
        this.searchContent = searchBarContent;
    }

    /**
     * Run the search process to display the list of doctors
     */
    private void Search() {
        String searchBarContent = this.searchBar.getText().toString().trim();

        if (!this.searchContent.equals(searchBarContent))
            this.SyncSearchContent(searchBarContent);

        this.FillDoctorsList();
    }

    /**
     * Get the matching doctors and build the list of them on the view
     */
    private void FillDoctorsList() {
        // Fetch the matching doctors
        List<Object[]> doctorsList = this.FetchMatchingDoctors();

        // Build the doctors list view procedurally
        this.BuildDoctorsListView(doctorsList);
    }

    /**
     * Retrieve the doctors matching with the search content
     * @return The list of matching doctors
     */
    private List<Object[]> FetchMatchingDoctors() {
        List<Object[]> doctorsList = new ArrayList<>();

        // Reasons
        List<String[]> reasonsList = new ArrayList<>();
        reasonsList.add(new String[]{ "Reason 1" });
        reasonsList.add(new String[]{ "Reason 2" });
        reasonsList.add(new String[]{ "Reason 3" });
        reasonsList.add(new String[]{ "Reason 4" });
        reasonsList.add(new String[]{ "Reason 5" });
        reasonsList.add(new String[]{ "Reason 6" });
        reasonsList.add(new String[]{ "Reason 7" });
        reasonsList.add(new String[]{ "Reason 8" });

        // Get the doctors
        doctorsList.add(new Object[]{"Jerry Lombart", "Angiologue", "Toulon", reasonsList});
        doctorsList.add(new Object[]{"Serge Pernant", "Pédiatre", "Bordeaux", reasonsList});
        doctorsList.add(new Object[]{"Chloé Laviolette", "Chirurgien", "Saint-Etienne", reasonsList});
        doctorsList.add(new Object[]{"Joachim Laviolette", "Chirurgien", "Saint-Etienne", reasonsList});
        doctorsList.add(new Object[]{"David Zenon", "Podologue", "Ermont", reasonsList});
        doctorsList.add(new Object[]{"Hamza Mebarek", "ORL", "Epinay-sur-Seine", reasonsList});
        doctorsList.add(new Object[]{"Axel Luffy", "Dentiste", "Chambéry", reasonsList});

        // Filter the list of doctors
        return this.FilterDoctorsList(doctorsList);
    }

    /**
     * Filter the given list of doctors to match the ones
     * Matching the pattern @searchContent
     * @param doctorsList The list of doctors to filter
     * @return The list of doctors filtered
     */
    private List<Object[]> FilterDoctorsList(List<Object[]> doctorsList) {
        List<Object[]> matchingDoctorsList = new ArrayList<>();

        if (this.searchContent.isEmpty()) return matchingDoctorsList;

        for (Object[] doctor: doctorsList) {
            for (Object doctorAttribute: doctor) {
                if (doctorAttribute instanceof String) {
                    if (((String) doctorAttribute).toLowerCase().contains(this.searchContent.toLowerCase())
                            && !matchingDoctorsList.contains(doctor)) {
                        matchingDoctorsList.add(doctor);
                    }
                }
            }
        }

        return matchingDoctorsList;
    }

    /**
     * Fill the list view with the matching doctors
     * @param doctorsList The list of matching doctors
     */
    private void BuildDoctorsListView(List<Object[]> doctorsList) {
        ArrayList<Map<String,Object>> doctorsMapList = new ArrayList<>();

        // Keys
        String pictureKey = this.getResources().getString(R.string.doctor_service_doctor_picture);
        String fullnameKey = this.getResources().getString(R.string.doctor_service_doctor_fullname);
        String specialityKey = this.getResources().getString(R.string.doctor_service_doctor_speciality);
        String addressKey = this.getResources().getString(R.string.doctor_service_doctor_address);
        String reasonsKey = this.getResources().getString(R.string.doctor_service_doctor_reason);
        String chevronKey = this.getResources().getString(R.string.search_list_item_chevron_label);

        for(int i = 0; i < doctorsList.size(); ++i) {
            Map<String,Object> doctorMap = new HashMap<>();
            doctorMap.put(pictureKey, R.mipmap.ic_launcher);
            doctorMap.put(fullnameKey, doctorsList.get(i)[0]);
            doctorMap.put(specialityKey, doctorsList.get(i)[1]);
            doctorMap.put(addressKey, doctorsList.get(i)[2]);
            doctorMap.put(reasonsKey, doctorsList.get(i)[3]);
            doctorMap.put(chevronKey, getResources().getString(R.string.search_list_item_chevron));
            doctorsMapList.add(doctorMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                doctorsMapList,
                R.layout.search_list,
                new String[] {
                        pictureKey,
                        fullnameKey,
                        specialityKey,
                        addressKey,
                        chevronKey,
                },
                new int[] {
                        R.id.search_list_item_doctor_picture,
                        R.id.search_list_item_doctor_fullname,
                        R.id.search_list_item_doctor_speciality,
                        R.id.search_list_item_doctor_address,
                        R.id.search_list_item_chevron
                });

        this.searchList.setAdapter(simpleAdapter);
    }

    /**
     * Handle the actions to perform when the content of the search bar changes
     */
    private void ObserveSearchBar() {
        this.searchBar.addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    Search();
                }
            }
        );
    }

    /**
     * Handle click events
     * @param v The Search view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn)
            this.Search();
    }

    /**
     * Handle action when clicking on one of the users of the list
     * @param parent
     * @param view Search view
     * @param position Clicked item position
     * @param id Clicked item id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create the intent
        Intent i = new Intent(SearchActivity.this, DoctorProfile.class);

        // Prepare the intent parameters
        String key = this.getResources().getString(R.string.search_intent_doctor);
        Map<String, Object> doctorData = (HashMap<String, Object>) parent.getAdapter().getItem(position);

        DoctorService doctorService = new DoctorService(this);

        // To represent the doctor object we use a specific class in charge of formatting and creating
         // the doctor as a Bundle with all the data we'll need afterwards
        Bundle doctor = doctorService.GetDoctorAsBundle(doctorData);
        i.putExtra(key, doctor);

        // Start the activity
        startActivity(i);
    }
}
