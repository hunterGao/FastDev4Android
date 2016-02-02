package com.huntergao.hunterweather.cityquery;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huntergao.hunterweather.db.City;
import com.huntergao.hunterweather.citylocation.LocationCity;
import com.huntergao.hunterweather.PreferenceUtils;
import com.huntergao.hunterweather.db.CityDB;
import com.huntergao.hunterweather.db.SelectCityDB;
import com.huntergao.hunterweather.view.CountDownView;

import java.util.List;

public class QueryCityActivity extends Activity implements OnClickListener,
		TextWatcher, OnItemClickListener {
	public static final String CITY_EXTRA_KEY = "city";
	private static final String AUTO_LOCATION_CITY_KEY = "auto_location_city";
	private RelativeLayout mRootView;
	private CountDownView mCountDownView;
	private ImageView mBackBtn;
	private TextView mLocationTV;
	private EditText mQueryCityET;
	private ImageButton mQueryCityExitBtn;
	private ListView mQueryCityListView;
	private GridView mHotCityGridView;
	private HotCityGridViewAdapter mHotCityAdapter;
	private LocationCity mLocationCity;
	private List<City> mHotCityList;
	private QueryCityAdapter mSearchCityAdapter;
	private Filter mFilter;
	
	private LocationCity.CityStatus mCityStatus = new LocationCity.CityStatus() {
		
		//定位成功后
		@Override
		public void update(String cityName) {
			dismissCountDownView();
			City city = CityDB.getInstance(QueryCityActivity.this).readCityByName(cityName);
			city.setLocation(true);
			mLocationTV.setText(cityName);
			PreferenceUtils.setPrefString(QueryCityActivity.this, AUTO_LOCATION_CITY_KEY, cityName);
			City locationCity = SelectCityDB.getInstance(QueryCityActivity.this).readLoactionCity();
			if(null != locationCity){
				if(!city.getName().equals(locationCity.getName())){
					SelectCityDB.getInstance(QueryCityActivity.this).deleteCity(locationCity.getPostID());
					SelectCityDB.getInstance(QueryCityActivity.this).writeCity(city);
				}
			}else{
				SelectCityDB.getInstance(QueryCityActivity.this).writeCity(city);
			}
		}
		
		@Override
		public void detecting() {
			showCountDownView();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_query_layout);
		initDatas();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initViews() {
		mRootView = (RelativeLayout) findViewById(R.id.city_add_bg);
		mBackBtn = (ImageView) findViewById(R.id.back_image);
		mLocationTV = (TextView) findViewById(R.id.location_text);
		mQueryCityET = (EditText) findViewById(R.id.queryCityText);
		mQueryCityExitBtn = (ImageButton) findViewById(R.id.queryCityExit);

		mQueryCityListView = (ListView) findViewById(R.id.cityList);
		mQueryCityListView.setOnItemClickListener(this);
		mSearchCityAdapter = new QueryCityAdapter(QueryCityActivity.this);
		mQueryCityListView.setAdapter(mSearchCityAdapter);
		mQueryCityListView.setTextFilterEnabled(true);
		mFilter = mSearchCityAdapter.getFilter();

		// mEmptyCityView = (TextView) findViewById(R.id.noCityText);

		mHotCityGridView = (GridView) findViewById(R.id.hotCityGrid);
		mHotCityAdapter = new HotCityGridViewAdapter(this, mHotCityList);
		mHotCityGridView.setAdapter(mHotCityAdapter);
		mHotCityGridView.setOnItemClickListener(this);

		mBackBtn.setOnClickListener(this);
		mLocationTV.setOnClickListener(this);
		mQueryCityExitBtn.setOnClickListener(this);
		mQueryCityET.addTextChangedListener(this);

		String cityName = PreferenceUtils.getPrefString(this,
				AUTO_LOCATION_CITY_KEY, "");
		if (TextUtils.isEmpty(cityName)) {
			startLocation();
		} else {
			mLocationTV.setText(cityName);
		}
	}

	private void initDatas() {
		mHotCityList = CityDB.getInstance(this).readHotCity();
	}

	//城市选择的ListView和GridView的选择事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.cityList:
			City city = mSearchCityAdapter.getItem(position);
			if (city.isSelect()) {
				Toast.makeText(this, "该城市已选择", Toast.LENGTH_SHORT).show();
				return;
			}
			SelectCityDB.getInstance(this).writeCity(city);
			finish();
			break;
		case R.id.hotCityGrid:
			City hotCity = mHotCityList.get(position);
			if (hotCity.isSelect()) {
				Toast.makeText(this, "该城市已选择", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			hotCity.setSelect(true);
			mHotCityAdapter.notifyDataSetChanged();
			CityDB.getInstance(this).updateHotCitySelect(hotCity.getName());
			SelectCityDB.getInstance(this).writeCity(hotCity);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_image:
			finish();
			break;
		case R.id.location_text:
			startLocation();
			break;
		case R.id.queryCityExit:
			mQueryCityET.setText("");
			break;
		case R.id.cancel_locate_city_btn:
			stopLocation();
			break;
		default:
			break;
		}
	}
	
	private void startLocation(){
		if(NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE){
			Toast.makeText(this, "网络错误，无法定位", Toast.LENGTH_SHORT).show();
			return;
		}
		if(null == mLocationCity){
			mLocationCity = new LocationCity(this, mCityStatus);
		}
		if(!mLocationCity.isStartLocation()){
			mLocationCity.startLocation();
		}
	}
	
	private void stopLocation(){
		dismissCountDownView();
		if(null != mLocationCity){
			mLocationCity.stopLocation();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopLocation();
	}

	@Override
	public void onBackPressed() {
		if (mCountDownView != null && mCountDownView.isCountingDown()) {
			mCountDownView.cancelCountDown();
		} else {
			super.onBackPressed();
		}
	}

	public boolean enoughToFilter() {
		return mQueryCityET.getText().length() > 0;
	}

	private void doBeforeTextChanged() {
		if (mQueryCityListView.getVisibility() == View.GONE) {
			mQueryCityListView.setVisibility(View.VISIBLE);
		}
	}

	private void doAfterTextChanged() {
		if (enoughToFilter()) {
			if (mFilter != null) {
				mFilter.filter(mQueryCityET.getText().toString().trim());
			}
		} else {
			if (mQueryCityListView.getVisibility() == View.VISIBLE) {
				mQueryCityListView.setVisibility(View.GONE);
			}
			if (mFilter != null) {
				mFilter.filter(null);
			}
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		doBeforeTextChanged();
	}

	//EditText文字改变监听事件
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {
			mQueryCityExitBtn.setVisibility(View.GONE);
		} else {
			mQueryCityExitBtn.setVisibility(View.VISIBLE);
		}
		doAfterTextChanged();
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private void showCountDownView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		inflater.inflate(R.layout.count_down_to_location, mRootView, true);
		mCountDownView = (CountDownView) mRootView
				.findViewById(R.id.count_down_to_locate);
		Button btn = (Button) mRootView
				.findViewById(R.id.cancel_locate_city_btn);
		btn.setOnClickListener(this);
		mCountDownView.setCountDownFinishedListener(new OnCountDownFinishedListener() {
			@Override
			public void onCountDownFinished() {
				Toast.makeText(QueryCityActivity.this, 
						"定位失败", Toast.LENGTH_SHORT).show();
				stopLocation();
			}
		});
		mCountDownView.startCountDown(10);
	}

	private void dismissCountDownView() {
		if (mCountDownView != null && mCountDownView.isCountingDown()){
			mCountDownView.cancelCountDown();
		}
	}
}
