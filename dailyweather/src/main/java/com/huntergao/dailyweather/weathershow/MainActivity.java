package com.huntergao.dailyweather.weathershow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.citymanage.ManageCityActivity;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.db.SelectCityDB;
import com.huntergao.dailyweather.view.WeatherTypefacedTextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnClickListener{

    protected static final String TAG = "HunterWeather";
    //	public static final String ADDRESS = "http://wthrcdn.etouch.cn/weather_mini?citykey=101020800";
    public static final String ADDRESS = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";
    private ViewPager mWeatherViewPager = null;
    private List<City> mCityList = new ArrayList<City>();

    private WeatherTypefacedTextView mTitleView = null;
    private WeatherTypefacedTextView mNoSelectView = null;

    private ImageView slideMenuBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //从数据库中读取已经选择的城市
        mCityList = SelectCityDB.getInstance(this).readAllCity();
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //标题
        mTitleView = (WeatherTypefacedTextView) findViewById(R.id.location_city_textview);
        mNoSelectView = (WeatherTypefacedTextView) findViewById(R.id.no_select_city);
        if(mCityList.size() > 0){
            mTitleView.setText(mCityList.get(0).getName());
        }
        //左侧滑动菜单按钮
        slideMenuBtn = (ImageView) findViewById(R.id.slidebarButton);
        slideMenuBtn.setOnClickListener(this);
        //ViewPager的设置
        mWeatherViewPager = (ViewPager) findViewById(R.id.city_weather_info_vp);
        WeatherPagerAdapter mAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        mWeatherViewPager.setOnPageChangeListener(new WeatherPageChangeListener());
        mWeatherViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.slidebarButton:
                Intent intent = new Intent(MainActivity.this, ManageCityActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class WeatherPagerAdapter extends FragmentStatePagerAdapter {

        public WeatherPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MainFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "beijing";
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
