package DataAccessObjects;

import java.sql.SQLException;

import javax.sound.midi.SysexMessage;

/**
 * Handles the exception from the DAO classes
 */

public class DataBaseException extends Exception {
    /**
     * Handles exception by bringing printing out a string and keeps track of exception object
     * @param s
     * @param e
     */
    public DataBaseException(String s, SQLException e){
        super();
        System.out.println(s + "\n");
    }

    /**
     * Handles exception by printing out the given string
     * @param s
     */
    public DataBaseException(String s){
        super();
        System.out.println(s + "\n");
    }
}
