/*
 * ��������
 * 1.ͷ���Ա����������䣬Ʒ�ƣ����ƺŴӱ��ػ�ȡ��������Ϣ�ɱ༭
 * 2.onresume �������°�ƴ������;ƴ������;ƴ�����˷�������Ϣ����ʾ��һ���ڸ�ҳ
 * 3.onresume �ղصĵ�ַ�ӱ��ػ�ȡ����ʾ��һ���ڸ�ҳ
 * 4.���������������ɶ���������ȡ
 * 5.ע�������˺�
 */

package com.example.carsharing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import longwaylist_fragmenttabhost.MainActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carsharing.R.string;
import com.example.carsharing.MyProgressDialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class PersonalCenterActivity extends Activity {

	TextView firsthistory;
	TextView firstdeal;
	TextView firstfavorite;

	public static ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, Object>> mylist2 = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	public static RequestQueue queue;
	// actionbar!!
	Drawer activity_drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	boolean isExit;
	boolean bfirsthistory = false, bfirstdeal = false, bfirstfavorite = false;
	Context context = PersonalCenterActivity.this;
	static ImageView image;
	ImageView drawericon;
	View commute;
	View shortway;
	View longway;
	View personalcenter;
	View taxi;
	View setting;
	View about;
	TextView name;
	TextView age;
	TextView description;
	TextView carnum;
	TextView sex;
	private final static String CACHE = "/css";
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";
	Uri photouri;
	private TextView drawername;
	private TextView drawernum;
	RatingBar ratingbar;
	// progressbar
	private static MyProgressDialog pd;
	// progressbar end

	// actionbarend!!

	// �û��ֻ���
	String UserPhoneNumber;

	// database
	private boolean loadok;
	DatabaseHelper db;
	SQLiteDatabase db1;
	Cursor dbresult;

	// database end!!

	Button quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		activity_drawer = new Drawer(this,R.id.person_center_layout);
		mDrawerToggle = activity_drawer.newdrawer();
		mDrawerLayout = activity_drawer.setDrawerLayout();

		firsthistory = (TextView) findViewById(R.id.first_history);
		firstdeal = (TextView) findViewById(R.id.first_receiving);
		firstfavorite = (TextView) findViewById(R.id.first_address);

		queue = Volley.newRequestQueue(this);
		Button historymore = (Button) findViewById(R.id.personalcenter_historymore);
		ratingbar = (RatingBar) findViewById(R.id.personalcenter_ratingBar);
		Button receivingmore = (Button) findViewById(R.id.personalcenter_receivingmore);
		Button addressmore = (Button) findViewById(R.id.personalcenter_addressmore);
		ImageButton imageedit = (ImageButton) findViewById(R.id.personalcenter_imageEdit);
		Button iwantcar = (Button) findViewById(R.id.personalcenter_iwantcar);
		quit = (Button) findViewById(R.id.personalcenter_quit);
		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		personalcenter = findViewById(R.id.drawer_personalcenter);
		setting = findViewById(R.id.drawer_setting);
		taxi = findViewById(R.id.drawer_taxi);
		name = (TextView) findViewById(R.id.personalcenter_name);
		age = (TextView) findViewById(R.id.personalcenter_age);
		description = (TextView) findViewById(R.id.personalcenter_description);
		carnum = (TextView) findViewById(R.id.personalcenter_carnumber);
		sex = (TextView) findViewById(R.id.personalcenter_sex);
		image = (ImageView) findViewById(R.id.personalcenter_icon);
		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);
		about = findViewById(R.id.drawer_respond);

		// ratingbar ����
		ratingbar.setRating(1.0f);
		// database

		Context phonenumber = PersonalCenterActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");

		Log.e("Personcenter_UserPhoneNum", UserPhoneNumber);

		db = new DatabaseHelper(getApplicationContext(), UserPhoneNumber, null,
				1);
		db1 = db.getWritableDatabase();

		// database end!!!

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent about = new Intent(PersonalCenterActivity.this,
						AboutActivity.class);
				startActivity(about);
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent setting = new Intent(PersonalCenterActivity.this,
						SettingActivity.class);
				startActivity(setting);
			}
		});
		taxi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		personalcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		shortway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent shortway = new Intent(PersonalCenterActivity.this,
						ShortWayActivity.class);
				shortway.putExtra("pre_page", "Drawer");
				startActivity(shortway);
			}
		});

		longway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent longway = new Intent(PersonalCenterActivity.this,
						MainActivity.class);
				startActivity(longway);
			}
		});

		commute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent commute = new Intent(PersonalCenterActivity.this,
						CommuteActivity.class);
				commute.putExtra("pre_page", "Drawer");
				startActivity(commute);
			}
		});

		iwantcar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent iwantcar = new Intent();
				iwantcar.setClass(PersonalCenterActivity.this,
						CarsharingTypeActivity.class);
				startActivity(iwantcar);
			}
		});

		imageedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent imageedit = new Intent();
				imageedit.setClass(PersonalCenterActivity.this,
						PeronalinfoModifyActivity.class);
				startActivity(imageedit);
			}
		});

		historymore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (loadok == true) {
					Intent historymore = new Intent();
					historymore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);

					historymore.putExtra("intent", 1);
					startActivity(historymore);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_dataRead),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		receivingmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (loadok == true) {
					Intent receivingmore = new Intent();
					receivingmore.setClass(PersonalCenterActivity.this,
							PersonCenterDetaillistActivity.class);
					receivingmore.putExtra("intent", 2);
					startActivity(receivingmore);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.warningInfo_dataRead),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		addressmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (loadok == true) {
				Intent addressmore = new Intent();
				addressmore.setClass(PersonalCenterActivity.this,
						PersonCenterDetaillistActivity.class);
				addressmore.putExtra("intent", 3);
				startActivity(addressmore);
				// } else {
				// Toast.makeText(getApplicationContext(),
				// getString(R.string.warningInfo_dataRead),
				// Toast.LENGTH_SHORT).show();
				// }
			}
		});

		

		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
	}

	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = PersonalCenterActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "�ļ���");
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String newfullname = sharedPref.getString("refreshname", "����");
		drawernum.setText(UserPhoneNumber);
		drawername.setText(newfullname);
		String newage = sharedPref.getString("refreshage", "����");

		String newdescription = sharedPref.getString("refreshdescription",
				"��������");

		String newcarnum = sharedPref.getString("refreshnum", "���ƺ�");

		String newsex = sharedPref.getString("refreshsex", "�Ա�");
		Log.e("carnum", newcarnum);
		name.setText(newfullname);
		age.setText(newage);
		sex.setText(newsex);
		description.setText(newdescription);
		carnum.setText(newcarnum);

		placeLikedListFlush();

		File photoFile = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber);
		if (photoFile.exists()) {
			photouri = Uri.fromFile(photoFile);
			drawericon.setImageURI(photouri);
			image.setImageURI(photouri);
		} else {
			image.setImageResource(R.drawable.ic_launcher);
			drawericon.setImageResource(R.drawable.ic_launcher);
		}

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPref = getApplicationContext()
						.getSharedPreferences(UserPhoneNumber,
								Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.PreferenceUserPassword),
						"0");
				editor.commit();
				// progressbar��ʼ
				pd = new MyProgressDialog(context);
				pd.setMessage("����ע��");
				pd.show();
				// progressbar����
				Intent quit = new Intent();
				quit.setClass(PersonalCenterActivity.this, LoginActivity.class);
				startActivity(quit);
			}
		});
		loadok = false;
		mylist1.clear();
		mylist2.clear();
		firsthistory.setText(R.string.first_history);
		bfirsthistory = false;// ��ʼ��
		firstdeal.setText(R.string.first_receiving);
		bfirstdeal = false;
		firstfavorite.setText(R.string.first_address);
		bfirstfavorite = false;

		if (dbresult.getCount() != 0) {
			firstfavorite.setText(mylist3.get(0).get("text"));
			bfirstfavorite = true;
		}

		// ������������ѯ���°�ƴ����������start!
		selectrequest(UserPhoneNumber);
		// ������������ѯ���°�ƴ����������end!

		// ��ѯ���������Ϣ
		sharingresult(UserPhoneNumber);

			

	}

	private void sharingresult(final String phonenum) {
		// TODO Auto-generated method stub
		String sharingresult_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarTake)
				+ getString(R.string.uri_selectcartake_action);

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				sharingresult_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("sharingresult", response);
						try {
							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("Title", jasitem.getString("dealTime"));

								if (jasitem.getString("sharingType").compareTo(
										"commute") == 0) {
									map.put("text", "��ƥ�䶩�������°�ƴ��");
								} else if (jasitem.getString("sharingType")
										.compareTo("shortway") == 0) {
									map.put("text", "��ƥ�䶩������;ƴ��");
								}

								map.put("requst", jasitem.getString("dealId")); // ���ص�

								String deal_readstatus = null;
								if (i == 0) {
									deal_readstatus = "receive";
									map.put("deal_readstatus", "receive");// ���ص�
								} else if (i == 3) {
									deal_readstatus = "reject";
									map.put("deal_readstatus", "reject");// ���ص�
								} else if (i == 2) {
									deal_readstatus = "assessOK";
									map.put("deal_readstatus", "assessOK");// ���ص�
								} else if (i == 1) {
									deal_readstatus = "unread";
									map.put("deal_readstatus", "unread");// ���ص�
								}

								Log.e("deal_readstatuspersoncenter",
										deal_readstatus);
								if (deal_readstatus.compareTo("unread") == 0) {// δ����Ϣ
									map.put("deal_readstatusIcon",
											R.drawable.ic_dealunread);
								} else if (deal_readstatus.compareTo("receive") == 0) {// �ѽ��ն������ȼ��ڡ�δ���ۡ���
									map.put("deal_readstatusIcon",
											R.drawable.ic_noneassess);
								} else if (deal_readstatus.compareTo("reject") == 0) {// �Ѿܾ�����
									map.put("deal_readstatusIcon",
											R.drawable.ic_action_dealreject);
								} else if (deal_readstatus
										.compareTo("assessOK") == 0) {// ������ɣ�������������ϡ���
									map.put("deal_readstatusIcon",
											R.drawable.ic_dealread);
								}

								mylist2.add(map);

							}
							if (bfirstdeal == false && jasA.length() > 0) {
								if (jasitem.getString("sharingType").compareTo(
										"commute") == 0) {
									firstdeal.setText(jasitem
											.getString("dealTime")
											+ "  "
											+ "���°�ƴ������");
									bfirstdeal = true;
								} else if (jasitem.getString("sharingType")
										.compareTo("shortway") == 0) {
									firstdeal.setText(jasitem
											.getString("dealTime")
											+ "  "
											+ "��;ƴ������");
									bfirstdeal = true;
								} else {
									firstdeal.setText(R.string.first_receiving);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("sharingresult", error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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

	private void longway_selectrequest(final String phonenum) {
		// TODO Auto-generated method stub
		String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_LongwayPublish)
				+ getString(R.string.uri_selectpublish_action);

		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				longwayway_selectpublish_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("longwayway_selectpublish_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title", jasitem.getString("startDate"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " �� "
												+ "  "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("publishTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									firsthistory.setText(startplace[0] + " ��  "
											+ endplace[0]);
									bfirsthistory = true;
								}
							}
							loadok = true;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("longwayway_selectpublish_result",
								error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "��������ʧ��",
								Toast.LENGTH_LONG);
						errorinfo.show();
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

	private void shortway_selectrequest(final String phonenum) {
		// TODO Auto-generated method stub
		String shortway_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_ShortwayRequest)
				+ getString(R.string.uri_selectrequest_action);
		// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				shortway_selectrequest_baseurl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("shortway_selectrequest_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title", jasitem.getString("startDate"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " ��  "
												+ "  "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("requestTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									// Log.e("startplace[0]",startplace[0]);
									firsthistory.setText(
									// jasitem
									// .getString("requestTime")
											// ��ҳ����ʾʱ��
											startplace[0] + " ��  "
													+ endplace[0]);
									bfirsthistory = true;
								}
							}
							longway_selectrequest(phonenum);
							loadok = true;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("shortway_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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

	private void selectrequest(final String phonenum) {
		// TODO Auto-generated method stub
		String commute_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CommuteRequest)
				+ getString(R.string.uri_selectrequest_action);
		// + "phonenum=" + phonenum;
		// "http://192.168.1.111:8080/CarsharingServer/CommuteRequest!selectrequest.action?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				commute_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.w("commute_selectrequest_result", response);
						try {

							JSONObject jasitem = null;
							JSONObject jas = new JSONObject(response);
							JSONArray jasA = jas.getJSONArray("result");
							for (int i = 0; i < jasA.length(); i++) {
								jasitem = jasA.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("Title",
										jasitem.getString("requestTime")
												+ " ÿ��"
												+ jasitem
														.getString("weekRepeat"));
								map.put("text",
										jasitem.getString("startPlace")
												+ "  "
												+ " �� "
												+ jasitem
														.getString("destination")
												+ "  ");
								map.put("requst",
										jasitem.getString("requestTime"));
								mylist1.add(map);
								if (bfirsthistory == false) {

									String startplace[] = jasitem.getString(
											"startPlace").split(",");
									String endplace[] = jasitem.getString(
											"destination").split(",");
									// Log.e("startplace[0]",startplace[0]);
									firsthistory.setText(
									// jasitem
									// .getString("requestTime")
											// ��ҳ����ʾʱ��
											startplace[0] + " ��  "
													+ endplace[0]);
									bfirsthistory = true;
								}
							}
							shortway_selectrequest(phonenum);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("commute_selectresult_result",
								error.getMessage(), error);
						// Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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

	public void placeLikedListFlush() {
		mylist3.clear();

		dbresult = db1.query(getString(R.string.dbtable_placeliked), null,
				null, null, null, null, null);
		Log.e("number", String.valueOf(dbresult.getCount()));
		if (dbresult.getCount() == 0) {
			return;
		}

		@SuppressWarnings("unchecked")
		HashMap<String, String>[] map = new HashMap[dbresult.getColumnCount()];

		dbresult.moveToFirst();

		int i = 0;
		while (!dbresult.isAfterLast()) {
			map[i] = new HashMap<String, String>();
			map[i].put("Title", String.valueOf(dbresult.getString(2)));
			map[i].put("text", String.valueOf(dbresult.getString(1)));

			mylist3.add(map[i]);
			Log.w("Map", mylist3.toString());
			dbresult.moveToNext();
			i++;
		}

	};



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// ��onRestoreInstanceState������ͬ��������״̬.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ���¼����ݸ�ActionBarDrawerToggle, �������true����ʾapp ͼ�����¼��Ѿ�������
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// �����������action bar items...

		return super.onOptionsItemSelected(item);
	}

	// actionbarend!!
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast toast = Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 50);
			toast.show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			if (Build.VERSION.SDK_INT >= 16) {
				finishAffinity();
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				super.onDestroy();
				System.exit(0);
			}

		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

}
