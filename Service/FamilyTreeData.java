package Service;

import java.util.HashSet;
import java.util.Set;

import Handler.FillLocations;
import Handler.FillNames;
import Model.Event;
import Model.Person;


/**
 * Data class to hold the people and event objects with the generated family tree
 */
public class FamilyTreeData {
    private Set<Person> people;
    private Set<Event> events;
    private FillNames male;
    private FillNames female;
    private FillNames last;
    private String descendant;
    private FillLocations locations;
    private FillNames type;

    public FamilyTreeData() {
        people = new HashSet<Person>();
        events = new HashSet<Event>();
        male = new FillNames();
        female = new FillNames();
        last = new FillNames();
        descendant = null;
        locations = new FillLocations();
        type = new FillNames();
    }

    /**
     * Add a new person to the Set
     *
     * @param p is the person to be added
     */
    public void addPerson(Person p) {
        people.add(p);
    }

    /**
     * Add a new event to the Set
     *
     * @param e is the event to be added
     */
    public void addEvent(Event e) {

        events.add(e);
    }

    /**
     * Converts the Hashset of people into an array of People
     *
     * @return the array of People
     */
    public Person[] getPersons() {
        Person[] list = new Person[people.size()];
        int i = 0;
        for (Person human : people) {
            list[i] = human;
            i++;
        }
        return list;
    }

    /**
     * converts the hashset of events into an array of Events
     *
     * @return the array of events
     */
    public Event[] getEvents() {
        Event[] list = new Event[events.size()];
        int i = 0;
        for (Event happiness : events) {
            list[i] = happiness;
            i++;
        }
        return list;
    }

    public FillNames getMale() {
        return male;
    }

    public void setMale(FillNames male) {
        this.male = male;
    }

    public FillNames getFemale() {
        return female;
    }

    public void setFemale(FillNames female) {
        this.female = female;
    }

    public FillNames getLast() {
        return last;
    }

    public void setLast(FillNames last) {
        this.last = last;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public FillLocations getLocations() {
        return locations;
    }

    public void setLocations(FillLocations locations) {
        this.locations = locations;
    }

    public FillNames getType() {
        return type;
    }

    public void setType(FillNames type) {
        this.type = type;
    }
}
