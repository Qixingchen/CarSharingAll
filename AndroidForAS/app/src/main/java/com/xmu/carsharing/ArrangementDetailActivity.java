/*订单详情界面，由listitem点击传值生成的界面
 * 所显示的订单在进入该界面时访问一遍服务器以刷新状态
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Tool.AppStat;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.util.Date;

public class ArrangementDetailActivity extends Activity {

	public static RequestQueue queue;
	private float SPX, SPY, DSX, DSY;
	private String carsharing_type, requesttime;
	private TextView sp, ep, st, rs, userrole, dealstatus;
	private String UserPhoneNumber;
	private String startDate, endDate, startTime, endTime, weekrepeat,
			mdealstatus;
	private int Date[];
	private ImageView fanhui;
	private Button reorder;
	private Date test_date = new Date();
	private String primary_short_startdate = null,
			primary_short_starttime = null, primary_short_endtime = null,
			primary_commute_startdate = null, primary_commute_starttime = null,
			primary_commute_endtime = null, primary_commute_enddate = null,
			primary_longway_startdate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrangement_detail);

		Context phonenumber = ArrangementDetailActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		queue = Volley.newRequestQueue(this);

		reorder = (Button) findViewById(R.id.arrangement_reorder);
		fanhui = (ImageView) findViewById(android.R.id.home);
		sp = (TextView) findViewById(R.id.arrangementdetail_startaddress);
		ep = (TextView) findViewById(R.id.arrangementdetail_endaddress);
		st = (TextView) findViewById(R.id.arrangementdetail_starttime);
		rs = (TextView) findViewById(R.id.arrangementdetail_remainsites);
		userrole = (TextView) findViewById(R.id.arrangementdetail_userrole);
		dealstatus = (TextView) findViewById(R.id.arrangementdetail_orderstatus);

		final String role;

		// actionbar操作!!
		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionbarEND!!

		Bundle bundle = this.getIntent().getExtras();
		sp.setText(bundle.getString("tsp")); // 起点
		ep.setText(bundle.getString("tep")); // 终点
		st.setText(bundle.getString("tst")); // 开始时间
		rs.setText(bundle.getString("trs")); // 需要座位
        startDate = bundle.getString("startdate");
		role = bundle.getString("userrole");
		mdealstatus = bundle.getString("dealstatus");
		if ((role.compareTo("p") == 0)
				|| (bundle.getString("userrole").compareTo("n") == 0)) {// 身份
			userrole.setText("乘客");
		} else {
			userrole.setText("司机");
		}
		if (mdealstatus.compareTo("0") == 0) {// 订单状态
			dealstatus.setText("服务器正在尽快为您匹配，请稍等！");
		} else if (mdealstatus.compareTo("1") == 0) {
			dealstatus.setText("订单已匹配，请查收！");
		} else if (mdealstatus.compareTo("2") == 0) {
			dealstatus.setText("长途拼车");
		}

		final String stp[] = bundle.getString("tsp").split(",");
		final String ep[] = bundle.getString("tep").split(",");


        carsharing_type = bundle.getString("carsharing_type");
        if(carsharing_type.compareTo("longway")!=0) {
            SPX = bundle.getFloat("SPX"); // 起点经度
            SPY = bundle.getFloat("SPY"); // 起点纬度
            DSX = bundle.getFloat("DSX"); // 终点经度
            DSY = bundle.getFloat("DSY"); // 终点纬度
            startTime = bundle.getString("starttime");
            endTime = bundle.getString("endtime");
            requesttime = bundle.getString("requesttime");

            if(carsharing_type.compareTo("commute") == 0){
                endDate = bundle.getString("enddate");
                weekrepeat = bundle.getString("weekrepeat");
            }
        }


		// actionbar中返回键监听

		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				ArrangementDetailActivity.this.finish();
			}
		});
		// end

		// reorder按钮的监听
		reorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (carsharing_type.compareTo("shortway") == 0) {
					Intent shortway = new Intent(
							ArrangementDetailActivity.this,
							OrderActivity.class);
					shortway.putExtra("cstype", "reshortcs");
					shortway.putExtra("stpusername", stp[0]);
					shortway.putExtra("stpmapname", stp[1]);
					shortway.putExtra("epusername", ep[0]);
					shortway.putExtra("epmapname", ep[1]);
					shortway.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					shortway.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					shortway.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					shortway.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					shortway.putExtra("userrole", role);
					shortway.putExtra("pre_page", "ReOrder");
					try {
						test_date = AppStat.time格式化.yyyy_MM_dd.parse(startDate);
						primary_short_startdate = AppStat.time格式化.yyyy年M月d日
								.format(test_date);
						test_date = AppStat.time格式化.HH_mm_ss.parse(startTime);
						primary_short_starttime = AppStat.time格式化.HH时mm分ss秒
								.format(test_date);
						test_date = AppStat.time格式化.HH_mm_ss.parse(endTime);
						primary_short_endtime = AppStat.time格式化.HH时mm分ss秒.format(test_date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					shortway.putExtra("re_short_startdate",
							primary_short_startdate);
					shortway.putExtra("re_short_starttime",
							primary_short_starttime);
					shortway.putExtra("re_short_endtime", primary_short_endtime);
					startActivity(shortway);
				} else if (carsharing_type.compareTo("commute") == 0) {
					Intent commute = new Intent(ArrangementDetailActivity.this,
							OrderActivity.class);
					commute.putExtra("cstype", "reworkcs");
					commute.putExtra("stpusername", stp[0]);
					commute.putExtra("stpmapname", stp[1]);
					commute.putExtra("epusername", ep[0]);
					commute.putExtra("epmapname", ep[1]);
					commute.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					commute.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					commute.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					commute.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					try {
						test_date = AppStat.time格式化.yyyy_MM_dd.parse(startDate);
						primary_commute_startdate = AppStat.time格式化.yyyy年M月d日
								.format(test_date);
						test_date = AppStat.time格式化.yyyy_MM_dd.parse(endDate);
						primary_commute_enddate = AppStat.time格式化.yyyy年M月d日
								.format(test_date);
						test_date = AppStat.time格式化.HH_mm_ss.parse(startTime);
						primary_commute_starttime = AppStat.time格式化.HH时mm分ss秒
								.format(test_date);
						test_date = AppStat.time格式化.HH_mm_ss.parse(endTime);
						primary_commute_endtime = AppStat.time格式化.HH时mm分ss秒
								.format(test_date);
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					commute.putExtra("re_commute_startdate",
							primary_commute_startdate);
					commute.putExtra("re_commute_enddate",
							primary_commute_enddate);
					commute.putExtra("re_commute_starttime",
							primary_commute_starttime);
					commute.putExtra("re_commute_endtime",
							primary_commute_endtime);
					commute.putExtra("weekrepeat", weekrepeat);
					commute.putExtra("userrole", role);
					commute.putExtra("pre_page", "ReOrder");
					startActivity(commute);
				} else if (carsharing_type.compareTo("longway") == 0) {
					Intent longway = new Intent(ArrangementDetailActivity.this,
							OrderActivity.class);
					longway.putExtra("cstype", "relongcs");
					longway.putExtra("stpmapname", stp[0]);
					longway.putExtra("epmapname", ep[0]);
					longway.putExtra("stpx", SPX);
					Log.e("SPX", String.valueOf(SPX));
					longway.putExtra("stpy", SPY);
					Log.e("SPY", String.valueOf(SPY));
					longway.putExtra("epx", DSX);
					Log.e("DSX", String.valueOf(DSX));
					longway.putExtra("epy", DSY);
					Log.e("DSY", String.valueOf(DSY));
					try {
						test_date = AppStat.time格式化.yyyy_MM_dd.parse(startDate);
						primary_longway_startdate = AppStat.time格式化.yyyy年M月d日
								.format(test_date);
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					longway.putExtra("re_longway_startdate",
							primary_longway_startdate);
					longway.putExtra("userrole", role);
					longway.putExtra("pre_page", "ReOrder");
					startActivity(longway);
				}
			}
		});

	}

}