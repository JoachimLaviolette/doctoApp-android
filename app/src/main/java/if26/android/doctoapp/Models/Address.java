package if26.android.doctoapp.Models;

public class Address {
    private long id;
    private String street1;
    private String street2;
    private String city;
    private String zip;
    private String country;

    // Constructor
    public Address(long id, String street1, String street2, String city, String zip, String country) {
        this.id = id;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    // Getters and setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet1() {
        return this.street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
