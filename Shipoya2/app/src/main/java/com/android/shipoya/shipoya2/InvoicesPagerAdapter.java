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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class InvoicesPagerAdapter extends PagerAdapter {

    Context context;
    String title[];

    public InvoicesPagerAdapter(Context context) {
        this.context = context;
        title = new String[]{"ALL","PENDING","PAID"};

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_fragment_layout,container,false);

        List<InvoiceChild> invoiceChildren = new ArrayList<>();
        InvoiceChild invoiceChild = new InvoiceChild();
        invoiceChildren.add(invoiceChild);

        InvoiceParent parent = new InvoiceParent("DEL", "MUM", "since", "date", "invoice", "cost", invoiceChildren);
        List<InvoiceParent> parents = Arrays.asList(parent, parent);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        InvoicesRecyclerAdapter adapter = new InvoicesRecyclerAdapter(context, parents);
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
        return title[position];
    }
}
