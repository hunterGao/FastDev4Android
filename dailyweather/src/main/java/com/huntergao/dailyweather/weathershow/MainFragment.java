package com.huntergao.dailyweather.weathershow;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjj.refresh.BeautifulRefreshLayout;
import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherApplication;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.util.WeatherIconUtils;
import com.huntergao.dailyweather.xml.XmlParserUtil;
import com.huntergao.dailyweather.xml.model.ForecastWeatherInfo;
import com.huntergao.dailyweather.xml.model.WeatherInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HG on 2017/10/19.
 */

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private static final String CITY = "city";

    private City city;
    private Handler handler = new Handler();
    private WeatherRecyclerViewAdapter adapter;
    private ImageView normalImageView;
    private ImageView blurredImageView;
    private int sum = 0;

    public static MainFragment newInstance(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city can not be null");
        }
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CITY, city);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        city = bundle.getParcelable(CITY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        normalImageView = (ImageView) rootView.findViewById(R.id.weather_background);
        blurredImageView = (ImageView) rootView.findViewById(R.id.weather_background_blur);
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
        adapter = new WeatherRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
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
                sum += dy;
                Log.e(TAG, "onScrolled: sum = " + sum / 15);
                if (sum / 15 > 255) {
                    blurredImageView.setAlpha(255);
                } else {
                    blurredImageView.setAlpha(sum / 15);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WeatherInfo weatherInfo = WeatherApplication.cityWeatherInfoMap.get(city);
        if (weatherInfo != null) {
            updateData(WeatherApplication.cityWeatherInfoMap.get(city));
            return;
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(WeatherApplication.ADDRESS + city.getPostID())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.e(TAG, "onResponse: code = " + code);
                final String xmlString = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        WeatherInfo info = XmlParserUtil.parseXmlWithPull(xmlString);
                        WeatherApplication.cityWeatherInfoMap.put(city, info);
                        updateData(info);
                    }
                });
            }
        });
    }

    private void updateData(WeatherInfo info) {
        adapter.setWeatherInfo(city.getName(), info);
        int sunrise = Integer.valueOf(info.getSunrise().split(":")[0]);
        int sunset = Integer.valueOf(info.getSunset().split(":")[0]);
        ForecastWeatherInfo forecast = info.getForecastList().get(0);
        String type = forecast.getDayWeather().getType();
        normalImageView.setImageResource(WeatherIconUtils
                .getWeatherNormalBg(type, sunrise, sunset));
        blurredImageView.setImageResource(WeatherIconUtils
                .getWeatherBlurBg(type, sunrise, sunset));
    }
}