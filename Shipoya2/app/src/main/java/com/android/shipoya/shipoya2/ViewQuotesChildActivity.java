package com.android.shipoya.shipoya2;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import java.util.Locale;

public class ViewQuotesChildActivity extends AppCompatActivity implements CardView.OnClickListener, ConfirmationFragment.OnConfirmationFragmentInteractionListener, NegotiationFragment.OnNegotiationFragmentInteractionListener {

    private String order_id;
    private static final String TAGlog = "logTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes_child);

        TextView order_id_view = (TextView) findViewById(R.id.orderId);
        CardView parentView = (CardView) findViewById(R.id.parentView);
        ((ImageView) findViewById(R.id.expandIndicator)).setImageResource(R.drawable.ic_expand_less_black_24dp);

        order_id = getIntent().getExtras().getString("order_id");
        order_id_view.setText(order_id);
        parentView.setOnClickListener(this);

        (new LoadData()).execute();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onConfirmationFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNegotiationFragmentInteraction(Uri uri) {

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
            DateFormat dateFinal1 = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
            DateFormat dateFinal2 = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            TextView src = (TextView) findViewById(R.id.from);
            TextView dest = (TextView) findViewById(R.id.to);
            TextView yr = (TextView) findViewById(R.id.year);
            TextView date = (TextView) findViewById(R.id.date);
            try {
                JSONObject object = new JSONObject(buffer);
                JSONArray array = object.getJSONArray("data");

                JSONObject obj = null;
                for (int i = 0; i < array.length(); i++) {
                    obj = array.getJSONObject(i);
                    if (obj.getString("order_id").equals(order_id)) {
                        break;
                    }
                }
                date.setText(dateFinal1.format(new Date(obj.getLong("scheduled_on"))));
                yr.setText(dateFinal2.format(new Date(obj.getLong("scheduled_on"))));
                src.setText(obj.getJSONObject("source").getString("city").substring(0, 3).toUpperCase());
                dest.setText(obj.getJSONObject("destination").getString("city").substring(0, 3).toUpperCase());

                if (obj.getJSONObject("quotation").getBoolean("show")) {
                    (findViewById(R.id.state2Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));
                    (findViewById(R.id.state1Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_red, null));

                    Bundle bundle1 = new Bundle();
                    ArrayList<Quotes_Negotiation_Holder> negotiation_holderList = new ArrayList<>();

                    JSONArray bidders = obj.getJSONObject("quotation").getJSONArray("bidders");
                    for (int i = 0; i < bidders.length(); i++) {
                        JSONObject bid = bidders.getJSONObject(i);
                        negotiation_holderList.add(new Quotes_Negotiation_Holder(
                                bid.getString("carrier_name"),
                                bid.getString("quote_amount"),
                                (bid.getString("quote_count").equals("latest quote")) ? "Negotiated" : "New Quote",
                                "", //img link
                                2,
                                bid.getString("bid_id"),
                                obj.getString("order_id")
                        ));
                    }

                    bundle1.putParcelableArrayList("data", negotiation_holderList);
                    NegotiationFragment negotiationFragment = new NegotiationFragment();
                    negotiationFragment.setArguments(bundle1);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container_fragment, negotiationFragment)
                            .commit();
                } else if (obj.getJSONObject("confirmation").getBoolean("show")) {
                    (findViewById(R.id.state2Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_red, null));
                    (findViewById(R.id.state1Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_red, null));
                    (findViewById(R.id.state3Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));

                    DateFormat dateFinal3 = new SimpleDateFormat("H:ma, d MMM yyyy", Locale.ENGLISH);
                    ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                    Quotes_confirmation_holder holder = new Quotes_confirmation_holder(
                            obj.getJSONObject("confirmation").getString("carrier_name"),
                            obj.getJSONObject("confirmation").getString("quote_amount"),
                            dateFinal3.format(new Date(obj.getLong("pick_up_date"))),
                            obj.getString("payment_terms"));
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", holder);
                    confirmationFragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container_fragment, confirmationFragment)
                            .commit();
                }
                else{
                    NewOrderFragment new_order = new NewOrderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("src", obj.getJSONObject("source").getString("area")+obj.getJSONObject("source").getString("city")+obj.getJSONObject("source").getString("state"));
                    bundle.putString("dest",obj.getJSONObject("destination").getString("area")+", "+obj.getJSONObject("source").getString("city")+", "+obj.getJSONObject("source").getString("state") );
                    bundle.putString("truck_type",obj.getJSONObject("new_request").getJSONObject("preferred_truck_type").getString("truck_type")+", "+obj.getJSONObject("new_request").getJSONObject("preferred_truck_type").getString("truck_tonnage"));
                    bundle.putString("exp_price", obj.getJSONObject("new_request").getString("expected_price"));
                    new_order.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container_fragment, new_order)
                            .commit();
                }

            } catch (Throwable t) {
                Log.d(TAGlog, t.getMessage());
            }
        }
    }
}
