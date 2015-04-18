package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.xmu.carsharing.R;

import java.io.File;

/**
 * Created by 雨蓝 on 2015/4/4.
 * todo 需要destory么?
 */
public class MaterialDrawer {

	private String username;
	private String userphonenum;
	private Uri userFacePhoto;
	//todo 用户头像
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	private String TITLES[] = {"上下班拼车","短途拼车","长途拼车列表查看","出租车拼车","设置","关于"};
	private int ICONS[] = {R.drawable.icon_commuters,R.drawable.icon_shortway,
			R.drawable.icon_longway,R.drawable.icon_taxi,R.drawable.ic_action_settings,
			R.drawable.ic_action_help};

	private RecyclerView mRecyclerView;                           // Declaring RecyclerView
	private RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
	private RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
	private DrawerLayout Drawer;                                  // Declaring DrawerLayout

	private ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
	private Toolbar toolbar;

	//class内部使用
	private Application mapplication;
	private Context mcontext;
	private Activity mactivity;
	private String logtag ="材料抽屉";
	private ToolWithActivityIn toolWithActivityIn;

	public MaterialDrawer(Activity mact,Toolbar mtoolbar){
		mactivity = mact;
		mapplication =mact.getApplication();
		mcontext = mapplication.getApplicationContext();
		toolbar = mtoolbar;
		toolWithActivityIn = new ToolWithActivityIn(mact);
		init();
	}

	//构造函数已调用
	private void init(){

		userphonenum = toolWithActivityIn.get用户手机号从偏好文件();
		username = toolWithActivityIn.get用户姓名从偏好文件(userphonenum);

		userFacePhoto = Uri.fromFile(new File(mactivity.
						getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));

		mRecyclerView = (RecyclerView) mactivity.findViewById(R.id.RecyclerView);
		// Assigning the RecyclerView Object to the xml View

		mRecyclerView.setHasFixedSize(true);
		// Letting the system know that the list objects are of fixed size


		// Creating the Adapter of MetarialDrawerAdapter class(which we are going to see in a bit)
		// And passing the titles,icons,header view name, header view email,
		// and header view profile picture

		Drawer = (DrawerLayout) mactivity.findViewById(R.id.test_Material_tool_bar_Drawer);
		// Drawer object Assigned
		// to the view

		mAdapter = new MetarialDrawerAdapter(TITLES,ICONS, username, userphonenum,
				userFacePhoto,mactivity,Drawer);

		mRecyclerView.setAdapter(mAdapter);
		// Setting the adapter to RecyclerView

		mLayoutManager = new LinearLayoutManager(mactivity);
		// Creating a
		// layout Manager

		mRecyclerView.setLayoutManager(mLayoutManager);
		// Setting the layout Manager



		mDrawerToggle = new ActionBarDrawerToggle(mactivity,Drawer,toolbar,
				R.string.drawer_open,R.string.drawer_close){

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
				// open I am not going to put anything here)
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// Code here will execute once drawer is closed
			}



		}; // Drawer Toggle Object Made
		Drawer.setDrawerListener(mDrawerToggle);
		// Drawer Listener set to the Drawer toggle
		mDrawerToggle.syncState();
		// Finally we set the drawer toggle sync State



	}


}
