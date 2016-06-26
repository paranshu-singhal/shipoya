package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterPhoneNumberFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public EnterPhoneNumberFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button btn = (Button)v.findViewById(R.id.button2);

        EditText countryCodeText = (EditText)v.findViewById(R.id.editText);
        EditText numberText = (EditText)v.findViewById(R.id.editText2);
        TextView loginText = (TextView)v.findViewById(R.id.login_textView);

        //Log.d("logtag", countryCodeText.getText().toString() + " , " + numberText.getText().toString());

        mListener.onButtonPressed(btn, countryCodeText, numberText);
        mListener.onLoginClicked1(loginText);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onButtonPressed(Button Btn, EditText code, EditText number);
        void onLoginClicked1(TextView loginTextView);
    }
}
