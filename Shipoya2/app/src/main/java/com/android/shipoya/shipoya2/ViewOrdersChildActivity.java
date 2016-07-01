package com.android.shipoya.shipoya2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewOrdersChildActivity extends AppCompatActivity implements CardView.OnClickListener {

    private static final String TAGlog = "logTag";
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_child);

        TextView order_id_view = (TextView) findViewById(R.id.orderId);
        CardView parentView = (CardView) findViewById(R.id.parentView);
        ImageView indicator = (ImageView) findViewById(R.id.expandIndicator);
        indicator.setImageResource(R.drawable.ic_expand_less_black_24dp);

        order_id = getIntent().getExtras().getString("order_id");
        order_id_view.setText(order_id);
        parentView.setOnClickListener(this);

        (new LoadData()).execute();

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    public class LoadData extends AsyncTask<Void, Void, String> {

        List<ViewOrdersChildHolderClass.ParentHolderClass> parentHolderClass = new ArrayList<>();
        List<ViewOrdersChildHolderClass.ChildHolderClass> childHolderClasses = new ArrayList<>();

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
            TextView src = (TextView) findViewById(R.id.from);
            TextView dest = (TextView) findViewById(R.id.to);
            TextView yr = (TextView) findViewById(R.id.year);
            TextView date = (TextView) findViewById(R.id.date);
            TextView source = (TextView) findViewById(R.id.textView35);
            TextView destination = (TextView) findViewById(R.id.textView36);


            DateFormat dateFinal1 = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
            DateFormat dateFinal2 = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            //Log.d(TAGlog, String.format("%d", buffer.length()));
            try {
                JSONObject object = new JSONObject(buffer);
                JSONArray array = object.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj.getJSONObject("transit").getBoolean("show") && obj.getString("order_id").equals(order_id)) {
                        date.setText(dateFinal1.format(new Date(obj.getLong("scheduled_on"))));
                        yr.setText(dateFinal2.format(new Date(obj.getLong("scheduled_on"))));
                        src.setText(obj.getJSONObject("source").getString("city").substring(0, 3).toUpperCase());
                        dest.setText(obj.getJSONObject("destination").getString("city").substring(0, 3).toUpperCase());
                        source.setText(obj.getJSONObject("source").getString("city") + ", " + obj.getJSONObject("destination").getString("state"));
                        destination.setText(obj.getJSONObject("destination").getString("city") + ", " + obj.getJSONObject("destination").getString("state"));

                        childHolderClasses.add(new ViewOrdersChildHolderClass.ChildHolderClass(2423422321L, "22hrs 32mins", "Maruti, Udyog Vihar"));
                        parentHolderClass.add(new ViewOrdersChildHolderClass.ParentHolderClass("HRXX-2345", "Delivered", "", 4, childHolderClasses));
                        break;
                    }
                }
            } catch (Throwable t) {
                Log.d(TAGlog, t.getMessage());
            }
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewOrdersChild);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewOrdersChildActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            ViewOrdersChildAdaptor adaptor = new ViewOrdersChildAdaptor(ViewOrdersChildActivity.this, parentHolderClass);
            mRecyclerView.setAdapter(adaptor);
        }
    }
}
