package com.android.shipoya.shipoya2;

import android.os.Parcel;
import android.os.Parcelable;

public class Quotes_confirmation_holder implements Parcelable{

    private String carr_name, money, pickup_date, payment_term;

    public Quotes_confirmation_holder(Parcel in) {
        this.carr_name = in.readString();
        this.money = in.readString();
        this.pickup_date = in.readString();
        this.payment_term = in.readString();
    }

    public Quotes_confirmation_holder(String carr_name, String money, String pickup_date, String payment_term) {
        this.carr_name = carr_name;
        this.money = money;
        this.pickup_date = pickup_date;
        this.payment_term = payment_term;
    }

    public String getCarr_name() {
        return carr_name;
    }

    public String getMoney() {
        return money;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public String getPayment_term() {
        return payment_term;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(carr_name);
        dest.writeString(money);
        dest.writeString(pickup_date);
        dest.writeString(payment_term);
    }

    public static final Parcelable.Creator<Quotes_confirmation_holder> CREATOR = new Parcelable.Creator<Quotes_confirmation_holder>() {
        @Override
        public Quotes_confirmation_holder createFromParcel(Parcel source) {
            return new Quotes_confirmation_holder(source);
        }

        @Override
        public Quotes_confirmation_holder[] newArray(int size) {
            return new Quotes_confirmation_holder[size];
        }
    };
}
