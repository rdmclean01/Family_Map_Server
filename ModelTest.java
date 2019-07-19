package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by rdmcl on 4/6/2018.
 */
public class ModelTest {


    private Model test;

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
    Event spouse_death = new Event("user","spouse","123","456","England","London","Death","2100");
    Event mother_death = new Event("user","mother","123","456","Africa","Ghana","Death","2170");
    Event pGrandfather_baptism = new Event("user","pGrandfather","123","456","Missouri","Lake","Baptism","1992");

    @Before
    public void setUp() throws Exception {
        test = new Model();
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

    }
    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void getChildren() throws Exception {
        Map<String,Person> children = test.getChildren();
        for (String key: children.keySet()) {
            System.out.println(key + " is the parent of " + children.get(key).getPersonID());
        }
    }

    @Test
    public void getPaternal() throws Exception {
        Set<String> paternal = test.getPaternal();

        assertTrue(paternal.contains(father.getPersonID()));
        assertTrue(paternal.contains(pGrandfather.getPersonID()));
        assertTrue(paternal.contains(pGrandmother.getPersonID()));
        assertFalse(paternal.contains(mother.getPersonID()));
        assertFalse(paternal.contains(mGrandfather.getPersonID()));
        assertFalse(paternal.contains(mGrandmother.getPersonID()));
    }

    @Test
    public void getMaternal() throws Exception {
        Set<String> paternal = test.getMaternal();
        assertTrue(paternal.contains(mother.getPersonID()));
        assertTrue(paternal.contains(mGrandfather.getPersonID()));
        assertTrue(paternal.contains(mGrandmother.getPersonID()));
        assertFalse(paternal.contains(father.getPersonID()));
        assertFalse(paternal.contains(pGrandfather.getPersonID()));
        assertFalse(paternal.contains(pGrandmother.getPersonID()));
    }

    @Test
    public void getFilteredEvents() throws Exception {
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

        HashSet<Event> filtered = test.getFilteredEvents();
        assertTrue(filtered.contains(user_birth));
        assertTrue(filtered.contains(spouse_death));
        assertTrue(filtered.contains(pGrandfather_baptism));

        Model.singleton.getFilter().updateFilter(0,false); // Exclude birth events
        filtered = test.getFilteredEvents();
        assertFalse(filtered.contains(user_birth));
        assertTrue(filtered.contains(spouse_death));

        Model.singleton.getFilter().updateFilter(0,true);
        Model.singleton.getFilter().updateFilter(3,false);  // Exclude paternal events
        filtered = test.getFilteredEvents();
        assertFalse(filtered.contains(father_birth));
        assertFalse(filtered.contains(pGrandfather_baptism));
        assertTrue(filtered.contains(spouse_death));
        assertTrue(filtered.contains(mother_death));
        assertTrue(filtered.contains(user_birth));

        Model.singleton.getFilter().updateFilter(3,true);
        Model.singleton.getFilter().updateFilter(5,false);
        filtered = test.getFilteredEvents();
        assertFalse(filtered.contains(father_birth));
        assertTrue(filtered.contains(mother_death));
    }

    @Test
    public void getFilteredPeople() throws Exception {
        Model.singleton.setFilter(4);
        Model.singleton.getFilter().initTypeData(4);
        Model.singleton.getFilter().setTypeToPosition("paternal",0);
        Model.singleton.getFilter().setTypeToPosition("maternal",1);
        Model.singleton.getFilter().setTypeToPosition("male",2);
        Model.singleton.getFilter().setTypeToPosition("female",3);

        HashSet<Person> filtered = test.getFilteredPeople();
        assertTrue(filtered.contains(father));
        assertTrue(filtered.contains(pGrandfather));
        assertTrue(filtered.contains(pGrandmother));
        assertTrue(filtered.contains(mother));
        assertTrue(filtered.contains(mGrandfather));
        assertTrue(filtered.contains(mGrandmother));

        Model.singleton.getFilter().updateFilter(0,false);
        filtered = test.getFilteredPeople();
        assertTrue(!filtered.contains(father));
        assertTrue(!filtered.contains(pGrandfather));
        assertTrue(!filtered.contains(pGrandmother));
        assertTrue(filtered.contains(mother));
        assertTrue(filtered.contains(mGrandfather));
        assertTrue(filtered.contains(mGrandmother));

        Model.singleton.getFilter().updateFilter(0,true);
        Model.singleton.getFilter().updateFilter(1,false);
        filtered = test.getFilteredPeople();
        assertTrue(filtered.contains(father));
        assertTrue(filtered.contains(pGrandfather));
        assertTrue(filtered.contains(pGrandmother));
        assertTrue(!filtered.contains(mother));
        assertTrue(!filtered.contains(mGrandfather));
        assertTrue(!filtered.contains(mGrandmother));

        Model.singleton.getFilter().updateFilter(1,true);
        Model.singleton.getFilter().updateFilter(2,false);
        filtered = test.getFilteredPeople();
        assertTrue(!filtered.contains(father));
        assertTrue(!filtered.contains(pGrandfather));
        assertTrue(filtered.contains(pGrandmother));
        assertTrue(filtered.contains(mother));
        assertTrue(!filtered.contains(mGrandfather));
        assertTrue(filtered.contains(mGrandmother));

        Model.singleton.getFilter().updateFilter(2,true);
        Model.singleton.getFilter().updateFilter(3,false);
        filtered = test.getFilteredPeople();
        assertTrue(filtered.contains(father));
        assertTrue(filtered.contains(pGrandfather));
        assertTrue(!filtered.contains(pGrandmother));
        assertTrue(!filtered.contains(mother));
        assertTrue(filtered.contains(mGrandfather));
        assertTrue(!filtered.contains(mGrandmother));


    }

}