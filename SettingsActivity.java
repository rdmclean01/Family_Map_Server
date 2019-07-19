package com.example.rdmcl.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import Model.Model;
import Model.Utils;
import Proxy.DataTransferObjects.LoginRequest;
import Proxy.ResyncTask;

public class SettingsActivity extends AppCompatActivity {

    // Widgets
    Spinner mStorySpinner;
    Spinner mFamilySpinner;
    Spinner mSpouseSpinner;
    Spinner mMapSpinner;
    Switch mStorySwitch;
    Switch mFamilySwitch;
    Switch mSpouseSwitch;
    LinearLayout mReSyncData;
    LinearLayout mLogout;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mStorySpinner = (Spinner) findViewById(R.id.settings_spinner_life_story);
        mStorySpinner.setSelection(Model.singleton.getSettings().getPosLifeStory());
        mStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Model.singleton.getSettings().setPosLifeStory(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStorySwitch = (Switch) findViewById(R.id.settings_switch_life_story);
        mStorySwitch.setChecked(Model.singleton.getSettings().isDisplayLifeStory());
        mStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Model.singleton.getSettings().setDisplayLifeStory(b);
            }
        });

        mFamilySpinner = (Spinner) findViewById(R.id.settings_spinner_family);
        mFamilySpinner.setSelection(Model.singleton.getSettings().getPosFamilyTree());
        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Model.singleton.getSettings().setPosFamilyTree(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFamilySwitch = (Switch) findViewById(R.id.settings_switch_family);
        mFamilySwitch.setChecked(Model.singleton.getSettings().isDisplayFamilyTree());
        mFamilySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Model.singleton.getSettings().setDisplayFamilyTree(b);
            }
        });

        mSpouseSpinner = (Spinner) findViewById(R.id.settings_spinner_spouse);
        mSpouseSpinner.setSelection(Model.singleton.getSettings().getPosSpouse());
        mSpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Model.singleton.getSettings().setPosSpouse(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpouseSwitch = (Switch) findViewById(R.id.settings_switch_spouse);
        mSpouseSwitch.setChecked(Model.singleton.getSettings().isDisplaySpouseLine());
        mSpouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Model.singleton.getSettings().setDisplaySpouseLine(b);
            }
        });

        mMapSpinner = (Spinner) findViewById(R.id.settings_spinner_map);
        mMapSpinner.setSelection(Model.singleton.getSettings().getPosMapType());
        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Model.singleton.getSettings().setPosMapType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mReSyncData = (LinearLayout) findViewById(R.id.setting_resync);
        mReSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new ResyncTask(SettingsActivity.this).execute(new LoginRequest(Model.singleton.getUserName(),Model.singleton.getPassword()));
            }
        });

        mLogout = (LinearLayout) findViewById(R.id.setting_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the data and return to the login screen
                Model.singleton.clearData();
                Model.singleton.clearSettings();
                Utils.startTopActiivty(getApplicationContext(),true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Model.singleton.getSettings().setPosMapType(mMapSpinner.getSelectedItemPosition());
                Utils.startTopActiivty(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
