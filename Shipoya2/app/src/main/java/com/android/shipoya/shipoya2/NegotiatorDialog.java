package com.android.shipoya.shipoya2;

import android.app.DialogFragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

public class NegotiatorDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.negotiator_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView profile = (ImageView) v.findViewById(R.id.profile_image);

        final Quotes_Negotiation_Holder holder = getArguments().getParcelable("data");

        ((TextView) v.findViewById(R.id.textView_name_carrier)).setText(holder.getCarr_name());
        ((TextView) v.findViewById(R.id.textView33)).setText(String.format("%d", holder.getRating()));
        ((TextView) v.findViewById(R.id.textView32)).setText(holder.getStatus());
        ((TextView) v.findViewById(R.id.textView31)).setText(holder.getMoney());
        TextView shipper_text = (TextView) v.findViewById(R.id.textView35);
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_IN);

        getBidDetails(shipper_text, progressBar, holder.getBid_id());

        final EditText newPrice = (EditText) v.findViewById(R.id.editText4);
        (v.findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticData.NETWORK_AVAILABLE) {
                    uploadNegotiate(holder, newPrice.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    public void getBidDetails(final TextView shipper_text, final ProgressBar progressBar, String bidId) {
        try {
            (new MakeRequest(
                    getActivity(),
                    "/auction_resources/api/load_negotiations/negotiation_details/" + URLEncoder.encode(bidId, "UTF-8"),
                    "GET",
                    false,
                    null,
                    new Interface() {
                        @Override
                        public void onPostExecute(String response) {
                            if (response != null && response.length() > 0) {
                                progressBar.setVisibility(View.GONE);
                                shipper_text.setVisibility(View.VISIBLE);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String shipper_price = obj.getJSONObject("data").getJSONArray("load_negotiations").getJSONObject(0).getJSONObject("negotiation_details").getString("shipper_price");
                                    shipper_text.setText(shipper_price);
                                } catch (Throwable t) {
                                    Log.d("logTag", t.getMessage());
                                }
                            }
                        }
                    }
            )).execute();
        } catch (Throwable t) {
            Log.d("logTag", t.getMessage());
        }
    }

    public void uploadNegotiate(Quotes_Negotiation_Holder holder, String newPrice) {

        try {
            JSONObject container = new JSONObject();
            container.put("bid_id", holder.getBid_id());
            container.put("shipper_price", newPrice);
            (new MakeRequest(
                    getActivity(),
                    "/auction_resources/api/load_negotiations",
                    "POST",
                    true,
                    container,
                    new Interface() {
                        @Override
                        public void onPostExecute(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                                getDialog().dismiss();
                            } catch (Throwable t) {
                                Log.d("logTag", t.getMessage());
                            }
                        }
                    }
            )).execute();
        } catch (Throwable t) {
            Log.d("logTag", t.getMessage());
        }

    }
}
