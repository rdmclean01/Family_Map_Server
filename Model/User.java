package Model;


/**
 * User class keeps track of the application users
 */
public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String userID;

    /**
     * Constructor sets all the member variables to ""
     */
    public User() {
        super();
        userName = "";
        password = "";
        email = "";
        firstName = "";
        lastName = "";
        gender = "";
        userID = "";
    }

    /**
     * Constructor to easily create a filled User
     *
     * @param userName
     * @param userPassword
     * @param userEmail
     * @param userFirstName
     * @param userLastName
     * @param userGender
     */

    public User(String userName, String userPassword, String userEmail, String userFirstName, String userLastName, String userGender) {
        this.userName = userName.toLowerCase();
        this.password = userPassword.toLowerCase();
        this.email = userEmail.toLowerCase();
        this.firstName = userFirstName.toLowerCase();
        this.lastName = userLastName.toLowerCase();
        this.gender = userGender.toLowerCase();
        this.userID = "";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUserfirstName(String userfirstName) {
        this.firstName = userfirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
