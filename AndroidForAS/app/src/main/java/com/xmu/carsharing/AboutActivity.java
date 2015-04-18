//设置。。。具体功能未实现

package com.xmu.carsharing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.Tool.MaterialDrawer;

public class AboutActivity extends ActionBarActivity {

	// actionbar!!
	private Toolbar toolbar;
	// actionbarend!!

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

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
