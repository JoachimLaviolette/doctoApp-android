package if26.android.doctoapp.Models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Education implements Serializable {
    private Doctor doctor;
    private String year;
    private String degree;

    public Education(Doctor doctor, String year, String degree) {
        this.doctor = doctor;
        this.year = year;
        this.degree = degree;
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

    public String getDegree() {
        return this.degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Education)) return false;

        Education e = (Education) obj;

        if (e.getDoctor() != null && this.getDoctor() != null) {
            if (!e.getDoctor().equals(this.getDoctor())) return false;
        }

        if ((e.getDoctor() == null && this.getDoctor() != null)
                || (e.getDoctor() != null && this.getDoctor() == null)) return false;
        
        if (!e.getYear().equals(this.getYear())) return false;

        return e.getDegree().equals(this.getDegree());
    }
}
