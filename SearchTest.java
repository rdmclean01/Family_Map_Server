package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rdmcl on 4/11/2018.
 */
public class SearchTest {

    private Search test;
    Person user = new Person("user", "me", "mclean", "m", "father", "mother", "spouse");
    Person spouse = new Person("user", "spouse", "kempton", "f", "", "", "me");
    Person father = new Person("user", "father", "mclean", "m", "pGrandfather", "pGrandmother", "mother");
    Person mother = new Person("user", "mother", "duke", "f", "mGrandfather", "mGrandmother", "father");
    Person pGrandfather = new Person("user", "pGrandfather", "mclean", "m", "", "", "pGrandmother");
    Person pGrandmother = new Person("user", "pGrandmother", "gould", "f", "", "", "pGrandfather");
    Person mGrandfather = new Person("user", "mGrandfather", "duke", "m", "", "", "mGrandmother");
    Person mGrandmother = new Person("user", "mGrandmother", "sidwell", "f", "", "", "mGrandmother");
    Event user_birth = new Event("user","me","123","456","Utah","Orem","Birth","1992");
    Event father_birth = new Event("user","father","123","456","California","San Diego","Birth","1961");
    Event mGrandmother_birth = new Event("user","mGrandmother","123","456","China","SLC","Birth","1930");
    Event spouse_death = new Event("user","spouse","123","456","Utah","London","Death","2100");
    Event mother_death = new Event("user","mother","123","456","Africa","Ghana","Death","2170");
    Event pGrandfather_baptism = new Event("user","pGrandfather","123","456","Missouri","Lake","Baptism","1992");

    @Before
    public void setUp() throws Exception {
        test = new Search();
        // Set up people
        user.setPersonID("me");
        spouse.setPersonID("spouse");
        father.setPersonID("father");
        mother.setPersonID("mother");
        pGrandfather.setPersonID("pGrandfather");
        pGrandmother.setPersonID("pGrandmother");
        mGrandfather.setPersonID("mGrandfather");
        mGrandmother.setPersonID("mGrandmother");
        Model.singleton.addToPeople(user);
        Model.singleton.addToPeople(spouse);
        Model.singleton.addToPeople(father);
        Model.singleton.addToPeople(mother);
        Model.singleton.addToPeople(pGrandfather);
        Model.singleton.addToPeople(pGrandmother);
        Model.singleton.addToPeople(mGrandfather);
        Model.singleton.addToPeople(mGrandmother);
        Model.singleton.setUser(user);

        // Set up events
        user_birth.setEventID("user_birth");
        father_birth.setEventID("father_birth");
        mGrandmother_birth.setEventID("mGrandmother_birth");
        spouse_death.setEventID("not_allowed");
        mother_death.setEventID("mother_death");
        pGrandfather_baptism.setEventID("pGrandfather_baptism");
        Model.singleton.addToEvents(user_birth);
        Model.singleton.addToEvents(father_birth);
        Model.singleton.addToEvents(mGrandmother_birth);
        Model.singleton.addToEvents(spouse_death);
        Model.singleton.addToEvents(mother_death);
        Model.singleton.addToEvents(pGrandfather_baptism);

        // Set up filter
        Model.singleton.setFilter(7);
        Model.singleton.getFilter().initTypeData(7);
        Model.singleton.getFilter().setTypeToPosition("Birth",0);
        Model.singleton.getFilter().setTypeToPosition("Death",1);
        Model.singleton.getFilter().setTypeToPosition("Baptism",2);
        Model.singleton.getFilter().setTypeToPosition("paternal",3);
        Model.singleton.getFilter().setTypeToPosition("maternal",4);
        Model.singleton.getFilter().setTypeToPosition("male",5);
        Model.singleton.getFilter().setTypeToPosition("female",6);

    }
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void search() throws Exception {
        // Test a people option
        test.search("mcl");
        assertTrue(Model.singleton.getSearch().mPeople.contains(user));
        assertTrue(Model.singleton.getSearch().mPeople.contains(father));
        assertTrue(Model.singleton.getSearch().mPeople.contains(pGrandfather));
        assertFalse(Model.singleton.getSearch().mPeople.contains(spouse));

        // test an event option
        test.search("UT");
        assertTrue(Model.singleton.getSearch().mEvents.contains(user_birth));
        assertTrue(Model.singleton.getSearch().mEvents.contains(spouse_death));
        assertTrue(!Model.singleton.getSearch().mEvents.contains(mGrandmother_birth));

    }

}