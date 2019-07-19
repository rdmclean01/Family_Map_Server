package Proxy;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import Model.Model;
import Model.Person;
import Model.Utils;
import Proxy.DataTransferObjects.EventRequest;
import Proxy.DataTransferObjects.EventResponse;
import Proxy.DataTransferObjects.LoginRequest;
import Proxy.DataTransferObjects.LoginResponse;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.PersonResponse;

/**
 * Created by rdmcl on 4/11/2018.
 */

public class ResyncTask extends AsyncTask<LoginRequest, Boolean, LoginResponse> {

    private static final String TAG = "ResyncTask";
    private Activity parent;

    public ResyncTask(Activity caller) {
        parent = caller;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... requests) {
        Log.i(TAG, "doInBackground");
        //Toast.makeText(parent,"Resyncing data",Toast.LENGTH_LONG);
        Proxy httpClient = new Proxy();
        LoginResponse response = httpClient.Login(requests[0]);
        if (response != null) {
            PersonResponse personResponse = httpClient.People(new PersonRequest(response.getAuthToken()));
            if (personResponse.getPersons() != null) {
                response.setAncestors(personResponse.getPersons());
            }
            EventResponse eventResponse = httpClient.Event(new EventRequest(response.getAuthToken()));
            if (eventResponse.getEvents() != null) {
                response.setEvents(eventResponse.getEvents());
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(LoginResponse response) {
        String message = "";
        boolean success = false;
        boolean userFound = false;
        if (response.getAuthToken() != null) {  // Successful log in
            Model.singleton.clearData();
            Log.i(TAG + " PostExecute", "Resync was successful");
            Model.singleton.setUserName(response.getUserName());
            //Model.singleton.setPersonId(response.getPersonId());
            Model.singleton.setMessage(response.getMessage());

            Proxy.authToken = response.getAuthToken();
            for (int i = 0; i < response.getAncestors().length; i++) {
                Person cur = response.getAncestors()[i];
                Model.singleton.addToPeople(response.getAncestors()[i]);
                //Model.singleton.addToEvents(response.getEvents()[i]);
                if (response.getPersonId().equals(response.getAncestors()[i].getPersonID())) {
                    Model.singleton.setUser(response.getAncestors()[i]);
                    message = "Welcome back " + cur.getFirstName() + " " + cur.getLastName()
                            + " (" + response.getUserName() + ")";
                    userFound = true;
                    success = true;
                }
            }
            for (int i = 0; i < response.getEvents().length; i++) {
                Model.singleton.addToEvents(response.getEvents()[i]);
            }
            if (!userFound) {
                message = "Login failed: Could not find user among people";
                success = false;

            }
            // message = "Successfully logged " + response.getUserName() + " into the family map";
        } else {                                // Unsuccessful log in
            Log.i(TAG + " PostExecute", "Resync was unsuccessful");
            message = response.getMessage();
            success = false;
        }

        if (success) {
            Utils.startTopActiivty(parent, false);
        } else {
            Toast.makeText(parent,message,Toast.LENGTH_LONG);
        }
    }
}
