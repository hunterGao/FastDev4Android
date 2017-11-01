package com.huntergao.dailyweather;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.huntergao.dailyweather.citylocation.LocationCity;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.db.CityDB;
import com.huntergao.dailyweather.util.AndroidUtils;
import com.huntergao.dailyweather.weathershow.MainActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by HG on 2017/10/31.
 */

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private static final String LAST_START = "lastStart";
    private TextView textView;
    private PermissionUtil.PermissionObject permissionObject;
    private PermissionUtil.PermissionRequestObject permissionRequest;
    private Handler handler = new Handler();
    private List<City> cityList = WeatherApplication.cityList;
    private Map<City, Integer> cityMap = WeatherApplication.cityPositionMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = (TextView) findViewById(R.id.splash_version);
        textView.setText(AndroidUtils.getVersionCode(this));
        permissionObject = PermissionUtil.with(this);
        boolean hasLocationPermission = permissionObject.has(Manifest.permission.ACCESS_FINE_LOCATION) &&
                permissionObject.has(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!hasLocationPermission) {
            permissionRequest = permissionObject.request(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionRequest.onAnyDenied(new Func() {
                @Override
                protected void call() {
                    MainActivity.start(SplashActivity.this);
                    finish();
                }
            }).onAllGranted(new Func() {
                @Override
                protected void call() {
                    location();
                }
            }).ask(1);
        } else {
            location();
        }
    }

    private void location() {
        LocationCity location = new LocationCity(SplashActivity.this, new LocationCity.LocationListener() {
            @Override
            public void onFail() {
                Log.e(TAG, "onFail: ");
                jump();
            }

            @Override
            public void onSuccess(String cityName) {
                Log.e(TAG, "onSuccess name = " + cityName);
                City city = CityDB.getInstance(SplashActivity.this).readCityByName(cityName);
                if (city == null) {
                    Log.e(TAG, "onSuccess: city is null");
                    jump();
                    return;
                }

                if (!cityList.contains(city)) {
                    cityList.add(city);
                    cityMap.put(city, 0);
                } else {
                    if (!cityList.get(0).equals(city)) {
                        cityList.remove(city);
                        cityList.add(0, city);
                    }
                }
                Log.e(TAG, "onSuccess: cityList.size = " + cityList.size());
                jump();
            }
        });
        location.startLocation();
    }

    private void jump() {
        MainActivity.start(SplashActivity.this);
        finish();
    }

    private void delayJump() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(SplashActivity.this);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionRequest != null) {
            permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
