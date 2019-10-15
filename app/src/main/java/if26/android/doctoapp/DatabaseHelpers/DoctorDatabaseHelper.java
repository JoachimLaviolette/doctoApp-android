package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Education;
import if26.android.doctoapp.Models.Experience;
import if26.android.doctoapp.Models.Language;
import if26.android.doctoapp.Models.PaymentOption;
import if26.android.doctoapp.Models.Reason;

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

        // Insert the address of the doc in the DB
        // And save the address id
        doctor = addressDatabaseHelper.InsertAddress(doctor);

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
                doctor.isUnderAgreement(),
                doctor.isHealthInsuranceCard(),
                doctor.isThirdPartyPayment(),
                doctor.GetAddressId(),
                doctor.getLastLogin(),
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
        List<Doctor> doctors = this.BuildDoctorsList(c);
        c.close();

        return doctors;
    }

    /**
     * Build the list of doctors iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching doctors
     */
    private List<Doctor> BuildDoctorsList(Cursor c) {
        List<Doctor> doctors = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> doctorData = new HashMap<>();

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

                // Get data from booking table for the current doctor
                //doctorData = this.GetBookingTableData(doctorData, doctorId);

                Doctor doctor = new Doctor(doctorData);
                doctor.setId(Long.parseLong(doctorId));
                doctors.add(doctor);
            } while (c.moveToNext());
        }

        return doctors;
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
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_ID
        );

        String[] args = { doctorAddressId };

        Cursor c = database.rawQuery(query, args);

        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < DoctoAppDatabaseContract.Address.TABLE_KEYS.length; i++) {
                    doctorData.put(
                            DoctoAppDatabaseContract.Address.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Address.TABLE_COLUMNS_POSITIONS[i])
                    );
                }
            } while (c.moveToNext());
        }

        return doctorData;
    }

    /**
     * Gather availability table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing availability table data for the given doctor
     */
    private Map<String, Object> GetAvailabilityTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Availability.TABLE_NAME,
                DoctoAppDatabaseContract.Availability.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        List<Availability> availabilities = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> availabilityData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Availability.TABLE_KEYS.length; i++) {
                    availabilityData.put(
                            DoctoAppDatabaseContract.Availability.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Availability.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Availability a = new Availability(
                        null,
                        availabilityData.get(DoctoAppDatabaseContract.Availability.COLUMN_NAME_DATE).toString(),
                        availabilityData.get(DoctoAppDatabaseContract.Availability.COLUMN_NAME_TIME).toString()
                );

                availabilities.add(a);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.Availability.TABLE_NAME, availabilities);

        return doctorData;
    }

    /**
     * Gather language table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing language table data for the given doctor
     */
    private Map<String, Object> GetLanguageTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Language.TABLE_NAME,
                DoctoAppDatabaseContract.Language.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        Set<Language> languages = new HashSet<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> languageData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Language.TABLE_KEYS.length; i++) {
                    languageData.put(
                            DoctoAppDatabaseContract.Language.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Language.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Language l = Language.valueOf(languageData.get(DoctoAppDatabaseContract.Language.COLUMN_NAME_LANGUAGE).toString());

                languages.add(l);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.Language.TABLE_NAME, languages);

        return doctorData;
    }

    /**
     * Gather payment option table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing payment option table data for the given doctor
     */
    private Map<String, Object> GetPaymentOptionTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.PaymentOption.TABLE_NAME,
                DoctoAppDatabaseContract.PaymentOption.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        Set<PaymentOption> paymentOptions = new HashSet<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> paymentOptionData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.PaymentOption.TABLE_KEYS.length; i++) {
                    paymentOptionData.put(
                            DoctoAppDatabaseContract.PaymentOption.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.PaymentOption.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                PaymentOption po = PaymentOption.valueOf(paymentOptionData.get(DoctoAppDatabaseContract.PaymentOption.COLUMN_NAME_PAYMENT_OPTION).toString());

                paymentOptions.add(po);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.PaymentOption.TABLE_NAME, paymentOptions);

        return doctorData;
    }

    /**
     * Gather reason table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing reason table data for the given doctor
     */
    private Map<String, Object> GetReasonTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Reason.TABLE_NAME,
                DoctoAppDatabaseContract.Reason.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        List<Reason> reasons = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> reasonData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Reason.TABLE_KEYS.length; i++) {
                    reasonData.put(
                            DoctoAppDatabaseContract.Reason.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Reason.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Reason r = new Reason(
                        Long.parseLong(reasonData.get(DoctoAppDatabaseContract.Reason.COLUMN_NAME_ID).toString()),
                        null,
                        reasonData.get(DoctoAppDatabaseContract.Reason.COLUMN_NAME_DESCRIPTION).toString()
                );

                reasons.add(r);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.Reason.TABLE_NAME, reasons);

        return doctorData;
    }

    /**
     * Gather education table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing education table data for the given doctor
     */
    private Map<String, Object> GetEducationTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Education.TABLE_NAME,
                DoctoAppDatabaseContract.Education.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        List<Education> trainings = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> educationData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Education.TABLE_KEYS.length; i++) {
                    educationData.put(
                            DoctoAppDatabaseContract.Education.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Education.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Education e = new Education(
                        null,
                        educationData.get(DoctoAppDatabaseContract.Education.COLUMN_NAME_YEAR).toString(),
                        educationData.get(DoctoAppDatabaseContract.Education.COLUMN_NAME_DEGREE).toString()
                );

                trainings.add(e);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.Education.TABLE_NAME, trainings);

        return doctorData;
    }

    /**
     * Gather experience table data in a map according to the given doctor data
     * @param doctorData The doctor data
     * @param doctorId The doctor id
     * @return A map containing experience table data for the given doctor
     */
    private Map<String, Object> GetExperienceTableData(Map<String, Object> doctorData, String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Experience.TABLE_NAME,
                DoctoAppDatabaseContract.Experience.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);

        List<Experience> experiences = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> experienceData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Experience.TABLE_KEYS.length; i++) {
                    experienceData.put(
                            DoctoAppDatabaseContract.Experience.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Experience.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Experience e = new Experience(
                        null,
                        experienceData.get(DoctoAppDatabaseContract.Experience.COLUMN_NAME_YEAR).toString(),
                        experienceData.get(DoctoAppDatabaseContract.Experience.COLUMN_NAME_DESCRIPTION).toString()
                );

                experiences.add(e);
            } while (c.moveToNext());
        }

        doctorData.put(DoctoAppDatabaseContract.Experience.TABLE_NAME, experiences);

        return doctorData;
    }

    private Map<String, Object> GetBookingTableData(Map<String, Object> doctorData, String doctorId) {
        // TODO
        return null;
    }
}
