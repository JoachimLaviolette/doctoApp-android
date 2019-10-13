package if26.android.doctoapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorService {
    /**
     * The calling activity class
     */
    private Context context;
    private Bundle doctor;
    private Resources resources;
    private static String
            pictureKey,
            fullnameKey,
            specialityKey,
            addressKey,
            pricesRefundsKey,
            paymentOptionsKey,
            descriptionKey,
            hoursContactsKey,
            educationKey,
            languagesKey,
            experiencesKey,
            reasonsKey;

    /**
     * Constructor
     * @param context The calling context
     */
    public DoctorService(Context context) {
        this.Instantiate(context);
        this.SetKeys();
    }

    /**
     * Constructor
     * @param context The calling context
     * @param doctor The context to get data from
     */
    public DoctorService(Context context, Bundle doctor) {
        this.doctor = doctor;
        this.Instantiate(context);
        this.SetKeys();
    }

    /**
     * Instantiate the context and the resources
     * @param context
     */
    private void Instantiate(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
    }

    private void SetKeys() {
        pictureKey = this.resources.getString(R.string.doctor_service_doctor_picture);
        fullnameKey = this.resources.getString(R.string.doctor_service_doctor_fullname);
        specialityKey = this.resources.getString(R.string.doctor_service_doctor_speciality);
        addressKey = this.resources.getString(R.string.doctor_service_doctor_address);
        pricesRefundsKey = this.resources.getString(R.string.doctor_service_doctor_prices_refunds);
        paymentOptionsKey = this.resources.getString(R.string.doctor_service_doctor_payment_options);
        descriptionKey = this.resources.getString(R.string.doctor_service_doctor_description);
        hoursContactsKey = this.resources.getString(R.string.doctor_service_doctor_hours_contacts);
        educationKey = this.resources.getString(R.string.doctor_service_doctor_education);
        languagesKey = this.resources.getString(R.string.doctor_service_doctor_languages);
        experiencesKey = this.resources.getString(R.string.doctor_service_doctor_experiences);
        reasonsKey = this.resources.getString(R.string.doctor_service_doctor_reason);
    }

    /**
     * Format the given doctor data and return it as a bundle
     * @param doctorData The doctor data
     * @return The doctor data as a Bundle
     */
    public Bundle GetDoctorAsBundle(Map<String,Object> doctorData) {
        Bundle doctor = this.doctor != null ? this.doctor : new Bundle();
        Object value;
        String[] attributes = new String[] {
                pictureKey,
                fullnameKey,
                specialityKey,
                addressKey,
                pricesRefundsKey,
                paymentOptionsKey,
                descriptionKey,
                hoursContactsKey,
                educationKey,
                languagesKey,
                experiencesKey,
                reasonsKey,
        };

        for (String attribute: attributes) {
            value = doctorData.get(attribute);

            if (value != null) {
                if (value instanceof Serializable)
                   doctor.putSerializable(attribute, (Serializable) value);
                else
                    doctor.putString(attribute, value.toString());
            }
        }

        return doctor;
    }

    public int GetDoctorPicture() {
        return this.doctor.getInt(pictureKey);
    }

    public String GetDoctorFullname() {
        return doctor.getString(fullnameKey);
    }

    public String GetDoctorFullnameAsTitle() {
        return
                this.resources.getString(R.string.choose_reason_dr) +
                " " +
                this.GetFirstName() +
                " " +
                this.GetLastName();
    }

    private String GetFirstName() {
        String name = this.GetDoctorFullname();
        name = name.substring(0, name.indexOf(" ")).trim();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        return name;
    }

    private String GetLastName() {
        String name = this.GetDoctorFullname();
        name = name.substring(name.indexOf(" ")).trim().toUpperCase();

        return name;
    }

    public String GetDoctorSpeciality() {
        return doctor.getString(specialityKey);
    }

    public String GetDoctorAddress() {
        return doctor.getString(addressKey);
    }

    public String GetDoctorPricesRefunds() {
        return doctor.getString(pricesRefundsKey);
    }

    public String GetDoctorPaymentOptions() {
        return doctor.getString(paymentOptionsKey);
    }

    public String GetDoctorDescription() {
        return doctor.getString(descriptionKey);
    }

    public Serializable GetDoctorHoursContacts() {
        List<HashMap<String,Object>> data = (ArrayList) doctor.getSerializable(hoursContactsKey);

        return data.get(0);
    }

    // TODO : This has nothing to do here since it's all about booking data so it must be put in another dedicated service
    public Serializable GetAppointmentData() {
        return doctor.getSerializable(hoursContactsKey);
    }

    public String GetDoctorEducation() {
        return doctor.getString(educationKey);
    }

    public String GetDoctorLanguages() {
        return doctor.getString(languagesKey);
    }

    public String GetDoctorExperiences() {
        return doctor.getString(experiencesKey);
    }

    public Serializable GetDoctorReasons() {
        return doctor.getSerializable(reasonsKey);
    }
}
