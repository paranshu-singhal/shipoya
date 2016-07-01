package com.android.shipoya.shipoya2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewOrderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quotes_new_order, container, false);
        ((TextView)v.findViewById(R.id.textView16)).setText(getArguments().getString("src"));
        ((TextView)v.findViewById(R.id.textView18)).setText(getArguments().getString("dest"));
        ((TextView)v.findViewById(R.id.textView20)).setText(getArguments().getString("truck_type"));
        ((TextView)v.findViewById(R.id.textView22)).setText(getArguments().getString("exp_price"));
        return v;
    }
}
