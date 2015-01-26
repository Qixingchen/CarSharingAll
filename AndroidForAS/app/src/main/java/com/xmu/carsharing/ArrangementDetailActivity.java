/*订单详情界面，由listitem点击传值生成的界面
 * 所显示的订单在进入该界面时访问一遍服务器以刷新状态
 */

package com.xmu.carsharing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import longwaylist_fragmenttabhost.MainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ArrangementDetailActivity extends Activity {

	public static RequestQueue queue;
	private float SPX, SPY, DSX, DSY;
	private String carsharing_type, requesttime;
	private TextView sp, ep, st, rs, userrole, dealstatus;
	private String UserPhoneNumber;
	private String startDate, endDate, startTime, endTime, weekrepeat,
			mdealstatus;
	private int Date[];
	private ImageView fanhui;
	private Button reorder;
	private SimpleDateFormat standard_date, standard_time, primary_date,
			primary_date1, primary_time;
	private Date test_date = new Date();
	private String primary_short_startdate = null,
			primary_short_starttime = null, primary_short_endtime = null,
			primary_commute_startdate = null, primary_commute_starttime = null,
			primary_commute_endtime = null, primary_commute_enddate = null,
			primary_longway_startdate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrangement_detail);

		Context phonenumber = ArrangementDetailActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		queue = Volley.newRequestQueue(this);

		reorder = (Button) findViewById(R.id.arrangement_reorder);
		fanhui = (ImageView) findViewById(android.R.id.home);
		sp = (TextView) findViewById(R.id.arrangementdetail_startaddress);
		ep = (TextView) findViewById(R.id.arrangementdetail_endaddress);
		st = (TextView) findViewById(R.id.arrangementdetail_starttime);
		rs = (TextView) findViewById(R.id.arrangementdetail_remainsites);
		userrole = (TextView) findViewById(R.id.arrangementdetail_userrole);
		dealstatus = (TextView) findViewById(R.id.arrangementdetail_orderstatus);

		final String role;

		// actionbar操作!!
		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionbarEND!!

		// 日期、时间标准格式
		standard_date = new SimpleDateFormat("yyyy-MM-dd",
				Locale.SIMPLIFIED_CHINESE);
		primary_date = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.SIMPLIFIED_CHINESE);
		primary_date1 = new SimpleDateFormat("yyyy年M月d日",
				Locale.SIMPLIFIED_CHINESE);
		standard_time = new SimpleDateFormat("HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		primary_time = new SimpleDateFormat("HH时mm分ss秒",
				Locale.SIMPLIFIED_CHINESE);

		Bundle bundle = this.getIntent().getExtras();
		sp.setText(bundle.getString("tsp")); // 起点
		ep.setText(bundle.getString("tep")); // 终点
		st.setText(bundle.getString("tst")); // 开始时间
		rs.setText(bundle.getString("trs")); // 需要座位
		role = bundle.getString("userrole");
		mdealstatus = bundle.getString("dealstatus");
		if ((role.compareTo("p") == 0)
				|| (bundle.getString("userrole").compareTo("n") == 0)) {// 身份
			userrole.setText("乘客");
		} else {
			userrole.setText("司机");
		}
		if (mdealstatus.compareTo("0") == 0) {// 订单状态
			dealstatus.setText("服务器正在尽快为您匹配，请稍等！");
		} else if (mdealstatus.compareTo("1") == 0) {
			dealstatus.setText("订单已匹配，请查收！");
		} else if (mdealstatus.compareTo("2") == 0) {
			dealstatus.setText("长途拼车");
		}

		final String stp[] = bundle.getString("tsp").split(",");
		final String ep[] = bundle.getString("tep").split(",");
		SPX = bundle.getFloat("SPX"); // 起点经度
		SPY = bundle.getFloat("SPY"); // 起点纬度
		DSX = bundle.getFloat("DSX"); // 终点经度
		DSY = bundle.getFloat("DSY"); // 终点纬度
		startDate = bundle.getString("startdate");
		endDate = bundle.getString("enddate");
		startTime = bundle.getString("starttime");
		endTime = bundle.getString("endtime");
		weekrepeat = bundle.getString("weekrepeat");
		carsharing_type = bundle.getString("carsharing_type");
		requesttime = bundle.getString("requesttime");

		// actionbar中返回键监听

		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				ArrangementDetailActivity.this.finish();
			}
		});
		// end

		// if (carsharing_type.compareTo("shortway") == 0) {
		// // 查询短途订单状态start!
		// shortway_updatestatus(UserPhoneNumber, requesttime);
		// // 查询短途订单状态end!
		// } else {
		// // 查询长途订单状态start!
		// commute_updatestatus(UserPhoneNumber, requesttime);
		// // 查询长途订单状态end!
		// }

		// reorder按钮的监听
		reorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (carsharing_type.compareTo("shortway") == 0) {
					Intent shortway = new Intent(
							ArrangementDetailActivity.this,
							ShortWayActivity.class);
					shortway.putExtra("stpusername", stp[0]);
					shortway.putExtra("stpmapname", stp[1]);
					shortway.putExtra("epusername", ep[0]);
					shortway.putExtra("epmapname", ep[1]);
					shortway.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					shortway.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					shortway.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					shortway.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					shortway.putExtra("userrole", role);
					shortway.putExtra("pre_page", "ReOrder");
					try {
						test_date = standard_date.parse(startDate);
						primary_short_startdate = primary_date
								.format(test_date);
						test_date = standard_time.parse(startTime);
						primary_short_starttime = primary_time
								.format(test_date);
						test_date = standard_time.parse(endTime);
						primary_short_endtime = primary_time.format(test_date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					shortway.putExtra("re_short_startdate",
							primary_short_startdate);
					shortway.putExtra("re_short_starttime",
							primary_short_starttime);
					shortway.putExtra("re_short_endtime", primary_short_endtime);
					startActivity(shortway);
				} else if (carsharing_type.compareTo("commute") == 0) {
					Intent commute = new Intent(ArrangementDetailActivity.this,
							CommuteActivity.class);
					commute.putExtra("stpusername", stp[0]);
					commute.putExtra("stpmapname", stp[1]);
					commute.putExtra("epusername", ep[0]);
					commute.putExtra("epmapname", ep[1]);
					commute.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					commute.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					commute.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					commute.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					try {
						test_date = standard_date.parse(startDate);
						primary_commute_startdate = primary_date1
								.format(test_date);
						test_date = standard_date.parse(endDate);
						primary_commute_enddate = primary_date1
								.format(test_date);
						test_date = standard_time.parse(startTime);
						primary_commute_starttime = primary_time
								.format(test_date);
						test_date = standard_time.parse(endTime);
						primary_commute_endtime = primary_time
								.format(test_date);
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					commute.putExtra("re_commute_startdate",
							primary_commute_startdate);
					commute.putExtra("re_commute_enddate",
							primary_commute_enddate);
					commute.putExtra("re_commute_starttime",
							primary_commute_starttime);
					commute.putExtra("re_commute_endtime",
							primary_commute_endtime);
					commute.putExtra("weekrepeat", weekrepeat);
					commute.putExtra("userrole", role);
					commute.putExtra("pre_page", "ReOrder");
					startActivity(commute);
				} else if (carsharing_type.compareTo("longway") == 0) {
					Intent longway = new Intent(ArrangementDetailActivity.this,
							LongWayActivity.class);
					longway.putExtra("stpmapname", stp[0]);
					longway.putExtra("epmapname", ep[0]);
					longway.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					longway.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					longway.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					longway.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					try {
						test_date = standard_date.parse(startDate);
						primary_longway_startdate = primary_date1
								.format(test_date);
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					longway.putExtra("re_longway_startdate",
							primary_longway_startdate);
					longway.putExtra("userrole", role);
					longway.putExtra("pre_page", "ReOrder");
					startActivity(longway);
				}
			}
		});

	}
	// private void commute_updatestatus(final String phonenum,
	// final String time){
	// 
	// String commute_updatestatus_baseurl =getString(R.string.uri_base)
	// + getString(R.string.uri_CommuteRequest)
	// +getString(R.string.uri_updatestatus_action);
	// //
	// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!updatestatus.action?";
	//
	// StringRequest stringRequest = new StringRequest(Request.Method.POST,
	// commute_updatestatus_baseurl,
	// new Response.Listener<String>() {
	//
	// @Override
	// public void onResponse(String response) {
	// 
	// // System.out.println("123");
	// Log.e("commute_updatestatus_result",response);
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// 
	// Log.e("shortway_updatestatus_result",
	// error.getMessage(), error);
	// }
	// })
	// {
	// protected Map<String, String> getParams() {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("phonenum", phonenum);
	// params.put("time", time);
	// return params;
	// }
	// }
	// ;
	// queue.add(stringRequest);
	// }
	//
	// private void shortway_updatestatus(final String phonenum,
	// final String time) {
	// 
	// String shortway_updatestatus_baseurl =getString(R.string.uri_base)
	// + getString(R.string.uri_ShortwayRequest)
	// +getString(R.string.uri_updatestatus_action);
	// //
	// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!updatestatus.action?";
	//
	// StringRequest stringRequest = new StringRequest(Request.Method.POST,
	// shortway_updatestatus_baseurl,
	// new Response.Listener<String>() {
	//
	// @Override
	// public void onResponse(String response) {
	// 
	// Log.e("shortway_updatestatus_result",response);
	// }
	// }, new Response.ErrorListener() {
	//
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// 
	// Log.e("shortway_updatestatus_result",
	// error.getMessage(), error);
	// }
	// }){
	// protected Map<String, String> getParams() {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("phonenum", phonenum);
	// params.put("time", time);
	// return params;
	// }
	// };
	// queue.add(stringRequest);
	// }

}