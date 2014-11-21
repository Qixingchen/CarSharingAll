package mysqlconnect;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetNowTime {

	/**
	 * @param args
	 */
	public static String getDay() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sf.format(new Date().getTime()));
		return sf.format(new Date().getTime());
	}

	public static String getTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(sf.format(new Date().getTime()));
		return sf.format(new Date().getTime());
	}
	/*
	 * public static void main(String[] args) {
	 * System.out.println(GetNowTime.getTime()); }
	 */

}
