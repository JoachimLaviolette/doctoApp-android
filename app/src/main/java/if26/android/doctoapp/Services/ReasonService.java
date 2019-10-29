package if26.android.doctoapp.Services;

import java.util.List;

import if26.android.doctoapp.Models.Reason;

public class ReasonService {
    public static boolean Contains(List<Reason> list1, Reason r2) {
        for (Reason r1 : list1) if (r1.equals(r2)) return true;

        return false;
    }
}
