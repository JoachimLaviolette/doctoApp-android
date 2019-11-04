package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Models.Availability;
import if26.android.doctoapp.Models.Doctor;

public class AvailabilityDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public AvailabilityDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new availability in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertAvailabilities(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] availabilityData = this.CreateAvailabilityData(doctor);

        for (Availability a: (List<Availability>) availabilityData[1]) {
            ContentValues availabilityContentValues = new ContentValues();
            String[] availabilityTableKeys = DoctoAppDatabaseContract.Availability.TABLE_KEYS;

            for (int i = 0; i < availabilityTableKeys.length; i++) {
                if (i < 1 && availabilityData[i] instanceof Long)
                    availabilityContentValues.put(availabilityTableKeys[i], (Long) availabilityData[i]);
                else if (i == 1)
                    availabilityContentValues.put(availabilityTableKeys[i], a.getDate());
                else
                    availabilityContentValues.put(availabilityTableKeys[i], a.getTime());
            }

            database.insert(
                    DoctoAppDatabaseContract.Availability.TABLE_NAME,
                    null,
                    availabilityContentValues
            );
        }

        return doctor;
    }

    /**
     * Update doctor availabilities
     * @param doctor The doctor to update availabilities of
     * @return If availabilities have correctly been updated
     */
    public boolean UpdateAvailabilities(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
        String[] args = { doctor.getId() + "" };

        if (database.delete(
                DoctoAppDatabaseContract.Availability.TABLE_NAME,
                DoctoAppDatabaseContract.Availability.COLUMN_NAME_DOCTOR + " = ?",
                args
        ) >= 0) {
            this.InsertAvailabilities(doctor);

            return true;
        }

        return false;
    }

    /**
     * Create data struct for availability using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateAvailabilityData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getAvailabilities()
        };
    }

    /**
     * Get the availabilities associated to the given doctor
     * @param doctorId The doctor id
     * @return The matching availabilities
     */
    public List<Availability> GetAvailabilitiesByDoctor(String doctorId) {
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
        List<Availability> availabilities = this.BuildAvailabilitiesList(c);
        c.close();

        return availabilities;
    }


    /**
     * Build the list of availabilities iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching availabilities
     */
    private List<Availability> BuildAvailabilitiesList(Cursor c) {
        List<Availability> availabilities = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> availabilityData = new LinkedHashMap<>();

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

        return availabilities;
    }
}
