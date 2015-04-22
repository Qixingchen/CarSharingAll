package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yulan on 2015/4/21 021.
 * 服务器查询模块
 * 要求实现回调函数getLongWayAnsCallBack,传回是否达到底端的isend和含有内容的LongWayListItemClass
 */
public class ServerQues {
	//生命周期量
	private Activity activity;
	private Context context;
	private Application application;
	private String logtag = "服务器查询";

	//vollery
	private RequestQueue queue;

	//回调函数
	private GetLongWayAnsCallBack getLongWayAnsCallBack;

	public ServerQues(Activity mact) {
		activity = mact;
		context = mact.getApplicationContext();
		application = mact.getApplication();
		init();
	}

	private void init() {
		queue = Volley.newRequestQueue(context);
	}

	//参数:身份(司机/乘客) 开始条数,结束条数,下拉刷新表
	public void longway查询(final String role, final int start, final int end,
	                      final GetLongWayAnsCallBack getLongWayAnsCallBack, final SwipeRefreshLayout swipeLayout) {
		this.getLongWayAnsCallBack = getLongWayAnsCallBack;
		if (swipeLayout != null) {
			swipeLayout.setRefreshing(true);
		}
		String longwayway_selectpublish_baseurl = activity.getString(R.string.uri_base)
				+ activity.getString(R.string.uri_LongwayPublish)
				+ activity.getString(R.string.uri_lookuppublish_action);


		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {


					Uri[] UserPhoto;
					String[] Username;
					String[] PublishTime;
					String[] NeedDate;
					String[] StartPlace;
					String[] DesPlace;
					String[] DetailString;
					String[] ID;

					@Override
					public void onResponse(String response) {
						Log.d(logtag, response);
						int theEndPoistion = end;
						boolean isend = false;
						// json
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");

							if (end > jasA.length()) {
								isend = true;
								theEndPoistion = jasA.length();
							} else {
								isend = false;
							}

							int length = theEndPoistion - start;
							Log.e(logtag,"长途订单总数="+length);
							UserPhoto = new Uri[length];
							Username = new String[length];
							PublishTime = new String[length];
							NeedDate = new String[length];
							StartPlace = new String[length];
							DesPlace = new String[length];
							DetailString = new String[length];
							ID = new String[length];

							for (int i = 0; i < length; i++) {


								jasitem = jasA.getJSONObject(i + start);
								HashMap<String, String> map = new HashMap<>();
								map.put("detail",
										jasitem.getString("startDate"));
								map.put("re_address",
										jasitem.getString("startPlace")
												+ "  "
												+ " 至 "
												+ jasitem
												.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("publishTime"));
								Username[i] = jasitem.getString(context.getString(R.string
										.LongWay_用户ID));
								PublishTime[i] = Tool.getSimpleDate(jasitem.getString
										(context.getString(R.string.LongWay_发布时间)));
								NeedDate[i] = jasitem.getString(context.getString(R
										.string.LongWay_开始日期));
								StartPlace[i] = jasitem.getString(context.getString(R.
										string.LongWay_开始地点));
								DesPlace[i] = jasitem.getString(context.getString(R
										.string.LongWay_目的地));
								DetailString[i] = jasitem.getString(context.getString(R
										.string.LongWay_备注信息));
								ID[i] = jasitem.getString(context.getString(R
										.string.LongWay_订单编号));

							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
						LongWayListItemClass longWayListItemClass = new
								LongWayListItemClass(UserPhoto, Username,
								PublishTime, NeedDate, StartPlace, DesPlace,
								DetailString, ID);
						getLongWayAnsCallBack.getLongWayAnsCallBack(isend, longWayListItemClass);
						if (swipeLayout != null) {
							swipeLayout.setRefreshing(false);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(logtag, error.getMessage(), error);
				Toast errorinfo = Toast.makeText(null, "网络连接失败",
						Toast.LENGTH_LONG);
				errorinfo.show();
			}
		}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("role", role);
				return params;
			}
		};

		queue.add(stringRequest);

	}

	public void longway查询(final String role, final int start, final int end,
	                      final GetLongWayAnsCallBack getLongWayAnsCallBack){
		longway查询(role,start,end,getLongWayAnsCallBack,null);
	}

	//回调函数
	public interface GetLongWayAnsCallBack {
		public void getLongWayAnsCallBack(boolean isend, LongWayListItemClass
				longWayListItemClass);
	}

}
