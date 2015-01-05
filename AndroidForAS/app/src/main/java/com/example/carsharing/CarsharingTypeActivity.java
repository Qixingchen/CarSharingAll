/*
 * ƴ������ѡ����棬ʵ�ּ򵥵Ľ�����ת
 */

package com.example.carsharing;

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

		// actionbar����!!

		// ��������!!
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// actionbarEND!!

		View workcs = findViewById(R.id.WorkCS);
		View shortcs = findViewById(R.id.ShortCS);
		View longcs = findViewById(R.id.LongCS);

		longcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent longcs = new Intent(CarsharingTypeActivity.this,
						LongWayActivity.class);
				longcs.putExtra("pre_page", "CarsharingType");
				startActivity(longcs);
			}
		});
		shortcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent shortcs = new Intent(CarsharingTypeActivity.this,
						ShortWayActivity.class);
				shortcs.putExtra("pre_page", "CarsharingType");
				startActivity(shortcs);
			}
		});

		workcs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent workcs = new Intent(CarsharingTypeActivity.this,
						CommuteActivity.class);
				workcs.putExtra("pre_page", "CarsharingType");
				startActivity(workcs);
			}
		});

	}

}
