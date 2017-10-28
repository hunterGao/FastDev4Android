package com.huntergao.dailyweather.citysearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherApplication;
import com.huntergao.dailyweather.db.City;

import java.util.List;

/**
 * Created by HG on 2017/10/27.
 */

public class CitySearchHotAdapter extends RecyclerView.Adapter<CitySearchHotAdapter.ViewHolder> {

    private Context context;
    private List<City> cityItems;
    private LayoutInflater inflater;

    public CitySearchHotAdapter(Context context, List<City> cityItems) {
        this.cityItems = cityItems;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CitySearchHotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.city_search_hot_item, parent, false);
        return new CitySearchHotAdapter.ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final CitySearchHotAdapter.ViewHolder holder, int position) {
        final City city = cityItems.get(position);
        if (city != null && !TextUtils.isEmpty(city.getName())) {
            if (WeatherApplication.cityList.contains(city)) {
                holder.cityTV.setTextColor(context.getResources().getColor(R.color.holo_blue_light));
            }
            holder.cityTV.setText(city.getName());
            holder.cityTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeatherApplication.jumpMainActivity(context, city);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cityItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView cityTV;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTV = (TextView) itemView.findViewById(R.id.city_search_hot_item_tv);
        }
    }
}
