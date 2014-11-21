package com.hibernate.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.support.HibernateSessionFactory;
import com.json.object.CarshareDealJSON;

public class CarshareDAO {

	public CarshareDealJSON selectrouteinfo(String dealid) {
		// 创建session
		Session session = HibernateSessionFactory.getSession();
		CarshareDealJSON routeinfo = new CarshareDealJSON();
		Transaction tx = session.beginTransaction();
		tx.begin();
		SQLQuery query = session
				.createSQLQuery("SELECT * FROM carshare_deal where DealID=:dealid");
		query.setParameter("dealid", dealid);
		try {
			Object[] result = (Object[]) query.list().get(0);
			tx.commit();
			routeinfo.setDealId(result[0].toString());
			routeinfo.setDealTime(result[1].toString());
			routeinfo.setSharingType(result[2].toString());
			routeinfo.setStartPlaceX(result[3].toString());
			routeinfo.setStartPlaceY(result[4].toString());
			routeinfo.setDestinationX(result[5].toString());
			routeinfo.setDestinationY(result[6].toString());
			routeinfo.setFinshStatus(result[7].toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 事务回滚
		} finally {
			// 关闭session
			session.close();
		}
		return routeinfo;
	}

}
