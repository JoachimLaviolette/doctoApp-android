package if26.android.doctoapp.Models;

import java.util.HashSet;
import java.util.Set;

public class Patient {
    private long id;
    private String lastname;
    private String firstname;
    private String birthdate;
    private String email;
    private String insurance_number;
    private Address address;
    private String lastLogin;
    private Set<Booking> appointments;

    public Patient(long id, String lastname, String firstname, String birthdate, String email, String insurance_number, Address address, String lastLogin) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.email = email;
        this.insurance_number = insurance_number;
        this.address = address;
        this.lastLogin = lastLogin;
        this.appointments = new HashSet<>();
    }

    public Patient(long id, String lastname, String firstname, String birthdate, String email, String insurance_number, Address address, String lastLogin, Set<Booking> appointments) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.email = email;
        this.insurance_number = insurance_number;
        this.address = address;
        this.lastLogin = lastLogin;
        this.appointments = appointments;
    }

    // Getters and setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInsurance_number() {
        return this.insurance_number;
    }

    public void setInsurance_number(String insurance_number) {
        this.insurance_number = insurance_number;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Set<Booking> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Booking> appointments) {
        this.appointments = appointments;
    }

    // Add methods
    public void addAppointment(Booking b) {
        this.appointments.add(b);
    }

    // Remove methods
    public void removeAppointment(Booking b) {
        if (this.appointments.contains(b))
            this.appointments.remove(b);
    }
}
