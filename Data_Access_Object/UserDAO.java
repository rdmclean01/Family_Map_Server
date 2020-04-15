package DataAccessObjects;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.User;

/**
 * This class links the User class with the User table in the Database
 */
public class UserDAO {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    public UserDAO() {
        // Blank constructor
    }

    /**
     * Create a new user table
     *
     * @throws DataBaseException if failure
     */
    public void createUserTable() throws DataBaseException {
        logger.entering("UserDao", "createUserTable");
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();

                stmt.executeUpdate("drop table if exists user");
                stmt.executeUpdate("create table user ( " +     // Define the columns
                        "id varchar(255) not null primary key,\n" +
                        "userName varchar(255) unique not null,\n" +
                        "userPassword varchar(255) not null,\n" +
                        "userEmail varchar(255) not null,\n" +
                        "userFirstName varchar(255) not null,\n" +
                        "userLastName varchar(255) not null,\n" +
                        "userGender varchar(255) not null,\n" +
                        "userPersonID varchar(255) not null,\n" +
                        "foreign key (userPersonID) references Person(id) )");
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("createUsers failed", e);
        }
        logger.exiting("UserDao", "createUserTable");
    }

    /**
     * Adds the user object into table
     *
     * @param user object to be added to the table
     * @return ID of the person
     * @throws DataBaseException #failure
     */
    public String addUser(User user) throws DataBaseException {
        logger.entering("Userdao", "addUser");

        if (isValidUser(user)) { // Fail when user is already in table
            throw new DataBaseException("User is already in the Database");
        }

        try {
            PreparedStatement stmt = null;
            try {
                String userID = user.getUserID();
                String userName = user.getUserName();
                String userPassword = user.getPassword();
                String userEmail = user.getEmail();
                String userFirstName = user.getFirstName();
                String userLastName = user.getLastName();
                String userGender = user.getGender();
                String sql = "insert into user (id,userName,userPassword,userEmail,userFirstName,userLastName,userGender,userPersonID) values( '" +
                        userID + "', '" + userName + "', '" + userPassword + "', '" + userEmail + "', '" + userFirstName + "', '" + userLastName + "', '" + userGender + "', '" + userID + "')";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                if (stmt.executeUpdate() != 1) {
                    throw new DataBaseException("addUser failed: Could not insert user");
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("addUser failed", e);
        }
        logger.exiting("UserDao", "addUser");
        return user.getUserID();
    }

    /**
     * Verifies if the User is already in the USER table
     * A valid user is one whose username is in the table
     *
     * @param user that should be checked
     * @return true if user is in the table
     * @throws DataBaseException
     */
    public boolean isValidUser(User user) throws DataBaseException {
        logger.entering("UserDao", "isValidUser");
        boolean result = false;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "select userName from user";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String received = rs.getString("userName"); // Check if user is correct
                    if (received.equals(user.getUserName())) {
                        result = true;
                        break;                                      // then exit
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
            throw new DataBaseException("isValidUser() failed", e);
        }
        logger.exiting("Userdao", "isValidUser");
        return result;
    }

    /**
     * A user can login when username and password are correct
     *
     * @param user to check against the login
     * @return true when user is allowed to login
     * @throws DataBaseException #failure
     */
    public boolean canLogin(User user) throws DataBaseException {
        logger.entering("Userdao", "canLogin");
        boolean result = false;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select userPassword from user where userName = '" + user.getUserName() + "'";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    result = (user.getPassword().equals(rs.getString("userPassword")));
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
            System.out.println("Username/Password combination did not match database");
            return false;
        }
        logger.exiting("UserDao", "canLogin");
        return result;
    }

    /**
     * Check if user is in the database
     *
     * @param user to check against database
     * @return true when user is not in the database
     * @throws DataBaseException #failure
     */
    public boolean isNotRegisteredUser(User user) throws DataBaseException {
        logger.entering("Userdao", "isNotRegisteredUser");
        boolean result = true;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "select userName from user where userName = '" + user.getUserName() + "'";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String received = rs.getString("userName");
                    if (received.equals(user.getUserName())) {
                        result = false;
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
            System.out.println(user.getUserName() + " is not in database");
            return true;
        }
        logger.exiting("Userdao", "isNotRegisteredUser");
        return result;
    }

    /**
     * Returns the personID for the given Username so someone can access the user's person object
     *
     * @param username of desired user
     * @return personID associated with that user's person object
     * @throws DataBaseException
     */
    public String getPersonID(String username) throws DataBaseException {
        logger.entering("Userdao", "getPersonID");
        String personID = null;
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {

                String sql = "select * from user where userName = '" + username + "'";
                stmt = db.getInstance().getConn().prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    personID = rs.getString("userPersonID");
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
            System.out.println(username + " is not in database");
            return null;
        }
        logger.exiting("Userdao", "getPersonID");
        return personID;
    }

    /**
     * Deletes the user associated with the username
     *
     * @param username to be deleted
     * @return true if the user was deleted
     * @throws DataBaseException #failure
     */
    public boolean deleteUser(String username) throws DataBaseException {
        logger.entering("Userdao", "deleteUser");
        boolean result = false;
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();
                String sql = "delete from user where userName = '" + username + "'";
                stmt.executeUpdate(sql);
                result = true;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("deleteUser() failed", e);
        }
        logger.exiting("Userdao", "deleteUser");
        return result;
    }

    /**
     * Destroys the clear user table
     *
     * @return true when the table was deleted
     * @throws DataBaseException #failure
     */
    public boolean clearUsers() throws DataBaseException {
        logger.entering("Userdao", "clearUsers");
        boolean result = false;
        try {
            Statement stmt = null;
            try {
                stmt = db.getInstance().getConn().createStatement();
                stmt.executeUpdate("drop table if exists user");
                result = true;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("ClearUsers failed", e);
        }
        logger.exiting("Userdao", "clearUsers");
        return result;
    }
}
