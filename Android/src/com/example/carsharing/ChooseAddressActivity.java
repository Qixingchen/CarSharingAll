/*
 * ѡ�������棬Ϊ���°�ƴ������;ƴ�������⳵ƴ�����ý���
 * �����˰ٶȽӿ�
 * ����ListView�ֱ�洢�ղصĵ�ַ����ʷ��ַ����Щ��Ϣ���ڱ��أ������������ж����ʧ��
 * ���ҵ�λ�á�ʵ�ֶ�λ
 */

package com.example.carsharing;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
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
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class ChooseAddressActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
		OnGetGeoCoderResultListener {

	EditText choose;
	int intentcall;
	View position;
	ImageView fanhui;
	Context context;

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor result, result2;

	ListView list1, list2;

	// database end!!

	// �û��ֻ���
	String UserPhoneNumber;

	// �ٶ�map
	String PointUserName, PointMapName;
	float longitude, latitude;

	// private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	/**
	 * �����ؼ������봰��
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;

	// �ٶ�mapend

	// �ٶȶ�λ

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	String UserCity, city;
	LocationClientOption option;

	// �ٶȶ�λend

	// database intent ����
	int Locationcount;

	// database intent ����end

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
				// TODO Auto-generated method stub
				ChooseAddressActivity.this.finish();
			}
		});

		// ��ȡ�û��ֻ���
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

		// �ٶȵ�ͼ����

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());

		// ��ʼ������ģ�飬ע�������¼�����
		// mPoiSearch = PoiSearch.newInstance();
		// mPoiSearch.setOnGetPoiSearchResultListener(this);

		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.chooseaddress_start);
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
				 * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});

		// �ٶȵ�ͼend

		// �ٶȶ�λ

		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		mLocationClient.start();

		// �ٶȶ�λend

		// actionbar����!!

		// ��������!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		position.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// �ٶȶ�λ

				Toast.makeText(getApplicationContext(),
						getString(R.string.warningInfo_waitForLocation),
						Toast.LENGTH_SHORT).show();

				LocationClientOption option = new LocationClientOption();
				option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
				option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				option.setScanSpan(1024);// ���÷���λ����ļ��ʱ��Ϊ5000ms
				option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
				mLocationClient.setLocOption(option);
				if (mLocationClient != null && mLocationClient.isStarted()) {
					mLocationClient.requestLocation();
				} else if (mLocationClient == null) {
					Log.e("�ٶȶ�λ", "��λ�ͻ��˿�");
				} else {
					Log.e("�ٶȶ�λ", "��λ�ͻ���û����");
				}
				// �ٶȶ�λ����

				// Intent myposition = new Intent(ChooseAddressActivity.this,
				// FindPositionActivity.class);
				// startActivity(myposition);
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �ٶ�map
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
				// TODO Auto-generated method stub
				Intent startplace = new Intent();

				// ���� ,Ҫ��ȡ���ֶ�����WHERE ������WHereֵ��don't group the rows��
				// don't filter by row groups������������

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
				Log.w("STintent���;���", startplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent����γ��", startplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
				ChooseAddressActivity.this.finish();

			}

		});

		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent startplace = new Intent();

				// ���� ,Ҫ��ȡ���ֶ�����WHERE ������WHereֵ��don't group the rows��
				// don't filter by row groups������������

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
				Log.w("STintent���;���", startplace
						.getStringExtra(getString(R.string.dbstring_longitude)));
				Log.w("STintent����γ��", startplace
						.getStringExtra(getString(R.string.dbstring_latitude)));
				ChooseAddressActivity.this.setResult(RESULT_OK, startplace);
				ChooseAddressActivity.this.finish();

			}
		});

		// intent & databaseend!!

	}

	// �ٶȵ؈D�_ʼ

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

			// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
			String strInfo = "��";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "�ҵ����";
			Toast.makeText(ChooseAddressActivity.this, strInfo,
					Toast.LENGTH_LONG).show();
		}
	}

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ChooseAddressActivity.this,
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
		String strInfo = String.format("γ�ȣ�%f ���ȣ�%f",
				result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
		Log.w("��γ��", strInfo);
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

		// ���� ,Ҫ��ȡ���ֶ�����WHERE ������WHereֵ��don't group the rows��
		// don't filter by row groups������������

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
				Log.w("��ʷ���ݿ�",
						"���" + PointUserName + "  map:" + PointMapName
								+ String.valueOf(longitude) + "&"
								+ String.valueOf(latitude));

			}
			// debug!!!
			// else {
			// dbresult.moveToFirst();
			// Log.w("��ʷ����", String.valueOf(dbresult.getFloat(3)));
			// Log.w("��ʷγ��", String.valueOf(dbresult.getFloat(4)));
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

	// �ٶȵ؈D�Y��

	// �ٶȶ�λ

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
			PointUserName = location.getTime() + "ʱ��λ��";
			longitude = (float) location.getLongitude();
			latitude = (float) location.getLatitude();

			if (location.getCity() == null) {
				Log.e("������", "��!");
			} else {
				Log.w("������", location.getCity());
				UserCity = location.getCity();
			}

			Log.w("�ٶȶ�λ", sb.toString());
			// database intent ����
			Locationcount++;
			Log.w("��λ����", String.valueOf(Locationcount));

			// database intent ����end

			// database

			// ���� ,Ҫ��ȡ���ֶ�����WHERE ������WHereֵ��don't group the rows��
			// don't filter by row groups������������

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
						Log.w("��ʷ���ݿ�", "���" + PointUserName + "  map:"
								+ PointMapName + String.valueOf(longitude)
								+ "&" + String.valueOf(latitude));

					}
					dbresult.close();
				}

				// database end

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
		}
	}

	// �ٶȶ�λ����

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
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
		// mPoiSearch.destroy();
		mSuggestionSearch.destroy();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		mLocationClient.start();

		// database intent ����
		Locationcount = 0;
		// database intent ����end
		// �ٶȶ�λ

		option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(0);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		mLocationClient.setLocOption(option);
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			Log.w("�ٶȶ�λ", "��λ�ͻ��˿ջ�û����");
		}
		// �ٶȶ�λ����

		// list��ֵ

		// database
		// ���� ,Ҫ��ȡ���ֶ�����WHERE ������WHereֵ��don't group the rows��don't filter by row
		// groups������������
		result = db1.query(getString(R.string.dbtable_placeliked), null, null,
				null, null, null, null);
		Log.w("ϲ������", String.valueOf(result.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result, new String[] {
						getString(R.string.dbstring_PlaceUserName),
						getString(R.string.dbstring_PlaceMapName) }, new int[] {
						R.id.re_address, R.id.detail });
		list1.setAdapter(adapter1);

		result2 = db1.query(getString(R.string.dbtable_placehistory), null,
				null, null, null, null, null);
		Log.w("��ʷ����", String.valueOf(result2.getCount()));

		@SuppressWarnings("deprecation")
		ListAdapter adapter2 = new SimpleCursorAdapter(this,
				R.layout.choose_start_vlist, result2, new String[] {
						getString(R.string.dbstring_PlaceUserName),
						getString(R.string.dbstring_PlaceMapName) }, new int[] {
						R.id.re_address, R.id.detail });

		list2.setAdapter(adapter2);

		// database end
		// list��ֵ����

	}

}
