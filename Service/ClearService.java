package Service;

import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.ClearResult;

public class ClearService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Clears all the tables in the database and creates empty ones
     *
     * @return a message about success or failure
     * @throws DataBaseException if it fails
     */
    public ClearResult clearService() throws DataBaseException {
        logger.entering("ClearService", "clearService");
        ClearResult result = new ClearResult();
        db db = DataAccessObjects.db.getInstance();
        try {
            db.openConnection();

            // Handle the user table
            if (!db.getUdao().clearUsers()) {
                result.setMessage("Clear User Table Failed");
                db.closeConnection(false);
                return result;
            }
            db.getUdao().createUserTable();

            // Handle the authToken table
            if (!db.getAdao().clearTokens()) {
                result.setMessage("Clear Token Table Failed");
                db.closeConnection(false);
                return result;
            }
            db.getAdao().createAuthTokens();

            // Handle the Event table
            if (!db.getEdao().clearEvents()) {
                result.setMessage("Clear Event Table Failed");
                db.closeConnection(false);
                return result;
            }
            db.getEdao().createEvents();

            // Handle the person table
            if (!db.getPdao().clearPersons()) {
                result.setMessage("Clear Person Table Failed");
                db.closeConnection(false);
                return result;
            }
            db.getPdao().createPersons();

            db.closeConnection(true);   //If we got here, transaction was successful

            result.setMessage("Clear succeeded");
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);  //Transaction was not successful
            System.out.println("Failed to clear all tables");
            result.setMessage(e.getMessage());
            e.printStackTrace();
            return result;
        }
        logger.exiting("ClearService", "clearService");
        return result;
    }
}


