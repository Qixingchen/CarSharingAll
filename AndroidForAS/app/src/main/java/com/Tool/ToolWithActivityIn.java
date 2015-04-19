package com.Tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.xmu.carsharing.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 雨蓝 on 2015/2/22.
 */
public class ToolWithActivityIn {
	//	检查gms是否存在
	//	false代表不存在
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Activity mactivity;
	private Context mcontext;

	public ToolWithActivityIn(Activity mactivity) {
		this.mactivity = mactivity;
		mcontext = mactivity.getApplicationContext();
	}


	public boolean hasGMS() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mactivity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, mactivity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.w("gcm状态", "This device is not supported.");
			}
			return false;
		}
		return true;
	}

	public String get用户手机号从偏好文件(){
		SharedPreferences sharedPref = mactivity
				.getSharedPreferences(mcontext.
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		String UserPhoneNumber;
		UserPhoneNumber = sharedPref.getString(mcontext.
				getString(R.string.PreferenceUserPhoneNumber), "0");
		return UserPhoneNumber;
	}

	public String get用户姓名从偏好文件(){

		return get用户姓名从偏好文件(get用户手机号从偏好文件());

	}

	public String get用户姓名从偏好文件(String UserPhoneNum){
		SharedPreferences sharedPref = mactivity
				.getSharedPreferences(UserPhoneNum,Context.MODE_PRIVATE);
		String UserName;
		UserName = sharedPref.getString(mcontext.
				getString(R.string.PreferenceUserName), "不知道你叫什么呢");
		return UserName;
	}

	public SparseArray<String> get用户详细信息从偏好文件(){
		return get用户详细信息从偏好文件(get用户手机号从偏好文件());
	}

	public SparseArray<String> get用户详细信息从偏好文件(String UserPhoneNum){

		SparseArray<String> returnans = new SparseArray<>(5);

		SharedPreferences sharedPref = mactivity.getApplicationContext().
				getSharedPreferences(UserPhoneNum, Context.MODE_PRIVATE);

		String newfullname = sharedPref.getString("refreshname", "姓名");

		String newage = sharedPref.getString("refreshage", "年龄");

		String newdescription = sharedPref.getString("refreshdescription",
				"车辆描述");

		String newcarnum = sharedPref.getString("refreshnum", "车牌号");

		String newsex = sharedPref.getString("refreshsex", "性别");

		returnans.put(AppStat.prefer用户详细信息对应编号.姓名,newfullname);
		returnans.put(AppStat.prefer用户详细信息对应编号.年龄,newage);
		returnans.put(AppStat.prefer用户详细信息对应编号.车辆描述,newdescription);
		returnans.put(AppStat.prefer用户详细信息对应编号.车牌号,newcarnum);
		returnans.put(AppStat.prefer用户详细信息对应编号.性别,newsex);


		return  returnans;
	}

	public void set快速登陆密码为空(String UserPhoneNum){
		SharedPreferences sharedPref = mactivity.getApplicationContext()
				.getSharedPreferences(UserPhoneNum,
						Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(mactivity.getString(R.string.PreferenceUserPassword),
				"0");
		editor.apply();
	}
}
