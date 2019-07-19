package Proxy;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import Model.Model;
import Model.Person;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.PersonResponse;

/**
 * Created by rdmcl on 3/23/2018.
 */

public class PeopleTask extends AsyncTask<PersonRequest, Void, PersonResponse> {
    private static final String TAG = "PeopleTask";
    private Context context;

    public PeopleTask(Context c) {
        context = c;
    }

    @Override
    protected PersonResponse doInBackground(PersonRequest... requests) {
        Log.i(TAG, "doInBackground");
        Proxy httpClient = new Proxy();
        PersonResponse response = new PersonResponse();
        for (int i = 0; i < requests.length; i++) {
            response = httpClient.People(requests[i]);
        }

        return response;
    }

    protected void onPostExecute(PersonResponse response) {
        Person[] ancestors = response.getPersons();
        if (ancestors != null) {
            Log.i(TAG + "onPostExecute", "Successfully received people");
            boolean success = false;
            for (int i = 0; i < ancestors.length; i++) {

                Model.singleton.addToPeople(ancestors[i]);
                if (ancestors[i].getPersonID().equals(Model.singleton.getPersonId())) {
                    Model.singleton.setUser(ancestors[i]);
                    Log.i(TAG + "onPostExecute", "Person associated with user: " + ancestors[i].getFirstName() + " " + ancestors[i].getLastName());
                    //Toast.makeText(context,"Person associated with user: " + ancestors[i].getFirstName() + " " + ancestors[i].getLastName(),Toast.LENGTH_LONG).show();
                success = true;
                }
            }

            if(success){
                Toast.makeText(context,"Person associated with user: " + Model.singleton.getUser().getFirstName() + " " + Model.singleton.getUser().getLastName(),Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context,"Could not find descendant",Toast.LENGTH_LONG).show();
            }

        } else {
            Log.i(TAG + "onPostExecute", "Received no people from server");
        }
        Toast.makeText(context,"PersonTask was called and finished",Toast.LENGTH_LONG).show();
    }
}
