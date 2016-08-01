package com.android.shipoya.shipoya2;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewOrdersActivity extends AppCompatActivity {

    private static final String TAGlog = "logTag";
    CustomSwipeRefreshLayout refreshLayout;
    int position = 0;
    private ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                position = pager.getCurrentItem();
                (new MakeRequest(
                        ViewOrdersActivity.this,
                        "/auction_resources/api/loads/shipper_loads",
                        "GET",
                        false,
                        null,
                        new Interface() {
                            @Override
                            public void onPostExecute(String response) {
                                try {
                                    if (response != null && response.length()>0) {
                                        StaticData.ORDER_DATA = new JSONObject(response);
                                        onPostExecute2();
                                    }
                                    else{
                                        Toast.makeText(ViewOrdersActivity.this,getResources().getString(R.string.check_conn) , Toast.LENGTH_LONG).show();
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

    protected void onPostExecute2() {
        //Log.d(TAGlog, String.format("%d", buffer.length()));
        List<OrderParent> parents = new ArrayList<>();
        List<OrderParent> parents_trans = new ArrayList<>();
        List<OrderParent> parents_clos = new ArrayList<>();

        try {
            JSONObject object = StaticData.ORDER_DATA;
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getJSONObject("transit").getBoolean("show") || obj.getJSONObject("closure").getBoolean("show")) {
                    //Log.d(TAGlog,String.format("%d",Long.parseLong(obj.getString("scheduled_on"))));
                    OrderParent op = new OrderParent(
                            obj.getJSONObject("source").getString("city").trim().substring(0, 3).toUpperCase(),
                            obj.getJSONObject("destination").getString("city").trim().substring(0, 3).toUpperCase(),
                            obj.getString("order_id"),
                            Long.parseLong(obj.getString("pick_up_date")));
                    parents.add(op);
                    if (obj.getJSONObject("transit").getBoolean("show")) {
                        parents_trans.add(op);
                    } else if (obj.getJSONObject("closure").getBoolean("show")) {
                        parents_clos.add(op);
                    }
                }

            }
        } catch (Throwable t) {
            Log.d(TAGlog, t.getMessage());
        }

        Collections.sort(parents);
        Collections.sort(parents_trans);
        Collections.sort(parents_clos);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new OrdersPagerAdapter(ViewOrdersActivity.this, parents, parents_trans, parents_clos));
        pager.setCurrentItem(position);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
}

