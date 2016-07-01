package com.android.shipoya.shipoya2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class ViewOrdersActivity extends AppCompatActivity {

    private static final String TAGlog = "logTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        (new LoadData()).execute();
    }

    public class LoadData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String s = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(openFileInput("ordersCache"));
                s = (String) ois.readObject();
            } catch (Throwable t) {
                Log.d(TAGlog, t.getMessage());
            }

            return s;
        }

        @Override
        protected void onPostExecute(String buffer) {
            Log.d(TAGlog, String.format("%d", buffer.length()));
            List<OrderParent> parents = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(buffer);
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj.getJSONObject("transit").getBoolean("show")) {
                        OrderParent op = new OrderParent(
                                obj.getJSONObject("source").getString("city").substring(0, 3).toUpperCase(),
                                obj.getJSONObject("destination").getString("city").substring(0, 3).toUpperCase(),
                                obj.getString("order_id"),
                                obj.getLong("scheduled_on")
                        );
                        parents.add(op);
                    }
                }
            } catch (Throwable t) {
                Log.d(TAGlog, t.getMessage());
            }
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(new OrdersPagerAdapter(ViewOrdersActivity.this, parents));
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(pager);
        }
    }
}
