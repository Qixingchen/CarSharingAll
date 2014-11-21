package com.struts.action;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.ShortwayRequestDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ShortwayAction extends ActionSupport {
private ShortwayRequestDao shortwaydao=new ShortwayRequestDao();
	
	/**增加请求
	 * httpparas：phonenum，userrole,startplacex，startplacey,destinationx，destinationy，startdate，starttime，endtime，
	 */
	public String addrequest() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject shortway_request=new JSONObject();
		if(shortwaydao.saveRequest(request))
		shortway_request.put(result,new Boolean(true));
		else 
	    shortway_request.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(shortway_request.toString());
		writer.flush();
		writer.close();	
		return "shortwayrequest";
	} 
	
	/**查询请求
	 * httpparas：phonenum
	 */
	public String selectrequest() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject select_request=new JSONObject();	
		select_request.put(result,shortwaydao.SelectRequest(request.getParameter("phonenum")));
		PrintWriter writer=response.getWriter();
		writer.write(select_request.toString());
		writer.flush();
		writer.close();	
		return "selectrequest";
	} 
	
	/**删除请求
	 * httpparas：phonenum，time
	 */
	public String deleterequest() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject delete_request=new JSONObject();
		if(shortwaydao.deleteRequest(request.getParameter("phonenum"),request.getParameter("time")))
		delete_request.put(result,new Boolean(true));
		else 
		delete_request.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(delete_request.toString());
		writer.flush();
		writer.close();	
		return "shortwaydelete";
	} 
	
	/**更新请求状态
	 * httpparas：phonenum，time
	 */
	public String updatestatus() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject update_status=new JSONObject();
		if(shortwaydao.updateRequeststatue(request.getParameter("phonenum"), Timestamp.valueOf(request.getParameter("time")), 1))
		update_status.put(result,new Boolean(true));
		else 
		update_status.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(update_status.toString());
		writer.flush();
		writer.close();	
		return "updatestatus";		
	}



}
