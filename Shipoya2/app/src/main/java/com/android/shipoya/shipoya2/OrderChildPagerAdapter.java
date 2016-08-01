package com.android.shipoya.shipoya2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderChildPagerAdapter extends FragmentPagerAdapter {

    String titles[];
    NewOrderFragment newOrderFragment;
    OrderTruckFragment orderTruckFragment;

    public OrderChildPagerAdapter(FragmentManager fm, NewOrderFragment newOrderFragment, OrderTruckFragment orderTruckFragment) {
        super(fm);
        titles = new String[]{"Truck Details", "Order Detail"};
        this.newOrderFragment = newOrderFragment;
        this.orderTruckFragment = orderTruckFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return newOrderFragment;
            case 0:
                return orderTruckFragment;
            default:
                return null;
        }
    }
}
