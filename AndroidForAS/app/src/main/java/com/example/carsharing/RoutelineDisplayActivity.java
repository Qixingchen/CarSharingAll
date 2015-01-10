package com.example.carsharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOvelray;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RoutelineDisplayActivity extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

	private String userid;

	private int position;
	private RequestQueue queue;
	private boolean isFirstLoc = true;
	private ImageView fanhui;// actionbar id
	private ImageView routeline_dealsign;
	/*
	 * 订单详情id
	 */
	private ImageButton dialerButton;
	private TextView driverPhone;
	private TextView routeline_startPlace;
	private TextView routeline_endPlace;
	private TextView routeline_Via;
	private TextView routeline_passengerOrder;
	private TextView routeline_carInfo;
	private TextView routeline_otherInfo;
	private Button routeline_receive;
	private Button routeline_reject;
	private Button routeline_assess;

	MapView mapview = null;
	RoutePlanSearch mSearch = null;
	DrivingRouteLine route = null;
	BaiduMap mBaidumap = null;
	PlanNode stNode = null;
	PlanNode enNode = null;

	List<PlanNode> passnodelist = new ArrayList<PlanNode>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_routeline_display);

		routeline_startPlace = (TextView) findViewById(R.id.routeline_startplace);
		routeline_endPlace = (TextView) findViewById(R.id.routeline_endplace);
		routeline_Via = (TextView) findViewById(R.id.routeline_via);
		routeline_passengerOrder = (TextView) findViewById(R.id.routeline_passengerorder);
		routeline_carInfo = (TextView) findViewById(R.id.routeline_carinfo);
		routeline_otherInfo = (TextView) findViewById(R.id.routeline_otherinfo);
		routeline_dealsign = (ImageView) findViewById(R.id.routeline_dealsign);
		routeline_dealsign.setVisibility(View.GONE);

		routeline_assess = (Button) findViewById(R.id.routeline_btn_assess);
		routeline_assess.setVisibility(View.GONE);
		routeline_assess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				// Intent receiveIntent = new Intent(
				// RoutelineDisplayActivity.this,
				// PersonCenterDetaillistActivity.class);
				// receiveIntent.putExtra("deal_readstatus", "receive");
				// receiveIntent.putExtra("intent", 2);
				// RoutelineDisplayActivity.this.setResult(1, receiveIntent);
				// RoutelineDisplayActivity.this.finish();

			}
		});

		routeline_receive = (Button) findViewById(R.id.routeline_btn_receive);
		routeline_receive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				routeline_reject.setVisibility(View.GONE);
				routeline_receive.setVisibility(View.GONE);
				routeline_assess.setVisibility(View.VISIBLE);

				// Intent receiveIntent = new Intent(
				// RoutelineDisplayActivity.this,
				// PersonCenterDetaillistActivity.class);
				// receiveIntent.putExtra("deal_readstatus", "receive");
				// receiveIntent.putExtra("intent", 2);
				// RoutelineDisplayActivity.this.setResult(1, receiveIntent);
				// RoutelineDisplayActivity.this.finish();

			}
		});

		routeline_reject = (Button) findViewById(R.id.routeline_btn_reject);
		routeline_reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				routeline_reject.setVisibility(View.GONE);
				routeline_receive.setVisibility(View.GONE);
				routeline_dealsign.setVisibility(View.VISIBLE);

				// Intent rejectIntent = new Intent(
				// RoutelineDisplayActivity.this,
				// PersonCenterDetaillistActivity.class);
				// rejectIntent.putExtra("deal_readstatus", "reject");
				// rejectIntent.putExtra("intent", 2);
				// RoutelineDisplayActivity.this.setResult(1, rejectIntent);
				// RoutelineDisplayActivity.this.finish();
			}
		});

		Log.e("deal_readstatus",
				getIntent().getExtras().getString("deal_readstatus"));
		// 订单状态判断start！！
		if (getIntent().getExtras().getString("deal_readstatus")
				.compareTo("receive") == 0) {
			routeline_reject.setVisibility(View.GONE);
			routeline_receive.setVisibility(View.GONE);
			routeline_assess.setVisibility(View.VISIBLE);
			routeline_dealsign.setVisibility(View.GONE);
		} else if (getIntent().getExtras().getString("deal_readstatus")
				.compareTo("reject") == 0) {
			routeline_reject.setVisibility(View.GONE);
			routeline_receive.setVisibility(View.GONE);
			routeline_assess.setVisibility(View.GONE);
			routeline_dealsign.setVisibility(View.VISIBLE);
		} else if (getIntent().getExtras().getString("deal_readstatus")
				.compareTo("unread") == 0) {
			routeline_reject.setVisibility(View.VISIBLE);
			routeline_receive.setVisibility(View.VISIBLE);
			routeline_assess.setVisibility(View.GONE);
			routeline_dealsign.setVisibility(View.GONE);
		} else {
			routeline_reject.setVisibility(View.GONE);
			routeline_receive.setVisibility(View.GONE);
			routeline_assess.setVisibility(View.GONE);
			routeline_dealsign.setVisibility(View.GONE);
		}
		// 订单状态判断end！！

		/*
		 * Called when the activity is first created.
		 */
		driverPhone = (TextView) findViewById(R.id.routeline_driverphone);
		dialerButton = (ImageButton) findViewById(R.id.routeline_call);
		dialerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intentcall = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + driverPhone.getText().toString()));
				RoutelineDisplayActivity.this.startActivity(intentcall);
			}
		});

		// actionbar中返回键监听
		fanhui = (ImageView) findViewById(android.R.id.home);
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				RoutelineDisplayActivity.this.finish();
			}
		});

		mapview = (MapView) findViewById(R.id.routelinemap);
		mBaidumap = mapview.getMap();
		mBaidumap.setMyLocationEnabled(true);
		// 定位初始化
		LocationClient mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		queue = Volley.newRequestQueue(this);
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		getroutepoint();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mapview == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaidumap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void getroutepoint() {
		String getpoint_baseurl = "http://192.168.1.111:8080/CarsharingServer/CarShare!lookuprouteinfo.action?dealid="
				+ getIntent().getExtras().getString("dealid");
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				getpoint_baseurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("routepoint", response);
						JSONObject result = null;
						JSONObject points = null;
						try {
							result = new JSONObject(response);
							points = result.getJSONObject("result");
							stNode = PlanNode.withLocation(new LatLng(points
									.getDouble("startPlaceY"), points
									.getDouble("startPlaceX")));
							enNode = PlanNode.withLocation(new LatLng(points
									.getDouble("destinationY"), points
									.getDouble("destinationX")));
							// stNode = PlanNode.withLocation(new
							// LatLng(24.442553,118.122932));
							// enNode = PlanNode.withLocation(new
							// LatLng(24.444964,118.099909));
							getpassnode();
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("login_result", error.getMessage(), error);
						Toast.makeText(getApplicationContext(),
								getString(R.string.warningInfo_networkError),
								Toast.LENGTH_LONG).show();
					}
				});
		queue.add(stringRequest);
	}

	private void getpassnode() {
		String getpoint_baseurl = "http://192.168.1.111:8080/CarsharingServer/CarTake!selectpassnode.action?dealid="
				+ getIntent().getExtras().getString("dealid");
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				getpoint_baseurl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("passnodes", response);
						JSONObject result = null;
						JSONArray passnodes = null;
						try {
							String via = "";
							result = new JSONObject(response);
							passnodes = result.getJSONArray("result");
							JSONObject passnode = null;
							TextOptions textoverlay = null;
							for (int i = 0; i < passnodes.length(); i++) {
								passnode = (JSONObject) passnodes.get(i);
								// pyj start
								if (i == 0) {
									routeline_startPlace.setText(passnode
											.getString("posionX")
											+ ","
											+ passnode.getString("posionY"));
								} else if (i == passnodes.length() - 1) {
									routeline_endPlace.setText(passnode
											.getString("posionX")
											+ ","
											+ passnode.getString("posionY"));
									routeline_Via.setText(via);
								} else {
									via = via + "  "
											+ passnode.getString("posionX")
											+ ","
											+ passnode.getString("posionY");
								}
								if (userid == passnode.get("passengerId")
										|| userid == passnode.get("driverId")) {
									if (userid == passnode.get("passengerId"))
										routeline_passengerOrder
												.setText("passengerOrder");
									else
										routeline_passengerOrder
												.setVisibility(View.GONE);
								}
								// pyj end
								passnodelist.add(PlanNode
										.withLocation(new LatLng(passnode
												.getDouble("posionY"), passnode
												.getDouble("posionX"))));
								textoverlay = new TextOptions()
										.bgColor(0xAAFFFF00)
										.fontSize(32)
										.fontColor(0xFFFF00FF)
										.text(Integer.toString(i + 1))
										.rotate(0)
										.position(
												new LatLng(
														passnode.getDouble("posionY"),
														passnode.getDouble("posionX")));
								mBaidumap.addOverlay(textoverlay).setZIndex(
										BIND_IMPORTANT);
							}
							mSearch.drivingSearch((new DrivingRoutePlanOption())
									.from(stNode).passBy(passnodelist)
									.to(enNode));
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("login_result", error.getMessage(), error);
						Toast.makeText(getApplicationContext(),
								getString(R.string.warningInfo_networkError),
								Toast.LENGTH_LONG).show();

					}
				});
		queue.add(stringRequest);
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mapview.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapview.onPause();
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			DrivingRouteOvelray overlay = new DrivingRouteOvelray(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getApplicationContext(), result.error.toString(),
					Toast.LENGTH_SHORT).show();

		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			result.getSuggestAddrInfo();
			return;
		}
	}

	private void selectcarinfo(final String phonenum) {
		
		String carinfo_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo)
				+ getString(R.string.uri_selectcarinfo_action);

		// Log.d("carinfo_selectrequest_baseurl",carinfo_selectrequest_baseurl);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						Log.e("carinfo_selectresult_result", response);
						String jas_id = null;
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							JSONObject json = json1.getJSONObject("result");
							jas_id = json.getString("id");
							Log.d("jas_id", jas_id);

						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						Log.e("carinfo_selectresult_result",
								error.getMessage(), error);
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				return params;
			}
		};

		queue.add(stringRequest);
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		

	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

}
