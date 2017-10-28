package com.huntergao.dailyweather.citysearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.db.CityDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2017/10/26.
 */

public class CitySearchActivity extends Activity implements Filterable, TextWatcher{

    private static final String TAG = "CitySearchActivity";
    private ListView searchLV;
    private EditText editText;
    private RecyclerView recyclerView;
    private View normalV;
    private TextView errorTV;
    CitySearchListAdapter listAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        normalV = findViewById(R.id.city_search_normal);
        searchLV = (ListView) findViewById(R.id.city_search_list);
        listAdapter = new CitySearchListAdapter(this);
        searchLV.setAdapter(listAdapter);
        searchLV.setTextFilterEnabled(true);
        editText = (EditText) findViewById(R.id.city_search_edit);
        editText.addTextChangedListener(this);
        errorTV = (TextView) findViewById(R.id.city_search_error);

        recyclerView = (RecyclerView) findViewById(R.id.city_search_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setHasFixedSize(true);
        CitySearchHotAdapter hotAdapter = new CitySearchHotAdapter(this, CityDB.getInstance(this).readHotCity());
        recyclerView.setAdapter(hotAdapter);
    }

    @Override
    public android.widget.Filter getFilter() {
        android.widget.Filter filter = new android.widget.Filter() {
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                List<City> cities = (ArrayList<City>) results.values;
                if (cities != null && !cities.isEmpty()) {
                    if (searchLV.getVisibility() == View.GONE) {
                        searchLV.setVisibility(View.VISIBLE);
                    }
                    if (errorTV.getVisibility() == View.VISIBLE) {
                        errorTV.setVisibility(View.GONE);
                    }
                    listAdapter.setResult(cities);
                } else {
                    if (searchLV.getVisibility() == View.VISIBLE) {
                        searchLV.setVisibility(View.GONE);
                    }
                    if (errorTV.getVisibility() == View.GONE) {
                        errorTV.setVisibility(View.VISIBLE);
                    }
                }
            }

            protected FilterResults performFiltering(CharSequence s) {

                FilterResults results = new FilterResults();
                List<City> queryResultCities = new ArrayList<City>();

                if (TextUtils.isEmpty(s))
                    return results;

                String str = s.toString();
                if (str.matches("[0-9]+")) {
                    queryResultCities = CityDB.getInstance(CitySearchActivity.this).readCityListByCode(str);
                } else if(str.matches("[a-zA-Z]+")) {
                    queryResultCities = CityDB.getInstance(CitySearchActivity.this).readCityListByPinyin(str);
                } else if(str.matches("[\\u4e00-\\u9fa5]+")) { //中文
                    queryResultCities = CityDB.getInstance(CitySearchActivity.this).readCityListByName(str);
                }

                results.values = queryResultCities;
                results.count = queryResultCities.size();
                return results;
            }
        };
        return filter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText != null) {
            if (normalV.getVisibility() == View.VISIBLE) {
                normalV.setVisibility(View.INVISIBLE);
            }
            String text = editText.getText().toString().trim();
            if (text.length() > 0 && getFilter() != null) {
                getFilter().filter(text);
            }
            else {
                if (searchLV.getVisibility() == View.VISIBLE) {
                    searchLV.setVisibility(View.GONE);
                }
                if (errorTV.getVisibility() == View.VISIBLE) {
                    errorTV.setVisibility(View.GONE);
                }
                if (normalV.getVisibility() == View.INVISIBLE) {
                    normalV.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
