/*
 * 短途拼车界面
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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ShortWayActivity extends Activity {
	private boolean requestok,carinfook;
	private boolean bstart, bend, blicensenum, bcarbrand, bcolor, bmodel, best,
			blst, bdate, bdriver, bpassenager;
	private EditText licensenum, carbrand;
	private EditText model, color;
	private RadioButton mRadio1, mRadio2;
	private Button next;
	private RadioGroup shortway_group;

	private RequestQueue queue;
	
	private int carinfochoosing_type;//作为车辆表信息修改方法的判别

	SimpleDateFormat standard_date, standard_time, primary_date, primary_time;
	String standard_shortway_startdate = null,
			standard_shortway_starttime = null,
			standard_shortway_endtime = null;
	Date test_date, now = new Date();

	View commute;
	View shortway;
	View longway;
	View personalcenter;
	View taxi;
	View setting;
	View about;
	
	ImageView drawericon;
	Uri photouri;
	boolean isExit;
	private ImageView exchange;
	private TextView drawername;
	private TextView drawernum;
	private int mHour, mMinute, mday, month, myear;
	static final int TIME_DIALOG = 0;
	static final int DATE_DIALOG = 1;
	static final int TIME_DIALOG1 = 2;
	private static final String Button = null;
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	Button datebutton;
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
	String userrole;

	Button sure;

	// actionbar!!
	Drawer activity_drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	// actionbarend!!

	int sum = 0;
	TextView s2;

	Calendar c = Calendar.getInstance();
	// 用户手机号
	String UserPhoneNumber;

	// 收藏
	ImageView star1, star2;
	// 收藏end

	// 表单数据保存

	String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;
	// float StartLongitude, EndLongitude, StartLatitude, EndLatitude;

	// 表单数据保存end

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;

	// databasse end

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_short_way);
		
		activity_drawer = new Drawer(this,R.id.short_way_layout);
		  mDrawerToggle = activity_drawer.newdrawer();
		  mDrawerLayout = activity_drawer.setDrawerLayout();

		bdriver = true;

		// 提取用户手机号
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = sharedPref.getString(
				getString(R.string.PreferenceUserPhoneNumber), "0");

		// 日期、时间标准格式
		standard_date = new SimpleDateFormat("yyyy-MM-dd",
				Locale.SIMPLIFIED_CHINESE);
		primary_date = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.SIMPLIFIED_CHINESE);
		standard_time = new SimpleDateFormat("HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		primary_time = new SimpleDateFormat("HH时mm分ss秒",
				Locale.SIMPLIFIED_CHINESE);

		queue = Volley.newRequestQueue(this);

		exchange = (ImageView) findViewById(R.id.shortway_exchange);
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

		datebutton = (Button) findViewById(R.id.shortway_dates);
		earlystarttime = (Button) findViewById(R.id.shortway_earliest_start_time);
		latestarttime = (Button) findViewById(R.id.shortway_latest_start_time);
		increase = (Button) findViewById(R.id.shortway_increase);
		decrease = (Button) findViewById(R.id.shortway_decrease);
		s2 = (TextView) findViewById(R.id.shortway_count);
		startplace = (Button) findViewById(R.id.shortway_startplace);
		endplace = (Button) findViewById(R.id.shortway_endplace);
		sure = (Button) findViewById(R.id.shortway_sure);
		
		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		setting = findViewById(R.id.drawer_setting);
		personalcenter = findViewById(R.id.drawer_personalcenter);
		about = findViewById(R.id.drawer_respond);
		taxi = findViewById(R.id.drawer_taxi);
		
		drawericon=(ImageView)findViewById(R.id.drawer_icon);
		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);
		carbrand = (EditText) findViewById(R.id.shortway_CarBrand);
		model = (EditText) findViewById(R.id.shortway_CarModel);
		color = (EditText) findViewById(R.id.shortway_color);
		licensenum = (EditText) findViewById(R.id.shortway_Num);

		licensenum.addTextChangedListener(numTextWatcher);
		carbrand.addTextChangedListener(detTextWatcher);
		color.addTextChangedListener(coTextWatcher);
		model.addTextChangedListener(moTextWatcher);

		next = (Button) findViewById(R.id.shortway_sure);
		next.setEnabled(false);
		db = new DatabaseHelper(ShortWayActivity.this, "test", null, 1);
		db1 = db.getWritableDatabase();

		final TextView content = (TextView) findViewById(R.id.shortway_content);
		mRadio1 = (RadioButton) findViewById(R.id.shortway_radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.shortway_radioButton2);
		shortway_group = (RadioGroup) findViewById(R.id.shortway_radiobutton01);
		star1 = (ImageView) findViewById(R.id.shortway_star);
		star2 = (ImageView) findViewById(R.id.shortway_star01);
		
		//judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		String PRE_PAGE = bundle.getString("pre_page");
		if(PRE_PAGE.compareTo("ReOrder") == 0){ //重新下单
			startplace.setText(bundle.getString("stpusername")+","+bundle.getString("stpmapname"));
			bstart = true;
			endplace.setText(bundle.getString("epusername")+","+bundle.getString("epmapname"));
			bend = true;
			startplace_longitude=bundle.getFloat("stpx");
			Log.e("startplace_longitude",String.valueOf(startplace_longitude));
			startplace_latitude=bundle.getFloat("stpy");
			Log.e("startplace_latitude",String.valueOf(startplace_latitude));
			destination_longitude=bundle.getFloat("epx");
			Log.e("destination_longitude",String.valueOf(destination_longitude));
			destination_latitude=bundle.getFloat("epy");
			Log.e("destination_latitude",String.valueOf(destination_latitude));
			datebutton.setText(bundle.getString("re_short_startdate"));
			bdate = true;
			earlystarttime.setText(bundle.getString("re_short_starttime"));
			best = true;
			latestarttime.setText(bundle.getString("re_short_endtime"));	
			blst = true;
		}
	    //judge the value of "pre_page"
		
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent about = new Intent(ShortWayActivity.this,
						AboutActivity.class);
				startActivity(about);
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent setting = new Intent(ShortWayActivity.this,
						SettingActivity.class);
				startActivity(setting);
			}
		});

		// database
		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();

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
				Intent personalcenter = new Intent(ShortWayActivity.this,
						PersonalCenterActivity.class);

				startActivity(personalcenter);
			}
		});

		shortway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		longway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent longway = new Intent(ShortWayActivity.this,
						MainActivity.class);
				startActivity(longway);
			}
		});

		commute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent commute = new Intent(ShortWayActivity.this,
						CommuteActivity.class);
				commute.putExtra("pre_page", "Drawer");
				startActivity(commute);
			}
		});

		// 绑定一个RadioGroup监听器
		shortway_group
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
								public CharSequence filter(
										CharSequence source, int start,
										int end, Spanned dest,
										int dstart, int dend) {
									return source.length() < 1 ? dest
											.subSequence(dstart, dend)
											: "";
								}
							} });
					carbrand.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {
							return source.length() < 1 ? dest
									.subSequence(dstart, dend) : "";
						}
					} });
					color.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {
							return source.length() < 1 ? dest
									.subSequence(dstart, dend) : "";
						}
					} });
					model.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {
							return source.length() < 1 ? dest
									.subSequence(dstart, dend) : "";
						}
					} });
					content.setText(getString(R.string.warningInfo_seatNeed));
					licensenum.setHintTextColor(Color
							.parseColor("#cccccc"));
					carbrand.setHintTextColor(Color
							.parseColor("#cccccc"));
					color.setHintTextColor(Color.parseColor("#cccccc"));
					model.setHintTextColor(Color.parseColor("#cccccc"));
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
								public CharSequence filter(
										CharSequence source, int start,
										int end, Spanned dest,
										int dstart, int dend) {

									return null;
								}
							} });
					carbrand.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {
							return null;
						}
					} });
					color.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {

							return null;
						}
					} });
					model.setFilters(new InputFilter[] { new InputFilter() {
						@Override
						public CharSequence filter(CharSequence source,
								int start, int end, Spanned dest,
								int dstart, int dend) {

							return null;
						}
					} });
					content.setText(getString(R.string.warningInfo_seatOffer));
					licensenum.setHintTextColor(Color
							.parseColor("#9F35FF"));
					carbrand.setHintTextColor(Color
							.parseColor("#9F35FF"));
					color.setHintTextColor(Color.parseColor("#9F35FF"));
					model.setHintTextColor(Color.parseColor("#9F35FF"));
//					licensenum.setText("");
//					carbrand.setText("");
//					color.setText("");
//					model.setText("");
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
			
		
		});
		
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (shortway_group.getCheckedRadioButtonId() == mRadio1.getId())
					userrole = "d";
				else
					userrole = "p";

				// 向服务器提交上下班拼车订单请求start!
				shortway_request(UserPhoneNumber, datebutton.getText()
						.toString(), earlystarttime.getText().toString(),
						latestarttime.getText().toString());
				// 向服务器提交上下班拼车订单请求end!

			}

			private void shortway_request(final String shortway_phonenum,
					final String shortway_date,
					final String shortway_starttime,
					final String shortway_endtime) {
				// TODO Auto-generated method stub

				// 强制转换日期格式start
				try {
					test_date = primary_date.parse(shortway_date);
					standard_shortway_startdate = standard_date
							.format(test_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					test_date = primary_time.parse(shortway_starttime);
					standard_shortway_starttime = standard_time
							.format(test_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					test_date = primary_time.parse(shortway_endtime);
					standard_shortway_endtime = standard_time.format(test_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 强制转换日期格式end!

				String shortway_baseurl = getString(R.string.uri_base)
						+ getString(R.string.uri_ShortwayRequest)
						+ getString(R.string.uri_addrequest_action);
				// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!addrequest.action?";

				Log.w("URL", shortway_baseurl);
				StringRequest stringRequest = new StringRequest(
						Request.Method.POST, shortway_baseurl,
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.d("shortway_result", response);
								JSONObject json1 = null;
								try {
									json1 = new JSONObject(response);
									requestok = json1.getBoolean("result");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (requestok == true) {
									
									if(carinfochoosing_type == 1){
										//add
										// 向服务器发送车辆信息修改请求start!
										carinfo(shortway_phonenum,licensenum.getText().toString(),
												carbrand.getText().toString(),model.getText().toString(),
												color.getText().toString(),String.valueOf(sum),1);
										// 向服务器发送车辆信息修改end!
									}
									else{
										//update
										// 向服务器发送车辆信息修改请求start!
										carinfo(shortway_phonenum,licensenum.getText().toString(),
												carbrand.getText().toString(),model.getText().toString(),
												color.getText().toString(),String.valueOf(sum),2);
										// 向服务器发送车辆信息修改end!
									}								
									
									Intent sure = new Intent(
											ShortWayActivity.this,
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
											ShortWayActivity.this,
											OrderResponseActivity.class);
									sure.putExtra(
											getString(R.string.request_response),
											"false");
								}
							}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("shortway_result", error.getMessage(),
										error);
								// Toast errorinfo = Toast.makeText(null,
								// "网络连接失败", Toast.LENGTH_LONG);
								// errorinfo.show();
								Intent sure = new Intent(ShortWayActivity.this,
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
							test_date = primary_date.parse(shortway_date);
							standard_shortway_startdate = standard_date
									.format(test_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							test_date = primary_time.parse(shortway_starttime);
							standard_shortway_starttime = standard_time
									.format(test_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							test_date = primary_time.parse(shortway_endtime);
							standard_shortway_endtime = standard_time
									.format(test_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 强制转换日期格式end!

						Map<String, String> params = new HashMap<String, String>();
						params.put("phonenum", shortway_phonenum);
						params.put("userrole", userrole);
						params.put("startplacex",
								String.valueOf(startplace_longitude));
						params.put("startplacey",
								String.valueOf(startplace_latitude));
						params.put(getString(R.string.uri_startplace),
								startplace.getText().toString());
						params.put("destinationx",
								String.valueOf(destination_longitude));
						params.put("destinationy",
								String.valueOf(destination_latitude));
						params.put(getString(R.string.uri_destination),
								endplace.getText().toString());
						params.put("startdate", standard_shortway_startdate);
						params.put("starttime", standard_shortway_starttime);
						params.put("endtime", standard_shortway_endtime);

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
				startActivityForResult(new Intent(ShortWayActivity.this,
						ChooseAddressActivity.class), 1);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(ShortWayActivity.this,
						ChooseArrivalActivity.class), 2);
			}
		});

		increase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum++;
				s2.setText("" + sum);
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
				s2.setText("" + sum);
				confirm();
			}
		});

		datebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
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
				showDialog(TIME_DIALOG1);

			}
		});

		
	}

	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		shortway.setBackgroundDrawable(getResources().getDrawable(R.color.blue_0099cc));
		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = ShortWayActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);
		Context context = ShortWayActivity.this;
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

		// switch (requestCode) {
		// case 1:
		// if (resultCode == Activity.RESULT_OK) {
		// String storeadd = data.getExtras().getString("storeadd");
		// String storedet = data.getExtras().getString("storedet");
		// String historyadd = data.getExtras().getString("historyadd");
		// String historydet = data.getExtras().getString("historydet");
		// if (storeadd != null && !storeadd.isEmpty()) {
		// startplace.setText(storeadd + "," + storedet);
		// bstart = true;
		// confirm();
		// } else if (historyadd != null && !historyadd.isEmpty()) {
		// startplace.setText(historyadd + "," + historydet);
		// bstart = true;
		// confirm();
		// }
		// }
		// case 2:
		// if (resultCode == Activity.RESULT_OK) {
		// String storeadd = data.getExtras().getString("storeadd");
		// String storedet = data.getExtras().getString("storedet");
		// String historyadd = data.getExtras().getString("historyadd");
		// String historydet = data.getExtras().getString("historydet");
		// if (storeadd != null && !storeadd.isEmpty()) {
		// endplace.setText(storeadd + "," + storedet);
		// bend = true;
		// confirm();
		// } else if (historyadd != null && !historyadd.isEmpty()) {
		// endplace.setText(historyadd + "," + historydet);
		// bend = true;
		// confirm();
		// }
		// }
		//
		// }
	}

	// 判断是否收藏
	private Boolean Pointisliked(String mapname) {

		// 表名 ,要获取的字段名，WHERE 条件，WHere值，don't group the rows，don't filter by row
		// groups，排序条件。
		Log.e("curMapName", mapname);
		Cursor isliked = db1.query(getString(R.string.dbtable_placeliked),
				null, getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[] { mapname }, null, null, null);

		if (isliked.getCount() != 0) {
			return true;
		} else {
			return false;
		}

	}
	
	//carnum,phonenum,carbrand,carmodel,carcolor,capacity
		public void carinfo(final String phonenum, final String carnum,
				final String carbrand, final String carmodel, final String carcolor,
				final String car_capacity,int type) {
			// TODO Auto-generated method stub
			
			String carinfotype;
			if(type==1){
				carinfotype = getString(R.string.uri_addcarinfo_action);
			}
			else{
				carinfotype = getString(R.string.uri_updatecarinfo_action);
			}
			
			String carinfo_baseurl = getString(R.string.uri_base)
					+ getString(R.string.uri_CarInfo)
					+ carinfotype;
//					+ "carnum=" + carnum +  "&phonenum=" 
//			+ phonenum + "&carbrand=" + carbrand + "&carmodel=" + carmodel
//			+ "&carcolor=" + carcolor +"&capacity=" + car_capacity;
			
			// "http://192.168.1.111:8080/CarsharingServer/CarInfo!changeinfo.action?";

//			Uri.encode(modify_baseurl, "@#&=*+-_.,:!?()/~'%");// 中文编码

			Log.d("carinfo_URL", carinfo_baseurl);
			// Instantiate the RequestQueue.
			// Request a string response from the provided URL.
			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, carinfo_baseurl,
					new Response.Listener<String>() {

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
							Log.e("carinfo_result", error.getMessage(),
									error);
//							 Toast errorinfo = Toast.makeText(null,
//							 "网络连接失败", Toast.LENGTH_LONG);
//							 errorinfo.show();
						}
					})
			{
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
							String jas_id = null;
							JSONObject json1 = null;
							try {
								json1 = new JSONObject(response);
								JSONObject json = json1.getJSONObject("result");   
								jas_id= json.getString("id");  
								
								if(jas_id.compareTo("") != 0 ){ //服务器上存在车辆信息时

									carinfochoosing_type = 2;
									
									carbrand.setText( json.getString("carBrand"));
									model.setText( json.getString("carModel"));
									licensenum.setText( json.getString("carNum"));
									color.setText( json.getString("carColor"));

								}else{
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
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, mTimeSetListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case TIME_DIALOG1:
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
					+ String.valueOf(minute) + "分");
			earlystarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00" + "秒");

			best = true;
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
					+ String.valueOf(minute) + "分" + "00" + "秒");
			latestarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00" + "秒");

			blst = true;
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
			datebutton.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bdate = true;
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
				&& ((bdriver && blicensenum && bcolor && bcarbrand) || bpassenager)
				&& best && blst && bdate && (sum > 0)) {
			next.setEnabled(true);
		} else {
			next.setEnabled(false);
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
			Intent returnp = new Intent(ShortWayActivity.this,
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