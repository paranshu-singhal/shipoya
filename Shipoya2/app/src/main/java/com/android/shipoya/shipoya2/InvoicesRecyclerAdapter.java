package com.android.shipoya.shipoya2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

/**
 * Created by aaaa on 6/13/2016.
 */

public class InvoicesRecyclerAdapter extends ExpandableRecyclerAdapter<InvoicesRecyclerAdapter.InvoiceParentHolder, InvoicesRecyclerAdapter.InvoiceChildHolder> {


    LayoutInflater inflater;

    public InvoicesRecyclerAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        inflater = LayoutInflater.from(context);
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
        InvoiceChild child =(InvoiceChild) childListItem;
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
            date.setText(invoiceParent.getDate());
            cost.setText(invoiceParent.getCost());
            invoiceId.setText(invoiceParent.getInvoiceId());
            since.setText(invoiceParent.getSince());
        }
    }

    public class InvoiceChildHolder extends ChildViewHolder {

        TextView total;
        CustomListView itemList;
        Button viewOrder, detailed;

        public InvoiceChildHolder(View itemView) {
            super(itemView);
            total = (TextView) itemView.findViewById(R.id.total);
            itemList = (CustomListView) itemView.findViewById(R.id.itemList);
            viewOrder = (Button) itemView.findViewById(R.id.viewOrder);
            detailed = (Button) itemView.findViewById(R.id.detailed);
        }

        public void bind(InvoiceChild invoiceChild) {
        }
    }
}
