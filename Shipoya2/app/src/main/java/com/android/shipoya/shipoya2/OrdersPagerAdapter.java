package com.android.shipoya.shipoya2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.Arrays;
import java.util.List;

public class OrdersPagerAdapter extends PagerAdapter {

    Context context;
    String[] arr = new String[3];
    List<OrderParent> parents, parents_trans, parents_clos;


    public OrdersPagerAdapter(Context context, List<OrderParent> parents, List<OrderParent> parents_trans, List<OrderParent> parents_clos) {
        this.context = context;
        this.parents = parents;
        this.parents_trans = parents_trans;
        this.parents_clos = parents_clos;
        arr[0] = "SCHEDULED";
        arr[1] = context.getResources().getString(R.string.in_transit);
        arr[2] = context.getResources().getString(R.string.delivered);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_fragment_layout,container,false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LinearLayout noEntry = (LinearLayout) view.findViewById(R.id.no_entry);

        OrdersRecyclerAdapter adapter=null;
        switch (position){
            case 0:
                adapter = new OrdersRecyclerAdapter(context, parents);
                break;
            case 1:
                adapter = new OrdersRecyclerAdapter(context, parents_trans);
                break;
            case 2:
                adapter = new OrdersRecyclerAdapter(context, parents_clos);
                break;
        }
        mRecyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            noEntry.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        (container).addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((LinearLayout) o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }
}
