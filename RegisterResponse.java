package DataTransferObjects;

import Model.User;

/**
 * Object class to handle the Register response
 */

public class RegisterResponse {
    private String authToken;
    private String userName;
    private String personId;
    private String message; // Error message


    /**
     * Vanilla constructor
     */
    public RegisterResponse() {
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
