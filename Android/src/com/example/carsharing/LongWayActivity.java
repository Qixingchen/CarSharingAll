/*
 * ��;ƴ������
 * ������дҳ
 * ���ʷ�������ȡ������Ϣ���Զ���䳵����Ϣ
 * ���ʷ������ύ����
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
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

public class LongWayActivity extends Activity {

	private EditText licensenum, carbrand;
	private EditText model, color;
	private ImageView exchange;
	private static boolean requestok, carinfook;

	private boolean 出发地址_完成输入, 目的地址_完成输入, 车牌号_完成输入, 车颜色_完成输入, 车品牌_完成输入, 车型号_完成输入, 日期_完成输入,
			司机吗, 乘客吗;
	private int mHour, mMinute, mday, month, myear;
	private RadioGroup longway_group;
	private RadioButton passangerRadioButton;
	private RadioButton driverRadioButton;
	static final int DATE_DIALOG = 1;

	private RequestQueue queue;

	private int carinfochoosing_type;// ��Ϊ��������Ϣ�޸ķ������б�

	Date test_date, now = new Date();

	SimpleDateFormat standard_date, primary_date;
	String standard_longway_startdate = null;

	private Button sure;
	private String username;
	Button datebutton;
	Button increase;
	Button decrease;
	EditText startplace;
	EditText endplace;
	EditText noteinfo;
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
	private TextView drawername;
	private TextView drawernum;
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	// �û��ֻ���
	String UserPhoneNumber;
	String userrole;

	int sum = 0;
	TextView s1;
	Calendar c = Calendar.getInstance();

	// actionbar!!
	Drawer activity_drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	// actionbarend!!

	// �����ݱ���

	String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;

	// �����ݱ���end

	// database

	DatabaseHelper db;
	SQLiteDatabase db1;

	// databasse end

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
		System.out.println("abc");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_long_way);

		activity_drawer = new Drawer(this, R.id.long_way_layout);
		mDrawerToggle = activity_drawer.newdrawer();
		mDrawerLayout = activity_drawer.setDrawerLayout();

		// ���ڡ�ʱ���׼��ʽ
		standard_date = new SimpleDateFormat("yyyy-MM-dd",
				Locale.SIMPLIFIED_CHINESE);
		primary_date = new SimpleDateFormat("yyyy��MM��dd��",
				Locale.SIMPLIFIED_CHINESE);

		queue = Volley.newRequestQueue(this);
		exchange = (ImageView) findViewById(R.id.longway_exchange);
		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String temp = startplace.getText().toString();

				startplace.setText(endplace.getText().toString());
				endplace.setText(temp);

			}
		});

		司机吗 = true;

		datebutton = (Button) findViewById(R.id.longway_dates);
		increase = (Button) findViewById(R.id.longway_increase);
		decrease = (Button) findViewById(R.id.longway_decrease);
		s1 = (TextView) findViewById(R.id.longway_count);
		sure = (Button) findViewById(R.id.longway_sure);
		sure.setEnabled(false);
		startplace = (EditText) findViewById(R.id.longway_start_place);
		endplace = (EditText) findViewById(R.id.longway_end_place);
		noteinfo = (EditText) findViewById(R.id.longway_remarkText);
		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);
		carbrand = (EditText) findViewById(R.id.longway_CarBrand);
		model = (EditText) findViewById(R.id.longway_CarModel);
		color = (EditText) findViewById(R.id.longway_color);
		setting = findViewById(R.id.drawer_setting);
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
		personalcenter = findViewById(R.id.drawer_personalcenter);
		taxi = findViewById(R.id.drawer_taxi);

		// judge the value of "pre_page"
		Bundle bundle = this.getIntent().getExtras();
		String PRE_PAGE = bundle.getString("pre_page");
		if (PRE_PAGE.compareTo("ReOrder") == 0) { // �����µ�
			startplace.setText(bundle.getString("stpmapname"));
			出发地址_完成输入 = true;
			endplace.setText(bundle.getString("epmapname"));
			目的地址_完成输入 = true;
			datebutton.setText(bundle.getString("re_longway_startdate"));
			日期_完成输入 = true;
		}
		// judge the value of "pre_page"

		// ��ȡ�û��ֻ���
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
		about = findViewById(R.id.drawer_respond);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent about = new Intent(LongWayActivity.this,
						AboutActivity.class);
				startActivity(about);
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent setting = new Intent(LongWayActivity.this,
						SettingActivity.class);
				startActivity(setting);
			}
		});

		// database end

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
				Intent personalcenter = new Intent(LongWayActivity.this,
						PersonalCenterActivity.class);
				personalcenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(personalcenter);
			}
		});

		// ��һ��RadioGroup������

		longway_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub18
				// ��ȡ������ѡ�����ID

				// "�����ṩ��"���䣬"�Ҳ����ṩ��"ʹ���ƺŵȱ༭�򲻿ɱ༭��������textView
				if (checkedId == passangerRadioButton.getId()) {
					乘客吗 = true;
					司机吗 = false;

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
					licensenum.setHintTextColor(Color.parseColor("#cccccc"));
					carbrand.setHintTextColor(Color.parseColor("#cccccc"));
					color.setHintTextColor(Color.parseColor("#cccccc"));
					model.setHintTextColor(Color.parseColor("#cccccc"));
					licensenum.setInputType(InputType.TYPE_NULL);
					carbrand.setInputType(InputType.TYPE_NULL);
					color.setInputType(InputType.TYPE_NULL);
					model.setInputType(InputType.TYPE_NULL);
				} else {
					乘客吗 = false;
					司机吗 = true;

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
					licensenum.setHintTextColor(Color.parseColor("#9F35FF"));
					carbrand.setHintTextColor(Color.parseColor("#9F35FF"));
					color.setHintTextColor(Color.parseColor("#9F35FF"));
					model.setHintTextColor(Color.parseColor("#9F35FF"));
					// licensenum.setText("");
					// carbrand.setText("");
					// color.setText("");
					// model.setText("");
					licensenum.setInputType(InputType.TYPE_CLASS_TEXT);
					carbrand.setInputType(InputType.TYPE_CLASS_TEXT);
					color.setInputType(InputType.TYPE_CLASS_TEXT);
					model.setInputType(InputType.TYPE_CLASS_TEXT);

					// ������������ѯ������Ϣ��start!
					selectcarinfo(UserPhoneNumber);
					// ������������ѯ������Ϣ��end!
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

									if (jas_id.compareTo("") != 0) { // �������ϴ��ڳ�����Ϣʱ

										carinfochoosing_type = 2;

										carbrand.setText(json
												.getString("carBrand"));
										model.setText(json
												.getString("carModel"));
										licensenum.setText(json
												.getString("carNum"));
										color.setText(json
												.getString("carColor"));

									}
									{
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

				if (longway_group.getCheckedRadioButtonId() == passangerRadioButton
						.getId())
					userrole = "p";
				else
					userrole = "d";

				// ��������ύ��;ƴ����������start!
				Context phonenumber = LongWayActivity.this;
				SharedPreferences filename = phonenumber.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				username = filename.getString("refreshfilename", "0");
				longway_request(username, userrole, datebutton.getText()
						.toString(), startplace.getText().toString(), endplace
						.getText().toString(), noteinfo.getText().toString());
				// ��������ύ��;ƴ����������end!
			}

			private void longway_request(final String longway_phonenum,
					final String longway_userrole,
					final String longway_startdate,
					final String longway_startplace,
					final String longway_destination,
					final String longway_noteinfo) {
				// TODO Auto-generated method stub

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
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (requestok == true) {

									if (carinfochoosing_type == 1) {
										// add
										// ����������ͳ�����Ϣ�޸�����start!
										carinfo(longway_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 1);
										// ����������ͳ�����Ϣ�޸�end!
									} else {
										// update
										// ����������ͳ�����Ϣ�޸�����start!
										carinfo(longway_phonenum, licensenum
												.getText().toString(), carbrand
												.getText().toString(), model
												.getText().toString(), color
												.getText().toString(), String
												.valueOf(sum), 2);
										// ����������ͳ�����Ϣ�޸�end!
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
									// "�ύʧ��", Toast.LENGTH_LONG);
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
								// "��������ʧ��", Toast.LENGTH_LONG);
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
						// POST������дgetParams����

						// ǿ��ת�����ڸ�ʽstart
						try {
							test_date = primary_date.parse(longway_startdate);
							standard_longway_startdate = standard_date
									.format(test_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// ǿ��ת�����ڸ�ʽend!

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

		// ������תstart!
		shortway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent shortway = new Intent(LongWayActivity.this,
						ShortWayActivity.class);
				startActivity(shortway);
			}
		});

		longway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		commute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent commute = new Intent(LongWayActivity.this,
						CommuteActivity.class);
				startActivity(commute);
			}
		});

		// ������תend!

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

		datebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});
	}

	TextWatcher spTextWatcher = new TextWatcher() {
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
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				出发地址_完成输入 = true;
			} else {
				出发地址_完成输入 = false;
			}
			confirm();

		}
	};

	TextWatcher epTextWatcher = new TextWatcher() {
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
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				目的地址_完成输入 = true;
			} else {
				目的地址_完成输入 = false;
			}
			confirm();

		}
	};

	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = LongWayActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);
		Context context = LongWayActivity.this;
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String fullname = sharedPref.getString("refreshname", "����");
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

		// Uri.encode(modify_baseurl, "@#&=*+-_.,:!?()/~'%");// ���ı���

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
									getApplicationContext(), "������Ϣ�޸�ʧ��",
									Toast.LENGTH_LONG);
							errorinfo.show();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("carinfo_result", error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null,
						// "��������ʧ��", Toast.LENGTH_LONG);
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
			// TODO Auto-generated method stub
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			DisplayToast(String.valueOf(year) + "��"
					+ String.valueOf(monthofYear + 1) + "��"
					+ String.valueOf(dayofMonth) + "��");
			datebutton.setText(String.valueOf(year) + "��"
					+ String.valueOf(monthofYear + 1) + "��"
					+ String.valueOf(dayofMonth) + "��");
			日期_完成输入 = true;
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
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = licensenum.getSelectionStart();
			editEnd = licensenum.getSelectionEnd();
			if (temp.length() > 0) {
				车牌号_完成输入 = true;
			} else {
				车牌号_完成输入 = false;
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
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				车品牌_完成输入 = true;
			} else {
				车品牌_完成输入 = false;
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
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				车颜色_完成输入 = true;
			} else {
				车颜色_完成输入 = false;
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
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = carbrand.getSelectionStart();
			editEnd = carbrand.getSelectionEnd();
			if (temp.length() != 0) {
				车型号_完成输入 = true;
			} else {
				车型号_完成输入 = false;
			}
			confirm();

		}
	};

	public void confirm() {
		if (出发地址_完成输入 && 目的地址_完成输入
				&& ((司机吗 && 车牌号_完成输入 && 车颜色_完成输入 && 车品牌_完成输入) || (乘客吗))
				&& 日期_完成输入 && (sum > 0)) {
			sure.setEnabled(true);
		} else {
			sure.setEnabled(false);
		}
	}

	// actionbar!!
	/* ��invalidateOptionsMenu()����ʱ���� */
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // ���nav drawer�Ǵ򿪵�, ������������ͼ�������action items
	// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// ��onRestoreInstanceState������ͬ��������״̬.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ���¼����ݸ�ActionBarDrawerToggle, �������true����ʾapp ͼ�����¼��Ѿ�������
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// �����������action bar items...

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
