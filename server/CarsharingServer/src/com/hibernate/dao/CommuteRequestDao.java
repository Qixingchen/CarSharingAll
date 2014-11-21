package com.hibernate.dao;

import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.hibernate.support.HibernateSessionFactory;
import com.json.object.CommuteRequestJSON;

public class CommuteRequestDao {
	
	public List<CommuteRequestJSON> SelectRequest(String  phonenum)
	{
    	//创建session
		Session session=HibernateSessionFactory.getSession();	
		List<CommuteRequestJSON> commutelist=new ArrayList<CommuteRequestJSON>();
		Transaction tx=session.beginTransaction();
		tx.begin();
		SQLQuery query=session.createSQLQuery("SELECT * FROM commute_request where UserID=:phonenum ORDER BY ID DESC");	
	    query.setParameter("phonenum",phonenum);
		try {
			List<Object []> list=query.list();
			tx.commit();
			Iterator<Object []> iterator=list.iterator();
			while(iterator.hasNext())
				{
				Object [] request=iterator.next();
				CommuteRequestJSON item=new CommuteRequestJSON();				
				item.setId(request[0].toString());
				item.setUserId(request[1].toString());
				item.setRequestTime(request[2].toString());
				item.setStartPlaceX(request[3].toString());
				item.setStartPlaceY(request[4].toString());
				item.setStartPlace(request[5].toString());
				item.setDestinationX(request[6].toString());
				item.setDestinationY(request[7].toString());
				item.setDestination(request[8].toString());
				item.setStartDate(request[9].toString());
				item.setEndDate(request[10].toString());
				item.setStartTime(request[11].toString());
				item.setEndTime(request[12].toString());
				item.setWeekRepeat(request[13].toString());
				item.setSupplyCar(request[14].toString());
				item.setDealStatus(request[15].toString());								
				commutelist.add(item);			
				}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
		}finally{
			//关闭session
			session.close();
		}		
		return commutelist;
	}
	
	public Boolean saveRequest(HttpServletRequest request)
	{
		//创建session
		Session session=HibernateSessionFactory.getSession();
		//开启事务
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
	    Query query=session.createSQLQuery("INSERT INTO commute_request (`UserID`, `RequestTime`, `StartPlaceX`, `StartPlaceY`,`StartPlace`,`DestinationX`,`DestinationY`,`Destination`, `StartDate`, `EndDate`, `StartTime`, `EndTime`, `WeekRepeat`, `SupplyCar`, `DealStatus`) " +
	    		"VALUES (:phonenum,:requesttime,:startplacex,:startplacey,:startplace,:destinationx,:destinationy,:destination,:startdate,:enddate,:starttime,:endtime,:weekrepeat,:supplycar,:status)");
	    query.setParameter("phonenum",request.getParameter("phonenum"));   
	    query.setParameter("requesttime",new Timestamp(System.currentTimeMillis()));
	    query.setParameter("startplacex",request.getParameter("startplacex"));   
	    query.setParameter("startplacey",request.getParameter("startplacey")); 
	    query.setParameter("startplace",request.getParameter("startplace"));
	    query.setParameter("destinationx",request.getParameter("destinationx")); 
	    query.setParameter("destinationy",request.getParameter("destinationy")); 
	    query.setParameter("destination",request.getParameter("destination")); 
	    query.setParameter("startdate",request.getParameter("startdate"));   
	    query.setParameter("enddate",request.getParameter("enddate"));   
	    query.setParameter("starttime",request.getParameter("starttime"));   
	    query.setParameter("endtime",request.getParameter("endtime"));   
	    query.setParameter("weekrepeat",request.getParameter("weekrepeat"));   
	    query.setParameter("supplycar",request.getParameter("supplycar")); 
	    query.setParameter("status",0); 
		try {		
			tx.begin();
			query.executeUpdate();
			//提交事务
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
			success=false;
			tx.rollback();
		}finally{
			//关闭session
			session.close();
		}
		return success;
	}
	
	public Boolean deleteRequest(String phonenum,String time)
	{
		//创建session
		Session session=HibernateSessionFactory.getSession();
		//开启事务
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		Query query=session.createSQLQuery("DELETE FROM commute_request WHERE UserID=:phonenum and RequestTime=:time");
	    query.setParameter("phonenum", phonenum);
	    query.setParameter("time",time);
		try {
			tx.begin();	
		    if(query.executeUpdate()==0)
		    	success=false;
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
			success=false;
			tx.rollback();
		}finally{
			//关闭session
			session.close();
		}
		return success;
	}
	
	public Boolean updateRequeststatue(String phonenum,Timestamp time,int status)
	{
		//创建session
		Session session=HibernateSessionFactory.getSession();
		//开启事务
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		Query query=session.createSQLQuery("UPDATE commute_request SET DealStatus=:status WHERE UserID=:phonenum and RequestTime=:time");
		query.setParameter("phonenum", phonenum);
		query.setParameter("time",time);
		query.setParameter("status",status);
		try {
			tx.begin();
			query.executeUpdate();
			//提交事务
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
			success=false;
			tx.rollback();
		}finally{
			//关闭session
			session.close();
		}
		return success;
	}
}
