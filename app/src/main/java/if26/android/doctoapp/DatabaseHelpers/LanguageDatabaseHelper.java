package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Set;

import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Language;

public class LanguageDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public LanguageDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new language in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertLanguages(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] languageData = this.CreateLanguageData(doctor);

        for (Language l: (Set<Language>) languageData[1]) {
            ContentValues languageContentValues = new ContentValues();
            String[] languageTableKeys = DoctoAppDatabaseContract.Language.TABLE_KEYS;

            for (int i = 0; i < languageTableKeys.length; i++) {
                if (i < 1 && languageData[i] instanceof Long)
                    languageContentValues.put(languageTableKeys[i], (Long) languageData[i]);
                else
                    languageContentValues.put(languageTableKeys[i], l.toString());
            }

            database.insert(
                    DoctoAppDatabaseContract.Language.TABLE_NAME,
                    null,
                    languageContentValues
            );
        }

        return doctor;
    }

    /**
     * Create data struct for language using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateLanguageData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getLanguages()
        };
    }
}
