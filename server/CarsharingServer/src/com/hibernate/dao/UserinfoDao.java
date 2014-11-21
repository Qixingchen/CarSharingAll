package com.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.entity.UserInfo;
import com.hibernate.support.HibernateSessionFactory;
import com.json.object.ShortwayRequestJSON;
import com.json.object.UserInfoJSON;

public class UserinfoDao{	
	 
	public Boolean saveUserinfo(UserInfo userinfo)
	{
		//����session
		Session session=HibernateSessionFactory.getSession();
		//��������
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		try {
			tx.begin();
	        //�������
			session.save(userinfo);
			//�ύ����
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//����ع�
			success=false;
			tx.rollback();
		}finally{
			//�ر�session
			session.close();
		}
		return success;
	}
	
	
	
	public List<UserInfoJSON> findUserByphonenum(String phonenum)
	{
			Session session=HibernateSessionFactory.getSession();	
			List<UserInfoJSON> userlist=new ArrayList<UserInfoJSON>();
			Transaction tx=session.beginTransaction();
			tx.begin();
			SQLQuery query=session.createSQLQuery("SELECT * FROM user_info where PhoneNum=:phonenum");
		    query.setParameter("phonenum",phonenum);
			try {
				List<Object []> list=query.list();
				tx.commit();
				Iterator<Object []> iterator=list.iterator();
				while(iterator.hasNext())
					{
					Object [] request=iterator.next();
					UserInfoJSON item=new UserInfoJSON();				
					item.setUserId(request[0].toString());
					item.setPhoneNum(request[1].toString());
					item.setPassWord(request[2].toString());
					if(request[3]!=null)
					item.setName(request[3].toString());
					if(request[4]!=null)
					item.setSex(request[4].toString());
					if(request[5]!=null)
					item.setAge(request[5].toString());
					if(request[6]!=null)
					item.setPhotoAddress(request[6].toString());
					if(request[7]!=null)
					item.setIdentifyNum(request[7].toString());
					if(request[8]!=null)
					item.setCreditScore(request[8].toString());						
					userlist.add(item);			
					}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//����ع�
			}finally{
				//�ر�session
				session.close();
			}		
			return userlist;
		}
	
	public Boolean checklogin(String phonenum,String pwd)
	{
		Session session=HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		tx.begin();
		Query query=session.createQuery("from UserInfo where PhoneNum = :phonenum and passWord= :password");
		query.setString("phonenum", phonenum);
		query.setString("password", pwd);
		List<UserInfo> users=null;
		try {
	        users=query.list();    
	        tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//�ر�session
			session.close();    
		}		
		if(users.size()==1)
			return true;
		else
			return false;
	}
	
	public Boolean updateUserinfo(HttpServletRequest request)
	{
		//����session
		Session session=HibernateSessionFactory.getSession();
		//��������
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		Query query = session
				.createSQLQuery("UPDATE user_info SET "
						+ "Name=:name,Sex =:sex,age=:age where PhoneNum =:phonenum");
		query.setParameter("name", request.getParameter("name"));
		query.setParameter("age", request.getParameter("age"));
		query.setParameter("sex", request.getParameter("sex"));
		query.setParameter("phonenum", request.getParameter("phonenum"));			
		try {
			tx.begin();
	        //�������
			query.executeUpdate();
			//�ύ����
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//����ع�
			success=false;
			tx.rollback();
		}finally{
			//�ر�session
			session.close();
		}
		return success;
	}
	
	public Boolean alterpassword(HttpServletRequest request)
	{
		//����session
		Session session=HibernateSessionFactory.getSession();
		//��������
		Boolean success=true;
		Transaction tx=session.beginTransaction();	
		Query query = session
				.createSQLQuery("UPDATE user_info SET "
						+ "PassWord=:password where PhoneNum =:phonenum");
		query.setParameter("password", request.getParameter("password"));
		query.setParameter("phonenum", request.getParameter("phonenum"));			
		try {
			tx.begin();
	        //�������
			query.executeUpdate();
			//�ύ����
		    tx.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//����ع�
			success=false;
			tx.rollback();
		}finally{
			//�ر�session
			session.close();
		}
		return success;
	}
}
