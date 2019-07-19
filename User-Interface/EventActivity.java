package com.example.rdmcl.client;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

/**
 * Zooms the google map fragment onto a specific event
 * Displays info about an event???
 * todo Extend something different because of the fragment
 */
public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Use a bundle
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        // todo put stuff into intent
        bundle.putString("eventID",intent.getStringExtra("eventID"));
        bundle.putBoolean("hasEvent",intent.getBooleanExtra("hasEvent",false));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.maps_fragment);

        if(fragment == null){
            fragment = new MapsFragment();
        }
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}
