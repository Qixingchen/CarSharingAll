/*
 * 订单提交反馈界面
 * 根据订单提交界面访问服务器得到的结果打印出订单提交成功与否
 * 从服务器读取车辆表实时更新本地的车辆表（订单填写页可能对车辆表坐车了更改）
 */

package com.xmu.carsharing;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderResponseActivity extends Activity {

	private RequestQueue queue;
	ImageView image;
	TextView text;
	Button backbtn;
	String UserPhoneNumber;

/*	private void selectcarinfo(final String phonenum) {
		
		String carinfo_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo)
				+ getString(R.string.uri_selectcarinfo_action);

		Log.d("carinfo_selectrequest_baseurl", carinfo_selectrequest_baseurl);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						Log.d("carinfo_select", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							JSONObject json = json1.getJSONObject("result");
							// brand.setText(json.getString("carBrand"));
							// model.setText( json.getString("carModel"));
							// num.setText( json.getString("carNum"));
							// color.setText( json.getString("carColor"));
							Context context = OrderResponseActivity.this;
							SharedPreferences sharedPref = context
									.getSharedPreferences(UserPhoneNumber,
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("refreshdescription", json
									.getString("carBrand").toString());
							editor.putString("refreshmodel",
									json.getString("carModel"));
							editor.putString("refreshcolor",
									json.getString("carColor"));
							editor.putString("refreshnum",
									json.getString("carNum"));
							editor.commit();
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
	}*/

    CarinfoStatus function_carstatus; /*车辆表信息读取，已封装在CarinfoStatus.java中*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_response);

        function_carstatus = new CarinfoStatus(this, R.id.order_response_layout,
                this.getApplicationContext());
		// 获取用户号码

		Context phonenumber = OrderResponseActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "文件名");

		queue = Volley.newRequestQueue(this);

		image = (ImageView) findViewById(R.id.order_response_imageView);
		text = (TextView) findViewById(R.id.order_response_textView);
		backbtn = (Button) findViewById(R.id.order_response_back);

		// 向服务器请求查询车辆信息表start!
		function_carstatus.selectcarinfo(UserPhoneNumber);
		// 向服务器请求查询车辆信息表end!

		Intent request_response = getIntent();
		String judging = request_response
				.getStringExtra(getString(R.string.request_response));

		if (judging.compareTo("false") == 0) {

			image.setImageResource(R.drawable.ic_error);
			text.setText("订单提交失败！请检查您的网络连接！");

			backbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					finish();
				}
			});
		} else {
			image.setImageResource(R.drawable.ic_ok);
			text.setText("您的订单提交成功！请耐心等待，我们会尽快反馈消息给您！");

			backbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					Intent back_to_personcenter = new Intent(
							OrderResponseActivity.this,
							PersonalCenterActivity.class);
					startActivity(back_to_personcenter);

				}
			});

		}

	}

}
