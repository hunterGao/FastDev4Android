package com.huntergao.dailyweather.citymanage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherApplication;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.util.WeatherIconUtils;
import com.huntergao.dailyweather.weathershow.MainActivity;
import com.huntergao.dailyweather.xml.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by HG on 2017/10/24.
 */

public class CityManageAdapter extends RecyclerView.Adapter<CityManageAdapter.ViewHolder> {
    private static final String TAG = "CityManageAdapter";

    private Context context;
    private LayoutInflater layoutInflater;
    private List<City> cityInfos;
    private boolean isDelete;
    private List<City> deleteInfos;

    public CityManageAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        cityInfos = WeatherApplication.cityList;
        deleteInfos = new ArrayList<>();
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }

    public boolean isDelete() {
        return !deleteInfos.isEmpty();
    }

    public void reset() {
        for (int i = 0; i < deleteInfos.size(); i++) {
            cityInfos.add(deleteInfos.get(i));
        }
        deleteInfos.clear();
        notifyDataSetChanged();
    }

    public void delete() {
        Log.e(TAG, "delete: ");
        if (deleteInfos.isEmpty()) {
            return;
        }

        Map<City, Integer> map = WeatherApplication.cityPositionMap;
        map.clear();
        for (int i = 0; i < cityInfos.size(); i++) {
            map.put(cityInfos.get(i), i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.city_manage_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WeatherInfo info = null;
        City city = null;
        if (position < cityInfos.size()) {
            city = cityInfos.get(position);
            info = WeatherApplication.cityWeatherInfoMap.get(city);
        }
        if (info != null) {
            String type = info.getForecastList().get(0).getDayWeather().getType();
            int sunrise = Integer.valueOf(info.getSunrise().split(":")[0]);
            int sunset = Integer.valueOf(info.getSunset().split(":")[0]);
            holder.cityTV.setText(info.getCity());
            holder.weatherIcon.setImageResource(WeatherIconUtils.getWeatherIcon(type, sunrise, sunset));
            holder.tempTV.setText(info.getWendu() + "°");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.start(context, position);
                }
            });
        }

        if (isDelete) {
            holder.deleteIV.setVisibility(View.VISIBLE);
        } else {
            holder.deleteIV.setVisibility(View.GONE);
        }

        final City finalCity = city;
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityInfos.remove(position);
                deleteInfos.add(finalCity);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView cityTV;
        protected ImageView weatherIcon;
        protected TextView tempTV;
        protected ImageView deleteIV;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTV = (TextView) itemView.findViewById(R.id.city_manage_item_city);
            weatherIcon = (ImageView) itemView.findViewById(R.id.city_manage_item_weather_icon);
            tempTV = (TextView) itemView.findViewById(R.id.city_manage_item_temp);
            deleteIV = (ImageView) itemView.findViewById(R.id.city_manage_item_delete);
        }
    }
}
