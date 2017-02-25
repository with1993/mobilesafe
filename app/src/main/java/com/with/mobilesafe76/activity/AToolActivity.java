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
		
		//�绰�����ز�ѯ����
		initPhoneAddress();
		//���ű��ݵķ���
		initSmsBackUp();
		//�������ͨ
		initCommonNumberQuery();
		//������
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
	 * �������ͨ��ʵ�ַ���
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
            progressDialog.setTitle("���ű���");
            //1,ָ��·��
            final String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms76.xml";
          //2,ָ������������ʽΪˮƽ
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
          //3,չʾ������
            progressDialog.show();
          //4,ֱ�ӵ��ñ��ݶ��ŷ�������
            new Thread(){
            	public void run() {
            		  SmsBackUp.backup(getApplicationContext(), path, new CallBack(){

						@Override
						public void setMax(int max) {
							//�ɿ������Լ�����,ʹ�öԻ����ǽ�����
							progressDialog.setMax(max);
						}

						@Override
						public void setProgress(int index) {
							//�ɿ������Լ�����,ʹ�öԻ����ǽ�����
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
