package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmu.carsharing.R;

/**
 * Created by 雨蓝 on 2015/3/23.
 * 这是数据库操作的汇总处
 * 要求传入activity和手机号
 * 务必需要调用Destory
 */
public class DataBaseAct {

	//class内部用
	private Application mapplication;
	private Context mcontext;
	private Activity mactivity;
	private String logtag = "数据库操作";

	//数据库
	private DatabaseHelper db;
	private SQLiteDatabase db1;

	//要求传入的值
	private String muserPhoneNumber;

	//传入用户名
	public DataBaseAct(Activity mact, String UserPhoneNumber) {
		mactivity = mact;
		mcontext = mact.getApplicationContext();
		mapplication = mact.getApplication();
		muserPhoneNumber = UserPhoneNumber;
		initDataBase();
	}

	//已经调用了
	private void initDataBase() {
		db = new DatabaseHelper(mcontext, muserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();
	}

		//判断集合

	public boolean is历史地点记录中有该记录(String mapname) {
		Cursor dbresult = db1.query(
				mcontext.getString(R.string.dbtable_placehistory), null,
				mcontext.getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[]{mapname}, null, null, null);
		if (0 == dbresult.getCount()) {
			dbresult.close();
			return false;
		} else {
			dbresult.close();
			return true;
		}

	}

	public boolean is偏爱地点记录中有该记录(String mapname) {
		Cursor dbresult = db1.query(
				mcontext.getString(R.string.dbtable_placeliked), null,
				mcontext.getString(R.string.dbstring_PlaceMapName) + "=?",
				new String[]{mapname}, null, null, null);
		if (0 == dbresult.getCount()) {
			dbresult.close();
			return false;
		} else {
			dbresult.close();
			return true;
		}

	}

		//罗列集合

	public Cursor showAll偏好地点() {
		Cursor result = db1.query(mcontext.getString(R.string.dbtable_placeliked), null,
				null, null, null, null, null);
		Log.w(logtag + "喜欢数量", String.valueOf(result.getCount()));
		return result;
	}

	public Cursor showAll历史地点() {
		Cursor result = db1.query(mcontext.getString(R.string.dbtable_placehistory), null,
				null, null, null, null, null);
		Log.w(logtag + "历史地点数量", String.valueOf(result.getCount()));
		return result;
	}


		//增加集合

	public void add偏好地点(String mapname, String UserMapname, double longitude,
	                    double latitude){
		ContentValues content = new ContentValues();
		content.put(mcontext.getString(R.string.dbstring_PlaceUserName),
				UserMapname);
		content.put(mcontext.getString(R.string.dbstring_PlaceMapName),
				mapname);
		content.put(mcontext.getString(R.string.dbstring_longitude),
				longitude);
		content.put(mcontext.getString(R.string.dbstring_latitude),
				latitude);
		db1.insert(mcontext.getString(R.string.dbtable_placeliked),
				null, content);
	}

	public void add历史地点(String mapname, String UserMapname, double longitude,
	                    double latitude) {
		ContentValues content = new ContentValues();
		content.put(mcontext.getString(R.string.dbstring_PlaceUserName),
				UserMapname);
		content.put(mcontext.getString(R.string.dbstring_PlaceMapName),
				mapname);
		content.put(mcontext.getString(R.string.dbstring_longitude),
				longitude);
		content.put(mcontext.getString(R.string.dbstring_latitude),
				latitude);
		db1.insert(mcontext.getString(R.string.dbtable_placehistory),
				null, content);
		Log.w("历史数据库", "添加" + UserMapname + "  map:"
				+ mapname + String.valueOf(longitude)
				+ "&" + String.valueOf(latitude));
	}

	public void if位于历史或偏爱记录并添加记录(String mapname, String UserMapname, double longitude,
	                             double latitude) {
		if (is偏爱地点记录中有该记录(mapname) == true) {
			return;
		}
		if (is历史地点记录中有该记录(mapname) == true) {
			return;
		}

		add历史地点(mapname, UserMapname, longitude, latitude);

	}

		//删除集合

	public void delete偏好地点(String[] selelectionArgs){
		// Define 'where' part of query.
		String selection = mcontext.getString(R.string.dbstring_PlaceMapName)
				+ " LIKE ?";
		// Specify arguments in placeholder order.

		// Issue SQL statement.
		db1.delete(mcontext.getString(R.string.dbtable_placeliked),
				selection, selelectionArgs);

	}

	public void delete历史地点(String[] selelectionArgs){
		// Define 'where' part of query.
		String selection = mcontext.getString(R.string.dbstring_PlaceMapName)
				+ " LIKE ?";
		// Specify arguments in placeholder order.

		// Issue SQL statement.
		db1.delete(mcontext.getString(R.string.dbtable_placehistory),
				selection, selelectionArgs);

	}



	public void Destory() {
		db1.close();
	}

}