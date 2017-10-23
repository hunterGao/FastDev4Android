package com.huntergao.dailyweather.weathershow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.refresh.BeautifulRefreshLayout;
import com.huntergao.dailyweather.R;

/**
 * Created by HG on 2017/10/19.
 */

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        BeautifulRefreshLayout beautifulRefreshLayout = (BeautifulRefreshLayout) rootView.findViewById(R.id.weather_refresh);
        beautifulRefreshLayout.setBuautifulRefreshListener(new BeautifulRefreshLayout.BuautifulRefreshListener() {
            @Override
            public void onRefresh(final BeautifulRefreshLayout refreshLayout) {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                });
            }
        });

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.weather_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new WeatherRecyclerViewAdapter(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: new State = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled: dx = " + dx + " dy = " + dy);
            }
        });
        return rootView;
    }
}
