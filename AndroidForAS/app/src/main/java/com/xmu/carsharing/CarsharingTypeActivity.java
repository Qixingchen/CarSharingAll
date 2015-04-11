/*
 * 拼车类型选择界面，实现简单的界面跳转
 */

package com.xmu.carsharing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CarsharingTypeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carsharing_type);

		// actionbar操作!!

		// 绘制向上!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		View workcs = findViewById(R.id.WorkCS);
		View shortcs = findViewById(R.id.ShortCS);
		View longcs = findViewById(R.id.LongCS);

		longcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent longcs = new Intent(CarsharingTypeActivity.this,
						OrderActivity.class);
				longcs.putExtra("pre_page", "CarsharingType");
				longcs.putExtra("cstype", "longcs");
				startActivity(longcs);
			}
		});
		shortcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent shortcs = new Intent(CarsharingTypeActivity.this,
						OrderActivity.class);
				shortcs.putExtra("pre_page", "CarsharingType");
				shortcs.putExtra("cstype", "shortcs");
				startActivity(shortcs);
			}
		});

		workcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent workcs = new Intent(CarsharingTypeActivity.this,
						OrderActivity.class);
				workcs.putExtra("pre_page", "CarsharingType");
				workcs.putExtra("cstype", "workcs");
				startActivity(workcs);
			}
		});

	}

}
