package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ((TextView)(findViewById(R.id.title_faq))).setText(getResources().getString(R.string.privacy_policy_title));
        TextView faq = (TextView) findViewById(R.id.faq);
        faq.setText(Html.fromHtml(readHtml()));
        faq.setGravity(Gravity.CENTER);
    }

    public String readHtml() {
        try {
            InputStream inputStream = getAssets().open("privacy.html");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int i;
            i = inputStream.read();
            while (i != -1) {
                outputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
            return outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }
}