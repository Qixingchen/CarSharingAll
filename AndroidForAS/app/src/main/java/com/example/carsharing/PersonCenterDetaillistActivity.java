/*
 * “更多”
 * 为个人中心三个“点击查看”共用
 * 1.intentcall=1  监听每一条item，点击该条item时按序访问服务器的三种类型订单，根据请求时间获取具体订单信息，传值到订单详情界面
 * 2.intentcall=2
 * 3.intentcall=3 调用适配器显示在本地已存储的收藏的地址
 */

package com.example.carsharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PersonCenterDetaillistActivity extends Activity {

	DatabaseHelper db;
	SQLiteDatabase db1;
	ListView list1;
	Button deletebtn;
	// private Vector<String> mdeal_readstatus = new Vector<>();
	private ImageView statusImage;
	public static RequestQueue queue;
	private String UserPhoneNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_center_detaillist);

		View itemView = View.inflate(PersonCenterDetaillistActivity.this,
				R.layout.dealstatus_listitem, null);
		statusImage = (ImageView) itemView.findViewById(R.id.list_dealstatus);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		queue = Volley.newRequestQueue(this);
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);

		UserPhoneNumber = sharedPref.getString("refreshfilename", "0");
		db = new DatabaseHelper(PersonCenterDetaillistActivity.this,
				UserPhoneNumber, null, 1);

		deletebtn = (Button) findViewById(R.id.mymessage_delete);
		list1 = (ListView) findViewById(R.id.WacthAllMessSent);

		int intentcall = bundle.getInt("intent");

		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		// 绑定XML中的ListView，作为Item的容器
		ListView list = (ListView) findViewById(R.id.WacthAllMessSent);
		// 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();

		if (1 == intentcall) {

			MyAdapter sAdapter_messSent = new MyAdapter(this,
					PersonalCenterActivity.mylist1, 1); // 数据来源
			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// Item监听跳转start!
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					String requesttime = PersonalCenterActivity.mylist1.get(
							position).get("requst");
					shortway_selectrequest(UserPhoneNumber, requesttime);
				}
			});
			// Item监听跳转end!

		}

		if (2 == intentcall) {

			// Bundle dealbundle = this.getIntent().getExtras();
			// deal_readstatus = dealbundle.getString("deal_readstatus");

			// 生成适配器，数组===》ListItem
			// MyAdapter sAdapter_messSent = new MyAdapter(this,
			// PersonalCenterActivity.mylist2, 2); // 数据来源
			SimpleAdapter sAdapter_messSent = new SimpleAdapter(this,
					PersonalCenterActivity.mylist2,
					R.layout.dealstatus_listitem, new String[] { "Title",
							"text", "requst", "deal_readstatus",
							"deal_readstatusIcon" }, new int[] { R.id.dealtime,
							R.id.dealkind, R.id.dealid, R.id.dealstatus,
							R.id.list_dealstatus });

			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// 监听item
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub

					// (array.size() < position)
					// array.add(position,String);
					// if((mdeal_readstatus[position].compareTo("receive") == 0)
					// || (mdeal_readstatus[position].compareTo("reject") == 0)
					// ){
					// statusImage.setImageResource(R.drawable.ic_dealread);
					// }else if(mdeal_readstatus[position].compareTo("unread")
					// == 0){
					// statusImage.setImageResource(R.drawable.ic_dealunread);
					// }

					Intent intent = new Intent(
							PersonCenterDetaillistActivity.this,
							RoutelineDisplayActivity.class);
					intent.putExtra(
							"dealid",
							(String) PersonalCenterActivity.mylist2.get(
									position).get("requst"));
					intent.putExtra(
							"deal_readstatus",
							(String) PersonalCenterActivity.mylist2.get(
									position).get("deal_readstatus"));
					// intent.putEx
					startActivity(intent);
					// startActivityForResult(intent,1);
				}
			});
		}

		if (3 == intentcall) {

			MyAdapter sAdapter_messSent = new MyAdapter(this,
					PersonalCenterActivity.mylist3, 3); // 数据来源
			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// 生成适配器，数组===》ListItem

		}

	}

	private void shortway_selectrequest(final String phonenum,
			final String request) {
		// TODO Auto-generated method stub
		String shortway_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_ShortwayRequest)
				+ getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				shortway_selectrequest_baseurl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.w("selectrequest_result", response);
						try {
							int i;
							JSONObject jasitem;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								if (jasitem.getString("requestTime").equals(
										request)) {

									Bundle bundle = new Bundle();
									Intent intent = new Intent(
											PersonCenterDetaillistActivity.this,
											ArrangementDetailActivity.class);

									bundle.putString("carsharing_type",
											"shortway");

									bundle.putString("requesttime", request);

									bundle.putFloat("SPX", Float
											.parseFloat(jasitem
													.getString("startPlaceX")));
									Log.e("startPlaceX",
											jasitem.getString("startPlaceX"));
									bundle.putFloat("SPY", Float
											.parseFloat(jasitem
													.getString("startPlaceY")));
									Log.e("startPlaceY",
											jasitem.getString("startPlaceY"));
									bundle.putFloat("DSX", Float
											.parseFloat(jasitem
													.getString("destinationX")));
									Log.e("destinationX",
											jasitem.getString("destinationX"));
									bundle.putFloat("DSY", Float
											.parseFloat(jasitem
													.getString("destinationY")));
									Log.e("destinationY",
											jasitem.getString("destinationY"));
									bundle.putString("tsp",
											jasitem.getString("startPlace"));
									bundle.putString("tep",
											jasitem.getString("destination"));
									bundle.putString(
											"tst",
											jasitem.getString("startDate")
													+ " "
													+ jasitem
															.getString("startTime")
													+ "-"
													+ jasitem
															.getString("endTime"));
									bundle.putString("startdate",
											jasitem.getString("startDate"));
									bundle.putString("starttime",
											jasitem.getString("startTime"));
									bundle.putString("endtime",
											jasitem.getString("endTime"));
									bundle.putString("trs", "xx");
									bundle.putString("dealstatus",
											jasitem.getString("dealStatus"));
									bundle.putString("userrole",
											jasitem.getString("userRole"));
									// Log.e("userrole",jasitem.getString("userRole"));
									intent.putExtras(bundle);
									startActivity(intent);
									break;
								}
							}
							if (i == jasA.length()) {
								commute_selectrequest(phonenum, request);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("shortway_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "网络连接失败",
						// Toast.LENGTH_LONG);
						// errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				return params;
			}
		};
		queue.add(stringRequest);

	}

	private void commute_selectrequest(final String phonenum,
			final String request) {
		// TODO Auto-generated method stub
		String commute_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CommuteRequest)
				+ getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				commute_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("commute_selectrequest_result", response);
						try {
							int i;
							Bundle bundle = new Bundle();
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								if (jasitem.getString("requestTime").equals(
										request)) {

									bundle.putString("carsharing_type",
											"commute");

									bundle.putString("requesttime", request);

									bundle.putFloat("SPX", Float
											.parseFloat(jasitem
													.getString("startPlaceX")));
									bundle.putFloat("SPY", Float
											.parseFloat(jasitem
													.getString("startPlaceY")));
									bundle.putFloat("DSX", Float
											.parseFloat(jasitem
													.getString("destinationX")));
									bundle.putFloat("DSY", Float
											.parseFloat(jasitem
													.getString("destinationY")));
									bundle.putString("tsp",
											jasitem.getString("startPlace"));
									bundle.putString("tep",
											jasitem.getString("destination"));
									bundle.putString(
											"tst",
											jasitem.getString("startDate")
													+ "至"
													+ jasitem
															.getString("endDate")
													+ "  "
													+ jasitem
															.getString("startTime")
													+ "-"
													+ jasitem
															.getString("endTime")
													+ "  "
													+ "每周:"
													+ jasitem
															.getString("weekRepeat"));
									bundle.putString("startdate",
											jasitem.getString("startDate"));
									bundle.putString("enddate",
											jasitem.getString("endDate"));
									bundle.putString("starttime",
											jasitem.getString("startTime"));
									bundle.putString("endtime",
											jasitem.getString("endTime"));
									bundle.putString("weekrepeat",
											jasitem.getString("weekRepeat"));
									bundle.putString("trs", "xx");
									bundle.putString("dealstatus",
											jasitem.getString("dealStatus"));
									bundle.putString("userrole",
											jasitem.getString("supplyCar"));
									break;
								}
							}
							if (i == jasA.length()) {
								longway_selectrequest(phonenum, request);
							} else {
								Intent intent = new Intent(
										PersonCenterDetaillistActivity.this,
										ArrangementDetailActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("commute_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "网络连接失败",
						// Toast.LENGTH_LONG);
						// errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				return params;
			}
		};

		queue.add(stringRequest);

	}

	public void longway_selectrequest(final String phonenum,
			final String request) {
		// TODO Auto-generated method stub
		String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_LongwayPublish)
				+ getString(R.string.uri_selectpublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("longwayway_selectpublish_result", response);
						try {
							int i;
							Bundle bundle = new Bundle();
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								if (jasitem.getString("publishTime").equals(
										request)) {

									bundle.putString("carsharing_type",
											"longway");

									bundle.putString("requesttime", request);

									bundle.putString("tsp",
											jasitem.getString("startPlace"));
									bundle.putString("tep",
											jasitem.getString("destination"));
									bundle.putString("tst",
											jasitem.getString("startDate"));
									bundle.putString("trs", "xx");
									bundle.putString("userrole",
											jasitem.getString("userRole"));
									bundle.putString("startdate",
											jasitem.getString("startDate"));
									bundle.putString("dealstatus", "2");
									break;
								}
							}
							if (i == jasA.length()) {
								Toast.makeText(getApplicationContext(),
										"该订单已不存在", Toast.LENGTH_SHORT).show();
							} else {
								Intent intent = new Intent(
										PersonCenterDetaillistActivity.this,
										ArrangementDetailActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("commute_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "网络连接失败",
						// Toast.LENGTH_LONG);
						// errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				return params;
			}
		};

		queue.add(stringRequest);

	}
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if(requestCode==1){
	// switch(resultCode){
	// case 1 :{//接受或拒绝
	// Log.e("aaa","a");
	// statusImage.setImageResource(R.drawable.ic_dealread);
	// break;
	// }
	// default:{
	// Log.e("bbb","b");
	// statusImage.setImageResource(R.drawable.ic_dealunread);
	// break;
	// }
	// }
	// }
	//
	// }

}
