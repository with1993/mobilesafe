package com.with.mobilesafe76.activity;
import java.io.File;

import com.with.mobilesafe76.engine.SmsBackUp;
import com.with.mobilesafe76.engine.SmsBackUp.CallBack;
import com.with.safe360.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AToolActivity extends Activity {
	private TextView tv_query_phone_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		
		//电话归属地查询方法
		initPhoneAddress();
		//短信备份的方法
		initSmsBackUp();
		//号码百事通
		initCommonNumberQuery();
		//程序锁
		initAppLock();
	}

	private void initAppLock() {
         TextView tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);	
         tv_app_lock.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				Intent intent=new Intent(getApplicationContext(),AppLockActivity.class);
 				startActivity(intent);
 			}
 		});
	}

	/**
	 * 号码百事通的实现方法
	 */
	private void initCommonNumberQuery() {
		 TextView tv_commonnumber_query = (TextView) findViewById(R.id.tv_commonnumber_query);
		 tv_commonnumber_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),CommonNumberQueryActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initSmsBackUp() {
          TextView tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
          tv_sms_backup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSmsBackUpDialog();
			}
		});
	}

	protected void showSmsBackUpDialog() {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIcon(R.id.iv_icon);
            progressDialog.setTitle("短信备份");
            //1,指定路径
            final String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms76.xml";
          //2,指定进度条的样式为水平
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
          //3,展示进度条
            progressDialog.show();
          //4,直接调用备份短信方法即可
            new Thread(){
            	public void run() {
            		  SmsBackUp.backup(getApplicationContext(), path, new CallBack(){

						@Override
						public void setMax(int max) {
							//由开发者自己决定,使用对话框还是进度条
							progressDialog.setMax(max);
						}

						@Override
						public void setProgress(int index) {
							//由开发者自己决定,使用对话框还是进度条
							progressDialog.setProgress(index);
						}
            			  
            		  }); 
                      progressDialog.dismiss();
            	};
            }.start();
	}

	private void initPhoneAddress() {
		tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
		tv_query_phone_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
			}
		});
	}
}
