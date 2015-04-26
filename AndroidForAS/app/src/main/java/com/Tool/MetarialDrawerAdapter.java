package com.Tool;

/**
 * Created by 雨蓝 on 2015/4/4.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmu.carsharing.R;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MetarialDrawerAdapter extends RecyclerView.Adapter<MetarialDrawerAdapter.ViewHolder> {

	private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
	// IF the view under inflation and population is header or Item
	private static final int TYPE_ITEM = 1;

	private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
	private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

	private String name;        //String Resource for header View Name
	private Uri profile;        //int Resource for header view profile picture
	private String email;       //String Resource for header view email

	//activity生命量
	private Context context;
	private Activity activity;
	DrawerLayout drawerLayout;

	public MetarialDrawerAdapter(String Titles[], int Icons[], String Name,
	                             String Email, Uri Profile,
	                             Activity passedActivity, DrawerLayout drawerLayout) { // MetarialDrawerAdapter Constructor with titles and icons parameter
		// titles, icons, name, email, profile pic are passed from the main activity as we
		mNavTitles = Titles;                //have seen earlier
		mIcons = Icons;
		name = Name;
		email = Email;
		profile = Profile;                     //here we assign those passed values to the values we declared here
		//in adapter
		this.activity = passedActivity;
		this.drawerLayout = drawerLayout;
	}

	// Creating a ViewHolder which extends the RecyclerView View Holder
	// ViewHolder are used to to store the inflated views in order to recycle them

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		int Holderid;

		TextView textView;
		ImageView imageView;
		ImageView profile;
		TextView Name;
		TextView email;
		Activity activityinviewhold;
		Context contextinviewhold;
		MaterialDrawerOnClickLis materialDrawerOnClickLis;
		DrawerLayout drawerLayout;

		public ViewHolder(View itemView, int ViewType, Activity ac,
		                  DrawerLayout mdrawerLayout) {

			super(itemView);
			activityinviewhold = ac;
			contextinviewhold = ac.getApplicationContext();
			itemView.setClickable(true);
			itemView.setOnClickListener(this);
			drawerLayout = mdrawerLayout;
			materialDrawerOnClickLis = new MaterialDrawerOnClickLis();


			if (ViewType == TYPE_ITEM) {
				textView = (TextView) itemView.findViewById(R.id.rowText);
				imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
				Holderid = 1;
			} else {


				Name = (TextView) itemView.findViewById(R.id.name);
				email = (TextView) itemView.findViewById(R.id.email);
				profile = (ImageView) itemView.findViewById(R.id.circleView);
				Holderid = 0;
			}
		}

		@Override
		public void onClick(View v) {

			materialDrawerOnClickLis.setDrawerIntent(activityinviewhold, getPosition(), drawerLayout);


		}

	}

	//Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
	//Created, In this method we inflate the md_drawer_item_rowr_item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
	// if the viewType is TYPE_HEADER
	// and pass it to the view holder

	@Override
	public MetarialDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_drawer_item_row, parent, false); //Inflating the layout

			ViewHolder vhItem = new ViewHolder(v, viewType, activity, drawerLayout); //Creating ViewHolder and passing the object of type view

			return vhItem; // Returning the created object

			//inflate your layout and pass it to view holder

		} else if (viewType == TYPE_HEADER) {

			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_toolbar_header, parent,
					false); //Inflating the layout

			ViewHolder vhHeader = new ViewHolder(v, viewType, activity, drawerLayout); //Creating ViewHolder and passing the object of type view

			return vhHeader; //returning the object created


		}
		return null;

	}

	@Override
	public void onBindViewHolder(MetarialDrawerAdapter.ViewHolder holder, int position) {
		if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
			// position by 1 and pass it to the holder while setting the text and image
			holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
			holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
		} else {
			try {
				holder.profile.setImageURI(profile);
			} catch (Exception e) {
				Log.e("材料主题", "用户图片不存在");
			}
			// Similarly we set the resources
			// for header
			// view
			holder.Name.setText(name);
			holder.email.setText(email);
		}
	}

	// This method returns the number of items present in the list
	@Override
	public int getItemCount() {
		return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
	}


	// Witht the following method we check what type of view is being passed
	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

}
