package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

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
}
