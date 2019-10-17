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

    /**
     * Get a reason by id
     * @param reasonId The reason id
     * @return The matching reason
     */
    public Reason GetReasonById(String reasonId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Reason.TABLE_NAME,
                DoctoAppDatabaseContract.Reason.COLUMN_NAME_ID
        );

        String[] args = {
                reasonId
        };

        Cursor c = database.rawQuery(query, args);
        Reason reason = this.BuildReasonsList(c).get(0);
        c.close();

        return reason;
    }

    /**
     * Get the reasons associated to the given doctor
     * @param doctorId The doctor id
     * @return THe matching reasons
     */
    public List<Reason> GetReasonsByDoctor(String doctorId) {
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
        List<Reason> reasons = this.BuildReasonsList(c);
        c.close();

        return reasons;
    }

    /**
     * Build the list of reasons iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching reasons
     */
    private List<Reason> BuildReasonsList(Cursor c) {
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

        return reasons;
    }
}
