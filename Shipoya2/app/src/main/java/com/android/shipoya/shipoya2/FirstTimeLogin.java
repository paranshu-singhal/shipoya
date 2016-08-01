package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FirstTimeLogin extends Fragment{

    FirstTimeLoginFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_first_time_login, container, false);

        final EditText username = (EditText) v.findViewById(R.id.user_name_firstTime);
        (v.findViewById(R.id.login_firstTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().length()>0){
                    mListener.onFirstTimeLoginPressed(username);
                }
                else{
                    username.setError(getResources().getString(R.string.text_error));
                }
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FirstTimeLoginFragmentListener) {mListener = (FirstTimeLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onLoginInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface FirstTimeLoginFragmentListener{
        void onFirstTimeLoginPressed(EditText username);
    }
}
