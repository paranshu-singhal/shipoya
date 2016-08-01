package com.android.shipoya.shipoya2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        TextView faq = (TextView) findViewById(R.id.faq);
        faq.setText(Html.fromHtml(readHtml()));
    }

    public String readHtml() {
        try {
            InputStream inputStream = getAssets().open("faq.html");
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