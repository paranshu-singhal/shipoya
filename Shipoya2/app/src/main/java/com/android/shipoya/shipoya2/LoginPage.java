package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends Fragment {

    private onLoginInteraction mListener;

    public LoginPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_page, container, false);

        EditText userName = (EditText) v.findViewById(R.id.user_name);
        EditText password = (EditText) v.findViewById(R.id.password);
        Button login = (Button) v.findViewById(R.id.login);
        TextView signup = (TextView) v.findViewById(R.id.sign_up);
        TextView forget = (TextView) v.findViewById(R.id.forget_password);
        TextView firstTimeLogin = (TextView)v.findViewById(R.id.first_time_login_go);
        mListener.onLogin(userName, password, login, signup, forget, firstTimeLogin);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onLoginInteraction) {
            mListener = (onLoginInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onLoginInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onLoginInteraction {
        void onLogin(EditText username, EditText password, Button login, TextView signup, TextView forgetPassword, TextView firstTimeLogin);
    }
}
