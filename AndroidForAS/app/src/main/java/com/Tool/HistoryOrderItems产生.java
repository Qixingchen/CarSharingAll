package com.Tool;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

/**
 * Created by Jo on 2015/4/26.
 * 历史订单列表内容的产生,修改自"mylist1产生"类
 */
public class HistoryOrderItems产生 {
	private Activity activity;

	private String logtag = "历史订单列表内容的产生";

	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;
	DataBaseAct dbact;

	//回调函数
	private GetHistoryOrderAnsCallBack getHistoryOrderAnsCallBack;

	public HistoryOrderItems产生(Activity act) {
		this.activity = act;
		getPhone = new ToolWithActivityIn(activity);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();
		dbact = new DataBaseAct(activity, UserPhoneNumber);
	}

	public void historyOrderItems产生(final int start, final int end,
	                                final GetHistoryOrderAnsCallBack  getLongWayAnsCallBack,
	                                final SwipeRefreshLayout swipeLayout) {

		if (swipeLayout != null) {
			swipeLayout.setRefreshing(true);
		}

//		ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
		String[] Carsharing_type;
		String[] NeedDate_time;
		String[] StartPlace, EndPlace;
		String[] Requesttime;
		int[] DealStatus;

		int theEndPoistion = end;
		boolean isend = false;

		Cursor[] cursors = new Cursor[3];
		cursors = dbact.read所有订单();
		Cursor dbresult1 = cursors[0]; //shortway
		Cursor dbresult2 = cursors[1]; //commute
		Cursor dbresult3 = cursors[2]; //longway
		int count = dbresult1.getCount()+ dbresult2.getCount()+dbresult3.getCount();
		Log.e(logtag,"数据库读取总数"+count);
		if (end > count) {
			isend = true;
			theEndPoistion = count;
		} else {
			isend = false;
		}

		int length = theEndPoistion - start;
		Log.e(logtag,"历史订单总数="+length);
		Carsharing_type = new String[length];
		NeedDate_time = new String[length];
		StartPlace = new String[length];
		EndPlace = new String[length];
		Requesttime = new String[length];
		DealStatus = new int[length];

		int i = 0;
		for (dbresult1.moveToFirst(); !dbresult1.isAfterLast()&i < length;
		               dbresult1.moveToNext()) { //短途

			Log.e(logtag,"in shortway");
			Carsharing_type[i] = "shortway";
			NeedDate_time[i] = dbresult1.getString(dbresult1.getColumnIndex("Startdate"))
					+ " "
					+ dbresult1.getString(dbresult1.getColumnIndex("Starttime")); //出发时间

			StartPlace[i] = dbresult1.getString(dbresult1.getColumnIndex
					("StartplaceName"));
			EndPlace[i] = dbresult1.getString(dbresult1.getColumnIndex("EndplaceName"));

			DealStatus[i] = dbresult1.getInt(dbresult1.getColumnIndex("Dealstatus"));

			Requesttime[i] = dbresult1.getString(dbresult1.getColumnIndex("requesttime"));
			i ++;

		}
		for (dbresult2.moveToFirst(); !dbresult2.isAfterLast()&i < length; dbresult2
				.moveToNext()) { //上下班

			Carsharing_type[i] = "commute";
			NeedDate_time[i] = dbresult2.getString(dbresult2.getColumnIndex("Starttime"))
					+ " "
					+ "每周"
					+ dbresult2.getString(dbresult2.getColumnIndex("Weekrepeat")); //出发时间

			StartPlace[i] = dbresult2.getString(dbresult2.getColumnIndex
					("StartplaceName"));
			EndPlace[i] = dbresult2.getString(dbresult2.getColumnIndex("EndplaceName"));

			DealStatus[i] = dbresult2.getInt(dbresult2.getColumnIndex("Dealstatus"));

			Requesttime[i] = dbresult2.getString(dbresult2.getColumnIndex("requesttime"));
			i ++;

		}
		for (dbresult3.moveToFirst(); !dbresult3.isAfterLast()&i < length;
		     dbresult3.moveToNext()) { //长途

			Carsharing_type[i] = "longway";
			NeedDate_time[i] = dbresult3.getString(dbresult3.getColumnIndex
					("Startdate")); //出发时间

			StartPlace[i] = dbresult3.getString(dbresult3.getColumnIndex
					("StartplaceName"));
			EndPlace[i] = dbresult3.getString(dbresult3.getColumnIndex
					("EndplaceName"));

			DealStatus[i] = dbresult3.getInt(dbresult3.getColumnIndex("Dealstatus"));

			Requesttime[i] = dbresult3.getString(dbresult3.getColumnIndex("requesttime"));
			i ++;

		}
		HistoryOrderListItemClass historyOrderListItemClass = new
				HistoryOrderListItemClass(Carsharing_type,
				NeedDate_time, StartPlace, EndPlace,Requesttime, DealStatus);
		getLongWayAnsCallBack.getHistoryOrderAnsCallBack(isend, historyOrderListItemClass);
		if (swipeLayout != null) {
			swipeLayout.setRefreshing(false);
		}
	}
	//回调函数
	public interface GetHistoryOrderAnsCallBack {
		public void getHistoryOrderAnsCallBack(boolean isend, HistoryOrderListItemClass
				historyOrderListItemClass);
	}
}
