/*
 * 设置
 */

package com.xmu.carsharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.Tool.MaterialDrawer;

public class SettingActivity extends AppCompatActivity {

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//actionbar
		toolbar = (Toolbar)findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		new MaterialDrawer(this,toolbar);
		//actionbar end

	}

	@Override
	public void onResume() {

		super.onResume();

	}
}
