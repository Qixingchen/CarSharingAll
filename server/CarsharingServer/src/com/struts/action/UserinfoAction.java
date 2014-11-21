package com.struts.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.UserinfoDao;
import com.hibernate.entity.UserInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UserinfoAction extends ActionSupport {
	   
	private UserinfoDao dao=new UserinfoDao();
	
	/**
	 * 检查号码是否重复
	 * http paras:phonenum
	 */
	public String checkphone() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String PhoneNum=new String("PhoneNum");
		JSONObject check_result=new JSONObject();
		if(dao.findUserByphonenum(request.getParameter("phonenum")).size()==0)
		check_result.put(PhoneNum,new Boolean(true));
		else 
		check_result.put(PhoneNum,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(check_result.toString());
		writer.flush();
		writer.close();	
		return "checkphonenum";
	} 
	
	/**
	 * 查询用户信息
	 * http paras:phonenum
	 */
	public String selectuserinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String user_info=new String("result");
		JSONObject info_result=new JSONObject();
		if(dao.findUserByphonenum(request.getParameter("phonenum")).size()==1)
		info_result.put(user_info,dao.findUserByphonenum(request.getParameter("phonenum")).get(0));
		PrintWriter writer=response.getWriter();
		writer.write(info_result.toString());
		writer.flush();
		writer.close();	
		return "userinfo";
	} 
	/**
	 * 用户注册
	 * http paras：phonenum，pwd
	 */
	public String userregister() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		UserInfo userinfo=new UserInfo();
		userinfo.setPhoneNum(request.getParameter("phonenum"));
		userinfo.setPassWord(request.getParameter("pwd"));
		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String Register=new String("Register");
		JSONObject register_result=new JSONObject();
		if(dao.saveUserinfo(userinfo))
		register_result.put(Register,new Boolean(true));
		else 
	    register_result.put(Register,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(register_result.toString());
		writer.flush();
		writer.close();		
		return "register";
	} 
	
	/**
	 * 用户登陆
	 * http paras：phonenum，pwd
	 */
	public String login() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String Login=new String("Login");
		JSONObject login_result=new JSONObject();
		if(dao.checklogin(request.getParameter("phonenum"),request.getParameter("pwd")))
		login_result.put(Login,new Boolean(true));
		else 
	    login_result.put(Login,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(login_result.toString());
		writer.flush();
		writer.close();		
		return "login";
	} 
	
	/**
	 * 修改用户信息
	 * http paras：phonenum,name,age,sex
	 */
	public String changeinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String Login=new String("Login");
		JSONObject change_result=new JSONObject();
		if(dao.updateUserinfo(request))
		change_result.put(Login,new Boolean(true));
		else 
	    change_result.put(Login,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(change_result.toString());
		writer.flush();
		writer.close();		
		return "changeinfo";
	} 
	
	/**
	 * 修改用户信息
	 * http paras：phonenum,name,age,sex
	 */
	public String alterpassword() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String Login=new String("Result");
		JSONObject alterpwd_result=new JSONObject();
		if(dao.alterpassword(request))
		alterpwd_result.put(Login,new Boolean(true));
		else 
		alterpwd_result.put(Login,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(alterpwd_result.toString());
		writer.flush();
		writer.close();		
		return "alterpassword";
	} 
}
