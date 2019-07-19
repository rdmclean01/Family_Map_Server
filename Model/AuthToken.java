package Model;

import java.util.UUID;

/**
 * Class which keeps track of the Authorization Tokens in the Database
 */

public class AuthToken {
    private String authToken;
    private String username;

    /**
     * Constructor to create authTokens using the UUID function
     */
    public AuthToken() {
        authToken = "";
    }

    public AuthToken(String username) {
        setUsername(username);  //Do this so you can make sure the format works
        authToken = "";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
