package com.android.shipoya.shipoya2;

import java.util.Calendar;

public class NotificationModel implements Comparable<NotificationModel> {
    String title, text, notificationId;
    boolean viewedFlag;
    long date;

    public NotificationModel(String title, String text, long date, String notificationId, boolean viewedFlag) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.notificationId = notificationId;
        this.viewedFlag = viewedFlag;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getLongDate() {
        return date;
    }

    public String getDate() {
        String converted = "";
        Calendar calendar = Calendar.getInstance();
        long diff = calendar.getTimeInMillis() - getLongDate();
        calendar.setTimeInMillis(diff);
        if ((int) calendar.get(Calendar.DAY_OF_YEAR) != 0)
            converted += calendar.get(Calendar.DAY_OF_YEAR) + " days ";
        if ((int) calendar.get(Calendar.HOUR) != 0)
            converted += calendar.get(Calendar.HOUR) + " hours ";
        if ((int) calendar.get(Calendar.MINUTE) != 0)
            converted += calendar.get(Calendar.MINUTE) + " minutes ";
        return converted + "ago";
    }

    public String getNotificationId() {
        return notificationId;
    }

    public boolean isViewedFlag() {
        return viewedFlag;
    }

    @Override
    public int compareTo(NotificationModel another) {
        if (this.getLongDate() > another.getLongDate()) {
            return -1;
        } else {
            return 1;
        }
    }
}
