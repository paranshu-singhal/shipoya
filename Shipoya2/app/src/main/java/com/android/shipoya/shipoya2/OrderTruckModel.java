package com.android.shipoya.shipoya2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderTruckModel implements Parcelable {

    List<ViewOrdersChildHolderClass.ParentHolderClass> parents;

    public OrderTruckModel(List<ViewOrdersChildHolderClass.ParentHolderClass> parents) {
        this.parents = parents;
    }

    public List<ViewOrdersChildHolderClass.ParentHolderClass> getParents() {
        return parents;
    }

    protected OrderTruckModel(Parcel in) {
    }

    public static final Creator<OrderTruckModel> CREATOR = new Creator<OrderTruckModel>() {
        @Override
        public OrderTruckModel createFromParcel(Parcel in) {
            return new OrderTruckModel(in);
        }

        @Override
        public OrderTruckModel[] newArray(int size) {
            return new OrderTruckModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
