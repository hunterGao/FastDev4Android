package com.huntergao.hunterweather.weathershow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.huntergao.hunterweather.db.City;
import com.huntergao.hunterweather.db.SelectCityDB;
import com.huntergao.hunterweather.citymanage.ManageCityActivity;
import com.huntergao.hunterweather.view.WeatherTypefacedTextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnClickListener{

    protected static final String TAG = "HunterWeather";
    //	public static final String ADDRESS = "http://wthrcdn.etouch.cn/weather_mini?citykey=101020800";
    public static final String ADDRESS = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";
    private ViewPager mWeatherViewPager = null;
    private WeatherPagerAdapter mAdapter = null;
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
        mAdapter = new WeatherPagerAdapter(this);
        mAdapter.addAllItems(mCityList);
        mWeatherViewPager.setOnPageChangeListener(new WeatherPageChangeListener());
        mWeatherViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCityList = SelectCityDB.getInstance(this).readAllCity();
        mAdapter.addAllItems(mCityList);
        if(mCityList.size() != 0){
            mNoSelectView.setVisibility(View.GONE);
            mTitleView.setText(mCityList.get(0).getName());
            mWeatherViewPager.setCurrentItem(0);
        }else{
            mNoSelectView.setVisibility(View.VISIBLE);
            mTitleView.setText("--");
        }
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

    class WeatherPageChangeListener implements OnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            if(mCityList.size() > position){
                mTitleView.setText(mCityList.get(position).getName());
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
