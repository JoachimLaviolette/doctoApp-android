package if26.android.doctoapp.Models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Experience implements Serializable {
    private Doctor doctor;
    private String year;
    private String description;

    // Constructor
    public Experience(Doctor doctor, String year, String description) {
        this.doctor = doctor;
        this.year = year;
        this.description = description;
    }

    // Getters and setters
    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Experience)) return false;

        Experience e = (Experience) obj;

        if (e.getDoctor() != null && this.getDoctor() != null) {
            if (!e.getDoctor().equals(this.getDoctor())) return false;
        }

        if ((e.getDoctor() == null && this.getDoctor() != null)
                || (e.getDoctor() != null && this.getDoctor() == null)) return false;
        if (!e.getYear().equals(this.getYear())) return false;

        return e.getDescription().equals(this.getDescription());
    }
}
