package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;

public class DoctorDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public DoctorDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Create a new doctor in the database using the given Doctor model
     * @param doctor The doctor model
     * @return If the doctor has successfully been created in the database
     */
    public boolean CreateDoctor(Doctor doctor) {
        AddressDatabaseHelper addressDatabaseHelper = new AddressDatabaseHelper(this.context);
        AvailabilityDatabaseHelper availabilityDatabaseHelper = new AvailabilityDatabaseHelper(this.context);
        EducationDatabaseHelper educationDatabaseHelper = new EducationDatabaseHelper(this.context);
        ExperienceDatabaseHelper experienceDatabaseHelper = new ExperienceDatabaseHelper(this.context);
        LanguageDatabaseHelper languageDatabaseHelper = new LanguageDatabaseHelper(this.context);
        PaymentOptionDatabaseHelper paymentOptionDatabaseHelper = new PaymentOptionDatabaseHelper(this.context);
        ReasonDatabaseHelper reasonDatabaseHelper = new ReasonDatabaseHelper(this.context);
        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(this.context);

        // Insert the address of the doc in the DB
        // And save the address id
        doctor = (Doctor) addressDatabaseHelper.InsertAddress(doctor);

        if (doctor.GetAddressId() == -1) return false;

        // Insert the doctor in the DB
        // And save its id
        doctor = this.InsertDoctor(doctor);

        if (doctor.getId() == -1) return false;

        // Insert the availabilities of the doc in the DB
        doctor = availabilityDatabaseHelper.InsertAvailabilities(doctor);

        // TODO : check if any has not been added

        // Insert the trainings of the doc in the DB
        doctor = educationDatabaseHelper.InsertTrainings(doctor);

        // TODO : check if any has not been added

        // Insert the experiences of the doc in the DB
        doctor = experienceDatabaseHelper.InsertExperiences(doctor);

        // TODO : check if any has not been added

        // Insert the languages of the doc in the DB
        doctor = languageDatabaseHelper.InsertLanguages(doctor);

        // TODO : check if any has not been added

        // Insert the payment options of the doc in the DB
        doctor = paymentOptionDatabaseHelper.InsertPaymentOptions(doctor);

        // TODO : check if any has not been added

        // Insert the reasons of the doc in the DB
        doctor = reasonDatabaseHelper.InsertReasons(doctor);

        // TODO : check if any has not been added

        // Insert the appointments of the doc in the DB
        doctor = (Doctor) bookingDatabaseHelper.InsertAppointments(doctor);

        // TODO : check if any has not been added

        return (doctor.getId() != -1);
    }

    /**
     * Create a data struct to gather doctor information
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateDoctorData(Doctor doctor) {
        return new Object[] {
                doctor.getLastname(),
                doctor.getFirstname(),
                doctor.getSpeciality(),
                doctor.getEmail(),
                doctor.getPwd(),
                doctor.getPwdSalt(),
                doctor.getDescription(),
                doctor.getContactNumber(),
                doctor.isUnderAgreement(),
                doctor.isHealthInsuranceCard(),
                doctor.isThirdPartyPayment(),
                doctor.GetAddressId(),
                doctor.getLastLogin(),
                doctor.getPicture(),
                doctor.getHeader(),
        };
    }

    /**
     * Insert a new doctor in the database from the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    private Doctor InsertDoctor(Doctor doctor) {
        ContentValues doctorContentValues = new ContentValues();
        String[] doctorTableKeys = DoctoAppDatabaseContract.Doctor.TABLE_KEYS_INSERT;
        Object[] doctorData = this.CreateDoctorData(doctor);

        for (int i = 0; i < doctorTableKeys.length; i++) {
            if (doctorData[i] instanceof Long)
                doctorContentValues.put(doctorTableKeys[i], (Long) doctorData[i]);
            else
                doctorContentValues.put(doctorTableKeys[i], doctorData[i].toString());
        }

        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();
        long doctorId = database.insert(
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                null,
                doctorContentValues
        );

        doctor.setId(doctorId);

        return doctor;
    }

    /**
     * Get all the doctors matching with the needle
     * See the SQL query to see what fields are considered
     * @param needle The search needle
     * @return A list of matching doctors
     */
    public List<Doctor> GetDoctors(String needle) {
        if (needle.isEmpty()) return new ArrayList<>();

        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT %s.* " +
                        "FROM %s " +
                        "JOIN %s ON %s.%s = %s.%s " +
                        "WHERE %s.%s LIKE ? " +
                        "OR %s.%s LIKE ? " +
                        "OR %s.%s LIKE ? " +
                        "OR %s.%s LIKE ? " +
                        "OR %s.%s LIKE ?",
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ADDRESS,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_ID,
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.COLUMN_NAME_LASTNAME,
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.COLUMN_NAME_FIRSTNAME,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_CITY,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET1,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET2,
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_COUNTRY
        );

        String[] args = {
                "%" + needle + "%",
                "%" + needle + "%",
                "%" + needle + "%",
        };

        Cursor c = database.rawQuery(query, args);
        List<Doctor> doctors = this.BuildDoctorsList(c, false);
        c.close();

        return doctors;
    }

    /**
     * Build the list of doctors iterating on the given cursor
     * @param c Cursor pointing search query results
     * @param fromPatient If the request comes from patient building process
     * @return The list of matching doctors
     */
    private List<Doctor> BuildDoctorsList(Cursor c, boolean fromPatient) {
        List<Doctor> doctors = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> doctorData = new LinkedHashMap<>();

                // Get data from doctor table
                doctorData = this.GetDoctorTableData(doctorData, c);

                // Retrieve the current doctor id
                String doctorAddressId = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ADDRESS).toString();
                String doctorId = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ID).toString();

                // Get data from address table for the current doctor
                doctorData = this.GetAddressTableData(doctorData, doctorAddressId);

                // Get data from availability table for the current doctor
                doctorData = this.GetAvailabilityTableData(doctorData, doctorId);

                // Get data from language table for the current doctor
                doctorData = this.GetLanguageTableData(doctorData, doctorId);

                // Get data from payment_option table for the current doctor
                doctorData = this.GetPaymentOptionTableData(doctorData, doctorId);

                // Get data from reason table for the current doctor
                doctorData = this.GetReasonTableData(doctorData, doctorId);

                // Get data from education table for the current doctor
                doctorData = this.GetEducationTableData(doctorData, doctorId);

                // Get data from experience table for the current doctor
                doctorData = this.GetExperienceTableData(doctorData, doctorId);

                // Create the doctor object
                Doctor doctor = new Doctor(doctorData);

                // Get data from booking table for the current doctor
                if (!fromPatient) doctor = this.GetBookingTableData(doctor);

                // Add the doctor to the list
                doctors.add(doctor);
            } while (c.moveToNext());
        }

        return doctors;
    }

    /**
     * Get a doctor by id
     * @param doctorId The id of the doctor to retrieve
     * @param fromPatient If the request comes from patient building process
     * @return The matching doctor
     */
    public Doctor GetDoctorById(String doctorId, boolean fromPatient) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ID
        );

        String[] args = {
                doctorId
        };

        Cursor c = database.rawQuery(query, args);
        List<Doctor> doctors = this.BuildDoctorsList(c, fromPatient);
        Doctor doctor = doctors.isEmpty() ? null : doctors.get(0);
        c.close();

        return doctor;
    }

    /**
     * Get a doctor by email
     * @param email The email
     * @return The matching doctor if found, null otherwise
     */
    public Doctor GetDoctorByEmail(String email) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Doctor.TABLE_NAME,
                DoctoAppDatabaseContract.Doctor.COLUMN_NAME_EMAIL
        );

        String[] args = {
                email
        };

        Cursor c = database.rawQuery(query, args);
        List<Doctor> doctors = this.BuildDoctorsList(c, false);
        Doctor doctor = doctors.isEmpty() ? null : doctors.get(0);
        c.close();

        return doctor;
    }

    /**
     * Gather doctor table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param c Cursor pointing on query results
     * @return A map containing doctor table data for the given doctor
     */
    private Map<String, Object> GetDoctorTableData(Map<String, Object> doctorData, Cursor c) {
        for (int i = 0; i < DoctoAppDatabaseContract.Doctor.TABLE_KEYS.length; i++) {
            doctorData.put(
                    DoctoAppDatabaseContract.Doctor.TABLE_KEYS[i],
                    c.getString(DoctoAppDatabaseContract.Doctor.TABLE_COLUMNS_POSITIONS[i])
            );
        }

        return doctorData;
    }

    /**
     * Gather address table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorAddressId The doctor address id
     * @return A map containing address table data for the given doctor
     */
    private Map<String, Object> GetAddressTableData(Map<String, Object> doctorData, String doctorAddressId) {
        AddressDatabaseHelper addressDatabaseHelper = new AddressDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                addressDatabaseHelper.GetAddressById(doctorAddressId));

        return doctorData;
    }

    /**
     * Gather availability table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing availability table data for the given doctor
     */
    private Map<String, Object> GetAvailabilityTableData(Map<String, Object> doctorData, String doctorId) {
        AvailabilityDatabaseHelper availabilityDatabaseHelper = new AvailabilityDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Availability.TABLE_NAME,
                availabilityDatabaseHelper.GetAvailabilitiesByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Gather language table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing language table data for the given doctor
     */
    private Map<String, Object> GetLanguageTableData(Map<String, Object> doctorData, String doctorId) {
        LanguageDatabaseHelper languageDatabaseHelper = new LanguageDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Language.TABLE_NAME,
                languageDatabaseHelper.GetLanguagesByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Gather payment option table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing payment option table data for the given doctor
     */
    private Map<String, Object> GetPaymentOptionTableData(Map<String, Object> doctorData, String doctorId) {
        PaymentOptionDatabaseHelper paymentOptionDatabaseHelper = new PaymentOptionDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.PaymentOption.TABLE_NAME,
                paymentOptionDatabaseHelper.GetPaymentOptionsByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Gather reason table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing reason table data for the given doctor
     */
    private Map<String, Object> GetReasonTableData(Map<String, Object> doctorData, String doctorId) {
        ReasonDatabaseHelper reasonDatabaseHelper = new ReasonDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Reason.TABLE_NAME,
                reasonDatabaseHelper.GetReasonsByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Gather education table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing education table data for the given doctor
     */
    private Map<String, Object> GetEducationTableData(Map<String, Object> doctorData, String doctorId) {
        EducationDatabaseHelper educationDatabaseHelper = new EducationDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Education.TABLE_NAME,
                educationDatabaseHelper.GetEducationByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Gather experience table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing experience table data for the given doctor
     */
    private Map<String, Object> GetExperienceTableData(Map<String, Object> doctorData, String doctorId) {
        ExperienceDatabaseHelper experienceDatabaseHelper = new ExperienceDatabaseHelper(this.context);

        doctorData.put(
                DoctoAppDatabaseContract.Experience.TABLE_NAME,
                experienceDatabaseHelper.GetExperiencesByDoctor(doctorId));

        return doctorData;
    }

    /**
     * Get booking table data associated to the given doctor
     * @param doctor The doctor object
     * @return The doctor object completed
     */
    private Doctor GetBookingTableData(Doctor doctor) {
        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(this.context);
        Set<Booking> appointments = bookingDatabaseHelper.GetAppointmentsByDoctor(doctor);
        doctor.setAppointments(appointments);

        return doctor;
    }
}
