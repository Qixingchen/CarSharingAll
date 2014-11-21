package com.example.carsharing;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.example.carsharing.R;

public class Drawer {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private Activity activity;
	private int viewid;

	public Drawer(Activity act,int id) {
		activity = act;
		viewid = id;
	}

	public ActionBarDrawerToggle newdrawer() {
		// actionbar!!
		mTitle = activity.getTitle();
		mDrawerTitle = activity.getString(R.string.app_name);

		// 需求修改!!
		mDrawerLayout = (DrawerLayout) activity
				.findViewById(viewid);
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

	public DrawerLayout setDrawerLayout()
	{
		return mDrawerLayout;
	}
}
