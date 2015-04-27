package com.Tool;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xmu.carsharing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jo on 2015/4/26.
 * HistoryOrderList页面删除某个item
 */
public class RemoveHistoryOrder {

	private Activity activity;
	private Context context;
	public static RequestQueue queue;
	String logtag = "historyitem删除界面：";

	DataBaseAct dbact;
	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;

	public RemoveHistoryOrder(Activity act,Context context){
		this.activity = act;
		this.context = context;
		getPhone = new ToolWithActivityIn(activity);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();
		dbact = new DataBaseAct(act,UserPhoneNumber);
		queue = Volley.newRequestQueue(context);
	}
	public void removeItem(HistoryOrderListItemClass historyOrderListItemData,
	                       int position){
		String type = historyOrderListItemData
				.HistoryOrderListItems[position]
				.Carsharing_type;
		String requesttime = historyOrderListItemData
				.HistoryOrderListItems[position].Requesttime;
		Log.e(logtag,"下单时间"+requesttime);
		if(type.compareTo("shortway") == 0){
			// 向服务器发起删除短途拼车的请求start!
			shortway_deleterequest(UserPhoneNumber, requesttime);
			// 向服务器发起删除短途拼车的请求end!
			dbact.delete某条历史订单("shortway",requesttime);
		}else if(type.compareTo("commute") == 0){
			// 向服务器发起删除上下班拼车订单的请求start!
			commute_deleterequest(UserPhoneNumber, requesttime);
			// 向服务器发起删除上下班拼车订单的请求end!
			dbact.delete某条历史订单("commute",requesttime);
		}else if(type.compareTo("longway") == 0){
			// 向服务器发起删除长途拼车的请求start!
			longway_deleterequest(UserPhoneNumber, requesttime);
			// 向服务器发起删除长途拼车的请求end!
			dbact.delete某条历史订单("longway",requesttime);
		}

	}
	private void longway_deleterequest(final String phonenum,
	                                   final String time) {

		String longway_deleterequest_baseurl = context
				.getString(R.string.uri_base)
				+ context.getString(R.string.uri_LongwayPublish)
				+ context.getString(R.string.uri_deletepublish_action);

		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, longway_deleterequest_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject jas = new JSONObject(response);
							if (jas.getBoolean("result") == true) {
								Toast.makeText(activity.getApplicationContext(),
										"删除成功",Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Toast errorinfo = Toast.makeText(null,
				// "网络连接失败", Toast.LENGTH_LONG);
				// errorinfo.show();
			}
		}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				params.put("time", time);

				return params;
			}

		};

		queue.add(stringRequest);
	}

	private void shortway_deleterequest(final String phonenum,
	                                    final String time) {

		String shortway_deleterequest_baseurl = context
				.getString(R.string.uri_base)
				+ context.getString(R.string.uri_ShortwayRequest)
				+ context.getString(R.string.uri_deleterequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!deleterequest.action?";

		// Log.d("URL", login_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, shortway_deleterequest_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject jas = new JSONObject(response);
							if (jas.getBoolean("result") == true) {
								Toast.makeText(activity.getApplicationContext(),
										"删除成功",Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				params.put("time", time);

				return params;
			}

		};

		queue.add(stringRequest);

	}

	private void commute_deleterequest(final String phonenum,
	                                   final String time) {


		String commute_deleterequst_baseurl = context
				.getString(R.string.uri_base)
				+ context.getString(R.string.uri_CommuteRequest)
				+ context.getString(R.string.uri_deleterequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!deleterequest.action?";

		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(
				Request.Method.POST, commute_deleterequst_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("deleterequest_result", response);
						try {
							JSONObject jas = new JSONObject(response);
							if (jas.getBoolean("result") == true) {
								Toast.makeText(activity.getApplicationContext(),
										"删除成功",Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {

							e.printStackTrace();
							Log.e("deletewrong", "not errorresponse");
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				params.put("time", time);
				return params;
			}
		};

		queue.add(stringRequest);
	}
}
