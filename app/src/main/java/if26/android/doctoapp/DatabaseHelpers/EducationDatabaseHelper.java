package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Education;

public class EducationDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public EducationDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new education in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertTrainings(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] educationData = this.CreateEducationData(doctor);

        for (Education e: (List<Education>) educationData[1]) {
            ContentValues educationContentValues = new ContentValues();
            String[] educationTableKeys = DoctoAppDatabaseContract.Education.TABLE_KEYS;

            for (int i = 0; i < educationTableKeys.length; i++) {
                if (i < 1 && educationData[i] instanceof Long)
                    educationContentValues.put(educationTableKeys[i], (Long) educationData[i]);
                else if (i == 1)
                    educationContentValues.put(educationTableKeys[i], e.getYear());
                else
                    educationContentValues.put(educationTableKeys[i], e.getDegree());
            }

            database.insert(
                DoctoAppDatabaseContract.Education.TABLE_NAME,
                null,
                educationContentValues
            );
        }

        return doctor;
    }

    /**
     * Create data struct for education using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateEducationData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getTrainings()
        };
    }

    /**
     * Get the trainings associated to the given doctor
     * @param doctorId The doctor id
     * @return The matching trainings
     */
    public List<Education> GetEducationByDoctor(String doctorId) {
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
        List<Education> trainings = this.BuildTrainingsList(c);
        c.close();

        return trainings;
    }

    /**
     * Build the list of trainings iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching trainings
     */
    private List<Education> BuildTrainingsList(Cursor c) {
        List<Education> trainings = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> educationData = new LinkedHashMap<>();

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

        return trainings;
    }
}
