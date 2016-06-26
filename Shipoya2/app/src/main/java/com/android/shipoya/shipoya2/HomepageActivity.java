package com.android.shipoya.shipoya2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;

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
        findViewById(R.id.viewQuotes).setOnClickListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.getTruck:
                i = new Intent(HomepageActivity.this, GetTruckActivity.class);
                break;
            case R.id.viewInvoices:
                i = new Intent(HomepageActivity.this, ViewInvoiceActivity.class);
                break;
            case R.id.viewOrders:
                i = new Intent(HomepageActivity.this, ViewOrdersActivity.class);
                break;
            case R.id.viewQuotes:
                i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
                break;
        }
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        if (item.getItemId() != R.id.item1) {
            Intent i = null;
            switch (item.getItemId()) {
                case R.id.item1:
                    break;
                case R.id.item2:
                    i = new Intent(HomepageActivity.this, GetTruckActivity.class);
                    break;
                case R.id.item3:
                    i = new Intent(HomepageActivity.this, ViewOrdersActivity.class);
                    break;
                case R.id.item4:
                    i = new Intent(HomepageActivity.this, ViewQuotesActivity.class);
                    break;
                case R.id.item5:
                    i = new Intent(HomepageActivity.this, ViewInvoiceActivity.class);
                    break;
                case R.id.item6:
                    i = new Intent(HomepageActivity.this, ContactUsActivity.class);
                    break;
                case R.id.item7:
                    break;
                case R.id.item8:
                    break;
            }
            startActivity(i);
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
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }
}

