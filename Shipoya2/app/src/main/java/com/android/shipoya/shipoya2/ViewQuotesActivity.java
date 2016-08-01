package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewQuotesActivity extends AppCompatActivity {

    private static final String TAGlog = "logTag";
    CustomSwipeRefreshLayout refreshLayout;
    int position = 0;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quotes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        refreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                position = pager.getCurrentItem();
                (new MakeRequest(
                        ViewQuotesActivity.this,
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
                                        onPostExecute2();
                                    } else {
                                        Toast.makeText(ViewQuotesActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Throwable t) {
                                    Log.d(TAGlog, t.getMessage());
                                }
                            }
                        }
                )).execute();

            }
        });
        onPostExecute2();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (StaticData.ORDER_CHANGED) {
            position = pager.getCurrentItem();
            StaticData.ORDER_CHANGED=false;
            onPostExecute2();
        }
    }

    private void onPostExecute2() {
        List<OrderParent> parents = new ArrayList<>();
        List<OrderParent> parentsNeg = new ArrayList<>();
        List<OrderParent> parentsconf = new ArrayList<>();

        try {
            JSONObject object = StaticData.ORDER_DATA;
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                //Log.d(TAGlog, ""+i);
                JSONObject obj = array.getJSONObject(i);
                if (!obj.getJSONObject("transit").getBoolean("show") && !obj.getJSONObject("closure").getBoolean("show")) {
                    OrderParent op = new OrderParent(
                            (obj.getJSONObject("source").getString("city").length() > 2) ? obj.getJSONObject("source").getString("city").trim().substring(0, 3).toUpperCase() : obj.getJSONObject("source").getString("city").trim().toUpperCase(),
                            (obj.getJSONObject("destination").getString("city").length() > 2) ? obj.getJSONObject("destination").getString("city").trim().substring(0, 3).toUpperCase() : obj.getJSONObject("destination").getString("city").trim().toUpperCase(),
                            obj.getString("order_id"),
                            obj.getLong("pick_up_date"));
                    if (obj.getJSONObject("new_request").getBoolean("show")) {
                        parents.add(op);
                    } else if (obj.getJSONObject("quotation").getBoolean("show")) {
                        parentsNeg.add(op);
                    } else if (obj.getJSONObject("confirmation").getBoolean("show")) {
                        parentsconf.add(op);
                    }
                }
            }
        } catch (Throwable t) {
            Log.d(TAGlog, "quotes " + t.getMessage());
        }
        Collections.sort(parents);
        Collections.sort(parentsNeg);
        Collections.sort(parentsconf);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new QuotesPagerAdapter(ViewQuotesActivity.this, parents, parentsNeg, parentsconf));
        pager.setCurrentItem(position);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}

