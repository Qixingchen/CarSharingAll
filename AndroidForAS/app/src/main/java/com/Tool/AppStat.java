package com.Tool;

/**
 * Created by 雨蓝 on 2015/2/9.
 * 这里描述了app所使用的状态常量
 */
public class AppStat {

	//smssdk量
	public static class SMSSDK{
		public static final int OK = 1;
		public static final int ERROR = 2;
	}
	//注册界面
	public static class Register{
		public static final int 发送验证短信 = 1;
		public static final int 跳转界面验证 = 2;
	}
	//抽屉跳转的类名
	public static class classname{
		public static final String 个人中心 = "PersonalCenterActivity";
		public static final String 上下班 = "CommuteActivity";
		public static final String 短途 = "ShortWayActivity";
		public static final String 长途 = "LongWayActivity";
		public static final String 出租车 = "";
		public static final String 设置 = "SettingActivity";
		public static final String 关于 = "AboutActivity";


	}
}
