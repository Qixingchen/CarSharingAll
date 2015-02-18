package com.Tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.widget.Toast;

import com.xmu.carsharing.R;
import com.xmu.carsharing.RegisterSecondActivity;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 雨蓝 on 2015/2/9.
 * @author 雨蓝
 * 初始化时需要context
 * 务必记得销毁destory()
 *
 */
public class SmssdkClass {
	private Context mcontext;
	private String Countriescode = "86";
	private String PhoneNum;

	private EventHandler smssdkEveHandlerForResgister = new EventHandler(){
		@Override
		public void afterEvent(int event, int result, Object data) {
			if (result == SMSSDK.RESULT_COMPLETE){
				startActivityRegisterSec();
			}else if ( result == SMSSDK.RESULT_ERROR){
				Looper.prepare();
				Toast.makeText(mcontext,mcontext.getString(R.string
						.warningInfo_smsAuthCodeError),Toast.LENGTH_LONG).show();
				Looper.loop();
			}

		}
	};

	//初始化smssdk
	public SmssdkClass(final Context mcontext) {
		this.mcontext = mcontext;
		SMSSDK.initSDK(mcontext,"5ba14c5d6110","50c98f764679a7774d12e440d093bb4d");
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {

				if (result == SMSSDK.RESULT_COMPLETE) {
					//回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						//提交验证码成功
					}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
						//获取验证码成功
					}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
						//返回支持发送验证码的国家列表
					}
				}else{
					((Throwable)data).printStackTrace();
				}
			}
		};
		SMSSDK.registerEventHandler(eh); //注册短信回调
	}


	//发送短信
	public void sendsms(String phonenum){
		//SMSSDK.getSupportedCountries();
		SMSSDK.getVerificationCode(Countriescode,phonenum);
		Toast.makeText(mcontext,mcontext.getString(R.string
				.warningInfo_smsAuthCodeSend),Toast.LENGTH_LONG).show();
	}

	//提交验证验证码
	public void versmsvercode(String phonenum,String vercode){
		SMSSDK.submitVerificationCode(Countriescode,phonenum,vercode);
		PhoneNum = phonenum;
		SMSSDK.registerEventHandler(smssdkEveHandlerForResgister);
	}

	public void destory(){
		SMSSDK.unregisterEventHandler(smssdkEveHandlerForResgister);
	}

	private void startActivityRegisterSec(){
		SharedPreferences sharedPref = mcontext
				.getSharedPreferences("file_text",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("refreshfilename", PhoneNum);
		editor.commit();

		Intent next = new Intent(mcontext,
				RegisterSecondActivity.class);

		next.putExtra(mcontext.getString(R.string.user_phonenum), PhoneNum);// 传值

		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mcontext.startActivity(next);
	}


}
