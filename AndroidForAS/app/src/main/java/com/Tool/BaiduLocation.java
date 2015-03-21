package com.Tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by 雨蓝 on 2015/3/16.
 * 务必调用暂停和停止.但也要记得调用 resume
 * 获取城市需要实现 GetCityCallBack 接口
 * 获取精确地址需要实现 GetHignLocationCallBack 接口
 */
public class BaiduLocation {

	public LocationClient mLocationClient = null;
	private BDLocationListener getcityLocation = new GetcityLocationListener();
	private BDLocationListener getHignLocation = new GetHighLocationListener();

	public String UserCity = null;


	//回调函数
	private GetCityCallBack getCityCallBack;
	private GetHignLocationCallBack getHignLocationCallBack;

	//class内部用
	private Application mapplication;
	private Context mcontext;
	private Activity mactivity;
	private String logtag = "百度定位";

	public BaiduLocation(Activity mact) {
		mactivity = mact;
		mapplication = mact.getApplication();
		mcontext = mapplication.getApplicationContext();
		initBDLocation();
	}

	// 初始化定位模块
	//注意，不负责设置定位选项，不设置监听器
	//构造时已调用
	private void initBDLocation() {

		mLocationClient = new LocationClient(mcontext);     //声明LocationClient类
	}

	//判断并重启定位
	private boolean shouldRestartAndDo() {
		if (mLocationClient.isStarted()) {
			return false;
		} else {
			mLocationClient.start();
			//return true;//TODO 这里会造成死循环！
			return false;
		}
	}

	//启动精确定位,传入回调函数
	public void getHignLocation( GetHignLocationCallBack gethlcb ) {
		mLocationClient.stop();
		Log.w(logtag,"获取精确地址中");
		mLocationClient.unRegisterLocationListener(getcityLocation);
		mLocationClient.registerLocationListener(getHignLocation);    //注册监听函数
		mLocationClient.setLocOption(setoption(AppStat.百度定位设置选项.精确定位));
		getHignLocationCallBack = gethlcb;
		mLocationClient.start();

	}


	//获取用户城市,传入回调函数
	public void getUserCity(GetCityCallBack getccb) {
		mLocationClient.stop();
		Log.w(logtag,"获取用户城市中");
		mLocationClient.unRegisterLocationListener(getHignLocation);
		mLocationClient.registerLocationListener(getcityLocation);    //注册监听函数
		LocationClientOption options = setoption(AppStat.百度定位设置选项.定位城市);
		mLocationClient.setLocOption(options);
		getCityCallBack = getccb;
		mLocationClient.start();
	}


	//获取城市的监听器
	private class GetcityLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if (location.getCity() == null) {
				mLocationClient.stop();
				Log.e(logtag, "城市名空!");
			} else {
				UserCity = location.getCity();
				getCityCallBack.getcityname(UserCity);
				Log.w(logtag+"城市函数", "城市名"+UserCity);
				mLocationClient.stop();
			}

		}
	}

	//精确定位的监听器
	private class GetHighLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			} else {
				mLocationClient.stop();
				getHignLocationCallBack.getHignLocationCallback(location.getLongitude()
						,location.getLatitude(),location.getAddrStr());
				Log.e(logtag,"获取地址"+location.getAddrStr());
			}


		}
	}

	//定位设置
	private LocationClientOption setoption(int whatToLocation) {
		LocationClientOption options = new LocationClientOption();

		options.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
		options.setScanSpan(1200);// 设置发起定位请求的间隔时间为5000ms
		options.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		options.setAddrType("all");
		options.setProdName("厦大拼车");
		options.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向

		if (whatToLocation == AppStat.百度定位设置选项.定位城市) {
			options.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
		} else {
			options.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		}
		return options;

	}

	//获取城市的回调接口
	public interface GetCityCallBack {
		public String getcityname(String city);
	}

	//精确定位的回调接口
	public interface GetHignLocationCallBack {
		public void getHignLocationCallback(double longitude,double latitude,String addr);
	}


	public void Stop() {
		mLocationClient.stop();
	}

	public void Destory() {
		mLocationClient.stop();
	}

	public void Resume() {
		mLocationClient.start();
	}


}
