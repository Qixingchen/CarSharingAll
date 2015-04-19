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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
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
import com.Tool.MaterialDrawer;
import com.Tool.Mylist1;
import com.Tool.OrderReleasing;
import com.Tool.ToolWithActivityIn;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PersonalCenterActivity extends ActionBarActivity implements OrderReleasing.GetordersCallBack
,OrderReleasing.GetPairedOrderCallBack {

	private TextView firsthistory;
	private TextView firstdeal;
	private TextView firstfavorite;


	public static ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, Object>> mylist2 = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();

	private OrderReleasing histotical_orders; /*查询发布过的订单（已封装在OrderRealeasing.java中）*/
	private Mylist1 getMylist1;

	public static RequestQueue queue;

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

	boolean isExit;
	boolean bfirstdeal = false, bfirstfavorite = false;
	boolean writetodb = false, empty = true;//empty:标志数据表是否为空，空：true
	private Context context = PersonalCenterActivity.this;
	static ImageView image;

	private TextView name, age, description, carnum, sex;

	private RatingBar ratingbar;

	private String logtag = "个人中心";

	private Button quit;


	// progressbar
	private static MyProgressDialog pd;  /*建议对progressbar的实现进行封装*/
	// progressbar end

	// 用户手机号
	private String UserPhoneNumber;

	// database
//	private DatabaseHelper db;
//	private SQLiteDatabase db1;
	private Cursor dbresult;
	private DataBaseAct dataBaseAct;

	// database end!!

	//tool类
	ToolWithActivityIn toolWithActivityIn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);

		histotical_orders = new OrderReleasing(this);


		//actionbar
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		new MaterialDrawer(this, toolbar);
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

		toolWithActivityIn = new ToolWithActivityIn(this);
		UserPhoneNumber = toolWithActivityIn.get用户手机号从偏好文件();
		Log.e("电话号码", UserPhoneNumber);

		// database
		dataBaseAct = new DataBaseAct(this, UserPhoneNumber); //数据库相关动作
		// database end!!!


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
					historymore.setClass(PersonalCenterActivity.this, PersonCenterDetaillistActivity.class);
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

				if (writetodb == true) {
					Intent receivingmore = new Intent();
					receivingmore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);
					receivingmore.putExtra("intent", AppStat.个人中心_详情界面跳转代号.收到的匹配);
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
		UserPhoneNumber = toolWithActivityIn.get用户手机号从偏好文件();
		SparseArray<String> userinfo = toolWithActivityIn.get用户详细信息从偏好文件(UserPhoneNumber);

		name.setText(userinfo.get(AppStat.prefer用户详细信息对应编号.姓名));
		age.setText(userinfo.get(AppStat.prefer用户详细信息对应编号.年龄));
		sex.setText(userinfo.get(AppStat.prefer用户详细信息对应编号.性别));
		description.setText(userinfo.get(AppStat.prefer用户详细信息对应编号.车辆描述));
		carnum.setText(userinfo.get(AppStat.prefer用户详细信息对应编号.车牌号));

		placeLikedListFlush();

		//todo 使用更好的方式刷新列表
		mylist2.clear();
		firstdeal.setText(R.string.first_receiving);
		bfirstdeal = false;

		firstfavorite.setText(R.string.first_address);
		bfirstfavorite = false;
		if (dbresult.getCount() != 0) {
			firstfavorite.setText(mylist3.get(0).get("text"));
			bfirstfavorite = true;
		}

		// 向服务器发起查询短途、上下班、长途拼车历史订单请求start!
		histotical_orders.orders(UserPhoneNumber, PersonalCenterActivity.this);
		/*将历史订单从服务器载入数据库，即刷新数据库。一次登录只做一次*/
		// 向服务器发起查询短途、上下班、长途拼车订单请求end!


		/*-----从本地数据库中读取第一条，shortway>commute>longway------*/
		mylist1.clear();
		firsthistory.setText(R.string.first_history);
		//产生mylist1，供personalcenterdetail界面使用
		getMylist1 = new Mylist1(this);
		mylist1 = getMylist1.mylist1产生();
		if (writetodb == true) {
			if (empty == false)
				firsthistory.setText(mylist1.get(0).get("text"));
		}

		// 查询订单结果信息
		sharingresult(UserPhoneNumber);

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				toolWithActivityIn.set快速登陆密码为空(UserPhoneNumber);
				Toast.makeText(getApplicationContext(), "正在注销", Toast.LENGTH_SHORT).show();
				Intent quit = new Intent();
				quit.setClass(PersonalCenterActivity.this, LoginActivity.class);
				startActivity(quit);
			}
		});

	}

	//todo 迁移
	private void sharingresult(final String phonenum) {


	}

	public void placeLikedListFlush() {
		mylist3.clear();

		dbresult = dataBaseAct.showAll偏好地点();

		@SuppressWarnings("unchecked")
		HashMap<String, String>[] map = new HashMap[dbresult.getColumnCount()];

		dbresult.moveToFirst();

		int i = 0;
		while (!dbresult.isAfterLast()) {
			map[i] = new HashMap<>();
			map[i].put("Title", String.valueOf(dbresult.getString(2)));
			map[i].put("text", String.valueOf(dbresult.getString(1)));

			mylist3.add(map[i]);
			Log.w("Map", mylist3.toString());
			dbresult.moveToNext();
			i++;
		}

	}

	;

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

	//我发布过的订单回调接口
	public void getordersCallBack(boolean WriteToDb_ok, boolean empty) {
		this.writetodb = WriteToDb_ok;
		this.empty = empty;
		Log.e("write_ok?", String.valueOf(writetodb));
		Log.e("empty?", String.valueOf(empty));
	}

	@Override
	public void getPairedOrderCallBack(JSONObject jasitem, int jasA_length) {
		if (bfirstdeal == false && jasA_length > 0) {
			try {
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
