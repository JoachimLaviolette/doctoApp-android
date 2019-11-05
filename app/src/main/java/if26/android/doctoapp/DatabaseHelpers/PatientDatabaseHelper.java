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
import if26.android.doctoapp.Models.Patient;

public class PatientDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public PatientDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Create a new patient in the database using the given Patient model
     * @param patient The patient model
     * @return If the patient has successfully been created in the database
     */
    public boolean CreatePatient(Patient patient) {
        AddressDatabaseHelper addressDatabaseHelper = new AddressDatabaseHelper(this.context);
        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(this.context);

        // Insert the address of the patient in the DB
        // And save the address id
        patient = (Patient) addressDatabaseHelper.InsertAddress(patient);

        if (patient.GetAddressId() == -1) return false;

        // Insert the patient in the DB
        // And save its id
        patient = this.InsertPatient(patient);

        // Insert the appointments of the patient in the DB
        // And save its id
        patient = (Patient) bookingDatabaseHelper.InsertAppointments(patient);

        return (patient.getId() != -1);
    }

    /**
     * Update the given patient model-related patient in the database using the given Patient model data
     * @param patient The patient model
     * @return If the patient has successfully been updated in the database
     */
    public boolean UpdatePatient(Patient patient) {
        AddressDatabaseHelper addressDatabaseHelper = new AddressDatabaseHelper(this.context);

        // Update the address of the patient in the DB
        if(!addressDatabaseHelper.UpdateAddress(patient)) return false;

        // Update the patient in the DB
        ContentValues patientContentValues = new ContentValues();
        String[] patientTableKeys = DoctoAppDatabaseContract.Patient.TABLE_KEYS_INSERT;
        Object[] patientData = this.CreatePatientData(patient);

        for (int i = 0; i < patientTableKeys.length; i++) {
            if (patientData[i] instanceof Long)
                patientContentValues.put(patientTableKeys[i], (Long) patientData[i]);
            else
                patientContentValues.put(patientTableKeys[i], patientData[i].toString());
        }

        String[] args = { patient.getId() + "" };

        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        return (database.update(
                DoctoAppDatabaseContract.Patient.TABLE_NAME,
                patientContentValues,
                DoctoAppDatabaseContract.Patient.COLUMN_NAME_ID + " = ?",
                args
        ) == 1);
    }

    /**
     * Create a data struct to gather patient information
     * @param patient The patient model
     * @return The data struct
     */
    private Object[] CreatePatientData(Patient patient) {
        return new Object[] {
                patient.getLastname(),
                patient.getFirstname(),
                patient.getBirthdate(),
                patient.getEmail(),
                patient.getPwd(),
                patient.getPwdSalt(),
                patient.getInsuranceNumber(),
                patient.GetAddressId(),
                patient.getLastLogin(),
                patient.getPicture(),
        };
    }

    /**
     * Insert a new patient in the database from the given patient model
     * @param patient The patient model
     * @return The patient model completed
     */
    private Patient InsertPatient(Patient patient) {
        ContentValues patientContentValues = new ContentValues();
        String[] patientTableKeys = DoctoAppDatabaseContract.Patient.TABLE_KEYS_INSERT;
        Object[] patientData = this.CreatePatientData(patient);

        for (int i = 0; i < patientTableKeys.length; i++) {
            if (patientData[i] instanceof Long)
                patientContentValues.put(patientTableKeys[i], (Long) patientData[i]);
            else
                patientContentValues.put(patientTableKeys[i], patientData[i].toString());
        }

        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();
        long patientId = database.insert(
                DoctoAppDatabaseContract.Patient.TABLE_NAME,
                null,
                patientContentValues
        );

        patient.setId(patientId);

        return patient;
    }

    /**
     * Get all the patients matching with the needle
     * See the SQL query to see what fields are considered
     * @return A list of matching patients
     */
    public List<Patient> GetPatients() {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s",
                DoctoAppDatabaseContract.Patient.TABLE_NAME
        );

        Cursor c = database.rawQuery(query, null);
        List<Patient> patients = this.BuildPatientsList(c, false);
        c.close();

        return patients;
    }

    /**
     * Get a patient by id
     * @param patientId The id of the patient to retrieve
     * @param fromDoctor If the request comes from doctor building process
     * @return The matching patient
     */
    public Patient GetPatientById(String patientId, boolean fromDoctor) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Patient.TABLE_NAME,
                DoctoAppDatabaseContract.Patient.COLUMN_NAME_ID
        );

        String[] args = { patientId };

        Cursor c = database.rawQuery(query, args);
        List<Patient> patients = this.BuildPatientsList(c, fromDoctor);
        Patient patient = patients.isEmpty() ? null : patients.get(0);
        c.close();

        return patient;
    }

    /**
     * Get a patient by email
     * @param email The email
     * @return The matching patient if found, null otherwise
     */
    public Patient GetPatientByEmail(String email) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Patient.TABLE_NAME,
                DoctoAppDatabaseContract.Patient.COLUMN_NAME_EMAIL
        );

        String[] args = { email };

        Cursor c = database.rawQuery(query, args);
        List<Patient> patients = this.BuildPatientsList(c, false);
        Patient patient = patients.isEmpty() ? null : patients.get(0);
        c.close();

        return patient;
    }

    /**
     * Build the list of patients iterating on the given cursor
     * @param c Cursor pointing search query results
     * @param fromDoctor If the request comes from doctor building process
     * @return The list of matching patients
     */
    private List<Patient> BuildPatientsList(Cursor c, boolean fromDoctor) {
        List<Patient> patients = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> patientData = new LinkedHashMap<>();

                // Get data from patient table
                patientData = this.GetPatientTableData(patientData, c);

                // Retrieve the current patient address id
                String patientAddressId = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_ADDRESS).toString();

                // Get data from address table for the current patient
                patientData = this.GetAddressTableData(patientData, patientAddressId);

                // Create the patient object
                Patient patient = new Patient(patientData);

                // Get data from booking table for the current patient
                if (!fromDoctor) patient = this.GetBookingTableData(patient);

                // Add the patient to the list
                patients.add(patient);
            } while (c.moveToNext());
        }

        return patients;
    }

    /**
     * Remove a patient by id
     * @param patientId The id of the patient to remove
     * @return If the patient was successfully removed
     */
    public boolean DeletePatientById(String patientId) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
        String[] args = { patientId };

        return database.delete(
            DoctoAppDatabaseContract.Patient.TABLE_NAME,
            DoctoAppDatabaseContract.Patient.COLUMN_NAME_ID + " = ?",
            args
        ) == 1;
    }

    /**
     * Gather patient table data in a map according to the given patient data
     * @param patientData The patient data
     * @param c Cursor pointing on query results
     * @return A map containing patient table data for the given patient
     */
    private Map<String, Object> GetPatientTableData(Map<String, Object> patientData, Cursor c) {
        for (int i = 0; i < DoctoAppDatabaseContract.Patient.TABLE_KEYS.length; i++) {
            patientData.put(
                    DoctoAppDatabaseContract.Patient.TABLE_KEYS[i],
                    c.getString(DoctoAppDatabaseContract.Patient.TABLE_COLUMNS_POSITIONS[i])
            );
        }

        return patientData;
    }

    /**
     * Gather address table data in a map according to the given patient data
     * @param patientData The patient data
     * @param patientAddressId The patient address id
     * @return A map containing address table data for the given patient
     */
    private Map<String, Object> GetAddressTableData(Map<String, Object> patientData, String patientAddressId) {
        AddressDatabaseHelper addressDatabaseHelper = new AddressDatabaseHelper(this.context);

        patientData.put(
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                addressDatabaseHelper.GetAddressById(patientAddressId));

        return patientData;
    }

    /**
     * Get booking table data associated to the given patient
     * @param patient The patient object
     * @return The patient object completed
     */
    private Patient GetBookingTableData(Patient patient) {
        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(this.context);
        Set<Booking> appointments = bookingDatabaseHelper.GetAppointmentsByPatient(patient);
        patient.setAppointments(appointments);

        return patient;
    }
}
