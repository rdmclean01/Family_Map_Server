package Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.LoadRequest;
import DataTransferObjects.LoadResponse;
import Model.Event;
import Model.Person;
import Model.User;

public class LoadService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Deletes and then loads a new family tree for the user
     *
     * @param request object with proper information about load
     * @return a response object
     */
    public LoadResponse loadService(LoadRequest request) throws DataBaseException {
        logger.entering("LoadService", "loadService");

        LoadResponse response = new LoadResponse();
        db db = DataAccessObjects.db.getInstance();

        try {
            db.openConnection();

            for (int i = 0; i < request.getUsers().length; i++) {  // Delete the users, events, and people associated with users
                deleteFamilyTree(request.getUsers()[i].getUserName(), db);
            }

            User[] users = request.getUsers();                  // Load the requested users in
            for (int i = 0; i < users.length; i++) {
                if(users[i].getUserID().equals("")) {
                    users[i].setUserID(UUID.randomUUID().toString());
                }
                db.getUdao().addUser(users[i]);
            }

            Event[] events = request.getEvents();               // Load in the requested events
            for (int i = 0; i < events.length; i++) {
                db.getEdao().addEvent(events[i]);
            }

            Person[] people = request.getPersons();             // Load in the requested people
            for (int i = 0; i < people.length; i++) {
                db.getPdao().addPerson(people[i]);
            }

            db.closeConnection(true);                   // Load was successful

            response.setMessage("Successfully added " +         // Set a message for the user
                    users.length + " users, " +
                    people.length + " persons, and " +
                    events.length + " events to the database.");
        } catch (DataBaseException e) {                         // Access to the database failed
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);                  // Do not commit the changes
            response.setMessage("ERROR: Could not load information into the database"); // Tell the user what happened
            e.printStackTrace();
            return response;
        }
        logger.exiting("LoadService", "loadService");
        return response;                                        // Return the right response
    }

    /**
     * Initially deletes the family tree users, events, and people
     *
     * @param username that needs to be wiped out
     * @param db       connection to the database
     * @return whether or not the delete was successful
     */
    public boolean deleteFamilyTree(String username, db db) {
        logger.entering("LoadService", "deleteFamilyTree");
        boolean response = false;
        try {
            if (!db.getUdao().deleteUser(username)) {     // Delete the users
                throw new DataBaseException("Could not delete user");
            }
            if (!db.getEdao().deleteEvent(username)) {  // Delete the events
                throw new DataBaseException("Could not delete events");
            }
            if (!db.getPdao().deletePerson(username)) {   // Delete the people
                throw new DataBaseException("Could not delete person");
            }
            response = true;                            // Successful deletion

        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            System.out.println("Failed to delete all references to username before loading new family tree");
            e.printStackTrace();
            assert response == false;   // Assumption that response is still false
            return response;
        }
        logger.exiting("LoadService", "deleteFamilyTree");
        return response;
    }
}
