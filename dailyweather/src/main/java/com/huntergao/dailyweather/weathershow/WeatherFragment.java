package com.huntergao.dailyweather.weathershow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherListAdapter;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.util.SystemUtils;
import com.huntergao.dailyweather.util.WeatherIconUtils;
import com.huntergao.dailyweather.view.XListView;
import com.huntergao.dailyweather.xml.XmlParserUtil;
import com.huntergao.dailyweather.xml.model.ForecastWeatherInfo;
import com.huntergao.dailyweather.xml.model.TodayWeatherInfo;


@SuppressLint({ "NewApi", "ValidFragment" })
public class WeatherFragment extends Fragment {
	public static final String ARG_CITY = "city";
	public static final String ARG_WEATHERINFO = "weatherInfo";
	public static final String REFRESH_TIME_KEY = "refreshTime";
	
	private XListView mListView;
	private WeatherListAdapter mWeatherAdapter;
	private ImageView mNormalImageView;
	private ImageView mBlurredImageView;
	private View mListHeaderView;

	private int mLastDampedScroll;
	private int mHeaderHeight = -1;

	// 当前天气的View
	private ImageView mCurWeatherIV; //图片
	private TextView mCurWeatherTV; //天气类型：晴  多云 阴等等
	private TextView mCurHighTempTV; //最高温度
	private TextView mCurLowTempTV; //最低温度
	private TextView mCurFeelsTempTV; //当前温度
	private TextView mCurWeatherCopyTV; //天气的发布时间

	private City mCity = null;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			TodayWeatherInfo info = XmlParserUtil.parseXmlWithPull(
					getActivity(), response);
//			App.mSelectWeatherInfoMap.put(mCity.getName(), info);
//			initDataView(info);
		};
	};
	
	public WeatherFragment(City city) {
		mCity = city;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_weather, container, false);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	/**
	 * 初始化所有的view
	 * 
	 * @param view
	 */
	private void initViews(View view) {
		mListView = (XListView) view.findViewById(R.id.drag_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(new XListView.IXListViewListener() {
			
			@Override
			public void onRefresh() {
				updateWeatherView();
				onLoad();
			}
			
			@Override
			public void onLoadMore() {
				onLoad();
			}
		});
		mNormalImageView = (ImageView) view
				.findViewById(R.id.weather_background);
		mBlurredImageView = (ImageView) view
				.findViewById(R.id.weather_background_blurred);
		mBlurredImageView.setImageAlpha((int) (0 * 255));

		mListHeaderView = LayoutInflater.from(getActivity()).inflate(
				R.layout.weather_today, null);
		// 获取屏幕高度
		int displayHeight = SystemUtils.getDisplayHeight(getActivity());
		// HeaderView高度=屏幕高度-标题栏高度-状态栏高度
		mHeaderHeight = displayHeight
				- getResources().getDimensionPixelSize(
						R.dimen.abs__action_bar_default_height)
				- SystemUtils.getStatusBarHeight(getActivity());
		mListHeaderView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, mHeaderHeight));
		// 计算背景View的高度，适当比屏幕高度多一点，
		// 之所以多1/8是为了后面滑动ListView时背景能跟随滑动。
		int backgroundHeight = displayHeight
				- SystemUtils.getStatusBarHeight(getActivity()) + mHeaderHeight / 8;
		mNormalImageView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, backgroundHeight));
		mBlurredImageView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, backgroundHeight));

		mListView.addHeaderView(mListHeaderView, null, false);// 给ListView添加HeaderView
		mWeatherAdapter = new WeatherListAdapter(getActivity());
		mListView.setAdapter(mWeatherAdapter);
		mListView.setOnScrollListener(mOnScrollListener);// 监听滑动
		initCurWeatherViews(view);
		mListView.setSelection(0);// 默认选中最上面一个view
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	/**
	 * 初始化当前天气的view，即ListView的HeaderView
	 * 
	 * @param view
	 */
	private void initCurWeatherViews(View view) {
		mCurWeatherIV = (ImageView) view.findViewById(R.id.main_icon);
		mCurWeatherTV = (TextView) view.findViewById(R.id.weather_description);
		mCurHighTempTV = (TextView) view.findViewById(R.id.temp_high);
		mCurLowTempTV = (TextView) view.findViewById(R.id.temp_low);
		mCurFeelsTempTV = (TextView) view.findViewById(R.id.temperature);
		mCurWeatherCopyTV = (TextView) view.findViewById(R.id.copyright);

		updateWeatherView();
	}
	
	private void updateWeatherView(){
//		TodayWeatherInfo weatherInfo = App.mSelectWeatherInfoMap.get(mCity.getName());
//		if(weatherInfo == null){
//			HttpUtil.sendHttpRequest(App.ADDRESS+mCity.getPostID(), new HttpCallBackListener() {
//
//				@Override
//				public void onFinish(String response) {
//					Message msg = new Message();
//					msg.obj = response;
//					mHandler.sendMessage(msg);
//				}
//
//				@Override
//				public void onError(Exception e) {
//
//				}
//			});
//		}else{
//			initDataView(weatherInfo);
//		}
	}

	private void initDataView(TodayWeatherInfo weatherInfo) {
		int sunrise = Integer.valueOf(weatherInfo.getSunrise().split(":")[0]);
		int sunset = Integer.valueOf(weatherInfo.getSunset().split(":")[0]);
		ForecastWeatherInfo info = weatherInfo.getForecastList().get(0);
		String type = info.getDayWeather().getType();
		mNormalImageView.setImageResource(WeatherIconUtils
				.getWeatherNromalBg(type, sunrise, sunset));
		mBlurredImageView.setImageResource(WeatherIconUtils
				.getWeatherBlurBg(type, sunrise, sunset));
		mCurWeatherIV.setImageResource(WeatherIconUtils.getWeatherIcon(type, sunrise, sunset));
		mCurWeatherTV.setText(type);
		mCurFeelsTempTV.setText(weatherInfo.getWendu());
		mCurHighTempTV.setText(info.getHigh());/*"°"*/
		mCurLowTempTV.setText(info.getLow());
		mCurWeatherCopyTV.setText(weatherInfo.getUpdatetime()
				+ "发布");
		mWeatherAdapter.setWeather(weatherInfo);
	}

	@Override
	public void onResume() {
		super.onResume();
		mListView.setSelection(0);// 选中第一个view，当fragment被回收后再重新创建时恢复状态
	}

	// ListView滑动监听，更新背景模糊度和移动距离
	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			View topChild = view.getChildAt(1);// 获取ListView的第一个View
			if (topChild == null) {
				onNewScroll(0);
			} else if (topChild != mListHeaderView) {
				onNewScroll(mListHeaderView.getHeight());
			} else {
				onNewScroll(-topChild.getTop());
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	};

	/**
	 * 更新背景模糊度和移动距离
	 * 
	 * @param scrollPosition
	 */
	private void onNewScroll(int scrollPosition) {

		// 控制模糊背景的alpha值
		float ratio = Math.min(1.5f * (-mListHeaderView.getTop())
				/ mHeaderHeight, 1.0f);
		int newAlpha = Math.round(ratio * 255);
		// Apply on the ImageView if needed
		mBlurredImageView.setImageAlpha(0);

		// 控制背景滑动距离
		int dampedScroll = Math.round(scrollPosition * 0.125f);
		int offset = mLastDampedScroll - dampedScroll;
		mNormalImageView.offsetTopAndBottom(offset);
		mBlurredImageView.offsetTopAndBottom(offset);
		mLastDampedScroll = dampedScroll;
	}

	
}
