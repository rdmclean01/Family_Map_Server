package DataTransferObjects;

/**
 * Clear Result is an object that holds information about whether the tables were deleted
 */

public class ClearResult {
    private String message;

    /**
     * vanilla constructor
     */
    public ClearResult() {
        message = "";
    }


    /**
     * Allows you to communicate a message with other classes
     * @param input
     */
    public ClearResult(String input){
        message = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
