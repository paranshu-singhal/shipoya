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

import java.util.List;

public class SearchPagerAdapter extends PagerAdapter {

    Context context;
    String title[];
    List<OrderParent> loads;
    List<OrderParent> orders;

    public SearchPagerAdapter(Context context, List<OrderParent> loads, List<OrderParent> orders) {
        this.context = context;
        title = new String[]{"Quotes", "Orders", "Invoices"};
        this.loads = loads;
        this.orders = orders;
    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_fragment_layout, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        switch (position) {
            case 0:
                QuotesRecyclerAdapter adapter = new QuotesRecyclerAdapter(context, loads);
                mRecyclerView.setAdapter(adapter);
                break;
            case 1:
                OrdersRecyclerAdapter adapter1 = new OrdersRecyclerAdapter(context, orders);
                mRecyclerView.setAdapter(adapter1);
                break;
            case 2:

                break;
        }
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
        return title[position];
    }
}
