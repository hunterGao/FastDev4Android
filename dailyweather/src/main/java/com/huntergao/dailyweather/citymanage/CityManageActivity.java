package com.huntergao.dailyweather.citymanage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.citysearch.CitySearchActivity;

/**
 * Created by HG on 2017/10/24.
 */

public class CityManageActivity extends Activity implements View.OnClickListener{

    private CoordinatorLayout rootView;
    private RecyclerView recyclerView;
    private ImageView editIV;
    private ImageView backTV;
    private TextView titleTV;
    private TextView cancelTV;
    private TextView confirmTV;
    private boolean isEdit;
    private CityManageAdapter adapter;

    public static void start(Context context) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, CityManageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        rootView = (CoordinatorLayout) findViewById(R.id.city_manage_root);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.city_manage_fab);
        fab.setOnClickListener(this);
        editIV = (ImageView) findViewById(R.id.city_manage_tb_edit);
        editIV.setOnClickListener(this);
        backTV = (ImageView) findViewById(R.id.city_manage_tb_back);
        backTV.setOnClickListener(this);
        titleTV = (TextView) findViewById(R.id.city_manage_tb_title);
        cancelTV = (TextView) findViewById(R.id.city_manage_tb_cancel);
        cancelTV.setOnClickListener(this);
        confirmTV = (TextView) findViewById(R.id.city_manage_tb_confirm);
        confirmTV.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.city_manage_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CityManageAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_manage_tb_edit:
                edit();
                break;
            case R.id.city_manage_tb_back:
                finish();
                break;
            case R.id.city_manage_tb_cancel:
                if (adapter.isDelete()) {
                    Snackbar.make(rootView, "是否舍弃修改", Snackbar.LENGTH_INDEFINITE)
                            .setAction("舍弃", new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    edit();
                                    adapter.reset();
                                }
                            }).show();
                } else {
                    edit();
                }
                break;
            case R.id.city_manage_tb_confirm:
                edit();
                break;
            case R.id.city_manage_fab:
                Intent intent = new Intent(CityManageActivity.this, CitySearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void edit() {
        if (isEdit) {
            isEdit = false;
            editIV.setVisibility(View.VISIBLE);
            backTV.setVisibility(View.VISIBLE);
            adapter.setDelete(false);
            adapter.delete();
            Toolbar.LayoutParams params = (Toolbar.LayoutParams) titleTV.getLayoutParams();
            params.gravity = Gravity.NO_GRAVITY;
            titleTV.setLayoutParams(params);
            cancelTV.setVisibility(View.GONE);
            confirmTV.setVisibility(View.GONE);
        } else {
            isEdit = true;
            editIV.setVisibility(View.GONE);
            backTV.setVisibility(View.GONE);
            adapter.setDelete(true);
            Toolbar.LayoutParams params = (Toolbar.LayoutParams) titleTV.getLayoutParams();
            params.gravity = Gravity.CENTER;
            titleTV.setLayoutParams(params);
            cancelTV.setVisibility(View.VISIBLE);
            confirmTV.setVisibility(View.VISIBLE);
        }
    }
}
