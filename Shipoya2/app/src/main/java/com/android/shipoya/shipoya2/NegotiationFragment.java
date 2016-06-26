package com.android.shipoya.shipoya2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NegotiationFragment extends Fragment {

    private static final String ARG_PARAM1 = "data";
    private List<Quotes_Negotiation_Holder> list;

    private OnNegotiationFragmentInteractionListener mListener;

    public NegotiationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.quotes_negotiation, container, false);
        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.quotes_negotiation_recycler);
        QuotesNegotiationRecyclerAdaptor adaptor = new QuotesNegotiationRecyclerAdaptor(getActivity(), list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adaptor);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNegotiationFragmentInteractionListener) {
            mListener = (OnNegotiationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNegotiationFragmentInteractionListener {
        void onNegotiationFragmentInteraction(Uri uri);
    }
}
