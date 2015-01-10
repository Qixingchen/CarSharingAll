//设置。。。具体功能未实现

package com.example.carsharing;

import java.io.File;

import longwaylist_fragmenttabhost.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity {

	// actionbar!!
	Drawer activity_drawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	View commute;
	View shortway;
	View longway;
	View personalcenter;
	View taxi;
	View setting;
	View about;
	ImageView drawericon;
	Uri photouri;
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";
	String UserPhoneNumber;
	private TextView drawername;
	private TextView drawernum;

	// actionbarend!!

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);
		activity_drawer = new Drawer(this, R.id.about_layout);
		mDrawerToggle = activity_drawer.newdrawer();
		mDrawerLayout = activity_drawer.setDrawerLayout();
		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);

		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		about = findViewById(R.id.drawer_respond);
		setting = findViewById(R.id.drawer_setting);
		taxi = findViewById(R.id.drawer_taxi);
		personalcenter = findViewById(R.id.drawer_personalcenter);
		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent about = new Intent(getApplicationContext(),
						AboutActivity.class);
				startActivity(about);
			}
		});

		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent setting = new Intent(getApplicationContext(),
						SettingActivity.class);
				startActivity(setting);
			}
		});

		taxi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
			}
		});

		personalcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent personalcenter = new Intent(getApplicationContext(),
						PersonalCenterActivity.class);
				personalcenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(personalcenter);
			}
		});

		shortway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent shortway = new Intent(getApplicationContext(),
						ShortWayActivity.class);
				shortway.putExtra("pre_page", "Drawer");
				startActivity(shortway);
			}
		});

		longway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent longway = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(longway);
			}
		});

		commute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
				Intent commute = new Intent(getApplicationContext(),
						CommuteActivity.class);
				commute.putExtra("pre_page", "Drawer");
				startActivity(commute);
			}
		});

	}

	// actionbar!!

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// 在onRestoreInstanceState发生后，同步触发器状态.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 将事件传递给ActionBarDrawerToggle, 如果返回true，表示app 图标点击事件已经被处理
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// 处理你的其他action bar items...

		return super.onOptionsItemSelected(item);
	}

	// actionbarend!!
	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		about.setBackgroundDrawable(getResources().getDrawable(
				R.color.blue_0099cc));
		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = AboutActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);
		Context context = AboutActivity.this;
		SharedPreferences sharedPref = context.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String fullname = sharedPref.getString("refreshname", "姓名");
		drawername.setText(fullname);
		File photoFile = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber);
		if (photoFile.exists()) {
			photouri = Uri.fromFile(photoFile);
			drawericon.setImageURI(photouri);
		} else {
			drawericon.setImageResource(R.drawable.ic_launcher);
		}
	}

}
