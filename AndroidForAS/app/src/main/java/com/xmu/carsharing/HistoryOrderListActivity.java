package com.xmu.carsharing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Tool.DataBaseAct;
import com.Tool.HistoryOrderItems产生;
import com.Tool.HistoryOrderListAdapter;
import com.Tool.HistoryOrderListItemClass;
import com.Tool.MaterialDrawer;
import com.Tool.ToolWithActivityIn;

public class HistoryOrderListActivity extends ActionBarActivity implements
		HistoryOrderItems产生.GetHistoryOrderAnsCallBack,
		SwipeRefreshLayout.OnRefreshListener,
		HistoryOrderListAdapter.OnViewHolderListener{

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

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

	//数据库操作
	DataBaseAct dbact;
	private String UserPhoneNumber;
	private ToolWithActivityIn getPhone;


	private Context mcontext;

	//下拉刷新
	private boolean isRefresh = false;
	private SwipeRefreshLayout swipeLayout;
	private int nowLastItemIndex = 0;

	private String logtag = "历史订单列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order_list);

		//actionbar
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		//actionbar end

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
		dbact = new DataBaseAct(this,UserPhoneNumber);
		getPhone = new ToolWithActivityIn(this);
		UserPhoneNumber = getPhone.get用户手机号从偏好文件();

		mcontext = getApplicationContext();

	}

	@Override
	protected void onResume(){
		super.onResume();
		historyOrderItems产生.historyOrderItems产生(0, listSingelLoadItems,this,swipeLayout);
		nowLastItemIndex = listSingelLoadItems;
	}

	@Override
	public void getHistoryOrderAnsCallBack(boolean isend,final HistoryOrderListItemClass
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
		historyOrderListAdapter.setOnItemClickListener(new HistoryOrderListAdapter.OnItemClickListener() {

			@Override
			public void onItemClickListener(View view, int position) {
				Toast.makeText(getApplicationContext(),
						"detail被点击"+position,
						Toast.LENGTH_SHORT).show();

					/*从数据库中查找requesttime与之对应的条目*/
				String requesttime = historyOrderListItemClass
						.HistoryOrderListItems[position].Requesttime;
				Log.e(logtag,"下单时间"+requesttime);
				Cursor dbresult =  dbact.read某条历史订单(requesttime);
				bundle向详情界面传值(dbresult,requesttime);
			}
		});
		//RecyclerView End
	}

	private void bundle向详情界面传值(Cursor dbresult,String requesttime)
	{
		String date_时间日期组合;

		Bundle bundle = new Bundle();
		Intent intent = new Intent(HistoryOrderListActivity.this,
				ArrangementDetailActivity.class);

		if(dbresult.getCount() == 0) return;

		dbresult.moveToFirst();

		String carsharing_type = dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Carsharing_type)));

		bundle.putString("carsharing_type", carsharing_type);
		bundle.putString("startplace", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_StartplaceName))));
		bundle.putString("endplace", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_EndplaceName))));
		bundle.putString("startdate", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Startdate))));
		bundle.putString("restseats", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Restseats))));
		bundle.putString("userrole", dbresult.getString(dbresult.getColumnIndex
				(mcontext.getString(R.string.dbstring_Userrole))));

		if (carsharing_type.compareTo("longway") != 0) {

			bundle.putString("requesttime", requesttime);
			bundle.putFloat("startplaceX", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_StartplaceX))));
			bundle.putFloat("startplaceY", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_StartplaceY))));
			bundle.putFloat("endplaceX", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_EndplaceX))));
			bundle.putFloat("endplaceY", dbresult.getFloat(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_EndplaceY))));
			bundle.putString("starttime", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Starttime))));
			bundle.putString("endtime", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Endtime))));
			bundle.putString("dealstatus", dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Dealstatus))));


			if(carsharing_type.compareTo("shortway") == 0){

				date_时间日期组合 =  dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Startdate)))
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Starttime))); //出发时间
				bundle.putString("date_时间日期组合",date_时间日期组合);

			}
			else if (carsharing_type.compareTo("commute") == 0) {

				bundle.putString("enddate", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Enddate))));
				date_时间日期组合 = dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Startdate)))
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Starttime)))
						+ " "
						+ "每周"
						+ " "
						+ dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Weekrepeat)));
				bundle.putString("date_时间日期组合", date_时间日期组合 );
				bundle.putString("enddate", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Enddate))));
				bundle.putString("weekrepeat", dbresult.getString(dbresult.getColumnIndex
						(mcontext.getString(R.string.dbstring_Weekrepeat))));

			}
		}
		else{ //longway
			date_时间日期组合 =  dbresult.getString(dbresult.getColumnIndex
					(mcontext.getString(R.string.dbstring_Startdate)))
					+ " "; //出发时间
			bundle.putString("date_时间日期组合",date_时间日期组合);
		}

		intent.putExtras(bundle);
		startActivity(intent);
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
