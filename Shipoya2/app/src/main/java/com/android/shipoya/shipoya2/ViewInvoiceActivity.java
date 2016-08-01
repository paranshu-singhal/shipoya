package com.android.shipoya.shipoya2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewInvoiceActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabLayout;
    private CustomSwipeRefreshLayout refreshLayout;
    int position = 0;
    private static String logTag = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quotes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setData();

        final SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        refreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    position=pager.getCurrentItem();
                    (new MakeRequest(
                            ViewInvoiceActivity.this,
                            "/auction_resources/api/loads/invoice_list/entity/shipper/identifier/" + URLEncoder.encode(preferences.getString("entity_id", null), "UTF-8"),
                            "GET",
                            false,
                            null,
                            new Interface() {
                                @Override
                                public void onPostExecute(String response) {
                                    try {
                                        if (response != null && response.length() > 0) {
                                            StaticData.ORDER_DATA = new JSONObject(response);
                                            setData();
                                        } else {
                                            Toast.makeText(ViewInvoiceActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Throwable t) {
                                        Log.d(logTag, "SplashScreen" + t.getMessage());
                                    }
                                }
                            }
                    )).execute();

                } catch (Throwable t) {
                    Log.d(logTag, t.getMessage());
                }
            }
        });

    }

    public void setData() {
        try {
            List<InvoiceParent> allInvoice = new ArrayList<>();
            List<InvoiceParent> pendingInvoices = new ArrayList<>();
            List<InvoiceParent> paidInvoices = new ArrayList<>();

            JSONObject main = StaticData.INVOICE_DATA;
            JSONArray data = main.getJSONArray("data");
            JSONObject obj;

            for (int i = 0; i < data.length(); i++) {
                obj = data.getJSONObject(i);
                List<InvoiceChild> childList = new ArrayList<>();

                NewOrderHolder holder = new NewOrderHolder(
                        obj.getJSONObject("source").getString("city") + ", "+obj.getJSONObject("source").getString("state"),
                        "",
                        obj.getJSONObject("destination").getString("city") + ", "+obj.getJSONObject("source").getString("state"),
                        obj.getJSONObject("preferred_truck_type").getString("truck_type"),
                        obj.getString("load_type"),
                        obj.getString("payment_terms"),
                        "",
                        obj.getString("num_trucks"),
                        false);

                childList.add(new InvoiceChild(
                        obj.getJSONObject("preferred_truck_type").getString("truck_type"),
                        obj.getString("num_trucks"),
                        obj.getString("invoice_amount"), holder, obj.getString("invoice_url")));

                String sourceT, destinationT;
                if (obj.getJSONObject("source").getString("city").trim().length() < 3)
                    sourceT = obj.getJSONObject("source").getString("city");
                else
                    sourceT = obj.getJSONObject("source").getString("city").trim().substring(0, 3).toUpperCase();
                if (obj.getJSONObject("destination").getString("city").trim().length() < 3)
                    destinationT = obj.getJSONObject("destination").getString("city");
                else
                    destinationT = obj.getJSONObject("destination").getString("city").trim().substring(0, 3).toUpperCase();

                InvoiceParent ip = new InvoiceParent(sourceT, destinationT, obj.getString("invoice_status"),
                        obj.getString("order_id"), obj.getString("invoice_amount"), obj.getLong("pick_up_date"), childList);

                allInvoice.add(ip);
                if(obj.getString("invoice_status").equals("pending")) pendingInvoices.add(ip);
                else if(obj.getString("invoice_status").equals("paid")) paidInvoices.add(ip);
            }

            Collections.sort(allInvoice);
            Collections.sort(pendingInvoices);
            Collections.sort(paidInvoices);

            pager.setAdapter(new InvoicesPagerAdapter(this, allInvoice, pendingInvoices, paidInvoices));
            tabLayout.setupWithViewPager(pager);
            pager.setCurrentItem(position);
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        } catch (Exception e) {
            Log.d("Invoice", e.toString());
        }
    }
}
