package if26.android.doctoapp.Models;

import android.content.Context;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class Resident implements Serializable {
    protected long id;
    protected String lastname;
    protected String firstname;
    protected String email;
    protected String pwd;
    protected String pwdSalt;
    protected String lastLogin;
    protected Address address;
    protected Set<Booking> appointments;

    public Resident(long id, String lastname, String firstname, String email, String pwd, String pwdSalt, Address address, String lastLogin) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.pwd = pwd;
        this.pwdSalt = pwdSalt;
        this.address = address;
        this.lastLogin = lastLogin;
        this.appointments = new LinkedHashSet<>();
    }

    public Resident(long id, String lastname, String firstname, String email, String pwd, String pwdSalt, Address address, String lastLogin, Set<Booking> appointments) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.pwd = pwd;
        this.pwdSalt = pwdSalt;
        this.address = address;
        this.lastLogin = lastLogin;
        this.appointments = appointments;
    }

    public Resident(Map<String, Object> residentData) {}

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

    public String getFullname() {
        return this.firstname + " " + getLastname().toUpperCase();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwdSalt() {
        return this.pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
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

    // Transitive getters and setters
    public String GetZip() {
        return this.address.getZip();
    }

    public String GetStreet2() {
        return this.address.getStreet2();
    }

    public String GetStreet1() {
        return this.address.getStreet1();
    }

    public String GetCountry() {
        return this.address.getCountry();
    }

    public String GetCity() {
        return this.address.getCity();
    }

    public long GetAddressId() {
        return this.address.getId();
    }

    public void SetAddressId(long id) {
        this.address.setId(id);
    }

    public String GetCityCountry() {
        return this.GetCity() + ", " + this.GetCountry().toUpperCase();
    }

    public String GetFullAddress() {
        return this.GetStreet1() + ", " + this.GetStreet2() + "\n"
                + this.GetZip() + ", " + this.GetCity() + "\n"
                + this.GetCountry();
    }


    // Update methods
    public abstract void UpdateRelatedData();
    protected abstract void UpdateAppointmentsResidentId();

    public abstract Resident Update(Context context);
}
