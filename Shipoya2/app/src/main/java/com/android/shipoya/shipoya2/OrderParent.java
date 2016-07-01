package com.android.shipoya.shipoya2;
public class OrderParent {


    private String from, to, orderId;
    private Long date;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Long getDate() {
        return date;
    }

    public String getOrderId() {
        return orderId;
    }

    public  OrderParent(String from, String to, String orderId, Long date) {
        this.from = from;
        this.to = to;
        this.orderId = orderId;
        this.date = date;

    }
}

