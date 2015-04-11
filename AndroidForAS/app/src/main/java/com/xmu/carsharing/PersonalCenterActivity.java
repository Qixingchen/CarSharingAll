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

import com.Tool.AppStat;
import com.Tool.DataBaseAct;
import com.Tool.DatabaseHelper;
import com.Tool.Drawer;
import com.Tool.OrderReleasing;
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


public class PersonalCenterActivity extends Activity implements OrderReleasing.GetordersCallBack {

	TextView firsthistory;
	TextView firstdeal;
	TextView firstfavorite;


/*    public static ArrayList<HashMap<String, String>> mylist1 = new
			ArrayList<HashMap<String, String>>();*/
    public static ArrayList<HashMap<String, Object>> mylist2 = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();

    OrderReleasing histotical_orders; /*查询发布过的订单（已封装在OrderRealeasing.java中）*/

	public static RequestQueue queue;

	// actionbar!!
	Drawer drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	// actionbarend!!

	boolean isExit;
	boolean bfirstdeal = false, bfirstfavorite = false;
	boolean writetodb = false;
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
	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor dbresult;
	DataBaseAct dbact;

	// database end!!

	private String logtag = "个人中心";
	private String first_item;

	Button quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);

        histotical_orders = new OrderReleasing(this);
		dbact = new DataBaseAct(this,UserPhoneNumber); //数据库相关动作

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

		// 向服务器发起查询短途、上下班、长途拼车订单请求start!
		histotical_orders.orders(UserPhoneNumber, PersonalCenterActivity.this);
        /*将历史订单从服务器载入数据库，即刷新数据库。一次登录只做一次*/
		// 向服务器发起查询短途、上下班、长途拼车订单请求end!

	//	Listeners();

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

			//	Log.e("write_ok?",String.valueOf(writetodb));
				if (writetodb == true) {
					Intent historymore = new Intent();
					historymore.putExtra("intent", AppStat.个人中心_详情界面跳转代号.发布的消息);
					historymore.setClass(PersonalCenterActivity.this,PersonCenterDetaillistActivity.class);
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
				
			//	if (writetodb == true) {
					Intent receivingmore = new Intent();
					receivingmore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);
					receivingmore.putExtra("intent", AppStat.个人中心_详情界面跳转代号.收到的匹配);
					startActivity(receivingmore);
			//	} else {
			//		Toast.makeText(getApplicationContext(),
			//				getString(R.string.warningInfo_dataRead),
			//				Toast.LENGTH_SHORT).show();
			//	}
			}
		});

		addressmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent addressmore = new Intent();
				addressmore.setClass(PersonalCenterActivity.this,
						PersonCenterDetaillistActivity.class);
				addressmore.putExtra("intent", AppStat.个人中心_详情界面跳转代号.收藏的地点);
				startActivity(addressmore);
			}
		});

	}


	@Override
	public void onResume() {

		super.onResume();

		//todo 锁定列表信息，或者将完成判断置否
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


		firsthistory.setText(R.string.first_history);
		/*-----从本地数据库中读取第一条，shortway>commute>longway------*/
		//todo 数据载入本地数据库完成是否如何判断
//		while(writetodb != true) {}  //一直等到读取完
		first_item = dbact.read_FirstOrder();
		firsthistory.setText(first_item);


		mylist2.clear();
		firstdeal.setText(R.string.first_receiving);
		bfirstdeal = false;

		firstfavorite.setText(R.string.first_address);
		bfirstfavorite = false;
		if (dbresult.getCount() != 0) {
			firstfavorite.setText(mylist3.get(0).get("text"));
			bfirstfavorite = true;
		}



		// 查询订单结果信息
		sharingresult(UserPhoneNumber);

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

	//我发布过的订单回调接口
	public void getordersCallBack(boolean WriteToDb_ok){
		writetodb = WriteToDb_ok;
		Log.e("write_ok?",String.valueOf(writetodb));

	}

    //todo 检查是否必要
	public void getordersCallBack(float longitude_latitude[],String place_name[],
	                              String date_time[],String carsharing_type,
	                              String dealstatus,String userrole,String weekrepeat,
	                              String tst,String rest_seats){}


}
