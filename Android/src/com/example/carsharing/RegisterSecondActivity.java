/*
 * 注册2
 * 填写密码，检查两次密码输入是否一样，密码是否为空
 * 想服务器发送注册信息，服务器上写入该号码
 */

package com.example.carsharing;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carsharing.Hash_pwd;

public class RegisterSecondActivity extends Activity {

	private ImageView cancelx1, cancelx2;
	private EditText m1, m2;
	Hash_pwd hash = new Hash_pwd();
	public static RequestQueue queue;
	private static boolean registerOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_second);

		m1 = (EditText) findViewById(R.id.registersecond_phonenumeditText);
		m2 = (EditText) findViewById(R.id.registersecond_phonenumeditText1);
		queue = Volley.newRequestQueue(this);

		cancelx1 = (ImageView) findViewById(R.id.registersecond_clearphonenumimageView);
		cancelx1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m1.setText("");
			}
		});
		cancelx2 = (ImageView) findViewById(R.id.registersecond_clearphonenumimageView1);
		cancelx2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m2.setText("");
			}
		});
		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		// 页面跳转start!
		Button next = (Button) findViewById(R.id.registersecond_nextbutton);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (m1.getText().toString().compareTo(m2.getText().toString()) == 0 
						&& m1.getText().toString().compareTo("") != 0) {
					// TODO Auto-generated method stub

					// 向服务器发送注册信息start					
					Intent userregister_second = getIntent();
					String register_phonenum = userregister_second
							.getStringExtra(getString(R.string.user_phonenum));
					// 从注册第一页获取电话号码
					userregister(register_phonenum,
							hash.hashans(m1.getText().toString()));
					// 向服务器发送注册信息end

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warnningInfo_pwdTwiError),
							Toast.LENGTH_LONG).show();
				}
			}

			private void userregister(final String phonenum, final String pwd) {
				// TODO Auto-generated method stub
				String userregister_baseurl = getString(R.string.uri_base)
						+ getString(R.string.uri_UserInfo)
						+ getString(R.string.uri_userregister_action);
				// "http://192.168.1.111:8080/CarsharingServer/UserInfo!userregister.action?";
				// userregister_baseurl = userregister_baseurl + "phonenum=" +
				// phonenum + "&pwd=" +pwd;

				// Log.d("URL", userregister_baseurl);
				// Instantiate the RequestQueue.
				// Request a string response from the provided URL.
				StringRequest stringRequest = new StringRequest(
						Request.Method.POST, userregister_baseurl,
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.d("userregister_result", response);
								JSONObject json1 = null;
								try {
									json1 = new JSONObject(response);
									registerOK = json1.getBoolean("Register");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (registerOK == true) {
									SharedPreferences sharedPref = getApplicationContext()
											.getSharedPreferences(phonenum,
													Context.MODE_PRIVATE);
									SharedPreferences.Editor editor2 = sharedPref
											.edit();
									editor2.putString(
											getString(R.string.PreferenceUserPassword),
											pwd);
									editor2.commit();
									
									Intent next = new Intent(
											RegisterSecondActivity.this,
											PersonalCenterActivity.class);
									next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(next);
								} else {
									Toast errorinfo = Toast.makeText(
											getApplicationContext(), "注册失败",
											Toast.LENGTH_LONG);
									errorinfo.show();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("userregister_result",
										error.getMessage(), error);
								// Toast errorinfo = Toast.makeText(null,
								// "网络连接失败", Toast.LENGTH_LONG);
								// errorinfo.show();
							}
						}) {
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("phonenum", phonenum);
						params.put("pwd", pwd);

						return params;
					}
				};

				queue.add(stringRequest);

			}
		});
		// 页面跳转end!

	}

	public void setActionBarLayout(int layoutId) {

		// actionbar操作!!
		ActionBar actionBar = getActionBar();

		if (null != actionBar) {

			actionBar.setDisplayHomeAsUpEnabled(true);

			// actionBar.setDisplayShowHomeEnabled( false );
			//
			// actionBar.setDisplayShowCustomEnabled(true);
			//
			//
			// LayoutInflater inflator = (LayoutInflater)
			// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//
			// View v = inflator.inflate(layoutId, null);
			//
			// ActionBar.LayoutParams layout = new
			// ActionBar.LayoutParams(LayoutParams.FILL_PARENT,
			// LayoutParams.FILL_PARENT);
			//
			// actionBar.setCustomView(v,layout);

		}
		// actionbarend!!

	}

	// actionbar操作!!
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.register_acbar_two_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// actionbarcEnd!!!
}