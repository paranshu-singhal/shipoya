package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewQuotesChild extends AppCompatActivity implements CardView.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes_child);

        TextView order_id = (TextView)findViewById(R.id.orderId);
        CardView parentView = (CardView)findViewById(R.id.parentView);
        ImageView indicator = (ImageView)findViewById(R.id.expandIndicator);
        indicator.setImageResource(R.drawable.ic_expand_less_black_18dp);

        order_id.setText(getIntent().getExtras().getString("order_id"));
        parentView.setOnClickListener(this);

        NewOrderFragment new_order = new NewOrderFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, new_order)
                .commit();
    }

    @Override
    public void onClick(View v) {
            onBackPressed();
    }
}
