/*
 * 注册1
 * 填写电话号码，获取验证码
 * 监听电话号码的填写，访问服务器检查该号码是否已注册
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Tool.AppStat;
import com.Tool.MobileOrNo;
import com.Tool.SmssdkClass;
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

public class RegisterActivity extends Activity {

	private EditText Authorizeinput;
	private EditText PhoneNum;
	private CheckBox Agreement;
	private boolean agree, phone, authory;
	private static boolean chcp = false;
	Context context = RegisterActivity.this;
	private Button next;
	private Button get_auth;
	private ImageView cancel;
	public static RequestQueue queue;
	public static String checkphone_result;
	private SmssdkClass smssdkClass;

	MobileOrNo mobliejudging = new MobileOrNo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		smssdkClass = new SmssdkClass(context);

		get_auth = (Button) findViewById(R.id.register_getauthbutton); // 获取验证码
		next = (Button) findViewById(R.id.register_nextbutton);
		next.setEnabled(false);
		PhoneNum = (EditText) findViewById(R.id.register_phonenumeditText);
		Authorizeinput = (EditText) findViewById(R.id.register_inputauthnumedittext);
		Agreement = (CheckBox) findViewById(R.id.register_agreecheckbox);
		PhoneNum.addTextChangedListener(PhTextWatcher);
		Authorizeinput.addTextChangedListener(ConTextWatcher);
		queue = Volley.newRequestQueue(this);

		cancel = (ImageView) findViewById(R.id.register_clearphonenumimageView);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				PhoneNum.setText("");
				confirm();
			}
		});

		// 页面跳转start!
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				checkphone(PhoneNum.getText().toString(),AppStat.Register.跳转界面验证);
				// json
				if (chcp == true) {
					smssdkClass.versmsvercode(PhoneNum.getText().toString(),
							Authorizeinput.getText().toString());

				}
				// json
			}

		});

		// 页面跳转end!

		// 获取验证码start!
		get_auth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				// 判断是否为一个合法的电话号码
				if (mobliejudging.mobilejudging(PhoneNum.getText().toString())) {
					checkphone(PhoneNum.getText().toString(),AppStat.Register.发送验证短信);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_phonumerror),
							Toast.LENGTH_LONG).show();
				}
			}

		});

		// 获取验证码end!

		Agreement
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						if (isChecked) {
							agree = true;
						} else {
							agree = false;
						}
						confirm();
					}
				});

		// PhoneNum.setOnFocusChangeListener(new
		// android.view.View.OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		//
		// if (hasFocus) {
		//
		// // 此处为得到焦点时的处理内容
		//
		// } else {
		//
		// // 此处为失去焦点时的处理内容
		// checkphone(PhoneNum.getText().toString());
		// }
		//
		// }
		// });

		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!
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
		inflater.inflate(R.menu.register_acbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// actionbarcEnd!!!

	TextWatcher PhTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			editStart = PhoneNum.getSelectionStart();
			editEnd = PhoneNum.getSelectionEnd();
			if (temp.length() > 0) {
				phone = true;
			} else {
				phone = false;
			}
			confirm();
		}
	};

	TextWatcher ConTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			editStart = Authorizeinput.getSelectionStart();
			editEnd = Authorizeinput.getSelectionEnd();
			if (temp.length() != 0) {
				authory = true;
			} else {
				authory = false;
			}
			confirm();
		}
	};

	public void confirm() {
		if (agree == true && phone == true && authory == true) {
			next.setEnabled(true);
		} else {
			next.setEnabled(false);
		}
	}

	public void checkphone(final String phonenum, final int checkreason) {

		String checkphone_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_UserInfo)
				+ getString(R.string.uri_checkphone_action);
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
						if (chcp == false) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.warningInfo_userRegited),
									Toast.LENGTH_LONG).show();
							phone = false;
							confirm();
						}else {
							if (checkreason == AppStat.Register.发送验证短信) {
								smssdkClass.sendsms(PhoneNum.getText().toString());
							}
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("checkphone_result", error.getMessage(), error);
						checkphone_result = null;
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

	@Override
	public void onDestroy() {
		super.onDestroy();  // Always call the superclass

		smssdkClass.destory();
	}
}
