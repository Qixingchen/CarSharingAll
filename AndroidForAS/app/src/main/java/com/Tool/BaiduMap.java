package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xmu.carsharing.R;

/**
 * Created by 雨蓝 on 2015/3/16.
 * 务必调用销毁函数
 */
public class BaiduMap {

	private String UserCity,city;
	String PointUserName, PointMapName;
	float longitude, latitude;

	private com.baidu.mapapi.map.BaiduMap mBaiduMap = null;


	//搜索关键字输入窗口
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;

	//名称建议
	private SuggestionSearch mSuggestionSearch = null;

	//Geo编码与反编码
	private GeoCoder mSearch = null;


	//class内部使用
	private Application mapplication;
	private Context mcontext;
	private Activity mactivity;

	public BaiduMap(Activity mact){
		mactivity = mact;
		mapplication =mact.getApplication();
		mcontext = mapplication.getApplicationContext();
		initMap();
	}

	//构造函数已调用
	private void initMap(){

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(mcontext);

		// 初始化搜索模块，注册搜索事件监听
		// mPoiSearch = PoiSearch.newInstance();
		// mPoiSearch.setOnGetPoiSearchResultListener(this);

	}

	//点名称建议
	//传入回调监听器 关键字
	public void getPointSuggest(OnGetSuggestionResultListener listener,String keyword){
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(listener);
		mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
				.keyword(keyword).city(UserCity));
	}

	//Geo编码
	//传入回调监听器，地址
	public  void GetGeoCoder(OnGetGeoCoderResultListener listener,String Addr){
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(listener);
		mSearch.geocode(new GeoCodeOption()
				.city(UserCity).address(Addr));
	}

	//反Geo编码
	//传入回调监听器，地址
	public void getReverseGeoCode(OnGetGeoCoderResultListener listener,LatLng ptCenter){
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(listener);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}

	//自动填充文本框
	public void autoCompleteTextView(int viewid){
		keyWorldsView = (AutoCompleteTextView) mactivity.findViewById(viewid);
		sugAdapter = new ArrayAdapter<String>(mcontext,viewid);
		keyWorldsView.setAdapter(sugAdapter);
		// mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
		// .findFragmentById(R.id.map))).getBaiduMap();

		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
			                              int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
			                          int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				if (UserCity != null && !UserCity.isEmpty()) {
					city = UserCity;
				} else {
					city = "";
				}
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
	}

	//暂停
	public void stop(){

	}

	//销毁
	public void destory()
	{
		mSearch.destroy();
		mSuggestionSearch.destroy();
	}
}
