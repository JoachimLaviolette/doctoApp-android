package if26.android.doctoapp.Services;

import java.util.List;

import if26.android.doctoapp.Models.Availability;

public class AvailabilityService {
    public static boolean Contains(List<Availability> list1, Availability a) {
        for (Availability a1 : list1) if (a1.equals(a)) return true;

        return false;
    }
}
