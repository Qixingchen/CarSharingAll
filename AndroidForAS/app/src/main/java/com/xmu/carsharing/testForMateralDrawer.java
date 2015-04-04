package com.xmu.carsharing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.Tool.MaterialDrawer;


public class testForMateralDrawer extends ActionBarActivity {


	private Toolbar toolbar;
	private MaterialDrawer materialDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_for_materal_drawer);
		toolbar = (Toolbar)findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		materialDrawer = new MaterialDrawer(this,toolbar);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_test_for_materal_drawer, menu);
		return true;
	}


}
