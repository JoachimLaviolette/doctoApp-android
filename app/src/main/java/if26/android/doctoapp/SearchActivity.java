package if26.android.doctoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private EditText searchBar;
    private Button searchBtn;
    private ListView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.Instantiate();
        this.SubscribeEvents();
        this.RetrieveExtraParams();
        this.FillList();
    }

    private void Instantiate() {
        this.searchBar = findViewById(R.id.search_bar);
        this.searchBtn = findViewById(R.id.search_btn);
        this.searchList = findViewById(R.id.search_list);
    }

    private void SubscribeEvents() {
        this.searchBtn.setOnClickListener(this);
    }

    private void RetrieveExtraParams() {
        // Get the intent from Main activity
        Intent i = getIntent();

        // Create a bundle containing the extra params
        Bundle bundle = i.getExtras();

        // Retrieve the params
        String key = this.getResources().getString(R.string.main_intent_search);
        String searchContent = bundle.getString(key);

        // Insert the param in the search bar
        this.searchBar.setText(searchContent);

        if (searchContent.trim().isEmpty()) return;

        // If the search content was not empty, search for it
        this.Search();
    }

    private void FillList() {
        List<String[]> doctorsList = new ArrayList<>();
        doctorsList.add(new String[]{"Jerry Lombart", "Angiologue", "Toulon"});
        doctorsList.add(new String[]{"Serge Pernant", "Pédiatre", "Paris"});
        doctorsList.add(new String[]{"Chloé Chareyron", "Chirurgien", "Saint-Etienne"});

        ArrayList<Map<String,Object>> doctorsMapList = new ArrayList<>();

        for(int i =0; i < doctorsList.size(); i++) {
            Map<String,Object> doctorMap = new HashMap<>();
            doctorMap.put("search_list_item_user_picture", R.mipmap.ic_launcher);
            doctorMap.put("search_list_item_user_fullname", doctorsList.get(i)[0]);
            doctorMap.put("search_list_item_user_description", doctorsList.get(i)[1] + "\n" + doctorsList.get(i)[2]);
            doctorsMapList.add(doctorMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                doctorsMapList,
                R.layout.activity_search_list,
                new String[] {
                        "search_list_item_user_picture",
                        "search_list_item_user_fullname",
                        "search_list_item_user_description"
                },
                new int[] {
                        R.id.search_list_item_user_picture,
                        R.id.search_list_item_user_fullname,
                        R.id.search_list_item_user_description
                });

        this.searchList.setAdapter(simpleAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search_btn:
                this.Search();
                break;
            default:
                return;
        }
    }

    private void Search() {
        // TODO: Display the list of results
    }
}
