package com.android.shipoya.shipoya2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity implements EnterPhoneNumberFragment.OnFragmentInteractionListener,
        VerifyOtpFragment.OnFragmentInteractionListener,
        LoginPageFragment.OnFragmentInteractionListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String logTag = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (savedInstanceState != null) {
            return;
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService


            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");


                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();


                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Log.d(logTag ,"GCM registration error!");
                } else {
                    Log.d(logTag ,"Error occurred");
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Log.d(logTag,"Google Play Service is not install/enabled in this device!" );
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Log.d(logTag,"This device does not support for Google Play Service!" );
            }
        } else {
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }
            EnterPhoneNumberFragment fragmentPhoneNumber = new EnterPhoneNumberFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, fragmentPhoneNumber)
                    .commit();
    }

    @Override
    public void onButtonPressed(Button Btn, final EditText code, final EditText number) {

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().length() <= 0) {
                    code.setError(getResources().getString(R.string.text_error));
                } else if (number.getText().length() != 10) {
                    number.setError(getResources().getString(R.string.number_error));
                } else {
                    VerifyOtpFragment otpFragment = new VerifyOtpFragment();
                    Bundle bun = new Bundle();
                    bun.putString("CountryCode", code.getText().toString());
                    bun.putString("Number", number.getText().toString());
                    otpFragment.setArguments(bun);

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left)
                            .replace(R.id.main_container, otpFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

    }


    @Override
    public void onVerifyPressed(Button btn, final EditText otp) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().equals("1234")) {
                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onLoginClicked1(TextView loginTextView) {

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPageFragment loginFragment = new LoginPageFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left)
                        .replace(R.id.main_container, loginFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public void onLoginClicked2(TextView loginTextView) {

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPageFragment loginFragment = new LoginPageFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left)
                        .replace(R.id.main_container, loginFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    @Override
    public void onFragmentInteraction(EditText username, EditText password) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(logTag, "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(logTag, "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
