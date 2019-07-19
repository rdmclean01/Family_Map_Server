package Model;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by rdmcl on 3/21/2018.
 */

public class Search {
    Set<Person> mPeople;
    Set<Event> mEvents;

    public Person[] getPeople() {
        if (mPeople != null) {
            Person[] people = new Person[mPeople.size()];
            int position = 0;
            for (Person p : mPeople) {
                people[position] = p;
                position++;
            }
            return people;
        }
        return new Person[0];
    }

    public void addToPeople(Person p) {
        if (mPeople == null) {
            mPeople = new HashSet<>();
        }
        mPeople.add(p);
    }

    public Event[] getEvents() {
        if (mEvents != null) {
            Event[] events = new Event[mEvents.size()];
            int position = 0;
            for (Event e : mEvents) {
                events[position] = e;
                position++;
            }
            return events;
        }
        return new Event[0];
    }

    public void addToEvents(Event event) {
        if (mEvents == null) {
            mEvents = new HashSet<>();
        }
        mEvents.add(event);
    }

    public void search(String str) {
        str = str.toLowerCase();
        mPeople = new HashSet<>();
        mEvents = new HashSet<>();
        if(str.equals("")){
            return;
        }
        for (Person p : Model.singleton.getFilteredPeople()) {
            if (p.getFirstName().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToPeople(p);
            }
            if (p.getLastName().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToPeople(p);
            }
        }

        for (Event e : Model.singleton.getFilteredEvents()) {
            if (e.getCountry().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToEvents(e);
            }
            if (e.getCity().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToEvents(e);

            }
            if (e.getEventType().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToEvents(e);

            }
            if (e.getYear().toLowerCase().contains(str)) {
                Model.singleton.getSearch().addToEvents(e);
            }
        }
    }
}
