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

		// �����޸�!!
		mDrawerLayout = (DrawerLayout) activity
				.findViewById(viewid);
		mDrawerToggle = new ActionBarDrawerToggle(activity, /* ���� Activity */
		mDrawerLayout, /* DrawerLayout ���� */
		R.drawable.ic_drawer, /* nav drawer ͼ�������滻'Up'���� */
		R.string.drawer_open, /* "�� drawer" ���� */
		R.string.drawer_close /* "�ر� drawer" ���� */
		) {

			/** ��drawer������ȫ�رյ�״̬ʱ���� */
			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				activity.getActionBar().setTitle(mTitle);
			}

			/** ��drawer������ȫ�򿪵�״̬ʱ���� */
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				activity.getActionBar().setTitle(mDrawerTitle);
			}
		};

		// ����drawer������ΪDrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getActionBar().setHomeButtonEnabled(true);

		// actionbar!!
		/* ��invalidateOptionsMenu()����ʱ���� */
		// @Override
		// public boolean onPrepareOptionsMenu(Menu menu) {
		// // ���nav drawer�Ǵ򿪵�, ������������ͼ�������action items
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
