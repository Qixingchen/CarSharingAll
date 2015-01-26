/*
f * 登陆界面
 * 根据用户名显示对应头像
 * 登陆成功到界面跳转执行的间隙：访问服务器获取车辆表和个人信息表写在本地（用于软件卸载后第一次打开）
 * 登陆访问服务器，成功则跳转界面
 * 添加progress bar加载效果
 */

package com.xmu.carsharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends Activity {

	private EditText phonenumber, password;
	private boolean bphn, bpw, suc = false, loadok;
	private Button next;
	boolean isExit;
	public static RequestQueue queue;
	Hash_pwd hash = new Hash_pwd();
	String UserPhoneNumber;
	// progressbar
	private static MyProgressDialog pd;
	Context context = LoginActivity.this;
	// progressbar end

	// 图片更改
	ImageView login_personpicture;
	Uri PhotoUri;

	// 图片更改end!!

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login_personpicture = (ImageView) findViewById(R.id.login_personpicture);

		phonenumber = (EditText) findViewById(R.id.login_phonenumedittext);
		queue = Volley.newRequestQueue(this);
		password = (EditText) findViewById(R.id.login_pwdedtitext);
		phonenumber.addTextChangedListener(phnTextWatcher);
		password.addTextChangedListener(pwdTextWatcher);

		next = (Button) findViewById(R.id.login_loginbutton);
		next.setEnabled(false);
		Context phonenum = LoginActivity.this;
		SharedPreferences filename = phonenum
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		SharedPreferences sharedPref1 = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		String UserNameForAutoLogin = sharedPref1.getString("refreshfilename",
				"0");
		if (UserNameForAutoLogin.compareTo("0") != 0) {
			phonenumber.setText(UserNameForAutoLogin);
		}

		autologin(UserNameForAutoLogin);

		// 登陆按钮触发start!
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 向服务器发送登陆请求start!
				login(phonenumber.getText().toString(),
						hash.hashans(password.getText().toString()));
				// 向服务器发送登陆请求end!

			}
		});

		Button register = (Button) findViewById(R.id.login_userregister);

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent register = new Intent(LoginActivity.this,
						RegisterActivity.class);
				register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(register);
			}
		});

		Button forget = (Button) findViewById(R.id.login_forgetpwdbutton);
		forget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent forget = new Intent(LoginActivity.this,
						ForgetPasswordActivity.class);
				startActivity(forget);
			}
		});
	}

	TextWatcher phnTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
			UserPhotoChange(phonenumber.getText().toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = phonenumber.getSelectionStart();
			editEnd = phonenumber.getSelectionEnd();
			if (temp.length() > 0) {
				bphn = true;
			} else {
				bphn = false;
			}
			confirm();

		}
	};

	TextWatcher pwdTextWatcher = new TextWatcher() {
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
			editStart = password.getSelectionStart();
			editEnd = password.getSelectionEnd();
			if (temp.length() != 0) {
				bpw = true;
			} else {
				bpw = false;
			}
			confirm();
		}
	};

	public void confirm() {
		if (bphn && bpw) {
			next.setEnabled(true);
		} else {
			next.setEnabled(false);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast toast = Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 50);
			toast.show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			super.onDestroy();
			System.exit(0);
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}

	};

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		loadok = false;
		UserPhotoChange(phonenumber.getText().toString());
	}

	// 图片更改

	private void UserPhotoChange(String UserName) {

		if (UserName.isEmpty()) {
			login_personpicture.setImageResource(R.drawable.ic_launcher);
			return;
		}
		Log.w("UserPhotoChange", UserName);

		File Photo = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserName);
		PhotoUri = Uri.fromFile(Photo);
		if (Photo.exists()) {
			login_personpicture.setImageURI(PhotoUri);
		} else {
			login_personpicture.setImageResource(R.drawable.ic_launcher);
		}

		// 图片更改end!!
	}

	// 自动登录
	private void autologin(String UserName) {
		if (UserName.compareTo("0") == 0) {
			return;
		}
		SharedPreferences sharedPref = this.getSharedPreferences(UserName,
				Context.MODE_PRIVATE);

		String Password = sharedPref.getString(
				getString(R.string.PreferenceUserPassword), "0");
		if (Password.compareTo("0") != 0) {
			login(UserName, Password);
		}

	}

	// 自动登录end

	private void login(final String phonenum, final String pwd) {
		

		// Toast.makeText(getApplicationContext(), "正在登陆", Toast.LENGTH_SHORT)
		// .show();
		pd = new MyProgressDialog(context);
		pd.setMessage("正在登陆");
		pd.show();

		String login_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_UserInfo)
				+ getString(R.string.uri_login_action);
		// "http://192.168.1.111:8080/CarsharingServer/UserInfo!login.action?";
		// login_baseurl = login_baseurl + "phonenum=" + phonenum +
		// "&pwd=" +pwd;

		// Log.d("URL", login_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				login_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("login_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							suc = json1.getBoolean("Login");
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
						if (suc == true) {

							// 向服务器请求查询车辆信息表start!
							// selectuserinfo(phonenum);
							selectcarinfo(phonenum);
							selectuserimage("13210891397");
							// 向服务器请求查询车辆信息表end!

							Context context = LoginActivity.this;
							SharedPreferences sharedPref = context
									.getSharedPreferences("file_text",
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("refreshfilename", phonenum);
							editor.commit();

							SharedPreferences sharedPref2 = context
									.getSharedPreferences(phonenum,
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor2 = sharedPref2
									.edit();
							editor2.putString(
									getString(R.string.PreferenceUserPassword),
									pwd);
							editor2.commit();

							// if(loadok == true){

							// }

						} else {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(),
									getString(R.string.warningInfo_pwdWrong),
									Toast.LENGTH_LONG);
							errorinfo.show();
							pd.hide();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("login_result", error.getMessage(), error);
						Toast.makeText(getApplicationContext(),
								getString(R.string.warningInfo_networkError),
								Toast.LENGTH_LONG).show();
						pd.hide();

					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(getString(R.string.uri_phonenum), phonenum);
				params.put(getString(R.string.uri_pwd), pwd);

				return params;
			}
		};

		queue.add(stringRequest);
		// 登陆按钮触发end!
	}

	private void selectuserimage(final String phonenum) {
		
		// String imageUrl = "http://i.imgur.com/7spzG.png";
		String imageUrl = "http://192.168.1.111:8080/CarsharingServer/images/Userphoto/"
				+ phonenum + ".jpg";

		ImageRequest imageRequest = new ImageRequest(imageUrl,
				new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap bitmap) {
						
						// login_personpicture.setImageResource(R.drawable.ic_action_back);
						login_personpicture.setImageBitmap(bitmap);

						// 把头像写在本地start！
						saveMyBitmap(bitmap);
						// 把头像写在本地end！

					}

				}, 0, 0, null, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						login_personpicture
								.setImageResource(R.drawable.ic_launcher);
					}
				});
		queue.add(imageRequest);
	}

	public void saveMyBitmap(Bitmap mBitmap) {
		File f = new File(
				"/storage/emulated/0/Android/data/com.xmu.carsharing/files/Pictures"
						+ ".jpg");
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void selectcarinfo(final String phonenum) {
		
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
							Context phonenumber = LoginActivity.this;
							SharedPreferences filename = phonenumber
									.getSharedPreferences(
											getString(R.string.PreferenceDefaultName),
											Context.MODE_PRIVATE);
							UserPhoneNumber = filename.getString(
									"refreshfilename", "文件名");
							SharedPreferences sharedPref = context
									.getSharedPreferences(UserPhoneNumber,
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("refreshdescription",
									json.getString("carBrand"));
							editor.putString("refreshmodel",
									json.getString("carModel"));
							editor.putString("refreshcolor",
									json.getString("carColor"));
							editor.putString("refreshnum",
									json.getString("carNum"));
							editor.putString("refreshcapacity",
									json.getString("carCapacity"));
							editor.commit();
							// 向服务器请求查询个人信息（姓名年龄性别）start!
							selectuserinfo(phonenum);
							// 向服务器请求查询个人信息（姓名年龄性别）end!

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
							json1 = new JSONObject(response);
							JSONObject json = json1.getJSONObject("result");
							Context phonenumber = LoginActivity.this;
							SharedPreferences filename = phonenumber
									.getSharedPreferences(
											getString(R.string.PreferenceDefaultName),
											Context.MODE_PRIVATE);
							UserPhoneNumber = filename.getString(
									"refreshfilename", "文件名");
							SharedPreferences sharedPref = context
									.getSharedPreferences(UserPhoneNumber,
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							if (json.getString("sex").compareTo("w") == 0) {// 性别：女
								editor.putString("refreshsex", "女");
							} else if (json.getString("sex").compareTo("m") == 0) {
								editor.putString("refreshsex", "男");
							}
							editor.putString("refreshname",
									json.getString("name"));
							editor.putString("refreshage",
									json.getString("age"));
							editor.commit();
							Intent login = new Intent(LoginActivity.this,
									PersonalCenterActivity.class);
							login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(login);

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
