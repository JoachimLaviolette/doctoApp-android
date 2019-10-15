package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import if26.android.doctoapp.Models.Doctor;

public class AddressDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public AddressDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new address in the db using the given doctor model
     * @param doctor The doctor model
     * @return The doctor model completed
     */
    public Doctor InsertAddress(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        String[] addressData = this.CreateAddressData(doctor);

        ContentValues addressContentValues = new ContentValues();
        String[] addressTableKeys = DoctoAppDatabaseContract.Address.TABLE_KEYS_INSERT;

        for (int i = 0; i < addressTableKeys.length; i++)
            addressContentValues.put(addressTableKeys[i], addressData[i]);

        long addressId = database.insert(
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                null,
                addressContentValues
        );

        doctor.SetAddressId(addressId);

        return doctor;
    }

    /**
     * Create data struct for address using the given doctor model
     * @param doctor The doctor model
     * @return The data struct
     */
    private String[] CreateAddressData(Doctor doctor) {
        return new String[] {
                doctor.GetCity(),
                doctor.GetCountry().toUpperCase(),
                doctor.GetStreet1(),
                doctor.GetStreet2(),
                doctor.GetZip(),
        };
    }
}
