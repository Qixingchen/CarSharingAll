/*
 * 长途拼车界面
 * 订单填写页
 * 访问服务器获取车辆信息，自动填充车辆信息
 * 访问服务器提交订单
 */

package com.xmu.carsharing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Tool.Drawer;
import com.Tool.IdentityBtn;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LongWayActivity extends Activity {

	private EditText licensenum, carbrand;
	private EditText model, color;
	private ImageView exchange;
	private static boolean requestok, carinfook;

	private boolean bstart, bend, bnum, bcolor, bcarbrand, bmodel, bdate;
	private boolean	mbdriver, mbpassenager;
	private int mHour, mMinute, mday, month, myear;
	private RadioGroup longway_group;
	private RadioButton passangerRadioButton;
	private RadioButton driverRadioButton;
	static final int DATE_DIALOG = 1;

	private RequestQueue queue;

	private int carinfochoosing_type;// 作为车辆表信息修改方法的判别

	private Date test_date, now = new Date();

	private SimpleDateFormat standard_date, primary_date;
	private String standard_longway_startdate = null;

	private Button sure;
	private String username;
	private Button datebutton;
	private Button increase;
	private Button decrease;
	private EditText startplace;
	private EditText endplace;
	private EditText noteinfo;
	private boolean isExit;

	// 用户手机号
	private String UserPhoneNumber;
	private String userrole;

	int sum = 0;
	private TextView s1;
	private Calendar c = Calendar.getInstance();

	// actionbar!!
	private Drawer drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	// actionbarend!!

	// 表单数据保存

	private String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;

	// 表单数据保存end

	// database

	private DatabaseHelper db;
	private SQLiteDatabase db1;

	// databasse end

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_long_way);

        final IdentityBtn function_identity; /*身份选择，已封装在IdentityBtn.java中*/
        function_identity = new IdentityBtn(this, R.id.long_way_layout);

		//actionbar
		drawer = new Drawer(this, R.id.long_way_layout);
		mDrawerToggle = drawer.newdrawer();
		mDrawerLayout = drawer.setDrawerLayout();
		//actionbar end

		// 日期、时间标准格式
		standard_date = new SimpleDateFormat("yyyy-MM-dd",
				Locale.SIMPLIFIED_CHINESE);
		primary_date = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.SIMPLIFIED_CHINESE);

		queue = Volley.newRequestQueue(this);
		exchange = (ImageView) findViewById(R.id.longway_exchange);
		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				String temp = startplace.getText().toString();

				startplace.setText(endplace.getText().toString());
				endplace.setText(temp);

			}
		});

		function_identity.bdriver = true;

		datebutton = (Button) findViewById(R.id.longway_dates);
		increase = (Button) findViewById(R.id.longway_increase);
		decrease = (Button) findViewById(R.id.longway_decrease);
		s1 = (TextView) findViewById(R.id.longway_count);
		sure = (Button) findViewById(R.id.longway_sure);
		sure.setEnabled(false);
		startplace = (EditText) findViewById(R.id.longway_start_place);
		endplace = (EditText) findViewById(R.id.longway_end_place);
		noteinfo = (EditText) findViewById(R.id.longway_remarkText);
		carbrand = (EditText) findViewById(R.id.longway_CarBrand);
		model = (EditText) findViewById(R.id.longway_CarModel);
		color = (EditText) findViewById(R.id.longway_color);
		licensenum = (EditText) findViewById(R.id.longway_Num);

		licensenum.addTextChangedListener(numTextWatcher);
		carbrand.addTextChangedListener(detTextWatcher);
		color.addTextChangedListener(coTextWatcher);
		model.addTextChangedListener(moTextWatcher);
		startplace.addTextChangedListener(spTextWatcher);
		endplace.addTextChangedListener(epTextWatcher);

		final TextView content = (TextView) findViewById(R.id.longway_content);
		longway_group = (RadioGroup) findViewById(R.id.longway_radiobutton);
		passangerRadioButton = (RadioButton) findViewById(R.id.longway_radioButton02);
		driverRadioButton = (RadioButton) findViewById(R.id.longway_radioButton01);

		// judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		String PRE_PAGE = bundle.getString("pre_page");
		if (PRE_PAGE.compareTo("ReOrder") == 0) { // 重新下单
			startplace.setText(bundle.getString("stpmapname"));
			bstart = true;
			endplace.setText(bundle.getString("epmapname"));
			bend = true;
			datebutton.setText(bundle.getString("re_longway_startdate"));
			bdate = true;
		}
		// judge the value of "pre_page"

		// 提取用户手机号
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = sharedPref.getString(
				getString(R.string.PreferenceUserPhoneNumber), "0");

		// database
		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();


		// database end



        // 绑定一个RadioGroup监听器（身份选择）
        function_identity.IdentityChoosing("longway",UserPhoneNumber);
        mbdriver = function_identity.bdriver;
        mbpassenager = function_identity.bpassenager;
        confirm();

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				if (longway_group.getCheckedRadioButtonId() == passangerRadioButton
						.getId())
					userrole = "p";
				else
					userrole = "d";

				// 向服务器提交长途拼车订单请求start!
				Context phonenumber = LongWayActivity.this;
				SharedPreferences filename = phonenumber.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				username = filename.getString("refreshfilename", "0");
				longway_request(username, userrole, datebutton.getText()
						.toString(), startplace.getText().toString(), endplace
						.getText().toString(), noteinfo.getText().toString());
				// 向服务器提交长途拼车订单请求end!
			}

			private void longway_request(final String longway_phonenum,
					final String longway_userrole,
					final String longway_startdate,
					final String longway_startplace,
					final String longway_destination,
					final String longway_noteinfo) {
				

				String longway_addrequest_baseurl = getString(R.string.uri_base)
						+ getString(R.string.uri_LongwayPublish)
						+ getString(R.string.uri_addpublish_action);
				// + "phonenum=" + longway_phonenum
				// + "&userrole=" + longway_userrole
				// + "&startdate=" + standard_longway_startdate
				// + "&startplace=" + longway_startplace
				// + "&destination=" + longway_destination
				// + "&noteinfo=" + longway_noteinfo;

				// Log.d("longway_baseurl",longway_addrequest_baseurl);

				StringRequest stringRequest = new StringRequest(
						Request.Method.POST, longway_addrequest_baseurl,
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.d("longway_result", response);
								JSONObject json1 = null;
								try {
									json1 = new JSONObject(response);
									requestok = json1.getBoolean("result");
								} catch (JSONException e) {
									
									e.printStackTrace();
								}
								if (requestok == true) {

									if (carinfochoosing_type == 1) {
										// add
										// 向服务器发送车辆信息修改请求start!
										carinfo(longway_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 1);
										// 向服务器发送车辆信息修改end!
									} else {
										// update
										// 向服务器发送车辆信息修改请求start!
										carinfo(longway_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 2);
										// 向服务器发送车辆信息修改end!
									}

									Intent sure = new Intent(
											LongWayActivity.this,
											OrderResponseActivity.class);
									sure.putExtra(
											getString(R.string.request_response),
											"true");
									sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(sure);
								} else {
									// Toast errorinfo =
									// Toast.makeText(getApplicationContext(),
									// "提交失败", Toast.LENGTH_LONG);
									// errorinfo.show();
									Intent sure = new Intent(
											LongWayActivity.this,
											OrderResponseActivity.class);
									sure.putExtra(
											getString(R.string.request_response),
											"false");
									sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(sure);
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("longway_result", error.getMessage(),
										error);
								// Toast errorinfo = Toast.makeText(null,
								// "网络连接失败", Toast.LENGTH_LONG);
								// errorinfo.show();
								Intent sure = new Intent(LongWayActivity.this,
										OrderResponseActivity.class);
								sure.putExtra(
										getString(R.string.request_response),
										"false");
								sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(sure);
							}
						}) {
					protected Map<String, String> getParams() {
						// POST方法重写getParams函数

						// 强制转换日期格式start
						try {
							test_date = primary_date.parse(longway_startdate);
							standard_longway_startdate = standard_date
									.format(test_date);
						} catch (ParseException e) {
							
							e.printStackTrace();
						}

						// 强制转换日期格式end!

						Map<String, String> params = new HashMap<String, String>();
						params.put(getString(R.string.uri_phonenum),
								longway_phonenum);
						params.put(getString(R.string.uri_userrole),
								longway_userrole);
						params.put(getString(R.string.uri_startplace),
								longway_startplace);
						params.put(getString(R.string.uri_destination),
								longway_destination);
						params.put(getString(R.string.uri_startdate),
								standard_longway_startdate);
						params.put(getString(R.string.uri_noteinfo),
								longway_noteinfo);

						return params;
					}
				};
				queue.add(stringRequest);
			}
		});

		increase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				sum++;
				s1.setText("" + sum);
				confirm();
			}
		});

		decrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				sum--;
				if (sum < 0) {
					sum = 0;
				}
				s1.setText("" + sum);
				confirm();
			}
		});

		datebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				showDialog(DATE_DIALOG);
			}
		});
	}

	private TextWatcher spTextWatcher = new TextWatcher() {
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
			
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				bstart = true;
			} else {
				bstart = false;
			}
			confirm();

		}
	};

	private TextWatcher epTextWatcher = new TextWatcher() {
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
			
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				bend = true;
			} else {
				bend = false;
			}
			confirm();

		}
	};

	@Override
	public void onResume() {

		super.onResume();

		drawer.OnResumeRestore();

	}

	// carnum,phonenum,carbrand,carmodel,carcolor,capacity
	public void carinfo(final String phonenum, final String carnum,
			final String carbrand, final String carmodel,
			final String carcolor, final String car_capacity, int type) {
		

		String carinfotype;
		if (type == 1) {
			carinfotype = getString(R.string.uri_addcarinfo_action);
		} else {
			carinfotype = getString(R.string.uri_updatecarinfo_action);
		}

		String carinfo_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo) + carinfotype;
		// + "carnum=" + carnum + "&phonenum="
		// + phonenum + "&carbrand=" + carbrand + "&carmodel=" + carmodel
		// + "&carcolor=" + carcolor +"&capacity=" + car_capacity;

		// "http://192.168.1.111:8080/CarsharingServer/CarInfo!changeinfo.action?";

		// Uri.encode(modify_baseurl, "@#&=*+-_.,:!?()/~'%");// 中文编码

		Log.d("carinfo_URL", carinfo_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("carinfo_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							carinfook = json1.getBoolean("result");
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
						if (carinfook == false) {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(), "车辆信息修改失败",
									Toast.LENGTH_LONG);
							errorinfo.show();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("carinfo_result", error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null,
						// "网络连接失败", Toast.LENGTH_LONG);
						// errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("carnum", carnum);
				params.put("phonenum", phonenum);
				params.put("carbrand", carbrand);
				params.put("carmodel", carmodel);
				params.put("carcolor", carcolor);
				params.put("capacity", car_capacity);
				return params;
			}
		};

		queue.add(stringRequest);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.show();
	};

	private OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {
			
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			DisplayToast(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			datebutton.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bdate = true;
			confirm();
		}
	};
	private  TextWatcher numTextWatcher = new TextWatcher() {
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
			
			editStart = licensenum.getSelectionStart();
			editEnd = licensenum.getSelectionEnd();
			if (temp.length() > 0) {
				bnum = true;
			} else {
				bnum = false;
			}
			confirm();

		}
	};

	private  TextWatcher detTextWatcher = new TextWatcher() {
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
			
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				bcarbrand = true;
			} else {
				bcarbrand = false;
			}
			confirm();

		}
	};

	private  TextWatcher coTextWatcher = new TextWatcher() {
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
			
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				bcolor = true;
			} else {
				bcolor = false;
			}
			confirm();

		}
	};

	private  TextWatcher moTextWatcher = new TextWatcher() {
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
			
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				bmodel = true;
			} else {
				bmodel = false;
			}
			confirm();

		}
	};

	public void confirm() {
		if (bstart && bend
				&& ((mbdriver && bnum && bcolor && bcarbrand) || (mbpassenager))
				&& bdate && (sum > 0)) {
			sure.setEnabled(true);
		} else {
			sure.setEnabled(false);
		}
	}

	// actionbar!!
	/* 当invalidateOptionsMenu()调用时调用 */
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // 如果nav drawer是打开的, 隐藏与内容视图相关联的action items
	// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// 在onRestoreInstanceState发生后，同步触发器状态.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 将事件传递给ActionBarDrawerToggle, 如果返回true，表示app 图标点击事件已经被处理
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// 处理你的其他action bar items...

		return super.onOptionsItemSelected(item);
	}

	// actionbarend!!

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent returnp = new Intent(LongWayActivity.this,
					PersonalCenterActivity.class);
			returnp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(returnp);
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
