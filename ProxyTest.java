package Proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Model;
import Model.Person;
import Model.Event;
import Proxy.DataTransferObjects.ClearResult;
import Proxy.DataTransferObjects.EventRequest;
import Proxy.DataTransferObjects.EventResponse;
import Proxy.DataTransferObjects.LoginRequest;
import Proxy.DataTransferObjects.LoginResponse;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.PersonResponse;
import Proxy.DataTransferObjects.RegisterRequest;
import Proxy.DataTransferObjects.RegisterResponse;

import static org.junit.Assert.*;

/**
 * Created by rdmcl on 3/21/2018.
 */
public class ProxyTest {


    Proxy test;


    String serverHost = "localhost";
    String serverPort = "8080";
    String username = "test.proxy";
    String password = "testPassword";
    String email = "test@byu.net";
    String firstName = "test";
    String lastName = "mclean";
    String gender = "M";
    @Before
    public void setUp() throws Exception {
        test = new Proxy();
        Proxy.serverHost = serverHost;
        Proxy.serverPort = serverPort;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void login() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setPassword(password);

        LoginResponse response = test.Login(request);
        System.out.println("Token: " + response.getAuthToken() +
                "\nUsername: " + response.getUserName() +
                "\nPersonID: " + response.getPersonId() +
                "\nMessage: " + response.getMessage());

    }

    @Test
    public void register() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUserName(username);
        request.setPassword(password);
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setGender(gender);

        RegisterResponse response = test.Register(request);
        System.out.println("Token: " + response.getAuthToken() +
                "\nUsername: " + response.getUserName() +
                "\nPersonID: " + response.getPersonId() +
                "\nMessage: " + response.getMessage());
    }

    @Test
    public void people() throws Exception {
        PersonRequest request = new PersonRequest();
        // todo: Hardcoded authToken
        request.setAuthToken("9c1997a7-c1f1-4502-b2eb-b6ad7b0de3c8");

        PersonResponse response = test.People(request);
        for(int i = 0; i < response.getPersons().length;i++){
            Person p = response.getPersons()[i];
            System.out.println(p.getFirstName() + " " + p.getLastName());
        }
    }

    @Test
    public void event() throws Exception {
        EventRequest request = new EventRequest();
        // todo hardcoded authToken
        request.setAuthToken("605a0e0f-8f54-4299-8e65-35ea41ea2ce1");

        EventResponse response = test.Event(request);
        for(int i = 0; i < response.getEvents().length;i++){
            Event e = response.getEvents()[i];
            System.out.println(e.getCity()+ ", " + e.getCountry() + ", " + e.getYear());
        }
    }

    @Test
    public void clear() throws Exception {
        ClearResult response = test.Clear();
        System.out.println(response.getMessage());
    }


}