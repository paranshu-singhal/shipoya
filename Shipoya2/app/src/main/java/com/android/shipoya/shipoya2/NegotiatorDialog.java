package com.android.shipoya.shipoya2;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NegotiatorDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.negotiator_dialog, container, false);

        ImageView profile = (ImageView)v.findViewById(R.id.profile_image);

        Bundle bundle = getArguments();

        ((TextView)v.findViewById(R.id.textView_name_carrier)).setText(bundle.getString("carr_name"));
        ((TextView)v.findViewById(R.id.textView33)).setText(bundle.getString("rating"));
        ((TextView)v.findViewById(R.id.textView32)).setText(bundle.getString("status"));
        ((TextView)v.findViewById(R.id.textView31)).setText(bundle.getString("price"));

        getDialog().setCanceledOnTouchOutside(false);
        v.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}
