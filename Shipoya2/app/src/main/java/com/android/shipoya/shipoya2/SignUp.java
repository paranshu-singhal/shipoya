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

public class SignUp extends Fragment {

    private OnSignUpInteraction mListener;

    public SignUp() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button signUp = (Button) v.findViewById(R.id.sign_up);
        EditText firstName = (EditText) v.findViewById(R.id.first_name);
        EditText lastName = (EditText) v.findViewById(R.id.last_name);
        EditText companyName = (EditText) v.findViewById(R.id.company_name);
        EditText mobile = (EditText) v.findViewById(R.id.mobile_no);
        EditText email = (EditText) v.findViewById(R.id.email);

        TextView loginText = (TextView) v.findViewById(R.id.login);

        //Log.d("logtag", countryCodeText.getText().toString() + " , " + numberText.getText().toString());

        mListener.onSignUp(signUp, firstName, lastName, companyName, mobile, email);
        mListener.onLoginTextClicked(loginText);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpInteraction) {
            mListener = (OnSignUpInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onLoginInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignUpInteraction {
        void onSignUp(Button signUp, EditText firstName, EditText lastName, EditText companyName, EditText mobile, EditText email);

        void onLoginTextClicked(TextView loginTextView);
    }
}
