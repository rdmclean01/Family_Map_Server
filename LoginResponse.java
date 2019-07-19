package DataTransferObjects;

import Model.User;

/**
 * Object holder for Login Responses
 */

public class LoginResponse {

    private String authToken;
    private String userName;
    private String personId;
    private String message;

    /**
     * Vanilla constructor
     */
    public LoginResponse() {
        authToken = null;
        userName = null;
        personId = null;
        message = null;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
