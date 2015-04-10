package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xmu.carsharing.OrderResponseActivity;
import com.xmu.carsharing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 雨蓝 on 2015/4/5.
 * 订单服务器通讯类
 */
public class ServerSubmit {

	//生命周期量
	private Activity activity;
	private Context context;
	private Application application;
	private String logtag = "服务器提交";

	//vollery
	private RequestQueue queue;


	public ServerSubmit(Activity mact) {
		activity = mact;
		context = mact.getApplicationContext();
		application = mact.getApplication();
		init();
	}

	private void init() {
		queue = Volley.newRequestQueue(context);
	}

	//参数要求:用户电话,开始日期,结束日期,开始时间,结束时间,周期重复字符串,开始经纬度,结束经纬度,开始地点,结束地点,
	public void commute订单提交(final String userphoneNum,
	                        final String commute_startdate, final String commute_enddate,
	                        final String commute_starttime, final String commute_endtime,
	                        final String weekrepeat,
	                        final double startplace_longitude,
	                        final double startplace_latitude,
	                        final double destination_longitude,
	                        final double destination_latitude,
	                        final String startplace,
	                        final String endplace, final String userrole) {

		final String time已经格式化[] = time格式化操作(commute_startdate, commute_enddate,
				commute_starttime, commute_endtime);


		String commute_baseurl = activity.getString(R.string.uri_base)
				+ activity.getString(R.string.uri_CommuteRequest)
				+ activity.getString(R.string.uri_addrequest_action);

		Log.e("commute_URL", commute_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, commute_baseurl,
				new Response.Listener<String>() {

					boolean requestok;

					@Override
					public void onResponse(String response) {
						Log.d("commute_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							requestok = json1.getBoolean("result");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (requestok == true) {
							Log.e("statue", "requestok");
							Intent sure = new Intent(
									activity, OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"true");
							sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(sure);
						} else {
							Intent sure = new Intent(
									activity, OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"false");
							sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(sure);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("commute_result", error.getMessage(),
						error);
				Intent sure = new Intent(activity, OrderResponseActivity.class);
				sure.putExtra(
						activity.getString(R.string.request_response),
						"false");
				sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(sure);
			}
		}) {
			protected Map<String, String> getParams() {
				// POST方法重写getParams函数


				Map<String, String> params = new HashMap<>();
				params.put(activity.getString(R.string.uri_phonenum),
						userphoneNum);
				params.put(activity.getString(R.string.uri_startplacex),
						String.valueOf(startplace_longitude));
				params.put(activity.getString(R.string.uri_startplacey),
						String.valueOf(startplace_latitude));
				params.put(activity.getString(R.string.uri_startplace),
						startplace);
				params.put(activity.getString(R.string.uri_destinationx),
						String.valueOf(destination_longitude));
				params.put(activity.getString(R.string.uri_destinationy),
						String.valueOf(destination_latitude));
				params.put(activity.getString(R.string.uri_destination),
						endplace);
				params.put(activity.getString(R.string.uri_startdate),
						time已经格式化[AppStat.time格式化数组序号.最早日期]);
				params.put(activity.getString(R.string.uri_enddate),
						time已经格式化[AppStat.time格式化数组序号.最晚日期]);
				params.put(activity.getString(R.string.uri_starttime),
						time已经格式化[AppStat.time格式化数组序号.最早时间]);
				params.put(activity.getString(R.string.uri_endtime),
						time已经格式化[AppStat.time格式化数组序号.最晚时间]);
				params.put(activity.getString(R.string.uri_weekrepeat),
						weekrepeat);
				params.put(activity.getString(R.string.uri_supplycar), userrole);

				return params;
			}


		};
		queue.add(stringRequest);
	}

	//参数要求:用户电话,日期,开始时间,结束时间,周期重复字符串,开始经纬度,结束经纬度,开始地点,结束地点,
	public void shortway订单提交(final String shortway_phonenum,
	                         final String shortway_date,
	                         final String shortway_starttime,
	                         final String shortway_endtime,final String userrole,
	                         final double startplace_longitude,
	                         final double startplace_latitude,
	                         final double destination_longitude,
	                         final double destination_latitude,
	                         final String startplace,final String endplace
	                         ){

		String shortway_baseurl = activity.getString(R.string.uri_base)
				+ activity.getString(R.string.uri_ShortwayRequest)
				+ activity.getString(R.string.uri_addrequest_action);

		Log.w("URL", shortway_baseurl);
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, shortway_baseurl,
				new Response.Listener<String>() {

					boolean requestok;

					@Override
					public void onResponse(String response) {
						Log.d("shortway_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							requestok = json1.getBoolean("result");
						} catch (JSONException e) {

							e.printStackTrace();
						}
						if (requestok == true) {
							Intent sure = new Intent(
									activity,
									OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"true");
							sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(sure);
						} else {
							Intent sure = new Intent(
									activity,
									OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"false");
						}
					}

				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("shortway_result", error.getMessage(),
						error);
				// Toast errorinfo = Toast.makeText(null,
				// "网络连接失败", Toast.LENGTH_LONG);
				// errorinfo.show();
				Intent sure = new Intent(activity,
						OrderResponseActivity.class);
				sure.putExtra(
						activity.getString(R.string.request_response),
						"false");
				sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(sure);
			}
		}) {
			protected Map<String, String> getParams() {
				// POST方法重写getParams函数

				// 强制转换日期格式start
				String time已经格式化[]=time格式化操作(shortway_date,shortway_date,
						shortway_starttime,shortway_endtime);
				// 强制转换日期格式end!

				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", shortway_phonenum);
				params.put("userrole", userrole);
				params.put("startplacex",
						String.valueOf(startplace_longitude));
				params.put("startplacey",
						String.valueOf(startplace_latitude));
				params.put(activity.getString(R.string.uri_startplace),startplace);
				params.put("destinationx",
						String.valueOf(destination_longitude));
				params.put("destinationy",
						String.valueOf(destination_latitude));
				params.put(activity.getString(R.string.uri_destination),endplace);
				params.put("startdate", time已经格式化[AppStat.time格式化数组序号.最早日期]);
				params.put("starttime", time已经格式化[AppStat.time格式化数组序号.最早时间]);
				params.put("endtime", time已经格式化[AppStat.time格式化数组序号.最晚时间]);

				return params;
			}
		};

		queue.add(stringRequest);



	}

	public void longway订单提交(final String longway_phonenum,
	                             final String longway_userrole,
	                             final String longway_startdate,
	                             final String longway_startplace,
	                             final String longway_destination,
	                             final String longway_noteinfo) {

		String longway_addrequest_baseurl = activity.getString(R.string.uri_base)
				+ activity.getString(R.string.uri_LongwayPublish)
				+ activity.getString(R.string.uri_addpublish_action);

		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, longway_addrequest_baseurl,
				new Response.Listener<String>() {

					boolean requestok;
					@Override
					public void onResponse(String response) {
						Log.d("longway_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							requestok = json1.getBoolean("result");
						} catch (JSONException e) {

							e.printStackTrace();
						}
						if (requestok == true) {

							Intent sure = new Intent(
									activity,OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"true");
							sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(sure);
						} else {
							// Toast errorinfo =
							// Toast.makeText(getApplicationContext(),
							// "提交失败", Toast.LENGTH_LONG);
							// errorinfo.show();
							Intent sure = new Intent(
									activity,OrderResponseActivity.class);
							sure.putExtra(
									activity.getString(R.string.request_response),
									"false");
							sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							activity.startActivity(sure);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("longway_result", error.getMessage(),
						error);
				// Toast errorinfo = Toast.makeText(null,
				// "网络连接失败", Toast.LENGTH_LONG);
				// errorinfo.show();
				Intent sure = new Intent(activity,
						OrderResponseActivity.class);
				sure.putExtra(
						activity.getString(R.string.request_response),
						"false");
				sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(sure);
			}
		}) {
			protected Map<String, String> getParams() {
				// POST方法重写getParams函数

				Date temp_date;
				String standard_startdate = null;
				// 强制转换日期格式start
				try {
					temp_date = AppStat.time格式化.yyyy年MM月dd日.parse(longway_startdate);
					standard_startdate = AppStat.time格式化.yyyy_MM_dd.format(temp_date);
				} catch (ParseException e) {

					e.printStackTrace();
				}

				// 强制转换日期格式end!

				Map<String, String> params = new HashMap<String, String>();
				params.put(activity.getString(R.string.uri_phonenum),
						longway_phonenum);
				params.put(activity.getString(R.string.uri_userrole),
						longway_userrole);
				params.put(activity.getString(R.string.uri_startplace),
						longway_startplace);
				params.put(activity.getString(R.string.uri_destination),
						longway_destination);
				params.put(activity.getString(R.string.uri_startdate),
						standard_startdate);
				params.put(activity.getString(R.string.uri_noteinfo),
						longway_noteinfo);

				return params;
			}
		};
		queue.add(stringRequest);

	}

	//参数要求:IdentityBtn 用户电话,驾照编号,车品牌,车类型,车颜色,sum是什么....
	public void car信息提交(IdentityBtn function_identity, String UserPhoneNum,
	                    String licensenum, String carbrand, String model, String color, int sum) {
		if (function_identity.carinfochoosing_type == 1) {
			// add
			car信息提交辅助(UserPhoneNum, licensenum, carbrand, model, color, String
					.valueOf(sum), 1);
		} else {
			// update
			car信息提交辅助(UserPhoneNum, licensenum, carbrand, model, color, String
					.valueOf(sum), 2);
		}
	}


	private void car信息提交辅助(final String phonenum, final String carnum,
	                       final String carbrand, final String carmodel,
	                       final String carcolor, final String car_capacity, int type) {


		String carinfotype;
		if (type == 1) {
			carinfotype = activity.getString(R.string.uri_addcarinfo_action);
		} else {
			carinfotype = activity.getString(R.string.uri_updatecarinfo_action);
		}

		String carinfo_baseurl = activity.getString(R.string.uri_base)
				+ activity.getString(R.string.uri_CarInfo) + carinfotype;
		Log.d("carinfo_URL", carinfo_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_baseurl, new Response.Listener<String>() {
			boolean carinfook;

			@Override
			public void onResponse(String response) {
				Log.d("carinfo_result", response);
				JSONObject json1 = null;
				try {
					json1 = new JSONObject(response);
					carinfook = json1.getBoolean("result");
				} catch (JSONException e) {

					e.printStackTrace();
				}
				if (carinfook == false) {
					Toast errorinfo = Toast.makeText(
							context, "车辆信息修改失败",
							Toast.LENGTH_LONG);
					errorinfo.show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("carinfo_result", error.getMessage(), error);
			}
		}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("carnum", carnum);
				params.put("phonenum", phonenum);
				params.put("carbrand", carbrand);
				params.put("carmodel", carmodel);
				params.put("carcolor", carcolor);
				params.put("capacity", car_capacity);
				return params;
			}
		};

		queue.add(stringRequest);
	}

	//格式化时间类
	private String[] time格式化操作(String startdate, String enddate, String starttime,
	                           String endtime) {
		String[] time格式化后 = new String[4];
		Date temp_date;

		try {
			temp_date = AppStat.time格式化.yyyy年MM月dd日.parse(enddate);
			time格式化后[AppStat.time格式化数组序号.最晚日期] = AppStat.time格式化.yyyy_MM_dd.format(temp_date);


			temp_date = AppStat.time格式化.yyyy年MM月dd日.parse(startdate);
			time格式化后[AppStat.time格式化数组序号.最早日期] = AppStat.time格式化.yyyy_MM_dd.format(temp_date);


			temp_date = AppStat.time格式化.HH时mm分ss秒.parse(starttime);
			time格式化后[AppStat.time格式化数组序号.最早时间] = AppStat.time格式化.HH_mm_ss.format(temp_date);


			temp_date = AppStat.time格式化.HH时mm分ss秒.parse(endtime);
			time格式化后[AppStat.time格式化数组序号.最晚时间] = AppStat.time格式化.HH_mm_ss.format(temp_date);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return time格式化后;
	}

}
