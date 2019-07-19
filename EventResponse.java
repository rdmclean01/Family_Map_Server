package DataTransferObjects;

import Model.AuthToken;
import Model.Event;

/**
 * Holds the response objects for the handler
 */

public class EventResponse {
    private String message;
    private Event[] events;
    private Event event;


    /**
     * Empty public constructor
     */
    public EventResponse() {
        super();
        message = "";
        event = null;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] e) {
        events = new Event[e.length];
        for (int i = 0; i < e.length; i++) {
            this.events[i] = e[i];
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
