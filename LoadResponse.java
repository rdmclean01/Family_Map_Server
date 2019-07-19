package DataTransferObjects;

/**
 * Created by rdmcl on 2/12/2018.
 */

public class LoadResponse {
    String message;


    /**
     * Vanilla Constructor to handle response
     */
    public LoadResponse() {
        message = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
