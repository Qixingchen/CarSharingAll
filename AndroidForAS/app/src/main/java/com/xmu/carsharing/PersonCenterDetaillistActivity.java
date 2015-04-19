/*
 * “更多”
 * 为个人中心三个“点击查看”共用
 * 1.intentcall=1  监听每一条item，点击该条item时按序访问服务器的三种类型订单，根据请求时间获取具体订单信息，传值到订单详情界面
 * 2.intentcall=2
 * 3.intentcall=3 调用适配器显示在本地已存储的收藏的地址
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.Tool.AppStat;
import com.Tool.DataBaseAct;
import com.Tool.DatabaseHelper;
import com.Tool.OrderReleasing;
import com.Tool.ToolWithActivityIn;

public class PersonCenterDetaillistActivity extends Activity implements OrderReleasing.GetordersCallBack {

	DatabaseHelper db;
	SQLiteDatabase db1;
	DataBaseAct dbact;
	ListView list1;
	ImageButton deletebtn;
	// private Vector<String> mdeal_readstatus = new Vector<>();
	private ImageView statusImage;
	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;
	OrderReleasing histotical_orders; /*查询发布过的订单（已封装在OrderRealeasing.java中）*/

	private String requesttime;
	private int intentcall;
	boolean writetodb = false,empty = true;//empty:标志数据库是否为空，空：true

	private String date_时间日期组合;

	// 绑定XML中的ListView，作为Item的容器
	private ListView list;
	private Context mcontext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_center_detaillist);

		getPhone = new ToolWithActivityIn(this);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();
		Log.e("UserPhoneNumber",UserPhoneNumber);

		histotical_orders = new OrderReleasing(this);
		dbact = new DataBaseAct(this,UserPhoneNumber);

		View itemView = View.inflate(PersonCenterDetaillistActivity.this,
				R.layout.dealstatus_listitem, null);
		statusImage = (ImageView) itemView.findViewById(R.id.list_dealstatus);

		db = new DatabaseHelper(PersonCenterDetaillistActivity.this,
				UserPhoneNumber, null, 1);

		deletebtn = (ImageButton) findViewById(R.id.mymessage_delete);
		list1 = (ListView) findViewById(R.id.WacthAllMessSent);

		mcontext = getApplicationContext();

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

			Log.e("status", "im in 发布的消息");

			final DeleteAdapter sAdapter_messSent = new DeleteAdapter(this,
					PersonalCenterActivity.mylist1,
					AppStat.个人中心_详情界面跳转代号.发布的消息); // 数据来源

			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// Item监听跳转start!
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
				                        int position, long arg3) {
					list.setClickable(false);
					requesttime = PersonalCenterActivity.mylist1.get(position).get
							("requst");
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

			DeleteAdapter sAdapter_messSent = new DeleteAdapter(this,
					PersonalCenterActivity.mylist3, 3); // 数据来源
			// 添加并且显示
			list.setAdapter(sAdapter_messSent);

			// 生成适配器，数组===》ListItem

		}
	}

	private void bundle向详情界面传值(Cursor dbresult)
	{

		Bundle bundle = new Bundle();
		Intent intent = new Intent(PersonCenterDetaillistActivity.this,
				ArrangementDetailActivity.class);

		if(dbresult == null) return;
		dbresult.moveToFirst();

		String carsharing_type = dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Carsharing_type)));
		//todo 有错误 全部跳转长途了
		bundle.putString("carsharing_type", carsharing_type);
		bundle.putString("startplace", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_StartplaceName))));
		bundle.putString("endplace", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_EndplaceName))));
		bundle.putString("startdate", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Startdate))));
		bundle.putString("restseats", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Restseats))));
		bundle.putString("userrole", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Userrole))));

		if (carsharing_type.compareTo("longway") != 0) {

			bundle.putString("requesttime", requesttime);
			bundle.putFloat("startplaceX", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_StartplaceX))));
			bundle.putFloat("startplaceY", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_StartplaceY))));
			bundle.putFloat("endplaceX", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_EndplaceX))));
			bundle.putFloat("endplaceY", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_EndplaceY))));
			bundle.putString("starttime", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Starttime))));
			bundle.putString("endtime", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Endtime))));
			bundle.putString("dealstatus", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Dealstatus))));


			if(carsharing_type.compareTo("shortway") == 0){

				date_时间日期组合 =  dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Startdate)))
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Starttime))); //出发时间
				bundle.putString("date_时间日期组合",date_时间日期组合);

			}
			else if (carsharing_type.compareTo("commute") == 0) {

				bundle.putString("enddate", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Enddate))));
				date_时间日期组合 = dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Startdate)))
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Starttime)))
						+ " "
						+ "每周"
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Weekrepeat)));
				bundle.putString("date_时间日期组合", date_时间日期组合 );
				bundle.putString("enddate", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Enddate))));
				bundle.putString("weekrepeat", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Weekrepeat))));

			}
		}
		else{ //longway
			date_时间日期组合 =  dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Startdate)))
					+ " "; //出发时间
			bundle.putString("date_时间日期组合",date_时间日期组合);
		}

		intent.putExtras(bundle);
		startActivity(intent);
	}

	//回调函数.来自OrderRelease类
	public void getordersCallBack(boolean WriteToDb_ok,boolean empty){
		this.writetodb = WriteToDb_ok;
		this.empty = empty;
		Log.e("write ok?",String.valueOf(writetodb));
		Log.e("empty ?",String.valueOf(empty));
	}

}
