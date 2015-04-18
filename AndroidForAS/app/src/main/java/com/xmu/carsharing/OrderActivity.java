/*
 * 上下班拼车界面
 * 订单填写页
 * 访问服务器获取车辆信息，自动填充车辆信息
 * 访问服务器提交订单
 */

package com.xmu.carsharing;
import java.text.ParseException;
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
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Tool.AppStat;
import com.Tool.DataBaseAct;
import com.Tool.Drawer;
import com.Tool.IdentityBtn;
import com.Tool.MaterialDrawer;
import com.Tool.ServerSubmit;
import com.Tool.ToolWithActivityIn;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends ActionBarActivity {

	static final int TIME_DIALOG = 0;
	static final int DATE_DIALOG = 1;
	static final int TIME_DIALOG01 = 2;
	static final int DATE_DIALOG01 = 3;
	static final int DATE_DIALOG02 = 4;
	private RequestQueue queue;
	private String userPhoneNum;

	private ImageView exchange;
	private static boolean requestok, carinfook;
	private boolean bstart, bend, blicensenum, bcarbrand, bcolor, bmodel, bmon,
			btue, bwed, bthu, bfri, bsat, bsun,bdate,
			bstartdate, benddate, bearlystarttime, blatestarttime;
    private boolean mbpassenager,mbdriver;

	private EditText carbrand;
	private EditText model;
	private EditText color;
	private EditText licensenum;

	private RadioButton mRadio1, mRadio2;
	private Button sure;
	private RadioGroup order_group;
	private CheckBox mon, tue, wed, thu, fri, sat, sun;

	private String standard_enddate = null;

	private Button datebutton;
	private Button startdate;
	private Button enddate;
	private Button earlystarttime;
	private Button latestarttime;
	private Button increase;
	private Button decrease;
	private Button startplace;
	private Button endplace;

	private int carinfochoosing_type;// 作为车辆表信息修改方法的判别

	private View depdate,lastingdate,detailtime,repeat,remark;

	private float startplace_longitude;
	private float startplace_latitude;
	private float destination_longitude;
	private float destination_latitude;
	private String userrole;
	private String weekrepeat = "";

	private int sum = 0;
	private TextView s1;
	private TextView noteinfo;

	private Calendar c = Calendar.getInstance();

	private String cstype;

	// 用户手机号
	private String UserPhoneNumber;

	// 收藏
	private ImageView star1, star2;
	// 收藏end

	// 表单数据保存

	private String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;

	// 表单数据保存end

	// database

	private DataBaseAct dataBaseAct;

	// databasse end

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

	//todo 迁移中
//	SimpleDateFormat standard_date, standard_time, primary_date, primary_time;
//	Date test_date, now = new Date();
	String standard_startdate = null,
			standard_starttime = null,
			standard_endtime = null;

	//tool类
	ToolWithActivityIn toolWithActivityIn;
	ServerSubmit serverSubmit;

	private IdentityBtn function_identity; /*身份选择，已封装在IdentityBtn.java中*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		toolWithActivityIn = new ToolWithActivityIn(this);
		serverSubmit = new ServerSubmit(this);
        function_identity = new IdentityBtn(this);

		//actionbar
		toolbar = (Toolbar)findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		new MaterialDrawer(this,toolbar);
		//actionbar end

		queue = Volley.newRequestQueue(this);
		exchange = (ImageView) findViewById(R.id.commute_exchange);
		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
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

		function_identity.bdriver = true;
		bmon = true;
		btue = true;
		bwed = true;
		bthu = true;
		bfri = true;

		startdate = (Button) findViewById(R.id.startdate);
		earlystarttime = (Button) findViewById(R.id.earliest_start_time);
		enddate = (Button) findViewById(R.id.enddate);
		latestarttime = (Button) findViewById(R.id.latest_start_time);
		increase = (Button) findViewById(R.id.order_increase);
		decrease = (Button) findViewById(R.id.order_decrease);
		s1 = (TextView) findViewById(R.id.order_count);
		startplace = (Button) findViewById(R.id.commute_startplace);
		endplace = (Button) findViewById(R.id.commute_endplace);
		datebutton = (Button)findViewById(R.id.depature_dates);
		sure = (Button) findViewById(R.id.commute_sure);
		sure.setEnabled(false);

		carbrand = (EditText) findViewById(R.id.CarBrand);
		model = (EditText) findViewById(R.id.CarModel);
		color = (EditText) findViewById(R.id.carcolor);
		licensenum = (EditText) findViewById(R.id.CarNum);

		licensenum.addTextChangedListener(numTextWatcher);
		carbrand.addTextChangedListener(detTextWatcher);
		color.addTextChangedListener(coTextWatcher);
		model.addTextChangedListener(moTextWatcher);

		final TextView content = (TextView) findViewById(R.id.order_content);

		order_group = (RadioGroup) findViewById(R.id.role_radiobutton);
		mRadio1 = (RadioButton) findViewById(R.id.driver);
		mRadio2 = (RadioButton) findViewById(R.id.passenger);// 绑定一个RadioGroup监听器

		mon = (CheckBox) findViewById(R.id.commute_checkBox1);
		tue = (CheckBox) findViewById(R.id.commute_checkBox2);
		wed = (CheckBox) findViewById(R.id.commute_checkBox3);
		thu = (CheckBox) findViewById(R.id.commute_checkBox4);
		fri = (CheckBox) findViewById(R.id.commute_checkBox5);
		sat = (CheckBox) findViewById(R.id.commute_checkBox6);
		sun = (CheckBox) findViewById(R.id.commute_checkBox7);

		star1 = (ImageView) findViewById(R.id.cummute_star);
		star2 = (ImageView) findViewById(R.id.commute_star01);

		depdate = (View)findViewById(R.id.order_layout_depdate);
		detailtime = (View)findViewById(R.id.order_layout_detailtime);
		lastingdate = (View)findViewById(R.id.order_layout_lastingdate);
		repeat = (View)findViewById(R.id.order_layout_rep);
		remark = (View)findViewById(R.id.order_layout_remark);

		noteinfo=(EditText)findViewById(R.id.order_remarkText);
		// 提取用户手机号
		UserPhoneNumber = toolWithActivityIn.get用户手机号从偏好文件();

		// judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		cstype = bundle.getString(AppStat.order页面跳转意图.意图名称);
		if(cstype.compareTo(AppStat.order页面跳转意图.上下班)==0||cstype.compareTo(AppStat
				.order页面跳转意图.上下班重新下单)==0){
			depdate.setVisibility(View.GONE);
			detailtime.setVisibility(View.VISIBLE);
			lastingdate.setVisibility(View.VISIBLE);
			repeat.setVisibility(View.VISIBLE);
			remark.setVisibility(View.GONE);
		}else if(cstype.compareTo(AppStat.order页面跳转意图.长途)==0||cstype.compareTo
				(AppStat.order页面跳转意图.长途重新下单)==0){
			depdate.setVisibility(View.VISIBLE);
			detailtime.setVisibility(View.GONE);
			lastingdate.setVisibility(View.GONE);
			repeat.setVisibility(View.GONE);
			remark.setVisibility(View.VISIBLE);
		}else if(cstype.compareTo(AppStat.order页面跳转意图.短途)==0||cstype.compareTo
				(AppStat.order页面跳转意图.短途重新下单)==0){
			depdate.setVisibility(View.VISIBLE);
			detailtime.setVisibility(View.VISIBLE);
			lastingdate.setVisibility(View.GONE);
			repeat.setVisibility(View.GONE);
			remark.setVisibility(View.GONE);
		}
		if (cstype.compareTo(AppStat.order页面跳转意图.上下班重新下单) == 0) { // 重新下单
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
		}else if(cstype.compareTo(AppStat.order页面跳转意图.长途重新下单)==0){
			startplace.setText(bundle.getString("stpmapname"));
			bstart = true;
			endplace.setText(bundle.getString("epmapname"));
			bend = true;
			datebutton.setText(bundle.getString("re_longway_startdate"));
			bdate = true;
		}else if(cstype.compareTo(AppStat.order页面跳转意图.短途重新下单)==0){
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
			datebutton.setText(bundle.getString("re_short_startdate"));
			bdate = true;
			earlystarttime.setText(bundle.getString("re_short_starttime"));
			bearlystarttime = true;
			latestarttime.setText(bundle.getString("re_short_endtime"));
			blatestarttime = true;
		}
		// judge the value of "pre_page"

		// database
		dataBaseAct = new DataBaseAct(this,UserPhoneNumber);


		// database end

		star1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bstart) {
					if (dataBaseAct.is偏爱地点记录中有该记录(StartPointMapName)) {
						String[] selelectionArgs = { StartPointMapName };
						dataBaseAct.delete偏好地点(selelectionArgs);
						star1.setImageResource(R.drawable.ic_action_not_important);

					} else {
						dataBaseAct.add偏好地点(StartPointMapName,StartPointUserName,
								startplace_longitude,startplace_latitude);

						String[] selelectionArgs = { StartPointMapName };
						dataBaseAct.delete历史地点(selelectionArgs);
						star1.setImageResource(R.drawable.ic_action_important);
					}

				}

			}
		});

		star2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bend) {
					if (dataBaseAct.is偏爱地点记录中有该记录(EndPointMapName)) {
						String[] selelectionArgs = { EndPointMapName };
						dataBaseAct.delete偏好地点(selelectionArgs);
						star2.setImageResource(R.drawable.ic_action_not_important);

					} else {
						dataBaseAct.add偏好地点(EndPointMapName,EndPointUserName,
								destination_longitude,destination_latitude);

						String[] selelectionArgs = { EndPointMapName };
						dataBaseAct.delete历史地点(selelectionArgs);
						star2.setImageResource(R.drawable.ic_action_important);
					}

				}

			}
		});


		mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
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
				
				if (isChecked) {
					bsun = true;
				} else {
					bsun = false;
				}
				confirm();
			}
		});

        //绑定一个RadioGroup监听器(身份选择)
        function_identity.IdentityChoosing("commute",UserPhoneNumber);
        mbdriver = function_identity.bdriver;
        mbpassenager = function_identity.bpassenager;
        confirm();

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				userPhoneNum =toolWithActivityIn.get用户手机号从偏好文件();

				if(cstype.compareTo("workcs")==0||cstype.compareTo("reworkcs")==0){
					function_identity.IdentityChoosing("commute",UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId()) {
						userrole = "y";
					} else
						userrole = "n";

					// 向服务器提交上下班拼车订单请求start!

					Log.e("type",cstype);
					serverSubmit.commute订单提交(userPhoneNum,startdate.getText().toString
							(),enddate.getText().toString(),earlystarttime.getText()
							.toString(),latestarttime.getText().toString(),weekrepeat,
							startplace_longitude,startplace_latitude,
							destination_longitude,destination_latitude,
							startplace.toString(),endplace.toString(),userrole);
					// 向服务器提交上下班拼车订单请求end!

				}else if(cstype.compareTo("longcs")==0||cstype.compareTo("relongcs")==0){
					function_identity.IdentityChoosing("longway",UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId())
						userrole = "p";
					else
						userrole = "d";

					// 向服务器提交长途拼车订单请求start!
					Context phonenumber = OrderActivity.this;
					SharedPreferences filename = phonenumber.getSharedPreferences(
							getString(R.string.PreferenceDefaultName),
							Context.MODE_PRIVATE);
					serverSubmit.longway订单提交(userPhoneNum,userrole,datebutton.getText()
							.toString(),startplace.getText().toString(), endplace
							.getText().toString(), noteinfo.getText().toString());
					// 向服务器提交长途拼车订单请求end!
				}else if(cstype.compareTo("shortcs")==0||cstype.compareTo("reshortcs")==0){
					function_identity.IdentityChoosing("shortway",UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId())
						userrole = "d";
					else
						userrole = "p";

					// 向服务器提交上下班拼车订单请求start!

					serverSubmit.shortway订单提交(userPhoneNum,datebutton.getText()
							.toString(),earlystarttime.getText().toString(),
							latestarttime.getText().toString(),userrole,
							startplace_longitude,startplace_latitude,
							destination_longitude,destination_latitude,startplace.toString(),
							endplace.toString());
					// 向服务器提交上下班拼车订单请求end!
				}
			}

//			//todo 正在移动
//			private void commute_request(final String commute_phonenum,
//						final String commute_startdate,
//						final String commute_enddate,
//						final String commute_starttime, final String commute_endtime) {
//
//
//					weekrepeat = "";
//					if (bmon)
//						weekrepeat += "1";
//					if (btue)
//						weekrepeat += "2";
//					if (bwed)
//						weekrepeat += "3";
//					if (bthu)
//						weekrepeat += "4";
//					if (bfri)
//						weekrepeat += "5";
//					if (bsat)
//						weekrepeat += "6";
//					if (bsun)
//						weekrepeat += "7";
//
//
//					String commute_baseurl = getString(R.string.uri_base)
//							+ getString(R.string.uri_CommuteRequest)
//							+ getString(R.string.uri_addrequest_action);
//					// + "phonenum=" + commute_phonenum + "&startplacex=" +
//					// String.valueOf(startplace_longitude) +
//					// "&startplacey=" + String.valueOf(startplace_latitude) +
//					// "&destinationx=" + String.valueOf(destination_longitude) +
//					// "&destinationy=" + String.valueOf(destination_latitude) +
//					// "&startdate=" + standard_startdate
//					// + "&enddate=" + standard_enddate
//					// + "&starttime=" + standard_starttime
//					// + "&endtime=" + standard_endtime + "&weekrepeat=" +
//					// weekrepeat + "&userrole=" + userrole;
//
//					Log.e("commute_URL", commute_baseurl);
//					// Instantiate the RequestQueue.
//					// Request a string response from the provided URL.
//					StringRequest stringRequest = new StringRequest(
//							Request.Method.POST, commute_baseurl,
//							new Response.Listener<String>() {
//
//								@Override
//								public void onResponse(String response) {
//									Log.d("commute_result", response);
//									JSONObject json1 = null;
//									try {
//										json1 = new JSONObject(response);
//										requestok = json1.getBoolean("result");
//									} catch (JSONException e) {
//
//										e.printStackTrace();
//									}
//
//									if (requestok == true) {
//										Log.e("statue", "requestok");
//										if (function_identity.carinfochoosing_type == 1) {
//											// add
//											// 向服务器发送车辆信息修改请求start!
//											carinfo(commute_phonenum, licensenum
//													.getText().toString(), carbrand
//													.getText().toString(), model
//													.getText().toString(), color
//													.getText().toString(), String
//													.valueOf(sum), 1);
//											// 向服务器发送车辆信息修改end!
//											Log.e("statue", "carinok");
//										} else {
//											// update
//											// 向服务器发送车辆信息修改请求start!
//											carinfo(commute_phonenum, licensenum
//													.getText().toString(), carbrand
//													.getText().toString(), model
//													.getText().toString(), color
//													.getText().toString(), String
//													.valueOf(sum), 2);
//											// 向服务器发送车辆信息修改end!
//										}
//
//										Intent sure = new Intent(
//												OrderActivity.this,
//												OrderResponseActivity.class);
//										sure.putExtra(
//												getString(R.string.request_response),
//												"true");
//										sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//										startActivity(sure);
//									} else {
//										// Toast errorinfo =
//										// Toast.makeText(getApplicationContext(),
//										// "提交失败", Toast.LENGTH_LONG);
//										// errorinfo.show();
//										Intent sure = new Intent(
//												OrderActivity.this,
//												OrderResponseActivity.class);
//										sure.putExtra(
//												getString(R.string.request_response),
//												"false");
//										sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//										startActivity(sure);
//									}
//								}
//						}, new Response.ErrorListener() {
//							@Override
//							public void onErrorResponse(VolleyError error) {
//								Log.e("commute_result", error.getMessage(),
//										error);
//								// Toast errorinfo = Toast.makeText(null,
//								// "网络连接失败", Toast.LENGTH_LONG);
//								// errorinfo.show();
//								Intent sure = new Intent(OrderActivity.this,
//										OrderResponseActivity.class);
//								sure.putExtra(
//										getString(R.string.request_response),
//										"false");
//								sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//								startActivity(sure);
//							}
//						}) {
//					protected Map<String, String> getParams() {
//						// POST方法重写getParams函数
//						// 强制转换日期格式start
//						try {
//							test_date = primary_date.parse(commute_enddate);
//							standard_enddate = standard_date
//									.format(test_date);
//						} catch (ParseException e) {
//
//							e.printStackTrace();
//						}
//
//						try {
//							test_date = primary_date.parse(commute_startdate);
//							standard_startdate = standard_date
//									.format(test_date);
//						} catch (ParseException e) {
//
//							e.printStackTrace();
//						}
//
//						try {
//							test_date = primary_time.parse(commute_starttime);
//							standard_starttime = standard_time
//									.format(test_date);
//						} catch (ParseException e) {
//
//							e.printStackTrace();
//						}
//
//						try {
//							test_date = primary_time.parse(commute_endtime);
//							standard_endtime = standard_time.format(test_date);
//						} catch (ParseException e) {
//
//							e.printStackTrace();
//						}
//						// 强制转换日期格式end!
//
//						Map<String, String> params = new HashMap<String, String>();
//						params.put(getString(R.string.uri_phonenum),
//								commute_phonenum);
//						params.put(getString(R.string.uri_startplacex),
//								String.valueOf(startplace_longitude));
//						params.put(getString(R.string.uri_startplacey),
//								String.valueOf(startplace_latitude));
//						params.put(getString(R.string.uri_startplace),
//								startplace.getText().toString());
//						params.put(getString(R.string.uri_destinationx),
//								String.valueOf(destination_longitude));
//						params.put(getString(R.string.uri_destinationy),
//								String.valueOf(destination_latitude));
//						params.put(getString(R.string.uri_destination),
//								endplace.getText().toString());
//						params.put(getString(R.string.uri_startdate),
//								standard_startdate);
//						params.put(getString(R.string.uri_enddate),
//								standard_enddate);
//						params.put(getString(R.string.uri_starttime),
//								standard_starttime);
//						params.put(getString(R.string.uri_endtime),
//								standard_endtime);
//						params.put(getString(R.string.uri_weekrepeat),
//								weekrepeat);
//						params.put(getString(R.string.uri_supplycar), userrole);
//						// Log.w("phonemum", commute_phonenum);
//						// Log.w("startplacex",
//						// String.valueOf(startplace_longitude));
//						// Log.w("startdate", standard_startdate);
//						// Log.w("starttime", standard_starttime);
//						// Log.w("weekrepeat",weekrepeat );
//						// Log.w("userrole",userrole );
//						// Log.w("enddate",standard_enddate );
//
//						return params;
//					}
//				};
//
//				queue.add(stringRequest);
//
//			}

//			private void shortway_request(final String shortway_phonenum,
//			                              final String shortway_date,
//			                              final String shortway_starttime,
//			                              final String shortway_endtime) {
//
//				//todo 迁移中
//
//				// 强制转换日期格式start
////				try {
////					test_date = primary_date.parse(shortway_date);
////					standard_startdate = standard_date
////							.format(test_date);
////				} catch (ParseException e) {
////
////					e.printStackTrace();
////				}
////
////				try {
////					test_date = primary_time.parse(shortway_starttime);
////					standard_starttime = standard_time
////							.format(test_date);
////				} catch (ParseException e) {
////
////					e.printStackTrace();
////				}
////
////				try {
////					test_date = primary_time.parse(shortway_endtime);
////					standard_endtime = standard_time.format(test_date);
////				} catch (ParseException e) {
////
////					e.printStackTrace();
////				}
//				// 强制转换日期格式end!
//
//
//			}
//
//
//			private void longway_request(final String longway_phonenum,
//			                             final String longway_userrole,
//			                             final String longway_startdate,
//			                             final String longway_startplace,
//			                             final String longway_destination,
//			                             final String longway_noteinfo) {
//
//
//				String longway_addrequest_baseurl = getString(R.string.uri_base)
//						+ getString(R.string.uri_LongwayPublish)
//						+ getString(R.string.uri_addpublish_action);
//				// + "phonenum=" + longway_phonenum
//				// + "&userrole=" + longway_userrole
//				// + "&startdate=" + standard_longway_startdate
//				// + "&startplace=" + longway_startplace
//				// + "&destination=" + longway_destination
//				// + "&noteinfo=" + longway_noteinfo;
//
//				// Log.d("longway_baseurl",longway_addrequest_baseurl);
//
//				StringRequest stringRequest = new StringRequest(
//						Request.Method.POST, longway_addrequest_baseurl,
//						new Response.Listener<String>() {
//
//							@Override
//							public void onResponse(String response) {
//								Log.d("longway_result", response);
//								JSONObject json1 = null;
//								try {
//									json1 = new JSONObject(response);
//									requestok = json1.getBoolean("result");
//								} catch (JSONException e) {
//
//									e.printStackTrace();
//								}
//								if (requestok == true) {
//
//									if (carinfochoosing_type == 1) {
//										// add
//										// 向服务器发送车辆信息修改请求start!
//										carinfo(longway_phonenum, licensenum
//												.getText().toString(), carbrand
//												.getText().toString(), model
//												.getText().toString(), color
//												.getText().toString(), String
//												.valueOf(sum), 1);
//										// 向服务器发送车辆信息修改end!
//									} else {
//										// update
//										// 向服务器发送车辆信息修改请求start!
//										carinfo(longway_phonenum, licensenum
//												.getText().toString(), carbrand
//												.getText().toString(), model
//												.getText().toString(), color
//												.getText().toString(), String
//												.valueOf(sum), 2);
//										// 向服务器发送车辆信息修改end!
//									}
//
//									Intent sure = new Intent(
//										OrderActivity.this,
//											OrderResponseActivity.class);
//									sure.putExtra(
//											getString(R.string.request_response),
//											"true");
//									sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//									startActivity(sure);
//								} else {
//									// Toast errorinfo =
//									// Toast.makeText(getApplicationContext(),
//									// "提交失败", Toast.LENGTH_LONG);
//									// errorinfo.show();
//									Intent sure = new Intent(
//											OrderActivity.this,
//											OrderResponseActivity.class);
//									sure.putExtra(
//											getString(R.string.request_response),
//											"false");
//									sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//									startActivity(sure);
//								}
//							}
//						}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						Log.e("longway_result", error.getMessage(),
//								error);
//						// Toast errorinfo = Toast.makeText(null,
//						// "网络连接失败", Toast.LENGTH_LONG);
//						// errorinfo.show();
//						Intent sure = new Intent(OrderActivity.this,
//								OrderResponseActivity.class);
//						sure.putExtra(
//								getString(R.string.request_response),
//								"false");
//						sure.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(sure);
//					}
//				}) {
//					protected Map<String, String> getParams() {
//						// POST方法重写getParams函数
//
//						// 强制转换日期格式start
//						try {
//							test_date = primary_date.parse(longway_startdate);
//							standard_startdate = standard_date
//									.format(test_date);
//						} catch (ParseException e) {
//
//							e.printStackTrace();
//						}
//
//						// 强制转换日期格式end!
//
//						Map<String, String> params = new HashMap<String, String>();
//						params.put(getString(R.string.uri_phonenum),
//								longway_phonenum);
//						params.put(getString(R.string.uri_userrole),
//								longway_userrole);
//						params.put(getString(R.string.uri_startplace),
//								longway_startplace);
//						params.put(getString(R.string.uri_destination),
//								longway_destination);
//						params.put(getString(R.string.uri_startdate),
//								standard_startdate);
//						params.put(getString(R.string.uri_noteinfo),
//								longway_noteinfo);
//
//						return params;
//					}
//				};
//				queue.add(stringRequest);
//			}
		});

		startplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				startActivityForResult(new Intent(OrderActivity.this,
						ChooseAddressActivity.class), 1);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				startActivityForResult(new Intent(OrderActivity.this,
						ChooseArrivalActivity.class), 2);
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

		startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(DATE_DIALOG);
			}

		});

		enddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(DATE_DIALOG01);

			}
		});

		earlystarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(TIME_DIALOG);
			}
		});

		datebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				showDialog(DATE_DIALOG02);
			}
		});

		latestarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(TIME_DIALOG01);

			}
		});
	}

	@Override
	public void onResume() {

		super.onResume();

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

				if (dataBaseAct.is偏爱地点记录中有该记录(StartPointMapName)) {
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

				if (dataBaseAct.is偏爱地点记录中有该记录(EndPointMapName)) {
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


	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, mTimeSetListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener3,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case DATE_DIALOG01:
			return new DatePickerDialog(this, mDateSetListener1,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case TIME_DIALOG01:
			return new TimePickerDialog(this, mTimeSetListener1,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		case DATE_DIALOG02:
			return new DatePickerDialog(this,mDateSetListener2,c.get(Calendar.YEAR),
					c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));

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

			DisplayToast("时间为:" + String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			latestarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			blatestarttime = true;
			confirm();
		}
	};
	private OnDateSetListener mDateSetListener2 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
		                      int dayofMonth) {

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

	private OnDateSetListener mDateSetListener3 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
		                      int dayofMonth) {

			DisplayToast(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			startdate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bdate = true;
			confirm();
		}
	};


	private OnDateSetListener mDateSetListener1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {

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

			if (temp.length() != 0) {
				bmodel = true;
			} else {
				bmodel = false;
			}
			confirm();

		}
	};

	public void confirm() {
		mbdriver = function_identity.bdriver;
		mbpassenager = function_identity.bpassenager;
		if(cstype.compareTo("reworkcs")==0||cstype.compareTo("workcs")==0){
			if (bstart
					&& bend
					&& (bmon || btue || bwed || bthu || bfri || bsat || bsun)
					&& ((mbdriver && blicensenum && bcolor && bcarbrand) ||mbpassenager)
					//todo bstartdate 有问题
					&& bstartdate && benddate && bearlystarttime && blatestarttime
					&& (sum > 0)) {
				sure.setEnabled(true);
			} else {
				sure.setEnabled(false);
			}
		}else if(cstype.compareTo("relongcs")==0||cstype.compareTo("longcs")==0){
			if (bstart && bend
					&& ((mbdriver && blicensenum && bcolor && bcarbrand) || (mbpassenager))
					&& bdate && (sum > 0)) {
				sure.setEnabled(true);
			} else {
				sure.setEnabled(false);
			}
		}else if(cstype.compareTo("reshortcs")==0||cstype.compareTo("shortcs")==0){
			if (bstart
					&& bend
					&& ((mbdriver && blicensenum && bcolor && bcarbrand) || mbpassenager)
					&& bearlystarttime && blatestarttime && bdate && (sum > 0)) {
				sure.setEnabled(true);
			} else {
				sure.setEnabled(false);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent returnp = new Intent(OrderActivity.this,
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
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}

}
