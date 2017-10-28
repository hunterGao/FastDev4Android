package com.huntergao.dailyweather.weathershow;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity implements OnClickListener{

    private static final String TAG = "MainActivity";
    private static final String POSITION = "position";
    private Map<City, Integer> cityMap = WeatherApplication.cityPositionMap;
    private List<City> cityList = WeatherApplication.cityList;
    //	public static final String ADDRESS = "http://wthrcdn.etouch.cn/weather_mini?citykey=101020800";
    private ViewPager weatherViewPager;
    private WeatherPagerAdapter pageAdapter;
    private ImageView slideMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getIntent().getIntExtra(POSITION, 0);
        Log.e(TAG, "onCreate: position = " + position);
        setContentView(R.layout.activity_main);
        //从数据库中读取已经选择的城市
//        mCityList = SelectCityDB.getInstance(this).readAllCity();
        cityList.add(new City("北京", "101010100", false));
        cityList.add(new City("海淀", "101010200", false));
        for (int i = 0; i < cityList.size(); i++) {
            cityMap.put(cityList.get(i), i);
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int position = getIntent().getIntExtra(POSITION, 0);
        Log.e(TAG, "onResume: position = " + position);
        pageAdapter.notifyDataSetChanged();
        weatherViewPager.setCurrentItem(position);
    }

    /**
     * 初始化View
     */
    private void initView() {
        slideMenuBtn = (ImageView) findViewById(R.id.sidebarButton);
        slideMenuBtn.setOnClickListener(this);
        // ViewPager的设置
        weatherViewPager = (ViewPager) findViewById(R.id.city_weather_info_vp);
        pageAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        weatherViewPager.setOnPageChangeListener(new WeatherPageChangeListener());
        weatherViewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sidebarButton:
                Intent intent = new Intent(MainActivity.this, CityManageActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static void jump(Context context, int position) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(POSITION, position);
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

    private class WeatherPageChangeListener implements OnPageChangeListener{

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
