package com.hibernate.dao;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.support.HibernateSessionFactory;
import com.json.object.CarInfoJSON;

public class CarinfoDAO {
	
	public CarInfoJSON selectcarinfo(String  phonenum)
	{
    	//创建session
		Session session=HibernateSessionFactory.getSession();	
		CarInfoJSON carinfo=new CarInfoJSON();
		Transaction tx=session.beginTransaction();
		tx.begin();
		SQLQuery query=session.createSQLQuery("SELECT * FROM car_info where DriverID=:phonenum");
	    query.setParameter("phonenum",phonenum);
		try {
			 Object[] result=(Object[]) query.list().get(0);
			 tx.commit();
			 carinfo.setId(result[0].toString());
			 carinfo.setCarNum(result[1].toString());
			 carinfo.setDriverId(result[2].toString());
			 carinfo.setCarBrand(result[3].toString());
			 carinfo.setCarModel(result[4].toString());
			 carinfo.setCarColor(result[5].toString());
			 carinfo.setCarCapacity(result[6].toString());
//			 carinfo.setDrivedYears(result[7].toString());
//			 carinfo.setCarPohto(result[8].toString());
//			 carinfo.setDriveLicense(result[9].toString());	         
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//事务回滚
		}finally{
			//关闭session
			session.close();
		}		
		return carinfo;
	}
	
	public Boolean saveCarinfo(HttpServletRequest request)
	{
		//创建session
		Session session=HibernateSessionFactory.getSession();
		//开启事务
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
	    Query query=session.createSQLQuery("INSERT INTO car_info (`CarNum`, `DriverID`, `CarBrand`, `CarModel`, `CarColor`, `CarCapacity`) VALUES " +
	    		"(:carnum,:phonenum,:carbrand,:carmodel,:carcolor,:capacity)");
	    query.setParameter("carnum",request.getParameter("carnum"));
	    query.setParameter("phonenum",request.getParameter("phonenum"));   
	    query.setParameter("carbrand",request.getParameter("carbrand"));
	    query.setParameter("carmodel",request.getParameter("carmodel"));   
	    query.setParameter("carcolor",request.getParameter("carcolor"));    
	    query.setParameter("capacity",request.getParameter("capacity")); 
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

	public Boolean updatecarinfo(HttpServletRequest request)
	{
		//创建session
		Session session=HibernateSessionFactory.getSession();
		//开启事务
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		Query query=session.createSQLQuery("UPDATE car_info SET CarNum=:carnum,CarBrand=:carbrand,CarModel=:carmodel,CarColor=:carcolor,CarCapacity=:capacity WHERE DriverID=:phonenum");
		query.setParameter("carnum", request.getParameter("carnum"));
		query.setParameter("carbrand", request.getParameter("carbrand"));
		query.setParameter("carmodel", request.getParameter("carmodel"));
		query.setParameter("carcolor", request.getParameter("carcolor"));
		query.setParameter("capacity", request.getParameter("capacity"));
		query.setParameter("phonenum", request.getParameter("phonenum"));
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
