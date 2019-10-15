package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Reason;

public class ReasonDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public ReasonDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new reason in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertReasons(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] reasonData = this.CreateReasonData(doctor);

        for (Reason r: (List<Reason>) reasonData[1]) {
            ContentValues reasonContentValues = new ContentValues();
            String[] reasonTableKeys = DoctoAppDatabaseContract.Reason.TABLE_KEYS_INSERT;

            for (int i = 0; i < reasonTableKeys.length; i++) {
                if (i < 1 && reasonData[i] instanceof Long)
                    reasonContentValues.put(reasonTableKeys[i], (Long) reasonData[i]);
                else
                    reasonContentValues.put(reasonTableKeys[i], r.getDescription());
            }

            long reasonId = database.insert(
                    DoctoAppDatabaseContract.Reason.TABLE_NAME,
                    null,
                    reasonContentValues
            );

            doctor.SetReasonId(r, reasonId);
        }

        return doctor;
    }

    /**
     * Create data struct for reason using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreateReasonData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getReasons()
        };
    }
}
