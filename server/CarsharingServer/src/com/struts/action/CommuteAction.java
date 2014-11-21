package com.struts.action;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import com.hibernate.dao.CommuteRequestDao;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class CommuteAction extends ActionSupport {	
	
	private CommuteRequestDao commutedao=new CommuteRequestDao();
	
	/**增加请求
	 * httpparas：phonenum，startplacex，startplacey,destinationx，destinationy，startdate，enddate，starttime，endtime，weekrepeat，supplycar
	 */
	public String addrequest() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject commute_request=new JSONObject();
		if(commutedao.saveRequest(request))
		commute_request.put(result,new Boolean(true));
		else 
		commute_request.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(commute_request.toString());
		writer.flush();
		writer.close();	
		return "commuterequest";
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
		select_request.put(result,commutedao.SelectRequest(request.getParameter("phonenum")));
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
		if(commutedao.deleteRequest(request.getParameter("phonenum"),request.getParameter("time")))
		delete_request.put(result,new Boolean(true));
		else 
		delete_request.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(delete_request.toString());
		writer.flush();
		writer.close();	
		return "commutedelete";
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
		if(commutedao.updateRequeststatue(request.getParameter("phonenum"), Timestamp.valueOf(request.getParameter("time")), 1))
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
