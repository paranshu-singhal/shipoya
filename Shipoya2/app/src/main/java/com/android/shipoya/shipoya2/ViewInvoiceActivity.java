package com.android.shipoya.shipoya2;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewInvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new InvoicesPagerAdapter(this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

//        List<InvoiceChild> invoiceChildren = new ArrayList<>();
//        InvoiceChild invoiceChild = new InvoiceChild();
//        invoiceChildren.add(invoiceChild);
//
//        InvoiceParent parent= new InvoiceParent("DEL","MUM","since","date","invoice","cost",invoiceChildren);
//        List<InvoiceParent> parents = Arrays.asList(parent,parent);
//
//        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewInvoices);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        InvoicesRecyclerAdapter adapter = new InvoicesRecyclerAdapter(this, parents);
//        mRecyclerView.setAdapter(adapter);

    }
}
