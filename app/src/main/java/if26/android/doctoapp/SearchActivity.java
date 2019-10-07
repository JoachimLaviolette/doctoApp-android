package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

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
    private static UserService userService;

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
        userService = new UserService(this);
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
     * Retrieve the params sent by the Main view
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
        List<String[]> doctorsList = this.FetchMatchingDoctors();

        // Build the doctors list view procedurally
        this.BuildDoctorsListView(doctorsList);
    }

    /**
     * Retrieve the doctors matching with the search content
     * @return The list of matching doctors
     */
    private List<String[]> FetchMatchingDoctors() {
        List<String[]> doctorsList = new ArrayList<>();

        // Get the doctors
        doctorsList.add(new String[]{"Jerry Lombart", "Angiologue", "Toulon"});
        doctorsList.add(new String[]{"Serge Pernant", "Pédiatre", "Bordeaux"});
        doctorsList.add(new String[]{"Chloé Laviolette", "Chirurgien", "Saint-Etienne"});
        doctorsList.add(new String[]{"Joachim Laviolette", "Chirurgien", "Saint-Etienne"});
        doctorsList.add(new String[]{"David Zenon", "Podologue", "Ermont"});
        doctorsList.add(new String[]{"Hamza Mebarek", "ORL", "Epinay-sur-Seine"});
        doctorsList.add(new String[]{"Axel Luffy", "Dentiste", "Chambéry"});

        // Filter the list of doctors
        return this.FilterDoctorsList(doctorsList);
    }

    /**
     * Filter the given list of doctors to match the ones
     * Matching the pattern @searchContent
     * @param doctorsList The list of doctors to filter
     * @return The list of doctors filtered
     */
    private List<String[]> FilterDoctorsList(List<String[]> doctorsList) {
        List<String[]> matchingDoctorsList = new ArrayList<>();

        if (this.searchContent.isEmpty()) return matchingDoctorsList;

        for (String[] doctor: doctorsList) {
            for (String doctorAttribute: doctor) {
                if (doctorAttribute.toLowerCase().contains(this.searchContent.toLowerCase())
                    && !matchingDoctorsList.contains(doctor)) {
                    matchingDoctorsList.add(doctor);
                }
            }
        }

        return matchingDoctorsList;
    }

    /**
     * Fill the list view with the matching doctors
     * @param doctorsList The list of matching doctors
     */
    private void BuildDoctorsListView(List<String[]> doctorsList) {
        ArrayList<Map<String,Object>> doctorsMapList = new ArrayList<>();

        for(int i = 0; i < doctorsList.size(); ++i) {
            Map<String,Object> doctorMap = new HashMap<>();
            doctorMap.put("search_list_item_user_picture", R.mipmap.ic_launcher);
            doctorMap.put("search_list_item_user_fullname", doctorsList.get(i)[0]);
            doctorMap.put("search_list_item_user_description", doctorsList.get(i)[1] + "\n" + doctorsList.get(i)[2]);
            doctorMap.put("search_list_item_chevron", getResources().getString(R.string.search_list_item_chevron));
            doctorsMapList.add(doctorMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                doctorsMapList,
                R.layout.activity_search_list,
                new String[] {
                        "search_list_item_user_picture",
                        "search_list_item_user_fullname",
                        "search_list_item_user_description",
                        "search_list_item_chevron"
                },
                new int[] {
                        R.id.search_list_item_user_picture,
                        R.id.search_list_item_user_fullname,
                        R.id.search_list_item_user_description,
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
        Map<String, Object> doctor = (HashMap<String, Object>) parent.getAdapter().getItem(position);

         // To represent the doctor object we use a specific class in charge of formatting and creating
         // the doctor as a Bundle with all the data we'll need afterwards
        Bundle user = userService.GetDoctorAsBundle(doctor);
        i.putExtra(key, user);

        // Start the activity
        startActivity(i);
    }
}
