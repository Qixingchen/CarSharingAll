package com.Tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmu.carsharing.AboutActivity;
import com.xmu.carsharing.OrderActivity;
import com.xmu.carsharing.PersonalCenterActivity;
import com.xmu.carsharing.R;
import com.xmu.carsharing.SettingActivity;

import java.io.File;

import longwaylist_fragmenttabhost.LongWayListActivity;


/*
*传入 Activity 和 Activity的layout ID
* 依次调用
* mDrawerToggle = activity_drawer.newdrawer();
* mDrawerLayout = activity_drawer.setDrawerLayout();
* 并在onresume中调用 OnResumeRestore();
* 即可
* todo 抽屉染色
**/
public class Drawer {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private Activity activity;
	private Context mcontext;
	private int viewid;
	private String logtag = "DrawerClass";
	private String pageNmae = "Drawer";
	private View commute;
	private View shortway;
	private View longway;
	private View personalcenter;
	private View taxi;
	private View setting;
	private View about;

	public String UserPhoneNumber;
	public Uri photouri;
	private TextView drawername;
	private TextView drawernum;
	private ImageView drawericon;

	public static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	public Drawer(Activity act, int id) {
		activity = act;
		viewid = id;
		mcontext = activity.getApplicationContext();
		commute = activity.findViewById(R.id.drawer_commute);
		shortway = activity.findViewById(R.id.drawer_shortway);
		longway = activity.findViewById(R.id.drawer_longway);
		setting = activity.findViewById(R.id.drawer_setting);
		personalcenter = activity.findViewById(R.id.drawer_personalcenter);
		taxi = activity.findViewById(R.id.drawer_taxi);
		about = activity.findViewById(R.id.drawer_respond);

		drawericon = (ImageView) activity.findViewById(R.id.drawer_icon);
		drawername = (TextView) activity.findViewById(R.id.drawer_name);
		drawernum = (TextView) activity.findViewById(R.id.drawer_phone);

		photouri = Uri.fromFile(new File(activity.
				getExternalFilesDir(Environment.DIRECTORY_PICTURES),IMAGE_FILE_NAME2));

		Log.e(logtag+"actname",activity.getClass().getSimpleName());

		OnResumeRestore();

		setDrawerIntent();
	}

	public ActionBarDrawerToggle newdrawer() {
		// actionbar!!
		mTitle = activity.getTitle();
		mDrawerTitle = activity.getString(R.string.app_name);

		// 需求修改!!

		mDrawerToggle = new ActionBarDrawerToggle(activity, /* 承载 Activity */
				mDrawerLayout, /* DrawerLayout 对象 */
				R.drawable.ic_drawer, /* nav drawer 图标用来替换'Up'符号 */
				R.string.drawer_open, /* "打开 drawer" 描述 */
				R.string.drawer_close /* "关闭 drawer" 描述 */
		) {

			/** 当drawer处于完全关闭的状态时调用 */
			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				activity.getActionBar().setTitle(mTitle);
			}

			/** 当drawer处于完全打开的状态时调用 */
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				activity.getActionBar().setTitle(mDrawerTitle);
			}
		};

		// 设置drawer触发器为DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getActionBar().setHomeButtonEnabled(true);

		// actionbar!!
		/* 当invalidateOptionsMenu()调用时调用 */
		// @Override
		// public boolean onPrepareOptionsMenu(Menu menu) {
		// // 如果nav drawer是打开的, 隐藏与内容视图相关联的action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		// return super.onPrepareOptionsMenu(menu);
		// }
		return mDrawerToggle;
	}

	public DrawerLayout setDrawerLayout() {
		return mDrawerLayout;
	}

	//设置抽屉的点击监听器 已在构造函数中调用
	private void setDrawerIntent() {

		shortway.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.短途) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent shortway = new Intent(activity,OrderActivity.class);
				shortway.putExtra("pre_page", pageNmae);
				shortway.putExtra("cstype", "shortcs");
				activity.startActivity(shortway);
			}
		});

		longway.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.长途) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent longway = new Intent(activity, LongWayListActivity.class);
				longway.putExtra("pre_page", pageNmae);
				longway.putExtra("cstype", "longcs");
				activity.startActivity(longway);
			}
		});

		commute.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.上下班) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent commute = new Intent(activity, OrderActivity.class);
				commute.putExtra("pre_page", pageNmae);
				commute.putExtra("cstype", "workcs");
				activity.startActivity(commute);
			}
		});

		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.关于) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent about = new Intent(activity, AboutActivity.class);
				activity.startActivity(about);
			}
		});

		setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.设置) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent setting = new Intent(activity, SettingActivity.class);
				activity.startActivity(setting);
			}
		});

		taxi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//TODO 出租车部分未完成，直接关闭抽屉

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.出租车) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
			}
		});

		personalcenter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.个人中心) {
					mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
					return;
				}

				mDrawerLayout.closeDrawer(activity.findViewById(R.id.left_drawer));
				Intent personalcenter = new Intent(activity, PersonalCenterActivity.class);
				personalcenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(personalcenter);
			}
		});
	}

	//请在onresume中调用，用于恢复相关信息
	public void OnResumeRestore() {

		mDrawerLayout = (DrawerLayout) activity.findViewById(viewid);

//		mDrawerLayout.setBackgroundDrawable(activity.getResources().getDrawable(
//				R.color.blue_0099cc));

		SharedPreferences filename = mcontext
				.getSharedPreferences(
						mcontext.getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);

		SharedPreferences sharedPref = mcontext.getSharedPreferences(
				UserPhoneNumber, Context.MODE_PRIVATE);
		String fullname = sharedPref.getString("refreshname", "姓名");
		drawername.setText(fullname);
		File photoFile = new File(
				mcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				UserPhoneNumber);
		if (photoFile.exists()) {
			photouri = Uri.fromFile(photoFile);
			drawericon.setImageURI(photouri);
		} else {
			drawericon.setImageResource(R.drawable.ic_launcher);
		}
	}
}
