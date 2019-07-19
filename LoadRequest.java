package DataTransferObjects;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Created by rdmcl on 2/12/2018.
 */

public class LoadRequest {
    User[] users;
    Person[] persons;
    Event[] events;


    /**
     * Constructor that parses the arrays passed in
     *
     * @param users
     * @param people
     * @param events
     */
    public LoadRequest(User[] users, Person[] people, Event[] events) {
        this.users = users;
        this.persons = people;
        this.events = events;
    }

    public LoadRequest() {
        users = null;
        persons = null;
        events = null;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
