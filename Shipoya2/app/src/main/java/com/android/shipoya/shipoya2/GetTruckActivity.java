package com.android.shipoya.shipoya2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GetTruckActivity extends AppCompatActivity implements
        DatePickerFragment.giveDate,
        TimePickerFragment.giveTime,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        TruckTypeDialogFragment.onInteractionListener,
        PaymentModeDialogFragment.onInteractionListener {

    private static final String logTag = "logTag";
    EditText fromAutoTextView;
    EditText toAutoTextView;

    private boolean valid = false;
    private int year, month, day;
    private String Pickup_date_ms = String.format("%d", new Date(System.currentTimeMillis()).getTime());

    DateFormat dateFormatYear = new SimpleDateFormat("yyyy", Locale.US);
    DateFormat dateFormatMonth = new SimpleDateFormat("MMMM", Locale.US);
    DateFormat dateFormatDay = new SimpleDateFormat("d", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_truck);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //buildGoogleApiClient();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.crossNumber_container);
        linearLayout.setOnClickListener(this);

        fromAutoTextView = (EditText) findViewById(R.id.FromAutocomplete);
        toAutoTextView = (EditText) findViewById(R.id.ToAutocomplete);

        fromAutoTextView.setOnClickListener(this);
        toAutoTextView.setOnClickListener(this);


        ((TextView) findViewById(R.id.textViewPayment1)).setText(getResources().getStringArray(R.array.payment_type_1)[0]);
        ((TextView) findViewById(R.id.textViewPayment2)).setText(getResources().getStringArray(R.array.payment_type_2)[0]);

        ((TextView) findViewById(R.id.textViewTruckType1)).setText(getResources().getStringArray(R.array.truck_type_1)[0]);
        ((TextView) findViewById(R.id.textViewTruckType2)).setText(getResources().getStringArray(R.array.truck_type_2)[0]);

        ((TextView) findViewById(R.id.textViewYear)).setText(dateFormatYear.format(new Date(System.currentTimeMillis())));
        ((TextView) findViewById(R.id.textViewMonth)).setText(dateFormatMonth.format(new Date(System.currentTimeMillis())));
        ((TextView) findViewById(R.id.textViewDay)).setText(dateFormatDay.format(new Date(System.currentTimeMillis())));

        Spinner spinner2 = (Spinner) findViewById(R.id.material_type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.material_type, R.layout.spinner_textview);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        (findViewById(R.id.button_get_truck)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = true;
                validateData();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        fromAutoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                toAutoTextView.requestFocus();
//            }
//        });
//        toAutoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            }
//        });
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy MM d");

        this.year = year;
        this.month = monthOfYear + 1;
        this.day = dayOfMonth;

        try {
            Date date = dateFormat.parse(String.format("%d %d %d", year, monthOfYear + 1, dayOfMonth));
            TextView yr = (TextView) findViewById(R.id.textViewYear);
            TextView month = (TextView) findViewById(R.id.textViewMonth);
            TextView day = (TextView) findViewById(R.id.textViewDay);

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

        DateFormat dateFormat1 = new SimpleDateFormat("d MM yyyy HH mm");
        // Log.d(logTag,""+day+" "+month+" "+year+" "+hourOfDay+" "+minute);
        try {
            Pickup_date_ms = String.format("%d", (dateFormat1.parse("" + day + " " + month + " " + year + " " + hourOfDay + " " + minute).getTime()));
            Log.d(logTag, Pickup_date_ms);
        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.crossNumber_container:
                selectNoTrucks();
                break;
            case R.id.FromAutocomplete:
                findPlace(v);
                break;
            case R.id.ToAutocomplete:
                findPlace(v);
                break;
        }
    }

    private void selectNoTrucks() {
        final TextView numberText = (TextView) findViewById(R.id.textView4);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.number_picker, null);
        final NumberPicker picker = (NumberPicker) npView.findViewById(R.id.numberPicker);
        picker.setMinValue(1);
        picker.setMaxValue(99);
        picker.setWrapSelectorWheel(true);

        builder.setTitle(Html.fromHtml("<font color='#F06423'>" + getResources().getString(R.string.number_of_trucks) + "</font>"))
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

    public void findPlace(View view) {
        try {
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(filter)
                    .build(this);
            if (view.getId() == R.id.FromAutocomplete) {
                startActivityForResult(intent, 1);
            } else {
                startActivityForResult(intent, 2);
            }
        } catch (Throwable e) {
            Log.d(logTag, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
//            Log.d(logTag, "Name: " + place.getName());
//            Log.d(logTag, "Address: " + place.getAddress());

            if (requestCode == 1) {
                fromAutoTextView.setText(place.getName() + ", " + place.getAddress());
                toAutoTextView.requestFocus();
            } else {
                toAutoTextView.setText(place.getName() + ", " + place.getAddress());
            }

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            Log.d(logTag, status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //buildGoogleApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
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

        EditText material_weight = (EditText) findViewById(R.id.material_weight);
        EditText expected_price = (EditText) findViewById(R.id.expected_price);

        if (fromAutoTextView.getText().toString().trim().length() <= 0) {
            fromAutoTextView.setError(getResources().getString(R.string.text_error));
        } else if (toAutoTextView.getText().toString().trim().length() <= 0) {
            toAutoTextView.setError(getResources().getString(R.string.text_error));
        } else if (fromAutoTextView.getText().equals(toAutoTextView.getText())) {
            toAutoTextView.setError(getResources().getString(R.string.src_dest_same_error));
        } else if (material_weight.getText().length() <= 0) {
            material_weight.setError(getResources().getString(R.string.text_error));
        } else if (Integer.parseInt(material_weight.getText().toString()) <= 0) {
            material_weight.setError(getResources().getString(R.string.greater_than_zero_error));
        } else if (expected_price.getText().toString().trim().length() <= 0) {
            expected_price.setError(getResources().getString(R.string.greater_than_zero_error));
        } else if (Integer.parseInt(expected_price.getText().toString()) <= 0 || Integer.parseInt(expected_price.getText().toString().trim()) > 9900000) {
            expected_price.setError(getResources().getString(R.string.between_zero_99));
        } else { makeJson(); }
    }

    public String[] split_address(String address) {
        ArrayList<String> array = new ArrayList<>(Arrays.asList(address.split(",")));
        if (array.get(array.size() - 1).trim().toLowerCase().equals("india")) {
            array.remove(array.size() - 1);
        }
        if (array.size() < 2) {
            Toast.makeText(GetTruckActivity.this, "Source and Destination should be Area, City, State format", Toast.LENGTH_LONG).show();
            valid = false;
        }
        String area = "";
        for (int i = 0; i < array.size() - 2; i++) {
            area += array.get(i);
        }
        String addr[] = {area, array.get(array.size() - 2), array.get(array.size() - 1)};
        return addr;
    }

    private void makeJson() {
        try {
            JSONObject container = new JSONObject();
            container.put("order_name", "App Order");
            String srcArray[] = split_address(fromAutoTextView.getText().toString().trim());
            if (valid) {
                container.put("source", (new JSONObject()).put("area", srcArray[0]).put("city", srcArray[1]).put("state", srcArray[2]));
                String destArray[] = split_address(toAutoTextView.getText().toString().trim());
                if (valid) {
                    container.put("destination", (new JSONObject()).put("area", destArray[0]).put("city", destArray[1]).put("state", destArray[2]));
                    container.put("pick_up_date", Long.parseLong(Pickup_date_ms));
                    container.put("load_visibility", "all");
                    container.put("expected_price", Long.parseLong(((EditText) findViewById(R.id.expected_price)).getText().toString()));
                    container.put("consignment_type", "Part Truck Load");
                    container.put("consignment_weight", Long.parseLong(((EditText) findViewById(R.id.material_weight)).getText().toString()));
                    container.put("num_trucks", ((TextView) findViewById(R.id.textView4)).getText().toString());
                    container.put("consignment_dimensions", (new JSONObject()).put("dimensions", (new JSONArray()).put(0, 44).put(1, 44).put(2, 44)));
                    container.put("material_type", ((Spinner) findViewById(R.id.material_type)).getSelectedItem());
                    container.put("preferred_truck_type", (new JSONObject()).put("truck_tonnage", "0-2 MT").put("truck_length", "0-9 feet").put("truck_type", ((TextView) findViewById(R.id.textViewTruckType1)).getText().toString()));
                    container.put("payment_type", "Advance");
                    container.put("payment_mode", ((TextView) findViewById(R.id.textViewPayment2)).getText().toString());
                    container.put("payment_terms", ((TextView) findViewById(R.id.textViewPayment1)).getText().toString());
                    container.put("loading_amount", 44);
                    container.put("unloading_amount", 44);

                    Log.d(logTag, container.toString());

                    (new MakeRequest(
                            this,
                            "/auction_resources/api/loads",
                            "POST",
                            true,
                            container,
                            new Interface() {
                                @Override
                                public void onPostExecute(String response) {
                                    if (response != null && response.length() > 0) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            Toast.makeText(GetTruckActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                            LoadNewData();
                                        } catch (Throwable t) {
                                            Log.d(logTag, t.getMessage());
                                        }
                                    }
                                }
                            }
                    )).execute();
                }
            }
        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }
    }

    private void LoadNewData() {
        (new MakeRequest(
                this,
                "/auction_resources/api/loads/shipper_loads",
                "GET",
                true,
                null,
                new Interface() {
                    @Override
                    public void onPostExecute(String response) {
                        try {
                            if (response != null && response.length() > 0) {
                                StaticData.ORDER_DATA = new JSONObject(response);
                                GetTruckActivity.this.finish();
                            }
                        } catch (Throwable t) {
                            Log.d(logTag, t.getMessage());
                        }
                    }
                }
        )).execute();
    }
}