package com.android.shipoya.shipoya2;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    Toolbar bar;
    SimpleDateFormat dateFinal = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);
    private static final String TAGlog = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            search(intent.getStringExtra(SearchManager.QUERY).toLowerCase());
        } else {
            this.finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void search(String query) {

        DatabaseHelper helper = new DatabaseHelper(this);
        helper.open();
        getSupportActionBar().setTitle("Search for '" + query + "'");

        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this,
                        RecentSearchProviderShipper.AUTHORITY,
                        RecentSearchProviderShipper.MODE);
        suggestions.saveRecentQuery(query, null);

        List<OrderParent> orders = new ArrayList<>();
        List<OrderParent> quotes = new ArrayList<>();

        SearchPagerAdapter searchPagerAdapter;

        try {
            JSONObject main = StaticData.ORDER_DATA;
            JSONArray data = main.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);

                //adding data to all tab
                if (!obj.getJSONObject("transit").getBoolean("show") && !obj.getJSONObject("closure").getBoolean("show") && match(obj, query)) {
                    OrderParent op = new OrderParent(
                            (obj.getJSONObject("source").getString("city").length() > 2) ? obj.getJSONObject("source").getString("city").substring(0, 3).toUpperCase() : obj.getJSONObject("source").getString("city").toUpperCase(),
                            (obj.getJSONObject("destination").getString("city").length() > 2) ? obj.getJSONObject("destination").getString("city").substring(0, 3).toUpperCase() : obj.getJSONObject("destination").getString("city".toUpperCase()),
                            obj.getString("order_id"),
                            obj.getLong("scheduled_on")
                    );
                    quotes.add(op);
                }
                if ((obj.getJSONObject("transit").getBoolean("show") || obj.getJSONObject("closure").getBoolean("show")) && match(obj, query)) {
                    OrderParent op = new OrderParent(
                            obj.getJSONObject("source").getString("city").substring(0, 3).toUpperCase(),
                            obj.getJSONObject("destination").getString("city").substring(0, 3).toUpperCase(),
                            obj.getString("order_id"),
                            Long.parseLong(obj.getString("scheduled_on")));
                    orders.add(op);
                }
            }
            Collections.sort(quotes);
            Collections.sort(orders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchPagerAdapter = new SearchPagerAdapter(SearchActivity.this, quotes, orders);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(searchPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }

    private boolean match(JSONObject obj, String query) {
        try {
            if ((obj.getJSONObject("source").getString("city") + ", " + obj.getJSONObject("source").getString("state")).toLowerCase().contains(query) ||
                    (obj.getJSONObject("destination").getString("city") + ", " + obj.getJSONObject("destination").getString("state")).toLowerCase().contains(query) ||
                    obj.getString("order_id").toLowerCase().contains(query) ||
                    obj.getString("shipper_name").toLowerCase().contains(query) ||
                    obj.getString("order_name").toLowerCase().contains(query) ||
                    obj.getString("payment_terms").toLowerCase().contains(query) ||
                    dateFinal.format(new Date(obj.getLong("scheduled_on") * 1000)).toLowerCase().contains(query))
                return true;
        } catch (Exception e) {
            Log.d("TAG EX", e.getMessage());
        }
        return false;
    }
}
