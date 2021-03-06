package if26.android.doctoapp.Models;

import java.io.Serializable;

public class Booking implements Serializable {
    private long id;
    private Patient patient;
    private Doctor doctor;
    private Reason reason;
    private String fullDate; // appointment date, as Day, Month X
    private String date; // appointment date, as YYYY-MM-DD
    private String time; // appointment time
    private String bookingDate; // when the booking has been made

    // Constructor
    public Booking(long id, Patient patient, Doctor doctor, Reason reason, String fullDate, String date, String time, String bookingDate) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.reason = reason;
        this.fullDate = fullDate;
        this.date = date;
        this.time = time;
        this.bookingDate = bookingDate;
    }

    // Getters and setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getFullDate() {
        return this.fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
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

    // Transitive getters and setters
    public long GetDoctorId() {
        return this.doctor.getId();
    }

    public long GetPatientId() {
        return this.patient.getId();
    }

    public long GetReasonId() {
        return this.reason.getId();
    }

    public String GetDoctorFullname() {
        return this.doctor.getFullname();
    }
}
