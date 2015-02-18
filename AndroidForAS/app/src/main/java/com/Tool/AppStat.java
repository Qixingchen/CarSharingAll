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
}
