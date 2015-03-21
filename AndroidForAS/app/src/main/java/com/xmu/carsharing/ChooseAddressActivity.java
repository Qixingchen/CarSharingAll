/**
 * 选择起点界面，为上下班拼车，短途拼车，出租车拼车公用界面
 * 接入了百度接口
 * 两个ListView分别存储收藏的地址和历史地址，这些信息存在本地，会随着软件的卸载消失。
 * “我的位置”实现定位
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.Tool.BaiduLocation;
import com.Tool.BaiduMapClass;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.sug.SuggestionSearch;

public class ChooseAddressActivity extends Activity implements
		BaiduLocation.GetHignLocationCallBack, BaiduLocation.GetCityCallBack,
		BaiduMapClass.GetReverseGeoCoderCallBack, BaiduMapClass.GetGeoCoderCallBack {

	private EditText choose;
	private View position;
	private ImageView fanhui;
	private String logtag = "选择地点页面";

	// database

	private DatabaseHelper db;
	private SQLiteDatabase db1;
	private Cursor result, result2;

	private ListView list1, list2;

	// database end!!

	// 用户手机号
	private String UserPhoneNumber;

	//TODO 迁移百度地图
	// 百度map
	private String PointUserName, PointMapName;
	double longitude, latitude;

	// private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	// 百度mapend

	// 百度定位
	private BaiduLocation baidulocation;
	private String UserCity;
	// 百度定位end

	//百度map

	private BaiduMapClass baidumapclass;

	// 百度map结束

	// database intent 辅助
	int Locationcount;

	// database intent 辅助end

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_address);
		list1 = (ListView) findViewById(R.id.chooseaddress_list1);
		list2 = (ListView) findViewById(R.id.chooseaddress_list2);
		choose = (EditText) findViewById(R.id.chooseaddress_start);
		Button send = (Button) findViewById(R.id.chooseaddress_send);
		position = findViewById(R.id.chooseaddress_mylocation);
		fanhui = (ImageView) findViewById(android.R.id.home);

		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ChooseAddressActivity.this.finish();
			}
		});

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


		// 百度定位


		baidulocation = new BaiduLocation(this);
		baidulocation.getUserCity(ChooseAddressActivity.this);

		// 百度定位end

		//百度map

		baidumapclass = new BaiduMapClass(this);

		// 百度map结束

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
				baidulocation.getHignLocation(ChooseAddressActivity.this);

			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 百度map
				String city;
				if (!choose.getText().toString().isEmpty()) {
					PointUserName = choose.getText().toString();
					if (UserCity != null && !UserCity.isEmpty()) {
						city = UserCity;
					} else {
						city = getString(R.string.defaultCity);
					}
					//todo try-catch
					baidumapclass.GetGeoCoder(ChooseAddressActivity.this, PointUserName,
							city);
					// baidumapend

				} else {
					Intent startplace = new Intent();
					ChooseAddressActivity.this.setResult(RESULT_CANCELED,
							startplace);
					ChooseAddressActivity.this.finish();
				}

			}
		});

		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			                        long arg3) {
				Intent startplace = new Intent();

				// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
				// don't filter by row groups，排序条件。

				result.moveToPosition(arg2);

				PointUserName = result.getString(1);
				PointMapName = result.getString(2);
				longitude = result.getFloat(3);
				latitude = result.getFloat(4);

				startplace.putExtra(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				startplace.putExtra(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				startplace.putExtra(getString(R.string.dbstring_longitude),
						String.valueOf(longitude));
				startplace.putExtra(getString(R.string.dbstring_latitude),
						String.valueOf(latitude));

				ContentValues content = new ContentValues();
				content.put(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				content.put(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				content.put(getString(R.string.dbstring_longitude), longitude);
				content.put(getString(R.string.dbstring_latitude), latitude);
				Log.w("STintent发送经度", startplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent发送纬度", startplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
				ChooseAddressActivity.this.finish();

			}

		});

		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			                        long arg3) {
				Intent startplace = new Intent();

				// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
				// don't filter by row groups，排序条件。

				result2.moveToPosition(arg2);

				PointUserName = result2.getString(1);
				PointMapName = result2.getString(2);
				longitude = result2.getFloat(3);
				latitude = result2.getFloat(4);

				startplace.putExtra(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				startplace.putExtra(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				startplace.putExtra(getString(R.string.dbstring_longitude),
						String.valueOf(longitude));
				startplace.putExtra(getString(R.string.dbstring_latitude),
						String.valueOf(latitude));

				ContentValues content = new ContentValues();
				content.put(getString(R.string.dbstring_PlaceUserName),
						PointUserName);
				content.put(getString(R.string.dbstring_PlaceMapName),
						PointMapName);
				content.put(getString(R.string.dbstring_longitude), longitude);
				content.put(getString(R.string.dbstring_latitude), latitude);
				Log.w("STintent发送经度", startplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent发送纬度", startplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
				ChooseAddressActivity.this.finish();

			}
		});

		// intent & databaseend!!

	}

	// 百度定位

	//精确定位回调函数
	@Override
	public void getHignLocationCallback(double longi, double lati, String addr) {
		longitude = longi;
		latitude = lati;
		PointMapName = addr;

		//数据库操作
		Cursor dbresult = db1.query(
				getString(R.string.dbtable_placeliked), null,
				getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[]{PointMapName}, null, null, null);
		if (0 == dbresult.getCount()) {
			dbresult = db1.query(
					getString(R.string.dbtable_placehistory), null,
					getString(R.string.dbstring_PlaceMapName) + "=?",
					new String[]{PointMapName}, null, null, null);
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
			//数据库操作 结束
		}

		// intent

		Intent startplace = new Intent();

		startplace.putExtra(getString(R.string.dbstring_PlaceUserName),
				PointUserName);
		startplace.putExtra(getString(R.string.dbstring_PlaceMapName),
				PointMapName);
		startplace.putExtra(getString(R.string.dbstring_longitude),
				String.valueOf(longitude));
		startplace.putExtra(getString(R.string.dbstring_latitude),
				String.valueOf(latitude));

		ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
		ChooseAddressActivity.this.finish();

		// intent end


	}

	//获取城市回调函数
	@Override
	public String getcityname(String mcity) {
		UserCity = mcity;
		Log.w(logtag, "获取城市回调函数");
		baidumapclass.autoCompleteTextView(R.id.chooseaddress_start, UserCity);
		return mcity;
	}

	// 百度定位结束

	//百度地图

	//Geo回调函数
	@Override
	public void getGeoCoderCallBack(double longitude, double latitude) {
		baidumapclass.getReverseGeoCode(ChooseAddressActivity.this, longitude, latitude);

	}

	//反Geo回调函数
	@Override
	public void getReverseGeoCoderCallBack(String Addr) {

		PointMapName = Addr;

		// database

		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
		// don't filter by row groups，排序条件。

		Cursor dbresult = db1.query(getString(R.string.dbtable_placeliked),
				null, getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[]{PointMapName}, null, null, null);
		if (0 == dbresult.getCount()) {
			dbresult = db1.query(getString(R.string.dbtable_placehistory),
					null, getString(R.string.dbstring_PlaceMapName) + "=?",
					new String[]{PointMapName}, null, null, null);
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

		Intent startplace = new Intent();
		startplace.putExtra(getString(R.string.dbstring_PlaceUserName), choose
				.getText().toString());
		startplace.putExtra(getString(R.string.dbstring_PlaceMapName),
				PointMapName);
		startplace.putExtra(getString(R.string.dbstring_longitude),
				String.valueOf(longitude));
		startplace.putExtra(getString(R.string.dbstring_latitude),
				String.valueOf(latitude));

		ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
		ChooseAddressActivity.this.finish();

		// intent end
	}


	//百度地图结束

	@Override
	protected void onStop() {
		baidulocation.Stop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			baidulocation.Destory();
			result.close();
			result2.close();
			db1.close();

			mSearch.destroy();
			// mPoiSearch.destroy();
			mSuggestionSearch.destroy();
		} catch (Throwable e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		baidulocation.Resume();

		// database intent 辅助
		Locationcount = 0;
		// database intent 辅助end

		// list赋值

		// database
		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，don't filter by row
		// groups，排序条件。
		result = db1.query(getString(R.string.dbtable_placeliked), null, null,
				null, null, null, null);
		Log.w("喜欢数量", String.valueOf(result.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result, new String[]{
				getString(R.string.dbstring_PlaceUserName),
				getString(R.string.dbstring_PlaceMapName)}, new int[]{
				R.id.re_address, R.id.detail});
		list1.setAdapter(adapter1);

		result2 = db1.query(getString(R.string.dbtable_placehistory), null,
				null, null, null, null, null);
		Log.w("历史数量", String.valueOf(result2.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter2 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result2, new String[]{
				getString(R.string.dbstring_PlaceUserName),
				getString(R.string.dbstring_PlaceMapName)}, new int[]{
				R.id.re_address, R.id.detail});

		list2.setAdapter(adapter2);

		// database end
		// list赋值结束

	}


}
