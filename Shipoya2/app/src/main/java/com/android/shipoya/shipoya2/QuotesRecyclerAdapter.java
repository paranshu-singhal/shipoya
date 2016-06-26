package com.android.shipoya.shipoya2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuotesRecyclerAdapter extends RecyclerView.Adapter<QuotesRecyclerAdapter.ViewHolder> {


    Context context;
    List<QuoteParent> parentList;

    public QuotesRecyclerAdapter(Context context, List<QuoteParent> parentList) {
        this.context = context;
        this.parentList = parentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView src, dest, order_id, yr, date;
        public ViewHolder(View itemView) {
            super(itemView);

            src = (TextView)itemView.findViewById(R.id.from);
            dest = (TextView)itemView.findViewById(R.id.to);
            order_id = (TextView)itemView.findViewById(R.id.orderId);
            yr = (TextView)itemView.findViewById(R.id.year);
            date = (TextView)itemView.findViewById(R.id.date);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_orders_parent, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardView cardView = (CardView)v.findViewById(R.id.container_cardView);

                Intent intent = new Intent(context, ViewQuotesChildActivity.class);
                Bundle bun = new Bundle();
                bun.putString("order_id", ((TextView)v.findViewById(R.id.orderId)).getText().toString());
                //Log.d("Adaptor: logTag", order_id_2);
                intent.putExtras(bun);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,cardView, context.getResources().getString(R.string.view_orders_transition));
                    context.startActivity(intent, options.toBundle());
                }
                else{
                    context.startActivity(intent);
                }
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuoteParent obj = parentList.get(position);
        holder.src.setText(obj.getFrom());
        holder.dest.setText(obj.getTo());
        holder.order_id.setText(obj.getOrderId());

        DateFormat dateOriginal = new SimpleDateFormat("d MM yyyy", Locale.ENGLISH);
        DateFormat dateFinal1 = new SimpleDateFormat("EEE, MMM d");
        DateFormat dateFinal2 = new SimpleDateFormat("yyyy");
        try {
            Date date = dateOriginal.parse(obj.getDate());
            holder.date.setText(dateFinal1.format(date));
            holder.yr.setText(dateFinal2.format(date));
        }
        catch (Throwable t){
            Log.d("logtag", t.getMessage());
        }
    }
    @Override
    public int getItemCount() {
        return parentList.size();
    }
}
