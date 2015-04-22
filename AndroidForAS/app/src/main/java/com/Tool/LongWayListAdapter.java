package com.Tool;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmu.carsharing.R;

/**
 * Created by Yulan on 2015/4/21 021.
 */
public class LongWayListAdapter extends RecyclerView.Adapter<LongWayListAdapter.ViewHolder> {

	private LongWayListItemClass longWayListItemData;
	private static Activity mactivity;
	private static String logtag = "长途列表适配器";

	//回调
	private OnViewHolderListener onViewHolderListener;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public ImageView UserPhotoImageView;
		public TextView UserNameTextView, NeedDateTextView,
				PublishDateTextView, ItemDetileTextView;

		public ViewHolder(View v) {
			super(v);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(logtag, "onClick--> position = " + getPosition());
				}
			});

			UserPhotoImageView = (ImageView) v.findViewById(R.id.LongwayListCardItemUserImage);
			UserNameTextView = (TextView) v.findViewById(R.id.LongwayListCardItemUserName);
			NeedDateTextView = (TextView) v.findViewById(R.id.LongwayListCardItemNeedTime);
			PublishDateTextView = (TextView) v.findViewById(R.id.LongwayListCardItemPublishTime);
			ItemDetileTextView = (TextView) v.findViewById(R.id.LongwayListCardItemDetile);
		}
	}

	public LongWayListAdapter(LongWayListItemClass longWayListItemData,
	                          Activity activity,OnViewHolderListener onViewHolderListener) {
		this.longWayListItemData = longWayListItemData;
		mactivity = activity;
		this.onViewHolderListener = onViewHolderListener;
	}

	@Override
	public LongWayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.long_way_list_card_item, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder((LinearLayout) v);
		return vh;
	}

	@Override
	public void onBindViewHolder(LongWayListAdapter.ViewHolder holder, int position) {
		holder.UserPhotoImageView.setImageURI(longWayListItemData
				.LongWayListItems[position].UserPhoto);
		holder.UserNameTextView.setText(longWayListItemData.LongWayListItems[position]
				.Username);
		holder.NeedDateTextView.setText(longWayListItemData.LongWayListItems[position]
				.NeedDate);
		holder.PublishDateTextView.setText(longWayListItemData.LongWayListItems[position]
				.PublishTime);
		holder.ItemDetileTextView.setText(longWayListItemData.LongWayListItems[position]
				.StartPlace + " 至 "+ longWayListItemData.LongWayListItems[position]
				.DesPlace+ "\n" + longWayListItemData.LongWayListItems[position].DetailString);

		if (position == getItemCount() - 1) onViewHolderListener.onRequestedLastItem();

	}

	//到底加载回调
	public interface OnViewHolderListener {
		void onRequestedLastItem();
	}

	@Override
	public int getItemCount() {
		return longWayListItemData == null ? 0 : longWayListItemData.LongWayListItems.length;
	}
}
