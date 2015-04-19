/*
 * 选择终点界面，为上下班拼车，短途拼车，出租车拼车公用界面
 * 接入了百度接口
 * 两个ListView分别存储收藏的地址和历史地址，这些信息存在本地，会随着软件的卸载消失。
 * “我的位置”实现定位
 * todo 删除
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.Button;

import com.Tool.DatabaseHelper;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class ChooseArrivalActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
		OnGetGeoCoderResultListener {

	EditText choose;
	int intentcall;
	View position;
	ImageView fanhui;

	// 用户手机号
	String UserPhoneNumber;

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor result, result2;

	ListView list1, list2;

	// database end!!

	// 百度map
	String PointUserName, PointMapName;
	float longitude, latitude;

	// private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;

	// 百度mapend

	// 百度定位

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	String UserCity, city;
	LocationClientOption option;

	// 百度定位end

	// database intent 辅助
	int Locationcount;

	// database intent 辅助end

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_arrival);
		list1 = (ListView) findViewById(R.id.choosearrival_list1);
		list2 = (ListView) findViewById(R.id.choosearrival_list2);
		Button send1 = (Button) findViewById(R.id.choosearrival_send);
		choose = (EditText) findViewById(R.id.choosearrival_start);
		position = findViewById(R.id.choosearrival_mylocation);
		fanhui = (ImageView) findViewById(android.R.id.home);

		// 提取用户手机号
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = sharedPref.getString(
				getString(R.string.PreferenceUserPhoneNumber), "0");

		// database

		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();

		// database end!!!

		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ChooseArrivalActivity.this.finish();
			}
		});

		// 百度地图操作

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		// 初始化搜索模块，注册搜索事件监听
		// mPoiSearch = PoiSearch.newInstance();
		// mPoiSearch.setOnGetPoiSearchResultListener(this);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.choosearrival_start);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
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

		// 百度地图end

		// 百度定位

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.start();

		// 百度定位end

		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		position.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 百度定位
				Toast.makeText(getApplicationContext(),
						getString(R.string.warningInfo_waitForLocation),
						Toast.LENGTH_SHORT).show();

				LocationClientOption option = new LocationClientOption();
				option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
				option.setScanSpan(1024);// 设置发起定位请求的间隔时间为5000ms
				option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
				mLocationClient.setLocOption(option);
				if (mLocationClient != null && mLocationClient.isStarted()) {
					mLocationClient.requestLocation();
				} else {
					Log.w("百度定位", "定位客户端空或没启动");
				}
				// 百度定位结束

				// Intent myposition = new Intent(ChooseArrivalActivity.this,
				// FindPositionActivity.class);
				// startActivity(myposition);
			}
		});

		send1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 百度map
				if (!choose.getText().toString().isEmpty()) {
					PointUserName = choose.getText().toString();
					if (UserCity != null && !UserCity.isEmpty()) {
						city = UserCity;
					} else {
						city = getString(R.string.defaultCity);
					}
					mSearch.geocode(new GeoCodeOption().city(city).address(
							choose.getText().toString()));
					// baidumapend

				} else {
					Intent startplace = new Intent();
					ChooseArrivalActivity.this.setResult(RESULT_CANCELED,
							startplace);
					ChooseArrivalActivity.this.finish();
				}

			}
		});

		// intent & database!!
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent endplace = new Intent();

				// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
				// don't filter by row groups，排序条件。

				result.moveToPosition(arg2);

				PointUserName = result.getString(1);
				PointMapName = result.getString(2);
				longitude = result.getFloat(3);
				latitude = result.getFloat(4);

				endplace.putExtra(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				endplace.putExtra(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				endplace.putExtra(getString(R.string.dbstring_longitude),
						String.valueOf(longitude));
				endplace.putExtra(getString(R.string.dbstring_latitude),
						String.valueOf(latitude));
				Log.w("STintent发送经度", endplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent发送纬度", endplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseArrivalActivity.this.setResult(RESULT_OK, endplace);
				ChooseArrivalActivity.this.finish();

			}

		});

		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent endplace = new Intent();

				// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
				// don't filter by row groups，排序条件。

				result2.moveToPosition(arg2);

				PointUserName = result2.getString(1);
				PointMapName = result2.getString(2);
				longitude = result2.getFloat(3);
				latitude = result2.getFloat(4);

				endplace.putExtra(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				endplace.putExtra(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				endplace.putExtra(getString(R.string.dbstring_longitude),
						String.valueOf(longitude));
				endplace.putExtra(getString(R.string.dbstring_latitude),
						String.valueOf(latitude));
				Log.w("STintent发送经度", endplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent发送纬度", endplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseArrivalActivity.this.setResult(RESULT_OK, endplace);
				ChooseArrivalActivity.this.finish();

			}
		});

		// intent & databaseend!!

	}

	// 百度地圖開始

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			// PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			// mBaiduMap.setOnMarkerClickListener(overlay);
			// overlay.setData(result);
			// overlay.addToMap();
			// overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.warningInfo_noAnswer),
					Toast.LENGTH_SHORT).show();
		} else {
			// Toast.makeText(Baidumaptest.this, result.getName() + ": " +
			// result.getAddress(), Toast.LENGTH_SHORT)
			// .show();

		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
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

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

			Toast.makeText(this, getString(R.string.warningInfo_noAnswer),
					Toast.LENGTH_LONG).show();
			return;
		}
		longitude = (float) result.getLocation().longitude;
		latitude = (float) result.getLocation().latitude;

		LatLng ptCenter = new LatLng(latitude, longitude);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
		Log.w("经纬度", strInfo);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, getString(R.string.warningInfo_noAnswer),
					Toast.LENGTH_LONG).show();
			return;
		}
		PointMapName = result.getAddress();
		Log.w("mapname", PointMapName);
		// database

		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
		// don't filter by row groups，排序条件。

		Cursor dbresult = db1.query(getString(R.string.dbtable_placeliked),
				null, getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[] { PointMapName }, null, null, null);
		if (0 == dbresult.getCount()) {
			dbresult = db1.query(getString(R.string.dbtable_placehistory),
					null, getString(R.string.dbstring_PlaceMapName) + "=?",
					new String[] { PointMapName }, null, null, null);
			if (0 == dbresult.getCount()) {
				ContentValues content = new ContentValues();
				content.put(getString(R.string.dbstring_PlaceUserName), choose
						.getText().toString());
				content.put(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				content.put(getString(R.string.dbstring_longitude), longitude);
				content.put(getString(R.string.dbstring_latitude), latitude);
				db1.insert(getString(R.string.dbtable_placehistory), null,
						content);
				Log.w("历史数据库",
						"添加" + PointUserName + "  map:" + PointMapName
								+ String.valueOf(longitude) + "&"
								+ String.valueOf(latitude));

			}
			// debug!!!
			// else {
			// dbresult.moveToFirst();
			// Log.w("历史经度", String.valueOf(dbresult.getFloat(3)));
			// Log.w("历史纬度", String.valueOf(dbresult.getFloat(4)));
			// }
			// debug end!!
		}
		dbresult.close();

		// database end

		// intent

		Intent endplace = new Intent();
		endplace.putExtra(getString(R.string.dbstring_PlaceUserName), choose
				.getText().toString());
		endplace.putExtra(getString(R.string.dbstring_PlaceMapName),
				PointMapName);
		endplace.putExtra(getString(R.string.dbstring_longitude),
				String.valueOf(longitude));
		endplace.putExtra(getString(R.string.dbstring_latitude),
				String.valueOf(latitude));

		ChooseArrivalActivity.this.setResult(RESULT_OK, endplace);
		ChooseArrivalActivity.this.finish();

		// intent end

	}

	// 百度地圖結束

	// 百度定位

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

			PointMapName = location.getAddrStr();
			PointUserName = location.getTime() + "时的位置";
			longitude = (float) location.getLongitude();
			latitude = (float) location.getLatitude();

			if (location.getCity() == null) {
				Log.e("城市名", "空!");
			} else {
				Log.w("城市名", location.getCity());
				UserCity = location.getCity();
			}

			Log.w("百度定位", sb.toString());

			// database intent 辅助
			Locationcount++;
			Log.w("定位计数", String.valueOf(Locationcount));

			// database intent 辅助end

			// database

			// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
			// don't filter by row groups，排序条件。

			if (Locationcount >= 3) {
				Cursor dbresult = db1.query(
						getString(R.string.dbtable_placeliked), null,
						getString(R.string.dbstring_PlaceMapName) + "=?",
						new String[] { PointMapName }, null, null, null);
				if (0 == dbresult.getCount()) {
					dbresult = db1.query(
							getString(R.string.dbtable_placehistory), null,
							getString(R.string.dbstring_PlaceMapName) + "=?",
							new String[] { PointMapName }, null, null, null);
					if (0 == dbresult.getCount()) {
						ContentValues content = new ContentValues();
						content.put(getString(R.string.dbstring_PlaceUserName),
								PointUserName);
						content.put(getString(R.string.dbstring_PlaceMapName),
								PointMapName);
						content.put(getString(R.string.dbstring_longitude),
								longitude);
						content.put(getString(R.string.dbstring_latitude),
								latitude);
						db1.insert(getString(R.string.dbtable_placehistory),
								null, content);
						Log.w("历史数据库", "添加" + PointUserName + "  map:"
								+ PointMapName + String.valueOf(longitude)
								+ "&" + String.valueOf(latitude));

					}
					dbresult.close();
				}

				// database end

				// intent

				Intent endplace = new Intent();

				endplace.putExtra(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				endplace.putExtra(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				endplace.putExtra(getString(R.string.dbstring_longitude),
						String.valueOf(longitude));
				endplace.putExtra(getString(R.string.dbstring_latitude),
						String.valueOf(latitude));

				ChooseArrivalActivity.this.setResult(RESULT_OK, endplace);
				ChooseArrivalActivity.this.finish();

				// intent end
			}
		}
	}

	// 百度定位结束

	// 生命周期!!

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();

		result.close();
		result2.close();
		db1.close();
		mSearch.destroy();
		mSuggestionSearch.destroy();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		mLocationClient.start();

		// database intent 辅助
		Locationcount = 0;
		// database intent 辅助end
		// 百度定位

		option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		mLocationClient.setLocOption(option);
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			Log.w("百度定位", "定位客户端空或没启动");
		}
		// 百度定位结束

		// list赋值

		// database
		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，don't filter by row
		// groups，排序条件。
		result = db1.query(getString(R.string.dbtable_placeliked), null, null,
				null, null, null, null);
		Log.w("喜欢数量", String.valueOf(result.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result, new String[] {
						getString(R.string.dbstring_PlaceUserName),
						getString(R.string.dbstring_PlaceMapName) }, new int[] {
						R.id.re_address, R.id.detail });
		list1.setAdapter(adapter1);

		result2 = db1.query(getString(R.string.dbtable_placehistory), null,
				null, null, null, null, null);
		Log.w("历史数量", String.valueOf(result2.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter2 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result2, new String[] {
						getString(R.string.dbstring_PlaceUserName),
						getString(R.string.dbstring_PlaceMapName) }, new int[] {
						R.id.re_address, R.id.detail });

		list2.setAdapter(adapter2);

		// database end
		// list赋值结束

	}

	// 生命周期end!!

}