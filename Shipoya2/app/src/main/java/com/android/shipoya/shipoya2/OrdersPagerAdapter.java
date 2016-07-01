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
    List<OrderParent> parents;

    public OrdersPagerAdapter(Context context, List<OrderParent> parents) {
        this.context = context;
        arr[0] = context.getResources().getString(R.string.scheduled);
        arr[1] = context.getResources().getString(R.string.in_transit);
        arr[2] = context.getResources().getString(R.string.delivered);
        this.parents = parents;
    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_orders_pager_fragment,container,false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewInvoices);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        OrdersRecyclerAdapter adapter = new OrdersRecyclerAdapter(context, parents);
        mRecyclerView.setAdapter(adapter);
        ((ViewPager) container).addView(view);
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
