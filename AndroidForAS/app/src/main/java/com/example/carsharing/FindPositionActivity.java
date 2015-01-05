/*
 * 地图显示界面
 */

package com.example.carsharing;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import android.app.Activity;
import android.os.Bundle;

public class FindPositionActivity extends Activity {
	MapView mapview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_find_position);
		mapview = (MapView) findViewById(R.id.bmapView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapview.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapview.onPause();
	}

}
