package longwaylist_fragmenttabhost;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carsharing.AboutActivity;
import com.example.carsharing.CommuteActivity;
import com.example.carsharing.LongWayActivity;
import com.example.carsharing.PersonalCenterActivity;
import com.example.carsharing.R;
import com.example.carsharing.SettingActivity;
import com.example.carsharing.ShortWayActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	public static final String PAGE1_ID = "page1";
	public static final String PAGE2_ID = "page2";

	public static RequestQueue queue;

	private TabHost tabHost; // TabHost
	private List<View> views; // ViewPager内的View对象集合
	private FragmentManager manager; // Activity管理器
	private ViewPager pager; // ViewPager

	String UserPhoneNumber;

	View commute;
	View shortway;
	View longway;
	View personalcenter;
	View taxi;
	View setting;
	View about;
	Uri photouri;

	ImageView drawericon;
	private TextView drawername;
	private TextView drawernum;

	// actionbar!!
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private static final String IMAGE_FILE_NAME2 = "faceImage2.jpg";

	// actionbarend!!

	@Override
	protected void onCreate(Bundle arg0) {
		photouri = Uri.fromFile(new File(this
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				IMAGE_FILE_NAME2));
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		queue = Volley.newRequestQueue(this);

		drawericon = (ImageView) findViewById(R.id.drawer_icon);
		drawername = (TextView) findViewById(R.id.drawer_name);
		drawernum = (TextView) findViewById(R.id.drawer_phone);
		// 初始化资源
		pager = (ViewPager) findViewById(R.id.viewpager);
		tabHost = (TabHost) findViewById(R.id.tab_host);
		manager = getSupportFragmentManager();
		views = new ArrayList<View>();

		views.add(manager.findFragmentById(R.id.fragment1).getView());
		views.add(manager.findFragmentById(R.id.fragment2).getView());

		// 管理tabHost开始
		tabHost.setup();

		// 传一个空的内容给TabHost，不能用上面两个fragment
		TabContentFactory factory = new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return new View(MainActivity.this);
			}
		};

		TabSpec tabSpec = tabHost.newTabSpec(PAGE1_ID);
		tabSpec.setIndicator(createTabView("我是乘客"));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec(PAGE2_ID);
		tabSpec.setIndicator(createTabView("我是司机"));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);

		tabHost.setCurrentTab(0);
		// 管理tabHost结束

		// 设置监听器和适配器
		pager.setAdapter(new PageAdapter());
		pager.setOnPageChangeListener(new PageChangeListener());
		tabHost.setOnTabChangedListener(new TabChangeListener());

		// actionbar!!
		mTitle = mDrawerTitle = getTitle();

		// 需求修改!!
		mDrawerLayout = (DrawerLayout) findViewById(R.id.longway_list_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* 承载 Activity */
		mDrawerLayout, /* DrawerLayout 对象 */
		R.drawable.ic_drawer, /* nav drawer 图标用来替换'Up'符号 */
		R.string.drawer_open, /* "打开 drawer" 描述 */
		R.string.drawer_close /* "关闭 drawer" 描述 */
		) {

			/** 当drawer处于完全关闭的状态时调用 */
			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** 当drawer处于完全打开的状态时调用 */
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		// 设置drawer触发器为DrawerListener
		if (mDrawerLayout == null) {
			Log.e("mDrawerLayout", "isNull");
		}

		if (mDrawerToggle == null) {
			Log.e("mDrawerToggle", "isNull");
		}

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// actionbarend!!

		commute = findViewById(R.id.drawer_commute);
		shortway = findViewById(R.id.drawer_shortway);
		longway = findViewById(R.id.drawer_longway);
		about = findViewById(R.id.drawer_respond);
		setting = findViewById(R.id.drawer_setting);
		taxi = findViewById(R.id.drawer_taxi);
		personalcenter = findViewById(R.id.drawer_personalcenter);
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

	/**
	 * PageView Adapter
	 * 
	 * @author Administrator
	 * 
	 */
	private class PageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(views.get(position));
			return views.get(position);
		}
	}

	/**
	 * 标签页点击切换监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class TabChangeListener implements OnTabChangeListener {
		@Override
		public void onTabChanged(String tabId) {
			if (PAGE1_ID.equals(tabId)) {
				pager.setCurrentItem(0);
			} else if (PAGE2_ID.equals(tabId)) {
				pager.setCurrentItem(1);
			}
		}
	}

	/**
	 * ViewPager滑动切换监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int page_id) {

			if (page_id == 1) {// "我是乘客"
				Log.d("page_id", "1");
				// 刷新所有司机发布的信息start!
				lookuppublish("p");
				// 刷新所有司机发布的信息end!
			} else {// "我是司机"
				Log.d("page_id", "2");
				// 刷新所有乘客发布的信息start!
				lookuppublish("d");
				// 刷新所有乘客发布的信息end!
			}

			tabHost.setCurrentTab(page_id);
		}

		private void lookuppublish(final String role) {
			
			String longwayway_selectpublish_baseurl = getString(R.string.uri_base)
					+ getString(R.string.uri_LongwayPublish)
					+ getString(R.string.uri_lookuppublish_action);

			// "http://192.168.1.111:8080/CarsharingServer/ShortwayRequest!selectrequest.action?";

			StringRequest stringRequest = new StringRequest(
					Request.Method.POST, longwayway_selectpublish_baseurl,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d("longway_lookuppublish_result", response);
							// json
							// json
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d("longway_lookuppublish_result",
									error.getMessage(), error);
							Toast errorinfo = Toast.makeText(null, "网络连接失败",
									Toast.LENGTH_LONG);
							errorinfo.show();
						}
					}) {
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("role", role);
					return params;
				}
			};

			queue.add(stringRequest);

		}
	}

	/**
	 * 创建tab View
	 * 
	 * @param string
	 * @return
	 */
	private View createTabView(String name) {
		View tabView = getLayoutInflater().inflate(R.layout.tab, null);
		TextView textView = (TextView) tabView.findViewById(R.id.tab_text);
		textView.setText(name);
		return tabView;
	}

	// actionbar!!
	/* 当invalidateOptionsMenu()调用时调用 */
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // 如果nav drawer是打开的, 隐藏与内容视图相关联的action items
	// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	// return super.onPrepareOptionsMenu(menu);
	// }

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

		switch (item.getItemId()) {

		case R.id.LongwayListAdd: {
			Intent newlongway = new Intent(MainActivity.this,
					LongWayActivity.class);
			newlongway.putExtra("pre_page", "Main");
			startActivity(newlongway);
		}
			break;

		case R.id.LongwayListFind: {
		}
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	// actionbarend!!

	// actionbar操作!!

	// 添加actionbar菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.longway_list_acbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// actionbarend!!
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {

		super.onResume(); // Always call the superclass method first

		longway.setBackgroundDrawable(getResources().getDrawable(
				R.color.blue_0099cc));
		// Get the Camera instance as the activity achieves full user focus
		Context phonenumber = MainActivity.this;
		SharedPreferences filename = phonenumber
				.getSharedPreferences(
						getString(R.string.PreferenceDefaultName),
						Context.MODE_PRIVATE);
		UserPhoneNumber = filename.getString("refreshfilename", "0");
		drawernum.setText(UserPhoneNumber);
		Context context = MainActivity.this;
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
