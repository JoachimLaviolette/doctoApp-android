package if26.android.doctoapp.Models;

import java.io.Serializable;

public class Reason implements Serializable {
    private long id;
    private Doctor doctor;
    private String description;

    // Constructor
    public Reason(long id, Doctor doctor, String description) {
        this.id = id;
        this.doctor = doctor;
        this.description = description;
    }

    // Getters and setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
