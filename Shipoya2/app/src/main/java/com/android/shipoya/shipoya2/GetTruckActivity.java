package com.android.shipoya.shipoya2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class GetTruckActivity extends AppCompatActivity implements
        DatePickerFragment.giveDate,
        TimePickerFragment.giveTime,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        TextWatcher,
        TruckTypeDialogFragment.onInteractionListener,
        PaymentModeDialogFragment.onInteractionListener {

    private static final String logTag = "logTag";
    private GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView fromAutoTextView;
    AutoCompleteTextView toAutoTextView;
    String pickup_date_ms;
    String Pickup_date_ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_truck);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        buildGoogleApiClient();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.crossNumber_container);
        linearLayout.setOnClickListener(this);

        fromAutoTextView = (AutoCompleteTextView) findViewById(R.id.FromAutocomplete);
        toAutoTextView = (AutoCompleteTextView) findViewById(R.id.ToAutocomplete);

        fromAutoTextView.addTextChangedListener(this);
        toAutoTextView.addTextChangedListener(this);

        ((TextView) findViewById(R.id.textViewPayment1)).setText(getResources().getStringArray(R.array.payment_type_1)[0]);
        ((TextView) findViewById(R.id.textViewPayment2)).setText(getResources().getStringArray(R.array.payment_type_2)[0]);

        ((TextView) findViewById(R.id.textViewTruckType1)).setText(getResources().getStringArray(R.array.truck_type_1)[0]);
        ((TextView) findViewById(R.id.textViewTruckType2)).setText(getResources().getStringArray(R.array.truck_type_2)[0]);

        (findViewById(R.id.button_get_truck)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_get_truck, menu);
        return true;
    }

    public void scheduleOnClick(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onGiveDate(int year, int monthOfYear, int dayOfMonth) {
        pickup_date_ms = String.format("%d %d %d", year, monthOfYear, dayOfMonth);
        //Log.d(logTag, String.format("%d %d %d", year, monthOfYear, dayOfMonth));
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM d");
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        DateFormat dateFormatMonth = new SimpleDateFormat("MMMM");
        DateFormat dateFormatDay = new SimpleDateFormat("d");

        try {
            Date date = dateFormat.parse(String.format("%d %d %d", year, monthOfYear + 1, dayOfMonth));
            TextView yr = (TextView) findViewById(R.id.textView3);
            TextView month = (TextView) findViewById(R.id.textView2);
            TextView day = (TextView) findViewById(R.id.textView);

            yr.setText(dateFormatYear.format(date));
            month.setText(dateFormatMonth.format(date));
            day.setText(dateFormatDay.format(date));

        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }

        TimePickerFragment tpf = new TimePickerFragment();
        tpf.show(getSupportFragmentManager(), "Time Picker");

    }

    @Override
    public void onGiveTime(int hourOfDay, int minute) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy MM d H m");
        DateFormat dateFormatMs = new SimpleDateFormat("S");
        try {
            Pickup_date_ms = dateFormatMs.format(dateFormat.parse(pickup_date_ms + " " + hourOfDay + " " + minute));
            //Log.d(logTag, Pickup_date_ms);
        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        final TextView numberText = (TextView) findViewById(R.id.textView4);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.number_picker, null);
        final NumberPicker picker = (NumberPicker) npView.findViewById(R.id.numberPicker);
        picker.setMinValue(1);
        picker.setMaxValue(99);
        picker.setWrapSelectorWheel(true);

        builder.setTitle(getResources().getString(R.string.number_of_trucks))
                .setView(npView)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                numberText.setText(String.format("%d", picker.getValue()));
                            }
                        }
                );
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        buildGoogleApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LatLngBounds bounds = new LatLngBounds(new LatLng(-85, 180), new LatLng(85, -180));
                AutocompleteFilter filter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                        .build();
                PendingResult<AutocompletePredictionBuffer> result =
                        Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, s.toString(), bounds, filter);
                result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                    @Override
                    public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                        List<String> placesList = new ArrayList<>();
                        for (int i = 0; i < autocompletePredictions.getCount(); i++) {
                            placesList.add(autocompletePredictions.get(i).getFullText(null).toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(GetTruckActivity.this, android.R.layout.simple_dropdown_item_1line, placesList);
                            //Log.d(TAGlog,phonesCollection.toString());

                            if (fromAutoTextView.hasFocus()) fromAutoTextView.setAdapter(adapter);
                            else toAutoTextView.setAdapter(adapter);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void exchangeOnClick(View v) {
        String from = fromAutoTextView.getText().toString();
        String to = toAutoTextView.getText().toString();
        fromAutoTextView.setText(to);
        toAutoTextView.setText(from);
    }

    public void onTruckTypeClick(View v) {
        TruckTypeDialogFragment ttdf = new TruckTypeDialogFragment();
        ttdf.show(getSupportFragmentManager(), "TruckType");
    }

    public void onPaymentTypeClick(View v) {
        PaymentModeDialogFragment pmdf = new PaymentModeDialogFragment();
        pmdf.show(getSupportFragmentManager(), "PaymentMode");
    }

    @Override
    public void onPaymentTypeSelected(final Spinner spinner1, final Spinner spinner2, Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment prev = getSupportFragmentManager().findFragmentByTag("PaymentMode");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
                ((TextView) findViewById(R.id.textViewPayment1)).setText(getResources().getStringArray(R.array.payment_type_1)[spinner1.getSelectedItemPosition()]);
                ((TextView) findViewById(R.id.textViewPayment2)).setText(getResources().getStringArray(R.array.payment_type_2)[spinner2.getSelectedItemPosition()]);
            }
        });
    }

    @Override
    public void onTruckTypeSelected(final Spinner spinner1, final Spinner spinner2, Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment prev = getSupportFragmentManager().findFragmentByTag("TruckType");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
                ((TextView) findViewById(R.id.textViewTruckType1)).setText(getResources().getStringArray(R.array.truck_type_1)[spinner1.getSelectedItemPosition()]);
                ((TextView) findViewById(R.id.textViewTruckType2)).setText(getResources().getStringArray(R.array.truck_type_2)[spinner2.getSelectedItemPosition()]);
            }
        });
    }

    public void validateData() {

        EditText material_type = (EditText) findViewById(R.id.material_type);
        EditText material_weight = (EditText) findViewById(R.id.material_weight);
        EditText expected_price = (EditText) findViewById(R.id.expected_price);

        if (fromAutoTextView.getText().length() <= 0) {
            fromAutoTextView.setError(getResources().getString(R.string.text_error));
        } else if (toAutoTextView.getText().length() <= 0) {
            toAutoTextView.setError(getResources().getString(R.string.text_error));
        } else if (fromAutoTextView.getText().equals(toAutoTextView.getText())) {
            toAutoTextView.setError(getResources().getString(R.string.src_dest_same_error));
        } else if (material_type.getText().length() == 0) {
            material_type.setError(getResources().getString(R.string.text_error));
        } else if (material_weight.getText().length() <= 0) {
            material_weight.setError(getResources().getString(R.string.text_error));
        } else if (Integer.parseInt(material_weight.getText().toString()) <= 0) {
            material_weight.setError(getResources().getString(R.string.greater_than_zero_error));
        } else if (expected_price.getText().length() <= 0) {
            expected_price.setError(getResources().getString(R.string.greater_than_zero_error));
        } else if (Integer.parseInt(expected_price.getText().toString()) <= 0) {
            expected_price.setError(getResources().getString(R.string.greater_than_zero_error));
        } else {
            (new uploadServer()).execute();
        }
    }

    public String[] split_address(String address) {
        String array[] = address.split(",");
        String area = "";
        for (int i = 0; i < array.length - 2; i++) {
            area += array[i];
        }
        String addr[] = {area, array[array.length - 2], array[array.length - 1]};
        return addr;
    }

    public JSONObject makeJson() {
        JSONObject container = new JSONObject();
        try {
            container.put("order_name", "fff");
            String srcArray[] = split_address(fromAutoTextView.getText().toString());
            container.put("source", (new JSONObject()).put("area", srcArray[0]).put("city", srcArray[1]).put("state", srcArray[2]));
            String destArray[] = split_address(toAutoTextView.getText().toString());
            container.put("destination", (new JSONObject()).put("area", destArray[0]).put("city", destArray[1]).put("state", destArray[2]));
            container.put("pick_up_date", Long.parseLong(Pickup_date_ms));
            container.put("load_visibility", "all");
            container.put("expected_price", Long.parseLong(((EditText) findViewById(R.id.expected_price)).getText().toString()));
            container.put("consignment_type", "Part Truck Load");
            container.put("consignment_weight", Long.parseLong(((EditText) findViewById(R.id.material_weight)).getText().toString()));
            container.put("num_trucks", ((TextView) findViewById(R.id.textView4)).getText().toString());
            container.put("consignment_dimensions", (new JSONObject()).put("dimensions", (new JSONArray()).put(0, 44).put(1, 44).put(2, 44)));
            container.put("material_type", ((EditText) findViewById(R.id.material_type)).getText().toString());
            container.put("preferred_truck_type", (new JSONObject()).put("truck_tonnage", "0-2 MT").put("truck_length", "0-9 feet").put("truck_type", ((TextView) findViewById(R.id.textViewTruckType1)).getText().toString()));
            container.put("payment_type", "Advance");
            container.put("payment_mode", ((TextView) findViewById(R.id.textViewPayment2)).getText().toString());
            container.put("payment_terms", ((TextView) findViewById(R.id.textViewPayment1)).getText().toString());
            container.put("loading_amount", 44);
            container.put("unloading_amount", 44);
        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }
        return container;
    }

    public class uploadServer extends AsyncTask<Void, Void, String> {

        SharedPreferences sharedPref = getSharedPreferences("cookie", Context.MODE_PRIVATE);

        @Override
        protected String doInBackground(Void... params) {

            StringBuilder buffer = null;
            try {
                HttpsURLConnection conn = (HttpsURLConnection) (new URL("https://www.shipoya.com/auction_resources/api/loads")).openConnection();

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Cookie", TextUtils.join(";", sharedPref.getStringSet("cookies", null)));
                conn.connect();

                JSONObject container = makeJson();
                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(container.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                Log.d(logTag, "Post Truck Data " + String.format("code %d", conn.getResponseCode()));

                buffer = new StringBuilder();
                InputStream is = conn.getInputStream();
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
                JSONObject object = new JSONObject(s);
                Toast.makeText(GetTruckActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                GetTruckActivity.this.finish();
            }
            catch (Throwable t) {
                Log.d(logTag, t.getMessage());
            }
        }
    }
}