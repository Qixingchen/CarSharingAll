package longwaylistfragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carsharing.LongWayArrangementDetail;
import com.example.carsharing.R;
import com.viewlist.XListView;
import com.viewlist.XListView.IXListViewListener;

public class Fragment2 extends Fragment implements IXListViewListener {
	private static List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	boolean over = false;
	private int pos = 0;
	public static RequestQueue queue;
	private XListView mListView;
	private SimpleAdapter mAdapter;
	// private ArrayAdapter<String> mAdapter;
	private String[] items = new String[] { "re_address", "detail" };
	private Handler mHandler;
	private int start = 0;
	private int refreshtime = 0;
	private String lastrefreshtime = "刚刚";

	// actionbar!!
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	View commute;
	View shortway;
	View longway;
	View taxi;
	View personalcenter;
	boolean isExit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_longway_list2, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		queue = Volley.newRequestQueue(this.getActivity());
		super.onActivityCreated(savedInstanceState);
		mListView = (XListView) getActivity().findViewById(R.id.xListView2);
		mListView.setPullLoadEnable(true);
		list.clear();
		lookuppublish("p", pos, pos + 10);
		pos = pos + 10;
		mAdapter = new SimpleAdapter(getActivity(), list,
				R.layout.choose_start_vlist, new String[] { "re_address",
						"detail" }, new int[] { R.id.re_address, R.id.detail });
		mListView.setAdapter(mAdapter);
		// mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		// mListView.setAdapter(mAdapter);
		// mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		mListView.startRefresh();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				String requesttime = Fragment2.list.get(position - 1).get(
						"requst");
				longwaylookup_intent("p", requesttime);

			}
		});

		// actionbar!!

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime((lastrefreshtime));
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日   HH:mm:ss     ");
		Date curDate = new Date(System.currentTimeMillis());
		lastrefreshtime = formatter.format(curDate);
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				list.clear();
				pos = 0;
				lookuppublish("p", pos, pos + 10);
				pos = pos + 10;
				mAdapter = new SimpleAdapter(getActivity(), list,
						R.layout.choose_start_vlist, new String[] {
								"re_address", "detail" }, new int[] {
								R.id.re_address, R.id.detail });
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				lookuppublish("p", pos, pos + 10);
				pos = pos + 10;
				mAdapter = new SimpleAdapter(getActivity(), list,
						R.layout.choose_start_vlist, new String[] {
								"re_address", "detail" }, new int[] {
								R.id.re_address, R.id.detail });
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	private void lookuppublish(final String role, final int start, final int end) {
		
		String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_LongwayPublish)
				+ getString(R.string.uri_lookuppublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("longway_lookuppublish_result", response);
						// json
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							if (end > jasA.length())
								over = true;
							else
								over = false;
							for (int i = 0; i < jasA.length(); i++) {
								if (i >= start && i < end) {

									jasitem = jasA.getJSONObject(i);
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("detail",
											jasitem.getString("startDate"));
									map.put("re_address",
											jasitem.getString("startPlace")
													+ "  "
													+ " 至 "
													+ jasitem
															.getString("destination")
													+ "  ");
									map.put("requst",
											jasitem.getString("publishTime"));
									list.add(map);
								}
							}
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("longway_lookuppublish_result",
								error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "网络连接失败",
								Toast.LENGTH_LONG);
						errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("role", role);
				return params;
			}
		};

		queue.add(stringRequest);

	}

	private void longwaylookup_intent(final String role, final String request) {
		
		String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_LongwayPublish)
				+ getString(R.string.uri_lookuppublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d("longway_lookuppublish_result", response);
						// json
						try {
							int i;
							Bundle bundle = new Bundle();
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								if (jasitem.getString("publishTime").equals(
										request)) {

									bundle.putString("carsharing_type",
											"longway");

									bundle.putString("userid",
											jasitem.getString("userId"));

									bundle.putString("requesttime", request);

									bundle.putString("tsp",
											jasitem.getString("startPlace"));
									bundle.putString("tep",
											jasitem.getString("destination"));
									bundle.putString("tst",
											jasitem.getString("startDate"));
									bundle.putString("trs", "xx");
									break;
								}
							}
							if (i == jasA.length()) {
								Toast.makeText(
										getActivity().getApplicationContext(),
										"该订单已不存在", Toast.LENGTH_SHORT).show();
							} else {
								Intent intent = new Intent(getActivity(),
										LongWayArrangementDetail.class);
								intent.putExtras(bundle);
								startActivity(intent);
							}
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("commute_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "网络连接失败",
						// Toast.LENGTH_LONG);
						// errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("role", role);
				return params;
			}
		};

		queue.add(stringRequest);

	}

}