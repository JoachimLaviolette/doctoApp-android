package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import if26.android.doctoapp.Models.Address;
import if26.android.doctoapp.Models.Resident;

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
     * @param resident The Resident model
     * @return The doctor model completed
     */
    public Resident InsertAddress(Resident resident) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        String[] addressData = this.CreateAddressData(resident);

        ContentValues addressContentValues = new ContentValues();
        String[] addressTableKeys = DoctoAppDatabaseContract.Address.TABLE_KEYS_INSERT;

        for (int i = 0; i < addressTableKeys.length; i++)
            addressContentValues.put(addressTableKeys[i], addressData[i]);

        long addressId = database.insert(
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                null,
                addressContentValues
        );

        resident.SetAddressId(addressId);

        return resident;
    }

    /**
     * Update the given resident model-associated address in the db using the given resident model data
     * @param resident The Resident model
     * @return The resident model completed
     */
    public boolean UpdateAddress(Resident resident) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        String[] addressData = this.CreateAddressData(resident);

        ContentValues addressContentValues = new ContentValues();
        String[] addressTableKeys = DoctoAppDatabaseContract.Address.TABLE_KEYS_INSERT;

        for (int i = 0; i < addressTableKeys.length; i++)
            addressContentValues.put(addressTableKeys[i], addressData[i]);

         String[] args = { resident.GetAddressId() + "" };

        return database.update(
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                addressContentValues,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_ID + " = ?",
                args
        ) == 1;
    }

    /**
     * Create data struct for address using the given resident
     * @param resident The Resident model
     * @return The data struct
     */
    private String[] CreateAddressData(Resident resident) {
        return new String[] {
                resident.GetStreet1(),
                resident.GetStreet2(),
                resident.GetCity(),
                resident.GetZip(),
                resident.GetCountry().toUpperCase(),
        };
    }

    /**
     * Get an address by id
     * @param addressId The address id
     * @return The matching address
     */
    public Address GetAddressById(String addressId) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Address.TABLE_NAME,
                DoctoAppDatabaseContract.Address.COLUMN_NAME_ID
        );

        String[] args = { addressId };

        Cursor c = database.rawQuery(query, args);
        List<Address> addressList = this.BuildAddressesList(c);
        Address address = addressList.isEmpty() ? null : addressList.get(0);
        c.close();

        return address;
    }

    /**
     * Build the list of addresses iterating on the given cursor
     * @param c Cursor pointing search query results
     * @return The list of matching addresses
     */
    private List<Address> BuildAddressesList(Cursor c) {
        List<Address> addresses = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> addressData = new LinkedHashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Address.TABLE_KEYS.length; i++) {
                    addressData.put(
                            DoctoAppDatabaseContract.Address.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Address.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                Address a = new Address(
                        Long.parseLong(addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_ID).toString()),
                        addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET1).toString(),
                        addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET2).toString(),
                        addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_CITY).toString(),
                        addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_ZIP).toString(),
                        addressData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_COUNTRY).toString()
                );

                addresses.add(a);
            } while (c.moveToNext());
        }

        return addresses;
    }
}
