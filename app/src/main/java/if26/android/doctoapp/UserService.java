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

    public UserService(Context context) {
        this.context = context;
    }

    /**
     * Format the given doctor data and return it as a bundle
     * @param doctorMap The doctor data
     * @return The doctor data as a Bundle
     */
    public Bundle GetDoctorAsBundle(Map<String,Object> doctorMap) {
        Bundle user = new Bundle();
        Resources resources = this.context.getResources();
        String value;

        for (String key: new String[] {
                resources.getString(R.string.user_service_user_picture),
                resources.getString(R.string.user_service_user_fullname),
                resources.getString(R.string.user_service_user_description)
        }) {
            value = doctorMap.get(key).toString();
            user.putString(key, value);
        }

        return user;
    }
}
