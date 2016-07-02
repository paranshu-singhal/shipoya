package com.android.shipoya.shipoya2;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class MakeRequest {

    private static final String logTag = "logTag";
    public static String sendRequest(Context ctx, String url, String method, JSONObject container) {

        SharedPreferences sharedPref = ctx.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        StringBuilder buffer = null;
        try {
            HttpsURLConnection conn = (HttpsURLConnection) (new URL("https://www.shipoya.com")).openConnection();

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cookie", TextUtils.join(";", sharedPref.getStringSet("cookies", null)));
            conn.connect();

            if(method.equals("POST")) {
                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(container.toString());
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            Log.d(logTag, "MakeRequest " + String.format("code %d", conn.getResponseCode()));

            buffer = new StringBuilder();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            is.close();
            conn.disconnect();
        } catch (Throwable t) {
            Log.d(logTag, t.getMessage());
        }
        return buffer.toString();
    }
}
