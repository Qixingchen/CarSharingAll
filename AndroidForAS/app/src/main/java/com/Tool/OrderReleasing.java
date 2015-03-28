package com.Tool;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xmu.carsharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * “我发布过的订单”
 * 有两个activity在调用：个人中心和个人中心详情界面
 * Created by Jo on 2015/3/24.
 */
public class OrderReleasing {

	//todo 调整公开性
	private Activity activity;
	private String Logtag = "历史订单界面";
	//todo 修改无法理解的简称
/*    public String carsharing_type,tsp,tep,tst,
            startdate,enddate,starttime,endtime,
            trs,dealstatus,userrole,weekrepeat;*/
    //tsp:startPlace's name,tep:endPlace's name,trs:剩余容量
	private static float[] longitude_latitude = new float[4];
	//数组按序为：startplaceX,startplaceY,endplaceX,endplaceY

	private static String[] date_time = new String[4];
	//数组按序为：startdate,enddate,starttime,endtime（没有就赋值为NULL）

	private static String[] place_name = new String[2];
	//数组按序为：startplace's name ,endplace's name

	private String carsharing_type,dealstatus,userrole,weekrepeat = "",tst = "",
	rest_seats = "xx"; //tst?

    private boolean bfirsthistory = false;
	public boolean loadok = false;
    private String firstItem_type; //首页显示的历史订单的类型
	private String[] startplace ;
	private String[] endplace;
    private static ArrayList<HashMap<String, String>> mylist1_0 = new
			ArrayList<HashMap<String, String>>();

    RequestQueue queue;

	//回调函数
	private GetordersCallBack getordersCallBack;
	private GetordersCallBack getorders_personalcenter;


    public OrderReleasing(Activity act){
        this.activity = act;
    }

    private void longway_selectrequest(final String phonenum,
                                      final String request,final int act
                                      ) {

        String longwayway_selectpublish_baseurl = activity.getString(R.string.uri_base)
                + activity.getString(R.string.uri_LongwayPublish)
                + activity.getString(R.string.uri_selectpublish_action);

        // "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                longwayway_selectpublish_baseurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.w(Logtag+"长途", response);
                        try {
                            int i;
                            Bundle bundle = new Bundle();
                            JSONObject jasitem = null;
                            JSONObject jas = new JSONObject(response);
                            JSONArray jasA = jas.getJSONArray("result");
	                        //todo 分拆函数
                                if(act == AppStat.is个人中心Or详情界面.详情界面) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        Log.e(Logtag+"jasitemtime",
		                                        jasitem.getString("requestTime"));
                                        Log.e(Logtag+"request",request);
                                        if (jasitem.getString("publishTime").equals(
                                                request)) {

                                            carsharing_type = "longway";
                                            place_name[0] = jasitem.getString
		                                            ("startPlace");
                                            place_name[1] = jasitem.getString
		                                            ("destination");
                                            date_time[0] = jasitem.getString("startDate");
                                            userrole = jasitem.getString("userRole");
                                            dealstatus = "2";
                                            break;
                                        }
                                    }
	                                if (i == jasA.length()&&act == AppStat.is个人中心Or详情界面
			                                .详情界面) {
		                                Toast.makeText(activity.getApplicationContext(),
				                                "该订单已不存在", Toast.LENGTH_SHORT).show();
	                                }
	                                else { //已经监听到点击的Item，可以开始回调了
		                                getordersCallBack.getordersCallBack
				                                (longitude_latitude, place_name, date_time,
						                                carsharing_type, dealstatus,
						                                userrole, weekrepeat, tst, rest_seats);
	                                }
                                }

                    /*------------PersonalCenterActivity.java---start--------------------*/
                                else if(act == AppStat.is个人中心Or详情界面.个人中心) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("Title", jasitem.getString("startDate"));
                                        map.put("text",
                                                jasitem.getString("startPlace")
                                                        + "  "
                                                        + " 至 "
                                                        + "  "
                                                        + jasitem
                                                        .getString("destination")
                                                        + "  ");
                                        map.put("requst",
                                                jasitem.getString("publishTime"));
                                        mylist1_0.add(map);
                                        if (bfirsthistory == false) {

                                            firstItem_type = "longway";
                                            startplace = jasitem.getString(
                                                    "startPlace").split(",");
                                            endplace = jasitem.getString(
                                                    "destination").split(",");

                                            bfirsthistory = true;
                                        }
                                    }

	                                loadok = true;
	                                if(bfirsthistory == true)
		                                Log.e("历史订单第一条记录:","longwayyes");
	                                else
		                                Log.e("历史订单第一条记录:","longwayno");

	                                getorders_personalcenter.getorders_personalcenter
			                                (mylist1_0,firstItem_type,startplace,
					                                endplace,bfirsthistory);
                                }

                    /*------------PersonalCenterActivity.java---end-----------------------*/

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Logtag+"长途",
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

    private void commute_selectrequest(final String phonenum,
                                       final String request,final int act) {

        String commute_selectrequest_baseurl = activity.getString(R.string.uri_base)
                + activity.getString(R.string.uri_CommuteRequest)
                + activity.getString(R.string.uri_selectrequest_action);
        // "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!selectrequest.action?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                commute_selectrequest_baseurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.w(Logtag+"上下班", response);
                try {
                    int i;
                    Bundle bundle = new Bundle();
                    JSONObject jasitem = null;
                    JSONObject jas = new JSONObject(response);
                    JSONArray jasA = jas.getJSONArray("result");

                        if(act == AppStat.is个人中心Or详情界面.详情界面) {
                            for (i = 0; i < jasA.length(); i++) {
                                jasitem = jasA.getJSONObject(i);
                                Log.e("jasitemtime",jasitem.getString("requestTime"));
                                Log.e("request",request);
                                if (jasitem.getString("requestTime").equals(
                                        request)) {

                                    carsharing_type = "commute";

                                    longitude_latitude[0] = Float
                                            .parseFloat(jasitem
                                                    .getString("startPlaceX"));
	                                longitude_latitude[1] = Float
                                            .parseFloat(jasitem
                                                    .getString("startPlaceY"));
	                                longitude_latitude[2] = Float
                                            .parseFloat(jasitem
                                                    .getString("destinationX"));
	                                longitude_latitude[3] = Float
                                            .parseFloat(jasitem
                                                    .getString("destinationY"));
                                    place_name[0] = jasitem.getString("startPlace");
                                    place_name[1] = jasitem.getString("destination");
                                    tst = jasitem.getString("startDate")
                                            + "至"
                                            + jasitem
                                            .getString("endDate")
                                            + "  "
                                            + jasitem
                                            .getString("startTime")
                                            + "-"
                                            + jasitem
                                            .getString("endTime")
                                            + "  "
                                            + "每周:"
                                            + jasitem
                                            .getString("weekRepeat");

                                    date_time[0] = jasitem.getString("startDate");
                                    date_time[1] = jasitem.getString("endDate");
                                    date_time[2] = jasitem.getString("startTime");
                                    date_time[3] = jasitem.getString("endTime");
                                    weekrepeat = jasitem.getString("weekRepeat");
                                    dealstatus = jasitem.getString("dealStatus");
                                    userrole = jasitem.getString("supplyCar");

                                    break;
                                }
                            }
	                        if (i == jasA.length()) {
		                        longway_selectrequest(phonenum, request,act);
	                        }
	                        else { //已经监听到点击的Item，可以开始回调了
		                        getordersCallBack.getordersCallBack
				                        (longitude_latitude, place_name, date_time,
						                        carsharing_type, dealstatus,
						                        userrole, weekrepeat, tst, rest_seats);
	                        }
                        }

                        /*------------PersonalCenterActivity.java---start--------------------*/
                         if(act == AppStat.is个人中心Or详情界面.个人中心) {
                            for (i = 0; i < jasA.length(); i++) {
                                jasitem = jasA.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("Title",
                                        jasitem.getString("requestTime")
                                                + " 每周"
                                                + jasitem
                                                .getString("weekRepeat"));
                                map.put("text",
                                        jasitem.getString("startPlace")
                                                + "  "
                                                + " 至 "
                                                + jasitem
                                                .getString("destination")
                                                + "  ");
                                map.put("requst",
                                        jasitem.getString("requestTime"));
                                mylist1_0.add(map);
                                if (bfirsthistory == false) {

                                    firstItem_type = "commute";
                                    startplace = jasitem.getString(
                                            "startPlace").split(",");
                                    endplace = jasitem.getString(
                                            "destination").split(",");

                                    bfirsthistory = true;
                                }
                            }
	                         if(bfirsthistory == true)
		                         Log.e("历史订单第一条记录:","commutewayyes");
	                         else
		                         Log.e("历史订单第一条记录:","commutewayno");

	                         longway_selectrequest(phonenum, request,act);
                        }
                    /*------------PersonalCenterActivity.java---end-----------------------*/

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Logtag+"上下班",
                        error.getMessage(), error);
                // Toast errorinfo = Toast.makeText(null, "网络连接失败",
                // Toast.LENGTH_LONG);
                // errorinfo.show();
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

    private void shortway_selectrequest(final String phonenum,
                                        final String request,final int act) {

        String shortway_selectrequest_baseurl = activity.getString(R.string.uri_base)
                + activity.getString(R.string.uri_ShortwayRequest)
                + activity.getString(R.string.uri_selectrequest_action);
        // "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                shortway_selectrequest_baseurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("selectrequest_result", response);
                        try {
                            int i = 0;
                            JSONObject jasitem;
                            JSONObject jas = new JSONObject(response);
                            JSONArray jasA = jas.getJSONArray("result");

                    /*------------PersonCenterDetaillistActivity.java---start--------------*/
                                if(act == AppStat.is个人中心Or详情界面.详情界面) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        Log.e("jasitemtime",jasitem.getString("requestTime"));
                                        Log.e("request",request);
                                        if (jasitem.getString("requestTime").equals(
                                                request)) {

                                            carsharing_type = "shortway";
	                                        longitude_latitude [0] = Float
			                                        .parseFloat(jasitem
					                                        .getString("startPlaceX"));
	                                        longitude_latitude [1] = Float
                                                    .parseFloat(jasitem
                                                            .getString("startPlaceY"));
	                                        longitude_latitude [2] = Float
                                                    .parseFloat(jasitem
                                                            .getString("destinationX"));
	                                        longitude_latitude [3] = Float
                                                    .parseFloat(jasitem
                                                            .getString("destinationY"));
                                            place_name [0] = jasitem.getString
		                                            ("startPlace");
                                            place_name [1] = jasitem.getString
		                                            ("destination");
                                            tst = jasitem.getString("startDate")
                                                    + " "
                                                    + jasitem
                                                    .getString("startTime")
                                                    + "-"
                                                    + jasitem
                                                    .getString("endTime");
                                            date_time [0] = jasitem.getString
		                                            ("startDate");
	                                        date_time [1] = "";
                                            date_time [2] = jasitem.getString
		                                            ("startTime");
                                            date_time [3] = jasitem.getString
		                                            ("endTime");
                                            dealstatus = jasitem.getString("dealStatus");
                                            userrole = jasitem.getString("userRole");
	                                        break;
                                        }
                                    }
	                                if (i == jasA.length()) {
		                                commute_selectrequest(phonenum, request,act);
	                                }
	                                else { //已经监听到点击的Item，可以开始回调了
		                                getordersCallBack.getordersCallBack
				                                (longitude_latitude, place_name, date_time,
						                                carsharing_type, dealstatus,
						                                userrole, weekrepeat, tst, rest_seats);
	                                }
                                }
                    /*------------PersonCenterDetaillistActivity.java---end--------------*/

                    /*------------PersonalCenterActivity.java---start--------------------*/
                                 if(act == AppStat.is个人中心Or详情界面.个人中心) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("Title", jasitem.getString("startDate"));
                                        map.put("text",
                                                jasitem.getString("startPlace")
                                                        + "  "
                                                        + " 至  "
                                                        + "  "
                                                        + jasitem
                                                        .getString("destination")
                                                        + "  ");
                                        map.put("requst",
                                                jasitem.getString("requestTime"));
                                        mylist1_0.add(map);
                                        if (bfirsthistory == false) {

                                            firstItem_type = "shortway";
                                            startplace = jasitem.getString(
                                                    "startPlace").split(",");
                                            endplace = jasitem.getString(
                                                    "destination").split(",");

                                            bfirsthistory = true;
                                        }

                                    }
	                                 if(bfirsthistory == true)
		                                 Log.e("历史订单第一条记录:","shortwayyes");
	                                 else
		                                 Log.e("历史订单第一条记录:","shortwayno");

	                                 Log.e("bfirsthistory_order", String.valueOf(bfirsthistory));
	                                 commute_selectrequest(phonenum, request,act);
                                }
                    /*------------PersonalCenterActivity.java---end-----------------------*/


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Logtag+"短途",
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

    public void orders(String UserPhoneNumber, String requesttime,final int act,
                       GetordersCallBack getordersCB){

        queue = Volley.newRequestQueue(activity);
        mylist1_0.clear();
	    if(act == AppStat.is个人中心Or详情界面.详情界面) {
		    getordersCallBack = getordersCB;
	    }
	    else if(act == AppStat.is个人中心Or详情界面.个人中心){
		    getorders_personalcenter = getordersCB;
	    }
        shortway_selectrequest(UserPhoneNumber, requesttime,act);

    }

	//结果回调函数
	public interface GetordersCallBack{
		public void getordersCallBack(float longitude_latitude[],String place_name[],
		                              String date_time[],String carsharing_type,
		                              String dealstatus,String userrole,String weekrepeat,
		                              String tst,String rest_seats);
		public void getorders_personalcenter(ArrayList mylist1_0,String firstItem_type,
		                                     String startplace[],
		                                     String endplace[],boolean bfirsthistory);
	}

}
