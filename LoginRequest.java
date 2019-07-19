package DataTransferObjects;

/**
 * Objects for Login Requests
 */

public class LoginRequest {
    private String userName;
    private String password;

    /**
     * vanilla constructor
     */
    public LoginRequest() {
        userName = null;
        password = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
