package if26.android.doctoapp.Services;

import java.util.List;

import if26.android.doctoapp.Models.Education;

public class EducationService {
    public static boolean Contains(List<Education> list1, Education e) {
        for (Education e1 : list1) if (e1.equals(e)) return true;

        return false;
    }
}
