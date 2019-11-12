package if26.android.doctoapp.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.Models.Booking;
import if26.android.doctoapp.Models.Doctor;
import if26.android.doctoapp.Models.Patient;
import if26.android.doctoapp.Models.Reason;
import if26.android.doctoapp.Models.Resident;
import if26.android.doctoapp.Services.DateTimeService;

public class BookingDatabaseHelper {
    private DoctoAppDatabaseHelper databaseHelper;
    private Context context;

    /**
     * Constructor
     * @param context The calling activity
     */
    public BookingDatabaseHelper(Context context) {
        if (context == null) return;
        this.context = context;
        this.databaseHelper = new DoctoAppDatabaseHelper(this.context);
    }

    /**
     * Insert a new booking in the db using the given resident model
     * @param resident The resident model
     * @return The resident model completed
     */
    public Resident InsertAppointments(Resident resident) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();

        Object[] bookingData = this.CreateBookingData(resident);

        for (Booking a: (Set<Booking>) bookingData[1]) {
            ContentValues bookingContentValues = new ContentValues();
            String[] bookingTableKeys = DoctoAppDatabaseContract.Booking.TABLE_KEYS;

            for (int i = 0; i < bookingTableKeys.length; i++) {
                switch (i) {
                    case 0: // patient
                        if (resident instanceof Patient)
                            bookingContentValues.put(bookingTableKeys[i], resident.getId());
                        else
                            bookingContentValues.put(bookingTableKeys[i], a.GetPatientId());
                        break;
                    case 1: // doctor
                        if (resident instanceof Doctor)
                            bookingContentValues.put(bookingTableKeys[i], resident.getId());
                        else
                            bookingContentValues.put(bookingTableKeys[i], a.GetDoctorId());
                        break;
                    case 2: // reason
                        bookingContentValues.put(bookingTableKeys[i], a.GetReasonId());
                        break;
                    case 3:
                        bookingContentValues.put(bookingTableKeys[i], a.getFullDate());
                        break;
                    case 4:
                        bookingContentValues.put(bookingTableKeys[i], a.getDate());
                        break;
                    case 5:
                        bookingContentValues.put(bookingTableKeys[i], a.getTime());
                        break;
                    case 6:
                        bookingContentValues.put(bookingTableKeys[i], a.getBookingDate());
                        break;
                }
            }

            database.insert(
                    DoctoAppDatabaseContract.Booking.TABLE_NAME,
                    null,
                    bookingContentValues
            );
        }

        return resident;
    }

    /**
     * Insert a new appointment using the provided model
     * @param booking The appointment to add
     * @return If the appointment was properly added in the database
     */
    public boolean InsertAppointment(Booking booking) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
        ContentValues bookingContentValues = new ContentValues();
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_PATIENT, booking.getPatient().getId());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_DOCTOR, booking.getDoctor().getId());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_REASON, booking.getReason().getId());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_FULL_DATE, booking.getFullDate());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_DATE, booking.getDate());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_TIME, booking.getTime());
        bookingContentValues.put(DoctoAppDatabaseContract.Booking.COLUMN_NAME_BOOKING_DATE, booking.getBookingDate());

        return database.insert(
                DoctoAppDatabaseContract.Booking.TABLE_NAME,
                null,
                bookingContentValues
        ) != -1;
    }

    /**
     * Remove the given booking from the db
     * @param booking The booking model of the booking to remove
     * @return If the booking was successfully removed
     */
    public boolean DeleteBooking(Booking booking) {
        SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
        String[] args = { booking.GetPatientId() + "", booking.GetDoctorId() + "", booking.GetReasonId() + "", booking.getBookingDate() };

        return database.delete(
                DoctoAppDatabaseContract.Booking.TABLE_NAME,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_PATIENT + " = ? AND "
                + DoctoAppDatabaseContract.Booking.COLUMN_NAME_DOCTOR + " = ? AND "
                + DoctoAppDatabaseContract.Booking.COLUMN_NAME_REASON + " = ? AND "
                + DoctoAppDatabaseContract.Booking.COLUMN_NAME_BOOKING_DATE + " = ?",
                args
        ) == 1;
    }

    /**
     * Create data struct for booking using the given resident model
     * @param resident The resident model
     * @return The data struct
     */
    private Object[] CreateBookingData(Resident resident) {
        return new Object[] {
                resident.getId(),
                resident.getAppointments()
        };
    }

    /**
     * Get the appointments associated to the given doctor
     * @param doctor The doctor object
     * @return The matching appointments
     */
    public Set<Booking> GetAppointmentsByDoctor(Doctor doctor) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ?",
                DoctoAppDatabaseContract.Booking.TABLE_NAME,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_DOCTOR
        );

        String[] args = { doctor.getId() + "" };

        Cursor c = database.rawQuery(query, args);
        Set<Booking> appointments = this.BuildDoctorAppointmentsList(c, doctor);
        c.close();

        return appointments;
    }

    /**
     * Get the appointments associated to the given patient
     * @param patient The patient
     * @return The matching appointments
     */
    public Set<Booking> GetAppointmentsByPatient(Patient patient) {
        SQLiteDatabase database = this.databaseHelper.getReadableDatabase();

        String query = String.format(
                "SELECT * " +
                        "FROM %s " +
                        "WHERE %s = ? " +
                        "AND %s > ? OR (%s = ? AND %s > ?)",
                DoctoAppDatabaseContract.Booking.TABLE_NAME,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_PATIENT,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_DATE,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_DATE,
                DoctoAppDatabaseContract.Booking.COLUMN_NAME_TIME
        );

        String currentDate = DateTimeService.GetCurrentDate();
        String currentTime = DateTimeService.GetCurrentTime();

        String[] args = {
                patient.getId() + "",
                currentDate,
                currentDate,
                currentTime,
        };

        Cursor c = database.rawQuery(query, args);
        Set<Booking> appointments = this.BuildPatientAppointmentsList(c, patient);
        c.close();

        return appointments;
    }

    /**
     * Build the list of appointments iterating on the given cursor
     * @param c Cursor pointing search query results
     * @param doctor The doctor object to complete
     * @return The list of matching appointments
     */
    private Set<Booking> BuildDoctorAppointmentsList(Cursor c, Doctor doctor) {
        PatientDatabaseHelper patientDatabaseHelper = new PatientDatabaseHelper(this.context);
        ReasonDatabaseHelper reasonDatabaseHelper = new ReasonDatabaseHelper(this.context);
        Set<Booking> appointments = new LinkedHashSet<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> bookingData = new LinkedHashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Booking.TABLE_KEYS.length; i++) {
                    bookingData.put(
                            DoctoAppDatabaseContract.Booking.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Booking.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                // Create the patient object from the patient id
                Patient patient = patientDatabaseHelper.GetPatientById(
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_PATIENT).toString(),
                        true
                );
                patient.setAppointments(appointments);

                // Create the reason object from the reason id
                Reason reason = reasonDatabaseHelper.GetReasonById(
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_REASON).toString()
                );
                reason.setDoctor(doctor);

                Booking a = new Booking(
                        patient,
                        doctor,
                        reason,
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_FULL_DATE).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_DATE).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_TIME).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_BOOKING_DATE).toString()
                );

                appointments.add(a);
            } while (c.moveToNext());
        }

        return appointments;
    }

    /**
     * Build the list of appointments iterating on the given cursor
     * @param c Cursor pointing search query results
     * @param patient The patient object to complete
     * @return The list of matching appointments
     */
    private Set<Booking> BuildPatientAppointmentsList(Cursor c, Patient patient) {
        DoctorDatabaseHelper doctorDatabaseHelper = new DoctorDatabaseHelper(this.context);
        ReasonDatabaseHelper reasonDatabaseHelper = new ReasonDatabaseHelper(this.context);
        Set<Booking> appointments = new LinkedHashSet<>();

        if (c.moveToFirst()) {
            do {
                Map<String, Object> bookingData = new LinkedHashMap<>();

                for (int i = 0; i < DoctoAppDatabaseContract.Booking.TABLE_KEYS.length; i++) {
                    bookingData.put(
                            DoctoAppDatabaseContract.Booking.TABLE_KEYS[i],
                            c.getString(DoctoAppDatabaseContract.Booking.TABLE_COLUMNS_POSITIONS[i])
                    );
                }

                // Create the doctor object from the doctor id
                Doctor doctor = doctorDatabaseHelper.GetDoctorById(
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_DOCTOR).toString(),
                        true
                );
                doctor.setAppointments(appointments);

                // Create the reason object from the reason id
                Reason reason = reasonDatabaseHelper.GetReasonById(
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_REASON).toString()
                );
                reason.setDoctor(doctor);

                // Create the appointment
                Booking a = new Booking(
                        patient,
                        doctor,
                        reason,
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_FULL_DATE).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_DATE).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_TIME).toString(),
                        bookingData.get(DoctoAppDatabaseContract.Booking.COLUMN_NAME_BOOKING_DATE).toString()
                );

                appointments.add(a);
            } while (c.moveToNext());
        }

        return appointments;
    }
}
