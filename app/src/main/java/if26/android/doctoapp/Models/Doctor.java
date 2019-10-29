package if26.android.doctoapp.Models;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import if26.android.doctoapp.Codes.ErrorCode;
import if26.android.doctoapp.DatabaseHelpers.DoctoAppDatabaseContract;
import if26.android.doctoapp.DatabaseHelpers.DoctorDatabaseHelper;
import if26.android.doctoapp.R;
import if26.android.doctoapp.Services.DateTimeService;
import if26.android.doctoapp.Services.StringFormatterService;

public class Doctor extends Resident {
    private String speciality;
    private String description;
    private String contactNumber;
    private boolean isUnderAgreement;
    private boolean isHealthInsuranceCard;
    private boolean isThirdPartyPayment;
    private String header;
    private List<Availability> availabilities;
    private Set<Language> languages;
    private Set<PaymentOption> paymentOptions;
    private List<Reason> reasons;
    private List<Education> trainings;
    private List<Experience> experiences;

    // Constructors
    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String contactNumber, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin, String picture, String header) {
        super(
                id,
                lastname,
                firstname,
                email,
                pwd,
                pwdSalt,
                address,
                lastLogin,
                picture
        );
        this.speciality = speciality;
        this.description = description;
        this.contactNumber = contactNumber;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.header = header;
        this.availabilities = new ArrayList<>();
        this.languages = new LinkedHashSet<>();
        this.paymentOptions = new LinkedHashSet<>();
        this.reasons = new LinkedList<>();
        this.trainings = new LinkedList<>();
        this.experiences = new LinkedList<>();
    }

    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String contactNumber, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin, String picture, String header, List<Availability> availabilities, Set<Language> languages, Set<PaymentOption> paymentOptions, List<Reason> reasons, List<Education> trainings, List<Experience> experiences, Set<Booking> appointments) {
        super(
                id,
                lastname,
                firstname,
                email,
                pwd,
                pwdSalt,
                address,
                lastLogin,
                picture,
                appointments
        );
        this.speciality = speciality;
        this.description = description;
        this.contactNumber = contactNumber;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.header = header;
        this.availabilities = availabilities;
        this.languages = languages;
        this.paymentOptions = paymentOptions;
        this.reasons = reasons;
        this.trainings = trainings;
        this.experiences = experiences;
    }

    public Doctor(long id, String lastname, String firstname, String speciality, String email, String description, String contactNumber, String pwd, String pwdSalt, boolean isUnderAgreement, boolean isHealthInsuranceCard, boolean isThirdPartyPayment, Address address, String lastLogin, String picture, String header, List<Availability> availabilities, Set<Language> languages, Set<PaymentOption> paymentOptions, List<Reason> reasons, List<Education> trainings, List<Experience> experiences) {
        super(
                id,
                lastname,
                firstname,
                email,
                pwd,
                pwdSalt,
                address,
                lastLogin,
                picture
        );
        this.speciality = speciality;
        this.description = description;
        this.contactNumber = contactNumber;
        this.isUnderAgreement = isUnderAgreement;
        this.isHealthInsuranceCard = isHealthInsuranceCard;
        this.isThirdPartyPayment = isThirdPartyPayment;
        this.header = header;
        this.availabilities = availabilities;
        this.languages = languages;
        this.paymentOptions = paymentOptions;
        this.reasons = reasons;
        this.trainings = trainings;
        this.experiences = experiences;
    }

    public Doctor(Map<String, Object> doctorData) {
        super(doctorData);
        this.id = Long.parseLong(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_ID).toString());
        this.lastname = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_LASTNAME).toString();
        this.firstname = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_FIRSTNAME).toString();
        this.speciality = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_SPECIALITY).toString();
        this.email = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_EMAIL).toString();
        this.description = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_DESCRIPTION).toString();
        this.contactNumber = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_CONTACT_NUMBER).toString();
        this.pwd = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_PWD).toString();
        this.pwdSalt = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_PWD_SALT).toString();
        this.isUnderAgreement = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_UNDER_AGREEMENT).toString());
        this.isHealthInsuranceCard = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_HEALTH_INSURANCE_CARD).toString());
        this.isThirdPartyPayment = Boolean.parseBoolean(doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_IS_THIRD_PARTY_PAYMENT).toString());
        this.address = (Address) doctorData.get(DoctoAppDatabaseContract.Address.TABLE_NAME);
        this.lastLogin = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_LAST_LOGIN).toString();
        this.picture = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_PICTURE).toString();
        this.header = doctorData.get(DoctoAppDatabaseContract.Doctor.COLUMN_NAME_HEADER).toString();
        this.availabilities = (List<Availability>) doctorData.get(DoctoAppDatabaseContract.Availability.TABLE_NAME);
        this.languages = (Set<Language>) doctorData.get(DoctoAppDatabaseContract.Language.TABLE_NAME);
        this.paymentOptions = (Set<PaymentOption>) doctorData.get(DoctoAppDatabaseContract.PaymentOption.TABLE_NAME);
        this.reasons = (List<Reason>) doctorData.get(DoctoAppDatabaseContract.Reason.TABLE_NAME);
        this.trainings = (List<Education>) doctorData.get(DoctoAppDatabaseContract.Education.TABLE_NAME);
        this.experiences = (List<Experience>) doctorData.get(DoctoAppDatabaseContract.Experience.TABLE_NAME);
        this.appointments = (Set<Booking>) doctorData.get(DoctoAppDatabaseContract.Booking.TABLE_NAME);
    }

    // Getters and setters
    public String getSpeciality() {
        return this.speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    // Super getters and setters
    /**
     * Return the contact number as a string
     * @return The contact number
     */
    public String getContactNumberAsString() {
        String contactNumber = this.contactNumber.replaceAll(" ", "").trim();

        if (contactNumber.length() != 10
            && contactNumber.length() != 12)
            return ErrorCode.DATA_FORMAT_ERROR;

        if (contactNumber.startsWith("+"))
            return contactNumber.substring(0, 3) +
                    " " +
                    contactNumber.charAt(3) +
                    " " +
                    contactNumber.substring(4, 6) +
                    " " +
                    contactNumber.substring(6, 8) +
                    " " +
                    contactNumber.substring(8, 10) +
                    " " +
                    contactNumber.substring(10);

        return contactNumber.substring(0, 2) +
                " " +
                contactNumber.substring(2, 4) +
                " " +
                contactNumber.substring(4, 6) +
                " " +
                contactNumber.substring(6, 8) +
                " " +
                contactNumber.substring(8);
    }

    /**
     * Return prices and refunds data as a string
     * @return The prices and refunds data
     */
    public String getPricesAndRefundsAsString(Context context) {
        Resources resources = context.getResources();

        String isUnderAgreement = "- ";
        isUnderAgreement += this.isUnderAgreement ?
                resources.getString(R.string.doctor_profile_is_under_agreement_true) : resources.getString(R.string.doctor_profile_is_under_agreement_false);

        String isHealthInsuranceCard = "- ";
        isHealthInsuranceCard += this.isHealthInsuranceCard() ?
                resources.getString(R.string.doctor_profile_is_health_insurance_card_true) : resources.getString(R.string.doctor_profile_is_health_insurance_card_false);

        String isThirdPartyPayment = this.isThirdPartyPayment ?
                "- " + resources.getString(R.string.doctor_profile_is_third_party_payment_true) : "";

        return (isUnderAgreement + "\n"
                + isHealthInsuranceCard + "\n"
                + isThirdPartyPayment).trim();
    }

    /**
     * Return doctor payment options as a string
     * @return The payment options
     */
    public String getPaymentOptionsAsString() {
        String paymentOptions = "";

        for (PaymentOption po: this.paymentOptions) paymentOptions += po.toString().toLowerCase().replace("_", " ") + ", ";

        if (paymentOptions.isEmpty()) return paymentOptions;

        paymentOptions = StringFormatterService.Capitalize(paymentOptions);
        paymentOptions = paymentOptions.substring(0, paymentOptions.lastIndexOf(","));

        return paymentOptions;
    }

    /**
     * Return doctor languages as a string
     * @return The languages
     */
    public String getLanguagesAsString() {
        String languages = "";

        for (Language l: this.languages) languages += "- " + l.toString().toUpperCase() + "\n";

        return languages.trim();
    }

    /**
     * Return trainings as a string
     * @return The trainings
     */
    public String getTrainingsAsString() {
        String trainings = "";

        for (Education t: this.trainings) trainings += "- " + t.getYear() + ": " + t.getDegree() + "\n";

        return trainings.trim();
    }

    /**
     * Return experiences as a string
     * @return The experiences
     */
    public String getExperiencesAString() {
        String experiences = "";

        for (Experience e: this.experiences) experiences += "- " + e.getYear() + ": " + e.getDescription() + "\n";

        return experiences.trim();
    }

    /**
     * Organize the availabilities by day
     * @param weeksNumber On how many weeks to display
     * @return A map containing all the availabilities per day
     */
    public Map<String, List<Availability>> getAvailabilitiesPerDay(int weeksNumber) {
        Map<String, List<Availability>> availabilitiesPerDay = new LinkedHashMap<>();

        // Thursday, October 17
        // Friday, October 18
        // Monday, October 21
        // ...
        // Thursday, October 24 // if weeksNumber == 1
        // ...
        // Thursday, October 31 // if weeksNumber == 2

        Map<String, List<Availability>> availabilitiesPerDayRef = new LinkedHashMap<>();

        for (Availability a : this.availabilities) {
            if (!availabilitiesPerDayRef.containsKey(a.getDate())) {
                availabilitiesPerDayRef.put(a.getDate(), new ArrayList<Availability>());
            }
        }

        for (int x = 0; x <= 7 * weeksNumber; x++) {
            String date = DateTimeService.GetDateFromCurrent(x);
            if (availabilitiesPerDayRef.containsKey(DateTimeService.GetDayFromDate(date)))
                availabilitiesPerDay.put(date, new ArrayList<Availability>());
        }


        for (Availability a : this.availabilities) {
            if (availabilitiesPerDayRef.containsKey(a.getDate()))
                availabilitiesPerDayRef.get(a.getDate()).add(a);
        }

        for (String date : availabilitiesPerDay.keySet()) {
            String day = DateTimeService.GetDayFromDate(date);
            if (availabilitiesPerDayRef.containsKey(day)) {
                for (Availability a: availabilitiesPerDayRef.get(day)) {
                    availabilitiesPerDay.get(date).add(
                            new Availability(
                                    a.getDoctor(),
                                    date,
                                    a.getTime()
                            )
                    );
                }
            }
        }

        return availabilitiesPerDay;
    }

    // Update methods
    public void UpdateRelatedData() {
        this.UpdateAvailabilitiesDoctorId();
        this.UpdateReasonsDoctorId();
        this.UpdateTrainingsDoctorId();
        this.UpdateExperiencesDoctorId();
        this.UpdateAppointmentsResidentId();
    }

    private void UpdateAvailabilitiesDoctorId() { for (Availability a: this.availabilities) a.setDoctor(this); }

    private void UpdateReasonsDoctorId() {
        for (Reason r: this.reasons) r.setDoctor(this);
    }

    private void UpdateTrainingsDoctorId() {
        for (Education t: this.trainings) t.setDoctor(this);
    }

    private void UpdateExperiencesDoctorId() { for (Experience e: this.experiences) e.setDoctor(this); }

    protected void UpdateAppointmentsResidentId() { for (Booking a: this.appointments) a.setDoctor(this); }

    /**
     * Update the current doctor
     * @param context The calling activity
     * @return The doctor with refreshed data
     */
    public Resident Update(Context context) { return new DoctorDatabaseHelper(context).GetDoctorById(this.id + ""); }

    // Transitive getters and setters
    public void SetReasonId(Reason r, long reasonId) { if (this.reasons.contains(r)) r.setId(reasonId); }
}
