package com.struts.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.LongwayPublishDao;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class LongwayAction extends ActionSupport {
    private LongwayPublishDao longwaydao=new LongwayPublishDao();
    

	/**查询请求
	 * Httpparas：phonenum
	 */
	public String selectpublish() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject select_request=new JSONObject();	
		select_request.put(result,longwaydao.selectPublish(request.getParameter("phonenum")));
		PrintWriter writer=response.getWriter();
		writer.write(select_request.toString());
		writer.flush();
		writer.close();	
		return "selectpublish";
	} 
	
	/**查看发布
	 * Httpparas：role
	 */
	public String lookuppublish() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject lookup_publish=new JSONObject();	
		lookup_publish.put(result,longwaydao.lookuppublish(request.getParameter("role")));
		PrintWriter writer=response.getWriter();
		writer.write(lookup_publish.toString());
		writer.flush();
		writer.close();	
		return "lookuppublish";
	} 
	/**
	 * 发布信息
	 * Httpparas：phonenum,userrole,startdate,startplace,destination,noteinfo
	 */
	public String addpublish() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject longway_publish=new JSONObject();
		if(longwaydao.savePublish(request))
		longway_publish.put(result,new Boolean(true));
		else 
		longway_publish.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(longway_publish.toString());
		writer.flush();
		writer.close();	
		return "longwaypublish";
	} 
	
	/**删除发布
	 * httpparas：phonenum，time
	 */
	public String deletepublish() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject delete_publish=new JSONObject();
		if(longwaydao.deleteRequest(request.getParameter("phonenum"),request.getParameter("time")))
		delete_publish.put(result,new Boolean(true));
		else 
		delete_publish.put(result,new Boolean(false));
		PrintWriter writer=response.getWriter();
		writer.write(delete_publish.toString());
		writer.flush();
		writer.close();	
		return "longwaydelete";
	} 
}
