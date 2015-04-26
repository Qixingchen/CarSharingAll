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
 * Created by Jo on 2015/4/26.
 * 历史订单列表适配器
 */
public class HistoryOrderListAdapter extends RecyclerView.Adapter<HistoryOrderListAdapter.ViewHolder>{
	private HistoryOrderListItemClass historyOrderListItemData;
	private static Activity mactivity;
	private static String logtag = "长途列表适配器";

	//回调
	private OnViewHolderListener onViewHolderListener;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public ImageView carsharing_type_image;
		public TextView carsharing_type_text, dealstatus,
				needdate_time, startplace,endplace;

		public ViewHolder(View v) {
			super(v);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(logtag, "onClick--> position = " + getPosition());
				}
			});

			carsharing_type_image = (ImageView) v.findViewById(R.id.HistoryOrder_type_image);
			carsharing_type_text = (TextView) v.findViewById(R.id.HistoryOrder_type_text);
			dealstatus = (TextView) v.findViewById(R.id.HistoryOrder_dealstatus);
			needdate_time = (TextView) v.findViewById(R.id.HistoryOrder_starttime);
			startplace = (TextView) v.findViewById(R.id.HistoryOrder_startplace);
			endplace = (TextView) v.findViewById(R.id.HistoryOrder_endplace);
		}
	}

	public HistoryOrderListAdapter(HistoryOrderListItemClass historyOrderListItemData,
	                          Activity activity,OnViewHolderListener onViewHolderListener) {
		this.historyOrderListItemData = historyOrderListItemData;
		mactivity = activity;
		this.onViewHolderListener = onViewHolderListener;
	}

	@Override
	public HistoryOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.history_order_card_item, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder((LinearLayout) v);
		return vh;
	}

	@Override
	public void onBindViewHolder(HistoryOrderListAdapter.ViewHolder holder, int position) {

		holder.dealstatus.setText(historyOrderListItemData.HistoryOrderListItems[position]
				.DealStatus);
		holder.needdate_time.setText(historyOrderListItemData.HistoryOrderListItems[position]
				.NeedDate_time);
		holder.startplace.setText(historyOrderListItemData.HistoryOrderListItems[position]
				.StartPlace);
		holder.endplace.setText(historyOrderListItemData.HistoryOrderListItems[position]
				.EndPlace);

		String type = historyOrderListItemData.HistoryOrderListItems[position]
				.Carsharing_type;
		if(type.compareTo("shortway") == 0) {
			holder.carsharing_type_image.setImageResource(R.drawable.icon_shortway);
			holder.carsharing_type_text.setText("短途-拼车");
		}else if(type.compareTo("commute") == 0){
			holder.carsharing_type_image.setImageResource(R.drawable.icon_commuters);
			holder.carsharing_type_text.setText("上下班-拼车");
		}else if(type.compareTo("longway") == 0){
			holder.carsharing_type_image.setImageResource(R.drawable.icon_longway);
			holder.carsharing_type_text.setText("长途-拼车");
		}else if(type.compareTo("taxi") == 0){
			holder.carsharing_type_image.setImageResource(R.drawable.icon_taxi);
			holder.carsharing_type_text.setText("出租车-拼车");
		}

		if (position == getItemCount() - 1) onViewHolderListener.onRequestedLastItem();

	}

	//到底加载回调
	public interface OnViewHolderListener {
		void onRequestedLastItem();
	}

	@Override
	public int getItemCount() {
		return historyOrderListItemData == null ? 0 : historyOrderListItemData.HistoryOrderListItems.length;
	}
}
