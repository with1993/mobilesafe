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
			 * 锁屏杀死进程的方法 
			 */
			private void initLockScreenClear() {
				cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);
				//根据锁屏清理服务是否开启去,决定是否单选框选中
				boolean isrunning = ServiceUtil.isRunning(getApplicationContext(), "com.with.mobilesafe76.Service.LockScreenService");
				//单选框的显示状态
				if(isrunning){
					cb_lock_clear.setText("锁屏清理已开启");
				}else
				{
					cb_lock_clear.setText("锁屏清理已关闭");
				}
				//cb_lock_clear选中状态维护
				cb_lock_clear.setChecked(isrunning);
				
				cb_lock_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//isChecked就作为是否选中的状态
						if(isChecked){
							cb_lock_clear.setText("锁屏清理已开启");
							startService(new Intent(getApplicationContext(),LockScreenService.class));
						}else
						{
							cb_lock_clear.setText("锁屏清理已关闭");
							stopService(new Intent(getApplicationContext(),LockScreenService.class));
						}
					}
				});
			}

			/**
			 * 系统应用是否显示的方法
			 */
			private void initSystemShow() {
				cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
				//对之前存储过的状态进行回显
				boolean showSystem = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false);
				//单选框的显示状态
				cb_show_system.setChecked(showSystem);
				if(showSystem){
					cb_show_system.setText("显示系统进程");
				}else
				{
					cb_show_system.setText("隐藏系统进程");
				}
				
				cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//isChecked就作为是否选中的状态
						if(isChecked){
							cb_show_system.setText("显示系统进程");
						}else
						{
							cb_show_system.setText("隐藏系统进程");
						}
						SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, isChecked);
					}
				});
			}
}
