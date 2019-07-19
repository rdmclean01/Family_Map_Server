package Model;

import android.view.Display;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import Model.Person;

/**
 * Created by rdmcl on 3/16/2018.
 */

public class Model {

    public static Model singleton = new Model();


    // Store Login Response data
    private String authToken;
    private String userName;

    private String password;

    private String personId;
    private String message;
    // Person Data
    private Person user;


    Map<String, Person> people; // Links person objects up to their ID number
    Set<String> paternal;      // List of all the paternal ancestors of the user
    Set<String> maternal;      // List of all the maternal ancestors of the user
    HashSet<Person> filteredPeople; // Lists all the filtered people

    Map<String, Person> children; // Links up the ID to all the children todo: children of whom?
    // Event Data
    Map<String, Event> events;  // Links event objects to their person ID number
    Set<String> eventTypes;    // Keeps track of the event types for the filter
    HashSet<Event> filteredEvents;
    Map<String, Float> typeColor; // Links up the eventTypes to a certain color

    // Other Data
    Settings settings;          // Root node for all the settings
    Filter filter;              // Root node for all the filter settings
    Search mSearch;             // Root node for all the search objects

    public Search getSearch() {
        if (mSearch == null) {
            mSearch = new Search();
        }
        return mSearch;
    }

    public void clearData() {

        Model.singleton.people.clear();
        Model.singleton.paternal = null;
        Model.singleton.maternal = null;

        Model.singleton.events.clear();
        Model.singleton.eventTypes = null;
        Model.singleton.filteredEvents = null;
        Model.singleton.typeColor = null;
    }

    public void clearSettings() {
        Model.singleton.settings = null;
        Model.singleton.filter = null;
    }

    public Filter getFilter() {

        return filter;
    }

    public void setFilter(int size) {
        if (filter == null) {
            filter = new Filter();
            filter.FilterSettings = new boolean[size];
            for (int i = 0; i < size; i++) {
                filter.FilterSettings[i] = true;
            }
        }
        return;
    }

    public Map<String, Person> getChildren() {
        if (children == null) {
            children = new TreeMap<>();
        }
        for (String keyParent : Model.singleton.getPeople().keySet()) {
            Person parent = Model.singleton.getPeople().get(keyParent);
            for (String keyChild : Model.singleton.getPeople().keySet()) {
                Person child = Model.singleton.getPeople().get(keyChild);
                if (child.getFather().equals(parent.getPersonID())) {
                    if (!children.containsKey(parent.getPersonID())) {
                        children.put(parent.getPersonID(), child);
                    }
                }
                if (child.getMother().equals(parent.getPersonID())) {
                    if (!children.containsKey(parent.getPersonID())) {
                        children.put(parent.getPersonID(), child);
                    }
                }
            }
        }


        return children;

    }

    public HashSet<Event> getFilteredEvents() {
        boolean includeMales = true;
        boolean includeFemales = true;
        boolean includePaternal = true;
        boolean includeMaternal = true;

        filteredEvents = new HashSet<>();

        for (String key : Model.singleton.events.keySet()) {
            String curType = Model.singleton.events.get(key).getEventType();
            if (Model.singleton.getFilter() != null) {
                // Grab all the filtered events from the event types
                for (int i = 0; i < Model.singleton.getFilter().getFilterSettings().length; i++) {
                    //String foo = Model.singleton.getFilter().typeData[i];
                    if (Model.singleton.getFilter().typeData[i].equals(curType)) {
                        if (Model.singleton.getFilter().getFilterSettings()[i]) {
                            filteredEvents.add(Model.singleton.getEvents().get(key));
                        }
                    }
                    if (Model.singleton.getFilter().typeData[i].equals("male")) {
                        includeMales = Model.singleton.getFilter().getFilterSettings()[i];
                    }
                    if (Model.singleton.getFilter().typeData[i].equals("female")) {
                        includeFemales = Model.singleton.getFilter().getFilterSettings()[i];
                    }
                    if (Model.singleton.getFilter().typeData[i].equals("paternal")) {
                        includePaternal = Model.singleton.getFilter().getFilterSettings()[i];
                    }
                    if (Model.singleton.getFilter().typeData[i].equals("maternal")) {
                        includeMaternal = Model.singleton.getFilter().getFilterSettings()[i];
                    }
                }
            } else {
                filteredEvents.add(Model.singleton.getEvents().get(key));
            }
        }
        for (String key : Model.singleton.getEvents().keySet()) {
            Event e = Model.singleton.getEvents().get(key);
            if (!includeMales) {
                if (Model.singleton.getPeople().get(e.getPersonID()).getGender().toLowerCase().equals("m")) {
                    if (filteredEvents.contains(e)) {
                        filteredEvents.remove(e);
                    }
                }
            }
            if (!includeFemales) {
                if (Model.singleton.getPeople().get(e.getPersonID()).getGender().toLowerCase().equals("f")) {
                    if (filteredEvents.contains(e)) {
                        filteredEvents.remove(e);
                    }
                }
            }
            if (!includePaternal) {
                if (Model.singleton.getPaternal().contains(e.getPersonID())) {
                    if (filteredEvents.contains(e)) {
                        filteredEvents.remove(e);
                    }
                }
            }
            if (!includeMaternal) {
                if (Model.singleton.getMaternal().contains(e.getPersonID())) {
                    if (filteredEvents.contains(e)) {
                        filteredEvents.remove(e);
                    }
                }
            }
        }
        return filteredEvents;
    }

    public HashSet<Person> getFilteredPeople() {
        filteredPeople = new HashSet<>();
        boolean includeMales = true;
        boolean includeFemales = true;
        boolean includePaternal = true;
        boolean includeMaternal = true;
        if (Model.singleton.getFilter() != null) {
            for (int i = 0; i < Model.singleton.getFilter().getFilterSettings().length; i++) {
                if (Model.singleton.getFilter().typeData[i].equals("male")) {
                    includeMales = Model.singleton.getFilter().getFilterSettings()[i];
                }
                if (Model.singleton.getFilter().typeData[i].equals("female")) {
                    includeFemales = Model.singleton.getFilter().getFilterSettings()[i];
                }
                if (Model.singleton.getFilter().typeData[i].equals("paternal")) {
                    includePaternal = Model.singleton.getFilter().getFilterSettings()[i];
                }
                if (Model.singleton.getFilter().typeData[i].equals("maternal")) {
                    includeMaternal = Model.singleton.getFilter().getFilterSettings()[i];
                }
            }
        } else {
        }
        for (String key : Model.singleton.getPeople().keySet()) {
            filteredPeople.add(Model.singleton.getPeople().get(key));
        }
        for (String key : Model.singleton.getPeople().keySet()) {
            Person cur = Model.singleton.getPeople().get(key);
            if (!includeMales) {
                if (cur.getGender().toLowerCase().equals("m")) {
                    if (filteredPeople.contains(cur)) {
                        filteredPeople.remove(cur);
                    }
                }
            }
            if (!includeFemales) {
                if (cur.getGender().toLowerCase().equals("f")) {
                    if (filteredPeople.contains(cur)) {
                        filteredPeople.remove(cur);
                    }
                }
            }
            if (!includePaternal) {
                if (Model.singleton.getPaternal().contains(cur.getPersonID())) {
                    if (filteredPeople.contains(cur)) {
                        filteredPeople.remove(cur);
                    }
                }
            }
            if (!includeMaternal) {
                if (Model.singleton.getMaternal().contains(cur.getPersonID())) {
                    if (filteredPeople.contains(cur)) {
                        filteredPeople.remove(cur);
                    }
                }
            }
        }

        return filteredPeople;
    }

    public Set<String> getPaternal() {

        if (paternal == null) {
            paternal = new HashSet<>();
            recurse_ancestors(Model.singleton.user.getFather(), paternal);
        }
        return paternal;
    }

    public Set<String> getMaternal() {
        if (maternal == null) {
            maternal = new HashSet<>();
            recurse_ancestors(Model.singleton.user.getMother(), maternal);
        }
        return maternal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void recurse_ancestors(String personID, Set<String> ancestors) {
        if (personID.equals("")) {
            return;
        }
        Person person = Model.singleton.getPeople().get(personID);
        ancestors.add(person.getPersonID());

        recurse_ancestors(person.getFather(), ancestors);
        recurse_ancestors(person.getMother(), ancestors);
        return;
    }

    public Settings getSettings() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    public void addToPeople(Person person) {
        String ID = person.getPersonID();

        if (people == null) {
            people = new HashMap<>();
        }
        if (!people.containsKey(ID)) {
            people.put(ID, person);
        }
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void addToEvents(Event event) {
        String ID = event.getEventID();
        if (events == null) {
            events = new HashMap<>();
        }
        if (!events.containsKey(ID)) {
            events.put(ID, event);
        }
    }

    public Person getUser() {
        if (user == null) {
            user = new Person();
        }
        return user;
    }

    public void setUser(Person user) {
        if (user == null) {
            user = new Person();
        }
        this.user = user;
    }

    public void setTypeColor() {
        if (eventTypes == null) {
            eventTypes = new HashSet<>();
        }
        // First, construct the event types
        for (Event current : getFilteredEvents()) {
            if (!eventTypes.contains(current.getEventType())) {
                eventTypes.add(current.getEventType());
            }
        }

        typeColor = new HashMap<>();
        float offset = 360 / eventTypes.size();
        float current = 0;
        for (String type : eventTypes) {
            typeColor.put(type, current);
            current += offset;
        }

        return;
    }

    public Set<String> getEventTypes() {

        if (eventTypes == null) {
            eventTypes = new TreeSet<>();
        }
        if (events != null) {
            for (String key : events.keySet()) {
                String cur = getEvents().get(key).getEventType();
                if (!eventTypes.contains(cur)) {
                    eventTypes.add(cur);
                }
            }
        }
        return eventTypes;
    }

    public void setEventTypes(Set<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPersonId() {
        return personId;
    }

    public Map<String, Float> getTypeColor() {
        return typeColor;
    }

    public String getUserName() {
        return userName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setTypeColor(Map<String, Float> typeColor) {
        this.typeColor = typeColor;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
