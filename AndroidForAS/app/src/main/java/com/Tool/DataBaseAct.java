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

	public void add_OdersToDb(String carsharing_type, String requesttime,
	                          float longitude_latitude[],String place_name[],
	                         String date_time[],String dealstatus,String userrole,
	                         String weekrepeat,String  display_firstitem,
	                         Integer rest_seats){

		Log.e("write to db","writing");

		ContentValues content = new ContentValues();
		/*3个表的公共字段*/
		content.put(mcontext.getString(R.string.dbstring_requesttime),requesttime);
		content.put(mcontext.getString(R.string.dbstring_StartplaceName),place_name[0]);
		content.put(mcontext.getString(R.string.dbstring_EndplaceName),place_name[1]);
		content.put(mcontext.getString(R.string.dbstring_Display_firstItem),display_firstitem);
		content.put(mcontext.getString(R.string.dbstring_Startdate),date_time[0]);
		content.put(mcontext.getString(R.string.dbstring_Dealstatus),dealstatus);
		content.put(mcontext.getString(R.string.dbstring_Userrole),userrole);
		content.put(mcontext.getString(R.string.dbstring_Restseats),rest_seats);
		content.put(mcontext.getString(R.string.dbstring_Carsharing_type),carsharing_type);

		if(carsharing_type.compareTo("longway") != 0) {

			/*2个表的公共字段*/
			content.put(mcontext.getString(R.string.dbstring_StartplaceX),
					longitude_latitude[0]);
			content.put(mcontext.getString(R.string.dbstring_StartplaceY),
					longitude_latitude[1]);
			content.put(mcontext.getString(R.string.dbstring_EndplaceX),
					longitude_latitude[2]);
			content.put(mcontext.getString(R.string.dbstring_EndplaceY),
					longitude_latitude[3]);
			content.put(mcontext.getString(R.string.dbstring_Starttime),date_time[2]);
			content.put(mcontext.getString(R.string.dbstring_Endtime),date_time[3]);

			if (carsharing_type.compareTo("commute") == 0) {

				content.put(mcontext.getString(R.string.dbstring_Enddate),date_time[1]);
				content.put(mcontext.getString(R.string.dbstring_Weekrepeat),
						weekrepeat);
				db1.insert(mcontext.getString(R.string.dbtable_commuteOrders),null, content);
				Log.e("write to db","commute write ok!");
			} else if (carsharing_type.compareTo("shortway") == 0) {
				db1.insert(mcontext.getString(R.string.dbtable_shortwayOrders),null, content);
				Log.e("write to db","shortway write ok!");
			}
		}
		else { //longway
			db1.insert(mcontext.getString(R.string.dbtable_longwayOrders),null,content);
			Log.e("write to db","longway write ok!");
		}

	}

		//shortway>commute>longway  查询第一条历史记录
	public String read_FirstOrder() {
		Cursor dbresult = db1.query(
				mcontext.getString(R.string.dbtable_shortwayOrders),
				new String[] {mcontext.getString(R.string.dbstring_Display_firstItem)},
				mcontext.getString(R.string.dbstring_id) + "=?",
				new String[]{"1"}, null, null, null);

		if (0 == dbresult.getCount()) {
			dbresult = db1.query(mcontext.getString(R.string.dbtable_commuteOrders),
			new String[] {mcontext.getString(R.string.dbstring_Display_firstItem)},
					mcontext.getString(R.string.dbstring_id) + "=?",
					new String[]{"1"}, null, null, null);

			if(0 == dbresult.getCount()){
				dbresult = db1.query(mcontext.getString(R.string.dbtable_longwayOrders),
				new String[] {mcontext.getString(R.string.dbstring_Display_firstItem)},
						mcontext.getString(R.string.dbstring_id) + "=?",
						new String[]{"1"}, null, null, null);
				if(0 == dbresult.getCount())
					return "查询不到您发布过的订单噢..";
			}
		}
		return dbresult.getString(0);
	}

		//获取被点击的item的详细信息
	public Cursor read某条历史订单(String requesttime){  //用requesttime来确定被点击的是哪一条
		Cursor dbresult = db1.query(
				mcontext.getString(R.string.dbtable_shortwayOrders),null,
				mcontext.getString(R.string.dbstring_id) + "=?",
				new String[]{requesttime}, null, null, null);

		if (0 == dbresult.getCount()) {
			dbresult = db1.query(mcontext.getString(R.string.dbtable_commuteOrders),null,
					mcontext.getString(R.string.dbstring_id) + "=?",
					new String[]{requesttime}, null, null, null);
			if(0 == dbresult.getCount()){
				dbresult = db1.query(mcontext.getString(R.string.dbtable_longwayOrders),null,
						mcontext.getString(R.string.dbstring_id) + "=?",
						new String[]{requesttime}, null, null, null);
			}
		}
	//	dbresult.moveToFirst();
		return dbresult;
	}

		//All orders
	public Cursor[] read所有订单(){

		Cursor[] cursors = new Cursor[3];
		cursors[0] = db1.query(
				mcontext.getString(R.string.dbtable_shortwayOrders),
				null, null, null, null, null, null);

		cursors[1] = db1.query(
				mcontext.getString(R.string.dbtable_commuteOrders),
				null, null, null, null, null, null);

		cursors[2] = db1.query(
				mcontext.getString(R.string.dbtable_longwayOrders),
				null, null, null, null, null, null);

		return cursors;  //Cursor数组

	}

	public void Destory() {
		db1.close();
	}

}