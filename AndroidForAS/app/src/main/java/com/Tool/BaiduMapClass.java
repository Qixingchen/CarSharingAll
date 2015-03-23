package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xmu.carsharing.R;

/**
 * Created by 雨蓝 on 2015/3/16.
 * 务必调用销毁函数
 * GEO需要GetGeoCoder函数和GetGeoCoderCallBack接口
 * 反Geo需要getReverseGeoCode和GetReverseGeoCoderCallBack接口。
 * autoCompleteTextView用于自动填充文本框 todo此函数无法使用
 */
public class BaiduMapClass implements OnGetSuggestionResultListener {

	private String PointMapName;
	private double longitude, latitude;

	//搜索关键字输入窗口
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;

	//名称建议
	private SuggestionSearch mSuggestionSearch = null;

	//Geo编码与反编码
	private GeoCoder mSearch = null;
	//回调函数
	private GetGeoCoderCallBack getGeoCoderCallBack ;
	private GetReverseGeoCoderCallBack getReverseGeoCoderCallBack;
	//监听器
	private onGetGeoCodeResult GetGeo_ReverseGeoAnsListener = new onGetGeoCodeResult();


	//class内部使用
	private Application mapplication;
	private Context mcontext;
	private Activity mactivity;
	private String logtag ="百度地图类";

	public BaiduMapClass(Activity mact){
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


	}

	//Geo编码
	//传入回调函数，地址 用户城市
	public  void GetGeoCoder(GetGeoCoderCallBack getGeoCCB,String Addr,String UserCity){
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(GetGeo_ReverseGeoAnsListener);
		getGeoCoderCallBack = getGeoCCB;
		mSearch.geocode(new GeoCodeOption()
				.city(UserCity).address(Addr));
	}

	//Geo回调接口
	public interface GetGeoCoderCallBack{
		public void getGeoCoderCallBack(double longitude,double latitude);
	}



	//反Geo编码
	//传入回调函数，经纬度
	public void getReverseGeoCode(GetReverseGeoCoderCallBack getReGeoCCB,
	                              Double longitude, Double latitude){
		LatLng ptCenter = new LatLng(latitude, longitude);
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(GetGeo_ReverseGeoAnsListener);
		getReverseGeoCoderCallBack = getReGeoCCB;
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}


	//反GEO回调接口
	public interface GetReverseGeoCoderCallBack{
		public void getReverseGeoCoderCallBack(String Addr);
	}

	//Geo监听器与反Geo监听器
	private class onGetGeoCodeResult implements OnGetGeoCoderResultListener{
		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

				Toast.makeText(mactivity, mcontext.getString(R.string.warningInfo_noAnswer),
						Toast.LENGTH_LONG).show();
				return;
			}
			longitude = result.getLocation().longitude;
			latitude = result.getLocation().latitude;

			String strInfo = String.format("纬度：%f 经度：%f",
					result.getLocation().latitude, result.getLocation().longitude);
			Log.w(logtag+"经纬度", strInfo);

			getGeoCoderCallBack.getGeoCoderCallBack(longitude,latitude);
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(mactivity, mcontext.getString(R.string.warningInfo_noAnswer),
						Toast.LENGTH_LONG).show();
				return;
			}
			PointMapName = result.getAddress();
			Log.w(logtag+"mapname", PointMapName);
			getReverseGeoCoderCallBack.getReverseGeoCoderCallBack(PointMapName);
		}

	}


	//自动填充文本框
	public void autoCompleteTextView(int viewid,final String UserCity){
		keyWorldsView = (AutoCompleteTextView) mactivity.findViewById(viewid);
		sugAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_dropdown_item_1line);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView.setAdapter(sugAdapter);

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
				String city;
				if (UserCity != null && !UserCity.isEmpty()) {
					city = UserCity;
				} else {
					city = "";
				}
				// 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				mSuggestionSearch
						.requestSuggestion(new SuggestionSearchOption()
								.keyword(cs.toString()).city(city));
			}
		});
	}

	@Override
	public void onGetSuggestionResult  (SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	//暂停
	public void stop(){

	}

	//销毁
	public void destory(){
		mSearch.destroy();
		mSuggestionSearch.destroy();
	}
}
