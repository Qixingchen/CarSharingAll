/*
 * “更多”
 * 为个人中心三个“点击查看”共用
 * 1.intentcall=1  监听每一条item，点击该条item时按序访问服务器的三种类型订单，根据请求时间获取具体订单信息，传值到订单详情界面
 * 2.intentcall=2
 * 3.intentcall=3 调用适配器显示在本地已存储的收藏的地址
 */

package com.xmu.carsharing;

import java.util.ArrayList;
import java.util.HashMap;


import com.Tool.AppStat;
import com.Tool.DataBaseAct;
import com.Tool.OrderReleasing;

import com.Tool.DatabaseHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PersonCenterDetaillistActivity extends Activity implements OrderReleasing.GetordersCallBack {

	DatabaseHelper db;
	SQLiteDatabase db1;
	DataBaseAct dbact;
	ListView list1;
	ImageButton deletebtn;
	// private Vector<String> mdeal_readstatus = new Vector<>();
	private ImageView statusImage;
	private String UserPhoneNumber;
	OrderReleasing histotical_orders; /*查询发布过的订单（已封装在OrderRealeasing.java中）*/

	private String requesttime;
	private int intentcall;
	boolean writetodb = false;

	private String date_时间日期组合;


	// 生成动态数组，并且转载数据
	ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
//	ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
//	ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();

	// 绑定XML中的ListView，作为Item的容器
	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_center_detaillist);

		histotical_orders = new OrderReleasing(this);
		dbact = new DataBaseAct(this,UserPhoneNumber);

		View itemView = View.inflate(PersonCenterDetaillistActivity.this,
				R.layout.dealstatus_listitem, null);
		statusImage = (ImageView) itemView.findViewById(R.id.list_dealstatus);

		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);

		UserPhoneNumber = sharedPref.getString("refreshfilename", "0");
		db = new DatabaseHelper(PersonCenterDetaillistActivity.this,
				UserPhoneNumber, null, 1);

		deletebtn = (ImageButton) findViewById(R.id.mymessage_delete);
		list1 = (ListView) findViewById(R.id.WacthAllMessSent);

		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		list = (ListView) findViewById(R.id.WacthAllMessSent);

	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		intentcall = bundle.getInt("intent");
		Log.e("detail_intentcall",String.valueOf(intentcall));

		list.setClickable(true);
		if (AppStat.个人中心_详情界面跳转代号.发布的消息 == intentcall) { //intentcall = 1

			Log.e("status","im in 发布的消息");
			mylist1产生();

			final MyAdapter sAdapter_messSent = new MyAdapter(this,mylist1,
					AppStat.个人中心_详情界面跳转代号.发布的消息); // 数据来源
			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// Item监听跳转start!
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
				                        int position, long arg3) {
					list.setClickable(false);
					requesttime = mylist1.get(position).get("requst");
					Log.e("requesttime", requesttime);

					/*从数据库中查找requesttime与之对应的条目*/
					Cursor dbresult =  dbact.read某条历史订单(requesttime);
					bundle向详情界面传值(dbresult);

			//		sAdapter_messSent.notifyDataSetChanged();

				}
			});
			// Item监听跳转end!
		}

		if (AppStat.个人中心_详情界面跳转代号.收到的匹配 == intentcall) {

			SimpleAdapter sAdapter_messSent = new SimpleAdapter(this,
					PersonalCenterActivity.mylist2,
					R.layout.dealstatus_listitem, new String[]{"Title",
					"text", "requst", "deal_readstatus",
					"deal_readstatusIcon"}, new int[]{R.id.dealtime,
					R.id.dealkind, R.id.dealid, R.id.dealstatus,
					R.id.list_dealstatus});

			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// 监听item
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
				                        int position, long arg3) {

					Intent intent = new Intent(
							PersonCenterDetaillistActivity.this,
							RoutelineDisplayActivity.class);
					intent.putExtra(
							"dealid",
							(String) PersonalCenterActivity.mylist2.get(
									position).get("requst"));
					intent.putExtra(
							"deal_readstatus",
							(String) PersonalCenterActivity.mylist2.get(
									position).get("deal_readstatus"));

					startActivity(intent);

				}
			});
		}

		if (AppStat.个人中心_详情界面跳转代号.收藏的地点 == intentcall) {

			MyAdapter sAdapter_messSent = new MyAdapter(this,
					PersonalCenterActivity.mylist3, 3); // 数据来源
			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// 生成适配器，数组===》ListItem

		}
	}

	private void mylist1产生() {
		Cursor[] cursors = new Cursor[3];
		cursors = dbact.read所有订单();
		Cursor dbresult1 = cursors[0]; //shortway
		Cursor dbresult2 = cursors[1]; //commute
		Cursor dbresult3 = cursors[2]; //longway
		HashMap<String, String> map = new HashMap<String, String>();
		dbresult1.moveToFirst();
		dbresult2.moveToFirst();
		dbresult3.moveToFirst();
		Log.e("status","im in mylist1产生");
		while (!dbresult1.isAfterLast()) { //短途
			date_时间日期组合 = dbresult1.getString(R.string.dbstring_Startdate)
					+ " "
					+ dbresult1.getString(R.string.dbstring_Starttime); //出发时间
			map.put("Title", date_时间日期组合 );
			Log.e("Title", date_时间日期组合);
			map.put("text",
					dbresult1.getString(R.string.dbstring_StartplaceName)
							+ "  "
							+ " 至 "
							+ "  "
							+ dbresult1.getString(R.string.dbstring_EndplaceName)
							+ "  ");
			Log.e("text",
					dbresult1.getString(R.string.dbstring_StartplaceName)
							+ "  "
							+ " 至 "
							+ "  "
							+ dbresult1.getString(R.string.dbstring_EndplaceName)
							+ "  ");
			map.put("requst",
					dbresult1.getString(R.string.dbstring_requesttime)); //隐藏部分
			mylist1.add(map);
		}
		while(!dbresult2.isAfterLast()){ //上下班
			Log.e("status","im in dbresult2");
			date_时间日期组合 = dbresult2.getString(R.string.dbstring_Startdate)
					+ " "
					+ dbresult2.getString(R.string.dbstring_Starttime)
					+ " "
					+ "每周"
					+ " "
					+ dbresult2.getString(R.string.dbstring_Weekrepeat);
			map.put("Title", date_时间日期组合 );
			Log.e("Title", date_时间日期组合);
			map.put("text",
					dbresult2.getString(R.string.dbstring_StartplaceName)
							+ "  "
							+ " 至  "
							+ "  "
							+ dbresult2.getString(R.string.dbstring_EndplaceName)
							+ "  ");
			Log.e("text",
					dbresult2.getString(R.string.dbstring_StartplaceName)
							+ "  "
							+ " 至  "
							+ "  "
							+ dbresult2.getString(R.string.dbstring_EndplaceName)
							+ "  ");
			map.put("requst",
					dbresult1.getString(R.string.dbstring_requesttime));
			mylist1.add(map);
		}
		while(!dbresult3.isAfterLast()){ //长途
			date_时间日期组合 = dbresult3.getString(R.string.dbstring_Startdate);
			map.put("Title", date_时间日期组合 );
			map.put("text",
					dbresult3.getString(R.string.dbstring_StartplaceName)
							+ "  "
							+ " 至  "
							+ "  "
							+ dbresult3.getString(R.string.dbstring_EndplaceName)
							+ "  ");
			map.put("requst",
					dbresult1.getString(R.string.dbstring_requesttime));
			mylist1.add(map);
		}
	}

	private void bundle向详情界面传值(Cursor dbresult)
	{
		String carsharing_type = dbresult.getString(R.string.dbstring_Carsharing_type);
		Bundle bundle = new Bundle();
		Intent intent = new Intent(
				PersonCenterDetaillistActivity.this,
				ArrangementDetailActivity.class);

		bundle.putString("carsharing_type", dbresult.getString(R.string.dbstring_Carsharing_type));
		bundle.putString("startplace", dbresult.getString(R.string.dbstring_StartplaceName));
		bundle.putString("endplace", dbresult.getString(R.string.dbstring_EndplaceName));
		bundle.putString("startdate", dbresult.getString(R.string.dbstring_Startdate));
		bundle.putString("restseats", dbresult.getString(R.string.dbstring_Restseats));
		bundle.putString("dealstatus", dbresult.getString(R.string.dbstring_Dealstatus));
		bundle.putString("userrole", dbresult.getString(R.string.dbstring_Userrole));

		if (carsharing_type.compareTo("longway") != 0) {

			bundle.putString("requesttime", requesttime);
			bundle.putFloat("startplaceX", dbresult.getFloat(R.string.dbstring_StartplaceX));
			bundle.putFloat("startplaceY", dbresult.getFloat(R.string.dbstring_StartplaceY));
			bundle.putFloat("endplaceX", dbresult.getFloat(R.string.dbstring_EndplaceX));
			bundle.putFloat("endplaceY", dbresult.getFloat(R.string.dbstring_EndplaceY));
			bundle.putString("starttime", dbresult.getString(R.string.dbstring_Starttime));
			bundle.putString("endtime", dbresult.getString(R.string.dbstring_Endtime));


			if(carsharing_type.compareTo("shortway") == 0){

				date_时间日期组合 =  dbresult.getString(R.string.dbstring_Startdate)
						+ " "
						+ dbresult.getString(R.string.dbstring_Starttime); //出发时间
				bundle.putString("date_日期时间组合",date_时间日期组合);

			}
			else if (carsharing_type.compareTo("commute") == 0) {

				bundle.putString("enddate", dbresult.getString(R.string.dbstring_Enddate));
				date_时间日期组合 = dbresult.getString(R.string.dbstring_Startdate)
						+ " "
						+ dbresult.getString(R.string.dbstring_Starttime)
						+ " "
						+ "每周"
						+ " "
						+ dbresult.getString(R.string.dbstring_Weekrepeat);
				bundle.putString("Title", date_时间日期组合 );
				bundle.putString("enddate", dbresult.getString(R.string.dbstring_Enddate));
				bundle.putString("weekrepeat", dbresult.getString(R.string.dbstring_Weekrepeat));

			}
		}
		else{ //longway
			date_时间日期组合 =  dbresult.getString(R.string.dbstring_Startdate)
					+ " "
					+ dbresult.getString(R.string.dbstring_Starttime); //出发时间
			bundle.putString("date_时间日期组合",date_时间日期组合);
		}

		intent.putExtras(bundle);
		startActivity(intent);
	}

	//回调函数.来自OrderRelease类
	public void getordersCallBack(boolean WriteToDb_ok){
		writetodb = WriteToDb_ok;
		Log.e("write ok?",String.valueOf(writetodb));
	}

}
