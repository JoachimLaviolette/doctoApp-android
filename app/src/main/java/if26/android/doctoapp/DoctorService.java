package if26.android.doctoapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Map;

public class DoctorService {
    /**
     * The calling activity class
     */
    private Context context;
    private Resources resources;
    private static String
            doctorKey,
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
            experiencesKey;

    public DoctorService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();

        doctorKey = this.resources.getString(R.string.doctor_service_bundle_key_doctor);
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
    }

    /**
     * Format the given doctor data and return it as a bundle
     * @param doctorMap The doctor data
     * @return The doctor data as a Bundle
     */
    public Bundle GetDoctorAsBundle(Map<String,Object> doctorMap) {
        Bundle user = new Bundle();
        String value;
        String[] attributes = new String[] {
                pictureKey,
                fullnameKey,
                specialityKey,
                addressKey,
        };

        for (String attribute: attributes) {
            value = doctorMap.get(attribute).toString();
            user.putString(attribute, value);
        }

        return user;
    }

    public int GetDoctorPicture(Bundle doctor) {
        return Integer.
                parseInt(
                    doctor
                    .getBundle(doctorKey)
                    .getString(pictureKey)
        );
    }

    public String GetDoctorFullname(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(fullnameKey);
    }

    public String GetDoctorSpeciality(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(specialityKey);
    }

    public String GetDoctorAddress(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(addressKey);
    }

    public String GetDoctorPricesRefunds(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(pricesRefundsKey);
    }

    public String GetDoctorPaymentOptions(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(paymentOptionsKey);
    }

    public String GetDoctorDescription(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(descriptionKey);
    }

    public String GetDoctorHoursContacts(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(hoursContactsKey);
    }

    public String GetDoctorEducation(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(educationKey);
    }

    public String GetDoctorLanguages(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(languagesKey);
    }

    public String GetDoctorExperiences(Bundle doctor) {
        return doctor
                .getBundle(doctorKey)
                .getString(experiencesKey);
    }
}
