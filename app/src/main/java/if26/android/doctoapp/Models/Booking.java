package if26.android.doctoapp.Models;

public class Booking {
    private Patient patient;
    private Doctor doctor;
    private Reason reason;
    private String date; // appointment date
    private String time; // appointment time
    private String bookingDate; // when the booking has been made

    // Constructor
    public Booking(Patient patient, Doctor doctor, Reason reason, String date, String time, String bookingDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.reason = reason;
        this.date = date;
        this.time = time;
        this.bookingDate = bookingDate;
    }

    // Getters and setters
    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Reason getReason() {
        return this.reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
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

    public String getBookingDate() {
        return this.bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
