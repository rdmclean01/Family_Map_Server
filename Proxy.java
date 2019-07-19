package Proxy;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Model.Model;
import Proxy.DataTransferObjects.ClearResult;
import Proxy.DataTransferObjects.EventRequest;
import Proxy.DataTransferObjects.EventResponse;
import Proxy.DataTransferObjects.LoginRequest;
import Proxy.DataTransferObjects.LoginResponse;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.PersonResponse;
import Proxy.DataTransferObjects.RegisterRequest;
import Proxy.DataTransferObjects.RegisterResponse;

/**
 * Created by rdmcl on 3/21/2018.
 */

public class Proxy {

    // Singleton objects to access with passing as parameters
    public static String serverHost = "";
    public static String serverPort = "";
    public static String authToken = "";


    /**
     * Contacts the server to log in a pre-existing user
     *
     * @param request contains info about the user to log in
     * @return LoginResponse
     */
    public LoginResponse Login(LoginRequest request) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");   // Specify type
            http.setDoOutput(true);  // There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();  // Send the request to the server

            String reqData = encode(request);  // Make a request string
            OutputStream reqBody = http.getOutputStream();   // Get the request body
            writeString(reqData, reqBody);                   // Write the string to the request body
            reqBody.close();                                 // Close the requestBody

            LoginResponse response = new LoginResponse();
            // Handling the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the JSON string and convert it to JAVA
                response = (LoginResponse) decode(http.getInputStream(), LoginResponse.class);
            } else {
                // Handle an error message, print the error message to the screen
                //todo I don't know how to handle these errors
                System.out.println("ERROR: " + http.getResponseMessage());
                response.setUserName(request.getUserName());
                response.setMessage("ERROR: " + http.getResponseMessage());
                //Model.singleton.setMessage("ERROR: " + http.getResponseMessage());
            }
            return response;

        } catch (IOException e) {
            // todo handle exception
            Log.i("Login Proxy", "Error: could not receive data from database");
            e.printStackTrace();
        }
        return null; // Return an error. This shouldn't execute
    }

    /**
     * Contacts the server and asks to Register a new user
     *
     * @param request contains information about request
     * @return the RegisterResponse
     */
    public RegisterResponse Register(RegisterRequest request) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");   // Specify type
            http.setDoOutput(true);  // There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();  // Send the request to the server

            String reqData = encode(request);  // Make a request string
            OutputStream reqBody = http.getOutputStream();   // Get the request body
            writeString(reqData, reqBody);                   // Write the string to the request body
            reqBody.close();                                 // Close the requestBody

            RegisterResponse response = new RegisterResponse();
            // Handling the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the JSON string and convert it to JAVA
                response = (RegisterResponse) decode(http.getInputStream(), RegisterResponse.class);

            } else {
                // Handle an error message, print the error message to the screen
                //todo I don't know how to handle these errors
                System.out.println("ERROR: " + http.getResponseMessage());
                response.setUserName(request.getUserName());
                response.setMessage("ERROR: " + http.getResponseMessage());
                Log.i("Register Proxy", "Did not receive HTTP OK");
            }
            return response;
        } catch (IOException e) {
            // todo handle exception
            //Log.i("Register Proxy", "Error: could not receive data from database");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Requests the array of ancestors for the current user
     * @param request contains the authToken needed for request
     * @return the array of people grabbed from the database
     */
    public PersonResponse People(PersonRequest request) {
        //todo: turn on assert statements
        assert request.getAuthToken() == Proxy.authToken;
        PersonResponse response = new PersonResponse();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // No request body
            http.addRequestProperty("Authorization", request.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the JSON string and convert it to JAVA
                response = (PersonResponse) decode(http.getInputStream(), PersonResponse.class);
                System.out.println("STOP");
            } else {
                Log.i("Person Proxy", "Did not receive HTTP OK");
            }
            return response;

        } catch (IOException e) {
            Log.i("People Proxy", "Error: could not receive data from database");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Grabs event data from the database
     * @param request contains info about request
     * @return the eventResponse
     */
    public EventResponse Event(EventRequest request){
        EventResponse response = new EventResponse();
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // No request body
            http.addRequestProperty("Authorization", request.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the JSON string and convert it to JAVA
                response = (EventResponse) decode(http.getInputStream(), EventResponse.class);
                System.out.println("STOP");
            } else {
                Log.i("Event Proxy", "Did not receive HTTP OK");
            }
            return response;
        }catch (IOException e) {
            Log.i("Event Proxy", "Error: could not receive data from database");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Clears the database
     * This is only for internal testing. This should not exist in application
     * @return whether the database was cleared or not
     */
    public ClearResult Clear(){
        ClearResult response = new ClearResult();
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);    // No request body

            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the JSON string and convert it to JAVA
                response = (ClearResult) decode(http.getInputStream(), ClearResult.class);
                System.out.println("STOP");
            } else {
                Log.i("Clear Proxy", "Did not receive HTTP OK");
            }
            return response;
        }catch (IOException e) {
            Log.i("Clear Proxy", "Error: could not receive data from database");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Writes a string to an output stream
     * @param str to write into output stream
     * @param os output stream as the destination
     * @throws IOException when failure occurs
     */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    /**
     * From Json to Java
     *
     * @param input is a JSON string to be converted to JAVA
     * @param cls   is the JAVA class to put the string in
     * @return is the JAVA object
     */
    private static Object decode(InputStream input, Class<?> cls) {
        InputStreamReader reader = new InputStreamReader(input);

        Gson gson = new Gson();
        return gson.fromJson(reader, cls);
    }


    /**
     * From Java to JSON
     *
     * @param output is a Java object to be converted to JSON
     * @return is a JSON string
     */
    private static String encode(Object output) {

        Gson gson = new Gson();
        return gson.toJson(output);
    }

}
