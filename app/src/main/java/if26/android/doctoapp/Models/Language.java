package if26.android.doctoapp.Models;

import java.io.Serializable;

public enum Language implements Serializable {
    FR { public String toString() { return "French"; } },
    EN { public String toString() { return "English"; } },
    ES { public String toString() { return "Spanish"; } },
    GER { public String toString() { return "German"; } },
    IT { public String toString() { return "Italian"; } };

    /**
     * Get the Language value of the given string representation
     * @param lString The string representation of the language
     * @return The language value associated
     */
    public static Language GetValueOf(String lString) {
        switch (lString) {
            case "French": return FR;
            case "English": return EN;
            case "Spanish": return ES;
            case "German": return GER;
            case "Italian": return IT;
            default: return null;
        }
    }
}
