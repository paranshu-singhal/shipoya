package com.android.shipoya.shipoya2;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetTruckActivity extends AppCompatActivity implements
        DatePickerFragment.giveDate,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener ,
        TextWatcher{

    private static final String logTag = "logTag";
    private GoogleApiClient mGoogleApiClient;
    AutoCompleteTextView fromAutoTextView;
    AutoCompleteTextView toAutoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_truck);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        buildGoogleApiClient();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.crossNumber_container);
        linearLayout.setOnClickListener(this);

        fromAutoTextView = (AutoCompleteTextView)findViewById(R.id.FromAutocomplete);
        toAutoTextView = (AutoCompleteTextView)findViewById(R.id.ToAutocomplete);

        fromAutoTextView.addTextChangedListener(this);
        toAutoTextView.addTextChangedListener(this);
    }
    public void scheduleOnClick(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_get_truck, menu);
        return true;
    }

    @Override
    public void onGiveDate(int year, int monthOfYear, int dayOfMonth) {
        Log.d(logTag, String.format("%d %d %d", year, monthOfYear, dayOfMonth));
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM d");
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        DateFormat dateFormatMonth = new SimpleDateFormat("MMMM");
        DateFormat dateFormatDay = new SimpleDateFormat("d");
        try {
            Date date = dateFormat.parse(String.format("%d %d %d", year, monthOfYear+1, dayOfMonth));
            TextView yr = (TextView)findViewById(R.id.textView3);
            TextView month = (TextView)findViewById(R.id.textView2);
            TextView day = (TextView)findViewById(R.id.textView);

            yr.setText(dateFormatYear.format(date));
            month.setText(dateFormatMonth.format(date));
            day.setText(dateFormatDay.format(date));
        }
        catch (Throwable t){
            Log.d(logTag, t.getMessage());
        }

        TimePickerFragment tpf = new TimePickerFragment();
        tpf.show(getSupportFragmentManager(), "Time Picker");

    }
    @Override
    public void onClick(View v) {
        final TextView numberText = (TextView)findViewById(R.id.textView4);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.number_picker, null);
        final NumberPicker picker = (NumberPicker)npView.findViewById(R.id.numberPicker);
        picker.setMinValue(1);
        picker.setMaxValue(99);
        picker.setWrapSelectorWheel(true);

        builder.setTitle(getResources().getString(R.string.number_of_trucks))
                .setView(npView)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                numberText.setText(String.format("%d",picker.getValue()));
                            }
                        }
                );
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected synchronized void buildGoogleApiClient(){
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
                LatLngBounds bounds  = new LatLngBounds(new LatLng(-85,180), new LatLng(85,-180));
                AutocompleteFilter filter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                        .build();
                PendingResult<AutocompletePredictionBuffer> result =
                        Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, s.toString(), bounds, filter);
                result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                    @Override
                    public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                        List<String> placesList = new ArrayList<>();
                        for(int i=0;i<autocompletePredictions.getCount();i++){
                            placesList.add(autocompletePredictions.get(i).getFullText(null).toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(GetTruckActivity.this, android.R.layout.simple_dropdown_item_1line, placesList);
                            //Log.d(TAGlog,phonesCollection.toString());

                            if(fromAutoTextView.hasFocus()) fromAutoTextView.setAdapter(adapter);
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
    public void exchangeOnClick(View v){
        String from = fromAutoTextView.getText().toString();
        String to = toAutoTextView.getText().toString();
        fromAutoTextView.setText(to);
        toAutoTextView.setText(from);
    }

    public void onTruckTypeClick(View v){
        TruckTypeDialogFragment ttdf = new TruckTypeDialogFragment();
        ttdf.show(getSupportFragmentManager(), "TruckType");
    }

    public void onPaymentTypeClick(View v){
        PaymentModeDialogFragment pmdf = new PaymentModeDialogFragment();
        pmdf.show(getSupportFragmentManager(), "PaymentMode");
    }
}