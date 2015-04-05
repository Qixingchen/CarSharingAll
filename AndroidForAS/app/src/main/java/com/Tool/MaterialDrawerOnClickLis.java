
package com.Tool;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.xmu.carsharing.AboutActivity;
import com.xmu.carsharing.CommuteActivity;
import com.xmu.carsharing.LongWayActivity;
import com.xmu.carsharing.PersonalCenterActivity;
import com.xmu.carsharing.SettingActivity;
import com.xmu.carsharing.ShortWayActivity;

/**
 * Created by 雨蓝 on 2015/4/4.
 */
public  class MaterialDrawerOnClickLis {

	private String pageNmae = "Drawer";


	//设置抽屉的点击监听器
	public void setDrawerIntent(Activity activity, int Position,
	                         DrawerLayout mDrawerLayout) {

		switch (Position) {
			case AppStat.材料抽屉按钮序号.个人中心:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.个人中心) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent personalcenter = new Intent(activity, PersonalCenterActivity.class);
				personalcenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(personalcenter);
				break;
			case AppStat.材料抽屉按钮序号.上下班拼车:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.上下班) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent commute = new Intent(activity, CommuteActivity.class);
				commute.putExtra("pre_page", pageNmae);
				activity.startActivity(commute);
				break;
			case AppStat.材料抽屉按钮序号.短途拼车:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.短途) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent shortway = new Intent(activity, ShortWayActivity.class);
				shortway.putExtra("pre_page", pageNmae);
				activity.startActivity(shortway);
				break;
			case AppStat.材料抽屉按钮序号.长途拼车列表:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.长途) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent longway = new Intent(activity, LongWayActivity.class);
				longway.putExtra("pre_page", pageNmae);
				activity.startActivity(longway);
				break;
			case AppStat.材料抽屉按钮序号.出租车拼车:

				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.出租车) {
					mDrawerLayout.closeDrawers();
					return;
				}

				//TODO 出租车部分未完成，直接关闭抽屉
				mDrawerLayout.closeDrawers();
				break;
			case AppStat.材料抽屉按钮序号.设置:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.设置) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent setting = new Intent(activity, SettingActivity.class);
				activity.startActivity(setting);

			case AppStat.材料抽屉按钮序号.关于:
				if (activity.getClass().getSimpleName() == AppStat.抽屉跳转的类名.关于) {
					mDrawerLayout.closeDrawers();
					return;
				}

				mDrawerLayout.closeDrawers();
				Intent about = new Intent(activity, AboutActivity.class);
				activity.startActivity(about);
				break;
			default:
				Toast.makeText(activity.getApplicationContext(), "异常按动", Toast.LENGTH_LONG).show();

		}

	}
}
