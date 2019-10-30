package if26.android.doctoapp.Models;

import android.content.Context;

import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.DatabaseHelpers.DoctoAppDatabaseContract;
import if26.android.doctoapp.DatabaseHelpers.PatientDatabaseHelper;

public class Patient extends Resident {
    private String birthdate;
    private String insuranceNumber;

    public Patient(long id, String lastname, String firstname, String birthdate, String email, String pwd, String pwdSalt, String insuranceNumber, Address address, String lastLogin, String picture) {
        super(
                id,
                lastname,
                firstname,
                email,
                pwd,
                pwdSalt,
                address,
                lastLogin,
                picture
        );
        this.birthdate = birthdate;
        this.insuranceNumber = insuranceNumber;
    }

    public Patient(long id, String lastname, String firstname, String birthdate, String email, String pwd, String pwdSalt, String insuranceNumber, Address address, String lastLogin, String picture, Set<Booking> appointments) {
        super(
                id,
                lastname,
                firstname,
                email,
                pwd,
                pwdSalt,
                address,
                lastLogin,
                picture,
                appointments
        );
        this.birthdate = birthdate;
        this.insuranceNumber = insuranceNumber;
    }

    public Patient(Map<String, Object> patientData) {
        super(patientData);
        this.id = Long.parseLong(patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_ID).toString());
        this.lastname = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_LASTNAME).toString();
        this.firstname = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_FIRSTNAME).toString();
        this.birthdate = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_BIRTHDATE).toString();
        this.email = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_EMAIL).toString();
        this.insuranceNumber = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_INSURANCE_NUMBER).toString();
        this.pwd = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_PWD).toString();
        this.pwdSalt = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_PWD_SALT).toString();
        this.address = (Address) patientData.get(DoctoAppDatabaseContract.Address.TABLE_NAME);
        this.lastLogin = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_LAST_LOGIN).toString();
        this.picture = patientData.get(DoctoAppDatabaseContract.Patient.COLUMN_NAME_PICTURE).toString();
        this.appointments = (Set<Booking>) patientData.get(DoctoAppDatabaseContract.Booking.TABLE_NAME);
    }

    // Getters and setters
    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getInsuranceNumber() {
        return this.insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    // Update methods
    public void UpdateRelatedData() {
        this.UpdateAppointmentsResidentId();
    }

    protected void UpdateAppointmentsResidentId() {
        for (Booking a: this.appointments) a.setPatient(this);
    }

    /**
     * Update the current patient
     * @param context The calling context
     * @return The patient with refreshed data
     */
    public Resident Update(Context context) {
        return new PatientDatabaseHelper(context).GetPatientById(this.id + "", false);
    }
}
