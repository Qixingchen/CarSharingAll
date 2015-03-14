/*
 * 自定义适配器
 * 个人中心的3个“更多”共用
 * 根据intentcall执行不同操作
 * 监听垃圾桶，访问服务器，在服务器和本地同时删除该条信息
 */

package com.xmu.carsharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MyAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	public static RequestQueue queue;
	private String username;
	private int intentcall;

	// 用户手机号
	String UserPhoneNumber;

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor dbresult;

	// database end

	public MyAdapter(Context context, ArrayList<HashMap<String, String>> data,
			int intentcall) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
		this.intentcall = intentcall;
	}

	public MyAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		MyLayout mylayout = null;
		if (convertView == null) {
			mylayout = new MyLayout();

			// 获取组件布局
			convertView = layoutInflater.inflate(R.layout.messagesent_listitem,
					null);
			mylayout.date_time = (TextView) convertView
					.findViewById(R.id.sentdata);
			mylayout.placename = (TextView) convertView
					.findViewById(R.id.sentplace);
			mylayout.test = (TextView) convertView
					.findViewById(R.id.requesttime);
			mylayout.delete_button = (ImageButton) convertView
					.findViewById(R.id.mymessage_delete);

			// 这里要注意，是使用的tag来存储数据的。
			convertView.setTag(mylayout);
		} else {
			mylayout = (MyLayout) convertView.getTag();
		}

		mylayout.date_time.setText(data.get(position).get("Title"));
		mylayout.placename.setText(data.get(position).get("text"));
		final String time = data.get(position).get("requst");

		mylayout.delete_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				queue = Volley.newRequestQueue(context);
				// 提取用户手机号
				SharedPreferences sharedPref = context.getSharedPreferences(
						context.getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				UserPhoneNumber = sharedPref.getString("refreshfilename", "0");

				if (intentcall == 1) {

					// 主要是用time的值来辨识应该执行什么删除

					// 向服务器发起删除上下班拼车订单的请求start!
					commute_deleterequest(UserPhoneNumber, time);
					// 向服务器发起删除上下班拼车订单的请求end!

					// 向服务器发起删除短途拼车的请求start!
					shortway_deleterequest(UserPhoneNumber, time);
					// 向服务器发起删除短途拼车的请求end!

					// 向服务器发起删除长途拼车的请求start!
					longway_deleterequest(UserPhoneNumber, time);
					// 向服务器发起删除长途拼车的请求end!

				}
				if (intentcall == 3) {
					db = new DatabaseHelper(context, UserPhoneNumber, null, 1);
					db1 = db.getWritableDatabase();

					// Define 'where' part of query.
					String selection = context
							.getString(R.string.dbstring_PlaceMapName)
							+ " LIKE ?";
					// Specify arguments in placeholder order.
					String[] selelectionArgs = { data.get(position)
							.get("Title") };

					// Issue SQL statement.
					db1.delete(context.getString(R.string.dbtable_placeliked),
							selection, selelectionArgs);
					data.remove(position);

					notifyDataSetChanged();
					db1.close();

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
								Log.d("longway_deleterequest_result", response);
								try {
									JSONObject jas = new JSONObject(response);
									if (jas.getBoolean("result") == true) {
										// by-pyj
										data.remove(position);
										notifyDataSetChanged();
										// pyj-end
									}
								} catch (JSONException e) {
									
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("longway_deleterequest_result",
										error.getMessage(), error);
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
								Log.d("shortway_deleterequest_result", response);
								try {
									JSONObject jas = new JSONObject(response);
									if (jas.getBoolean("result") == true) {
										// by-pyj
										data.remove(position);
										notifyDataSetChanged();
										// pyj-end
									}
								} catch (JSONException e) {
									
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("shortway_deleterequest_result",
										error.getMessage(), error);
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

			private void commute_deleterequest(final String phonenum,
					final String time) {
				

				String commute_deleterequst_baseurl = context
						.getString(R.string.uri_base)
						+ context.getString(R.string.uri_CommuteRequest)
						+ context.getString(R.string.uri_deleterequest_action);
				// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!deleterequest.action?";

				Log.d("commute_deleterequest_URL", commute_deleterequst_baseurl);
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
										// by-pyj
										data.remove(position);
										notifyDataSetChanged();
										Log.e("deletewrong", "ok");
										// pyj-end
									}
								} catch (JSONException e) {
									
									e.printStackTrace();
									Log.e("deletewrong", "not errorresponse");
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								// Log.e("deleterequest_result",
								// error.getMessage(), error);
								Log.e("deletewrong", "errorresponse");
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
		});

		return convertView;
	}

	public class MyLayout {
		public TextView test;
		public TextView date_time;
		public TextView placename;
		public ImageButton delete_button;

		// public TextView tv_1;
		// public TextView tv_2;

	}

}
