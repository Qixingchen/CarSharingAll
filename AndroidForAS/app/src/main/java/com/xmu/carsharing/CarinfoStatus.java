package com.xmu.carsharing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆表状态查询函数。检查服务器上是否已经存在用户车辆信息
 * 以及获取用户的车辆信息
 * 若未存在，carinfochoosing_type = 1,否则等于2
 * Created by Jo on 2015/3/9.
 */
public class CarinfoStatus {

    private int viewid;
    private Context context;
    private Activity activity;
    public int carinfochoosing_type;
    public static RequestQueue queue;
    //...other attributes

    public CarinfoStatus(Activity act,int id,Context context){
        this.activity = act;
        this.viewid = id;
        this.context = context;
    }
    public void selectcarinfo(final String UserPhoneNumber){

        queue = Volley.newRequestQueue(activity);
        String carinfo_selectrequest_baseurl = activity.getString(R.string.uri_base)
                + activity.getString(R.string.uri_CarInfo)
                + activity.getString(R.string.uri_selectcarinfo_action);

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

                    //对车辆表是否存在的判断start!!
                    jas_id = json.getString("id");
                    Log.d("jas_id", jas_id);

                    if (jas_id.compareTo("") == 0) {
                        carinfochoosing_type = 1;
                        Log.e("carinfochoosing_type", "1");
                    } else {
                        carinfochoosing_type = 2;
                        Log.e("carinfochoosing_type", "2");
                    }
                    //对车辆表是否存在的判断end!!

                    //把服务器上的车辆表信息写在本地start!!
                    SharedPreferences sharedPref = context
                            .getSharedPreferences(UserPhoneNumber,
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("refreshdescription", json
                            .getString("carBrand").toString());
                    editor.putString("refreshmodel",
                            json.getString("carModel"));
                    editor.putString("refreshcolor",
                            json.getString("carColor"));
                    editor.putString("refreshnum",
                            json.getString("carNum"));
                    editor.commit();
                    //把服务器上的车辆表信息写在本地end!!

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
                params.put("phonenum", UserPhoneNumber);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
