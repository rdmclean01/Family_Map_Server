package Service;

import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.AuthTokenDAO;
import DataAccessObjects.DataBaseException;
import DataAccessObjects.PersonDAO;
import DataAccessObjects.db;
import DataTransferObjects.PersonRequest;
import DataTransferObjects.PersonResponse;
import Model.AuthToken;
import Model.Person;


public class PersonService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }


    /**
     * Returns ALL family members of the current user
     *
     * @param request does not have a personID with it
     * @return the response object with the array of person objects
     * @throws DataBaseException with error message
     */
    public PersonResponse userAncestors(PersonRequest request) throws DataBaseException {
        logger.entering("PersonService", "userAncestors");
        PersonResponse response = new PersonResponse();
        db db = DataAccessObjects.db.getInstance();
        try {
            db.openConnection();
            if (db.getAdao().isValidToken(request.getAuthToken())) { // Confirm authorization
                String username = db.getAdao().getUserName(request.getAuthToken()); // Get username with authorization
                Person[] persons = db.getPdao().readPersons(username);  // Get all the user's ancestors
                response.setPersons(persons);
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
                logger.info("ERROR: Authorization token was invalid");
                response.setMessage("ERROR: Authorization token was invalid");
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage("ERROR: Could not fetch family person objects");
            e.printStackTrace();
        }
        logger.exiting("PersonService", "userAncestors");
        return response;
    }

    /**
     * Use this to return a single person associated with the personID
     *
     * @param request that contains a personID
     * @return the response object with a single person object
     * @throws DataBaseException
     */
    public PersonResponse singlePerson(PersonRequest request) throws DataBaseException {
        logger.entering("PersonService", "singlePerson");

        PersonResponse response = new PersonResponse();
        db db = DataAccessObjects.db.getInstance();
        try {
            db.openConnection();
            if (db.getAdao().isValidToken(request.getAuthToken())) {    // Check authorization
                String username = db.getAdao().getUserName(request.getAuthToken()); // Get username
                Person person = new Person();
                person.setPersonID(request.getPersonID());
                person.setDescendant(username);
                if (db.getPdao().isValidPerson(person)) {   // Check that the username and authorization match
                    person = db.getPdao().readPerson(request.getPersonID());
                    response.setPerson(person);
                    db.closeConnection(true);
                } else {
                    db.closeConnection(false);
                    response.setMessage("ERROR: PersonID does not match user");
                }

            } else {
                db.closeConnection(false);
                response.setMessage("ERROR: Authorization token was invalid");
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage("ERROR: Could not fetch person associated with personID");
            e.printStackTrace();
            return response;
        }
        logger.exiting("PersonService", "singlePerson");
        return response;
    }
}
