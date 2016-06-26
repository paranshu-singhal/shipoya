package com.android.shipoya.shipoya2;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewQuotesChildActivity extends AppCompatActivity implements CardView.OnClickListener, ConfirmationFragment.OnConfirmationFragmentInteractionListener, NegotiationFragment.OnNegotiationFragmentInteractionListener{

    boolean new_order_enabled=false, confirmation_enabled = false, negotiation_enabled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes_child);

        TextView order_id = (TextView)findViewById(R.id.orderId);
        CardView parentView = (CardView)findViewById(R.id.parentView);
        ImageView indicator = (ImageView)findViewById(R.id.expandIndicator);
        indicator.setImageResource(R.drawable.ic_expand_less_black_24dp);

        Log.d("child: logTag", getIntent().getExtras().getString("order_id"));
        order_id.setText(getIntent().getExtras().getString("order_id"));
        parentView.setOnClickListener(this);

        Quotes_confirmation_holder holder = new Quotes_confirmation_holder("abcd carriers", "254000", "10:40AM 23 May,2016", "80% Advance");
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", holder);

        Bundle bundle1 = new Bundle();
        ArrayList<Quotes_Negotiation_Holder> negotiation_holderList = new ArrayList<>();
        negotiation_holderList.add(new Quotes_Negotiation_Holder("Gaurav Carriers", "234500", "Ongoing", "", 3));
        negotiation_holderList.add(new Quotes_Negotiation_Holder("Paranshu Carriers", "543000", "Delivered", "", 4));
        bundle1.putParcelableArrayList("data", negotiation_holderList);

        final NewOrderFragment new_order = new NewOrderFragment();
        final ConfirmationFragment confirmationFragment = new ConfirmationFragment();
        final NegotiationFragment negotiationFragment = new NegotiationFragment();

        confirmationFragment.setArguments(bundle);
        negotiationFragment.setArguments(bundle1);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, new_order)
                .commit();

        LinearLayout new_order_ll = (LinearLayout)findViewById(R.id.state1);
        LinearLayout negotiation_ll = (LinearLayout)findViewById(R.id.state2);
        LinearLayout confirmation_ll = (LinearLayout)findViewById(R.id.state3);

        confirmation_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = (TextView)findViewById(R.id.state1Text);
                t.setBackground(getDrawable(R.drawable.circle_green));
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, confirmationFragment)
                        .commit();
            }
        });

        new_order_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new_order)
                        .commit();
            }
        });

        negotiation_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, negotiationFragment)
                        .commit();
            }
        });

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
}
