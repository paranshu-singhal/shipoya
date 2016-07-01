package com.android.shipoya.shipoya2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity implements EnterPhoneNumberFragment.OnFragmentInteractionListener,
        VerifyOtpFragment.OnFragmentInteractionListener,
        LoginPageFragment.OnFragmentInteractionListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String logTag = "logTag";
    int response_code_1, response_code_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_main);
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
                    Log.d(logTag, "GCM registration error!");
                } else {
                    Log.d(logTag, "Error occurred");
                }
            }
        };

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
    protected void onResume() {
        super.onResume();
        //Log.d(logTag, "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.w(logTag, "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onFragmentInteraction(final EditText username, final EditText password, Button btnLogin) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new load(username.getText().toString(), password.getText().toString())).execute();
            }
        });
    }

    public class load extends AsyncTask<Void, Void, String> {

        String username, password;

        public load(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... voids) {

            StringBuilder buffer = null;
            try {
                HttpsURLConnection conn = (HttpsURLConnection) (new URL("https://www.shipoya.com/auth_resources/api/user_login")).openConnection();

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                JSONObject obj = new JSONObject();
                obj.put("identifier", username);
                obj.put("password", password);
                obj.put("remember_me", "true");

                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(obj.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                Log.d(logTag, String.format("code %d", conn.getResponseCode()));
                response_code_1 = conn.getResponseCode();

                buffer = new StringBuilder();
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                SharedPreferences sharedPref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                List<String> cookiesHeader = conn.getHeaderFields().get("Set-Cookie");
                if (cookiesHeader != null) {
                    Set<String> foo = new HashSet<>(cookiesHeader);
                    editor.putStringSet("cookies", foo).apply();
                }
                is.close();
                conn.disconnect();

            } catch (Throwable t) {
                Log.d(logTag, t.getMessage());
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.getBoolean("success")) {
                    SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("entity_id", obj.getJSONObject("user_data").getString("entity_id"));
                    editor.putString("full_name", obj.getJSONObject("user_data").getString("full_name"));
                    editor.putString("entity_type", obj.getJSONObject("user_data").getString("entity_type"));
                    editor.putString("image_url", obj.getJSONObject("user_data").getString("image_url"));
                    editor.apply();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.invalid_pp), Toast.LENGTH_LONG).show();
                }

            } catch (Throwable t) {
                Log.d(logTag, "exception: " + t.getMessage());
            }
            (new loadOrderData()).execute();
        }
    }

    public class loadOrderData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            StringBuilder buffer = null;
            SharedPreferences sharedPref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
            try {
                HttpsURLConnection conn = (HttpsURLConnection) (new URL("https://www.shipoya.com/auction_resources/api/loads/shipper_loads")).openConnection();

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Cookie", TextUtils.join(";", sharedPref.getStringSet("cookies", null)));
                conn.connect();

                Log.d(logTag, "loadOrderData" + String.format("code %d", conn.getResponseCode()));
                response_code_2 = conn.getResponseCode();

                buffer = new StringBuilder();
                InputStream is =
                conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                is.close();
                conn.disconnect();
            } catch (Throwable t) {
                Log.d(logTag, t.getMessage());
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (response_code_1 == 200 && response_code_2==200 && s!=null) {

                    File fileWeatherCache = new File(getFilesDir(), "ordersCache");
                    if (fileWeatherCache.exists()) {
                        FileOutputStream fos = openFileOutput("ordersCache", Context.MODE_PRIVATE);
                        fos.write(s.getBytes());
                        fos.close();
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(), "ordersCache")));
                        oos.writeObject(s);
                    }
                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
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
