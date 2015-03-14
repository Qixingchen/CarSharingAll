/*
 * 忘记密码
 * 发送验证码，密码重设
 */

package com.xmu.carsharing;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.Tool.Hash_pwd;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {
	private ImageView cancelx1, cancelx2, cancelx3;
	private static boolean chcp = false;
	private boolean suc = false;
	private static RequestQueue queue;
	Hash_pwd hash = new Hash_pwd();
	private EditText phonenum;
	private EditText authcode;
	private Button authcode_btn;
	private Button btn_login;
	private EditText first_rePassword;
	private EditText second_rePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);

		queue = Volley.newRequestQueue(this);
		phonenum = (EditText) findViewById(R.id.forget_password_phonenumeditText);
		authcode = (EditText) findViewById(R.id.forget_password_inputauthnumedittext);
		authcode_btn = (Button) findViewById(R.id.forget_password_getauthbutton);
		first_rePassword = (EditText) findViewById(R.id.forget_password_passwordeditText);
		second_rePassword = (EditText) findViewById(R.id.forget_password2_passwordeditText);

		authcode_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				checkphone(phonenum.getText().toString());
			}

		});

		btn_login = (Button) findViewById(R.id.forget_password_confirmbutton);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (first_rePassword.getText().toString()
						.compareTo(second_rePassword.getText().toString()) == 0
						&& first_rePassword.getText().toString().compareTo("") != 0) {

					alterpassword(phonenum.getText().toString(),
							hash.hashans(first_rePassword.getText().toString()));

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warnningInfo_pwdTwiError),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		cancelx1 = (ImageView) findViewById(R.id.forget_password_clearphonenumimageView);
		cancelx1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				phonenum.setText("");
			}
		});
		first_rePassword = (EditText) findViewById(R.id.forget_password_passwordeditText);
		cancelx2 = (ImageView) findViewById(R.id.forget_password_clearimageView);
		cancelx2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				first_rePassword.setText("");
			}
		});
		second_rePassword = (EditText) findViewById(R.id.forget_password2_passwordeditText);
		cancelx3 = (ImageView) findViewById(R.id.forget_password2__clearimageView);
		cancelx3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				second_rePassword.setText("");
			}
		});
	}

	public void checkphone(final String phonenum) {

		String checkphone_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_UserInfo)
				+ getString(R.string.uri_checkphone_action);
		Log.e("checkphone_url", checkphone_baseurl);
		// "http://192.168.1.111:8080/CarsharingServer/UserInfo!checkphone.action?";
		// checkphone_baseurl = checkphone_baseurl + "phonenum=" + phonenum ;

		// Log.w("URL", checkphone_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				checkphone_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("checkphone_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							chcp = json1.getBoolean("PhoneNum");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (chcp == true) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.warningInfo_phonumerror),
									Toast.LENGTH_LONG).show();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("checkphone_result", error.getMessage(), error);
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
		Log.w("stringrequest", stringRequest.getUrl());

		// Add the request to the RequestQueue.
		queue.add(stringRequest);

		// volleyend!!
	}

	private void alterpassword(final String phonenum, final String pwd) {

		String login_baseurl = "http://192.168.1.111:8080/CarsharingServer/UserInfo!alterpassword.action?";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				login_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("alterpassword_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							suc = json1.getBoolean("Result");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (suc == true) {
							Intent btn_login = new Intent(
									ForgetPasswordActivity.this,
									PersonalCenterActivity.class);
							startActivity(btn_login);
						} else {
							Toast.makeText(getApplicationContext(), "密码修改失败！",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("alterpassword_result", error.getMessage(), error);

					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				params.put("password", pwd);
				return params;
			}
		};

		queue.add(stringRequest);
	}
}