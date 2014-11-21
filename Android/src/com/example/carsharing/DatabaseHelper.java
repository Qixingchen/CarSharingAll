/*
 * �������ݿ�
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
		// TODO �������ݿ�󣬶����ݿ�Ĳ���

		db.execSQL("CREATE TABLE placeLiked (_id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");
		db.execSQL("CREATE TABLE placeHistroy (_id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceUserName TEXT, PlaceMapName TEXT ,longitude  REAL, latitude REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �������ݿ�汾�Ĳ���
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// TODO ÿ�γɹ������ݿ�����ȱ�ִ��
	}
}
