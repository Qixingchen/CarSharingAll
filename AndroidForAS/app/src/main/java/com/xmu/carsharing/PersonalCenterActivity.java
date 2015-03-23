/*
 * 个人中心
 * 1.头像，性别，姓名，年龄，品牌，车牌号从本地获取，个人信息可编辑
 * 2.onresume 更新上下班拼车，短途拼车，长途拼车个人发布的信息，显示第一条在该页
 * 3.onresume 收藏的地址从本地获取，显示第一条在该页
 * 4.评价锁定，评价由订单结束获取
 * 5.注销更换账号
 */

package com.xmu.carsharing;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Tool.DatabaseHelper;
import com.Tool.Drawer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalCenterActivity extends Activity {

	TextView firsthistory;
	TextView firstdeal;
	TextView firstfavorite;

	public static ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, Object>> mylist2 = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	public static RequestQueue queue;

	// actionbar!!
	Drawer drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	// actionbarend!!

	boolean isExit;
	boolean bfirsthistory = false, bfirstdeal = false, bfirstfavorite = false;
	Context context = PersonalCenterActivity.this;
	static ImageView image;

	TextView name;
	TextView age;
	TextView description;
	TextView carnum;
	TextView sex;
	private final static String CACHE = "/css";

	RatingBar ratingbar;
	// progressbar
	private static MyProgressDialog pd;  /*建议对progressbar的实现进行封装*/
	// progressbar end



	// 用户手机号
	String UserPhoneNumber;

	// database
	private boolean loadok;
	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor dbresult;

	// database end!!

	private String logtag = "个人中心";

	Button quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);

		//actionbar
		drawer = new Drawer(this, R.id.person_center_layout);
		mDrawerToggle = drawer.newdrawer();
		mDrawerLayout = drawer.setDrawerLayout();
		//actionbar end

		firsthistory = (TextView) findViewById(R.id.first_history);
		firstdeal = (TextView) findViewById(R.id.first_receiving);
		firstfavorite = (TextView) findViewById(R.id.first_address);

		queue = Volley.newRequestQueue(this);
		Button historymore = (Button) findViewById(R.id.personalcenter_historymore);
		ratingbar = (RatingBar) findViewById(R.id.personalcenter_ratingBar);
		Button receivingmore = (Button) findViewById(R.id.personalcenter_receivingmore);
		Button addressmore = (Button) findViewById(R.id.personalcenter_addressmore);
		ImageButton imageedit = (ImageButton) findViewById(R.id.personalcenter_imageEdit);
		Button iwantcar = (Button) findViewById(R.id.personalcenter_iwantcar);
		quit = (Button) findViewById(R.id.personalcenter_quit);
		name = (TextView) findViewById(R.id.personalcenter_name);
		age = (TextView) findViewById(R.id.personalcenter_age);
		description = (TextView) findViewById(R.id.personalcenter_description);
		carnum = (TextView) findViewById(R.id.personalcenter_carnumber);
		sex = (TextView) findViewById(R.id.personalcenter_sex);
		image = (ImageView) findViewById(R.id.personalcenter_icon);

		// ratingbar 设置
		ratingbar.setRating(1.0f);  /*评价*/
		// database

		Context phonenumber = PersonalCenterActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");

		Log.e(logtag+"电话号码", UserPhoneNumber);

		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();

		// database end!!!


		iwantcar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent iwantcar = new Intent();
				iwantcar.setClass(PersonalCenterActivity.this,
						CarsharingTypeActivity.class);
				startActivity(iwantcar);
			}
		});

		imageedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent imageedit = new Intent();
				imageedit.setClass(PersonalCenterActivity.this,
						PeronalinfoModifyActivity.class);
				startActivity(imageedit);
			}
		});

		historymore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (loadok == true) {
					Intent historymore = new Intent();
					historymore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);

					historymore.putExtra("intent", 1);
					startActivity(historymore);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_dataRead),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		receivingmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (loadok == true) {
					Intent receivingmore = new Intent();
					receivingmore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);
					receivingmore.putExtra("intent", 2);
					startActivity(receivingmore);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_dataRead),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		addressmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				// if (loadok == true) {
				Intent addressmore = new Intent();
				addressmore.setClass(PersonalCenterActivity.this,
						PersonCenterDetaillistActivity.class);
				addressmore.putExtra("intent", 3);
				startActivity(addressmore);
				// } else {
				// Toast.makeText(getApplicationContext(),
				// getString(R.string.warningInfo_dataRead),
				// Toast.LENGTH_SHORT).show();
				// }
			}
		});

	}

	@Override
	public void onResume() {

		super.onResume();

		SharedPreferences filename = context.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "文件名");
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String newfullname = sharedPref.getString("refreshname", "姓名");

		String newage = sharedPref.getString("refreshage", "年龄");

		String newdescription = sharedPref.getString("refreshdescription",
				"车辆描述");

		String newcarnum = sharedPref.getString("refreshnum", "车牌号");

		String newsex = sharedPref.getString("refreshsex", "性别");
		Log.e("carnum", newcarnum);
		name.setText(newfullname);
		age.setText(newage);
		sex.setText(newsex);
		description.setText(newdescription);
		carnum.setText(newcarnum);

		placeLikedListFlush();


		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				SharedPreferences sharedPref = getApplicationContext()
						.getSharedPreferences(UserPhoneNumber,
								Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.PreferenceUserPassword),
						"0");
				editor.commit();
				// progressbar开始
				pd = new MyProgressDialog(context);
				pd.setMessage("正在注销");
				pd.show();
				// progressbar结束
				Intent quit = new Intent();
				quit.setClass(PersonalCenterActivity.this, LoginActivity.class);
				startActivity(quit);
			}
		});
		loadok = false;
		mylist1.clear();
		mylist2.clear();
		firsthistory.setText(R.string.first_history);
		bfirsthistory = false;// 初始化
		firstdeal.setText(R.string.first_receiving);
		bfirstdeal = false;
		firstfavorite.setText(R.string.first_address);
		bfirstfavorite = false;

		if (dbresult.getCount() != 0) {
			firstfavorite.setText(mylist3.get(0).get("text"));
			bfirstfavorite = true;
		}

		// 向服务器发起查询上下班拼车订单请求start!
		selectrequest(UserPhoneNumber);
		// 向服务器发起查询上下班拼车订单请求end!

		// 查询订单结果信息
		sharingresult(UserPhoneNumber);

	}

	private void sharingresult(final String phonenum) {
		
		String sharingresult_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarTake)
				+ getString(R.string.uri_selectcartake_action);

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				sharingresult_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("sharingresult", response);
						try {
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("Title", jasitem.getString("dealTime"));

								if (jasitem.getString("sharingType").compareTo(
										"commute") == 0) {
									map.put("text", "已匹配订单：上下班拼车");
								} else if (jasitem.getString("sharingType")
										.compareTo("shortway") == 0) {
									map.put("text", "已匹配订单：短途拼车");
								}

								map.put("requst", jasitem.getString("dealId")); // 隐藏的

								String deal_readstatus = null;
								if (i == 0) {
									deal_readstatus = "receive";
									map.put("deal_readstatus", "receive");// 隐藏的
								} else if (i == 3) {
									deal_readstatus = "reject";
									map.put("deal_readstatus", "reject");// 隐藏的
								} else if (i == 2) {
									deal_readstatus = "assessOK";
									map.put("deal_readstatus", "assessOK");// 隐藏的
								} else if (i == 1) {
									deal_readstatus = "unread";
									map.put("deal_readstatus", "unread");// 隐藏的
								}

								Log.e(logtag+"deal_readstat",deal_readstatus);
								if (deal_readstatus.compareTo("unread") == 0) {// 未读消息
									map.put("deal_readstatusIcon",
											R.drawable.ic_dealunread);
								} else if (deal_readstatus.compareTo("receive") == 0) {// 已接收订单（等价于“未评价”）
									map.put("deal_readstatusIcon",
											R.drawable.ic_noneassess);
								} else if (deal_readstatus.compareTo("reject") == 0) {// 已拒绝订单
									map.put("deal_readstatusIcon",
											R.drawable.ic_action_dealreject);
								} else if (deal_readstatus
										.compareTo("assessOK") == 0) {// 订单完成（包括“评价完毕”）
									map.put("deal_readstatusIcon",
											R.drawable.ic_dealread);
								}

								mylist2.add(map);

							}
							if (bfirstdeal == false && jasA.length() > 0) {
								if (jasitem.getString("sharingType").compareTo(
										"commute") == 0) {
									firstdeal.setText(jasitem
											.getString("dealTime")
											+ "  "
											+ "上下班拼车订单");
									bfirstdeal = true;
								} else if (jasitem.getString("sharingType")
										.compareTo("shortway") == 0) {
									firstdeal.setText(jasitem
											.getString("dealTime")
											+ "  "
											+ "短途拼车订单");
									bfirstdeal = true;
								} else {
									firstdeal.setText(R.string.first_receiving);
								}
							}
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("sharingresult", error.getMessage(), error);
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

		queue.add(stringRequest);

	}

	private void longway_selectrequest(final String phonenum) {
		
		String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_LongwayPublish)
				+ getString(R.string.uri_selectpublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w(logtag+"longway_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title", jasitem.getString("startDate"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " 至 "
												+ "  "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("publishTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									firsthistory.setText(startplace[0] + " 至  "
											+ endplace[0]);
									bfirsthistory = true;
								}
							}
							loadok = true;
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(logtag+"longway_result",error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "网络连接失败",
								Toast.LENGTH_LONG);
						errorinfo.show();
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

	private void shortway_selectrequest(final String phonenum) {
		
		String shortway_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_ShortwayRequest)
				+ getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				shortway_selectrequest_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w(logtag+"shortway_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title", jasitem.getString("startDate"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " 至  "
												+ "  "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("requestTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									// Log.e("startplace[0]",startplace[0]);
									firsthistory.setText(
									// jasitem
									// .getString("requestTime")
									// 首页不显示时间
											startplace[0] + " 至  "
													+ endplace[0]);
									bfirsthistory = true;
								}
							}
							longway_selectrequest(phonenum);
							loadok = true;
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(logtag+"shortway_result",error.getMessage(), error);
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

		queue.add(stringRequest);

	}

	private void selectrequest(final String phonenum) {
		
		String commute_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CommuteRequest)
				+ getString(R.string.uri_selectrequest_action);
		// + "phonenum=" + phonenum;
		// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				commute_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w(logtag+"commute_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title",
										jasitem.getString("requestTime")
												+ " 每周"
												+ jasitem
														.getString("weekRepeat"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " 至 "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("requestTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									// Log.e("startplace[0]",startplace[0]);
									firsthistory.setText(
									// jasitem
									// .getString("requestTime")
									// 首页不显示时间
											startplace[0] + " 至  "
													+ endplace[0]);
									bfirsthistory = true;
								}
							}
							shortway_selectrequest(phonenum);
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(logtag+"commute_result",error.getMessage(), error);
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

		queue.add(stringRequest);

	}

	public void placeLikedListFlush() {
		mylist3.clear();

		dbresult = db1.query(getString(R.string.dbtable_placeliked), null,
				null, null, null, null, null);
		Log.e("number", String.valueOf(dbresult.getCount()));
		if (dbresult.getCount() == 0) {
			return;
		}

		@SuppressWarnings("unchecked")
		HashMap<String, String>[] map = new HashMap[dbresult.getColumnCount()];

		dbresult.moveToFirst();

		int i = 0;
		while (!dbresult.isAfterLast()) {
			map[i] = new HashMap<String, String>();
			map[i].put("Title", String.valueOf(dbresult.getString(2)));
			map[i].put("text", String.valueOf(dbresult.getString(1)));

			mylist3.add(map[i]);
			Log.w("Map", mylist3.toString());
			dbresult.moveToNext();
			i++;
		}

	};

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
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast toast = Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 50);
			toast.show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			if (Build.VERSION.SDK_INT >= 16) {
				finishAffinity();
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				super.onDestroy();
				System.exit(0);
			}

		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			isExit = false;
		}

	};

}
