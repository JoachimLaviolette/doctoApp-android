package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    /**
     * Get the languages associated to the given doctor
     * @param doctorId The doctor id
     * @return The matching languages
     */
    public Set<Language> GetLanguagesByDoctor(String doctorId) {
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
        Set<Language> languages = this.BuildLanguagesList(c);
        c.close();

        return languages;
    }

    /**
     * Build the list of languages iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching languages
     */
    private Set<Language> BuildLanguagesList(Cursor c) {
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

        return languages;
    }
}
