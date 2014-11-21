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
import com.json.object.LongwayPublishJSON;

public class LongwayPublishDao {

	public List<LongwayPublishJSON> selectPublish(String phonenum) {
		// 创建session
		Session session = HibernateSessionFactory.getSession();
		List<LongwayPublishJSON> longwaylist = new ArrayList<LongwayPublishJSON>();
		Transaction tx = session.beginTransaction();
		tx.begin();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM longway_publish where UserID=:phonenum ORDER BY ID DESC");
		query.setParameter("phonenum", phonenum);
		try {
			List<Object[]> list = query.list();
			tx.commit();
			Iterator<Object[]> iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] request = iterator.next();
				LongwayPublishJSON item = new LongwayPublishJSON();
				item.setId(request[0].toString());
				item.setUserId(request[1].toString());
				item.setPublishTime(request[2].toString());
				item.setUserRole(request[3].toString());
				item.setStartDate(request[4].toString());
				item.setStartPlace(request[5].toString());
				item.setDestination(request[6].toString());
				item.setNoteInfo(request[7].toString());
				longwaylist.add(item);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 事务回滚
		} finally {
			// 关闭session
			session.close();
		}
		return longwaylist;
	}

	public Boolean savePublish(HttpServletRequest request) {
		// 创建session
		Session session = HibernateSessionFactory.getSession();
		// 开启事务
		Boolean success = true;
		Transaction tx = session.beginTransaction();
		Query query = session
				.createSQLQuery("INSERT INTO longway_publish(`UserID`, `PublishTime`, `UserRole`, `StartDate`, `StartPlace`, `Destination`, `NoteInfo`) VALUES (:phonenum,:publishtime,:userrole,:startdate,:startplace,:destination,:noteinfo)");
		query.setParameter("phonenum", request.getParameter("phonenum"));
		query.setParameter("publishtime",
				new Timestamp(System.currentTimeMillis()));
		query.setParameter("userrole", request.getParameter("userrole"));
		query.setParameter("startplace", request.getParameter("startplace"));
		query.setParameter("destination", request.getParameter("destination"));
		query.setParameter("startdate", request.getParameter("startdate"));
		query.setParameter("noteinfo", request.getParameter("noteinfo"));
		try {
			tx.begin();
			query.executeUpdate();
			// 提交事务
			tx.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 事务回滚
			success = false;
			tx.rollback();
		} finally {
			// 关闭session
			session.close();
		}
		return success;
	}

	public Boolean deleteRequest(String phonenum, String time) {
		// 创建session
		Session session = HibernateSessionFactory.getSession();
		// 开启事务
		Boolean success = true;
		Transaction tx = session.beginTransaction();
		Query query = session
				.createSQLQuery("DELETE FROM  longway_publish WHERE UserID=:phonenum and PublishTime=:time");
		query.setParameter("phonenum", phonenum);
		query.setParameter("time", time);
		try {
			tx.begin();
			if (query.executeUpdate() == 0)
				success = false;
			tx.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 事务回滚
			success = false;
			tx.rollback();
		} finally {
			// 关闭session
			session.close();
		}
		return success;
	}

	public List<LongwayPublishJSON> lookuppublish(String role) {
		// 创建session
		Session session = HibernateSessionFactory.getSession();
		List<LongwayPublishJSON> longwaylookup = new ArrayList<LongwayPublishJSON>();
		Transaction tx = session.beginTransaction();
		tx.begin();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM longway_publish where UserRole=:role ORDER BY ID DESC");
		query.setParameter("role", role);
		try {
			List<Object[]> list = query.list();
			tx.commit();
			Iterator<Object[]> iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] request = iterator.next();
				LongwayPublishJSON item = new LongwayPublishJSON();
				item.setId(request[0].toString());
				item.setUserId(request[1].toString());
				item.setPublishTime(request[2].toString());
				item.setUserRole(request[3].toString());
				item.setStartDate(request[4].toString());
				item.setStartPlace(request[5].toString());
				item.setDestination(request[6].toString());
				item.setNoteInfo(request[7].toString());
				longwaylookup.add(item);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 事务回滚
		} finally {
			// 关闭session
			session.close();
		}
		return longwaylookup;
	}
}
