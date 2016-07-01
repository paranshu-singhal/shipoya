package com.android.shipoya.shipoya2;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class ViewOrdersChildHolderClass {

    public static class ParentHolderClass implements ParentListItem{

        private String plate_number, status, image_link;
        private int rating;
        private List<ChildHolderClass> childList;

        public ParentHolderClass(String plate_number, String status, String image_link, int rating, List<ChildHolderClass> childList) {
            this.plate_number = plate_number;
            this.status = status;
            this.image_link = image_link;
            this.rating = rating;
            this.childList = childList;
        }

        public String getPlate_number() {
            return plate_number;
        }

        public String getStatus() {
            return status;
        }

        public String getImage_link() {
            return image_link;
        }

        public int getRating() {
            return rating;
        }

        @Override
        public List<?> getChildItemList() {
            return childList;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    public static class ChildHolderClass{
        private String eta, curr_location;
        private Long startTime;

        public ChildHolderClass(Long startTime, String eta, String curr_location) {
            this.startTime = startTime;
            this.eta = eta;
            this.curr_location = curr_location;
        }

        public Long getStartTime() {
            return startTime;
        }

        public String getEta() {
            return eta;
        }

        public String getCurr_location() {
            return curr_location;
        }
    }
}
