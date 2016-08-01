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

public class QuotesPagerAdapter extends PagerAdapter {

    Context context;
    String[] title;
    List<OrderParent> parents, parentsNeg,parentsConf ;

    public QuotesPagerAdapter(Context context, List<OrderParent> parents, List<OrderParent> parentsNeg, List<OrderParent> parentsConf) {
        this.context = context;
        title = new String[]{"NEW REQUEST","NEGOTIATED","CONFIRMED"};
        this.parents = parents;
        this.parentsNeg = parentsNeg;
        this.parentsConf = parentsConf;
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
        LinearLayout noEntry = (LinearLayout) view.findViewById(R.id.no_entry);

        QuotesRecyclerAdapter adapter=null;

        switch (position){
            case 0:
                adapter = new QuotesRecyclerAdapter(context, parents);
                break;
            case 1:
                adapter = new QuotesRecyclerAdapter(context, parentsNeg);
                break;
            case 2:
                adapter = new QuotesRecyclerAdapter(context, parentsConf);
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
        return view == (o);
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
