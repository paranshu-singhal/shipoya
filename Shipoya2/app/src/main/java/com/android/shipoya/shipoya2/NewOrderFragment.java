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
        View v = inflater.inflate(R.layout.new_order_fragment, container, false);

        NewOrderHolder holder = getArguments().getParcelable("data");

        ((TextView)v.findViewById(R.id.owner_name)).setText(holder.getShipperName());
        ((TextView)v.findViewById(R.id.source)).setText(holder.getSource());
        ((TextView)v.findViewById(R.id.destination)).setText(holder.getDestination());
        ((TextView)v.findViewById(R.id.truck_type)).setText(holder.getTruckType());
        ((TextView)v.findViewById(R.id.num_truck)).setText("X "+holder.getNumTrucks());
        ((TextView)v.findViewById(R.id.material_type)).setText(holder.getMaterialType());
        ((TextView)v.findViewById(R.id.quantity)).setText(holder.getConsignmentWeight());
        ((TextView)v.findViewById(R.id.payment_detail)).setText(holder.getPaymentDetail());
        return v;
    }
}
