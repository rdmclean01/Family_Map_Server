package Proxy;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Proxy.DataTransferObjects.RegisterRequest;

import static org.junit.Assert.*;

/**
 * Created by rdmcl on 3/24/2018.
 */
public class RegisterTaskTest {
    private RegisterTask test;
    private RegisterRequest mRequest;
    String username = "a";
    String password = "secret";
    String firstname = "first";
    String lastname = "last";
    String email = "email";
    String gender = "male";

    @Before
    public void setUp() throws Exception {
        test = new RegisterTask(null);
        mRequest = new RegisterRequest(username,password,email,firstname,lastname,gender);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doInBackground() throws Exception {
        test.doInBackground(mRequest);
    }

    @Test
    public void onPostExecute() throws Exception {
    }

}