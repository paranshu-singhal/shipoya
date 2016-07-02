package com.android.shipoya.shipoya2;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class NegotiatorDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.negotiator_dialog, container, false);

        ImageView profile = (ImageView)v.findViewById(R.id.profile_image);

        final Quotes_Negotiation_Holder holder = getArguments().getParcelable("data");

        ((TextView)v.findViewById(R.id.textView_name_carrier)).setText(holder.getCarr_name());
        ((TextView)v.findViewById(R.id.textView33)).setText(String.format("%d",holder.getRating()));
        ((TextView)v.findViewById(R.id.textView32)).setText(holder.getStatus());
        ((TextView)v.findViewById(R.id.textView31)).setText(holder.getMoney());

        getDialog().setCanceledOnTouchOutside(false);
        final EditText newPrice = (EditText)v.findViewById(R.id.editText4);

        (v.findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new uploadNegotiate(newPrice.getText().toString(), holder)).execute();
            }
        });
        return v;
    }

    public class uploadNegotiate extends AsyncTask<Void, Void, String>{

        Quotes_Negotiation_Holder holder;
        String newPrice;

        public uploadNegotiate(String newPrice, Quotes_Negotiation_Holder holder) {
            this.newPrice = newPrice;
            this.holder = holder;
        }

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait), true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String response=null;
            String url = "/auction_resources/api/load_negotiations";
            try {
                JSONObject container = new JSONObject();
                container.put("confirmed_bid_id", holder.getBid_id());
                container.put("shipper_price", newPrice);
                response = MakeRequest.sendRequest(getActivity(), url, "POST", container);
            }
            catch (Throwable t){
                Log.d("logTag", t.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            try{
                JSONObject object = new JSONObject(s);
                Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
            }
            catch (Throwable t){
                Log.d("logTag", t.getMessage());
            }
        }
    }


}
