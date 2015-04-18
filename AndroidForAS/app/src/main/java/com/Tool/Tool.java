package com.Tool;

import android.os.Environment;

/**
 * @author XuZhiwei (xuzw13@gmail.com) Create at 2012-8-17 上午10:14:40
 */
public class Tool {

	/**
	 * 检查是否存在SDCard
	 *
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	//订单界面 获取WeekRepeat字符串
	public static String getWeekRepeat(boolean bmon,boolean btue,boolean bwed,boolean bthu,
	                            boolean bfri,boolean bsat,boolean bsun){
		String weekrepeat = "";
						if (bmon)
									weekrepeat += "1";
						if (btue)
									weekrepeat += "2";
						if (bwed)
									weekrepeat += "3";
						if (bthu)
									weekrepeat += "4";
						if (bfri)
									weekrepeat += "5";
						if (bsat)
									weekrepeat += "6";
						if (bsun)
									weekrepeat += "7";
		return weekrepeat;
	}
}