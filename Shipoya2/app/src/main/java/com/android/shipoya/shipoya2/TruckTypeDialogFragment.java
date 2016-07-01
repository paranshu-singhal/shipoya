package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class TruckTypeDialogFragment extends DialogFragment {

    onInteractionListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.truck_type_dialog, container, false);

        Spinner spinner5 = (Spinner) v.findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.truck_type_1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter);

        Spinner spinner6 = (Spinner) v.findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.truck_type_2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter2);

        getDialog().setCanceledOnTouchOutside(false);
        Button btn = (Button)v.findViewById(R.id.button10);
        mCallback.onTruckTypeSelected(spinner5, spinner6, btn);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onInteractionListener) {
            mCallback = (onInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface onInteractionListener{
        void onTruckTypeSelected(Spinner spinner1, Spinner spinner2, Button btn);
    }
}
