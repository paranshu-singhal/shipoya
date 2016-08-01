package com.android.shipoya.shipoya2;

public class InvoiceChild {

    String itemName, quantity, amount, link;
    NewOrderHolder holder;

    public InvoiceChild(String itemName, String quantity, String amount, NewOrderHolder holder, String link) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.amount = amount;
        this.holder = holder;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getItemName() {
        return itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getAmount() {
        return amount;
    }

    public NewOrderHolder getHolder() {
        return holder;
    }
}
