package com.android.shipoya.shipoya2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewQuotesChildActivity extends AppCompatActivity implements
        CardView.OnClickListener,
        ConfirmationFragment.OnConfirmationFragmentInteractionListener,
        NegotiationFragment.OnNegotiationFragmentInteractionListener {

    private String order_id;
    private static final String TAGlog = "logTag";
    private JSONObject main;
    private boolean state2enabled = false, state3enabled = false;
    private NewOrderFragment newOrderFragment = null;
    private NegotiationFragment negotiationFragment = null;
    private ConfirmationFragment confirmationFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes_child);

        TextView order_id_view = (TextView) findViewById(R.id.orderId);
        CardView parentView = (CardView) findViewById(R.id.parentView);
        ((ImageView) findViewById(R.id.expandIndicator)).setImageResource(R.drawable.ic_expand_less_black_24dp);

        Intent intent = getIntent();

        Uri data = intent.getData();
        if (data != null) {
            order_id = data.getQueryParameter("orderId");
            Log.d(TAGlog, order_id);
        } else {
            order_id = intent.getExtras().getString("order_id");
            order_id_view.setText(order_id);
        }

        parentView.setOnClickListener(this);
        (findViewById(R.id.state1)).setOnClickListener(this);
        (findViewById(R.id.state2)).setOnClickListener(this);
        (findViewById(R.id.state3)).setOnClickListener(this);

        onPostExecute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parentView:
                onBackPressed();
                break;
            case R.id.state1:
                showState1();
                break;
            case R.id.state2:
                if (state2enabled) {
                    showState2();
                }
                break;
            case R.id.state3:
                if (state3enabled) {
                    showState3();
                }
                break;
        }
    }


    protected void onPostExecute() {

        DateFormat dateFinal1 = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
        DateFormat dateFinal2 = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        TextView src = (TextView) findViewById(R.id.from);
        TextView dest = (TextView) findViewById(R.id.to);
        TextView yr = (TextView) findViewById(R.id.year);
        TextView date = (TextView) findViewById(R.id.date);
        try {
            JSONObject object = StaticData.ORDER_DATA;
            JSONArray array = object.getJSONArray("data");

            JSONObject obj = null;
            for (int i = 0; i < array.length(); i++) {
                obj = array.getJSONObject(i);
                if (obj.getString("order_id").equals(order_id)) {
                    break;
                }
            }
            main = obj;

            date.setText(dateFinal1.format(new Date(main.getLong("pick_up_date") * 1000)));
            yr.setText(dateFinal2.format(new Date(main.getLong("pick_up_date") * 1000)));
            src.setText(main.getJSONObject("source").getString("city").trim().substring(0, 3).toUpperCase());
            dest.setText(main.getJSONObject("destination").getString("city").trim().substring(0, 3).toUpperCase());

            if (obj.getJSONObject("quotation").getBoolean("show")) {
                showState2();
            } else if (obj.getJSONObject("confirmation").getBoolean("show")) {
                showState3();
            } else {
                showState1();
            }

        } catch (Throwable t) {
            Log.d(TAGlog, "quotesChild: " + t.getMessage());
        }
    }

    public void showState1() {
        ((TextView) (findViewById(R.id.newOrderText))).setTypeface(null, Typeface.BOLD);
        ((TextView) (findViewById(R.id.quotationText))).setTypeface(null, Typeface.NORMAL);
        ((TextView) (findViewById(R.id.confirmedText))).setTypeface(null, Typeface.NORMAL);

        try {
            String srcTxt="", destTxt="";
            if((main.getJSONObject("source").has("area"))){
                srcTxt += (main.getJSONObject("source").getString("area") + ", "); }
            if(srcTxt.substring(srcTxt.length()-1).equals(",")){
                srcTxt = srcTxt.substring(0, srcTxt.length()-1);
            }
            srcTxt += main.getJSONObject("source").getString("city")
                    + ", " + main.getJSONObject("source").getString("state");
            srcTxt = srcTxt.trim();

            if((main.getJSONObject("destination").has("area"))){
                destTxt += (main.getJSONObject("destination").getString("area") + ", "); }
            if(destTxt.substring(destTxt.length()-1).equals(",")){
                destTxt = destTxt.substring(0, destTxt.length()-1);
            }
            destTxt += main.getJSONObject("destination").getString("city")
                    + ", " + main.getJSONObject("destination").getString("state");
            destTxt = destTxt.trim();

            NewOrderHolder newOrderHolder = new NewOrderHolder(
                    srcTxt,
                    main.getString("shipper_name"),
                    destTxt,
                    main.getJSONObject("preferred_truck_type").getString("truck_type"),
                    main.getString("material_type"),
                    main.getString("payment_terms"),
                    main.getString("consignment_weight")+" MT",
                    main.getString("num_trucks"),
                    false);

            newOrderFragment = new NewOrderFragment();
            Bundle b = new Bundle();
            b.putParcelable("data", newOrderHolder);
            newOrderFragment.setArguments(b);

            if (negotiationFragment == null && confirmationFragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_fragment, newOrderFragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, newOrderFragment)
                        .commit();
            }
        } catch (Throwable t) {
            Log.d(TAGlog, t.getMessage());
            t.printStackTrace();
        }
    }

    public void showState2() {
        state2enabled = true;
        try {
            (findViewById(R.id.state2Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));
            (findViewById(R.id.state1Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));

            ((TextView) (findViewById(R.id.newOrderText))).setTypeface(null, Typeface.NORMAL);
            ((TextView) (findViewById(R.id.quotationText))).setTypeface(null, Typeface.BOLD);
            ((TextView) (findViewById(R.id.confirmedText))).setTypeface(null, Typeface.NORMAL);

            Bundle bundle1 = new Bundle();
            ArrayList<Quotes_Negotiation_Holder> negotiation_holderList = new ArrayList<>();

            JSONArray bidders = main.getJSONObject("quotation").getJSONArray("bidders");
            for (int i = 0; i < bidders.length(); i++) {
                JSONObject bid = bidders.getJSONObject(i);
                negotiation_holderList.add(new Quotes_Negotiation_Holder(
                        bid.getString("carrier_name"),
                        bid.getString("quote_amount"),
                        (bid.getString("quote_count").equals("latest quote")) ? "Negotiated" : "New Quote",
                        "", //img link
                        2,
                        bid.getString("bid_id"),
                        main.getString("order_id"),
                        main.getString("payment_terms"),
                        main.getString("pick_up_date")
                ));
            }
            bundle1.putParcelableArrayList("data", negotiation_holderList);

            if (negotiationFragment == null && confirmationFragment == null) {
                negotiationFragment = new NegotiationFragment();
                negotiationFragment.setArguments(bundle1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_fragment, negotiationFragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, negotiationFragment)
                        .commit();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void showState3() {
        state3enabled = true;
        try {
            (findViewById(R.id.state2Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_red, null));
            (findViewById(R.id.state1Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));
            (findViewById(R.id.state3Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));

            ((TextView) (findViewById(R.id.newOrderText))).setTypeface(null, Typeface.NORMAL);
            ((TextView) (findViewById(R.id.quotationText))).setTypeface(null, Typeface.NORMAL);
            ((TextView) (findViewById(R.id.confirmedText))).setTypeface(null, Typeface.BOLD);

            if (newOrderFragment == null && negotiationFragment == null && confirmationFragment == null) {
                DateFormat dateFinal3 = new SimpleDateFormat("H:ma, d MMM yyyy", Locale.ENGLISH);
                Quotes_confirmation_holder holder = new Quotes_confirmation_holder(
                        main.getJSONObject("confirmation").getString("carrier_name"),
                        main.getJSONObject("confirmation").getString("quote_amount"),
                        dateFinal3.format(new Date(main.getLong("pick_up_date"))),
                        main.getString("payment_terms"));
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", holder);

                confirmationFragment = new ConfirmationFragment();
                confirmationFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_fragment, confirmationFragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, confirmationFragment)
                        .commit();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onNegotiationFragmentInteraction(final Quotes_Negotiation_Holder holder) {
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewQuotesChildActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_sure))
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new uploadConfirm(holder)).execute();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
        */
    }

    public void onConfirmItem(final Quotes_Negotiation_Holder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewQuotesChildActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_sure))
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StaticData.NETWORK_AVAILABLE) {
                            //(new uploadConfirm(holder)).execute();
                            String url = "/auction_resources/api/loads/" + holder.getOrder_id() + "/prelim_confirm";
                            try {
                                JSONObject container = new JSONObject();
                                container.put("confirmed_bid_id", holder.getBid_id());
                                (new MakeRequest(
                                        ViewQuotesChildActivity.this,
                                        url,
                                        "POST",
                                        true,
                                        container,
                                        new Interface() {
                                            @Override
                                            public void onPostExecute(String response) {
                                                onPostExecute2(response, holder);
                                            }
                                        }
                                )).execute();
                            } catch (Throwable t) {
                                Log.d(TAGlog, "ViewQuotesChild: " + t.getMessage());
                            }
                        } else {
                            Toast.makeText(ViewQuotesChildActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onPostExecute2(String s, final Quotes_Negotiation_Holder holder) {

        try {
            JSONObject object = new JSONObject(s);
            Toast.makeText(ViewQuotesChildActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
            (findViewById(R.id.state2Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_red, null));
            (findViewById(R.id.state3Text)).setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_green, null));

            SimpleDateFormat dateFormat = new SimpleDateFormat("H:ma, d MMM yyyy");

            confirmationFragment = new ConfirmationFragment();
            Quotes_confirmation_holder holder2 = new Quotes_confirmation_holder(
                    holder.getCarr_name(), holder.getMoney(), dateFormat.format(new Date(Long.parseLong(holder.getPickup_date()))), holder.getPayment_terms());
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", holder2);
            confirmationFragment.setArguments(bundle);

            if (StaticData.NETWORK_AVAILABLE) {
                (new MakeRequest(
                        this,
                        "/auction_resources/api/loads/shipper_loads",
                        "GET",
                        true,
                        null,
                        new Interface() {
                            @Override
                            public void onPostExecute(String response) {
                                try {
                                    if (response != null && response.length() > 0) {
                                        StaticData.ORDER_DATA = new JSONObject(response);
                                        state2enabled = false;
                                        state3enabled = true;
                                        StaticData.ORDER_CHANGED = true;
                                        showState3();
                                        getSupportFragmentManager().executePendingTransactions();
                                    } else {
                                        Toast.makeText(ViewQuotesChildActivity.this, getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Throwable t) {
                                    Log.d(TAGlog, t.getMessage());
                                }
                            }
                        }
                )).execute();
            } else {
                StaticData.showNetworkError(ViewQuotesChildActivity.this);
            }
            //Toast.makeText(ViewQuotesChildActivity.this, getResources().getString(R.string.swipe_refresh), Toast.LENGTH_LONG).show();
            //finish();
            //startActivity(getIntent());

        } catch (Throwable t) {
            Log.d("logTag", t.getMessage());
        }
    }


    @Override
    public void onConfirmationFragmentInteraction(Uri uri) {

    }
}
