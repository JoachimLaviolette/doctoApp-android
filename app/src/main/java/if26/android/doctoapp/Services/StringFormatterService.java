package if26.android.doctoapp.Services;

public class StringFormatterService {
    /**
     * Capitalize the given string
     * @param string The string to capitalize
     * @return The capitalized string
     */
    public static String Capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
