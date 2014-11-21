/*
 * ע��1
 * ��д�绰���룬��ȡ��֤��
 * �����绰�������д�����ʷ��������ú����Ƿ���ע��
 */

package com.example.carsharing;

import java.util.HashMap;
import java.util.Map;

import com.example.carsharing.MobileOrNo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RegisterActivity extends Activity {

	private EditText Authorize;
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

	MobileOrNo mobliejudging = new MobileOrNo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		get_auth = (Button) findViewById(R.id.register_getauthbutton); // ��ȡ��֤��
		next = (Button) findViewById(R.id.register_nextbutton);
		next.setEnabled(false);
		PhoneNum = (EditText) findViewById(R.id.register_phonenumeditText);
		Authorize = (EditText) findViewById(R.id.register_inputauthnumedittext);
		Agreement = (CheckBox) findViewById(R.id.register_agreecheckbox);
		PhoneNum.addTextChangedListener(PhTextWatcher);
		Authorize.addTextChangedListener(ConTextWatcher);
		queue = Volley.newRequestQueue(this);

		cancel = (ImageView) findViewById(R.id.register_clearphonenumimageView);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhoneNum.setText("");
				confirm();
			}
		});

		// ҳ����תstart!
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				checkphone(PhoneNum.getText().toString());
				// json
				if (chcp == true) {
					SharedPreferences sharedPref = context
							.getSharedPreferences("file_text",
									Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString("refreshfilename", PhoneNum.getText()
							.toString());
					editor.commit();

					Intent next = new Intent(RegisterActivity.this,
							RegisterSecondActivity.class);

					next.putExtra(getString(R.string.user_phonenum), PhoneNum
							.getText().toString());// ��ֵ

					next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(next);
				}
				// json
			}

		});

		// ҳ����תend!

		// ��ȡ��֤��start!
		get_auth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// �ж��Ƿ�Ϊһ���Ϸ��ĵ绰����
				if (mobliejudging.mobilejudging(PhoneNum.getText().toString())) {
					checkphone(PhoneNum.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_phonumerror),
							Toast.LENGTH_LONG).show();
				}
			}

		});

		// ��ȡ��֤��end!

		Agreement
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
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
		// // �˴�Ϊ�õ�����ʱ�Ĵ�������
		//
		// } else {
		//
		// // �˴�Ϊʧȥ����ʱ�Ĵ�������
		// checkphone(PhoneNum.getText().toString());
		// }
		//
		// }
		// });

		// actionbar����!!

		// ��������!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!
	}

	public void setActionBarLayout(int layoutId) {

		// actionbar����!!
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

	// actionbar����!!
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
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = Authorize.getSelectionStart();
			editEnd = Authorize.getSelectionEnd();
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

	public void checkphone(final String phonenum) {

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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (chcp == false) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.warningInfo_userRegited),
									Toast.LENGTH_LONG).show();
							phone = false;
							confirm();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("checkphone_result", error.getMessage(), error);
						checkphone_result = null;
						// Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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

}
