package com.android.shipoya.shipoya2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticData {


    public static JSONObject ORDER_DATA;
    public static JSONObject INVOICE_DATA;
    public static Boolean NETWORK_AVAILABLE;
    public static boolean ORDER_CHANGED=false;

    public static String phone_regex = "^[7-9][0-9]{9}$";
    public static String otp_regex = "[0-9]{5}";
    public static String email_regex = "^[-a-z0-9~!$%^&*_=+}{'?]+(.[-a-z0-9~!$%^&*_=+}{'?]+)*@([a-z0-9_][-a-z0-9_]*(.[-a-z0-9_]+)*.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(:[0-9]{1,5})?$/i";
    public static String email_regex2 = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,6}$";


    public static final String AUTH_URL = "https://shipoya.com/auth_resources/api/user_login";
    public static final String NEGOTIATION_LIST_URL = "https://shipoya.com/auction_resources/api/shipper_negotiations";
    public static final String ORDER_LIST_URL = "https://shipoya.com/auction_resources/api/loads/shipper_loads";

    public static final String LOGOUT = "https://shipoya.com/auth_resources/api/user_logout";
    public static final String UPDATE_CONTACT = "https://shipoya.com/entity_resources/api/shippers/" + "<entityId>" + "/contact";
    public static final String UPDATE_OFFICE = "https://shipoya.com/entity_resources/api/shippers/" + "<entityId>" + "/primary_office";
    public static final String PROFILE_DATA = "https://shipoya.com/entity_resources/api/shippers/" + "<entityId>";

    public static DateFormat dateFinal = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
    public static DateFormat year = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    public static final String COOKIES_HEADER = "Set-Cookie";

    public static final ImageLoader imageLoader = ImageLoader.getInstance();

    public static final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
            .cacheOnDisk(true).resetViewBeforeLoading(true)
            .showImageForEmptyUri(R.drawable.profile_bg)
            .showImageOnFail(R.drawable.profile_bg)
            .showImageOnLoading(R.drawable.profile_bg).build();

    public static void showNetworkError(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Network Connection Error");
        builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public static boolean validateEmail(String email){
        Pattern pt = Pattern.compile(email_regex2, Pattern.CASE_INSENSITIVE);
        Matcher mch = pt.matcher(email);
        return mch.find();
    }
    public static boolean validatePhone(String phone){
        Pattern pt = Pattern.compile(phone_regex);
        Matcher mch = pt.matcher(phone);
        return mch.find();
    }
}
