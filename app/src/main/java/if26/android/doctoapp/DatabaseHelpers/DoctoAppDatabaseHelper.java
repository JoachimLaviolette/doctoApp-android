package if26.android.doctoapp.DatabaseHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DoctoAppDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static String SQL_CREATE_ENTRIES_FILE = "sql_entries/create.sql";
    private static String SQL_DELETE_ENTRIES_FILE = "sql_entries/delete.sql";
    private static String SQL_CREATE_ENTRIES = null;
    private static String SQL_DELETE_ENTRIES = null;

    // To increment if the database version schema
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "docto_app.db";

    /**
     * Constructor
     * @param context The calling activity
     */
    public DoctoAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.Instantiate();
    }

    /**
     * Instantiate default entries
     */
    private void Instantiate() {
        SQL_CREATE_ENTRIES = this.ReadFileEntries(SQL_CREATE_ENTRIES_FILE);
        SQL_DELETE_ENTRIES = this.ReadFileEntries(SQL_DELETE_ENTRIES_FILE);
    }

    /**
     * Read the given file entries
     * @return The content as a String object
     */
    private String ReadFileEntries(String file) {
        InputStream reader = null;
        StringBuffer content = new StringBuffer();

        try {
            reader = this.context.getAssets().open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String line = br.readLine();

            while (line != null) {
                content.append(line);
                line = br.readLine();
            }

            return content.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        // Crucial statement
        // By default, foreign_keys = OFF
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    /**
     * Instantiate the app database
     * @param db The app database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (SQL_CREATE_ENTRIES == null) return;

        // Execute each create statement found in the create entries file
        String[] createStatements = SQL_CREATE_ENTRIES.split(";");
        for (String createStatement: createStatements)
            db.execSQL(createStatement);
    }

    /**
     * Handle the upgrade of the database
     * @param db The app database
     * @param oldVersion The old version
     * @param newVersion The new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (SQL_DELETE_ENTRIES == null) return;

        // Execute each delete statement found in the delete entries file
        String[] deleteStatements = SQL_DELETE_ENTRIES.split(";");
        for (String deleteStatement: deleteStatements)
            db.execSQL(deleteStatement);

        this.onCreate(db);
    }

    /**
     * Handle the downgrade of the database
     * @param db The app database
     * @param oldVersion The old version
     * @param newVersion The new version
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }
}