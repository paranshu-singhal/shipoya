package com.android.shipoya.shipoya2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class InvoicesRecyclerAdapter extends ExpandableRecyclerAdapter<InvoicesRecyclerAdapter.InvoiceParentHolder, InvoicesRecyclerAdapter.InvoiceChildHolder> {


    LayoutInflater inflater;
    public static DateFormat dateFinal = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
    Context ctx;

    public InvoicesRecyclerAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        inflater = LayoutInflater.from(context);
        this.ctx = context;
    }

    @Override
    public InvoiceParentHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = inflater.inflate(R.layout.invoice_parent_layout, parentViewGroup, false);
        return new InvoiceParentHolder(view);
    }

    @Override
    public InvoiceChildHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = inflater.inflate(R.layout.invoice_child_layout, childViewGroup, false);
        return new InvoiceChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(InvoiceParentHolder parentViewHolder, int position, ParentListItem parentListItem) {
        InvoiceParent parent = (InvoiceParent) parentListItem;
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(InvoiceChildHolder childViewHolder, int position, Object childListItem) {
        InvoiceChild child = (InvoiceChild) childListItem;
        childViewHolder.bind(child);
    }

    public class InvoiceParentHolder extends ParentViewHolder {
        TextView from, to, date, cost, invoiceId, since;

        public InvoiceParentHolder(View itemView) {
            super(itemView);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            cost = (TextView) itemView.findViewById(R.id.cost);
            invoiceId = (TextView) itemView.findViewById(R.id.invoiceId);
            since = (TextView) itemView.findViewById(R.id.since);
        }

        public void bind(InvoiceParent invoiceParent) {
            from.setText(invoiceParent.getFrom());
            to.setText(invoiceParent.getTo());
            date.setText(dateFinal.format(new Date(invoiceParent.getDate())));
            cost.setText(invoiceParent.getCost());
            invoiceId.setText(invoiceParent.getInvoiceId());
            since.setText(invoiceParent.getSince());
        }
    }

    public class InvoiceChildHolder extends ChildViewHolder {

        TextView total, amount, itemName, quantity;
//        CustomListView itemList;
        Button viewOrder, detailed;

        public InvoiceChildHolder(View itemView) {
            super(itemView);
            total = (TextView) itemView.findViewById(R.id.total);
            amount = (TextView) itemView.findViewById(R.id.amount);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            //  itemList = (CustomListView) itemView.findViewById(R.id.itemList);
            viewOrder = (Button) itemView.findViewById(R.id.viewOrder);
            detailed = (Button) itemView.findViewById(R.id.detailed);
        }

        public void bind(final InvoiceChild invoiceChild) {
            total.setText(invoiceChild.getAmount());
            amount.setText(invoiceChild.getAmount());
            itemName.setText(invoiceChild.getItemName());
            quantity.setText(invoiceChild.getQuantity());

            viewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showViewOrderDialog(invoiceChild.getHolder());
                }
            });
            detailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceChild.getLink())));
                }
            });
        }
    }

    public void showViewOrderDialog(NewOrderHolder holder) {

        View v = LayoutInflater.from(ctx).inflate(R.layout.new_order_fragment, null, false);
        ((TextView)v.findViewById(R.id.owner_name)).setText(holder.getShipperName());
        ((TextView)v.findViewById(R.id.source)).setText(holder.getSource());
        ((TextView)v.findViewById(R.id.destination)).setText(holder.getDestination());
        ((TextView)v.findViewById(R.id.truck_type)).setText(holder.getTruckType());
        ((TextView)v.findViewById(R.id.num_truck)).setText("X "+holder.getNumTrucks());
        ((TextView)v.findViewById(R.id.material_type)).setText(holder.getMaterialType());
//        ((TextView)v.findViewById(R.id.quantity)).setText(holder.getConsignmentWeight());
        ((TextView)v.findViewById(R.id.payment_detail)).setText(holder.getPaymentDetail());

        AlertDialog.Builder builder= new AlertDialog.Builder(ctx);
        builder.setView(v)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
