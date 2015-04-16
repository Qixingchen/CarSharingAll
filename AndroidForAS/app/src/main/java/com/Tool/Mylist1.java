package com.Tool;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jo on 2015/4/16.
 */
public class Mylist1 {

	private Activity activity;

	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;
	DataBaseAct dbact;

	public Mylist1(Activity act) {
		this.activity = act;
		getPhone = new ToolWithActivityIn(activity);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();
		dbact = new DataBaseAct(activity, UserPhoneNumber);
	}

	public ArrayList mylist1产生() {

		ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
		Cursor[] cursors = new Cursor[3];
		cursors = dbact.read所有订单();
		String text;
		String date_时间日期组合;
		Cursor dbresult1 = cursors[0]; //shortway
		Cursor dbresult2 = cursors[1]; //commute
		Cursor dbresult3 = cursors[2]; //longway
		dbresult1.moveToFirst();
		Log.e("dbresult1.getCount().", String.valueOf(dbresult1.getCount()));
		dbresult2.moveToFirst();
		Log.e("dbresult2.getCount().", String.valueOf(dbresult2.getCount()));
		dbresult3.moveToFirst();
		Log.e("dbresult3.getCount().", String.valueOf(dbresult3.getCount()));
		Log.e("status", "im in mylist1产生");

		for (dbresult1.moveToFirst(); !dbresult1.isAfterLast(); dbresult1.moveToNext()) { //短途

			HashMap<String, String> map = new HashMap<String, String>();

			date_时间日期组合 = dbresult1.getString(dbresult1.getColumnIndex("Startdate"))
					+ " "
					+ dbresult1.getString(dbresult1.getColumnIndex("Starttime")); //出发时间
			map.put("Title", date_时间日期组合);
			Log.e("Title", date_时间日期组合);

			text = dbresult1.getString(dbresult1.getColumnIndex("StartplaceName"))
					+ "  "
					+ " 至 "
					+ "  "
					+ dbresult1.getString(dbresult1.getColumnIndex("EndplaceName"))
					+ "  ";
			map.put("text", text);

			map.put("requst",
					dbresult1.getString(dbresult1.getColumnIndex("requesttime"))); //隐藏部分

			mylist1.add(map);
		}
		for (dbresult2.moveToFirst(); !dbresult2.isAfterLast(); dbresult2.moveToNext()) { //上下班

			HashMap<String, String> map = new HashMap<String, String>();

			Log.e("status", "im in dbresult2");
			date_时间日期组合 = dbresult2.getString(dbresult2.getColumnIndex
					("Startdate"))
					+ " "
					+ dbresult2.getString(dbresult2.getColumnIndex
					("Starttime"))
					+ " "
					+ "每周"
					+ " "
					+ dbresult2.getString(dbresult2.getColumnIndex
					("Weekrepeat"));
			map.put("Title", date_时间日期组合);
			Log.e("Title", date_时间日期组合);
			text = dbresult2.getString(dbresult2.getColumnIndex
					("StartplaceName"))
					+ "  "
					+ " 至  "
					+ "  "
					+ dbresult2.getString(dbresult2.getColumnIndex
					("EndplaceName"));
			map.put("text", text);
			map.put("requst",
					dbresult2.getString(dbresult2.getColumnIndex("requesttime")));
			mylist1.add(map);
		}
		for (dbresult3.moveToFirst(); !dbresult3.isAfterLast(); dbresult3.moveToNext()) { //长途

			HashMap<String, String> map = new HashMap<String, String>();

			date_时间日期组合 = dbresult3.getString(dbresult3.getColumnIndex
					("Startdate"));
			map.put("Title", date_时间日期组合);
			text = dbresult3.getString(dbresult3.getColumnIndex
					("StartplaceName"))
					+ "  "
					+ " 至  "
					+ "  "
					+ dbresult3.getString(dbresult3.getColumnIndex
					("EndplaceName"))
					+ "  ";
			map.put("text", text);
			map.put("requst",
					dbresult3.getString(dbresult3.getColumnIndex("requesttime")));
			mylist1.add(map);
		}
		return mylist1;
	}
}
