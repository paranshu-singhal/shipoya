package com.android.shipoya.shipoya2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class MakeRequest extends AsyncTask<Void, Void, String> {

    private Context ctx;
    private String url, method;
    private JSONObject container;
    private boolean display;
    private ProgressDialog dialog;
    private Interface i;
    HttpsURLConnection conn;
    private static final String logTag = "logTag";

    public MakeRequest(Context ctx, String url, String method, boolean display, JSONObject container, Interface i) {
        this.ctx = ctx;
        this.url = url;
        this.method = method;
        this.container = container;
        this.display = display;
        this.i = i;
    }

    @Override
    protected void onPreExecute() {
        if (display) {
            dialog = ProgressDialog.show(ctx, "", ctx.getResources().getString(R.string.loading_wait));
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        StringBuilder buffer = null;
        try {
            conn = (HttpsURLConnection) (new URL("https://www.shipoya.com" + url)).openConnection();

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            if (sharedPref.getStringSet("cookies", null) != null && !url.equals("/auth_resources/api/user_login")) {
                conn.setRequestProperty("Cookie", TextUtils.join(";", sharedPref.getStringSet("cookies", null)));
            }
            conn.connect();

            if (method.equals("POST")) {
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

            SharedPreferences.Editor editor = sharedPref.edit();

            List<String> cookiesHeader = conn.getHeaderFields().get("Set-Cookie");
            if (cookiesHeader != null) {
                Set<String> foo = new HashSet<>(cookiesHeader);
                editor.putStringSet("cookies", foo).apply();
            }
            is.close();
            conn.disconnect();

        } catch (Throwable t) {
            if (display) {
                dialog.cancel();
            }
            Log.d(logTag, "MakeRequestException " + t.getMessage());
            cancel(true);
        }
        return (buffer != null) ? buffer.toString() : "";
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(logTag, s);
        i.onPostExecute(s);
        if (display) dialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        try {
            if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST && url.equals("/auth_resources/api/user_login")) {
                Toast.makeText(ctx, "User login not successful. Please check your phone number or password.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ctx, ctx.getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();
            }
        } catch (SocketTimeoutException t) {
            Toast.makeText(ctx, ctx.getResources().getString(R.string.check_conn), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.d(logTag, "onCancelled " + e.getMessage());
        }
    }
}






