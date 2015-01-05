/*
 * 本地数据库
 */

package com.example.carsharing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	DatabaseHelper(Context context, String name, CursorFactory cursorFactory,
			int version) {
		super(context, name, cursorFactory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作

		db.execSQL("CREATE TABLE placeLiked (_id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");
		db.execSQL("CREATE TABLE placeHistroy (_id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// TODO 每次成功打开数据库后首先被执行
	}
}
