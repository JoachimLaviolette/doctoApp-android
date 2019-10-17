package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Get the experiences associated to the given doctor
     * @param doctorId The doctor id
     * @return The matching experiences
     */
    public List<Experience> GetExperiencesByDoctor(String doctorId) {
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
        List<Experience> experiences = this.BuildExperiencesList(c);
        c.close();

        return experiences;
    }

    /**
     * Build the list of experiences iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching experiences
     */
    private List<Experience> BuildExperiencesList(Cursor c) {
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

        return experiences;
    }
}
