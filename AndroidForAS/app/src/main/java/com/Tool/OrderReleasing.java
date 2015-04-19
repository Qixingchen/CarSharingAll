package com.Tool;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xmu.carsharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * “我发布过的订单”
 * 有两个activity在调用：个人中心和个人中心详情界面
 * Created by Jo on 2015/3/24.
 */
public class OrderReleasing {

	private Activity mactivity;
	private String Logtag = "我发布过的订单";

	private boolean WriteToDb_ok = false, empty = true; //empty:标志数据库是否为空。空：true

	private static float[] longitude_latitude = new float[4];
	//数组按序为：startplaceX,startplaceY,endplaceX,endplaceY

	private static String[] date_time = new String[4];
	//数组按序为：startdate,enddate,starttime,endtime（没有就赋值为NULL）

	private static String[] place_name = new String[2];
	//数组按序为：startplace's name ,endplace's name

	private String carsharing_type, dealstatus, userrole, weekrepeat = "",
			Date_time_list = "", //起始日期和时间
			display_item = "", requesttime = "";
	private int rest_seats;
	private static ArrayList<HashMap<String, String>> mylist1_0 = new
			ArrayList<HashMap<String, String>>();

	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;
	private DataBaseAct dbact;

	private RequestQueue queue;

	//回调函数
	private GetordersCallBack getordersCallBack;
	private GetPairedOrderCallBack getPairedOrderCallBack;


	public OrderReleasing(Activity act) {
		this.mactivity = act;
		getPhone = new ToolWithActivityIn(mactivity);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();
		dbact = new DataBaseAct(mactivity, UserPhoneNumber);
	}

	private void longway_selectrequest(final String phonenum) {

		String longwayway_selectpublish_baseurl = mactivity.getString(R.string.uri_base)
				+ mactivity.getString(R.string.uri_LongwayPublish)
				+ mactivity.getString(R.string.uri_selectpublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w(Logtag + "长途", response);
						try {
							int i;
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							Log.e("jasAlength_longway", String.valueOf(jasA.length()));
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								getJson(jasitem, "longway");
							}
							WriteToDb_ok = true;
							Log.e("write_ok_oerderreleas?", String.valueOf(WriteToDb_ok));
							if (jasA.length() != 0)  //标志此数据表是否为空
								if (empty = true) empty = false;
							getordersCallBack.getordersCallBack(WriteToDb_ok, empty);

						} catch (JSONException e) {

							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(Logtag + "长途",
						error.getMessage(), error);
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

	private void commute_selectrequest(final String phonenum) {

		String commute_selectrequest_baseurl = mactivity.getString(R.string.uri_base)
				+ mactivity.getString(R.string.uri_CommuteRequest)
				+ mactivity.getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				commute_selectrequest_baseurl, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.w(Logtag + "上下班", response);
				try {
					int i;
					JSONObject jasitem = null;
					JSONObject jas = new JSONObject(response);
					JSONArray jasA = jas.getJSONArray("result");

					Log.e("jasAlength_commute", String.valueOf(jasA.length()));
					for (i = 0; i < jasA.length(); i++) {
						jasitem = jasA.getJSONObject(i);
						getJson(jasitem, "commute");
					}
					if (jasA.length() != 0)  //标志此数据表是否为空
						if (empty = true) empty = false;
					longway_selectrequest(phonenum);
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(Logtag + "上下班",
						error.getMessage(), error);
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

	private void shortway_selectrequest(final String phonenum) {

		String shortway_selectrequest_baseurl = mactivity.getString(R.string.uri_base)
				+ mactivity.getString(R.string.uri_ShortwayRequest)
				+ mactivity.getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				shortway_selectrequest_baseurl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.w("selectrequest_result", response);
						try {
							int i = 0;
							JSONObject jasitem;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");

							Log.e("jasAlength_shortway", String.valueOf(jasA.length()));
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								getJson(jasitem, "shortway");
							}
							if (jasA.length() != 0)  //标志此数据表是否为空
								if (empty = true) empty = false;
							commute_selectrequest(phonenum);

                    /*------------PersonalCenterActivity .java---start--------------------*/
						/*	if (act == AppStat.is个人中心Or详情界面.个人中心) {
								for (i = 0; i < jasA.length(); i++) {
									jasitem = jasA.getJSONObject(i);
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("Title", jasitem.getString("startDate"));
									map.put("text",
											jasitem.getString("startPlace")
													+ "  "
													+ " 至  "
													+ "  "
													+ jasitem
													.getString("destination")
													+ "  ");
									map.put("requst",
											jasitem.getString("requestTime"));
									mylist1_0.add(map);
									if (bfirsthistory == false) {

										firstItem_type = "shortway";
										startplace = jasitem.getString(
												"startPlace").split(",");
										endplace = jasitem.getString(
												"destination").split(",");

										bfirsthistory = true;
									}

								}
								if (bfirsthistory == true)
									Log.e("历史订单第一条记录:", "shortwayyes");
								else
									Log.e("历史订单第一条记录:", "shortwayno");

								Log.e("bfirsthistory_order", String.valueOf(bfirsthistory));
								commute_selectrequest(phonenum, request, act);
							}*/
	             /*   ------------PersonalCenterActivity .java---end-----------------------*/


						} catch (JSONException e) {

							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(Logtag + "短途",
						error.getMessage(), error);
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

	private void getJson(JSONObject jasitem, String carsharing_type) throws
			JSONException {


		place_name[0] = jasitem.getString("startPlace");
		place_name[1] = jasitem.getString("destination");
		date_time[0] = jasitem.getString("startDate");
		display_item = place_name[0] + "  至  " + place_name[1]; //不是first_item
		rest_seats = 4;

		if (carsharing_type.compareTo("longway") != 0) {

			requesttime = jasitem.getString("requestTime");
			date_time[2] = jasitem.getString("startTime");
			date_time[3] = jasitem.getString("endTime");
			longitude_latitude[0] = Float
					.parseFloat(jasitem
							.getString("startPlaceX"));
			longitude_latitude[1] = Float
					.parseFloat(jasitem
							.getString("startPlaceY"));
			longitude_latitude[2] = Float
					.parseFloat(jasitem
							.getString("destinationX"));
			longitude_latitude[3] = Float
					.parseFloat(jasitem
							.getString("destinationY"));
			dealstatus = jasitem.getString("dealStatus");

			if (carsharing_type.compareTo("commute") == 0) {

				//todo 应该改服务器上的命名
				if (jasitem.getString("supplyCar").compareTo("y") == 0)
					userrole = "p";
				else userrole = "d";
				date_time[1] = jasitem.getString("endDate");
				weekrepeat = jasitem.getString("weekRepeat");

			} else if (carsharing_type.compareTo("shortway") == 0) {

				date_time[1] = "";
				weekrepeat = "";
				userrole = jasitem.getString("userRole");
			}
		} else { //longway

			date_time[1] = "";
			date_time[2] = "";
			date_time[3] = "";
			longitude_latitude[0] = 0;
			longitude_latitude[1] = 0;
			longitude_latitude[2] = 0;
			longitude_latitude[3] = 0;
			dealstatus = "";
			weekrepeat = "";
			requesttime = jasitem.getString("publishTime");
			userrole = jasitem.getString("userRole");

		}
		dbact.add_OdersToDb(carsharing_type, requesttime, longitude_latitude,
				place_name, date_time, dealstatus, userrole, weekrepeat,
				display_item, rest_seats);
	}


	public void orders(String UserPhoneNumber, GetordersCallBack getordersCB) {

		queue = Volley.newRequestQueue(mactivity);
		getordersCallBack = getordersCB;
		shortway_selectrequest(UserPhoneNumber); //将历史订单从服务器载入数据库

	}

	//匹配的订单状态
	public void pairedOrderResult(final String phonenum, final ArrayList<HashMap<String,
			Object>> mylist2) {
		String sharingresult_baseurl = mactivity.getString(R.string.uri_base)
				+ mactivity.getString(R.string.uri_CarTake)
				+ mactivity.getString(R.string.uri_selectcartake_action);

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				sharingresult_baseurl, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.w("sharingresult", response);
				try {
					JSONObject jasitem = null;
					JSONObject jas = new JSONObject(response);
					JSONArray jasA = jas.getJSONArray("result");
					for (int i = 0; i < jasA.length(); i++) {
						jasitem = jasA.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<>();
						map.put("Title", jasitem.getString("dealTime"));

						if (jasitem.getString("sharingType").compareTo(
								"commute") == 0) {
							map.put("text", "已匹配订单：上下班拼车");
						} else if (jasitem.getString("sharingType")
								.compareTo("shortway") == 0) {
							map.put("text", "已匹配订单：短途拼车");
						}

						map.put("requst", jasitem.getString("dealId")); // 隐藏的

						String deal_readstatus = null;
						if (i == 0) {
							deal_readstatus = "receive";
							map.put("deal_readstatus", "receive");// 隐藏的
						} else if (i == 3) {
							deal_readstatus = "reject";
							map.put("deal_readstatus", "reject");// 隐藏的
						} else if (i == 2) {
							deal_readstatus = "assessOK";
							map.put("deal_readstatus", "assessOK");// 隐藏的
						} else if (i == 1) {
							deal_readstatus = "unread";
							map.put("deal_readstatus", "unread");// 隐藏的
						}

						Log.e(Logtag + "deal_readstat", deal_readstatus);
						if (deal_readstatus.compareTo("unread") == 0) {// 未读消息
							map.put("deal_readstatusIcon",
									R.drawable.ic_dealunread);
						} else if (deal_readstatus.compareTo("receive") == 0) {// 已接收订单（等价于“未评价”）
							map.put("deal_readstatusIcon",
									R.drawable.ic_noneassess);
						} else if (deal_readstatus.compareTo("reject") == 0) {// 已拒绝订单
							map.put("deal_readstatusIcon",
									R.drawable.ic_action_dealreject);
						} else if (deal_readstatus
								.compareTo("assessOK") == 0) {// 订单完成（包括“评价完毕”）
							map.put("deal_readstatusIcon",
									R.drawable.ic_dealread);
						}

						mylist2.add(map);

					}
					getPairedOrderCallBack.getPairedOrderCallBack(jasitem, jasA.length());
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("sharingresult", error.getMessage(), error);
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

	//结果回调函数
	public interface GetordersCallBack {
		public void getordersCallBack(boolean WriteToDb_ok, boolean empty);
	}

	public interface GetPairedOrderCallBack {
		public void getPairedOrderCallBack(JSONObject jasitem, int jasA_length);
	}
}
