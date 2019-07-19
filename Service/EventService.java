package Service;

import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataAccessObjects.db;
import DataTransferObjects.EventRequest;
import DataTransferObjects.EventResponse;
import Model.Event;


public class EventService {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Service that handles returning all the events of a certain user
     *
     * @param request contains information about the associated user
     * @return the response and message about the success/ failure
     * @throws DataBaseException
     */
    public EventResponse eventService(EventRequest request) throws DataBaseException {
        logger.entering("EventService", "eventService");
        EventResponse response = new EventResponse();
        db db = DataAccessObjects.db.getInstance();
        try {
            db.openConnection();
            if (db.getAdao().isValidToken(request.getAuthToken())) {    // Need to check the authToken
                String username = db.getAdao().getUserName(request.getAuthToken()); // Find the username associated with the authorization token
                Event[] events = db.getEdao().readEvents(username); // Get the events
                response.setEvents(events);     // Populate the response
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
                logger.info("ERROR: Authorization token was invalid");
                response.setMessage("ERROR: Authorization token was invalid");
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage("ERROR: Could not fetch family events");
            e.printStackTrace();
        }
        logger.exiting("EventService", "eventService");
        return response;
    }

    /**
     * Service to return the event with the associated ID
     *
     * @param request containing information about the requested event
     * @return event and message about success or failure
     * @throws DataBaseException
     */
    public EventResponse eventServiceWithID(EventRequest request) throws DataBaseException {
        logger.entering("EventService", "eventServiceWithID");
        EventResponse response = new EventResponse();
        db db = DataAccessObjects.db.getInstance();
        try {
            db.openConnection();
            if (db.getAdao().isValidToken(request.getAuthToken())) {        // Confirm authorization
                String username = db.getAdao().getUserName(request.getAuthToken()); // Get authorized username
                Event event = new Event();
                event.setEventID(request.getEventID());     // Get the associated ID
                event.setDescendant(username);
                if (db.getEdao().isValidEvent(event)) {     // Check that the eventID matches the user
                    event = db.getEdao().readEvent(request.getEventID());
                    response.setEvent(event);   // Populate the response event with correct event
                    db.closeConnection(true);
                } else {
                    db.closeConnection(false);
                    response.setMessage("ERROR: EventID does not match user");
                }
            } else {
                db.closeConnection(false);
                response.setMessage("ERROR: Authorization token was invalid");
            }
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            db.closeConnection(false);
            response.setMessage("ERROR: Could not fetch event from the database");
            e.printStackTrace();
        }
        logger.exiting("EventService", "eventServiceWithID");
        return response;
    }
}
