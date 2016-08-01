package com.android.shipoya.shipoya2;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class QuotesNegotiationRecyclerAdaptor extends RecyclerView.Adapter<QuotesNegotiationRecyclerAdaptor.viewHolder> {

    Context ctx;
    List<Quotes_Negotiation_Holder> list;
    TextView carr_name, status, rating, price;
    Button confirm, negotiate;
    onFragmentListener mCallback;

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
                    ((ViewQuotesChildActivity)ctx).onConfirmItem(holder);
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

    public interface onFragmentListener {
        void onConfirmItem(Quotes_Negotiation_Holder holder);
    }


}
