package com.fileload.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCopyUtil {

	public String copyFile(String phonenum, String fileData, String destDir)
			throws IOException {
		// Date date = new Date();
		// String dirName = new SimpleDateFormat("yyyyMMdd").format(date);
		// String newFileName = new
		// SimpleDateFormat("yyyyMMddHHmmss_SSS").format(date);
		// String dirPath = destDir+dirName;
		File dir = new File(destDir);
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdir();
		}
		String newFilePath = destDir + phonenum + ".jpg";
		FileInputStream in = new FileInputStream(new File(fileData));
		FileOutputStream out = new FileOutputStream(newFilePath);
		int length = 1024;
		byte[] buffer = new byte[length];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				return newFilePath;
			} else
				out.write(buffer, 0, ins);
		}
	}

}
