package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentSetPassword extends Fragment{

    FragmentSetPasswordFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_password, container, false);

        final EditText password = (EditText)v.findViewById(R.id.set_password);

        (v.findViewById(R.id.login_setPass)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSetPassword(password.getText().toString().trim(), getArguments().getString("otp"));
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSetPasswordFragmentListener) {mListener = (FragmentSetPasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onLoginInteraction");
        }
    }

    public interface FragmentSetPasswordFragmentListener{
        void onSetPassword(String password, String otp);
    }
}
