package com.Tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.xmu.carsharing.R;

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
		SharedPreferences sharedPref = mactivity
				.getSharedPreferences(get用户手机号从偏好文件(),
						Context.MODE_PRIVATE);
		String UserName;
		UserName = sharedPref.getString(mcontext.
				getString(R.string.PreferenceUserName), "不知道你叫什么呢");
		return UserName;
	}

	public String get用户姓名从偏好文件(String UserPhineNum){
		SharedPreferences sharedPref = mactivity
				.getSharedPreferences(UserPhineNum,Context.MODE_PRIVATE);
		String UserName;
		UserName = sharedPref.getString(mcontext.
				getString(R.string.PreferenceUserName), "不知道你叫什么呢");
		return UserName;
	}

}
