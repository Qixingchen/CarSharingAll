package com.Tool;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by 雨蓝 on 2015/2/22.
 */
public class ToolWithActivityIn {
	//	检查gms是否存在
	//	false代表不存在
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Activity mactivity;

	public ToolWithActivityIn(Activity mactivity) {
		this.mactivity = mactivity;
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

}
