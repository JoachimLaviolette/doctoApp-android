package if26.android.doctoapp.Models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Availability implements Serializable {
    private Doctor doctor;
    private String date;
    private String time;

    // Constructor
    public Availability(Doctor doctor, String date, String time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Availability)) return false;

        Availability a = (Availability) obj;

        if (a.getDoctor() != null && this.getDoctor() != null) {
            if (!a.getDoctor().equals(this.getDoctor())) return false;
        }

        if ((a.getDoctor() == null && this.getDoctor() != null)
                || (a.getDoctor() != null && this.getDoctor() == null)) return false;
        if (!a.getDate().equals(this.getDate())) return false;

        return a.getTime().equals(this.getTime());
    }
}
