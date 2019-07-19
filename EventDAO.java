package DataAccessObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import Model.Event;

/**
 * A class to communicate with the Event table in the Database
 */
public class EventDAO {

    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Creates the Event table
     *
     * @throws DataBaseException
     */
    public void createEvents() throws DataBaseException {
        logger.entering("EventDao", "createEvents");
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();

                stmt.executeUpdate("drop table if exists event");
                stmt.executeUpdate("create table event ( " +    // Create proper columns
                        "id varchar(255) unique not null primary key,\n" +
                        "user varchar(255) not null,\n" +
                        "person varchar(255) not null,\n" +
                        "latitude varchar(255) not null,\n" +
                        "longitude varchar(255) not null,\n" +
                        "country varchar(255) not null,\n" +
                        "city varchar(255) not null,\n" +
                        "type varchar(255) not null,\n" +
                        "year varchar(255) not null,\n" +
                        "foreign key (user) references user(userName)," +
                        "foreign key (person) references person(id) )");
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("createEvents failed", e);
        }
        logger.exiting("EventDao", "createEvents");
    }

    /**
     * Adds a new Event to the table
     *
     * @param event
     * @return the EventID
     * @throws DataBaseException
     */
    public String addEvent(Event event) throws DataBaseException {
        logger.entering("EventDao", "addEvent");
        if (isValidEvent(event)) {
            logger.info("Event is already in the Database");
            throw new DataBaseException("Event is already in the Database");
        }
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "insert into event (id,user,person,latitude,longitude,country,city,type,year) values(?,?,?,?,?,?,?,?,?)";
                // Put all the correct values into the event string
                stmt = db.getInstance().getConn().prepareStatement(sql);
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getDescendant());
                stmt.setString(3, event.getPersonID());
                stmt.setString(4, event.getLatitude());
                stmt.setString(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setString(9, event.getYear());

                if (stmt.executeUpdate() != 1) {
                    throw new DataBaseException("addEvent failed: Could not insert event");
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("addEvent failed", e);
        }
        logger.exiting("EventDao", "addEvent");
        return event.getEventID();
    }

    /**
     * Checks if the event object is in the token table
     * Valid means that the ID and Username match
     *
     * @param event object to check with token
     * @return true if event is in the table
     * @throws DataBaseException if failed
     */
    public boolean isValidEvent(Event event) throws DataBaseException {
        logger.entering("EventDao", "isValidEvent");
        boolean result = false;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select * from event"; // Grab everything for comparison
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String username = rs.getString("user");
                    if (id.equals(event.getEventID()) && username.equals(event.getDescendant())) {
                        result = true;
                    }
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("isValidEvent() failed", e);
        }
        logger.exiting("EventDao", "isValidEvent");
        return result;
    }

    /**
     * Reads a specific event associated with ID parameter
     *
     * @param EventID passed in by user to grab from table
     * @return event object requested
     * @throws DataBaseException when failed
     */
    public Event readEvent(String EventID) throws DataBaseException {
        logger.entering("EventDao", "readEvent");
        Event event = new Event();
        event.setEventID(EventID);
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "select * from event where id = '" + EventID + "'";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) { // Populate grabbed object
                    event.setDescendant(rs.getString("user"));
                    event.setPersonID(rs.getString("person"));
                    event.setLatitude(rs.getString("latitude"));
                    event.setLongitude(rs.getString("longitude"));
                    event.setCountry(rs.getString("country"));
                    event.setCity(rs.getString("city"));
                    event.setEventType(rs.getString("type"));
                    event.setYear(rs.getString("year"));
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("\tERROR: readEvent() failed", e);
        }
        logger.exiting("EventDao", "readEvent");
        return event;
    }

    /**
     * Return all the events from the ancestors of the user
     *
     * @param username of the current user
     * @return array of events associated with the user
     * @throws DataBaseException when failed
     */
    public Event[] readEvents(String username) throws DataBaseException {
        logger.entering("EventDao", "readEvents");
        // Implement with Sets because size is impossible to estimate at this point
        Set<String> IDs = new HashSet<>();
        Set<Event> events = new HashSet<>();
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select id from event where user = '" + username + "'";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {                 //Grab all the matching rows from the table
                    IDs.add(rs.getString("id"));
                }

                for (String id : IDs) {              //Get the corresponding object for each row
                    Event current = readEvent(id);
                    events.add(current);
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("\tERROR: readEvents() failed", e);
        }

        //Transfer to an array from the list
        Event[] list = new Event[events.size()];
        int i = 0;
        for (Event current : events) {
            list[i] = current;
            i++;
        }
        logger.exiting("EventDao", "readEvents");
        return list;
    }

    /**
     * This is used to clear out the event table before loading new events
     *
     * @param username the user that gets a new family #adoption
     * @return true if deletion worked
     * @throws DataBaseException when failed
     */
    public boolean deleteEvent(String username) throws DataBaseException {
        logger.entering("EventDao", "deleteEvent");
        boolean result = false;
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();
                String sql = "delete from event where user = '" + username + "'";
                stmt.executeUpdate(sql);
                result = true;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("DeleteEvents() failed", e);
        }
        logger.exiting("EventDao", "deleteEvent");
        return result;
    }

    /**
     * Destroys the event table
     *
     * @return true when successful
     * @throws DataBaseException when failed
     */
    public boolean clearEvents() throws DataBaseException {
        logger.entering("EventDao", "clearEvents");
        boolean result = false;
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();
                stmt.executeUpdate("drop table if exists event");
                result = true;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("ClearEvents failed", e);
        }
        logger.exiting("EventDao", "clearEvents");
        return result;
    }
}
