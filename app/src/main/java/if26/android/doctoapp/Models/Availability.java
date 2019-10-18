package if26.android.doctoapp.Models;

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
}
