package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

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
}
