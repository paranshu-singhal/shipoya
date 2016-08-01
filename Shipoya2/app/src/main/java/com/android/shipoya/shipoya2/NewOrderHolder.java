package com.android.shipoya.shipoya2;

import android.os.Parcel;
import android.os.Parcelable;

public class NewOrderHolder implements Parcelable {

    String source, shipperName, destination, truckType, materialType, paymentDetail, consignmentWeight, numTrucks;
    boolean show;

    public NewOrderHolder(String source, String shipperName, String destination, String truckType, String materialType, String paymentDetail, String consignmentWeight, String numTrucks, boolean show) {
        this.source = source;
        this.shipperName = shipperName;
        this.destination = destination;
        this.truckType = truckType;
        this.materialType = materialType;
        this.paymentDetail = paymentDetail;
        this.consignmentWeight = consignmentWeight;
        this.numTrucks = numTrucks;
        this.show = show;
    }

    public NewOrderHolder(Parcel in) {
        source = in.readString();
        shipperName = in.readString();
        destination = in.readString();
        truckType = in.readString();
        materialType = in.readString();
        paymentDetail = in.readString();
        consignmentWeight = in.readString();
        numTrucks = in.readString();

    }

    public static final Creator<NewOrderHolder> CREATOR = new Creator<NewOrderHolder>() {
        @Override
        public NewOrderHolder createFromParcel(Parcel in) {
            return new NewOrderHolder(in);
        }

        @Override
        public NewOrderHolder[] newArray(int size) {
            return new NewOrderHolder[size];
        }
    };

    public String getSource() {
        return source;
    }

    public String getShipperName() {
        return shipperName;
    }

    public String getDestination() {
        return destination;
    }

    public String getTruckType() {
        return truckType;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public String getConsignmentWeight() {
        return consignmentWeight;
    }

    public String getNumTrucks() {
        return numTrucks;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
        dest.writeString(shipperName);
        dest.writeString(destination);
        dest.writeString(truckType);
        dest.writeString(materialType);
        dest.writeString(paymentDetail);
        dest.writeString(consignmentWeight);
        dest.writeString(numTrucks);

    }

    public boolean isShow() {
        return show;
    }
}
