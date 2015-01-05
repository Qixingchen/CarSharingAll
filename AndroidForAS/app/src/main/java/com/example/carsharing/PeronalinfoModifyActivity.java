/*
 * ��Ϣ�޸�
 * ͷ���޸ģ����ڱ���
 * ������Ϣ�޸��복����Ϣ�޸ģ�ͬʱ���ڱ��غͷ�����
 * ���ʷ�������ȡ�����������������Ƿ��Ѵ��ڳ�����ѡ��������ύ��ʽ��add����update
 */

package com.example.carsharing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.ContactsContract.FullNameStyle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.carsharing.Hash_pwd;

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

	private int carinfochoosing_type;// ��Ϊ��������Ϣ�޸ķ������б�

	Hash_pwd hash = new Hash_pwd();

	private String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
	/* ͷ������ */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String UserPhoneNumber;

	/* ������ */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

	public static Drawable fbm = null;
	Uri photouri;

	// carnum,phonenum,carbrand,carmodel,carcolor,capacity
	public void carinfo(final String phonenum, final String carnum,
			final String carbrand, final String carmodel,
			final String carcolor, final String car_capacity, int type) {
		// TODO Auto-generated method stub

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

		// Uri.encode(modify_baseurl, "@#&=*+-_.,:!?()/~'%");// ���ı���

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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (carinfook == false) {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(), "������Ϣ�޸�ʧ��",
									Toast.LENGTH_SHORT);
							errorinfo.show();
						} else {
							Toast info = Toast.makeText(
									getApplicationContext(), "������Ϣ�޸ĳɹ�",
									Toast.LENGTH_SHORT);
							info.show();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("carinfo_result", error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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
		// TODO Auto-generated method stub
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (modifyok == false) {
							Toast errorinfo = Toast.makeText(
									getApplicationContext(), "������Ϣ�޸�ʧ��",
									Toast.LENGTH_SHORT);
							errorinfo.show();
						} else {
							Toast info = Toast.makeText(
									getApplicationContext(), "������Ϣ�޸ĳɹ�",
									Toast.LENGTH_SHORT);
							info.show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("changeinfo_result", error.getMessage(), error);
						Toast errorinfo = Toast.makeText(null, "��������ʧ��",
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

	private void selectcarinfo(final String phonenum) {
		// TODO Auto-generated method stub
		String carinfo_selectrequest_baseurl = getString(R.string.uri_base)
				+ getString(R.string.uri_CarInfo)
				+ getString(R.string.uri_selectcarinfo_action);

		// Log.d("carinfo_selectrequest_baseurl",carinfo_selectrequest_baseurl);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				carinfo_selectrequest_baseurl, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peronalinfo_modify);
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
		// �����¼�����
		switchAvatar.setOnClickListener(listener);
		// ��ȡ�û��ֻ���
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
						// TODO Auto-generated method stub
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
					editor.putString("refreshsex", "Ů");
					sex = "w";
				} else {
					editor.putString("refreshsex", "��");
					sex = "m";
				}
				editor.commit();
				// TODO Auto-generated method stub
				SharedPreferences filename = phonenumber.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
				username = filename.getString("refreshfilename", "0");
				userpassword = filename.getString(
						getString(R.string.PreferenceUserPassword), "0");

				// ����������͸�����Ϣ�޸�����start!
				changeinfo(username, name.getText().toString(), age.getText()
						.toString(), sex);
				// ����������͸�����Ϣ�޸�����end!

				// ����������ͳ�����Ϣ�޸�����start!
				carinfo(username, num.getText().toString(), brand.getText()
						.toString(), model.getText().toString(), color
						.getText().toString(), capacity.getText().toString(),
						carinfochoosing_type);
				// ����������ͳ�����Ϣ�޸�end!

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

		// ������������ѯ������Ϣ��start!
		selectcarinfo(UserPhoneNumber);
		// ������������ѯ������Ϣ��end!

		// Get the Camera instance as the activity achieves full user focus

		Context phonenumber = PeronalinfoModifyActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "�ļ���");
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
		String sex = sharedPref.getString("refreshsex", "Ů");
		if (sex.compareTo("Ů") == 0) {
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
	 * ��ʾѡ��Ի���
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("����ͷ��")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // �����ļ�����
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// �жϴ洢���Ƿ�����ã����ý��д洢
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
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
							"δ�ҵ��洢�����޷��洢��Ƭ��", Toast.LENGTH_LONG).show();
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
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(context
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber)));
		startActivityForResult(intent, 2);
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
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
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
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
	// // TODO Auto-generated method stub
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	// // mTextView.setText(s);//�����������ʵʱ��ʾ
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
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
	// // TODO Auto-generated method stub
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	// // mTextView.setText(s);//�����������ʵʱ��ʾ
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
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
	// // TODO Auto-generated method stub
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	// // mTextView.setText(s);//�����������ʵʱ��ʾ
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
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
	// // TODO Auto-generated method stub
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	// // mTextView.setText(s);//�����������ʵʱ��ʾ
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
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
	// // TODO Auto-generated method stub
	// temp = s;
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	// // mTextView.setText(s);//�����������ʵʱ��ʾ
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
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
