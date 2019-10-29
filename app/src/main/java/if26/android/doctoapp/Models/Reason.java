package if26.android.doctoapp.Models;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Reason)) return false;

        Reason r = (Reason) obj;

        if (r.getDoctor() != null && this.getDoctor() != null) {
            if (!r.getDoctor().equals(this.getDoctor())) return false;
        }

        if ((r.getDoctor() == null && this.getDoctor() != null)
                || (r.getDoctor() != null && this.getDoctor() == null)) return false;

        return r.getDescription().equals(this.getDescription());
    }
}
