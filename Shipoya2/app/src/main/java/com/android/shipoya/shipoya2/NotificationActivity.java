package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        notificationList = (RecyclerView) findViewById(R.id.recyclerview);
        setUpData();
    }

    public void setUpData() {
        (new MakeRequest(
                this,
                "/user_resources/api/notifications",
                "GET",
                true,
                null,
                new Interface() {
                    @Override
                    public void onPostExecute(String response) {
                        try {
                            List<NotificationModel> notifications = new ArrayList<>();
                            JSONArray notificationArray = (new JSONObject(response)).getJSONArray("data");
                            for (int i = 0; i < notificationArray.length(); i++) {
                                notifications.add(new NotificationModel(
                                        notificationArray.getJSONObject(i).getString("title"),
                                        notificationArray.getJSONObject(i).getString("text"),
                                        getTime(notificationArray.getJSONObject(i).getString("timestamp")),
                                        notificationArray.getJSONObject(i).getString("notification_id"),
                                        notificationArray.getJSONObject(i).getBoolean("viewed_flag")
                                ));
                                getTime(notificationArray.getJSONObject(i).getString("timestamp"));
                            }

                            Collections.sort(notifications);
                            notificationList.setAdapter(new NotificationsRecyclerAdapter(NotificationActivity.this, notifications));
                            notificationList.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                            notificationList.setHasFixedSize(true);
                        } catch (Exception e) {
                            Log.d("NOTIFICATION", e.toString());
                        }
                    }
                }

        )).execute();
    }

    public long getTime(String timeStamp) {
        timeStamp = timeStamp.replace("T", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss");
        try {
            return dateFormat.parse(timeStamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
