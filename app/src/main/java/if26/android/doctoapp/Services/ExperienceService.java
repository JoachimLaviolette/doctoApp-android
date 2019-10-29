package if26.android.doctoapp.Services;

import java.util.List;

import if26.android.doctoapp.Models.Experience;

public class ExperienceService {
    public static boolean Contains(List<Experience> list1, Experience e) {
        for (Experience e1 : list1) if (e1.equals(e)) return true;

        return false;
    }
}
