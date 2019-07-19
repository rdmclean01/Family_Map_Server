package DataTransferObjects;

import Model.Event;


/**
 * Request holds the authToken and eventID
 */
public class EventRequest {
    private String authToken;
    private String eventID;

    /**
     * Creates an event request object
     * @param authToken
     * @param eventID
     */
    public EventRequest(String authToken, String eventID) {
        setAuthToken(authToken);
        setEventID(eventID);
    }

    public EventRequest() {
        setAuthToken(null);
        setEventID(null);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
