/*
 * 函数
 * 判断输入用户名是否为合法的号码
 * by Jo
 */

package com.xmu.carsharing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileOrNo {

	public boolean mobilejudging(String phonenum) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phonenum);
		return m.matches();

	}

}
