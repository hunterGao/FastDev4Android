package com.huntergao.hunterweather.citymanage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huntergao.hunterweather.BaseApplication;
import com.huntergao.hunterweather.HttpCallBackListener;
import com.huntergao.hunterweather.xml.model.TodayWeatherInfo;
import com.huntergao.hunterweather.db.City;
import com.huntergao.hunterweather.db.CityDB;
import com.huntergao.hunterweather.db.SelectCityDB;
import com.huntergao.hunterweather.cityquery.QueryCityActivity;
import com.huntergao.hunterweather.util.HttpUtils;
import com.huntergao.hunterweather.util.NetworkUtils;
import com.huntergao.hunterweather.view.DragSortGridView;
import com.huntergao.hunterweather.xml.XmlParserUtil;

public class ManageCityActivity extends Activity implements
		OnClickListener, OnItemClickListener{
	private DragSortGridView mGridView;
	private CityGridViewAdapter mAdapter;
	private ImageView mBackBtn; //返回按钮
	private ImageView mRefreshCityBtn; //刷新按钮 
	private ImageView mDividerLine;
	private ImageView mEditCityBtn; //城市编辑按钮
	private ImageView mConfirmCityBtn; //在编辑状态下，完成编辑按钮
	private ProgressBar mRefreshProgressBar;
	private List<City> mCityList;
	private GridCityRefreshTask mGridCityRefreshTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_manager_layout);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDatas();
		initViews();
	}

	@Override
	protected void onPause() {
		super.onPause();
		updateRefreshMode(false);// 暂停时更新刷新模式
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		mCityList = SelectCityDB.getInstance(ManageCityActivity.this).readAllCity();
		mAdapter = new CityGridViewAdapter(this, mCityList);
		for(City city : mCityList){
			if(city == null)
				continue;
			if(BaseApplication.mSelectWeatherInfoMap.get(city.getName()) == null){
				updateCityWeather(city);
			}
		}
	}

	private void initViews() {
		mGridView = (DragSortGridView) findViewById(R.id.my_city);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnReorderingListener(dragSortListener);
		mGridView.setOnItemClickListener(this);

		mBackBtn = (ImageView) findViewById(R.id.back_image);
		mRefreshCityBtn = (ImageView) findViewById(R.id.refresh_city);
		mDividerLine = (ImageView) findViewById(R.id.divider_line);
		mEditCityBtn = (ImageView) findViewById(R.id.edit_city);
		mConfirmCityBtn = (ImageView) findViewById(R.id.confirm_city);
		mRefreshProgressBar = (ProgressBar) findViewById(R.id.refresh_progress);

		mBackBtn.setOnClickListener(this);
		mRefreshCityBtn.setOnClickListener(this);
		mEditCityBtn.setOnClickListener(this);
		mConfirmCityBtn.setOnClickListener(this);
		mRefreshProgressBar.setOnClickListener(this);
		updateBtnStates();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_image: //后退按钮
			finish();
			break;
		case R.id.refresh_city:// 开始刷新
			updateRefreshMode(true);
			break;
		case R.id.refresh_progress:// 取消刷新
			updateRefreshMode(false);
			break;
		case R.id.edit_city:
		case R.id.confirm_city:
			changeEditMode();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int type = mAdapter.getItemViewType(position);
		switch(type){
		case CityGridViewAdapter.ADD_CITY_TYPE:
			Intent intent = new Intent(ManageCityActivity.this, QueryCityActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void updateCityWeather(City city) {
		if (city == null){
			return;
		}
		if (NetworkUtils.getNetworkState(this) == NetworkUtils.NETWORN_NONE) {
			Toast.makeText(this,"网络错误，请检查网络", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		//更新UI
		mGridCityRefreshTask = new GridCityRefreshTask(city);
		mGridCityRefreshTask.execute();
	}

	private void updateRefreshMode(boolean isRefresh) {
		if (isRefresh && NetworkUtils.getNetworkState(this) == NetworkUtils.NETWORN_NONE) {
			Toast.makeText(this, "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
			return;
		}
		mRefreshProgressBar.setVisibility(isRefresh ? View.VISIBLE
				: View.INVISIBLE);
		mRefreshCityBtn.setVisibility(isRefresh ? View.INVISIBLE : View.VISIBLE);
		mEditCityBtn.setEnabled(!isRefresh && (mCityList.size() > 1));
		mGridView.setEnabled(!isRefresh);
		mGridView.setOnReorderingListener(isRefresh ? null : dragSortListener);
		
		// 开一个异步线程去更新天气或者取消更新
		if (isRefresh) {
			mGridCityRefreshTask = new GridCityRefreshTask(null);
			mGridCityRefreshTask.execute();
		} else {
			mAdapter.setRefreshingIndex(-1);
			if (mGridCityRefreshTask != null)
				mGridCityRefreshTask.cancel(true);
		}

	}

	private DragSortGridView.OnReorderingListener dragSortListener = 
			new DragSortGridView.OnReorderingListener() {

		@Override
		public void onReordering(int fromPosition, int toPosition) {
			mAdapter.reorder(fromPosition, toPosition);
		}

		@Override
		public void beginRecordering(AdapterView<?> parent, View view,
				int position, long id) {
			if (mAdapter.isEditMode())
				return;
			changeEditMode();
		}

	};

	private void changeEditMode() {
		mAdapter.changeEditMode();
		if (mAdapter.isEditMode()) {
			mConfirmCityBtn.setVisibility(View.VISIBLE);
			mRefreshCityBtn.setVisibility(View.INVISIBLE);
			mDividerLine.setVisibility(View.INVISIBLE);
			mEditCityBtn.setVisibility(View.INVISIBLE);
		} else {
			mConfirmCityBtn.setVisibility(View.INVISIBLE);
			if (mRefreshProgressBar.getVisibility() != View.VISIBLE)
				mRefreshCityBtn.setVisibility(View.VISIBLE);
			mDividerLine.setVisibility(View.VISIBLE);
			mEditCityBtn.setVisibility(View.VISIBLE);
		}
		updateBtnStates();
	}

	public void deleteCityFromTable(int position) {
		if(position >= mCityList.size())
			return;
		City city = mCityList.get(position);
		mCityList.remove(position);
		// 从全局变量中删除
		BaseApplication.mSelectWeatherInfoMap.remove(city.getName());
		// 从城市表中删除
		SelectCityDB.getInstance(this).deleteCity(city.getPostID());
		CityDB.getInstance(this).updateHotCityNoSelect(city.getName());
		updateUI(false);
		if (mCityList.isEmpty())// 如果全部被删除完了，更新一下编辑状态
			changeEditMode();
	}

	private void updateUI(boolean isAdd) {
		mAdapter.notifyDataSetChanged();
		updateBtnStates();
	}

	/**
	 * 更新ActionBar按钮状态
	 */
	private void updateBtnStates() {
		mEditCityBtn.setEnabled(mCityList.size() > 1);
		mRefreshCityBtn.setEnabled(mCityList.size() > 1);
		mRefreshProgressBar.setEnabled(mCityList.size() > 1);
	}

	private final class GridCityRefreshTask extends
			AsyncTask<Void, Integer, String> {
		private City mTaskCity;
		private int mTaskIndex = -1;

		public GridCityRefreshTask(City city) {
			this.mTaskCity = city;
			mTaskIndex = mCityList.size()-1;
			if (city != null && mCityList.contains(city)){
				mTaskIndex = mCityList.indexOf(city);
			}else if(city == null){
				mTaskIndex = 0;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAdapter.setRefreshingIndex(mTaskIndex);// 开始
		}

		//该方法运行在子线程中
		@Override
		protected String doInBackground(Void... params) {
			//刷新单个城市
			if (mTaskCity != null) {
				String result = HttpUtils.getHttpData(BaseApplication.ADDRESS+mTaskCity.getPostID());
				return result;
			} else {
				List<City> tmpCities = new ArrayList<City>();
				tmpCities.addAll(mCityList);
				for (City city : tmpCities) {
					mTaskIndex++;
					if (city != null){
						refreshWeatherInfo(city);
					}
					publishProgress(mTaskIndex);// 下载完数据之后再更新界面
				}
			}
			return null;
		}

		//当doInBackground执行完毕后，该方法立即执行
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (mTaskCity != null && result != null) {
				mCityList.set(mTaskIndex, mTaskCity);
				TodayWeatherInfo info = XmlParserUtil.parseXmlWithPull(
						ManageCityActivity.this, result);
				BaseApplication.mSelectWeatherInfoMap.put(mTaskCity.getName(), info);
				updateUI(true);
			}
			mTaskIndex = -1;
			mAdapter.setRefreshingIndex(mTaskIndex);// 重置
			updateRefreshMode(false);// 结束刷新

		}

		//当在doInBackground（）方法中执行了publisProgress（）方法后，该方法立即执行
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			mAdapter.setRefreshingIndex(values[0]);
		}
	}

	private void refreshWeatherInfo(final City city) {
		HttpUtils.sendHttpRequest(BaseApplication.ADDRESS+city.getPostID(), new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				TodayWeatherInfo info = XmlParserUtil.parseXmlWithPull(
						ManageCityActivity.this, response);
				BaseApplication.mSelectWeatherInfoMap.put(city.getName(), info);
			}
			
			@Override
			public void onError(Exception e) {
				
			}
		});
	}

}
