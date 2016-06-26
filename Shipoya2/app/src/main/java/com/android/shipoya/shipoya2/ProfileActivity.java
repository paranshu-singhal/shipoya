package com.android.shipoya.shipoya2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    final int PI = 1;
    Toolbar toolbar;
    CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        boundViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                Intent chooser = Intent.createChooser(i, "Choose via");
                startActivityForResult(chooser, PI);
            }
        });
    }

    public void boundViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileImage.post(new Runnable() {
            @Override
            public void run() {
                if (requestCode == PI && data != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        profileImage.setImageBitmap(decoded);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this,getResources().getString(R.string.error_image), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
