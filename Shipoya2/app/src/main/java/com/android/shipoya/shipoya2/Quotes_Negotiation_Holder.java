package com.android.shipoya.shipoya2;

import android.os.Parcel;
import android.os.Parcelable;

public class Quotes_Negotiation_Holder implements Parcelable{

    private String carr_name, money, status, img_link, bid_id, order_id;
    private int rating;

    public Quotes_Negotiation_Holder(String carr_name, String money, String status, String img_link, int rating, String bid_id, String order_id) {
        this.carr_name = carr_name;
        this.money = money;
        this.status = status;
        this.img_link = img_link;
        this.rating = rating;
        this.bid_id=bid_id;
        this.order_id = order_id;
    }

    public Quotes_Negotiation_Holder(Parcel in) {
        this.carr_name = in.readString();
        this.money = in.readString();
        this.status = in.readString();
        this.img_link = in.readString();
        this.rating = in.readInt();
        this.bid_id = in.readString();
        this.order_id = in.readString();
    }

    public String getCarr_name() {
        return carr_name;
    }

    public String getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }

    public String getImg_link() {
        return img_link;
    }

    public int getRating() {
        return rating;
    }

    public String getBid_id() {
        return bid_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(carr_name);
        dest.writeString(money);
        dest.writeString(status);
        dest.writeString(img_link);
        dest.writeInt(rating);
        dest.writeString(bid_id);
        dest.writeString(order_id);
    }

    public static final Parcelable.Creator<Quotes_Negotiation_Holder> CREATOR = new Parcelable.Creator<Quotes_Negotiation_Holder>(){
        @Override
        public Quotes_Negotiation_Holder[] newArray(int size) {
            return new Quotes_Negotiation_Holder[size];
        }

        @Override
        public Quotes_Negotiation_Holder createFromParcel(Parcel source) {
            return new Quotes_Negotiation_Holder(source);
        }
    };
}
