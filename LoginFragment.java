package com.example.rdmcl.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import Model.Model;
import Proxy.DataTransferObjects.LoginRequest;
import Proxy.DataTransferObjects.PersonRequest;
import Proxy.DataTransferObjects.RegisterRequest;
import Proxy.Proxy;
import Proxy.LoginTask;
import Proxy.PeopleTask;
import Proxy.RegisterTask;

/**
 * Class that handles the login page of the application
 */
public class LoginFragment extends Fragment {

    // Need to create all the different widgets
    EditText mServerHost;
    EditText mServerPort;
    EditText mUsername;
    EditText mPassword;
    EditText mFirstName;
    EditText mLastName;
    EditText mEmail;

    Button mLogin;
    Button mRegister;
    Button mClear;
    RadioGroup mGender;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        //final Context context = this.getContext();

        mLogin = (Button) v.findViewById(R.id.button_login);
        mRegister = (Button) v.findViewById(R.id.button_register);
        mClear = (Button) v.findViewById(R.id.button_clear);
        mServerHost = (EditText) v.findViewById(R.id.hostEditText);
        mServerPort = (EditText) v.findViewById(R.id.portEditText);
        mUsername = (EditText) v.findViewById(R.id.userEditText);
        mPassword = (EditText) v.findViewById(R.id.passwordEditText);
        mFirstName = (EditText) v.findViewById(R.id.firstEditText);
        mLastName = (EditText) v.findViewById(R.id.lastEditText);
        mEmail = (EditText) v.findViewById(R.id.emailEditText);
        mGender = (RadioGroup) v.findViewById(R.id.gender_group);



        mLogin.setEnabled(canLogin());
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mLogin.setEnabled(false);   // Disables the button

                // Sets up the static Host and Port variables
                Proxy.serverHost = mServerHost.getText().toString();
                Proxy.serverPort = mServerPort.getText().toString();
                FragmentActivity thing = getActivity();

                // Create a request object
                // Call proxy server using the async task
                new LoginTask(getActivity()).execute(new LoginRequest(mUsername.getText().toString(), mPassword.getText().toString()));
            }
        });

        mRegister.setEnabled(canRegister());
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mRegister.setEnabled(false);
                // Sets up the static Host and Port variables
                Proxy.serverHost = mServerHost.getText().toString();
                Proxy.serverPort = mServerPort.getText().toString();
                String gender;
                String message;
                if(mGender.getCheckedRadioButtonId() == R.id.radio_male){
                    gender = "m";
                }
                else if(mGender.getCheckedRadioButtonId() == R.id.radio_female){
                    gender = "f";
                }
                else{
                    gender = "ERROR";
                }
                RegisterRequest request = new RegisterRequest(mUsername.getText().toString(), mPassword.getText().toString(),
                        mEmail.getText().toString(), mFirstName.getText().toString(), mLastName.getText().toString()
                        , gender);
                new RegisterTask(getActivity()).execute(request);
            }
        });

        mClear.setEnabled(true);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername.setText(null);
                mPassword.setText(null);
                mFirstName.setText(null);
                mLastName.setText(null);
                mEmail.setText(null);
                mGender.clearCheck();
            }
        });

        mServerHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLogin.setEnabled(canLogin());
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mServerPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLogin.setEnabled(canLogin());
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLogin.setEnabled(canLogin());
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLogin.setEnabled(canLogin());
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRegister.setEnabled(canRegister());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mRegister.setEnabled(canRegister());
            }
        });


        return v;
    }

    private boolean canLogin() {
        if (mServerHost == null) {
            return false;
        }
        if (mServerPort == null) {
            return false;
        }
        if (mUsername == null) {
            return false;
        }
        if (mPassword == null) {
            return false;
        }
        boolean host = mServerHost.getText().length() > 0;
        boolean port = mServerPort.getText().length() > 0;
        boolean username = mUsername.getText().length() > 0;
        boolean password = mPassword.getText().length() > 0;
        return host && port && username && password;
    }

    private boolean canRegister() {
        if (mFirstName == null) {
            return false;
        }
        if (mLastName == null) {
            return false;
        }
        if(mEmail == null){
            return false;
        }
        if(mGender == null){
            return false;
        }
        boolean firstName = mFirstName.getText().length() > 0;
        boolean lastName = mLastName.getText().length() > 0;
        boolean email = mEmail.getText().length() > 0;
        boolean gender = mGender.getCheckedRadioButtonId() == R.id.radio_male || mGender.getCheckedRadioButtonId() == R.id.radio_female;
        return firstName && lastName && email && gender && canLogin();
    }
}
