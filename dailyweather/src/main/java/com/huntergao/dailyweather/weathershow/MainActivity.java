package com.huntergao.dailyweather.weathershow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherApplication;
import com.huntergao.dailyweather.citymanage.CityManageActivity;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.util.AndroidUtils;
import com.huntergao.dailyweather.view.CountDownView;
import com.huntergao.dailyweather.view.ViewPagerIndicator;

import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String POSITION = "position";
    private List<City> cityList = WeatherApplication.cityList;
    //	public static final String ADDRESS = "http://wthrcdn.etouch.cn/weather_mini?citykey=101020800";
    private ViewPager weatherViewPager;
    private WeatherPagerAdapter pageAdapter;
    private ImageView slideMenuBtn;
    private CountDownView countDownView;
    private ViewPagerIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getIntent().getIntExtra(POSITION, 0);
        Log.e(TAG, "onCreate: position = " + position);
        setContentView(R.layout.activity_main);
        initView();

        if (!AndroidUtils.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || !AndroidUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || cityList.isEmpty()) {
            countDownView.cancelCountDown(1);
            return;
        }

        countDownView.cancelCountDown(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int position = getIntent().getIntExtra(POSITION, 0);
        if (!cityList.isEmpty()) {
            pageAdapter.notifyDataSetChanged();
            weatherViewPager.setCurrentItem(position);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        slideMenuBtn = (ImageView) findViewById(R.id.main_activity_side_btn);
        slideMenuBtn.setOnClickListener(this);
        // ViewPager的设置
        weatherViewPager = (ViewPager) findViewById(R.id.main_activity_vp);
        pageAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        weatherViewPager.setAdapter(pageAdapter);
        countDownView = (CountDownView) findViewById(R.id.main_activity_countdown);
        countDownView.setErrorClickListener(new CountDownView.OnErrorClickListener() {
            @Override
            public void onErrorClick(int type) {
                if (type == 1) {
                    CityManageActivity.start(MainActivity.this);
                }
            }
        });
        indicator = (ViewPagerIndicator) findViewById(R.id.main_activity_indicator);
        indicator.setViewPager(weatherViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_side_btn:
                CityManageActivity.start(this);
                break;
        }
    }

    public static void start(Context context, int position) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(POSITION, position);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private class WeatherPagerAdapter extends FragmentStatePagerAdapter {

        public WeatherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            City city = cityList.get(position);
            return MainFragment.newInstance(city);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return cityList.get(position).getName();
        }

    }

    private class WeatherPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {

        }
    }
}
