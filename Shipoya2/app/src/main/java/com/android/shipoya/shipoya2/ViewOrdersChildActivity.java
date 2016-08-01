package com.android.shipoya.shipoya2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
    ViewPager pager;
    TabLayout tabLayout;

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
        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        onPostExecute();

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }


    protected void onPostExecute() {

        List<ViewOrdersChildHolderClass.ParentHolderClass> parentHolderClass = new ArrayList<>();
        List<ViewOrdersChildHolderClass.ChildHolderClass> childHolderClasses = new ArrayList<>();

        TextView src = (TextView) findViewById(R.id.from);
        TextView dest = (TextView) findViewById(R.id.to);
        TextView yr = (TextView) findViewById(R.id.year);
        TextView date = (TextView) findViewById(R.id.date);

        DateFormat dateFinal1 = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
        DateFormat dateFinal2 = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        try {
            JSONObject object = StaticData.ORDER_DATA;
            JSONArray array = object.getJSONArray("data");

            JSONObject obj = null;
            for (int i = 0; i < array.length(); i++) {
                obj = array.getJSONObject(i);
                if (obj.getJSONObject("transit").getBoolean("show") && obj.getString("order_id").equals(order_id)) {
                    Log.d(TAGlog, obj.getString("order_id"));
                    break;
                }
            }
            date.setText(dateFinal1.format(new Date(Long.parseLong(obj.getString("pick_up_date")) * 1000)));
            yr.setText(dateFinal2.format(new Date(obj.getLong("pick_up_date") * 1000)));
            src.setText(obj.getJSONObject("source").getString("city").trim().substring(0, 3).toUpperCase());
            dest.setText(obj.getJSONObject("destination").getString("city").trim().substring(0, 3).toUpperCase());

            String srcTxt="", destTxt="";
            if((obj.getJSONObject("source").has("area"))){
                srcTxt += (obj.getJSONObject("source").getString("area") + ", "); }
            if(srcTxt.substring(srcTxt.length()-1).equals(",")){
                srcTxt = srcTxt.substring(0, srcTxt.length()-1);
            }
            srcTxt += obj.getJSONObject("source").getString("city")
                    + ", " + obj.getJSONObject("source").getString("state");
            srcTxt = srcTxt.trim();

            if((obj.getJSONObject("destination").has("area"))){
                destTxt += (obj.getJSONObject("destination").getString("area") + ", "); }
            if(destTxt.substring(destTxt.length()-1).equals(",")){
                destTxt = destTxt.substring(0, destTxt.length()-1);
            }
            destTxt += obj.getJSONObject("destination").getString("city")
                    + ", " + obj.getJSONObject("destination").getString("state");
            destTxt = destTxt.trim();

            NewOrderHolder newOrderHolder = new NewOrderHolder(
                    srcTxt,
                    obj.getString("shipper_name"),
                    destTxt,
                    obj.getJSONObject("preferred_truck_type").getString("truck_type"),
                    obj.getString("material_type"),
                    obj.getString("payment_terms"),
                    obj.getString("consignment_weight")+" MT",
                    obj.getString("num_trucks"),
                    false);
            childHolderClasses.add(new ViewOrdersChildHolderClass.ChildHolderClass(2423422321L, "22hrs 32mins", "Maruti, Udyog Vihar"));
            parentHolderClass.add(new ViewOrdersChildHolderClass.ParentHolderClass("HRXX-2345", "Delivered", "", 4, childHolderClasses));
            parentHolderClass.add(new ViewOrdersChildHolderClass.ParentHolderClass("HRXX-2345", "Delivered", "", 4, childHolderClasses));

            NewOrderFragment newOrderFragment = new NewOrderFragment();
            Bundle b = new Bundle();
            b.putParcelable("data", newOrderHolder);
            newOrderFragment.setArguments(b);

            OrderTruckModel orderTruckModel = new OrderTruckModel(parentHolderClass);
            OrderTruckFragment orderTruckFragment = new OrderTruckFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", orderTruckModel);
            orderTruckFragment.setArguments(bundle);

            pager.setAdapter(new OrderChildPagerAdapter(getSupportFragmentManager(), newOrderFragment, orderTruckFragment));
            tabLayout.setupWithViewPager(pager);

        } catch (Throwable t) {
            Log.d(TAGlog, t.getMessage());
        }
    }
}
