package com.example.carsharing;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ReOrderShortwayActivity extends Activity {
	private ImageView exchange;
	private int mHour, mMinute, mday, month, myear;
	static final int TIME_DIALOG = 0;
	static final int DATE_DIALOG = 1;
	static final int TIME_DIALOG1 = 2;
	private Button datebutton;
	private Button earlystarttime;
	private Button latestarttime;
	private Button increase;
	private Button decrease;
	private Button startplace;
	private Button endplace;
	private Button next;
	private TextView carbrand, model, color, licensenum, content;
	private RadioGroup shortway_group;
	private RadioButton mRadio1, mRadio2;
	private Calendar c = Calendar.getInstance();
	private float startplace_longitude;
	private float startplace_latitude;
	private float destination_longitude;
	private float destination_latitude;
	private String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;
	private String startdate, starttime, endtime;
	private int sum = 0;
	private TextView count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_re_order_shortway);

		// getID
		startplace = (Button) findViewById(R.id.re_shortway_startplace);
		endplace = (Button) findViewById(R.id.re_shortway_endplace);
		exchange = (ImageView) findViewById(R.id.re_shortway_exchange);
		datebutton = (Button) findViewById(R.id.re_shortway_dates);
		earlystarttime = (Button) findViewById(R.id.re_shortway_earliest_start_time);
		latestarttime = (Button) findViewById(R.id.re_shortway_latest_start_time);
		increase = (Button) findViewById(R.id.re_shortway_increase);
		decrease = (Button) findViewById(R.id.re_shortway_decrease);
		count = (TextView) findViewById(R.id.re_shortway_count);

		carbrand = (EditText) findViewById(R.id.shortway_CarBrand);
		model = (EditText) findViewById(R.id.shortway_CarModel);
		color = (EditText) findViewById(R.id.shortway_color);
		licensenum = (EditText) findViewById(R.id.shortway_Num);

		// licensenum.addTextChangedListener(numTextWatcher);
		// carbrand.addTextChangedListener(detTextWatcher);
		// color.addTextChangedListener(coTextWatcher);
		// model.addTextChangedListener(moTextWatcher);

		next = (Button) findViewById(R.id.shortway_sure);
		next.setEnabled(false);
		// db = new DatabaseHelper(ReOrderShortwayActivity.this, "test", null,
		// 1);
		// db1 = db.getWritableDatabase();

		content = (TextView) findViewById(R.id.shortway_content);
		mRadio1 = (RadioButton) findViewById(R.id.shortway_radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.shortway_radioButton2);
		shortway_group = (RadioGroup) findViewById(R.id.shortway_radiobutton01);
		// getID end

		// from arrangementActivity
		Bundle bundle = this.getIntent().getExtras();
		startplace.setText(bundle.getString("stpusername") + ","
				+ bundle.getString("stpmapname"));
		endplace.setText(bundle.getString("epusername") + ","
				+ bundle.getString("epmapname"));
		startplace_longitude = bundle.getFloat("spx");
		startplace_latitude = bundle.getFloat("spy");
		destination_longitude = bundle.getFloat("epx");
		destination_latitude = bundle.getFloat("epy");
		startdate = bundle.getString("re_short_startdate");
		starttime = bundle.getString("re_short_starttime");
		endtime = bundle.getString("re_short_endtime");
		datebutton.setText(startdate);
		earlystarttime.setText(starttime);
		latestarttime.setText(endtime);
		// end from arrangementActivity

		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				String temp = startplace.getText().toString();
				if (!temp.equals("选择起点")
						&& !endplace.getText().toString().equals("选择终点")) {
					startplace.setText(endplace.getText().toString());
					endplace.setText(temp);
					float a, b;
					a = startplace_longitude;
					b = startplace_latitude;
					startplace_longitude = destination_longitude;
					startplace_latitude = destination_latitude;
					destination_longitude = a;
					destination_latitude = b;

				}
			}
		});
		startplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				startActivityForResult(new Intent(ReOrderShortwayActivity.this,
						ChooseAddressActivity.class), 1);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				startActivityForResult(new Intent(ReOrderShortwayActivity.this,
						ChooseArrivalActivity.class), 2);
			}
		});
		datebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				showDialog(DATE_DIALOG);
			}
		});

		earlystarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(TIME_DIALOG);
			}
		});
		latestarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				showDialog(TIME_DIALOG1);

			}
		});
		increase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				sum++;
				count.setText("" + sum);
			}
		});

		decrease.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				sum--;
				if (sum < 0) {
					sum = 0;
				}
				count.setText("" + sum);
			}
		});

		// // 绑定一个RadioGroup监听器
		// shortway_group
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// 18
		// // 获取变更后的选中项的ID
		//
		// // "我能提供车"不变，"我不能提供车"使车牌号等编辑框不可编辑，并更改textView
		// if (checkedId == mRadio2.getId()) {
		// bpassenager = true;
		// bdriver = false;
		//
		// licensenum.setEnabled(false);
		// carbrand.setEnabled(false);
		// color.setEnabled(false);
		// model.setEnabled(false);
		//
		// licensenum
		// .setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(
		// CharSequence source, int start,
		// int end, Spanned dest,
		// int dstart, int dend) {
		// return source.length() < 1 ? dest
		// .subSequence(dstart, dend)
		// : "";
		// }
		// } });
		// carbrand.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		// return source.length() < 1 ? dest
		// .subSequence(dstart, dend) : "";
		// }
		// } });
		// color.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		// return source.length() < 1 ? dest
		// .subSequence(dstart, dend) : "";
		// }
		// } });
		// model.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		// return source.length() < 1 ? dest
		// .subSequence(dstart, dend) : "";
		// }
		// } });
		// content.setText(getString(R.string.warningInfo_seatNeed));
		// licensenum.setHintTextColor(Color
		// .parseColor("#cccccc"));
		// carbrand.setHintTextColor(Color
		// .parseColor("#cccccc"));
		// color.setHintTextColor(Color.parseColor("#cccccc"));
		// model.setHintTextColor(Color.parseColor("#cccccc"));
		// licensenum.setInputType(InputType.TYPE_NULL);
		// carbrand.setInputType(InputType.TYPE_NULL);
		// color.setInputType(InputType.TYPE_NULL);
		// model.setInputType(InputType.TYPE_NULL);
		// } else {
		// bpassenager = false;
		// bdriver = true;
		//
		// licensenum.setEnabled(true);
		// carbrand.setEnabled(true);
		// color.setEnabled(true);
		// model.setEnabled(true);
		//
		// licensenum
		// .setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(
		// CharSequence source, int start,
		// int end, Spanned dest,
		// int dstart, int dend) {
		//
		// return null;
		// }
		// } });
		// carbrand.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		// return null;
		// }
		// } });
		// color.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		//
		// return null;
		// }
		// } });
		// model.setFilters(new InputFilter[] { new InputFilter() {
		// @Override
		// public CharSequence filter(CharSequence source,
		// int start, int end, Spanned dest,
		// int dstart, int dend) {
		//
		// return null;
		// }
		// } });
		// content.setText(getString(R.string.warningInfo_seatOffer));
		// licensenum.setHintTextColor(Color
		// .parseColor("#9F35FF"));
		// carbrand.setHintTextColor(Color
		// .parseColor("#9F35FF"));
		// color.setHintTextColor(Color.parseColor("#9F35FF"));
		// model.setHintTextColor(Color.parseColor("#9F35FF"));
		// // licensenum.setText("");
		// // carbrand.setText("");
		// // color.setText("");
		// // model.setText("");
		// licensenum.setInputType(InputType.TYPE_CLASS_TEXT);
		// carbrand.setInputType(InputType.TYPE_CLASS_TEXT);
		// color.setInputType(InputType.TYPE_CLASS_TEXT);
		// model.setInputType(InputType.TYPE_CLASS_TEXT);
		//
		// // 向服务器请求查询车辆信息表start!
		// selectcarinfo(UserPhoneNumber);
		// // 向服务器请求查询车辆信息表end!
		// }
		// confirm();
		// }
		//
		//
		// });

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1: {
				StartPointMapName = data
						.getStringExtra(getString(R.string.dbstring_PlaceMapName));
				StartPointUserName = data
						.getStringExtra(getString(R.string.dbstring_PlaceUserName));
				startplace_longitude = Float
						.valueOf(data
								.getStringExtra(getString(R.string.dbstring_longitude)));
				startplace_latitude = Float.valueOf(data
						.getStringExtra(getString(R.string.dbstring_latitude)));

				startplace
						.setText(StartPointUserName + "," + StartPointMapName);

				break;

			}
			case 2: {

				EndPointMapName = data
						.getStringExtra(getString(R.string.dbstring_PlaceMapName));
				EndPointUserName = data
						.getStringExtra(getString(R.string.dbstring_PlaceUserName));
				destination_longitude = Float
						.valueOf(data
								.getStringExtra(getString(R.string.dbstring_longitude)));
				destination_latitude = Float.valueOf(data
						.getStringExtra(getString(R.string.dbstring_latitude)));

				endplace.setText(EndPointUserName + "," + EndPointMapName);
				break;
			}

			}

		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, mTimeSetListener,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener,
					c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
		case TIME_DIALOG1:
			return new TimePickerDialog(this, mTimeSetListener1,
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		}
		return null;
	}

	private OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			mHour = hourOfDay;
			mMinute = minute;
			earlystarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00" + "秒");
		}
	};

	private OnTimeSetListener mTimeSetListener1 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			mHour = hourOfDay;
			mMinute = minute;
			latestarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00" + "秒");
		}
	};

	private OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {
			
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			datebutton.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
		}
	};

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
	// editStart = licensenum.getSelectionStart();
	// editEnd = licensenum.getSelectionEnd();
	// if (temp.length() > 0) {
	// blicensenum = true;
	// } else {
	// blicensenum = false;
	// }
	// confirm();
	//
	// }
	// };
	// TextWatcher detTextWatcher = new TextWatcher() {
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
	// editStart = carbrand.getSelectionStart();
	// editEnd = carbrand.getSelectionEnd();
	// if (temp.length() != 0) {
	// bcarbrand = true;
	// } else {
	// bcarbrand = false;
	// }
	// confirm();
	//
	// }
	// };
	//
	// TextWatcher coTextWatcher = new TextWatcher() {
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
	// editStart = carbrand.getSelectionStart();
	// editEnd = carbrand.getSelectionEnd();
	// if (temp.length() != 0) {
	// bcolor = true;
	// } else {
	// bcolor = false;
	// }
	// confirm();
	//
	// }
	// };
	//
	// TextWatcher moTextWatcher = new TextWatcher() {
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
	// editStart = carbrand.getSelectionStart();
	// editEnd = carbrand.getSelectionEnd();
	// if (temp.length() != 0) {
	// bmodel = true;
	// } else {
	// bmodel = false;
	// }
	// confirm();
	//
	// }
	// };
	//
	// public void confirm() {
	// if (bstart
	// && bend
	// && ((bdriver && blicensenum && bcolor && bcarbrand) || bpassenager)
	// && best && blst && bdate && (sum > 0)) {
	// next.setEnabled(true);
	// } else {
	// next.setEnabled(false);
	// }
	// }
}
