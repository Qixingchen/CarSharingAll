/*
 * 本地数据库
 */

package com.Tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public DatabaseHelper(Context context, String name, CursorFactory cursorFactory,
	                      int version) {
		super(context, name, cursorFactory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库后，对数据库的操作

		db.execSQL("CREATE TABLE placeLiked (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");
		db.execSQL("CREATE TABLE placeHistroy (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");

		//历史订单数据表：上下班拼车，短途拼车，长途拼车
		db.execSQL("CREATE TABLE shortwayOrders(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"requesttime TEXT, StartplaceX REAL, StartplaceY REAL, " +
				"EndplaceX REAL,EndplaceY REAL, StartplaceName TEXT, EndplaceName TEXT, " +
				"Display_firstItem TEXT, Startdate TEXT, Starttime TEXT, " +
				"Endtime TEXT, Dealstatus INTEGER, Userrole TEXT, Restseats INEGER, " +
				"Carsharing_type TEXT);");

		db.execSQL("CREATE TABLE commuteOrders(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"requesttime TEXT, StartplaceX REAL, StartplaceY REAL, " +
				"EndplaceX REAL,EndplaceY REAL,StartplaceName TEXT, EndplaceName TEXT, " +
				"Display_firstItem TEXT, Startdate TEXT, Enddate TEXT, Starttime TEXT," +
				"Endtime TEXT, Weekrepeat TEXT, Dealstatus INTEGER, Userrole TEXT, Restseats INEGER, " +
				"Carsharing_type TEXT);");

		db.execSQL("CREATE TABLE longwayOrders(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"requesttime TEXT,  StartplaceName TEXT, EndplaceName TEXT, " +
				"Display_firstItem TEXT, Startdate TEXT, " +
				"Userrole TEXT, Restseats INTEGER, Carsharing_type TEXT);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 更改数据库版本的操作
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		//每次成功打开数据库后首先被执行
	}
}
