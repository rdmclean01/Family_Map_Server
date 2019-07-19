package Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.RegisterRequest;
import DataTransferObjects.RegisterResponse;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;


public class RegisterService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Interacts with database to register a user and generate a family tree for them
     *
     * @param request object containing user information
     * @return response object with authorization for success
     * @throws DataBaseException if applicable
     */
    public RegisterResponse registerService(RegisterRequest request) throws DataBaseException {
        logger.entering("RegisterService", "registerService");
        RegisterResponse response = new RegisterResponse();

        db db = DataAccessObjects.db.getInstance();
        try {
            // Create the user
            User user = new User();
            user.setUserName(request.getUserName());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setUserfirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setGender(request.getGender());
            user.setUserID(UUID.randomUUID().toString());

            // Create a person
            Person person = new Person();
            person.setDescendant(request.getUserName());
            person.setPersonID(user.getUserID());
            person.setFirstName(request.getFirstName());
            person.setLastName(request.getLastName());
            person.setGender(request.getGender());

            // Create an Auth Token
            AuthToken token = new AuthToken();
            token.setAuthToken(UUID.randomUUID().toString());
            token.setUsername(request.getUserName());

            db.openConnection();
            if (db.getUdao().isNotRegisteredUser(user)) {   // Make sure user is not already in database
                //Add a user
                db.getUdao().addUser(user);

                //Add a person
                db.getPdao().addPerson(person);

                // Add an authToken
                db.getAdao().addAuthToken(token);

                // Generate a new family tree (4 is the default number of generations)
                FamilyTreeData data = new FillService().generateFamilyTree(person, 4);
                Person[] persons = data.getPersons();
                Event[] events = data.getEvents();

                // Add the family tree into the database
                for (int i = 0; i < data.getPersons().length; i++) {
                    db.getPdao().addPerson(persons[i]);
                }
                for (int i = 0; i < data.getEvents().length; i++) {
                    db.getEdao().addEvent(events[i]);
                }

                db.closeConnection(true);

                // Populate the response object
                response.setAuthToken(token.getAuthToken());
                response.setUserName(request.getUserName());
                response.setPersonId(person.getPersonID());
                response.setMessage(null);   // Because there is no error
            } else {
                db.closeConnection(false);
                response.setMessage("User Already in the Database");
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setAuthToken(null);
            response.setMessage(response.getMessage() + "ERROR: Could not register User");
            return response;
        }
        logger.exiting("RegisterService", "registerService");
        return response;
    }
}


