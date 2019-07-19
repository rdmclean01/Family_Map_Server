package DataTransferObjects;

/**
 * Objects for fill responses
 */

public class FillResponse {
    private String message;

    /**
     * Response holds the message, formats it and moves it along
     */
    public FillResponse() {
        message = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
