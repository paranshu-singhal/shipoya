package com.android.shipoya.shipoya2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements
        VerifyOtpFragment.OnFragmentInteractionListener,
        LoginPage.onLoginInteraction,
        SignUp.OnSignUpInteraction,
        FirstTimeLogin.FirstTimeLoginFragmentListener,
        FragmentSetPassword.FragmentSetPasswordFragmentListener,
        SMSListener.OtpReceive {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver networkStateReceiver;
    private boolean doubleBackToExitPressedOnce = false;
    private String token;
    private String phone_number;
    SMSListener smsListener;
    IntentFilter filterSms;

    private static final String logTag = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (savedInstanceState != null) {
            return;
        }

        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                StaticData.NETWORK_AVAILABLE = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    token = intent.getStringExtra("token");
                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Log.d(logTag, "GCM registration error!");
                } else {
                    Log.d(logTag, "Error occurred");
                }
            }
        };
        Intent intent = new Intent(MainActivity.this, GCMRegistrationIntentService.class);
        startService(intent);

        smsListener = new SMSListener(this);
        filterSms = new IntentFilter();
        filterSms.addAction("android.provider.Telephony.SMS_RECEIVED");

        /*
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Log.d(logTag, "Google Play Service is not install/enabled in this device!");
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Log.d(logTag, "This device does not support for Google Play Service!");
            }
        } else {
            */
        //}

        SharedPreferences test = getSharedPreferences("cookie", Context.MODE_PRIVATE);
//        Log.d(logTag, (test.getStringSet("cookies", null)==null)?"cookies null":"cookies not null");

        if (test.getStringSet("cookies", null) == null || !isNetworkAvailable()) {
            LoginPage loginPage = new LoginPage();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, loginPage, "loginFragment")
                    .commit();
        } else {
            Intent i = new Intent(MainActivity.this, SplashScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onSignUp(Button signUp, final EditText firstName, final EditText lastName, final EditText companyName, final EditText mobile, final EditText email) {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().equals(""))
                    firstName.setError("Enter first name");
                else if (lastName.getText().toString().equals(""))
                    lastName.setError("Enter last name");
                else if (companyName.getText().toString().equals(""))
                    companyName.setError("Enter company name");
                else if (!StaticData.validatePhone(mobile.getText().toString().trim()))
                    mobile.setError("Enter valid mobile number");
                else if (!StaticData.validateEmail(email.getText().toString().trim()))
                    email.setError("Enter valid e-mail");
                else {
                    try {
                        JSONObject main = new JSONObject();
                        JSONObject name = new JSONObject();
                        name.put("first_name", firstName.getText().toString().trim());
                        name.put("last_name", lastName.getText().toString().trim());
                        main.put("formal_name", name);
                        main.put("entity_type", "shipper");
                        main.put("entity_name", companyName.getText().toString().trim());
                        main.put("mobile_number", mobile.getText().toString().trim());
                        main.put("email", email.getText().toString().trim());

                        if (StaticData.NETWORK_AVAILABLE) {
                            (new MakeRequest(
                                    MainActivity.this,
                                    "/user_resources/api/web_signup",
                                    "POST",
                                    true,
                                    main,
                                    new Interface() {
                                        @Override
                                        public void onPostExecute(String response) {
                                            if (response != null && response.length() > 0) {
                                                try {
                                                    JSONObject ans = new JSONObject(response);
                                                    Toast.makeText(MainActivity.this, ans.getString("message") + ". " + getResources().getString(R.string.check_mail_account), Toast.LENGTH_LONG).show();
                                                    onBackPressed();
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("Number", mobile.getText().toString().trim());
//
//                                                    VerifyOtpFragment otpFragment = new VerifyOtpFragment();
//                                                    otpFragment.setArguments(bundle);
//                                                    getSupportFragmentManager()
//                                                            .beginTransaction()
//                                                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
//                                                            .replace(R.id.main_container, otpFragment, "otpFragment")
//                                                            .addToBackStack(null)
//                                                            .commit();
                                                } catch (Throwable t) {
                                                    Log.d(logTag, t.getMessage());
                                                }
                                            } else {
                                                Toast.makeText(MainActivity.this, "User sign up not successful. PLease try again later.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            )).execute();
                        } else {
                            StaticData.showNetworkError(MainActivity.this);
                        }
                    } catch (Exception e) {
                        Log.d("SIGN_UP", e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onLoginTextClicked(TextView loginTextView) {
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onLogin(final EditText username, final EditText password, final Button login, final TextView signup, final TextView forget, final TextView firstTimeLogin) {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StaticData.validatePhone(username.getText().toString().trim())) {
                    username.setError(getResources().getString(R.string.number_error));
                } else if (password.getText().length() <= 0) {
                    password.setError(getResources().getString(R.string.text_error));
                } else {
                    JSONObject main = new JSONObject();
                    try {
                        main.put("remember_me", "true");
                        main.put("identifier", username.getText().toString().trim());
                        main.put("password", password.getText().toString().trim());
                        main.put("reg_id", token);

                        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPref.edit();
                        if (StaticData.NETWORK_AVAILABLE) {
                            (new MakeRequest(
                                    MainActivity.this,
                                    "/auth_resources/api/user_login",
                                    "POST",
                                    true,
                                    main,
                                    new Interface() {
                                        @Override
                                        public void onPostExecute(String response) {
                                            try {
                                                if (response != null && response.length() > 0) {
                                                    JSONObject main = new JSONObject(response);
                                                    if (main.getBoolean("success")) {
                                                        JSONObject userData = main.getJSONObject("user_data");
                                                        editor.putString("img_url", userData.getString("image_url"));

                                                        Log.d(logTag, userData.getString("entity_id"));

                                                        editor.putString("entity_id", userData.getString("entity_id"));
                                                        editor.putString("user_id", userData.getString("user_id"));
                                                        editor.putString("full_name", userData.getString("full_name"));
                                                        editor.putString("entity_type", userData.getString("entity_type"));
                                                        editor.putString("reg_id", token);
                                                        editor.apply();

                                                        Intent i = new Intent(MainActivity.this, SplashScreen.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }
                                            } catch (Throwable t) {
                                                Log.d(logTag, t.getMessage());
                                            }
                                        }
                                    }
                            )).execute();
                        } else {
                            StaticData.showNetworkError(MainActivity.this);
                        }
                    } catch (Throwable t) {
                        Log.d(logTag, "onLogin" + t.getMessage());
                    }
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp signUp = new SignUp();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_container, signUp, "signUp")
                        .addToBackStack(null)
                        .commit();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_container, new FirstTimeLogin(), "FirstTimeLogin")
                        .addToBackStack(null)
                        .commit();
            }
        });

        firstTimeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_container, new FirstTimeLogin(), "FirstTimeLogin")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onFirstTimeLoginPressed(final EditText username) {

        if (StaticData.NETWORK_AVAILABLE) {
            if (!StaticData.validatePhone(username.getText().toString().trim())) {
                username.setError(getResources().getString(R.string.number_error));
            } else {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                    requestSMSPermission();
                } else
                    registerReceiver(smsListener, filterSms);
                try {
                    phone_number = username.getText().toString().trim();
                    (new MakeRequest(
                            MainActivity.this,
                            "/auth_resources/api/forgot_password/reset_choice/mobile/reset_identifier/" + URLEncoder.encode(username.getText().toString().trim(), "UTF-8"),
                            "GET",
                            true,
                            null,
                            new Interface() {
                                @Override
                                public void onPostExecute(String response) {
                                    if (response != null && response.length() > 0) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Number", username.getText().toString().trim());
                                        VerifyOtpFragment otpFragment = new VerifyOtpFragment();
                                        otpFragment.setArguments(bundle);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                                .replace(R.id.main_container, otpFragment, "otpFragment")
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Sorry! This mobile number is not linked to any account. Please contact support.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                    )).execute();
                } catch (Throwable t) {
                    Log.d(logTag, t.getMessage());
                }
            }
        } else {
            StaticData.showNetworkError(MainActivity.this);
        }
    }

    @Override
    public void onOtpReceive(String otp) {
        onVerifyPressed(otp, phone_number);
    }

    @Override
    public void onVerifyPressed(final String otp, final String number) {

        if (StaticData.NETWORK_AVAILABLE) {
            (new MakeRequest(
                    MainActivity.this,
                    "/auth_resources/api/password_reset/mobile_reset/" + number + "/token/" + otp,
                    "GET",
                    true,
                    null,
                    new Interface() {
                        @Override
                        public void onPostExecute(String response) {
                            try {
                                JSONObject main = new JSONObject(response);
                                if (main.getBoolean("success")) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("otp", main.getString("token"));
                                    FragmentSetPassword setPassword = new FragmentSetPassword();
                                    setPassword.setArguments(bundle);

                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                            .replace(R.id.main_container, setPassword, "setPassword")
                                            .commit();
                                }
                                Toast.makeText(MainActivity.this, main.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Throwable t) {
                                Log.d(logTag, t.getMessage());
                            }
                        }
                    }
            )).execute();
        } else {
            StaticData.showNetworkError(MainActivity.this);
        }
    }

    @Override
    public void onSetPassword(String password, String otp) {
        if (StaticData.NETWORK_AVAILABLE) {
            try {
                JSONObject main = new JSONObject();
                main.put("password", password);

                (new MakeRequest(
                        MainActivity.this,
                        "/auth_resources/api/password_reset/set_new_password/token/" + URLEncoder.encode(otp, "UTF-8"),
                        "POST",
                        true,
                        main,
                        new Interface() {
                            @Override
                            public void onPostExecute(String response) {
                                if (response != null && response.length() > 0) {
                                    try {
                                        JSONObject main2 = new JSONObject(response);
                                        if (main2.getBoolean("success")) {
                                            Toast.makeText(MainActivity.this, main2.getString("message"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (Throwable t) {
                                        Log.d(logTag, t.getMessage());
                                    }
                                }
                            }
                        }
                )).execute();
            } catch (Throwable t) {
                Log.d(logTag, t.getMessage());
            }
        } else {
            StaticData.showNetworkError(MainActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("loginFragment") != null && getSupportFragmentManager().findFragmentByTag("loginFragment").isVisible()) {
            if (doubleBackToExitPressedOnce) {
                finish();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (getSupportFragmentManager().findFragmentByTag("otpFragment") != null && getSupportFragmentManager().findFragmentByTag("otpFragment").isVisible()) {
        } else if (getSupportFragmentManager().findFragmentByTag("setPassword") != null && getSupportFragmentManager().findFragmentByTag("setPassword").isVisible()) {
        } else {
            super.onBackPressed();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.RECEIVE_SMS)) {
            //         ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerReceiver(smsListener, filterSms);
            } else {
                Toast.makeText(this, "Allow permissions to continue..", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            }
        }
    }
}






