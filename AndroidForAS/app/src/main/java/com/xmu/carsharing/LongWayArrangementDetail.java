package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LongWayArrangementDetail extends Activity {
	public static RequestQueue queue;
	private float SPX, SPY, DSX, DSY;
	private String carsharing_type, requesttime, userid;
	private String UserPhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Context phonenumber = LongWayArrangementDetail.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		queue = Volley.newRequestQueue(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_long_way_arrangement_detail);
		// actionbar操作!!
		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionbarEND!!

		Bundle bundle = this.getIntent().getExtras();
		TextView sp = (TextView) findViewById(R.id.long_arrangementdetail_startaddress), ep = (TextView) findViewById(R.id.long_arrangementdetail_endaddress), st = (TextView) findViewById(R.id.long_arrangementdetail_starttime), rs = (TextView) findViewById(R.id.long_arrangementdetail_remainsites);

		sp.setText(bundle.getString("tsp"));
		ep.setText(bundle.getString("tep"));
		st.setText(bundle.getString("tst"));
		rs.setText(bundle.getString("trs"));

		userid = bundle.getString("userId");
		selectuserinfo(userid);
		SPX = bundle.getFloat("SPX");
		SPY = bundle.getFloat("SPY");
		DSX = bundle.getFloat("DSX");
		DSY = bundle.getFloat("DSY");
		carsharing_type = bundle.getString("carsharing_type");
		requesttime = bundle.getString("requesttime");
	}

	private void selectuserinfo(final String phonenum) {
		
		String userinfo_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_UserInfo)
				+ getString(R.string.uri_selectuserinfo_action);

		Log.d("userinfo_selectrequest_baseurl", userinfo_selectrequest_baseurl);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				userinfo_selectrequest_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("userinfo_select", response);
						JSONObject json1 = null;
						try {
							TextView username = (TextView) findViewById(R.id.long_arrangementdetail_name), usergender = (TextView) findViewById(R.id.long_arrangementdetail_gender), userintro = (TextView) findViewById(R.id.long_arrangementdetail_intro);
							json1 = new JSONObject(response);
							JSONObject json = json1.getJSONObject("result");
							if (json.getString("sex").compareTo("w") == 0) {// 性别：女
								usergender.setText("女");
							} else if (json.getString("sex").compareTo("m") == 0) {
								usergender.setText("男");
							}
							username.setText(json.getString("name"));
							userintro.setText("这个人真的很懒");

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("carinfo_selectresult_result",
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

}