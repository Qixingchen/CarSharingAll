package com.struts.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.fileload.util.FileCopyUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class FileUploadAction extends ActionSupport
{
	//服务器端要和客户端一致
	private String fileName = null;
	private String fileData = null;
	//必须有set方法
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String upload() throws Exception
	{
		System.out.print("uploading......");
		String webappPath = findServerPath();	
		String imageFile_path = ServletActionContext.getServletContext().getInitParameter("UPLOAD_USERHEADE_PATH");
		FileCopyUtil util = new FileCopyUtil();
	//	String fileType = "jpg";
	//	String[] array = fileName.split("\\.");
	//	String fileType = array[array.length-1];
    	String destDir = webappPath + imageFile_path;
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject upload_request=new JSONObject();	
		upload_request.put(result,"finish");
	    try{ 
	    	util.copyFile(fileName,fileData,destDir);
	    	PrintWriter writer=response.getWriter();
			writer.write(upload_request.toString());
			writer.flush();
			writer.close();	
	    } 
	    catch( Exception e ) {
	    	e.printStackTrace();
	    }
	    return "uploadfinish";
	}

	private String findServerPath()
	{	
        String classPath = this.getClass().getClassLoader().getResource("/").getPath();  
        try {  
            classPath =URLDecoder.decode(classPath,"gb2312");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }          
        int i = classPath.indexOf("webapps");
        String path = classPath.substring(1,i+7);
        return path;  
    }  

}

