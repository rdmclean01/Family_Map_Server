package Model;


/**
 * Person class keeps track of people in the ancestral tree
 * Ancestors of users
 */
public class Person {
    private String firstName;
    private String lastName;
    private String personID;
    private String gender;
    private String spouse;          //ID of person's spouse
    private String father;          //ID of person's father
    private String mother;          //ID of person's mother
    private String descendant;      //Username to which this person belongs

    /**
     * Constructor sets all the member variables to ""
     */
    public Person() {
        super();
        personID = "";
        descendant = "";
        father = "";
        firstName = "";
        gender = "";
        lastName = "";
        mother = "";
        spouse = "";
    }

    /**
     * Constructor to easily create a filled Person
     *
     * @param descendant      //username for descendent
     * @param personFirstName //first name
     * @param personLastName  //last name
     * @param personGender    gender
     * @param personFather    ID of father
     * @param personMother    ID of mother
     * @param personSpouse    ID of spouse
     */
    public Person(String descendant, String personFirstName, String personLastName, String personGender, String personFather, String personMother, String personSpouse) {
        this.descendant = descendant;
        this.firstName = personFirstName;
        this.lastName = personLastName;
        this.gender = personGender;
        this.father = personFather;
        this.mother = personMother;
        this.spouse = personSpouse;
        this.personID = "";
    }

    /**
     * Constructor to easily create a filled Person
     *
     * @param descendant //username for descendent
     */
    public Person(String descendant) {
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
        this.personID = null;
    }

    /**
     * @return the hashcode - but it is not based on the foreign key IDs to related persons
     */
    @Override
    public int hashCode() {
        int value = descendant.hashCode() + firstName.hashCode() + lastName.hashCode() +
                gender.hashCode() + personID.hashCode();
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o.hashCode() != this.hashCode()) {
            return false;
        }
        Person other = (Person) o;
        return other.hashCode() == this.hashCode();

    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
