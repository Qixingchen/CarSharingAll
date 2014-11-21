package com.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.support.HibernateSessionFactory;
import com.json.object.CarshareDealJSON;
import com.json.object.CartakeDealJSON;

public class CartakeDAO {
	
	public List<CarshareDealJSON> selectcartake(String  phonenum)
	{
		CarshareDAO  sharedao=new CarshareDAO();
    	//创建session
		Session session=HibernateSessionFactory.getSession();	
		List<CartakeDealJSON> cartakelist=new ArrayList<CartakeDealJSON>();
		List<CarshareDealJSON> carsharelist=new ArrayList<CarshareDealJSON>();
		Transaction tx=session.beginTransaction();
		tx.begin();
		SQLQuery query=session.createSQLQuery("SELECT * FROM cartake_deal where DriverID=:dphonenum or PassengerID=:pphonenum ORDER BY ID DESC");	
	    query.setParameter("dphonenum",phonenum);
	    query.setParameter("pphonenum",phonenum);
		try {
			List<Object []> list=query.list();
			tx.commit();
			Iterator<Object []> iterator=list.iterator();
			Object [] driver=null;
			while(iterator.hasNext())
				{
				Object [] request=iterator.next();			
				CartakeDealJSON item=new CartakeDealJSON();				
				item.setId(request[0].toString());
				item.setDealId(request[1].toString());
				item.setDriverId(request[2].toString());
				item.setPassengerId(request[3].toString());
				item.setPassengerOrder(request[4].toString());
				item.setPosionX(request[5].toString());    
				item.setPosionY(request[6].toString());
				if((driver!=null&&!request[1].toString().equals(driver[1].toString()))||driver==null)	
				cartakelist.add(item);	
				driver=request;
				}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
		}finally{
			//关闭session
			session.close();
		}	
		
		for(int i=0;i<cartakelist.size();i++)
		{
			CarshareDealJSON route=sharedao.selectrouteinfo(cartakelist.get(i).getDealId());
		    carsharelist.add(route);
		}
		return carsharelist;
	}
	
	public List<CartakeDealJSON> selectpassnode(String  dealid)
	{
    	//创建session
		Session session=HibernateSessionFactory.getSession();	
		List<CartakeDealJSON> passnodelist=new ArrayList<CartakeDealJSON>();
		Transaction tx=session.beginTransaction();
		tx.begin();
		SQLQuery query=session.createSQLQuery("SELECT * FROM cartake_deal where DealID=:dealid ORDER BY PassengerOrder");	
	    query.setParameter("dealid",dealid);
		try {
			List<Object []> list=query.list();
			tx.commit();
			Iterator<Object []> iterator=list.iterator();
			while(iterator.hasNext())
				{
				Object [] request=iterator.next();
				CartakeDealJSON item=new CartakeDealJSON();				
				item.setId(request[0].toString());
				item.setDealId(request[1].toString());
				item.setDriverId(request[2].toString());
				item.setPassengerId(request[3].toString());
				item.setPassengerOrder(request[4].toString());
				item.setPosionX(request[5].toString());
				item.setPosionY(request[6].toString());
				passnodelist.add(item);			
				}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
		}finally{
			//关闭session
			session.close();
		}		
		return passnodelist;
	}
}
