/*
 * 上下班拼车界面
 * 订单填写页
 * 访问服务器获取车辆信息，自动填充车辆信息
 * 访问服务器提交订单
 */

package com.example.carsharing;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import longwaylist_fragmenttabhost.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CommuteActivity extends Activity {

	private int mHour, mMinute, mday, month, myear;
	static final int TIME_DIALOG = 0;
	static final int DATE_DIALOG = 1;
	static final int TIME_DIALOG01 = 2;
	static final int DATE_DIALOG01 = 3;
	private RequestQueue queue;

	private String username;
	public static String commute_result;
	View commute;
	View shortway;
	View longway;
	View personalcenter;
	View taxi;
	View setting;
	View about;
	Uri photouri;
	ImageView drawericon;
	private TextView drawername;
	private TextView drawernum;
	boolean isExit;
	private ImageView exchange;
	private static boolean requestok, carinfook;
	private boolean bstart, bend, blicensenum, bcarbrand, bcolor, bmodel, bmon,
			btue, bwed, bthu, bfri, bsat, bsun, bpassenager, bdriver,
			bstartdate, benddate, bearlystarttime, blatestarttime;
	private static int carinfochoosing_type;

	private EditText carbrand;
	private EditText model;
	private EditText color;
	private EditText licensenum;

	private RadioButton mRadio1, mRadio2;
	private Button sure;
	private RadioGroup commute_group;
	private CheckBox mon, tue, wed, thu, fri, sat, sun;
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	SimpleDateFormat standard_date, standard_time, primary_date, primary_time;
	String standard_commute_startdate = null, standard_commute_enddate = null,
			standard_commute_starttime = null, standard_commute_endtime = null;
	Date test_date, now = new Date();

	Button startdate;
	Button enddate;
	Button earlystarttime;
	Button latestarttime;
	Button increase;
	Button decrease;
	Button startplace;
	Button endplace;

	float startplace_longitude;
	float startplace_latitude;
	float destination_longitude;
	float destination_latitude;
	String supplycar;
	String weekrepeat = "";

	int sum = 0;
	TextView s1;

	Calendar c = Calendar.getInstance();

	// 用户手机号
	String UserPhoneNumber;

	// 收藏
	ImageView star1, star2;
	// 收藏end

	// 表单数据保存

	String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;

	// 表单数据保存end

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;

	// databasse end

	// actionbar!!
	Drawer activity_drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	// actionbarend!!

	// carnum,phonenum,carbrand,carmodel,carcolor,capacity
	public void carinfo(final String phonenum, final String carnum,
			final String carbrand, final String carmodel,
			final String carcolor, final String car_capacity, int type) {
		// TODO Auto-generated method stub

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
							// TODO Auto-generated catch block
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
	protected void onCreate(Bundle savedInstanceState) {
		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commute);

		activity_drawer = new Drawer(this, R.id.commute_layout);
		mDrawerToggle = activity_drawer.newdrawer();
		mDrawerLayout = activity_drawer.setDrawerLayout();

		// 日期、时间标准格式
		standard_date = new SimpleDateFormat("yyyy-MM-dd",
				Locale.SIMPLIFIED_CHINESE);
		primary_date = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.SIMPLIFIED_CHINESE);
		standard_time = new SimpleDateFormat("HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		primary_time = new SimpleDateFormat("HH时mm分ss秒",
				Locale.SIMPLIFIED_CHINESE);

		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);
		queue = Volley.newRequestQueue(this);
		exchange = (ImageView) findViewById(R.id.commute_exchange);
		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String temp = startplace.getText().toString();
				if (!temp.equals("选择起点")
						&& !endplace.getText().toString().equals("选择终点")) {
					startplace.setText(endplace.getText().toString());
					endplace.setText(temp);
					float a, b;
					a = startplace_longitude;
					b = startplace_latitude;
					startplace_longitude = destination_longitude;
					startplace_latitude = destination_latitude;
					destination_longitude = a;
					destination_latitude = b;

				}
			}
		});

		bdriver = true;
		bmon = true;
		btue = true;
		bwed = true;
		bthu = true;
		bfri = true;

		startdate = (Button) findViewById(R.id.commute_startdate);
		earlystarttime = (Button) findViewById(R.id.commute_earliest_start_time);
		enddate = (Button) findViewById(R.id.commute_enddate);
		latestarttime = (Button) findViewById(R.id.commute_latest_start_time);
		increase = (Button) findViewById(R.id.commute_increase);
		decrease = (Button) findViewById(R.id.commute_decrease);
		s1 = (TextView) findViewById(R.id.commute_count);
		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		startplace = (Button) findViewById(R.id.commute_startplace);
		endplace = (Button) findViewById(R.id.commute_endplace);
		sure = (Button) findViewById(R.id.commute_sure);
		sure.setEnabled(false);

		carbrand = (EditText) findViewById(R.id.commute_CarBrand);
		model = (EditText) findViewById(R.id.commute_CarModel);
		color = (EditText) findViewById(R.id.commute_color);
		licensenum = (EditText) findViewById(R.id.commute_Num);

		licensenum.addTextChangedListener(numTextWatcher);
		carbrand.addTextChangedListener(detTextWatcher);
		color.addTextChangedListener(coTextWatcher);
		model.addTextChangedListener(moTextWatcher);

		final TextView content = (TextView) findViewById(R.id.commute_content);

		commute_group = (RadioGroup) findViewById(R.id.commute_radiobutton);
		mRadio1 = (RadioButton) findViewById(R.id.commute_radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.commute_radioButton2);// 绑定一个RadioGroup监听器

		mon = (CheckBox) findViewById(R.id.commute_checkBox1);
		tue = (CheckBox) findViewById(R.id.commute_checkBox2);
		wed = (CheckBox) findViewById(R.id.commute_checkBox3);
		thu = (CheckBox) findViewById(R.id.commute_checkBox4);
		fri = (CheckBox) findViewById(R.id.commute_checkBox5);
		sat = (CheckBox) findViewById(R.id.commute_checkBox6);
		sun = (CheckBox) findViewById(R.id.commute_checkBox7);

		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		setting = findViewById(R.id.drawer_setting);
		personalcenter = findViewById(R.id.drawer_personalcenter);
		taxi = findViewById(R.id.drawer_taxi);

		star1 = (ImageView) findViewById(R.id.cummute_star);
		star2 = (ImageView) findViewById(R.id.commute_star01);

		// 提取用户手机号
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = sharedPref.getString(
				getString(R.string.PreferenceUserPhoneNumber), "0");

		// judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		String PRE_PAGE = bundle.getString("pre_page");
		if (PRE_PAGE.compareTo("ReOrder") == 0) { // 重新下单
			startplace.setText(bundle.getString("stpusername") + ","
					+ bundle.getString("stpmapname"));
			bstart = true;
			endplace.setText(bundle.getString("epusername") + ","
					+ bundle.getString("epmapname"));
			bend = true;
			startplace_longitude = bundle.getFloat("stpx");
			Log.e("startplace_longitude", String.valueOf(startplace_longitude));
			startplace_latitude = bundle.getFloat("stpy");
			Log.e("startplace_latitude", String.valueOf(startplace_latitude));
			destination_longitude = bundle.getFloat("epx");
			Log.e("destination_longitude",
					String.valueOf(destination_longitude));
			destination_latitude = bundle.getFloat("epy");
			Log.e("destination_latitude", String.valueOf(destination_latitude));
			startdate.setText(bundle.getString("re_commute_startdate"));
			bstartdate = true;
			enddate.setText(bundle.getString("re_commute_enddate"));
			benddate = true;
			weekrepeat = bundle.getString("weekrepeat");
			earlystarttime.setText(bundle.getString("re_commute_starttime"));
			bearlystarttime = true;
			latestarttime.setText(bundle.getString("re_commute_endtime"));
			blatestarttime = true;

			// weekrepeat中checkbox的勾选
			int len = weekrepeat.length();
			for (int i = 0; i < len; i++) {
				if (weekrepeat.charAt(i) == '1') {
					mon.setChecked(true);
					bmon = true;
				}
				if (weekrepeat.charAt(i) == '2') {
					tue.setChecked(true);
					btue = true;
				}
				if (weekrepeat.charAt(i) == '3') {
					wed.setChecked(true);
					bwed = true;
				}
				if (weekrepeat.charAt(i) == '4') {
					thu.setChecked(true);
					bthu = true;
				}
				if (weekrepeat.charAt(i) == '5') {
					fri.setChecked(true);
					bfri = true;
				}
				if (weekrepeat.charAt(i) == '6') {
					sat.setChecked(true);
					bsat = true;
				}
				if (weekrepeat.charAt(i) == '7') {
					sun.setChecked(true);
					bsun = true;
				}
			}
			// 勾选end
		}
		// judge the value of "pre_page"

		// database
		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();
		about = findViewById(R.id.drawer_respond);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent about = new Intent(CommuteActivity.this,
						AboutActivity.class);
				startActivity(about);
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent setting = new Intent(CommuteActivity.this,
						SettingActivity.class);
				startActivity(setting);
			}
		});

		// database end

		star1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bstart) {
					if (Pointisliked(StartPointMapName)) {
						// Define 'where' part of query.
						String selection = getString(R.string.dbstring_PlaceMapName)
								+ " LIKE ?";
						// Specify arguments in placeholder order.
						String[] selelectionArgs = { StartPointMapName };
						// Issue SQL statement.
						db1.delete(getString(R.string.dbtable_placeliked),
								selection, selelectionArgs);
						star1.setImageResource(R.drawable.ic_action_not_important);

					} else {
						ContentValues content = new ContentValues();
						content.put(getString(R.string.dbstring_PlaceUserName),
								StartPointUserName);
						content.put(getString(R.string.dbstring_PlaceMapName),
								StartPointMapName);
						content.put(getString(R.string.dbstring_longitude),
								startplace_longitude);
						content.put(getString(R.string.dbstring_latitude),
								startplace_latitude);
						db1.insert(getString(R.string.dbtable_placeliked),
								null, content);

						// Define 'where' part of query.
						String selection = getString(R.string.dbstring_PlaceMapName)
								+ " LIKE ?";
						// Specify arguments in placeholder order.
						String[] selelectionArgs = { StartPointMapName };
						// Issue SQL statement.
						db1.delete(getString(R.string.dbtable_placehistory),
								selection, selelectionArgs);
						star1.setImageResource(R.drawable.ic_action_important);
					}

				}

			}
		});

		star2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bend) {
					if (Pointisliked(EndPointMapName)) {
						// Define 'where' part of query.
						String selection = getString(R.string.dbstring_PlaceMapName)
								+ " LIKE ?";
						// Specify arguments in placeholder order.
						String[] selelectionArgs = { EndPointMapName };
						// Issue SQL statement.
						db1.delete(getString(R.string.dbtable_placeliked),
								selection, selelectionArgs);
						star2.setImageResource(R.drawable.ic_action_not_important);

					} else {
						ContentValues content = new ContentValues();
						content.put(getString(R.string.dbstring_PlaceUserName),
								EndPointUserName);
						content.put(getString(R.string.dbstring_PlaceMapName),
								EndPointMapName);
						content.put(getString(R.string.dbstring_longitude),
								destination_longitude);
						content.put(getString(R.string.dbstring_latitude),
								destination_latitude);
						db1.insert(getString(R.string.dbtable_placeliked),
								null, content);

						// Define 'where' part of query.
						String selection = getString(R.string.dbstring_PlaceMapName)
								+ " LIKE ?";
						// Specify arguments in placeholder order.
						String[] selelectionArgs = { EndPointMapName };
						// Issue SQL statement.
						db1.delete(getString(R.string.dbtable_placehistory),
								selection, selelectionArgs);
						star2.setImageResource(R.drawable.ic_action_important);
					}

				}

			}
		});

		taxi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		personalcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent personalcenter = new Intent(CommuteActivity.this,
						PersonalCenterActivity.class);
				startActivity(personalcenter);
			}
		});

		shortway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent shortway = new Intent(CommuteActivity.this,
						ShortWayActivity.class);
				shortway.putExtra("pre_page", "Drawer");
				startActivity(shortway);
			}
		});

		longway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent longway = new Intent(CommuteActivity.this,
						MainActivity.class);
				startActivity(longway);
			}
		});

		commute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));

			}
		});

		mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bmon = true;
				} else {
					bmon = false;
				}
				confirm();
			}
		});
		tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					btue = true;
				} else {
					btue = false;
				}
				confirm();
			}
		});
		wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bwed = true;
				} else {
					bwed = false;
				}
				confirm();
			}
		});
		thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bthu = true;
				} else {
					bthu = false;
				}
				confirm();
			}
		});
		fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bfri = true;
				} else {
					bfri = false;
				}
				confirm();
			}
		});
		sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bsat = true;
				} else {
					bsat = false;
				}
				confirm();
			}
		});
		sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					bsun = true;
				} else {
					bsun = false;
				}
				confirm();
			}
		});

		commute_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub18
				// 获取变更后的选中项的ID

				// "我能提供车"不变，"我不能提供车"使车牌号等编辑框不可编辑，并更改textView
				if (checkedId == mRadio2.getId()) {

					bpassenager = true;
					bdriver = false;

					licensenum.setEnabled(false);
					carbrand.setEnabled(false);
					color.setEnabled(false);
					model.setEnabled(false);

					licensenum
							.setFilters(new InputFilter[] { new InputFilter() {
								@Override
								public CharSequence filter(CharSequence source,
										int start, int end, Spanned dest,
										int dstart, int dend) {
									return source.length() < 1 ? dest
											.subSequence(dstart, dend) : "";
								}
							} });
					carbrand.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {
							return source.length() < 1 ? dest.subSequence(
									dstart, dend) : "";
						}
					} });
					color.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {
							return source.length() < 1 ? dest.subSequence(
									dstart, dend) : "";
						}
					} });
					model.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {
							return source.length() < 1 ? dest.subSequence(
									dstart, dend) : "";
						}
					} });
					content.setText(getString(R.string.warningInfo_seatNeed));
					licensenum.setHintTextColor(getResources().getColor(
							R.color.gray_cccccc));
					carbrand.setHintTextColor(getResources().getColor(
							R.color.gray_cccccc));
					color.setHintTextColor(getResources().getColor(
							R.color.gray_cccccc));
					model.setHintTextColor(getResources().getColor(
							R.color.gray_cccccc));
					licensenum.setInputType(InputType.TYPE_NULL);
					carbrand.setInputType(InputType.TYPE_NULL);
					color.setInputType(InputType.TYPE_NULL);
					model.setInputType(InputType.TYPE_NULL);
				} else {

					bpassenager = false;
					bdriver = true;

					licensenum.setEnabled(true);
					carbrand.setEnabled(true);
					color.setEnabled(true);
					model.setEnabled(true);

					licensenum
							.setFilters(new InputFilter[] { new InputFilter() {
								@Override
								public CharSequence filter(CharSequence source,
										int start, int end, Spanned dest,
										int dstart, int dend) {

									return null;
								}
							} });
					carbrand.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {
							return null;
						}
					} });
					color.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {

							return null;
						}
					} });
					model.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest, int dstart,
								int dend) {

							return null;
						}
					} });
					content.setText(getString(R.string.warningInfo_seatOffer));
					licensenum.setHintTextColor(getResources().getColor(
							R.color.purple_9F35FF));
					carbrand.setHintTextColor(getResources().getColor(
							R.color.purple_9F35FF));
					color.setHintTextColor(getResources().getColor(
							R.color.purple_9F35FF));
					model.setHintTextColor(getResources().getColor(
							R.color.purple_9F35FF));
					// licensenum.setText("");
					// carbrand.setText("");
					// color.setText("");
					// model.setText("");
					licensenum.setInputType(InputType.TYPE_CLASS_TEXT);
					carbrand.setInputType(InputType.TYPE_CLASS_TEXT);
					color.setInputType(InputType.TYPE_CLASS_TEXT);
					model.setInputType(InputType.TYPE_CLASS_TEXT);

					// 向服务器请求查询车辆信息表start!
					selectcarinfo(UserPhoneNumber);
					// 向服务器请求查询车辆信息表end!
				}
				confirm();
			}

			private void selectcarinfo(final String phonenum) {
				// TODO Auto-generated method stub
				String carinfo_selectrequest_baseurl = getString(R.string.uri_base)
						+ getString(R.string.uri_CarInfo)
						+ getString(R.string.uri_selectcarinfo_action);

				Log.d("carinfo_selectrequest_baseurl",
						carinfo_selectrequest_baseurl);
				StringRequest stringRequest = new StringRequest(
						Request.Method.POST, carinfo_selectrequest_baseurl,
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub
								Log.d("carinfo_select", response);
								String jas_id = null;
								JSONObject json1 = null;
								try {
									json1 = new JSONObject(response);
									JSONObject json = json1
											.getJSONObject("result");
									jas_id = json.getString("id");

									if (jas_id.compareTo("") != 0) { // 服务器上存在车辆信息时

										carinfochoosing_type = 2;
										carbrand.setText(json
												.getString("carBrand"));
										model.setText(json
												.getString("carModel"));
										licensenum.setText(json
												.getString("carNum"));
										color.setText(json
												.getString("carColor"));

									} else {
										carinfochoosing_type = 1;
									}
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
						}) {
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("phonenum", phonenum);
						return params;
					}
				};

				queue.add(stringRequest);
			}

		});

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (commute_group.getCheckedRadioButtonId() == mRadio1.getId()) {
					supplycar = "y";
				} else
					supplycar = "n";

				// 向服务器提交上下班拼车订单请求start!
				Context phonenumber = CommuteActivity.this;
				SharedPreferences filename = phonenumber.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				username = filename.getString("refreshfilename", "0");
				commute_request(username, startdate.getText().toString(),
						enddate.getText().toString(), earlystarttime.getText()
								.toString(), latestarttime.getText().toString());
				// 向服务器提交上下班拼车订单请求end!

			}

			private void commute_request(final String commute_phonenum,
					final String commute_startdate,
					final String commute_enddate,
					final String commute_starttime, final String commute_endtime) {
				// TODO Auto-generated method stub

				weekrepeat = "";
				if (bmon)
					weekrepeat += "1";
				if (btue)
					weekrepeat += "2";
				if (bwed)
					weekrepeat += "3";
				if (bthu)
					weekrepeat += "4";
				if (bfri)
					weekrepeat += "5";
				if (bsat)
					weekrepeat += "6";
				if (bsun)
					weekrepeat += "7";

				// 强制转换日期格式start
				try {
					test_date = primary_date.parse(commute_startdate);
					standard_commute_startdate = standard_date
							.format(test_date);
					test_date = primary_date.parse(commute_enddate);
					standard_commute_enddate = standard_date.format(test_date);
					test_date = primary_time.parse(commute_starttime);
					standard_commute_starttime = standard_time
							.format(test_date);
					test_date = primary_time.parse(commute_endtime);
					standard_commute_endtime = standard_time.format(test_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 强制转换日期格式end!

				String commute_baseurl = getString(R.string.uri_base)
						+ getString(R.string.uri_CommuteRequest)
						+ getString(R.string.uri_addrequest_action);
				// + "phonenum=" + commute_phonenum + "&startplacex=" +
				// String.valueOf(startplace_longitude) +
				// "&startplacey=" + String.valueOf(startplace_latitude) +
				// "&destinationx=" + String.valueOf(destination_longitude) +
				// "&destinationy=" + String.valueOf(destination_latitude) +
				// "&startdate=" + standard_commute_startdate
				// + "&enddate=" + standard_commute_enddate
				// + "&starttime=" + standard_commute_starttime
				// + "&endtime=" + standard_commute_endtime + "&weekrepeat=" +
				// weekrepeat + "&supplycar=" + supplycar;

				Log.e("commute_URL", commute_baseurl);
				// Instantiate the RequestQueue.
				// Request a string response from the provided URL.
				StringRequest stringRequest = new StringRequest(
						Request.Method.POST, commute_baseurl,
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.d("commute_result", response);
								JSONObject json1 = null;
								try {
									json1 = new JSONObject(response);
									requestok = json1.getBoolean("result");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if (requestok == true) {

									if (carinfochoosing_type == 1) {
										// add
										// 向服务器发送车辆信息修改请求start!
										carinfo(commute_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 1);
										// 向服务器发送车辆信息修改end!
									} else {
										// update
										// 向服务器发送车辆信息修改请求start!
										carinfo(commute_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 2);
										// 向服务器发送车辆信息修改end!
									}

									Intent sure = new Intent(
											CommuteActivity.this,
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
											CommuteActivity.this,
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
								Log.e("commute_result", error.getMessage(),
										error);
								commute_result = null;
								// Toast errorinfo = Toast.makeText(null,
								// "网络连接失败", Toast.LENGTH_LONG);
								// errorinfo.show();
								Intent sure = new Intent(CommuteActivity.this,
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
							test_date = primary_date.parse(commute_startdate);
							standard_commute_startdate = standard_date
									.format(test_date);
							test_date = primary_date.parse(commute_enddate);
							standard_commute_enddate = standard_date
									.format(test_date);
							test_date = primary_time.parse(commute_starttime);
							standard_commute_starttime = standard_time
									.format(test_date);
							test_date = primary_time.parse(commute_endtime);
							standard_commute_endtime = standard_time
									.format(test_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 强制转换日期格式end!

						Map<String, String> params = new HashMap<String, String>();
						params.put(getString(R.string.uri_phonenum),
								commute_phonenum);
						params.put(getString(R.string.uri_startplacex),
								String.valueOf(startplace_longitude));
						params.put(getString(R.string.uri_startplacey),
								String.valueOf(startplace_latitude));
						params.put(getString(R.string.uri_startplace),
								startplace.getText().toString());
						params.put(getString(R.string.uri_destinationx),
								String.valueOf(destination_longitude));
						params.put(getString(R.string.uri_destinationy),
								String.valueOf(destination_latitude));
						params.put(getString(R.string.uri_destination),
								endplace.getText().toString());
						params.put(getString(R.string.uri_startdate),
								standard_commute_startdate);
						params.put(getString(R.string.uri_enddate),
								standard_commute_enddate);
						params.put(getString(R.string.uri_starttime),
								standard_commute_starttime);
						params.put(getString(R.string.uri_endtime),
								standard_commute_endtime);
						params.put(getString(R.string.uri_weekrepeat),
								weekrepeat);
						params.put(getString(R.string.uri_supplycar), supplycar);
						// Log.w("phonemum", commute_phonenum);
						// Log.w("startplacex",
						// String.valueOf(startplace_longitude));
						// Log.w("startdate", standard_commute_startdate);
						// Log.w("starttime", standard_commute_starttime);
						// Log.w("weekrepeat",weekrepeat );
						// Log.w("supplycar",supplycar );
						// Log.w("enddate",standard_commute_enddate );

						return params;
					}
				};

				queue.add(stringRequest);

			}
		});

		startplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(CommuteActivity.this,
						ChooseAddressActivity.class), 1);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(CommuteActivity.this,
						ChooseArrivalActivity.class), 2);
			}
		});

		increase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum++;
				s1.setText("" + sum);
				confirm();
			}
		});

		decrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum--;
				if (sum < 0) {
					sum = 0;
				}
				s1.setText("" + sum);
				confirm();
			}
		});

		startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}

		});

		enddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG01);

			}
		});

		earlystarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG);
			}
		});

		latestarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG01);

			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		commute.setBackgroundDrawable(getResources().getDrawable(
				R.color.blue_0099cc));
		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = CommuteActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);
		Context context = CommuteActivity.this;
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String fullname = sharedPref.getString("refreshname", "姓名");
		drawername.setText(fullname);
		File photoFile = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber);
		if (photoFile.exists()) {
			photouri = Uri.fromFile(photoFile);
			drawericon.setImageURI(photouri);
		} else {
			drawericon.setImageResource(R.drawable.ic_launcher);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1: {
				StartPointMapName = data
						.getStringExtra(getString(R.string.dbstring_PlaceMapName));
				StartPointUserName = data
						.getStringExtra(getString(R.string.dbstring_PlaceUserName));
				startplace_longitude = Float
						.valueOf(data
								.getStringExtra(getString(R.string.dbstring_longitude)));
				startplace_latitude = Float.valueOf(data
						.getStringExtra(getString(R.string.dbstring_latitude)));

				startplace
						.setText(StartPointUserName + "," + StartPointMapName);
				Log.w("STintent回报经度", String.valueOf(startplace_longitude));
				Log.w("STintent回报纬度", String.valueOf(startplace_latitude));
				Log.w("MapName", StartPointMapName);

				// 收藏

				if (Pointisliked(StartPointMapName)) {
					star1.setImageResource(R.drawable.ic_action_important);
				} else {
					star1.setImageResource(R.drawable.ic_action_not_important);
				}

				// 收藏end
				bstart = true;
				break;

			}
			case 2: {

				EndPointMapName = data
						.getStringExtra(getString(R.string.dbstring_PlaceMapName));
				EndPointUserName = data
						.getStringExtra(getString(R.string.dbstring_PlaceUserName));
				destination_longitude = Float
						.valueOf(data
								.getStringExtra(getString(R.string.dbstring_longitude)));
				destination_latitude = Float.valueOf(data
						.getStringExtra(getString(R.string.dbstring_latitude)));

				endplace.setText(EndPointUserName + "," + EndPointMapName);
				Log.w("ENDintent回报经度", String.valueOf(destination_longitude));
				Log.w("ENDintent回报纬度", String.valueOf(destination_latitude));

				// 收藏

				if (Pointisliked(EndPointMapName)) {
					star2.setImageResource(R.drawable.ic_action_important);
				} else {
					star2.setImageResource(R.drawable.ic_action_not_important);
				}

				// 收藏end

				bend = true;
				break;
			}

			}

		}

	}

	// 判断是否收藏
	private Boolean Pointisliked(String mapname) {

		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，don't filter by row
		// groups，排序条件。
		Cursor isliked = db1.query(getString(R.string.dbtable_placeliked),
				null, getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[] { mapname }, null, null, null);

		if (isliked.getCount() != 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, mTimeSetListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case DATE_DIALOG01:
			return new DatePickerDialog(this, mDateSetListener1,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case TIME_DIALOG01:
			return new TimePickerDialog(this, mTimeSetListener1,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

		}
		return null;
	}

	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.show();
	}

	private OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			DisplayToast("时间为:" + String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			earlystarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			bearlystarttime = true;
			confirm();
		}
	};

	private OnTimeSetListener mTimeSetListener1 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			DisplayToast("时间为:" + String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			latestarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			blatestarttime = true;
			confirm();
		}
	};
	private OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {
			// TODO Auto-generated method stub
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			DisplayToast(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			startdate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bstartdate = true;
			confirm();
		}
	};
	private OnDateSetListener mDateSetListener1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {
			// TODO Auto-generated method stub
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			DisplayToast(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			enddate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			benddate = true;
			confirm();
		}
	};
	TextWatcher numTextWatcher = new TextWatcher() {
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
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = licensenum.getSelectionStart();
			editEnd = licensenum.getSelectionEnd();
			if (temp.length() > 0) {
				blicensenum = true;
			} else {
				blicensenum = false;
			}
			confirm();

		}
	};
	TextWatcher detTextWatcher = new TextWatcher() {
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
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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

	TextWatcher coTextWatcher = new TextWatcher() {
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
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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

	TextWatcher moTextWatcher = new TextWatcher() {
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
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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
		if (bstart
				&& bend
				&& (bmon || btue || bwed || bthu || bfri || bsat || bsun)
				&& ((bdriver && blicensenum && bcolor && bcarbrand) || bpassenager)
				&& bstartdate && benddate && bearlystarttime && blatestarttime
				&& (sum > 0)) {
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
			Intent returnp = new Intent(CommuteActivity.this,
					PersonalCenterActivity.class);
			returnp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(returnp);
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy(); // Always call the superclass
		// Stop method tracing that the activity started during onCreate()
		android.os.Debug.stopMethodTracing();
	}

}
