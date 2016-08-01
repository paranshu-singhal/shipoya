package com.android.shipoya.shipoya2;

public class QuoteParent {


    private String from, to, orderId, date;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public String getOrderId() {
        return orderId;
    }

    public QuoteParent(String from, String to, String orderId, String date) {
        this.from = from;
        this.to = to;
        this.orderId = orderId;
        this.date = date;

    }
}
