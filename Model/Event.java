package Model;

/**
 * Event class keeps track of events for a given person
 */

public class Event {

    private String eventType;       //Type of event (birth, baptism, christening...)
    private String personID;     //ID of person to which event belongs
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private String year;
    private String descendant; //User to which this person belongs
    private String eventID;

    /**
     * Blank public constructor
     */
    public Event() {
        city = "";
        country = "";
        descendant = "";
        eventID = "";
        latitude = "";
        longitude = "";
        personID = "";
        eventType = "";
        year = "";
        this.eventID = "";
    }

    /**
     * Constructor to easily create a filled Event
     *
     * @param eventDescendant
     * @param eventPerson
     * @param eventLatitude
     * @param eventLongitude
     * @param eventCountry
     * @param eventCity
     * @param eventType
     * @param eventYear
     */
    public Event(String eventDescendant, String eventPerson, String eventLatitude, String eventLongitude, String eventCountry, String eventCity, String eventType, String eventYear) {

        this.descendant = eventDescendant;
        this.personID = eventPerson;
        this.latitude = eventLatitude;
        this.longitude = eventLongitude;
        this.country = eventCountry;
        this.city = eventCity;
        this.eventType = eventType;
        this.year = eventYear;
        this.eventID = "";
    }

    @Override
    public int hashCode() {
        return descendant.hashCode() + personID.hashCode() + latitude.hashCode() +
                longitude.hashCode() + country.hashCode() + city.hashCode() +
                eventType.hashCode() + year.hashCode() + eventID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o.hashCode() != this.hashCode()) {
            return false;
        }
        Event other = (Event) o;
        if (other.getEventID() != this.getEventID()) {
            return false;
        }
        return other.hashCode() == this.hashCode();

    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
