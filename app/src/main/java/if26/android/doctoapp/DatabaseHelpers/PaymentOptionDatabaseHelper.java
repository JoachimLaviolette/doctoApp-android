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
import if26.android.doctoapp.Models.PaymentOption;

public class PaymentOptionDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public PaymentOptionDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new payment option in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertPaymentOptions(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] paymentOptionData = this.CreatePaymentOptionData(doctor);

        for (PaymentOption po: (Set<PaymentOption>) paymentOptionData[1]) {
            ContentValues paymentOptionContentValues = new ContentValues();
            String[] paymentOptionTableKeys = DoctoAppDatabaseContract.PaymentOption.TABLE_KEYS;

            for (int i = 0; i < paymentOptionTableKeys.length; i++) {
                if (i < 1 && paymentOptionData[i] instanceof Long)
                    paymentOptionContentValues.put(paymentOptionTableKeys[i], (Long) paymentOptionData[i]);
                else
                    paymentOptionContentValues.put(paymentOptionTableKeys[i], po.toString());
            }

            database.insert(
                    DoctoAppDatabaseContract.PaymentOption.TABLE_NAME,
                    null,
                    paymentOptionContentValues
            );
        }

        return doctor;
    }

    /**
     * Create data struct for payment option using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private Object[] CreatePaymentOptionData(Doctor doctor) {
        return new Object[] {
                doctor.getId(),
                doctor.getPaymentOptions()
        };
    }

    /**
     * Get the payment options associated to the given doctor
     * @param doctorId The doctor id
     * @return The matching payment options
     */
    public Set<PaymentOption> GetPaymentOptionsByDoctor(String doctorId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.PaymentOption.TABLE_NAME,
                DoctoAppDatabaseContract.PaymentOption.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctorId };

        Cursor c = database.rawQuery(query, args);
        Set<PaymentOption> paymentOptions = this.BuildPaymentOptionsList(c);
        c.close();

        return paymentOptions;
    }

    /**
     * Build the list of payment options iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching payment options
     */
    private Set<PaymentOption> BuildPaymentOptionsList(Cursor c) {
        Set<PaymentOption> paymentOptions = new HashSet<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> paymentOptionData = new HashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.PaymentOption.TABLE_KEYS.length; i++) {
                    paymentOptionData.put(
                            DoctoAppDatabaseContract.PaymentOption.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.PaymentOption.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                PaymentOption po = PaymentOption.valueOf(paymentOptionData.get(DoctoAppDatabaseContract.PaymentOption.COLUMN_NAME_PAYMENT_OPTION).toString());

                paymentOptions.add(po);
            } while (c.moveToNext());
        }

        return paymentOptions;
    }

}
