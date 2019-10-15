package if26.android.doctoapp.Models;

public class Education {
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
}
