package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderTruckFragment extends Fragment {

    RecyclerView trucksDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment_layout, null);
        OrderTruckModel orderTruckModel = getArguments().getParcelable("data");
        trucksDetail = (RecyclerView) view.findViewById(R.id.recyclerview);
        trucksDetail.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        trucksDetail.setLayoutManager(mLayoutManager);
        ViewOrdersChildAdaptor adaptor = new ViewOrdersChildAdaptor(getContext(), orderTruckModel.getParents());
        trucksDetail.setAdapter(adaptor);
        return view;
    }
}
