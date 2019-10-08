package if26.android.doctoapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Map;

public class UserService {
    /**
     * The calling activity class
     */
    private Context context;
    private Resources resources;

    public UserService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
    }

    /**
     * Format the given doctor data and return it as a bundle
     * @param doctorMap The doctor data
     * @return The doctor data as a Bundle
     */
    public Bundle GetDoctorAsBundle(Map<String,Object> doctorMap) {
        Bundle user = new Bundle();
        String value;

        for (String key: new String[] {
                this.resources.getString(R.string.user_service_user_picture),
                this.resources.getString(R.string.user_service_user_fullname),
                this.resources.getString(R.string.user_service_user_description)
        }) {
            value = doctorMap.get(key).toString();
            user.putString(key, value);
        }

        return user;
    }

    public int GetDoctorPicture(Bundle doctor) {
        String key = this.resources.getString(R.string.user_service_bundle_key_doctor);

        return Integer.parseInt(
                doctor
                .getBundle(key)
                .getString(
                        this.resources.getString(
                                R.string.user_service_user_picture
                        )
                )
        );
    }

    public String GetDoctorFullname(Bundle doctor) {
        String key = this.resources.getString(R.string.user_service_bundle_key_doctor);

        return doctor
                .getBundle(key)
                .getString(this.resources.getString(R.string.user_service_user_fullname));
    }

    public String GetDoctorDescription(Bundle doctor) {
        String key = this.resources.getString(R.string.user_service_bundle_key_doctor);

        return doctor
                .getBundle(key)
                .getString(this.resources.getString(R.string.user_service_user_description));
    }
}
