package com.android.shipoya.shipoya2;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    private static final String TAGlog = "logTag";
    SimpleDateFormat dateFinal = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        overridePendingTransition(R.anim.splash_enter, R.anim.splash_exit);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        Log.d(TAGlog, preferences.getString("entity_id", null));

        if (StaticData.NETWORK_AVAILABLE) {

            try {
                final MakeRequest makeRequest1 = (new MakeRequest(
                        this,
                        "/auction_resources/api/loads/invoice_list/entity/shipper/identifier/" + URLEncoder.encode(preferences.getString("entity_id", null), "UTF-8"),
                        "GET",
                        false,
                        null,
                        new Interface() {
                            @Override
                            public void onPostExecute(String response) {
                                if (response != null && response.length() > 0) {
                                    try {
                                        StaticData.INVOICE_DATA = new JSONObject(response);
                                        addSuggestion.start();
                                        Intent intent = new Intent(SplashScreen.this, HomepageActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } catch (Throwable t) {
                                        Log.d(TAGlog, "SplashScreen" + t.getMessage());
                                        goBack();
                                    }
                                } else { goBack(); }
                            }
                        }
                ));

                (new MakeRequest(
                        this,
                        "/auction_resources/api/loads/shipper_loads",
                        "GET",
                        false,
                        null,
                        new Interface() {
                            @Override
                            public void onPostExecute(String response) {
                                try {
                                    if (response != null && response.length() > 0) {
                                        StaticData.ORDER_DATA = new JSONObject(response);
                                        makeRequest1.execute();
                                    } else {
                                        goBack();
                                    }
                                } catch (Throwable t) {
                                    Log.d(TAGlog, t.getMessage());
                                    goBack();
                                }
                            }
                        }
                )).execute();

            }
            catch (Throwable t){
                Log.d(TAGlog, t.getMessage());
                goBack();
            }
            }else{
                StaticData.showNetworkError(this);
                goBack();
            }

    }

    Thread addSuggestion = new Thread(new Runnable() {
        @Override
        public void run() {
            DatabaseHelper helper = new DatabaseHelper(SplashScreen.this);
            helper.open();
            helper.deleteTable(helper.TABLE_SUGGESTIONS);
            ContentValues suggestion = new ContentValues();
            try {
                JSONArray array = (StaticData.ORDER_DATA).getJSONArray("data");
                JSONObject obj;
                for (int i = 0; i < array.length(); i++) {
                    obj = array.getJSONObject(i);
                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, obj.getString("order_id"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, obj.getString("order_id"));
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, obj.getString("order_name"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, obj.getString("order_name"));
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, obj.getString("shipper_name"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, obj.getString("shipper_name"));
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, obj.getString("payment_terms"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, obj.getString("payment_terms"));
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    String area = (obj.getJSONObject("source").getString("city") + ", " + obj.getJSONObject("source").getString("state"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, area);
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, area);
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    area = (obj.getJSONObject("destination").getString("city") + ", " + obj.getJSONObject("destination").getString("state"));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, area);
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, area);
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);

                    suggestion.put(SearchManager.SUGGEST_COLUMN_TEXT_1, dateFinal.format(new Date(obj.getLong("scheduled_on") * 1000)));
                    suggestion.put(SearchManager.SUGGEST_COLUMN_QUERY, dateFinal.format(new Date(obj.getLong("scheduled_on") * 1000)));
                    suggestion.put(SearchManager.EXTRA_DATA_KEY, Calendar.getInstance().getTimeInMillis());
                    helper.insertData(helper.TABLE_SUGGESTIONS, suggestion);
                }
                helper.close();

            } catch (Throwable e) {
                Log.d(TAGlog, e.getMessage());
            }

        }
    });

    private void goBack() {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
