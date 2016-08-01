package com.android.shipoya.shipoya2;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class HomepageActivity extends AppCompatActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;

    private final String logTag = "logTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        boundViews();
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        findViewById(R.id.getTruck).setOnClickListener(this);
        findViewById(R.id.viewInvoices).setOnClickListener(this);
        findViewById(R.id.viewOrders).setOnClickListener(this);
        //findViewById(R.id.viewQuotes).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        //findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ((TextView) findViewById(R.id.carr_name_home)).setText(sharedPref.getString("full_name", null));
        ((TextView) findViewById(R.id.carr_type_home)).setText(sharedPref.getString("entity_type", null).toUpperCase());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_carrName_navHeader)).setText(sharedPref.getString("full_name", null));
        StaticData.imageLoader.displayImage(sharedPref.getString("image_url", null), (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile));

        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
            }
        });
    }

    public void boundViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        ComponentName componentName = new ComponentName(HomepageActivity.this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                showNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNotification(){
        Intent i = new Intent(this, NotificationActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.getTruck:
                i = new Intent(HomepageActivity.this, GetTruckActivity.class);
                break;
            case R.id.button1:
                i = new Intent(HomepageActivity.this, GetTruckActivity.class);
                break;
            case R.id.viewInvoices:
                i = new Intent(HomepageActivity.this, ViewInvoiceActivity.class);
                break;
            case R.id.button4:
                i = new Intent(HomepageActivity.this, ViewInvoiceActivity.class);
                break;
            case R.id.viewOrders:
                i = new Intent(HomepageActivity.this, ViewOrdersActivity.class);
                break;
            case R.id.button3:
                i = new Intent(HomepageActivity.this, ViewOrdersActivity.class);
                break;
            case R.id.viewQuotes:
                i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
                break;
            case R.id.button2:
                i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
                break;
        }
        startActivity(i);
    }

    public void onClickQuotes(View v){
        Intent i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);

        Intent i;
        switch (item.getItemId()) {

            case R.id.item1:
                i = new Intent(HomepageActivity.this, AboutUs.class);
                startActivity(i);
                break;
            case R.id.item2:
                i = new Intent(HomepageActivity.this, GetTruckActivity.class);
                startActivity(i);
                break;
            case R.id.item3:
                i = new Intent(HomepageActivity.this, ViewOrdersActivity.class);
                startActivity(i);
                break;
            case R.id.item4:
                i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
                startActivity(i);
                break;
            case R.id.item5:
                i = new Intent(HomepageActivity.this, ViewInvoiceActivity.class);
                startActivity(i);
                break;
            case R.id.item6:
                i = new Intent(HomepageActivity.this, ContactUs.class);
                startActivity(i);
                break;
            case R.id.item7:
                i = new Intent(HomepageActivity.this, FAQActivity.class);
                startActivity(i);
                break;
            case R.id.item8:
                logout();
                break;
            case R.id.item9:
                i = new Intent(HomepageActivity.this, PrivacyActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void logout() {
        try {
            final SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            final SharedPreferences preferences1 = getSharedPreferences("cookie", Context.MODE_PRIVATE);

            (new MakeRequest(
                    this,
                    "/auth_resources/api/android_logout/device_id/" + URLEncoder.encode(preferences.getString("reg_id", null), "UTF-8"),
                    "GET",
                    true,
                    null,
                    new Interface() {
                        @Override
                        public void onPostExecute(String response) {
                            preferences.edit().clear().apply();
                            preferences1.edit().clear().apply();
                            Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
            )).execute();
        }
        catch (Throwable t){
            Log.d(logTag,"logout: "+t.getMessage());
        }
    }
}

