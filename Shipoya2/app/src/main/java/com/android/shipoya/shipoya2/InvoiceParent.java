package com.android.shipoya.shipoya2;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by aaaa on 6/13/2016.
 */

public class InvoiceParent implements ParentListItem {


    String from, to, since, invoiceId, cost, date;
    List<InvoiceChild> childList;

    @Override
    public List<?> getChildItemList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSince() {
        return since;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public InvoiceParent(String from, String to, String since, String invoiceId, String cost, String date, List<InvoiceChild> childList) {
        this.from = from;
        this.to = to;
        this.since = since;
        this.invoiceId = invoiceId;
        this.cost = cost;
        this.date = date;
        this.childList=childList;
    }
}
