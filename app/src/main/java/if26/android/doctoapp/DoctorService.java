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
            addressKey;

    public DoctorService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();

        doctorKey = this.resources.getString(R.string.doctor_service_bundle_key_doctor);
        pictureKey = this.resources.getString(R.string.doctor_service_doctor_picture);
        fullnameKey = this.resources.getString(R.string.doctor_service_doctor_fullname);
        specialityKey = this.resources.getString(R.string.doctor_service_doctor_speciality);
        addressKey = this.resources.getString(R.string.doctor_service_doctor_address);
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
}
