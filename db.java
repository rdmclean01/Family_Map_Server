package DataAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class to handle the connections for all of the DAO classes
 */
public class db {
    private AuthTokenDAO Adao;
    private EventDAO Edao;
    private PersonDAO Pdao;
    private UserDAO Udao;
    protected Connection conn;

    /**
     * Constructor that initializes the DAO objects
     */
    private db() {
        Adao = new AuthTokenDAO();
        Edao = new EventDAO();
        Pdao = new PersonDAO();
        Udao = new UserDAO();

    }

    /**
     * Manage the instances of the connection
     */
    private static db instance = new db();

    public static db getInstance() {
        return instance;
    }

    /**
     * Manage the logger
     */
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }


    /**
     * Manage the sqlite driver
     */
    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the connection object
     */
    public Connection getConn() {
        return conn;
    }


    /**
     * openConnection simply creates a connection with the database
     *
     * @throws DataBaseException #failure
     */
    public void openConnection() throws DataBaseException {
        logger.entering("db", "openConnection");
        try {
            final String CONNECTION_URL = "jdbc:sqlite:name.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
            throw new DataBaseException("openConnection failed", e);
        }
        logger.exiting("db", "openConnection");
    }

    /**
     * closeConnection will close the connection with the database
     *
     * @param commit Will tell the code whether or not to commit the changes
     * @throws DataBaseException
     */
    public void closeConnection(boolean commit) throws DataBaseException {
        logger.entering("db", "closeConnection");
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseException("closeConnection failed", e);
        }
        logger.exiting("db", "closeConnection");
    }


    public AuthTokenDAO getAdao() {
        return Adao;
    }

    public void setAdao(AuthTokenDAO adao) {
        Adao = adao;
    }

    public EventDAO getEdao() {
        return Edao;
    }

    public void setEdao(EventDAO edao) {
        Edao = edao;
    }

    public PersonDAO getPdao() {
        return Pdao;
    }

    public void setPdao(PersonDAO pdao) {
        Pdao = pdao;
    }

    public UserDAO getUdao() {
        return Udao;
    }

    public void setUdao(UserDAO udao) {
        Udao = udao;
    }
}
