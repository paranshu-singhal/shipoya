package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewOrdersChildActivity extends AppCompatActivity implements CardView.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_child);

        TextView order_id = (TextView)findViewById(R.id.orderId);
        CardView parentView = (CardView)findViewById(R.id.parentView);
        ImageView indicator = (ImageView)findViewById(R.id.expandIndicator);
        indicator.setImageResource(R.drawable.ic_expand_less_black_24dp);

        order_id.setText(getIntent().getExtras().getString("order_id"));
        parentView.setOnClickListener(this);

        List<ViewOrdersChildHolderClass.ParentHolderClass> holderClass = new ArrayList<>();
        List<ViewOrdersChildHolderClass.ChildHolderClass> childHolderClasses = new ArrayList<>();

        childHolderClasses.add(new ViewOrdersChildHolderClass.ChildHolderClass("10:50AM 22 May 2016", "22hrs 45mins", "Maruti Udyog Vihar, Sector-19, Gurgaon"));
        holderClass.add(new ViewOrdersChildHolderClass.ParentHolderClass("HR55X-3578", "ongoing"," ", 3, childHolderClasses));
        holderClass.add(new ViewOrdersChildHolderClass.ParentHolderClass("HR55X-3579", "delivered", " ", 4, childHolderClasses));

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerviewOrdersChild);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ViewOrdersChildAdaptor adaptor = new ViewOrdersChildAdaptor(this,holderClass);
        mRecyclerView.setAdapter(adaptor);
    }

    @Override
    public void onClick(View v) {
            onBackPressed();
    }
}
