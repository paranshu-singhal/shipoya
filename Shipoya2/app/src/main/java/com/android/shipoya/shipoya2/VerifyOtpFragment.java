package com.android.shipoya.shipoya2;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyOtpFragment extends Fragment {

    private static final String ARG_PARAM2 = "Number";
    private String mNumber;

    private OnFragmentInteractionListener mListener;

    public VerifyOtpFragment() { }

    public static VerifyOtpFragment newInstance(String param2) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNumber = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verify_otp, container, false);

        TextView numberText = (TextView)v.findViewById(R.id.textView8);
        final TextView timeText = (TextView)v.findViewById(R.id.textView30);
        final EditText otp = (EditText)v.findViewById(R.id.editText3);
        Button btn = (Button)v.findViewById(R.id.button6);
        numberText.setText(mNumber);

        new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeText.setText(String.format("%d", (int)(millisUntilFinished / 1000)));
            }
            public void onFinish() {
            }
        }.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onVerifyPressed(otp.getText().toString(), mNumber);
            }
        });
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
        void onVerifyPressed(String otp, String username);
    }
}
