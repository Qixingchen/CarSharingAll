/*
 * 信息修改
 * 头像修改，存在本地
 * 个人信息修改与车辆信息修改，同时存在本地和服务器
 * 访问服务器获取车辆表，检查服务器上是否已存在车辆表，选择车辆表的提交方式是add还是update
 */

package com.xmu.carsharing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.Tool.Tool;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.Tool.Hash_pwd;

public class PeronalinfoModifyActivity extends Activity {
	private Button btn_login;
	private EditText name, age, brand, model, color, num, capacity;
	private boolean bname, bage, bbrand, bmodel, bcolor, bNum, bcapacity,
			bmale, bfemale;
	private static boolean modifyok, carinfook;
	private RadioGroup genderGroup;
	private RadioButton femaleRadioButton;
	private RadioButton maleRadioButton;
	private String sex;
	Context context = PeronalinfoModifyActivity.this;
	private LinearLayout switchAvatar;
	private ImageView faceImage;
	private final static String CACHE = "/css";
	private ImageView iamge;
	private String changeinfo_result;
	private String username;
	private String userpassword;
	public static RequestQueue queue;

//	private int carinfochoosing_type;// 作为车辆表信息修改方法的判别

	Hash_pwd hash = new Hash_pwd();

	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String UserPhoneNumber;

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

	public static Drawable fbm = null;
	Uri photouri;

	// carnum,phonenum,carbrand,carmodel,carcolor,capacity
	public void carinfo(final String phonenum, final String carnum,
			final String carbrand, final String carmodel,
			final String carcolor, final String car_capacity, int type) {
		

		String carinfotype;
		if (type == 1) {
			carinfotype = getString(R.string.uri_addcarinfo_action);
		} else {
			carinfotype = getString(R.string.uri_updatecarinfo_action);
		}

		String carinfo_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo) + carinfotype;
		// + "carnum=" + carnum + "&phonenum="
		// + phonenum + "&carbrand=" + carbrand + "&carmodel=" + carmodel
		// + "&carcolor=" + carcolor +"&capacity=" + car_capacity;

		// "http://192.168.1.111:8080/CarsharingServer/CarInfo!changeinfo.action?";

		Log.d("carinfo_URL", carinfo_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e("carinfo_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							carinfook = json1.getBoolean("result");
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
						if (carinfook == false) {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(), "车辆信息修改失败",
									Toast.LENGTH_SHORT);
							errorinfo.show();
						} else {
							Toast info = Toast.makeText(
									getApplicationContext(), "车辆信息修改成功",
									Toast.LENGTH_SHORT);
							info.show();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("carinfo_result", error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "网络连接失败",
								Toast.LENGTH_LONG);
						errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("carnum", carnum);
				params.put("phonenum", phonenum);
				params.put("carbrand", carbrand);
				params.put("carmodel", carmodel);
				params.put("carcolor", carcolor);
				params.put("capacity", car_capacity);
				return params;
			}
		};

		queue.add(stringRequest);
	}

	private void changeinfo(final String phonenum, final String name,
			final String age, final String sex) {
		
		String modify_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_UserInfo)
				+ getString(R.string.uri_changeinfo_action);
		// + "phonenum=" + phonenum
		// + "&name=" + name + "&age=" + age + "&sex=" + sex;
		// "http://192.168.1.111:8080/CarsharingServer/UserInfo!changeinfo.action?";

		Log.d("changeinfo_URL", modify_baseurl);
		// Instantiate the RequestQueue.
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				modify_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e("changeinfo_result", response);
						JSONObject json1 = null;
						try {
							json1 = new JSONObject(response);
							modifyok = json1.getBoolean("Login");
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
						if (modifyok == false) {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(), "个人信息修改失败",
									Toast.LENGTH_SHORT);
							errorinfo.show();
						} else {
							Toast info = Toast.makeText(
									getApplicationContext(), "个人信息修改成功",
									Toast.LENGTH_SHORT);
							info.show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("changeinfo_result", error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "网络连接失败",
								Toast.LENGTH_LONG);
						errorinfo.show();
					}
				}) {
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phonenum", phonenum);
				params.put("name", name);
				params.put("age", age);
				params.put("sex", sex);
				return params;
			}
		};

		queue.add(stringRequest);
	}

/*	private void selectcarinfo(final String phonenum) {
		
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

							if (jas_id.compareTo("") == 0) {
								carinfochoosing_type = 1;
								Log.e("carinfochoosing_type", "1");
							} else {
								carinfochoosing_type = 2;
								Log.e("carinfochoosing_type", "2");
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
	}*/

    CarinfoStatus function_carstatus; /*车辆表是否已存在的判断，已封装在CarinfoStatus.java中*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peronalinfo_modify);


        function_carstatus = new CarinfoStatus(this, R.id.personalinfo_modify_layout,
                this.getApplicationContext());

		fbm = null;
		name = (EditText) findViewById(R.id.informodify_FullName);
		age = (EditText) findViewById(R.id.informodify_age);
		queue = Volley.newRequestQueue(this);
		brand = (EditText) findViewById(R.id.informodify_CarBrand);
		model = (EditText) findViewById(R.id.informodify_CarModel);
		color = (EditText) findViewById(R.id.informodify_color);
		num = (EditText) findViewById(R.id.informodify_Num);
		capacity = (EditText) findViewById(R.id.informodify_capacity);
		name.addTextChangedListener(nameTextWatcher);
		age.addTextChangedListener(ageTextWatcher);
		// brand.addTextChangedListener(brandTextWatcher);
		// model.addTextChangedListener(modelTextWatcher);
		// color.addTextChangedListener(colorTextWatcher);
		// num.addTextChangedListener(numTextWatcher);
		// capacity.addTextChangedListener(capTextWatcher);
		genderGroup = (RadioGroup) findViewById(R.id.informodify_gender);
		femaleRadioButton = (RadioButton) findViewById(R.id.informodify_female);
		maleRadioButton = (RadioButton) findViewById(R.id.informodify_male);
		switchAvatar = (LinearLayout) findViewById(R.id.photolayout);
		faceImage = (ImageView) findViewById(R.id.Userbadge);
		// 设置事件监听
		switchAvatar.setOnClickListener(listener);
		// 提取用户手机号
		SharedPreferences sharedPref = this
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = sharedPref.getString(
				getString(R.string.PreferenceUserPhoneNumber), "0");

		genderGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						
						if (checkedId == femaleRadioButton.getId()) {
							bfemale = true;
						} else if (checkedId == maleRadioButton.getId()) {
							bmale = true;
						} else {
							bmale = false;
							bfemale = false;
						}
						confirm();
					}
				});

		btn_login = (Button) findViewById(R.id.informodify_btn_login);
		btn_login.setEnabled(false);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Context phonenumber = PeronalinfoModifyActivity.this;
				SharedPreferences sharedPref = context.getSharedPreferences(
						UserPhoneNumber, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("refreshname", name.getText().toString());

				editor.putString("refreshage", age.getText().toString());
				editor.putString("refreshdescription", brand.getText()
						.toString());
				editor.putString("refreshmodel", model.getText().toString());
				editor.putString("refreshcolor", color.getText().toString());
				editor.putString("refreshnum", num.getText().toString());
				editor.putString("refreshcapacity", capacity.getText()
						.toString());

				if (femaleRadioButton.isChecked() == true) {
					editor.putString("refreshsex", "女");
					sex = "w";
				} else {
					editor.putString("refreshsex", "男");
					sex = "m";
				}
				editor.commit();
				
				SharedPreferences filename = phonenumber.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				username = filename.getString("refreshfilename", "0");
				userpassword = filename.getString(
						getString(R.string.PreferenceUserPassword), "0");

				// 向服务器发送个人信息修改请求start!
				changeinfo(username, name.getText().toString(), age.getText()
						.toString(), sex);
				// 向服务器发送个人信息修改请求end!

				// 向服务器发送车辆信息修改请求start!
				carinfo(username, num.getText().toString(), brand.getText()
						.toString(), model.getText().toString(), color
						.getText().toString(), capacity.getText().toString(),
						function_carstatus.carinfochoosing_type);
				// 向服务器发送车辆信息修改end!

				Intent btn_login = new Intent();
				btn_login.setClass(PeronalinfoModifyActivity.this,
						PersonalCenterActivity.class);
				startActivity(btn_login);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		// 向服务器请求查询车辆信息表start!
		function_carstatus.selectcarinfo(UserPhoneNumber);
		// 向服务器请求查询车辆信息表end!

		// Get the Camera instance as the activity achieves full user focus

		Context phonenumber = PeronalinfoModifyActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "文件名");
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String newfullname = sharedPref.getString("refreshname", "");
		name.setText(newfullname);
		String newage = sharedPref.getString("refreshage", "");
		age.setText(newage);
		String newbrand = sharedPref.getString("refreshdescription", "");
		brand.setText(newbrand);
		String newmodel = sharedPref.getString("refreshmodel", "");
		model.setText(newmodel);
		String newcolor = sharedPref.getString("refreshcolor", "");
		color.setText(newcolor);
		String newnum = sharedPref.getString("refreshnum", "");
		num.setText(newnum);
		String newcapacity = sharedPref.getString("refreshcapacity", "");
		capacity.setText(newcapacity);
		String sex = sharedPref.getString("refreshsex", "女");
		if (sex.compareTo("女") == 0) {
			femaleRadioButton.setChecked(true);
		} else {
			maleRadioButton.setChecked(true);
		}

		File UserPhoto = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber);
		if (UserPhoto.exists()) {
			photouri = Uri.fromFile(UserPhoto);
			faceImage.setImageURI(photouri);
		} else {
			faceImage.setImageResource(R.drawable.ic_launcher);
		}

	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog();
		}
	};

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Tool.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(
												context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tool.hasSdcard()) {
					File tempFile = new File(
							context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
							IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(PeronalinfoModifyActivity.this,
							"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(context
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber)));
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			fbm = drawable;
			faceImage.setImageURI(Uri.fromFile(new File(context
					.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					UserPhoneNumber)));
			File oldphoto = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					IMAGE_FILE_NAME);
			if (oldphoto.exists()) {
				oldphoto.deleteOnExit();
			}
		}
	}

	TextWatcher nameTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			editStart = name.getSelectionStart();
			editEnd = name.getSelectionEnd();
			if (temp.length() > 0) {
				bname = true;
			} else {
				bname = false;
			}
			confirm();

		}
	};
	TextWatcher ageTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			editStart = age.getSelectionStart();
			editEnd = age.getSelectionEnd();
			if (temp.length() != 0) {
				bage = true;
			} else {
				bage = false;
			}
			confirm();

		}
	};

	// TextWatcher brandTextWatcher = new TextWatcher() {
	// private CharSequence temp;
	// private int editStart;
	// private int editEnd;
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// 
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// 
	// // mTextView.setText(s);//将输入的内容实时显示
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// 
	// editStart = brand.getSelectionStart();
	// editEnd = brand.getSelectionEnd();
	// if (temp.length() > 0) {
	// bbrand = true;
	// } else {
	// bbrand = false;
	// }
	// confirm();
	//
	// }
	// };
	// TextWatcher modelTextWatcher = new TextWatcher() {
	// private CharSequence temp;
	// private int editStart;
	// private int editEnd;
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// 
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// 
	// // mTextView.setText(s);//将输入的内容实时显示
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// 
	// editStart = model.getSelectionStart();
	// editEnd = model.getSelectionEnd();
	// if (temp.length() != 0) {
	// bmodel = true;
	// } else {
	// bmodel = false;
	// }
	// confirm();
	//
	// }
	// };
	// TextWatcher colorTextWatcher = new TextWatcher() {
	// private CharSequence temp;
	// private int editStart;
	// private int editEnd;
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// 
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// 
	// // mTextView.setText(s);//将输入的内容实时显示
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// 
	// editStart = color.getSelectionStart();
	// editEnd = color.getSelectionEnd();
	// if (temp.length() > 0) {
	// bcolor = true;
	// } else {
	// bcolor = false;
	// }
	// confirm();
	// }
	// };
	// TextWatcher numTextWatcher = new TextWatcher() {
	// private CharSequence temp;
	// private int editStart;
	// private int editEnd;
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// 
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// 
	// // mTextView.setText(s);//将输入的内容实时显示
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// 
	// editStart = num.getSelectionStart();
	// editEnd = num.getSelectionEnd();
	// if (temp.length() != 0) {
	// bNum = true;
	// } else {
	// bNum = false;
	// }
	// confirm();
	// }
	// };
	// TextWatcher capTextWatcher = new TextWatcher() {
	// private CharSequence temp;
	// private int editStart;
	// private int editEnd;
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// 
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// 
	// // mTextView.setText(s);//将输入的内容实时显示
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// 
	// editStart = capacity.getSelectionStart();
	// editEnd = capacity.getSelectionEnd();
	// if (temp.length() != 0) {
	// bcapacity = true;
	// } else {
	// bcapacity = false;
	// }
	// confirm();
	// }
	// };

	public void confirm() {
		if (bname && bage &&
		// bbrand && bmodel && bcolor && bNum && bcapacity
		// &&
				(bmale || bfemale)) {
			btn_login.setEnabled(true);
		} else {
			btn_login.setEnabled(false);
		}
	}

}
