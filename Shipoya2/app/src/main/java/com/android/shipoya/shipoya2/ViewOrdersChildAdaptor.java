package com.android.shipoya.shipoya2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

public class ViewOrdersChildAdaptor extends ExpandableRecyclerAdapter<ViewOrdersChildAdaptor.parentViewHolder, ViewOrdersChildAdaptor.childViewHolder> {

    LayoutInflater inflater;

    public ViewOrdersChildAdaptor(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        inflater = LayoutInflater.from(context);
    }


    public class parentViewHolder extends ParentViewHolder{

        TextView plate_number, status, rating;
        public parentViewHolder(View itemView) {
            super(itemView);
            plate_number = (TextView)itemView.findViewById(R.id.truckPlate);
            status = (TextView)itemView.findViewById(R.id.status);
            rating = (TextView)itemView.findViewById(R.id.rating);
        }

        public void bind(ViewOrdersChildHolderClass.ParentHolderClass obj){
            plate_number.setText(obj.getPlate_number());
            status.setText(obj.getStatus());
            String st="";
            for(int i=0;i<obj.getRating();i++){
                st +="*";
            }
            rating.setText(st);
        }
    }
    public class childViewHolder extends ChildViewHolder{

        TextView startTime, currLoc,eta;
        public childViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView)itemView.findViewById(R.id.startTime);
            currLoc = (TextView)itemView.findViewById(R.id.currentLocation);
            eta = (TextView)itemView.findViewById(R.id.estimatedTime);
        }
        public void bind(ViewOrdersChildHolderClass.ChildHolderClass obj){
            startTime.setText(obj.getStartTime());
            currLoc.setText(obj.getCurr_location());
            eta.setText(obj.getEta());
        }
    }



    @Override
    public parentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        return new parentViewHolder(inflater.inflate(R.layout.view_orders_child_parent, parentViewGroup, false));
    }

    @Override
    public childViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        return new childViewHolder(inflater.inflate(R.layout.view_orders_child_child, childViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(parentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        ViewOrdersChildHolderClass.ParentHolderClass parent = (ViewOrdersChildHolderClass.ParentHolderClass)parentListItem;
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(childViewHolder childViewHolder, int position, Object childListItem) {
        ViewOrdersChildHolderClass.ChildHolderClass child = (ViewOrdersChildHolderClass.ChildHolderClass)childListItem;
        childViewHolder.bind(child);
    }
}