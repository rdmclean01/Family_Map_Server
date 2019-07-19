package Proxy;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.rdmcl.client.MainActivity;

import Model.Model;
import Model.Person;
import Proxy.DataTransferObjects.EventRequest;
import Proxy.DataTransferObjects.EventResponse;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.PersonResponse;
import Proxy.DataTransferObjects.RegisterRequest;
import Proxy.DataTransferObjects.RegisterResponse;

/**
 * Created by rdmcl on 3/23/2018.
 */

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private static final String TAG = "Register";

    private FragmentActivity parent;

    public RegisterTask(FragmentActivity c) {
        parent = c;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... requests) {

        Log.i(TAG, "doInBackground");
        Proxy httpClient = new Proxy();
        RegisterResponse response = httpClient.Register(requests[0]);
        // Pass in an array, but if there were ever multiple, weird behavior would occur
        if (response != null) {
            PersonResponse personResponse = httpClient.People(new PersonRequest(response.getAuthToken()));
            if (personResponse.getPersons() != null) {
                response.setAncestors(personResponse.getPersons());
            }
            EventResponse eventResponse = httpClient.Event(new EventRequest(response.getAuthToken()));
            if(eventResponse.getEvents() != null){
                response.setEvents(eventResponse.getEvents());
            }
        }

        return response;
    }

    protected void onPostExecute(RegisterResponse response) {
        String message = "";
        boolean userFound = false;
        boolean success = false;
        if (response.getAuthToken() != null) {  // Successful log in
            Log.i(TAG + " onPostExecute", "Register was successful");
            Model.singleton.setUserName(response.getUserName());
            Model.singleton.setPersonId(response.getPersonId());
            Model.singleton.setMessage(response.getMessage());
            Proxy.authToken = response.getAuthToken();
            for (int i = 0; i < response.getAncestors().length; i++) {
                Person cur = response.getAncestors()[i];
                Model.singleton.addToPeople(response.getAncestors()[i]);
                Model.singleton.addToEvents(response.getEvents()[i]);
                if (response.getPersonId().equals(response.getAncestors()[i].getPersonID())) {
                    Model.singleton.setUser(response.getAncestors()[i]);
                    message = "Welcome " + cur.getFirstName() + " " + cur.getLastName()
                            + " (" + response.getUserName() + ")";
                    userFound = true;
                    success = true;
                }
            }
            for(int i = 0; i < response.getEvents().length;i++){
                Model.singleton.addToEvents(response.getEvents()[i]);
            }
            if (!userFound) {
                message = "Registration failed: Could not extract user's ancestors";
                success = false;
            }
        } else {                                // Unsuccessful log in
            Log.i(TAG + " onPostExecute", "Login was unsuccessful");
            message = "Registration Failed: " + response.getMessage();
        }
        MainActivity main = (MainActivity) parent;
        main.setLoginSuccess(success, message);
        //Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
    }
}
