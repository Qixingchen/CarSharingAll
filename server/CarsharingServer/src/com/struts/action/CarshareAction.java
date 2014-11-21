package com.struts.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.CarshareDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class CarshareAction extends ActionSupport {
    
	private CarshareDAO sharedao=new CarshareDAO();
	
	/**²éÑ¯Â·Ïß
	 * httpparas£ºdealid
	 */
	public String lookuprouteinfo() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject lookup_rounteinfo=new JSONObject();	
		lookup_rounteinfo.put(result,sharedao.selectrouteinfo(request.getParameter("dealid")));
		PrintWriter writer=response.getWriter();
		writer.write(lookup_rounteinfo.toString());
		writer.flush();
		writer.close();	
		return "lookuprouteinfo";
	} 
}
