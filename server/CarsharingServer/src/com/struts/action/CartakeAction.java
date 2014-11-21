package com.struts.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.hibernate.dao.CartakeDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class CartakeAction extends ActionSupport {
	
	private CartakeDAO takedao=new CartakeDAO();
	
	/**查询拼车结果
	 * httpparas：phonenum
	 */
	public String selectcartake() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject select_cartake=new JSONObject();	
		select_cartake.put(result,takedao.selectcartake(request.getParameter("phonenum")));
		PrintWriter writer=response.getWriter();
		writer.write(select_cartake.toString());
		writer.flush();
		writer.close();	
		return "selectcartake";
	} 
	
	/**查询路线详情passnode
	 * httpparas：dealid
	 */
	public String selectpassnode() throws Exception{
		HttpServletRequest request=(HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/json;charset=utf-8");	
		response.setCharacterEncoding("UTF-8");
		String result=new String("result");
		JSONObject select_passnode=new JSONObject();	
		select_passnode.put(result,takedao.selectpassnode(request.getParameter("dealid")));
		PrintWriter writer=response.getWriter();
		writer.write(select_passnode.toString());
		writer.flush();
		writer.close();	
		return "selectpassnode";
	} 
	
}
