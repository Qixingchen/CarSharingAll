/*
 * progress bar加载效果
 */

package com.example.carsharing;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

public class MyProgressDialog extends Dialog {
	private Context context = null;
	private String loadingMsg = null;

	public MyProgressDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		// setCancelable(false);
		this.context = context;

		setContentView(R.layout.progress_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		setMessage("正在加载中...");
	}

	public void setMessage(String strMessage) {
		loadingMsg = strMessage;
		TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

	}

}
