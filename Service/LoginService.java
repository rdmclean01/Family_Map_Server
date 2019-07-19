package Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.LoginRequest;
import DataTransferObjects.LoginResponse;
import Model.AuthToken;
import Model.User;


public class LoginService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Takes in a username and password and logs in (with authToken)if valid
     *
     * @param request contains user information
     * @return the authToken
     * @throws DataBaseException if failed
     */
    public LoginResponse loginService(LoginRequest request) throws DataBaseException {
        logger.entering("LoginService", "loginService");
        LoginResponse response = new LoginResponse();
        db db = DataAccessObjects.db.getInstance();
        try {
            User user = new User();                     //Construct the partial User from the info
            user.setUserName(request.getUserName());
            user.setPassword(request.getPassword());

            db.openConnection();
            if (db.getUdao().canLogin(user)) {
                AuthToken token = new AuthToken();  // Create and populate a token object
                token.setUsername(request.getUserName());
                token.setAuthToken(UUID.randomUUID().toString());
                db.getAdao().addAuthToken(token);
                String id = db.getPdao().getPersonId(request.getUserName());
                db.closeConnection(true);             // Database should have a new authToken in it

                // Populate response object
                response.setAuthToken(token.getAuthToken());
                response.setUserName(request.getUserName());
                response.setPersonId(id);
                response.setMessage(null); //Handle Error Message
            } else {
                response.setMessage("Credentials do not match the database. Could not login");
                db.closeConnection(false);
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage("ERROR: Login Request Failed");
            e.printStackTrace();
        }
        logger.exiting("LoginService", "loginService");
        return response;
    }
}
