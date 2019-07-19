package DataTransferObjects;

/**
 * Created by rdmcl on 2/12/2018.
 */

public class FillRequest {
    private String username;
    private int generations;

    /**
     * vanilla constructor
     */
    public FillRequest() {
        username = null;
        generations = 0;
    }

    /**
     * Constructor with correct arguments
     * @param username
     * @param generations
     */
    public FillRequest(String username, String generations) {
        this.username = username;
        setGenerations(generations);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(String generations) {
        this.generations = Integer.parseInt(generations);
    }

    public void setGenerations(int generations){
        this.generations = generations;
    }
}
