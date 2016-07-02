package com.android.shipoya.shipoya2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

public class QuotesNegotiationRecyclerAdaptor extends RecyclerView.Adapter<QuotesNegotiationRecyclerAdaptor.viewHolder> {

    Context ctx;
    List<Quotes_Negotiation_Holder> list;
    TextView carr_name, status, rating, price;
    Button confirm, negotiate;


    public QuotesNegotiationRecyclerAdaptor(Context ctx, List<Quotes_Negotiation_Holder> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profile;

        public viewHolder(View itemView) {
            super(itemView);
            carr_name = (TextView) itemView.findViewById(R.id.textView10);
            rating = (TextView) itemView.findViewById(R.id.textView11);
            status = (TextView) itemView.findViewById(R.id.textView13);
            price = (TextView) itemView.findViewById(R.id.textView12);
            profile = (ImageView) itemView.findViewById(R.id.profile_image);
            confirm = (Button) itemView.findViewById(R.id.button8);
            negotiate = (Button) itemView.findViewById(R.id.button9);
        }

        public void bind(final Quotes_Negotiation_Holder holder) {
            carr_name.setText(holder.getCarr_name());
            status.setText(holder.getStatus());
            price.setText(holder.getMoney());
            String rt = "";
            for (int i = 0; i < holder.getRating(); i++) {
                rt += "*";
            }
            rating.setText(rt);

            negotiate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNegotiaterDialog(holder);
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage(ctx.getResources().getString(R.string.confirm_sure))
                            .setPositiveButton(ctx.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    (new uploadConfirm(holder)).execute();
                                }
                            })
                            .setNegativeButton(ctx.getResources().getString(R.string.cancel), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        return new viewHolder(inflater.inflate(R.layout.quotes_negotiation_child, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Quotes_Negotiation_Holder obj = list.get(position);
        holder.bind(obj);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void showNegotiaterDialog(Quotes_Negotiation_Holder holder) {
        NegotiatorDialog dialog = new NegotiatorDialog();

        Bundle bundle = new Bundle();
        bundle.putParcelable("data", holder);
        dialog.setArguments(bundle);

        FragmentManager fm = ((Activity) ctx).getFragmentManager();
        dialog.show(fm, "tag");
    }

    public class uploadConfirm extends AsyncTask<Void, Void, String> {

        Quotes_Negotiation_Holder holder;

        public uploadConfirm(Quotes_Negotiation_Holder holder) {
            this.holder = holder;
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ctx, "", ctx.getResources().getString(R.string.loading_wait), true);
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = null;
            String url = "/auction_resources/api/loads/" + holder.getOrder_id() + "/prelim_confirm";
            try {
                JSONObject container = new JSONObject();
                container.put("confirmed_bid_id", holder.getBid_id());
                response = MakeRequest.sendRequest(ctx, url, "POST", container);
            } catch (Throwable t) {
                Log.d("logTag", t.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                Toast.makeText(ctx, object.getString("message"), Toast.LENGTH_LONG).show();
            } catch (Throwable t) {
                Log.d("logTag", t.getMessage());
            }
        }
    }


}
