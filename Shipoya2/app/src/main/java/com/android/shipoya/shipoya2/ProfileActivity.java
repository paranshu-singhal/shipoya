package com.android.shipoya.shipoya2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView profileImage;
    TextInputEditText firstName, lastName, contactPhone, eMail, addressLine1, area, zipCode, country, companyName, panNo, serviceTaxNo;

    String phone_number = "", phone_type = "", area_code = "";

    Button editContact, editOffice;
    AutoCompleteTextView state, city;
    boolean contactB = true, officeB = true;
    final int PI = 1;

    private final String logTag = "logTag";
    SharedPreferences preferencesUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        boundViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferencesUser = getSharedPreferences("user_data", MODE_PRIVATE);

        if (StaticData.NETWORK_AVAILABLE) {
            setUpData();
        } else {
            Toast.makeText(ProfileActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
        }

    }

    public void boundViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstName = (TextInputEditText) findViewById(R.id.first_name);
        lastName = (TextInputEditText) findViewById(R.id.last_name);
        contactPhone = (TextInputEditText) findViewById(R.id.contact_no);
        eMail = (TextInputEditText) findViewById(R.id.email);
        addressLine1 = (TextInputEditText) findViewById(R.id.address_line1);
        area = (TextInputEditText) findViewById(R.id.area);
        zipCode = (TextInputEditText) findViewById(R.id.zip_code);
        country = (TextInputEditText) findViewById(R.id.country);
        companyName = (TextInputEditText) findViewById(R.id.company_name);
        panNo = (TextInputEditText) findViewById(R.id.company_pan_no);
        serviceTaxNo = (TextInputEditText) findViewById(R.id.service_tax_no);
        editContact = (Button) findViewById(R.id.edit_contact);
        editOffice = (Button) findViewById(R.id.edit_office);
        state = (AutoCompleteTextView) findViewById(R.id.state);
        city = (AutoCompleteTextView) findViewById(R.id.city);
        //profileImage = (CircleImageView) findViewById(R.id.profileImage);

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact();
            }
        });

        editOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOffice();
            }
        });
        /*
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                Intent chooser = Intent.createChooser(i, "Choose via");
                startActivityForResult(chooser, PI);
            }
        });
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                showNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNotification() {
        Intent i = new Intent(this, NotificationActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        }
    }

    private void editContact() {
        if (contactB) {
            firstName.setEnabled(true);
            lastName.setEnabled(true);
            contactPhone.setEnabled(true);
            eMail.setEnabled(true);
            editContact.setText(getResources().getString(R.string.update));
            contactB = false;
        } else {
            if (firstName.getText().toString().trim().equals(""))
                firstName.setError("First name can't be empty");
            else if (lastName.getText().toString().trim().equals(""))
                lastName.setError("Last name can't be empty");
            else if (contactPhone.getText().toString().trim().equals(""))
                contactPhone.setError("Contact no. can't be empty");
            else if (eMail.getText().toString().trim().equals(""))
                eMail.setError("E-Mail can't be empty");
            else if (!eMail.getText().toString().trim().contains("@"))
                eMail.setError("Enter valid E-Mail");
            else {
                try {
                    JSONObject main = new JSONObject();

                    JSONObject name = new JSONObject();
                    name.put("first_name", firstName.getText().toString().trim());
                    name.put("last_name", lastName.getText().toString().trim());
                    main.put("contact_name", name);

                    JSONObject contact = new JSONObject();
                    contact.put("phone_type", "mobile");
                    contact.put("phone_number", contactPhone.getText().toString().trim());

                    main.put("contact_mobile", contact);
                    main.put("contact_email", eMail.getText().toString().trim());

                    Log.d(logTag, main.toString());

                    if (StaticData.NETWORK_AVAILABLE) {
                        uploadData("/entity_resources/api/shippers/" + preferencesUser.getString("entity_id", null) + "/contact", main);
                    } else {
                        StaticData.showNetworkError(ProfileActivity.this);
                    }

                } catch (Throwable e) {
                    Log.d(logTag, e.getMessage());
                } finally {
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    contactPhone.setEnabled(false);
                    eMail.setEnabled(false);
                    editContact.setText(getResources().getString(R.string.edit));
                    contactB = true;
                }
            }
        }
    }

    private void editOffice() {
        if (officeB) {
            addressLine1.setEnabled(true);
            area.setEnabled(true);
            zipCode.setEnabled(true);
            country.setEnabled(true);

            state.setEnabled(true);
            city.setEnabled(true);
            editOffice.setText(getResources().getString(R.string.update));
            officeB = false;
        } else {
            if (addressLine1.getText().toString().trim().equals(""))
                addressLine1.setError("address line can't be empty");
            else if (area.getText().toString().trim().equals(""))
                area.setError("area can't be empty");
            else if (zipCode.getText().toString().trim().equals(""))
                zipCode.setError("Zip code can't be empty");
            else if (country.getText().toString().trim().equals(""))
                country.setError("country can't be empty");
            else if (state.getText().toString().trim().equals(""))
                state.setError("state can't be empty");
            else if (city.getText().toString().trim().equals(""))
                city.setError("city can't be empty");
            else {
                try {
                    JSONObject main = new JSONObject();

                    JSONObject office = new JSONObject();
                    office.put("line1", addressLine1.getText().toString());

                    office.put("area", area.getText().toString().trim());
                    office.put("city", city.getText().toString().trim());
                    office.put("state", state.getText().toString().trim());
                    office.put("zip_code", zipCode.getText().toString().trim());
                    office.put("country", country.getText().toString().trim());
                    main.put("office_address", office);

                    JSONObject primary = new JSONObject();
                    primary.put("phone_type", phone_type);
                    primary.put("area_code", area_code);
                    primary.put("phone_number", phone_number);
                    main.put("primary_phone", primary);

                    Log.d(logTag, main.toString());

                    if (StaticData.NETWORK_AVAILABLE) {
                        uploadData("/entity_resources/api/shippers/" + preferencesUser.getString("entity_id", null) + "/primary_office", main);
                    } else {
                        Toast.makeText(ProfileActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                } finally {
                    addressLine1.setEnabled(false);
                    area.setEnabled(false);
                    zipCode.setEnabled(false);
                    country.setEnabled(false);

                    state.setEnabled(false);
                    city.setEnabled(false);
                    editOffice.setText(getResources().getString(R.string.edit));
                    officeB = true;
                }
            }
        }
    }

    private void setUpData() {
        (new MakeRequest(
                ProfileActivity.this,
                "/entity_resources/api/shippers/" + preferencesUser.getString("entity_id", null),
                "GET",
                true,
                null,
                new Interface() {
                    @Override
                    public void onPostExecute(String response) {
                        (findViewById(R.id.FrameLayoutProfile)).setVisibility(View.VISIBLE);
                        (findViewById(R.id.content_profile)).setVisibility(View.VISIBLE);

                        try {
                            JSONObject object = (new JSONObject(response)).getJSONObject("data");

                            serviceTaxNo.setText(object.getString("service_tax_num"));
                            companyName.setText(object.getString("shipper_regd_name"));
                            ((TextView) findViewById(R.id.carr_name)).setText(object.getString("shipper_regd_name"));
                            StaticData.imageLoader.displayImage(object.getString("shipper_logo"), (ImageView) findViewById(R.id.profile_image));
                            panNo.setText(object.getString("shipper_pan_num"));

                            //contact details
                            if (object.has("shipper_contact")) {
                                JSONObject contact = object.getJSONObject("shipper_contact");
                                firstName.setText(contact.getJSONObject("contact_name").getString("first_name"));
                                lastName.setText(contact.getJSONObject("contact_name").getString("last_name"));
                                eMail.setText(contact.getString("contact_email"));
                                contactPhone.setText(contact.getJSONObject("contact_mobile").getString("phone_number"));
                            }
                            //office details
                            if (object.has("primary_office")) {
                                JSONObject office = object.getJSONObject("primary_office");
                                addressLine1.setText(office.getJSONObject("office_address").getString("line1"));
                                city.setText(office.getJSONObject("office_address").getString("city"));
                                state.setText(office.getJSONObject("office_address").getString("state"));
                                area.setText(office.getJSONObject("office_address").getString("area"));
                                zipCode.setText(office.getJSONObject("office_address").getString("zip_code"));
                                country.setText(office.getJSONObject("office_address").getString("country"));
                                area_code = office.getJSONObject("primary_phone").getString("area_code");
                                phone_number = office.getJSONObject("primary_phone").getString("phone_number");
                                phone_type = office.getJSONObject("primary_phone").getString("phone_type");
                            }
                        } catch (Throwable t) {
                            Log.d(logTag, t.getMessage());
                        }
                    }
                }
        )).execute();
    }

    public void uploadData(String url, JSONObject container) {
        (new MakeRequest(
                this,
                url,
                "POST",
                true,
                container,
                new Interface() {
                    @Override
                    public void onPostExecute(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Toast.makeText(ProfileActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Throwable t) {
                            Log.d(logTag, t.getMessage());
                        }
                    }
                }
        )).execute();
    }
}
