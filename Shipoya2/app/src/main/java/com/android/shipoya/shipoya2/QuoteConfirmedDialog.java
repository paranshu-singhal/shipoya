package com.android.shipoya.shipoya2;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuoteConfirmedDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quote_confirmed_dialog, container, false);

        ((TextView)v.findViewById(R.id.textView37)).setText("Your order with "+getArguments().getString("carr_name")+" on "+getArguments().getString("date")+" has been confirmed");
        getDialog().setCanceledOnTouchOutside(false);
        (v.findViewById(R.id.button13)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}
