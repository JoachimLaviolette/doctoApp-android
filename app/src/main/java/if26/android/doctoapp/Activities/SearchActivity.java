package if26.android.doctoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.DatabaseHelpers.DoctorDatabaseHelper;
import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;
import if26.android.doctoapp.Models.Address;
import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Education;
import if26.android.doctoapp.Models.Experience;
import if26.android.doctoapp.Models.Language;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.PaymentOption;
import if26.android.doctoapp.Models.Reason;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.EncryptionService;
import if26.android.doctoapp.Services.RequestCode;

public class SearchActivity
        extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Patient loggedUser;

    private EditText searchBar;
    private Button searchBtn;
    private ListView searchList;
    private String searchContent;

    private static SharedPreferences sharedPrefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.Instantiate();
        this.SubscribeEvents();
        this.RetrieveExtraParams();
        this.SyncSearchBar();
        this.Search();

        // Create data in db
        // this.CreateDataDb();
    }

    /**
     * Method to test doctors db working
     */
    private void CreateDataDb() {
        this.CreateDoctor1();
        this.CreateDoctor2();
        this.CreateDoctor3();
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.getApplicationContext());
        this.CreatePatient(doctorDatabaseHelper.GetDoctors("Larrier").get(0));
    }
    private void CreateDoctor1() {
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.getApplicationContext());

        // doctor data
        String firstname = "Florence";
        String lastname = "Larrier";
        String email = "florence.larrier@gmail.com";
        String speciality = "Pediatrician";
        String description = "Specialized in child auscultation";
        boolean isUnderAgreement = true;
        boolean isHealthInsuranceCard = true;
        boolean isThirdPartyPayment = false;
        String lastLogin = DateTimeService.GetCurrentDateTime();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1("child" + pwdSalt);

        // address
        String city = "Lyon";
        String country = "France";
        String street1 = "9 boulevard des Belges";
        String street2 = "Secteur Fraissinette";
        String zip = "69000";
        Address address = new Address(-1, street1, street2, city, zip, country);

        // availabilities
        List<Availability> availabilitiesList = new ArrayList<>();
        Map<String,String[]> availabilities = new LinkedHashMap<>();
        availabilities.put("Monday", new String[] { "15:00", "15:30", "16:00", "18:00", "19:30" });
        availabilities.put("Thursday", new String[] { "08:00", "08:20", "09:00", "11:00" });
        availabilities.put("Friday", new String[] { "10:00", "14:00", "15:30" });

        for (String day: availabilities.keySet()) {
            for (String time: availabilities.get(day)) {
                Availability a = new Availability(null, day, time);
                availabilitiesList.add(a);
            }
        }

        // languages
        Set<Language> languagesList = new LinkedHashSet<>();
        languagesList.add(Language.FR);
        languagesList.add(Language.GER);
        languagesList.add(Language.IT);

        // payment options
        Set<PaymentOption> paymentOptionsList = new LinkedHashSet<>();
        paymentOptionsList.add(PaymentOption.CHEQUE);
        paymentOptionsList.add(PaymentOption.CREDIT_CARD);

        // reasons
        List<Reason> reasonsList = new ArrayList<>();
        reasonsList.add(new Reason(-1, null, "My child has pains"));
        reasonsList.add(new Reason(-1, null, "I want my child to be checked"));

        // trainings
        List<Education> trainingsList = new ArrayList<>();
        trainingsList.add(new Education(null, "2010", "Degree in pediatrics"));

        // experiences
        List<Experience> experiencesList = new ArrayList<>();
        experiencesList.add(new Experience(null, "2010", "9 years at Lyon Hospital"));

        Doctor doctor = new Doctor(
                -1,
                lastname,
                firstname,
                speciality,
                email,
                description,
                pwd,
                pwdSalt,
                isUnderAgreement,
                isHealthInsuranceCard,
                isThirdPartyPayment,
                address,
                lastLogin,
                availabilitiesList,
                languagesList,
                paymentOptionsList,
                reasonsList,
                trainingsList,
                experiencesList
        );

        doctorDatabaseHelper.CreateDoctor(doctor);
    }
    private void CreateDoctor2() {
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.getApplicationContext());

        // doctor data
        String firstname = "Lyssana";
        String lastname = "Vistorky";
        String email = "lyssana.vistorky@gmail.com";
        String speciality = "Dentist";
        String description = "Specialized in teeth surgery";
        boolean isUnderAgreement = false;
        boolean isHealthInsuranceCard = false;
        boolean isThirdPartyPayment = false;
        String lastLogin = DateTimeService.GetCurrentDateTime();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1("test" + pwdSalt);

// address
        String city = "Bordeaux";
        String country = "France";
        String street1 = "3 rue de la place";
        String street2 = "";
        String zip = "33000";
        Address address = new Address(-1, street1, street2, city, zip, country);

// availabilities
        List<Availability> availabilitiesList = new ArrayList<>();
        Map<String,String[]> availabilities = new LinkedHashMap<>();
        availabilities.put("Monday", new String[] { "15:00", "15:30", "16:00", "18:00", "19:30" });
        availabilities.put("Tuesday", new String[] { "11:00", "12:30", "14:00" });
        availabilities.put("Wednesday", new String[] { "16:00", "17:00" });
        availabilities.put("Thursday", new String[] { "08:00", "08:20", "09:00", "11:00" });
        availabilities.put("Friday", new String[] { "10:00", "14:00", "15:30" });

        for (String day: availabilities.keySet()) {
            for (String time: availabilities.get(day)) {
                Availability a = new Availability(null, day, time);
                availabilitiesList.add(a);
            }
        }

// languages
        Set<Language> languagesList = new LinkedHashSet<>();
        languagesList.add(Language.FR);

// payment options
        Set<PaymentOption> paymentOptionsList = new LinkedHashSet<>();
        paymentOptionsList.add(PaymentOption.CASH);

// reasons
        List<Reason> reasonsList = new ArrayList<>();
        reasonsList.add(new Reason(-1, null, "I have a decay"));
        reasonsList.add(new Reason(-1, null, "I want to do a descaling"));
        reasonsList.add(new Reason(-1, null, "I want to have my wisdom teeth removed"));

// trainings
        List<Education> trainingsList = new ArrayList<>();
        trainingsList.add(new Education(null, "2005", "Degree in dentistry"));

// experiences
        List<Experience> experiencesList = new ArrayList<>();
        experiencesList.add(new Experience(null, "2005", "4 years at Paris Hospital"));
        experiencesList.add(new Experience(null, "2009", "Bordeaux Dentistry Clinic"));

        Doctor doctor = new Doctor(
                -1,
                lastname,
                firstname,
                speciality,
                email,
                description,
                pwd,
                pwdSalt,
                isUnderAgreement,
                isHealthInsuranceCard,
                isThirdPartyPayment,
                address,
                lastLogin,
                availabilitiesList,
                languagesList,
                paymentOptionsList,
                reasonsList,
                trainingsList,
                experiencesList
        );

        doctorDatabaseHelper.CreateDoctor(doctor);
    }
    private void CreateDoctor3() {
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.getApplicationContext());

        String firstname = "Chloé";
        String lastname = "Laviolette";
        String email = "chloe.laviolette@gmail.com";
        String speciality = "Cardiologist";
        String description = "Specialized in heart surgery";
        boolean isUnderAgreement = true;
        boolean isHealthInsuranceCard = true;
        boolean isThirdPartyPayment = true;
        String lastLogin = DateTimeService.GetCurrentDateTime();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1("hello" + pwdSalt);

// address
        String city = "Saint-Etienne";
        String country = "France";
        String street1 = "18 rue Emile Zola";
        String street2 = "";
        String zip = "42230";
        Address address = new Address(-1, street1, street2, city, zip, country);

// availabilities
        List<Availability> availabilitiesList = new ArrayList<>();
        Map<String,String[]> availabilities = new LinkedHashMap<>();
        availabilities.put("Monday", new String[] { "15:00", "15:30", "16:00", "18:00", "19:30" });
        availabilities.put("Tuesday", new String[] { "11:00", "12:30", "14:00" });
        availabilities.put("Wednesday", new String[] { "16:00", "17:00" });
        availabilities.put("Thursday", new String[] { "08:00", "08:20", "09:00", "11:00" });
        availabilities.put("Friday", new String[] { "10:00", "14:00", "15:30" });
        availabilities.put("Saturday", new String[] { "09:00", "10:00", "10:30", "11:00" });
        availabilities.put("Sunday", new String[] { "09:00", "10:00", "10:30", "11:00" });

        for (String day: availabilities.keySet()) {
            for (String time: availabilities.get(day)) {
                Availability a = new Availability(null, day, time);
                availabilitiesList.add(a);
            }
        }

// languages
        Set<Language> languagesList = new LinkedHashSet<>();
        languagesList.add(Language.FR);
        languagesList.add(Language.EN);
        languagesList.add(Language.ES);
        languagesList.add(Language.IT);

// payment options
        Set<PaymentOption> paymentOptionsList = new LinkedHashSet<>();
        paymentOptionsList.add(PaymentOption.CASH);
        paymentOptionsList.add(PaymentOption.CHEQUE);
        paymentOptionsList.add(PaymentOption.CREDIT_CARD);

// reasons
        List<Reason> reasonsList = new ArrayList<>();
        reasonsList.add(new Reason(-1, null, "I feel pains around my heart"));
        reasonsList.add(new Reason(-1, null, "I want to make a check up"));
        reasonsList.add(new Reason(-1, null, "I have difficulties for breathing"));

// trainings
        List<Education> trainingsList = new ArrayList<>();
        trainingsList.add(new Education(null, "1997", "Certificate in Medicine"));
        trainingsList.add(new Education(null, "2000", "BSc Medicine"));
        trainingsList.add(new Education(null, "2002", "MSc Medicine"));

// experiences
        List<Experience> experiencesList = new ArrayList<>();
        experiencesList.add(new Experience(null, "2002", "2 years at Saint-Etienne Hospital"));
        experiencesList.add(new Experience(null, "2004", "4 years at Saint-Victor sur Loire Hospital"));
        experiencesList.add(new Experience(null, "2008", "Berland Hospital"));

        Doctor doctor = new Doctor(
                -1,
                lastname,
                firstname,
                speciality,
                email,
                description,
                pwd,
                pwdSalt,
                isUnderAgreement,
                isHealthInsuranceCard,
                isThirdPartyPayment,
                address,
                lastLogin,
                availabilitiesList,
                languagesList,
                paymentOptionsList,
                reasonsList,
                trainingsList,
                experiencesList
        );

        doctorDatabaseHelper.CreateDoctor(doctor);
    }
    private void CreatePatient(Doctor doctor) {
        PatientDatabaseHelper patientDbHelper = new PatientDatabaseHelper(this.getApplicationContext());
        // doctor data
        String lastname = "Coppens";
        String firstname = "Ewen";
        String birthdate = "1996-01-01";
        String email = "ewen.coppens@gmail.com";
        String insuranceNumber = "2 18 15 62 489 658 27";
        String lastLogin = DateTimeService.GetCurrentDateTime();
        String pwdSalt = EncryptionService.SALT();
        String pwd = EncryptionService.SHA1("world" + pwdSalt);

        // address
        String city = "Paris";
        String country = "France";
        String street1 = "23 Blvd Haussman";
        String street2 = "Bat 2B Bte 9 4e étage";
        String zip = "750015";
        Address address = new Address(-1, street1, street2, city, zip, country);

        // appointments
        Booking a1 = new Booking(null, doctor, doctor.getReasons().get(0), doctor.getAvailabilities().get(0).getDate(), doctor.getAvailabilities().get(0).getTime(), DateTimeService.GetCurrentDateTime());

        Set<Booking> appointmentsList = new LinkedHashSet<>();
        appointmentsList.add(a1);

        Patient patient = new Patient(
                -1,
                lastname,
                firstname,
                birthdate,
                email,
                pwd,
                pwdSalt,
                insuranceNumber,
                address,
                lastLogin,
                appointmentsList
        );

        patientDbHelper.CreatePatient(patient);
    }

    /**
     * Retrieve the view components references
     */
    private void Instantiate() {
        setTitle(R.string.title_search);
        sharedPrefences = this.getPreferences(Context.MODE_PRIVATE);
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
        // Get the logged user
        String key = this.getResources().getString(R.string.intent_logged_user);
        this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
        if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());

        // Get the search bar content
        key = this.getResources().getString(R.string.main_intent_search);
        this.searchContent = bundle.getString(key).trim();
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
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.getApplicationContext());
        List<Doctor> doctorsList = doctorDatabaseHelper.GetDoctors(this.searchContent);

        // Build the doctors list view procedurally
        this.BuildDoctorsListView(doctorsList);
    }

    /**
     * Fill the list view with the matching doctors
     * @param doctorsList The list of matching doctors
     */
    private void BuildDoctorsListView(List<Doctor> doctorsList) {
        ArrayList<Map<String, Object>> doctorsMapList = new ArrayList<>();

        // Keys
        String pictureKey = this.getResources().getString(R.string.doctor_service_doctor_picture);
        String fullnameKey = this.getResources().getString(R.string.doctor_service_doctor_fullname);
        String specialityKey = this.getResources().getString(R.string.doctor_service_doctor_speciality);
        String addressKey = this.getResources().getString(R.string.doctor_service_doctor_address);
        String doctorKey = this.getResources().getString(R.string.intent_doctor);
        String chevronKey = this.getResources().getString(R.string.search_list_item_chevron_label);

        for (Doctor doctor: doctorsList) {
            Map<String, Object> doctorMap = new LinkedHashMap<>();
            doctorMap.put(pictureKey, R.mipmap.ic_launcher);
            doctorMap.put(fullnameKey, doctor.getFullname());
            doctorMap.put(specialityKey, doctor.getSpeciality());
            doctorMap.put(addressKey, doctor.GetCityCountry());
            doctorMap.put(doctorKey, doctor);
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
     * @param v The view that has been clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn)
            this.Search();
    }

    /**
     * Handle action when clicking on one of the doctors of the list
     * @param parent
     * @param view Search view
     * @param position Clicked item position
     * @param id Clicked item id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create the intent
        Intent i = new Intent(SearchActivity.this, DoctorProfileActivity.class);

        // Prepare the intent parameters
        // The selected doctor
        String key = this.getResources().getString(R.string.intent_doctor);
        i.putExtra(key, (Serializable) ((LinkedHashMap<String, Object>) parent.getAdapter().getItem(position)).get(key));

        // The logged user
        key = this.getResources().getString(R.string.intent_logged_user);
        i.putExtra(key, this.loggedUser);

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
                this.loggedUser = bundle.containsKey(key) ? (Patient) bundle.getSerializable(key) : null;
                if (this.loggedUser != null) this.loggedUser = (Patient) this.loggedUser.Update(this.getApplicationContext());
            }
        }
    }
}
