package com.Tool;

import android.net.Uri;

/**
 * Created by Yulan on 2015/4/21 021.
 * 长途拼车的列表内容
 */
public class LongWayListItemClass {

	public static class LongWayListEveryItem {
		public Uri UserPhoto;
		public String Username;
		public String PublishTime, NeedDate;
		public String StartPlace, DesPlace;
		public String DetailString;
		public String ID;

		public LongWayListEveryItem(Uri userPhoto, String username, String publishTime, String needDate, String startPlace, String desPlace, String detailString, String id) {
			UserPhoto = userPhoto;
			Username = username;
			PublishTime = publishTime;
			NeedDate = needDate;
			StartPlace = startPlace;
			DesPlace = desPlace;
			DetailString = detailString;
			ID = id;
		}
//
//		public LongWayListEveryItem() {
//		}

	}

	public LongWayListEveryItem LongWayListItems[];

	public LongWayListItemClass(LongWayListEveryItem[] longWayListItems) {
		LongWayListItems = longWayListItems;
	}

	public LongWayListItemClass(Uri userPhoto[], String username[], String publishTime[], String needDate[], String startPlace[], String desPlace[], String detailString[], String ID[]) {
		int length = userPhoto.length;
		LongWayListItems = new LongWayListEveryItem[length];

		for (int i = 0; i < length; i++) {
			LongWayListItems[i] = new LongWayListEveryItem(userPhoto[i], username[i],
					publishTime[i], needDate[i], startPlace[i], desPlace[i], detailString[i], ID[i]);


		}
	}
}
