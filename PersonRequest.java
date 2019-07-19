package DataTransferObjects;

/**
 * Created by rdmcl on 2/12/2018.
 */

public class PersonRequest {
    private String personID;
    private String authToken;

    public PersonRequest() {
        personID = null;
        authToken = null;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
