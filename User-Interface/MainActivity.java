package com.example.rdmcl.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import Model.Model;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ERROR = 0;
    private static final int REQUEST_CODE_FILTER = 0;
    private static final int REQUEST_CODE_SETTINGS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        //Fragment fragment = fm.
        Fragment fragment = fm.findFragmentById(R.id.login_fragment);

        if (fragment == null) {
            fragment = new LoginFragment();
        }
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    /**
     * Method that tells the Main Activity to switch fragments
     *
     * @param loginSuccess to communicate success
     */
    public void setLoginSuccess(boolean loginSuccess, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        if (loginSuccess) {
            Model.singleton.getSettings().setUpSettings();
            // Switch to the other fragment
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.maps_fragment);
            //Bundle bundle = new Bundle();
            //bundle.putBoolean("hasEvent",false);

            if (fragment == null) {
                fragment = new MapsFragment();

            }
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        } else {
            // todo find a way to ungray the buttons
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    // Leave if services are unavailable.
                                    finish();
                                }
                            });
            errorDialog.show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_FILTER) {
            MapsFragment frag = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            frag.redrawLines(data.getBooleanExtra("needUpdate",true));
        }
    }


}
