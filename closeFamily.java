package Model;

/**
 * This is a wrapper function used to store data for the recycler family view on the person activity
 */

public class closeFamily {
    private String relationship;
    private Person mPerson;

    public closeFamily(String relationship, Person person) {
        this.relationship = relationship;
        mPerson = person;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Person getPerson() {
        return mPerson;
    }

    public void setPerson(Person person) {
        mPerson = person;
    }

    public String getName(){
        return mPerson.getFirstName() + " " + mPerson.getLastName();
    }
}
