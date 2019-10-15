package if26.android.doctoapp.Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.DatabaseHelpers.DoctoAppDatabaseContract;

public class Doctor {
    private long id;
    private String lastname;
    private String firstname;
    private String speciality;
    private String email;
    private String pwd;
    private String pwdSalt;
    private String description;
    private boolean isUnderAgreement;
    private boolean isHealthInsuranceCard;
    private boolean isThirdPartyPayment;
    private Address address;
    private String lastLogin;
    private List<Availability> availabilities;
    private Set<Language> languages;
    private Set<PaymentOption> paymentOptions;
    private List<Reason> reasons;
    private List<Education> trainings;
    private List<Experience> experiences;
    private Set<Booking> appointments;

    // Constructors
    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.speciality = speciality;
        this.email = email;
        this.description = description;
        this.pwd = pwd;
        this.pwdSalt = pwdSalt;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.address = address;
        this.lastLogin = lastLogin;
        this.availabilities = new ArrayList<>();
        this.languages = new HashSet<>();
        this.paymentOptions = new HashSet<>();
        this.reasons = new ArrayList<>();
        this.trainings = new ArrayList<>();
        this.experiences = new ArrayList<>();
        this.appointments = new HashSet<>();
    }

    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin, List<Availability> availabilities, Set<Language> languages, Set<PaymentOption> paymentOptions, List<Reason> reasons, List<Education> trainings, List<Experience> experiences, Set<Booking> appointments) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.speciality = speciality;
        this.email = email;
        this.description = description;
        this.pwd = pwd;
        this.pwdSalt = pwdSalt;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.address = address;
        this.lastLogin = lastLogin;
        this.availabilities = availabilities;
        this.languages = languages;
        this.paymentOptions = paymentOptions;
        this.reasons = reasons;
        this.trainings = trainings;
        this.experiences = experiences;
        this.appointments = appointments;
    }

    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin, List<Availability> availabilities, Set<Language> languages, Set<PaymentOption> paymentOptions, List<Reason> reasons, List<Education> trainings, List<Experience> experiences) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.speciality = speciality;
        this.email = email;
        this.description = description;
        this.pwd = pwd;
        this.pwdSalt = pwdSalt;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.address = address;
        this.lastLogin = lastLogin;
        this.availabilities = availabilities;
        this.languages = languages;
        this.paymentOptions = paymentOptions;
        this.reasons = reasons;
        this.trainings = trainings;
        this.experiences = experiences;
        this.appointments = new HashSet<>();
    }

    public Doctor(Map<String, Object> doctorData) {
        this.id = Long.parseLong(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ID).toString());
        this.lastname = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_LASTNAME).toString();
        this.firstname = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_FIRSTNAME).toString();
        this.speciality = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_SPECIALITY).toString();
        this.email = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_EMAIL).toString();
        this.description = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_DESCRIPTION).toString();
        this.pwd = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_PWD).toString();
        this.pwdSalt = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_PWD_SALT).toString();
        this.isUnderAgreement = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_UNDER_AGREEMENT).toString());
        this.isHealthInsuranceCard = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_HEALTH_INSURANCE_CARD).toString());
        this.isThirdPartyPayment = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_THIRD_PARTY_PAYMENT).toString());
        this.address = new Address(
                Long.parseLong(doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_ID).toString()),
                doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET1).toString(),
                doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_STREET2).toString(),
                doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_CITY).toString(),
                doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_ZIP).toString(),
                doctorData.get(DoctoAppDatabaseContract.Address.COLUMN_NAME_COUNTRY).toString()
        );
        this.lastLogin = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_LAST_LOGIN).toString();
        this.availabilities = (List<Availability>) doctorData.get(DoctoAppDatabaseContract.Availability.TABLE_NAME);
        this.languages = (Set<Language>) doctorData.get(DoctoAppDatabaseContract.Language.TABLE_NAME);
        this.paymentOptions = (Set<PaymentOption>) doctorData.get(DoctoAppDatabaseContract.PaymentOption.TABLE_NAME);
        this.reasons = (List<Reason>) doctorData.get(DoctoAppDatabaseContract.Reason.TABLE_NAME);
        this.trainings = (List<Education>) doctorData.get(DoctoAppDatabaseContract.Education.TABLE_NAME);
        this.experiences = (List<Experience>) doctorData.get(DoctoAppDatabaseContract.Experience.TABLE_NAME);
        this.appointments = (Set<Booking>) doctorData.get(DoctoAppDatabaseContract.Booking.TABLE_NAME);
    }

    // Getters and setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;

        // Automatically updates all related data
        this.UpdateAvailabilitiesDoctorId();
        this.UpdateReasonsDoctorId();
        this.UpdateTrainingsDoctorId();
        this.UpdateExperiencesDoctorId();
        // TODO : this.UpdateAppointmentsDoctorId();
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

    public String getSpeciality() {
        return this.speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUnderAgreement() {
        return this.isUnderAgreement;
    }

    public void setUnderAgreement(boolean underAgreement) {
        isUnderAgreement = underAgreement;
    }

    public boolean isHealthInsuranceCard() {
        return this.isHealthInsuranceCard;
    }

    public void setHealthInsuranceCard(boolean healthInsuranceCard) {
        isHealthInsuranceCard = healthInsuranceCard;
    }

    public boolean isThirdPartyPayment() {
        return this.isThirdPartyPayment;
    }

    public void setThirdPartyPayment(boolean thirdPartyPayment) {
        isThirdPartyPayment = thirdPartyPayment;
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

    public List<Availability> getAvailabilities() {
        return this.availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<PaymentOption> getPaymentOptions() {
        return this.paymentOptions;
    }

    public void setPaymentOptions(Set<PaymentOption> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<Reason> getReasons() {
        return this.reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

    public List<Education> getTrainings() {
        return this.trainings;
    }

    public void setTrainings(List<Education> trainings) {
        this.trainings = trainings;
    }

    public List<Experience> getExperiences() {
        return this.experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public Set<Booking> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Booking> appointments) {
        this.appointments = appointments;
    }

    // Add methods
    public void addAvailability(Availability a) {
        this.availabilities.add(a);
    }

    public void addLanguage(Language l) {
        this.languages.add(l);
    }

    public void addPaymentOption(PaymentOption p) {
        this.paymentOptions.add(p);
    }

    public void addReason(Reason r) {
        this.reasons.add(r);
    }

    public void addTraining(Education t) {
        this.trainings.add(t);
    }

    public void addExperience(Experience e) {
        this.experiences.add(e);
    }

    public void addAppointment(Booking b) {
        this.appointments.add(b);
    }

    // Remove methods
    public void removeAvailability(Availability a) {
        if (this.availabilities.contains(a))
            this.availabilities.remove(a);
    }

    public void removeLanguage(Language l) {
        if (this.languages.contains(l))
            this.languages.remove(l);
    }

    public void removePaymentOption(PaymentOption p) {
        if (this.paymentOptions.contains(p))
            this.paymentOptions.remove(p);
    }

    public void removeReason(Reason r) {
        if (this.reasons.contains(r))
            this.reasons.remove(r);
    }

    public void removeTraining(Education t) {
        if (this.trainings.contains(t))
            this.trainings.remove(t);
    }

    public void removeExperience(Experience e) {
        if (this.experiences.contains(e))
            this.experiences.remove(e);
    }

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

    // Update methods
    private void UpdateAppointmentsDoctorId() {
        for (Booking a: this.appointments) a.setDoctor(this);
    }

    private void UpdateExperiencesDoctorId() {
        for (Experience e: this.experiences) e.setDoctor(this);
    }

    private void UpdateTrainingsDoctorId() {
        for (Education t: this.trainings) t.setDoctor(this);
    }

    private void UpdateReasonsDoctorId() {
        for (Reason r: this.reasons) r.setDoctor(this);
    }

    private void UpdateAvailabilitiesDoctorId() {
        for (Availability a: this.availabilities) a.setDoctor(this);
    }

    public void SetReasonId(Reason r, long reasonId) {
        if (this.reasons.contains(r))
            r.setId(reasonId);
    }
}
