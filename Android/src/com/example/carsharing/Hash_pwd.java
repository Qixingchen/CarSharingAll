/*
 * 密码加密函数
 * 两次哈希
 */

package com.example.carsharing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash_pwd {

	public String hashans(String pwd) {
		pwd = Hash(pwd);
		pwd += "5f202ed4f91f39d16fe59e153f682ded8abc874897474a25ebc7914d2533c831";
		return Hash(pwd);

	}

	private String Hash(String need) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(need.getBytes());

		byte byteData[] = md.digest();

		// /convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}

}
