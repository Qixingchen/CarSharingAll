package com.Tool;

import android.app.Activity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xmu.carsharing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * radiobutton “乘客司机身份选择”
 * "我能提供车"不变，"我不能提供车"使车牌号等编辑框不可编辑，并更改textView
 * Created by Jo on 2015/3/8.
 */
public class IdentityBtn {

    private int viewid;
    private Activity activity;
    public int carinfochoosing_type;
    //  private boolean bcarbrand, bmodel, blicensenum, bcolor;/*品牌.型号.车牌号.颜色*/
    public boolean bpassenager, bdriver;
    private EditText carbrand;
    private EditText model;
    private EditText color;
    private EditText licensenum;
    private RadioButton mRadio1, mRadio2;
    private TextView content;
    private RadioGroup group;
    public static RequestQueue queue;

    public IdentityBtn(Activity act, int id) {
        this.activity = act;
        this.viewid = id;
    }

    public boolean IdentityChoosing(String SharingType, final String UserPhoneNumber) {

        queue = Volley.newRequestQueue(activity);

        if (SharingType.compareTo("commute") == 0) {

            carbrand = (EditText) activity.findViewById(R.id.commute_CarBrand);
            model = (EditText) activity.findViewById(R.id.commute_CarModel);
            color = (EditText) activity.findViewById(R.id.commute_color);
            licensenum = (EditText) activity.findViewById(R.id.commute_Num);
            mRadio1 = (RadioButton) activity.findViewById(R.id.commute_radioButton1);
            mRadio2 = (RadioButton) activity.findViewById(R.id.commute_radioButton2);// 绑定一个RadioGroup监听器
            content = (TextView) activity.findViewById(R.id.commute_content);
            group = (RadioGroup) activity.findViewById(R.id.commute_radiobutton);

        } else if (SharingType.compareTo("shortway") == 0) {

            carbrand = (EditText) activity.findViewById(R.id.shortway_CarBrand);
            model = (EditText) activity.findViewById(R.id.shortway_CarModel);
            color = (EditText) activity.findViewById(R.id.shortway_color);
            licensenum = (EditText) activity.findViewById(R.id.shortway_Num);
            mRadio1 = (RadioButton) activity.findViewById(R.id.shortway_radioButton1);
            mRadio2 = (RadioButton) activity.findViewById(R.id.shortway_radioButton2);// 绑定一个RadioGroup监听器
            content = (TextView) activity.findViewById(R.id.shortway_content);
            group = (RadioGroup) activity.findViewById(R.id.shortway_radiobutton01);

        } else {

            carbrand = (EditText) activity.findViewById(R.id.longway_CarBrand);
            model = (EditText) activity.findViewById(R.id.longway_CarModel);
            color = (EditText) activity.findViewById(R.id.longway_color);
            licensenum = (EditText) activity.findViewById(R.id.longway_Num);
            mRadio1 = (RadioButton) activity.findViewById(R.id.longway_radioButton01);
            mRadio2 = (RadioButton) activity.findViewById(R.id.longway_radioButton02);// 绑定一个RadioGroup监听器
            content = (TextView) activity.findViewById(R.id.longway_content);
            group = (RadioGroup) activity.findViewById(R.id.longway_radiobutton);

        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int checkedId) {

                // 获取变更后的选中项的ID

                // "我能提供车"不变，"我不能提供车"使车牌号等编辑框不可编辑，并更改textView
                if (checkedId == mRadio2.getId()) {

                    bpassenager = true;
                    bdriver = false;

                    licensenum.setEnabled(false);
                    carbrand.setEnabled(false);
                    color.setEnabled(false);
                    model.setEnabled(false);

                    licensenum.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest,
                                                   int dstart, int dend) {
                            return source.length() < 1 ? dest
                                    .subSequence(dstart, dend) : "";
                        }
                    }});
                    carbrand.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {
                            return source.length() < 1 ? dest.subSequence(
                                    dstart, dend) : "";
                        }
                    }});
                    color.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {
                            return source.length() < 1 ? dest.subSequence(
                                    dstart, dend) : "";
                        }
                    }});
                    model.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {
                            return source.length() < 1 ? dest.subSequence(
                                    dstart, dend) : "";
                        }
                    }});
                    content.setText(activity.getString(R.string.warningInfo_seatNeed));
                    licensenum.setHintTextColor(activity.getResources().getColor(
                            R.color.gray_cccccc));
                    carbrand.setHintTextColor(activity.getResources().getColor(
                            R.color.gray_cccccc));
                    color.setHintTextColor(activity.getResources().getColor(
                            R.color.gray_cccccc));
                    model.setHintTextColor(activity.getResources().getColor(
                            R.color.gray_cccccc));
                    licensenum.setInputType(InputType.TYPE_NULL);
                    carbrand.setInputType(InputType.TYPE_NULL);
                    color.setInputType(InputType.TYPE_NULL);
                    model.setInputType(InputType.TYPE_NULL);
                } else {

                    bpassenager = false;
                    bdriver = true;

                    licensenum.setEnabled(true);
                    carbrand.setEnabled(true);
                    color.setEnabled(true);
                    model.setEnabled(true);

                    licensenum
                            .setFilters(new InputFilter[]{new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source,
                                                           int start, int end, Spanned dest,
                                                           int dstart, int dend) {

                                    return null;
                                }
                            }});
                    carbrand.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {
                            return null;
                        }
                    }});
                    color.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {

                            return null;
                        }
                    }});
                    model.setFilters(new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest, int dstart,
                                                   int dend) {

                            return null;
                        }
                    }});
                    content.setText(activity.getString(R.string.warningInfo_seatOffer));
                    licensenum.setHintTextColor(activity.getResources().getColor(
                            R.color.purple_9F35FF));
                    carbrand.setHintTextColor(activity.getResources().getColor(
                            R.color.purple_9F35FF));
                    color.setHintTextColor(activity.getResources().getColor(
                            R.color.purple_9F35FF));
                    model.setHintTextColor(activity.getResources().getColor(
                            R.color.purple_9F35FF));
                    // licensenum.setText("");
                    // carbrand.setText("");
                    // color.setText("");
                    // model.setText("");
                    licensenum.setInputType(InputType.TYPE_CLASS_TEXT);
                    carbrand.setInputType(InputType.TYPE_CLASS_TEXT);
                    color.setInputType(InputType.TYPE_CLASS_TEXT);
                    model.setInputType(InputType.TYPE_CLASS_TEXT);

                    // 向服务器请求查询车辆信息表start!
                    selectcarinfo(UserPhoneNumber);
                    // 向服务器请求查询车辆信息表end!
                }
//                confirm();
            }

            private void selectcarinfo(final String phonenum) {

                String carinfo_selectrequest_baseurl = activity.getString(R.string.uri_base)
                        + activity.getString(R.string.uri_CarInfo)
                        + activity.getString(R.string.uri_selectcarinfo_action);

                Log.e("carinfo_selectrequest_baseurl",
                        carinfo_selectrequest_baseurl);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, carinfo_selectrequest_baseurl,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Log.d("carinfo_select", response);
                                String jas_id = null;
                                JSONObject json1 = null;
                                try {
                                    json1 = new JSONObject(response);
                                    JSONObject json = json1
                                            .getJSONObject("result");
                                    jas_id = json.getString("id");

                                    if (jas_id.compareTo("") != 0) { // 服务器上存在车辆信息时

                                        carinfochoosing_type = 2;
                                        carbrand.setText(json
                                                .getString("carBrand"));
                                        model.setText(json
                                                .getString("carModel"));
                                        licensenum.setText(json
                                                .getString("carNum"));
                                        color.setText(json
                                                .getString("carColor"));

                                    } else {
                                        carinfochoosing_type = 1;
                                    }
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
        });
        return true;
    }
}
