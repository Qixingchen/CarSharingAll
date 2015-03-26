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
    public String carsharing_type,tsp,tep,tst,
            startdate,enddate,starttime,endtime,
            trs,dealstatus,userrole,weekrepeat;
    //tsp:startPlace's name,tep:endPlace's name,tst?
    public float SPX,SPY,DSX,DSY;

    public boolean bfirsthistory = false;
    public boolean loadok = false;
    public String firstItem_type; //首页显示的历史订单的类型
    public String startplace[];
    public String endplace[];
    public static ArrayList<HashMap<String, String>> mylist1_0 = new ArrayList<HashMap<String, String>>();

    RequestQueue queue;

	//回调函数
	private GetordersCallBack getordersCallBack;


    public OrderReleasing(Activity act){
        this.activity = act;
    }

    private void longway_selectrequest(final String phonenum,
                                      final String request,final String act
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
                                if(act.compareTo("PersonCenterDetaillist") == 0) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        Log.e(Logtag+"jasitemtime",
		                                        jasitem.getString("requestTime"));
                                        Log.e(Logtag+"request",request);
                                        if (jasitem.getString("publishTime").equals(
                                                request)) {
	                                        //todo carsharing_type似乎无意义
                                            carsharing_type = "longway";
                                            tsp = jasitem.getString("startPlace");
                                            tep = jasitem.getString("destination");
                                            tst = jasitem.getString("startDate");
                                            trs = "xx";
                                            userrole = jasitem.getString("userRole");
                                            startdate = jasitem.getString("startDate");
                                            dealstatus = "2";

                                            break;
                                        }
                                    }
                                }

                    /*------------PersonalCenterActivity.java---start--------------------*/
                                else if(act.compareTo("PersonalCenter") == 0) {
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
                                            String startplace[] = jasitem.getString(
                                                    "startPlace").split(",");
                                            String endplace[] = jasitem.getString(
                                                    "destination").split(",");

                                            bfirsthistory = true;
                                        }
                                    }
                                }
                            else i = 0;  //do nothing..
                    /*------------PersonalCenterActivity.java---end-----------------------*/

                            if (i == jasA.length()&&act.compareTo("PersonCenterDetaillist") == 0) {
                                Toast.makeText(activity.getApplicationContext(),
                                        "该订单已不存在", Toast.LENGTH_SHORT).show();
                            }
                            loadok = true;
	                        getordersCallBack.getordersCallBack();
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
                                       final String request,final String act) {

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

                 //       if(act.compareTo("PersonCenterDetaillist") == 0) {
                            for (i = 0; i < jasA.length(); i++) {
                                jasitem = jasA.getJSONObject(i);
                                Log.e("jasitemtime",jasitem.getString("requestTime"));
                                Log.e("request",request);
                                if (jasitem.getString("requestTime").equals(
                                        request)) {

                                    carsharing_type = "commute";

                                    SPX = Float
                                            .parseFloat(jasitem
                                                    .getString("startPlaceX"));
                                    SPY = Float
                                            .parseFloat(jasitem
                                                    .getString("startPlaceY"));
                                    DSX = Float
                                            .parseFloat(jasitem
                                                    .getString("destinationX"));
                                    DSY = Float
                                            .parseFloat(jasitem
                                                    .getString("destinationY"));
                                    tsp = jasitem.getString("startPlace");
                                    tep = jasitem.getString("destination");
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

                                    startdate = jasitem.getString("startDate");
                                    enddate = jasitem.getString("endDate");
                                    starttime = jasitem.getString("startTime");
                                    endtime = jasitem.getString("endTime");
                                    weekrepeat = jasitem.getString("weekRepeat");
                                    trs = "xx";
                                    dealstatus = jasitem.getString("dealStatus");
                                    userrole = jasitem.getString("supplyCar");

                                    break;
                                }
                            }
                  //      }

                        /*------------PersonalCenterActivity.java---start--------------------*/
                         if(act.compareTo("PersonalCenter") == 0) {
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
                        }
                    /*------------PersonalCenterActivity.java---end-----------------------*/

                  //  else i=0; //do nothing..

                    if (i == jasA.length()) {
                        longway_selectrequest(phonenum, request,act);
                    }
                    loadok = true;
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
                                        final String request,final String act) {

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
                            int i;
                            JSONObject jasitem;
                            JSONObject jas = new JSONObject(response);
                            JSONArray jasA = jas.getJSONArray("result");

                    /*------------PersonCenterDetaillistActivity.java---start--------------*/
                             //   if(act.compareTo("PersonCenterDetaillist") == 0) {
                                    for (i = 0; i < jasA.length(); i++) {
                                        jasitem = jasA.getJSONObject(i);
                                        Log.e("jasitemtime",jasitem.getString("requestTime"));
                                        Log.e("request",request);
                                        if (jasitem.getString("requestTime").equals(
                                                request)) {

                                            carsharing_type = "shortway";
                                            SPX = Float
                                                    .parseFloat(jasitem
                                                            .getString("startPlaceX"));
                                            SPY = Float
                                                    .parseFloat(jasitem
                                                            .getString("startPlaceY"));
                                            DSX = Float
                                                    .parseFloat(jasitem
                                                            .getString("destinationX"));
                                            DSY = Float
                                                    .parseFloat(jasitem
                                                            .getString("destinationY"));
                                            tsp = jasitem.getString("startPlace");
                                            tep = jasitem.getString("destination");
                                            tst = jasitem.getString("startDate")
                                                    + " "
                                                    + jasitem
                                                    .getString("startTime")
                                                    + "-"
                                                    + jasitem
                                                    .getString("endTime");
                                            startdate = jasitem.getString("startDate");
                                            starttime = jasitem.getString("startTime");
                                            endtime = jasitem.getString("endTime");
                                            trs = "xx";
                                            dealstatus = jasitem.getString("dealStatus");
                                            userrole = jasitem.getString("userRole");
                                            break;
                                        }
                                    }
                            //    }
                    /*------------PersonCenterDetaillistActivity.java---end--------------*/

                    /*------------PersonalCenterActivity.java---start--------------------*/
                                 if(act.compareTo("PersonalCenter") == 0) {
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
                                        Log.e("bfirsthistory_order", String.valueOf(bfirsthistory));
                                    }
                                }
                    /*------------PersonalCenterActivity.java---end-----------------------*/

                          //  else i = 0; //do nothing..

                            if (i == jasA.length()) {
                                commute_selectrequest(phonenum, request,act);
                            }
                            loadok = true;
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

    public void orders(String UserPhoneNumber, String requesttime,final String act,
                       GetordersCallBack getordersCB){

        queue = Volley.newRequestQueue(activity);
        mylist1_0.clear();
	    getordersCallBack = getordersCB;
        shortway_selectrequest(UserPhoneNumber, requesttime,act);

    }

	//结果回调函数
	public interface GetordersCallBack{
		public void getordersCallBack();
	}

}
