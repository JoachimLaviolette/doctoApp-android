package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Experience;

public class ExperienceDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public ExperienceDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new experience in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertExperiences(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] experienceData = this.CreateExperienceData(doctor);

        for (Experience e: (List<Experience>) experienceData[1]) {
            ContentValues experienceContentValues = new ContentValues();
            String[] experienceTableKeys = DoctoAppDatabaseContract.Experience.TABLE_KEYS;

            for (int i = 0; i < experienceTableKeys.length; i++) {
                if (i < 1 && experienceData[i] instanceof Long)
                    experienceContentValues.put(experienceTableKeys[i], (Long) experienceData[i]);
                else if (i == 1)
                    experienceContentValues.put(experienceTableKeys[i], e.getYear());
                else
                    experienceContentValues.put(experienceTableKeys[i], e.getDescription());
            }

            database.insert(
                    DoctoAppDatabaseContract.Experience.TABLE_NAME,
                    null,
                    experienceContentValues
            );
        }

        return doctor;
    }

    /**
     * Create data struct for experience using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateExperienceData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getExperiences()
        };
    }
}
