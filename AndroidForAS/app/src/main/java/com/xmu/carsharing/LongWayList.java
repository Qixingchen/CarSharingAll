package com.xmu.carsharing;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.Tool.LongWayListAdapter;
import com.Tool.LongWayListItemClass;
import com.Tool.MaterialDrawer;
import com.Tool.ServerQues;

import java.security.cert.CertificateFactorySpi;


public class LongWayList extends ActionBarActivity implements ServerQues.GetLongWayAnsCallBack
,SwipeRefreshLayout.OnRefreshListener,LongWayListAdapter.OnViewHolderListener{

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

	//RecyclerView
	private RecyclerView mRecyclerView;
	private RecyclerView.LayoutManager mLayoutManager;
	private LongWayListAdapter longWayListAdapter;

	//判断是否到结尾
	private boolean isEnd = false;

	//列表数据
	private LongWayListItemClass longWayListItemClass;
	private int listSingelLoadItems = 20;

	//服务器查询
	private ServerQues serverQues;

	//用户属性
	private String userrole = "p";

	//菜单按钮
	private MenuItem IAmDriver,IAmPassagr;

	//下拉刷新
	private boolean isRefresh = false;
	private SwipeRefreshLayout swipeLayout;
	private int nowLastItemIndex = 0;


	private String logtag = "长途拼车列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_long_way_list);
		//actionbar
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		new MaterialDrawer(this, toolbar);
		//actionbar end

		//RecyclerView
		mRecyclerView = (RecyclerView) findViewById(R.id.LongWatListRecyclerView);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		//RecyclerView End

		//SwipeRefresh
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.LongwaySwipeToRefreash);
		swipeLayout.setOnRefreshListener(this);
		//SwipeRefresh END

		serverQues = new ServerQues(this);

	}

	@Override
	protected void onResume(){
		super.onResume();
		serverQues.longway查询(userrole,0,listSingelLoadItems,this,swipeLayout);
		nowLastItemIndex = listSingelLoadItems;

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_long_way_list, menu);
		IAmDriver = menu.findItem(R.id.LongWayLIst_I_Am_driver);
		IAmPassagr = menu.findItem(R.id.LongWayLIst_I_Am_Passage);
		if (IAmDriver == null || IAmPassagr == null){
			Log.e(logtag,"menuitem is null");
		}

		return true;
	}

	@Override
	public void getLongWayAnsCallBack(boolean isend, LongWayListItemClass longWayListItemClass) {
		this.isEnd = isend;
		this.longWayListItemClass = longWayListItemClass;
		//下拉刷新
		isRefresh = false;
		//下拉刷新 END
		//RecyclerView
		longWayListAdapter = new LongWayListAdapter(longWayListItemClass,this,this);
		mRecyclerView.setAdapter(longWayListAdapter);
		//RecyclerView End
	}

	//菜单项
	public void I_Am_driver(MenuItem item) {
		userrole = "d";
		IAmPassagr.setVisible(true);
		IAmDriver.setVisible(false);
		serverQues.longway查询(userrole,0,listSingelLoadItems,this,swipeLayout);
	}
	public void I_Am_Passage(MenuItem item) {
		userrole = "p";
		IAmDriver.setVisible(true);
		IAmPassagr.setVisible(false);
		serverQues.longway查询(userrole,0,listSingelLoadItems,this,swipeLayout);
	}

	@Override
	public void onRefresh() {
		if (!isRefresh){
			isRefresh = true;
			serverQues.longway查询(userrole,0,listSingelLoadItems,this,swipeLayout);
		}
	}

	@Override
	public void onRequestedLastItem() {
		if (!isEnd) {
			serverQues.longway查询(userrole, 0, nowLastItemIndex + listSingelLoadItems, this,swipeLayout);
		}
	}
}
