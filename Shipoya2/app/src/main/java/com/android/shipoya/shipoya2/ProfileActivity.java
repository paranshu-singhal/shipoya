package com.android.shipoya.shipoya2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    final int PI = 1;
    CircleImageView profileImage;
    private static final String logTag = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        (new LoadProfile()).execute();

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                Intent chooser = Intent.createChooser(i, "Choose via");
                startActivityForResult(chooser, PI);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileImage.post(new Runnable() {
            @Override
            public void run() {
                if (requestCode == PI && data != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        profileImage.setImageBitmap(decoded);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.error_image), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class LoadProfile extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ProfileActivity.this, "", getResources().getString(R.string.loading_wait), true);
        }

        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences sharedPrefUser = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String url = "https://www.shipoya.com/entity_resources/api/shippers/" + sharedPrefUser.getString("entity_id", null);
            return MakeRequest.sendRequest(ProfileActivity.this, url, "GET", null);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            (findViewById(R.id.main_layout)).setVisibility(View.VISIBLE);
            (findViewById(R.id.FrameLayoutProfile)).setVisibility(View.VISIBLE);

            //SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dTk:m:s");
            //SimpleDateFormat date2 = new SimpleDateFormat("d MMM yyyy");

            try {
                JSONObject container = (new JSONObject(s)).getJSONObject("data");
                ((TextView) findViewById(R.id.ContactPerson)).setText(container.getJSONObject("shipper_contact").getJSONObject("contact_name").getString("first_name") + " " + container.getJSONObject("shipper_contact").getJSONObject("contact_name").getString("last_name"));
                //((TextView)findViewById(R.id.active_since)).setText(date2.format((container.getString("date_added").split("T"))[0]));
                ((TextView) findViewById(R.id.carr_name)).setText(container.getString("shipper_entity_name"));
                ((TextView) findViewById(R.id.ComapanyName)).setText(container.getString("shipper_entity_name"));
                ((EditText) findViewById(R.id.ServiceTaxNo)).setText(container.getString("service_tax_num"));
                ((EditText) findViewById(R.id.CompanyPanNo)).setText(container.getString("shipper_pan_num"));
                ((EditText) findViewById(R.id.address1)).setText(container.getJSONObject("primary_office").getJSONObject("office_address").getString("line1"));
                ((EditText) findViewById(R.id.address2)).setText(container.getJSONObject("primary_office").getJSONObject("office_address").getString("area"));
                ((AutoCompleteTextView) findViewById(R.id.city)).setText(container.getJSONObject("primary_office").getJSONObject("office_address").getString("city"));
                ((AutoCompleteTextView) findViewById(R.id.state)).setText(container.getJSONObject("primary_office").getJSONObject("office_address").getString("state"));
                ((EditText) findViewById(R.id.e_mail)).setText(container.getJSONObject("shipper_contact").getString("contact_email"));
                ((EditText) findViewById(R.id.officeContact)).setText(container.getJSONObject("primary_office").getJSONObject("primary_phone").getString("phone_number"));
                //Log.d(logTag, container.getString("message"));
            } catch (Throwable t) {
                Log.d(logTag, t.getMessage());
            }
        }
    }
}



