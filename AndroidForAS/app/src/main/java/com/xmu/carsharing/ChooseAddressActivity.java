/**
 * 选择起点界面，为上下班拼车，短途拼车，出租车拼车公用界面
 * 接入了百度接口
 * 两个ListView分别存储收藏的地址和历史地址，这些信息存在本地，会随着软件的卸载消失。
 * “我的位置”实现定位
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import com.Tool.DataBaseAct;
import com.Tool.ToolWithActivityIn;

public class ChooseAddressActivity extends Activity implements
		BaiduLocation.GetHignLocationCallBack, BaiduLocation.GetCityCallBack,
		BaiduMapClass.GetReverseGeoCoderCallBack, BaiduMapClass.GetGeoCoderCallBack {

	private EditText choose;
	private View position;
	private ImageView fanhui;
	private String logtag = "选择地点页面";

	//tool类
	ToolWithActivityIn toolWithActivityIn;

	// database
	private Cursor result, result2;
	private DataBaseAct databaseact;
	private ListView list1, list2;

	// database end!!

	// 用户手机号
	private String UserPhoneNumber;

	// 百度map
	private String PointUserName, PointMapName;
	double longitude, latitude;

	// 百度mapend

	// 百度定位
	private BaiduLocation baidulocation;
	private String UserCity;
	// 百度定位end

	//百度map

	private BaiduMapClass baidumapclass;

	// 百度map结束

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

		toolWithActivityIn = new ToolWithActivityIn(this);

		// 提取用户手机号
		UserPhoneNumber = toolWithActivityIn.get用户手机号从偏好文件();

		// database

		databaseact = new DataBaseAct(this,UserPhoneNumber);

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
					try{
						baidumapclass.GetGeoCoder(ChooseAddressActivity.this, PointUserName,
								city);
					}
					catch (Exception e){
						e.printStackTrace();
					}

					// baidumapend

				} else {
					SendResultCancel();
				}

			}
		});

		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			                        long arg3) {

				result.moveToPosition(arg2);

				PointUserName = result.getString(1);
				PointMapName = result.getString(2);
				longitude = result.getFloat(3);
				latitude = result.getFloat(4);

				SendResult(PointMapName,PointUserName,longitude,latitude);

			}

		});

		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			                        long arg3) {


				// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，
				// don't filter by row groups，排序条件。

				result2.moveToPosition(arg2);

				PointUserName = result2.getString(1);
				PointMapName = result2.getString(2);
				longitude = result2.getFloat(3);
				latitude = result2.getFloat(4);

				SendResult(PointMapName,PointUserName,longitude,latitude);

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

		databaseact.if位于历史或偏爱记录并添加记录(PointMapName, PointUserName, longitude, latitude);

		//数据库操作end

		// intent

		SendResult(PointMapName,PointUserName,longitude,latitude);

		// intent end


	}

		//获取城市回调函数
	@Override
	public String getcityname(String mcity) {
		UserCity = mcity;
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
		databaseact.if位于历史或偏爱记录并添加记录(PointMapName, choose.getText().toString(),
				longitude, latitude);
		// database end

		// intent

		SendResult(PointMapName, PointUserName, longitude, latitude);

		// intent end
	}


	//百度地图结束


	//结果回报
	private void SendResultCancel(){
		Intent startplace = new Intent();
		ChooseAddressActivity.this.setResult(RESULT_CANCELED,
				startplace);
		ChooseAddressActivity.this.finish();
	}
	private void SendResult(String mapname, String UserMapname, double longitude,
	                        double latitude){
		Intent startplace = new Intent();

		startplace.putExtra(getString(R.string.dbstring_PlaceUserName),
				UserMapname);
		startplace.putExtra(getString(R.string.dbstring_PlaceMapName),
				mapname);
		startplace.putExtra(getString(R.string.dbstring_longitude),
				String.valueOf(longitude));
		startplace.putExtra(getString(R.string.dbstring_latitude),
				String.valueOf(latitude));

		Log.w(logtag+"发送经度", startplace
				.getStringExtra(getString(R.string.dbstring_longitude)));
		Log.w(logtag+"发送纬度", startplace
				.getStringExtra(getString(R.string.dbstring_latitude)));
		ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
		ChooseAddressActivity.this.finish();
	}


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
			databaseact.Destory();
		} catch (Throwable e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		baidulocation.Resume();

		// list赋值

		// database
		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，don't filter by row
		// groups，排序条件。
		result = databaseact.showAll偏好地点();

		@SuppressWarnings("deprecation")
		ListAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result, new String[]{
				getString(R.string.dbstring_PlaceUserName),
				getString(R.string.dbstring_PlaceMapName)}, new int[]{
				R.id.re_address, R.id.detail});
		list1.setAdapter(adapter1);

		result2 = databaseact.showAll历史地点();

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
