package com.xmu.carsharing;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.Tool.HistoryOrderItems产生;
import com.Tool.HistoryOrderListAdapter;
import com.Tool.HistoryOrderListItemClass;

public class HistoryOrderListActivity extends ActionBarActivity implements
		HistoryOrderItems产生.GetHistoryOrderAnsCallBack,
		SwipeRefreshLayout.OnRefreshListener,
		HistoryOrderListAdapter.OnViewHolderListener{

	//RecyclerView
	private RecyclerView mRecyclerView;
	private RecyclerView.LayoutManager mLayoutManager;
	private HistoryOrderListAdapter historyOrderListAdapter;

	//判断是否到结尾
	private boolean isEnd = false;

	//列表数据
	private HistoryOrderListItemClass historyOrderListItemClass;
	private int listSingelLoadItems = 10;

	//列表内容生成
	private HistoryOrderItems产生 historyOrderItems产生;

	//下拉刷新
	private boolean isRefresh = false;
	private SwipeRefreshLayout swipeLayout;
	private int nowLastItemIndex = 0;

	private String logtag = "历史订单列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order_list);

		//RecyclerView
		mRecyclerView = (RecyclerView) findViewById(R.id.HistoryOrderListRecyclerView);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		//RecyclerView End

		//SwipeRefresh
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.HistoryOrderSwipeToRefreash);
		swipeLayout.setOnRefreshListener(this);
		//SwipeRefresh END

		historyOrderItems产生 = new HistoryOrderItems产生(this);

	}

	@Override
	protected void onResume(){
		super.onResume();
		historyOrderItems产生.historyOrderItems产生(0, listSingelLoadItems,this,swipeLayout);
		nowLastItemIndex = listSingelLoadItems;
	}

	@Override
	public void getHistoryOrderAnsCallBack(boolean isend, HistoryOrderListItemClass
			historyOrderListItemClass) {
		this.isEnd = isend;
		this.historyOrderListItemClass = historyOrderListItemClass;
		//下拉刷新
		isRefresh = false;
		//下拉刷新 END
		//RecyclerView
		historyOrderListAdapter = new HistoryOrderListAdapter
				(historyOrderListItemClass,this,this);
		mRecyclerView.setAdapter(historyOrderListAdapter);
		//RecyclerView End
	}
	@Override
	public void onRefresh() {
		if (!isRefresh){
			isRefresh = true;
			historyOrderItems产生.historyOrderItems产生(0,listSingelLoadItems,this,swipeLayout);
		}
	}

	@Override
	public void onRequestedLastItem() {
		if (!isEnd) {
			historyOrderItems产生.historyOrderItems产生(0, nowLastItemIndex +
					listSingelLoadItems, this,swipeLayout);
		}
	}

}
