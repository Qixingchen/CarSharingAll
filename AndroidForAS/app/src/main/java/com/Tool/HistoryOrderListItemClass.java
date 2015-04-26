package com.Tool;

/**
 * Created by Jo on 2015/4/26.
 * 历史订单列表
 */
public class HistoryOrderListItemClass {

	public static class HistoryOrderListEveryItem {
		public String Carsharing_type;
		public String NeedDate_time;
		public String StartPlace, EndPlace;
		public String Requesttime;
		public int DealStatus;

		public HistoryOrderListEveryItem(String type, String date_time, String startPlace,
		                                 String endPlace,String requesttime,
		                                 int dealstatus) {
			this.Carsharing_type = type;
			this.NeedDate_time = date_time;
			this.StartPlace = startPlace;
			this.EndPlace = endPlace;
			this.Requesttime = requesttime;
			this.DealStatus = dealstatus;
		}
	}

	public HistoryOrderListEveryItem HistoryOrderListItems[];

	public HistoryOrderListItemClass(HistoryOrderListEveryItem[] historyOrderListItems) {
		HistoryOrderListItems = historyOrderListItems;
	}

	public HistoryOrderListItemClass(String type[], String date_time[], String startPlace[],
	                            String endPlace[],String requesttime[],int dealstatus[]) {
		int length = type.length;
		HistoryOrderListItems = new HistoryOrderListEveryItem[length];

		for (int i = 0; i < length; i++) {
			HistoryOrderListItems[i] = new HistoryOrderListEveryItem(type[i],
					date_time[i], startPlace[i], endPlace[i],
					requesttime[i], dealstatus[i]);

		}
	}

}
