package com.android.shipoya.shipoya2;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfirmationFragment extends Fragment {

    private static final String ARG_PARAM = "data";
    Quotes_confirmation_holder holder;

    private OnConfirmationFragmentInteractionListener mListener;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            holder = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_confirmation, container, false);
        final TextView carr_name = (TextView)v.findViewById(R.id.textView24);
        TextView money = (TextView)v.findViewById(R.id.textView23);
        final TextView pick_date = (TextView)v.findViewById(R.id.textView26);
        TextView payment_terms = (TextView)v.findViewById(R.id.textView28);

        (v.findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_confirmDialog(carr_name.getText().toString(), pick_date.getText().toString());
            }
        });

        carr_name.setText(holder.getCarr_name());
        money.setText(holder.getMoney());
        pick_date.setText(holder.getPickup_date());
        payment_terms.setText(holder.getPayment_term());
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmationFragmentInteractionListener) {
            mListener = (OnConfirmationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnConfirmationFragmentInteractionListener {
        void onConfirmationFragmentInteraction(Uri uri);
    }

    public void show_confirmDialog(String carr_name, String date){

        QuoteConfirmedDialog dialog = new QuoteConfirmedDialog();
        Bundle bundle = new Bundle();
        bundle.putString("carr_name", carr_name);
        bundle.putString("date", date);
        dialog.setArguments(bundle);
        FragmentManager fm = getActivity().getFragmentManager();
        dialog.show(fm, "tag");
    }
}
