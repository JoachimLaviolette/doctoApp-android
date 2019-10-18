package if26.android.doctoapp.Models;

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
}
