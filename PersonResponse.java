package DataTransferObjects;

import Model.Person;

/**
 * Created by rdmcl on 2/12/2018.
 */

public class PersonResponse {
    private String message;
    private Person[] persons;   // For the /person service
    private Person person;      // For the /person/personID service

    public PersonResponse() {
        message = null;
        person = null;
        persons = null;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = new Person[persons.length];
        for (int i = 0; i < persons.length; i++) {
            this.persons[i] = persons[i];
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
