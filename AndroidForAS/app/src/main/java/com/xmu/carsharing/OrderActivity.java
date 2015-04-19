/*
 * 拼车界面
 * 订单填写页
 * 访问服务器获取车辆信息，自动填充车辆信息
 * 访问服务器提交订单
 * todo 历史记录读取完成后不能自动刷新界面.界面一直显示 "历史记录读取中"
 * todo 车辆信息需要在一进入就刷新,或者默认选择为乘客
 *
 */

package com.xmu.carsharing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

import com.Tool.AppStat;
import com.Tool.DataBaseAct;
import com.Tool.IdentityBtn;
import com.Tool.MaterialDrawer;
import com.Tool.ServerSubmit;
import com.Tool.Tool;
import com.Tool.ToolWithActivityIn;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

public class OrderActivity extends ActionBarActivity {

	static final int TIME最早开始 = 0;
	static final int DATE_出发另一个 = 1;
	static final int TIME_最晚 = 2;
	static final int DATE_结束 = 3;
	static final int DATE_出发 = 4;
	private RequestQueue queue;
	private String userPhoneNum;

	private ImageView exchange;
	private static boolean requestok, carinfook;
	private boolean bstart, bend, blicensenum, bcarbrand, bcolor, bmodel, bmon,
			btue, bwed, bthu, bfri, bsat, bsun, bdate,
			bstartdate, benddate, bearlystarttime, blatestarttime;
	private boolean mbpassenager, mbdriver;

	private EditText carbrand;
	private EditText model;
	private EditText color;
	private EditText licensenum;

	private RadioButton mRadio1, mRadio2;
	private Button sure;
	private RadioGroup order_group;
	private CheckBox mon, tue, wed, thu, fri, sat, sun;

	private String standard_enddate = null;

	private Button startDateButton;
	private Button startdate;
	private Button enddate;
	private Button earlystarttime;
	private Button latestarttime;
	private Button increase;
	private Button decrease;
	private Button startplace;
	private Button endplace;

	private int carinfochoosing_type;// 作为车辆表信息修改方法的判别

	private View depdate, lastingdate, detailtime, repeat, remark;

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
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		new MaterialDrawer(this, toolbar);
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
		startDateButton = (Button) findViewById(R.id.depature_dates);
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

		depdate = findViewById(R.id.order_layout_depdate);
		detailtime = findViewById(R.id.order_layout_detailtime);
		lastingdate = findViewById(R.id.order_layout_lastingdate);
		repeat = findViewById(R.id.order_layout_rep);
		remark = findViewById(R.id.order_layout_remark);

		noteinfo = (EditText) findViewById(R.id.order_remarkText);
		// 提取用户手机号
		UserPhoneNumber = toolWithActivityIn.get用户手机号从偏好文件();

		// judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		cstype = bundle.getString(AppStat.order页面跳转意图.意图名称);
		if (cstype.compareTo(AppStat.order页面跳转意图.上下班) == 0 || cstype.compareTo(AppStat
				.order页面跳转意图.上下班重新下单) == 0) {
			depdate.setVisibility(View.GONE);
			detailtime.setVisibility(View.VISIBLE);
			lastingdate.setVisibility(View.VISIBLE);
			repeat.setVisibility(View.VISIBLE);
			remark.setVisibility(View.GONE);
		} else if (cstype.compareTo(AppStat.order页面跳转意图.长途) == 0 || cstype.compareTo
				(AppStat.order页面跳转意图.长途重新下单) == 0) {
			depdate.setVisibility(View.VISIBLE);
			detailtime.setVisibility(View.GONE);
			lastingdate.setVisibility(View.GONE);
			repeat.setVisibility(View.GONE);
			remark.setVisibility(View.VISIBLE);
		} else if (cstype.compareTo(AppStat.order页面跳转意图.短途) == 0 || cstype.compareTo
				(AppStat.order页面跳转意图.短途重新下单) == 0) {
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
		} else if (cstype.compareTo(AppStat.order页面跳转意图.长途重新下单) == 0) {
			startplace.setText(bundle.getString("stpmapname"));
			bstart = true;
			endplace.setText(bundle.getString("epmapname"));
			bend = true;
			startDateButton.setText(bundle.getString("re_longway_startdate"));
			bdate = true;
		} else if (cstype.compareTo(AppStat.order页面跳转意图.短途重新下单) == 0) {
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
			startDateButton.setText(bundle.getString("re_short_startdate"));
			bdate = true;
			earlystarttime.setText(bundle.getString("re_short_starttime"));
			bearlystarttime = true;
			latestarttime.setText(bundle.getString("re_short_endtime"));
			blatestarttime = true;
		}
		// judge the value of "pre_page"

		// database
		dataBaseAct = new DataBaseAct(this, UserPhoneNumber);
		// database end

		star1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bstart) {
					if (dataBaseAct.is偏爱地点记录中有该记录(StartPointMapName)) {
						String[] selelectionArgs = {StartPointMapName};
						dataBaseAct.delete偏好地点(selelectionArgs);
						star1.setImageResource(R.drawable.ic_action_not_important);

					} else {
						dataBaseAct.add偏好地点(StartPointMapName, StartPointUserName,
								startplace_longitude, startplace_latitude);

						String[] selelectionArgs = {StartPointMapName};
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
						String[] selelectionArgs = {EndPointMapName};
						dataBaseAct.delete偏好地点(selelectionArgs);
						star2.setImageResource(R.drawable.ic_action_not_important);

					} else {
						dataBaseAct.add偏好地点(EndPointMapName, EndPointUserName,
								destination_longitude, destination_latitude);

						String[] selelectionArgs = {EndPointMapName};
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
		function_identity.IdentityChoosing("commute", UserPhoneNumber);
		mbdriver = function_identity.bdriver;
		mbpassenager = function_identity.bpassenager;
		confirm();

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				weekrepeat = Tool.getWeekRepeat(bmon, btue, bwed, bthu, bfri, bsat, bsun);

				if (cstype.compareTo("workcs") == 0 || cstype.compareTo("reworkcs") == 0) {
					function_identity.IdentityChoosing("commute", UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId()) {
						userrole = "y";
					} else
						userrole = "n";

					// 向服务器提交上下班拼车订单请求start!

					Log.e("type", cstype);
					serverSubmit.commute订单提交(userPhoneNum, startdate.getText().toString
									(), enddate.getText().toString(), earlystarttime.getText()
									.toString(), latestarttime.getText().toString(), weekrepeat,
							startplace_longitude, startplace_latitude,
							destination_longitude, destination_latitude,
							startplace.getText().toString(), endplace.getText().toString(),
							userrole);
					// 向服务器提交上下班拼车订单请求end!

				} else if (cstype.compareTo("longcs") == 0 || cstype.compareTo("relongcs") == 0) {
					function_identity.IdentityChoosing("longway", UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId())
						userrole = "p";
					else
						userrole = "d";

					// 向服务器提交长途拼车订单请求start!
					Context phonenumber = OrderActivity.this;
					SharedPreferences filename = phonenumber.getSharedPreferences(
							getString(R.string.PreferenceDefaultName),
							Context.MODE_PRIVATE);
					serverSubmit.longway订单提交(userPhoneNum, userrole, startDateButton.getText()
							.toString(), startplace.getText().toString(), endplace
							.getText().toString(), noteinfo.getText().toString());
					// 向服务器提交长途拼车订单请求end!
				} else if (cstype.compareTo("shortcs") == 0 || cstype.compareTo("reshortcs") == 0) {
					function_identity.IdentityChoosing("shortway", UserPhoneNumber);
					if (order_group.getCheckedRadioButtonId() == mRadio1.getId())
						userrole = "d";
					else
						userrole = "p";

					// 向服务器提交上下班拼车订单请求start!

					serverSubmit.shortway订单提交(userPhoneNum, startDateButton.getText()
									.toString(), earlystarttime.getText().toString(),
							latestarttime.getText().toString(), userrole,
							startplace_longitude, startplace_latitude,
							destination_longitude, destination_latitude, startplace.toString(),
							endplace.toString());
					// 向服务器提交上下班拼车订单请求end!
				}
			}

		});

		startplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivityForResult(new Intent(OrderActivity.this,
						ChooseAddressActivity.class), AppStat.startOrDes地点Intent标号.start);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivityForResult(new Intent(OrderActivity.this,
						ChooseAddressActivity.class), AppStat.startOrDes地点Intent标号.des);
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
				if (sum < 1) {
					sum = 1;
				}
				s1.setText("" + sum);
				confirm();
			}
		});

		startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialog(DATE_出发另一个);
			}

		});

		enddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialog(DATE_结束);

			}
		});

		earlystarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialog(TIME最早开始);
			}
		});

		startDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				showDialog(DATE_出发);
			}
		});

		latestarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialog(TIME_最晚);

			}
		});
	}

	@Override
	public void onResume() {

		super.onResume();
		userPhoneNum = toolWithActivityIn.get用户手机号从偏好文件();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case AppStat.startOrDes地点Intent标号.start: {
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
				case AppStat.startOrDes地点Intent标号.des: {

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
			case TIME最早开始:
				return new TimePickerDialog(this, mEarlyStartTimeListener,
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
			case DATE_出发另一个:
				return new DatePickerDialog(this, mStartDateSetListener1,
						c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
			case DATE_结束:
				return new DatePickerDialog(this, mEndDateSetListener,
						c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
			case TIME_最晚:
				return new TimePickerDialog(this, mLateStartListener,
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
			case DATE_出发:
				return new DatePickerDialog(this, mStartDateSetListener, c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));

		}
		return null;
	}

	private OnTimeSetListener mEarlyStartTimeListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			//todo 删除00s
			earlystarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			bearlystarttime = true;
			confirm();
		}
	};

	private OnTimeSetListener mLateStartListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


			latestarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00秒");
			blatestarttime = true;
			confirm();
		}
	};

	//todo 这两个什么区别..
	private OnDateSetListener mStartDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
		                      int dayofMonth) {


			startDateButton.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bdate = true;
			bstartdate = true;
			confirm();
		}
	};

	private OnDateSetListener mStartDateSetListener1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
		                      int dayofMonth) {


			startdate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			bdate = true;
			bstartdate = true;
			confirm();
		}
	};


	private OnDateSetListener mEndDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
		                      int dayofMonth) {


			enddate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
			benddate = true;
			confirm();
		}
	};

	//todo 整理或删除文本填写检测
	TextWatcher numTextWatcher = new TextWatcher() {
		private CharSequence temp;



		@Override
		public void onTextChanged(CharSequence s, int start, int before,
		                          int count) {

			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
		if (cstype.compareTo("reworkcs") == 0 || cstype.compareTo("workcs") == 0) {
			if (bstart
					&& bend
					&& (bmon || btue || bwed || bthu || bfri || bsat || bsun)
					&& ((mbdriver && blicensenum && bcolor && bcarbrand) || mbpassenager)
					&& bstartdate && benddate && bearlystarttime && blatestarttime
					&& (sum > 0)) {
				sure.setEnabled(true);
			} else {
				sure.setEnabled(false);
			}
		} else if (cstype.compareTo("relongcs") == 0 || cstype.compareTo("longcs") == 0) {
			if (bstart && bend
					&& ((mbdriver && blicensenum && bcolor && bcarbrand) || (mbpassenager))
					&& bdate && (sum > 0)) {
				sure.setEnabled(true);
			} else {
				sure.setEnabled(false);
			}
		} else if (cstype.compareTo("reshortcs") == 0 || cstype.compareTo("shortcs") == 0) {
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
