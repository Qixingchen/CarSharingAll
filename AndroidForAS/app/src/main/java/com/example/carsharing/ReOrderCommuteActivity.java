package com.example.carsharing;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class ReOrderCommuteActivity extends Activity {

	private ImageView exchange;
	private int mHour, mMinute, mday, month, myear;
	private char item[];
	static final int TIME_DIALOG = 0;
	static final int DATE_DIALOG = 1;
	static final int TIME_DIALOG1 = 2;
	private Button startDate;
	private Button endDate;
	private Button earlystarttime;
	private Button latestarttime;
	private Button increase;
	private Button decrease;
	private Button startplace;
	private Button endplace;
	private Calendar c = Calendar.getInstance();
	private float startplace_longitude;
	private float startplace_latitude;
	private float destination_longitude;
	private float destination_latitude;
	private String StartPointUserName, StartPointMapName, EndPointUserName,
			EndPointMapName;
	private String startdate, enddate, starttime, endtime, weekrepeat;
	private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5,
			checkbox6, checkbox7;
	private int sum = 0;
	private TextView count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_re_order_commute);

		// getID
		startplace = (Button) findViewById(R.id.re_commute_startplace);
		endplace = (Button) findViewById(R.id.re_commute_endplace);
		exchange = (ImageView) findViewById(R.id.re_commute_exchange);
		startDate = (Button) findViewById(R.id.re_commute_startdate);
		endDate = (Button) findViewById(R.id.re_commute_enddate);
		earlystarttime = (Button) findViewById(R.id.re_commute_earliest_start_time);
		latestarttime = (Button) findViewById(R.id.re_commute_latest_start_time);
		increase = (Button) findViewById(R.id.re_commute_increase);
		decrease = (Button) findViewById(R.id.re_commute_decrease);
		count = (TextView) findViewById(R.id.re_commute_count);
		checkbox1 = (CheckBox) findViewById(R.id.re_commute_checkBox1);
		checkbox2 = (CheckBox) findViewById(R.id.re_commute_checkBox2);
		checkbox3 = (CheckBox) findViewById(R.id.re_commute_checkBox3);
		checkbox4 = (CheckBox) findViewById(R.id.re_commute_checkBox4);
		checkbox5 = (CheckBox) findViewById(R.id.re_commute_checkBox5);
		checkbox6 = (CheckBox) findViewById(R.id.re_commute_checkBox6);
		checkbox7 = (CheckBox) findViewById(R.id.re_commute_checkBox7);
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
		startdate = bundle.getString("re_commute_startdate");
		enddate = bundle.getString("re_commute_enddate");
		starttime = bundle.getString("re_commute_starttime");
		endtime = bundle.getString("re_commute_endtime");
		weekrepeat = bundle.getString("weekrepeat");
		startDate.setText(startdate);
		endDate.setText(enddate);
		earlystarttime.setText(starttime);
		latestarttime.setText(endtime);
		// end from arrangementActivity

		// weekrepeat中checkbox的勾选
		int len = weekrepeat.length();
		for (int i = 0; i < len; i++) {
			if (weekrepeat.charAt(i) == '1') {
				checkbox1.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '2') {
				checkbox2.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '3') {
				checkbox3.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '4') {
				checkbox4.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '5') {
				checkbox5.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '6') {
				checkbox6.setChecked(true);
			}
			if (weekrepeat.charAt(i) == '7') {
				checkbox7.setChecked(true);
			}
		}
		// 勾选end

		exchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(ReOrderCommuteActivity.this,
						ChooseAddressActivity.class), 1);
			}
		});

		endplace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(ReOrderCommuteActivity.this,
						ChooseArrivalActivity.class), 2);
			}
		});
		startDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});
		endDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});

		earlystarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG);
			}
		});
		latestarttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG1);

			}
		});
		increase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum++;
				count.setText("" + sum);
			}
		});

		decrease.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sum--;
				if (sum < 0) {
					sum = 0;
				}
				count.setText("" + sum);
			}
		});

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
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			earlystarttime.setText(String.valueOf(hourOfDay) + "时"
					+ String.valueOf(minute) + "分" + "00" + "秒");
		}
	};

	private OnTimeSetListener mTimeSetListener1 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			startDate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
		}
	};
	private OnDateSetListener mDateSetListener1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int monthofYear,
				int dayofMonth) {
			// TODO Auto-generated method stub
			mday = dayofMonth;
			month = monthofYear;
			myear = year;
			endDate.setText(String.valueOf(year) + "年"
					+ String.valueOf(monthofYear + 1) + "月"
					+ String.valueOf(dayofMonth) + "日");
		}
	};
}
