package com.struts.action;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.CarinfoDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class CarinfoAction extends ActionSupport {
	
	private CarinfoDAO cardao=new CarinfoDAO();	
	
	/**查询车辆信息
	 * httpparas：phonenum
	 */
	public String selectcarinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject select_carinfo=new JSONObject();	
		select_carinfo.put(result,cardao.selectcarinfo(request.getParameter("phonenum")));
		PrintWriter writer=response.getWriter();
		writer.write(select_carinfo.toString());
		writer.flush();
		writer.close();	
		return "selectcarinfo";
	} 
	
	
	/**
	 * 增加车辆信息
	 * http paras：carnum,phonenum,carbrand,carmodel,carcolor,capcity
	 */
	public String addcarinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject carinfo_add=new JSONObject();
		if(cardao.saveCarinfo(request))
		carinfo_add.put(result,new Boolean(true));
		else 
		carinfo_add.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(carinfo_add.toString());
		writer.flush();
		writer.close();	
		return "carinfoadd";
	} 
	
	/**更新车辆信息
	 * httpparas：carnum,,carbrand,carmodel,carcolor,capcity,phonenum
	 */
	public String updatecarinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject update_carinfo=new JSONObject();
		if(cardao.updatecarinfo(request))
		update_carinfo.put(result,new Boolean(true));
		else 
		update_carinfo.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(update_carinfo.toString());
		writer.flush();
		writer.close();	
		return "updatecarinfo";		
	}

}
