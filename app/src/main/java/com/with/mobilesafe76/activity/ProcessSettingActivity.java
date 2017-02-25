package com.with.mobilesafe76.activity;

import com.with.mobilesafe76.Service.LockScreenService;
import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.ServiceUtil;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.safe360.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessSettingActivity extends Activity {
	
	private CheckBox cb_show_system,cb_lock_clear;
             @Override
            protected void onCreate(Bundle savedInstanceState) {
            	super.onCreate(savedInstanceState);
            	setContentView(R.layout.activity_process_setting);
            	initSystemShow();
            	initLockScreenClear();
            }

			/**
			 * ����ɱ�����̵ķ��� 
			 */
			private void initLockScreenClear() {
				cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);
				//����������������Ƿ���ȥ,�����Ƿ�ѡ��ѡ��
				boolean isrunning = ServiceUtil.isRunning(getApplicationContext(), "com.with.mobilesafe76.Service.LockScreenService");
				//��ѡ�����ʾ״̬
				if(isrunning){
					cb_lock_clear.setText("���������ѿ���");
				}else
				{
					cb_lock_clear.setText("���������ѹر�");
				}
				//cb_lock_clearѡ��״̬ά��
				cb_lock_clear.setChecked(isrunning);
				
				cb_lock_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//isChecked����Ϊ�Ƿ�ѡ�е�״̬
						if(isChecked){
							cb_lock_clear.setText("���������ѿ���");
							startService(new Intent(getApplicationContext(),LockScreenService.class));
						}else
						{
							cb_lock_clear.setText("���������ѹر�");
							stopService(new Intent(getApplicationContext(),LockScreenService.class));
						}
					}
				});
			}

			/**
			 * ϵͳӦ���Ƿ���ʾ�ķ���
			 */
			private void initSystemShow() {
				cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
				//��֮ǰ�洢����״̬���л���
				boolean showSystem = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false);
				//��ѡ�����ʾ״̬
				cb_show_system.setChecked(showSystem);
				if(showSystem){
					cb_show_system.setText("��ʾϵͳ����");
				}else
				{
					cb_show_system.setText("����ϵͳ����");
				}
				
				cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//isChecked����Ϊ�Ƿ�ѡ�е�״̬
						if(isChecked){
							cb_show_system.setText("��ʾϵͳ����");
						}else
						{
							cb_show_system.setText("����ϵͳ����");
						}
						SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, isChecked);
					}
				});
			}
}
