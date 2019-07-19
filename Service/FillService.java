package Service;


import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.FillRequest;
import DataTransferObjects.FillResponse;
import Handler.FillLocations;
import Handler.FillNames;
import Model.Event;
import Model.Person;
import Model.User;

public class FillService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Handles the request object and turns it into a response
     *
     * @param request contains the information about the fill
     * @return a populated response object about the fill
     * @throws DataBaseException
     */
    public FillResponse fillService(FillRequest request) throws DataBaseException {

        logger.entering("FillService", "fillService");
        FillResponse response = new FillResponse();
        if (request.getGenerations() < 0) { // Must be a positive number to continue
            response.setMessage("ERROR: Generations must be a non-negative number");
            return response;
        }
        if (request.getGenerations() == 0) {      // Sets the default if previously unset
            request.setGenerations(4);
        }

        db db = DataAccessObjects.db.getInstance();

        try {


            db.openConnection();

            User user = new User();
            user.setUserName(request.getUsername());
            if (db.getUdao().isValidUser(user)) {

                // Generate the family tree for the requested user
                String personID = db.getUdao().getPersonID(request.getUsername());
                Person person = db.getPdao().readPerson(personID);
                FamilyTreeData data = generateFamilyTree(person, request.getGenerations());
                Person[] persons = data.getPersons();
                Event[] events = data.getEvents();

                if (!db.getPdao().deletePerson(request.getUsername())) {    // Delete the people in the database
                    response.setMessage("ERROR: Could not delete people");
                    throw new DataBaseException("Could not delete person");
                }
                if (!db.getEdao().deleteEvent(request.getUsername())) {     // Delete the events in the database
                    response.setMessage("ERROR: Could not delete events");
                    throw new DataBaseException("Could not delete events");
                }

                db.getPdao().addPerson(person);                             // Add the user person back into database
                for (int i = 0; i < data.getPersons().length; i++) {        // Add the new people
                    db.getPdao().addPerson(persons[i]);
                }
                for (int i = 0; i < data.getEvents().length; i++) {         // Add the new events
                    db.getEdao().addEvent(events[i]);
                }
                db.closeConnection(true);
                response.setMessage("Loaded Fill Data");
            } else {
                logger.log(Level.SEVERE, "Fill Failed: User must already be in the database");
                response.setMessage("Fill Failed: User must already be in the database");
                db.closeConnection(false);

            }

        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage(response.getMessage() + " Could not write to the database");
            e.printStackTrace();
        }

        logger.exiting("FillService", "fillService");
        return response;
    }

    /**
     * Generates a new family tree, but it is random and not linked up
     *
     * @param descendant  username of the current user
     * @param generations to be created
     * @return the new family tree object
     */
    public FamilyTreeData generateFamilyTree(String descendant, int generations) {
        double numNodes = Math.pow(2, generations + 1) - 1;
        String filePath = "json/";
        FamilyTreeData data = new FamilyTreeData();
        data.setDescendant(descendant);
        try {
            // Grab the data from the various json fill files
            FillNames fnames = new Gson().fromJson(new FileReader(filePath + "fnames.json"), FillNames.class);                  // Female first names
            data.setFemale(fnames);
            FillNames mnames = new Gson().fromJson(new FileReader(filePath + "mnames.json"), FillNames.class);                  // Males first names
            data.setMale(mnames);
            FillNames snames = new Gson().fromJson(new FileReader(filePath + "snames.json"), FillNames.class);                  // Surnames
            data.setLast(snames);
            FillLocations locations = new Gson().fromJson(new FileReader(filePath + "locations.json"), FillLocations.class);    // Locations data
            data.setLocations(locations);
            FillNames type = new Gson().fromJson(new FileReader(filePath + "eventType.json"), FillNames.class);                 // Types of events
            data.setType(type);

            for (int i = 0; i < numNodes; i++) {
                int gender = new Random().nextInt(2);   // Gender is binary #politicalCommentary
                String personID = UUID.randomUUID().toString();
                Person person = new Person();
                if (gender == 0) {    // Create a female
                    person.setFirstName(fnames.getData()[new Random().nextInt(fnames.getData().length)]);
                    person.setGender("f");
                } else {               // Create a male
                    person.setFirstName(mnames.getData()[new Random().nextInt(mnames.getData().length)]);
                    person.setGender("m");
                }
                person.setLastName(snames.getData()[new Random().nextInt(snames.getData().length)]);
                person.setFather(UUID.randomUUID().toString());
                person.setMother(UUID.randomUUID().toString());
                person.setSpouse(UUID.randomUUID().toString());
                person.setDescendant(descendant);
                person.setPersonID(personID);

                data.addPerson(person);                         // Add to the list of people

                createEvents(data, personID);


            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Handles the algorithm for generating a new family tree
     *
     * @param descendant  the person object associated with user
     * @param generations the number of generations to compute
     * @return an object that contains arrays of people and events for the family tree
     */
    public FamilyTreeData generateFamilyTree(Person descendant, int generations) {
        String filePath = "json/";
        FamilyTreeData data = new FamilyTreeData();
        try {
            // Grab the data from the various json fill files
            data.setDescendant(descendant.getDescendant());
            data.setFemale(new Gson().fromJson(new FileReader(filePath + "fnames.json"), FillNames.class));                 // Female first names
            data.setMale(new Gson().fromJson(new FileReader(filePath + "mnames.json"), FillNames.class));                   // Males first names
            data.setLast(new Gson().fromJson(new FileReader(filePath + "snames.json"), FillNames.class));                   // Surnames
            data.setLocations(new Gson().fromJson(new FileReader(filePath + "locations.json"), FillLocations.class));       // Locations data
            data.setType(new Gson().fromJson(new FileReader(filePath + "eventType.json"), FillNames.class));                // Types of events

            Person Father = createFather(generations, data, descendant.getLastName()); // Recursively create parents
            Person Mother = createMother(generations, data, Father);
            descendant.setFather(Father.getPersonID());
            descendant.setMother(Mother.getPersonID());
            createEvents(data, descendant.getPersonID());

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }
        return data;
    }


    /**
     * Create all the fathers in the family tree
     *
     * @param generations of fathers to create
     * @param data        objects holds a log of stuff
     * @param lastName    father gave to his children
     * @return object
     */
    public Person createFather(int generations, FamilyTreeData data, String lastName) {
        if (generations == 0) {
            return new Person();
        }
        String personID = UUID.randomUUID().toString();
        //String personID = "Father " + String.valueOf(generations);
        Person person = new Person();
        person.setFirstName(data.getMale().getData()[new Random().nextInt(data.getMale().getData().length)]);   // Male first Name
        person.setLastName(lastName);   // Get child's last name
        // Set spouse later
        person.setGender("m");
        Person Father = createFather(generations - 1, data, lastName); // Recursively create parents
        Person Mother = createMother(generations - 1, data, Father);
        person.setFather(Father.getPersonID());
        person.setMother(Mother.getPersonID());
        person.setDescendant(data.getDescendant());
        person.setPersonID(personID);
        createEvents(data, personID);
        return person;

    }

    /**
     * Recursive function to create all the mothers
     *
     * @param generations number of generations to create
     * @param data        object contains a bunch of stuff
     * @param spouse      is the husband of the mother
     * @return the mother person object
     */
    public Person createMother(int generations, FamilyTreeData data, Person spouse) {
        if (generations == 0) {
            return new Person();
        }
        String personID = UUID.randomUUID().toString();
        //String personID = "Mother " + String.valueOf(generations);

        spouse.setSpouse(personID); // Link up the Father to the Mother

        Person person = new Person();
        person.setSpouse(spouse.getPersonID());
        person.setPersonID(personID);
        person.setGender("f");
        person.setFirstName(data.getFemale().getData()[new Random().nextInt(data.getFemale().getData().length)]);   // Get female name
        person.setLastName(data.getLast().getData()[new Random().nextInt(data.getLast().getData().length)]);        // Create new maiden name
        Person Father = createFather(generations - 1, data, person.getLastName()); // Recursively create parents
        Person Mother = createMother(generations - 1, data, Father);
        person.setFather(Father.getPersonID());
        person.setMother(Mother.getPersonID());
        person.setDescendant(data.getDescendant());
        createEvents(data, personID);

        // Add both parents together to the database
        data.addPerson(person);
        data.addPerson(spouse);
        return person;
    }

    /**
     * Creates the events for a specific person
     *
     * @param data     is all the data needed for the family tree
     * @param personID of the person whose life events are being created
     */
    public void createEvents(FamilyTreeData data, String personID) {
        // Call the create Event function to populate events
        int birthYear = new Random().nextInt(2018); // Create a random year for the birth

        int numEvents = new Random().nextInt(3) + 2;    // Create a random number of events
        for (int j = 0; j < numEvents; j++) {
            Event event = new Event();
            if (j == 0) {                       // Every person has at least a birth and a death
                event.setEventType("Birth");
                event.setYear(String.valueOf(birthYear));
            } else if (j == 1) {
                event.setEventType("Death");    // Death must happen after birth
                int year = birthYear + new Random().nextInt(30) + 50;
                event.setYear(String.valueOf(year));
            } else {                            // Other events happen in between
                event.setEventType(data.getType().getData()[new Random().nextInt(data.getType().getData().length)]);
                int year = birthYear + new Random().nextInt(30);
                event.setYear(String.valueOf(year));
            }
            event.setPersonID(personID);        // Populate event object
            int eventLocation = new Random().nextInt(data.getLocations().getData().length);
            event.setCity(data.getLocations().getData()[eventLocation].getCity());
            event.setCountry(data.getLocations().getData()[eventLocation].getCountry());
            event.setLatitude(data.getLocations().getData()[eventLocation].getLatitude());
            event.setLongitude(data.getLocations().getData()[eventLocation].getLongitude());
            event.setDescendant(data.getDescendant());
            event.setEventID(UUID.randomUUID().toString());

            data.addEvent(event);   //Adds the event to the list
        }
    }

}


/*
Archive of working family tree data


Archive of create Person class that was just a test anyway
 public String createPerson(String gender, int generations, FamilyTreeData data) {
        if (generations == 0) {
            return null;    // todo: Correct base case?
        }
        String personID = UUID.randomUUID().toString();
        Person person = new Person();
        if (gender.equals("f")) {// Create a female
            person.setFirstName(data.getFemale().getData()[new Random().nextInt(data.getFemale().getData().length)]);
            person.setLastName(data.getLast().getData()[new Random().nextInt(data.getLast().getData().length)]); // todo: Get of father or of spouse based on gender
            person.setSpouse(createPerson("m", generations, data));
        } else if (gender.equals("m")) {    // Create a male
            person.setFirstName(data.getMale().getData()[new Random().nextInt(data.getMale().getData().length)]);
            person.setLastName(data.getLast().getData()[new Random().nextInt(data.getLast().getData().length)]); // todo: Get of father or of spouse based on gender
            person.setSpouse(createPerson("f", generations--, data));
        }
        person.setGender(gender);
        person.setFather(createPerson("m", generations--, data));
        person.setMother(createPerson("f", generations--, data));
        person.setDescendant(data.getDescendant());
        person.setPersonID(personID);
        data.addPerson(person);

        // Call the create Event function to populate events
        int birthYear = new Random().nextInt(2018); // Create a random year for the birth

        int numEvents = new Random().nextInt(3) + 2;    // Create a random number of events
        for (int j = 0; j < numEvents; j++) {
            Event event = new Event();
            if (j == 0) {                       // Every person has at least a birth and a death
                event.setEventType("Birth");
                event.setYear(String.valueOf(birthYear));
            } else if (j == 1) {
                event.setEventType("Death");    // Death must happen after birth
                int year = birthYear + new Random().nextInt(30) + 50;
                event.setYear(String.valueOf(year));
            } else {                            // Other events happen in between
                event.setEventType(data.getType().getData()[new Random().nextInt(data.getType().getData().length)]);
                int year = birthYear + new Random().nextInt(30);
                event.setYear(String.valueOf(year));
            }
            event.setPersonID(personID);        // Populate event object
            int eventLocation = new Random().nextInt(data.getLocations().getData().length);
            event.setCity(data.getLocations().getData()[eventLocation].getCity());
            event.setCountry(data.getLocations().getData()[eventLocation].getCountry());
            event.setLatitude(data.getLocations().getData()[eventLocation].getLatitude());
            event.setLongitude(data.getLocations().getData()[eventLocation].getLongitude());
            event.setDescendant(data.getDescendant());
            event.setEventID(UUID.randomUUID().toString());

            data.addEvent(event);   //Adds the event to the list
        }
        return personID;
    }
 */