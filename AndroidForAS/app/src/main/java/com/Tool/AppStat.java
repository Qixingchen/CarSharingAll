package com.Tool;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by 雨蓝 on 2015/2/9.
 * 这里描述了app所使用的状态常量
 */
public class AppStat {

	//smssdk量
	public static class SMSSDK {
		public static final int OK = 1;
		public static final int ERROR = 2;
	}

	//注册界面
	public static class Register {
		public static final int 发送验证短信 = 1;
		public static final int 跳转界面验证 = 2;
	}

	//抽屉跳转的类名
	public static class 抽屉跳转的类名 {
		public static final String 个人中心 = "PersonalCenterActivity";
		public static final String 上下班 = "CommuteActivity";
		public static final String 短途 = "ShortWayActivity";
		public static final String 长途 = "LongWayActivity";
		public static final String 出租车 = "";
		public static final String 设置 = "SettingActivity";
		public static final String 关于 = "AboutActivity";
	}

	//时间格式化器
	public static class 时间格式化 {
		private static Locale locale = Locale.SIMPLIFIED_CHINESE;
		public static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat(
				"yyyy-MM-dd", locale);
		public static final SimpleDateFormat yyyy年MM月dd日 = new SimpleDateFormat
				("yyyy年MM月dd日", locale);
		public static final SimpleDateFormat yyyy年M月d日 = new SimpleDateFormat
				("yyyy年M月d日", locale);
		public static final SimpleDateFormat HH_mm_ss = new SimpleDateFormat
				("HH:mm:ss", locale);
		public static final SimpleDateFormat HH时mm分ss秒 = new SimpleDateFormat
				("HH时mm分ss秒", locale);
	}

	//百度定位设置
	public static class 百度定位设置选项 {
		public static final int 定位城市 = 1;
		public static final int 精确定位 = 2;
	}
}
