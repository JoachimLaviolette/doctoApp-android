package if26.android.doctoapp.Models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public enum Language implements Serializable {
    FR { @NotNull
    public String toString() { return "French"; } },
    EN { @NotNull
    public String toString() { return "English"; } },
    ES { @NotNull
    public String toString() { return "Spanish"; } },
    GER { @NotNull
    public String toString() { return "German"; } },
    IT { @NotNull
    public String toString() { return "Italian"; } };

    /**
     * Get the Language value of the given string representation
     * @param languageName The string representation of the language
     * @return The language value associated
     */
    public static Language GetValueOf(String languageName) {
        switch (languageName) {
            case "French": return FR;
            case "English": return EN;
            case "Spanish": return ES;
            case "German": return GER;
            case "Italian": return IT;
            default: return null;
        }
    }
}
