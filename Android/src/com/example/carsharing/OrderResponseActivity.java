/*
 * �����ύ��������
 * ���ݶ����ύ������ʷ������õ��Ľ����ӡ�������ύ�ɹ����
 * �ӷ�������ȡ������ʵʱ���±��صĳ�����������дҳ���ܶԳ����������˸��ģ�
 */

package com.example.carsharing;

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

	private void selectcarinfo(final String phonenum) {
		// TODO Auto-generated method stub
		String carinfo_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo)
				+ getString(R.string.uri_selectcarinfo_action);
		
		Log.d("carinfo_selectrequest_baseurl",carinfo_selectrequest_baseurl);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_selectrequest_baseurl,
				new Response.Listener<String>(){

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.d("carinfo_select", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							JSONObject json = json1.getJSONObject("result");   
//							brand.setText(json.getString("carBrand"));
//							model.setText( json.getString("carModel"));
//							num.setText( json.getString("carNum"));
//							color.setText( json.getString("carColor"));
							Context context = OrderResponseActivity.this;
							SharedPreferences sharedPref = context.getSharedPreferences(
									UserPhoneNumber, Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("refreshdescription", json.getString("carBrand")
									.toString());
							editor.putString("refreshmodel", json.getString("carModel"));
							editor.putString("refreshcolor", json.getString("carColor"));
							editor.putString("refreshnum", json.getString("carNum"));
							editor.commit();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e("carinfo_selectresult_result",
						error.getMessage(), error);
			}
		})
		{
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				return params;
			}
		};

		queue.add(stringRequest);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_response);
		
		//��ȡ�û�����
		
		Context phonenumber = OrderResponseActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "�ļ���");
		
		queue = Volley.newRequestQueue(this);

		image = (ImageView) findViewById(R.id.order_response_imageView);
		text = (TextView) findViewById(R.id.order_response_textView);
		backbtn = (Button) findViewById(R.id.order_response_back);
		

		// ������������ѯ������Ϣ��start!
		selectcarinfo(UserPhoneNumber);
		// ������������ѯ������Ϣ��end!
		
		Intent request_response = getIntent();
		String judging = request_response
				.getStringExtra(getString(R.string.request_response));

		if (judging.compareTo("false") == 0) {

			image.setImageResource(R.drawable.ic_error);
			text.setText("�����ύʧ�ܣ����������������ӣ�");
			
			backbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		} else {
			image.setImageResource(R.drawable.ic_ok);
			text.setText("���Ķ����ύ�ɹ��������ĵȴ������ǻᾡ�췴����Ϣ������");
			
			backbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent back_to_personcenter = new Intent(
							OrderResponseActivity.this,
							PersonalCenterActivity.class);
					startActivity(back_to_personcenter);

				}
			});

		}

	}

}
