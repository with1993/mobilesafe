package com.with.mobilesafe76.activity;
import com.with.mobilesafe76.Service.AddressService;
import com.with.mobilesafe76.Service.BlackNumberService;
import com.with.mobilesafe76.Service.WatchDogService;
import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.ServiceUtil;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.View.SettingClickView;
import com.with.mobilesafe76.View.SettingItemView;
import com.with.safe360.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

		private SettingItemView siv_update;
		private SettingItemView siv_address;
		private String[] mToastStyleDes;
		private int mToastStyle;
		private SettingClickView scv_toast_style;
		private SettingClickView scv_location;

		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setting);
            //��ʼ���ؼ�
            initUI();
            //�Զ�����
            initUpdate();
            //���������
            initAddress();
            //���ù����ط��
            initToastStyle();
            //���ù�������ʾ��λ��
            initLocation();
            //�������ض���
            initBlackNumber();
            //���������
            initAppLock();
        }
		private void initAppLock() {
			final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
			boolean isRunning = ServiceUtil.isRunning(this, "com.itheima.mobilesafe74.service.WatchDogService");
			siv_app_lock.setCheck(isRunning);
			
			siv_app_lock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isCheck = siv_app_lock.isCheck();
					siv_app_lock.setCheck(!isCheck);
					if(!isCheck){
						//��������
						startService(new Intent(getApplicationContext(), WatchDogService.class));
					}else{
						//�رշ���
						stopService(new Intent(getApplicationContext(), WatchDogService.class));
					}
				}
			});
		}
		/**
		 * ���غ���������
		 */
		private void initBlackNumber() {
			final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
			boolean isRunning = ServiceUtil.isRunning(getApplicationContext(), "com.with.mobilesafe76.Service.BlackNumberService");
			siv_blacknumber.setCheck(isRunning);
			
			siv_blacknumber.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isCheck = siv_blacknumber.isCheck();
					siv_blacknumber.setCheck(!isCheck);
					if(!isCheck){
						startService(new Intent(getApplicationContext(),BlackNumberService.class));
					}else{
						stopService(new Intent(getApplicationContext(),BlackNumberService.class));
					}
				}
			});
		}
		/**
		 * ˫������view������Ļλ�õĴ�����
		 */
		private void initLocation() {
			scv_location.setTitle("��������ʾ���λ��");
			scv_location.setDes("���ù�������ʾ���λ��");
			scv_location.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getApplicationContext(), ToastLocationActivity.class));
				}
			});
		}

		private void initToastStyle() {
			
			 //����(��Ʒ)
			 scv_toast_style.setTitle("���ù�������ʾ���");
			 //1,���������������ڵ�string��������
			 mToastStyleDes = new String[]{"͸��","��ɫ","��ɫ","��ɫ","��ɫ"};
			 //2,SP��ȡ��˾��ʾ��ʽ������ֵ(int),���ڻ�ȡ��������
			 
			 mToastStyle = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
			 
			 //3,ͨ������,��ȡ�ַ��������е�����,��ʾ���������ݿؼ�
			 scv_toast_style.setDes(mToastStyleDes[mToastStyle]);
			 //4,��������¼�,�����Ի���
			 scv_toast_style.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//5,��ʾ��˾��ʽ�ĶԻ���
					showToastStyleDialog();
				}
			});
		}

			protected void showToastStyleDialog() {
				Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("��ѡ���������ʽ");
				//ѡ�񵥸���Ŀ�¼�����
				/*
				 * 1:string���͵�����������ɫ��������
				 * 2:�����Ի����ʱ���ѡ����Ŀ����ֵ
				 * 3:���ĳһ����Ŀ�󴥷��ĵ���¼�
				 * */
				builder.setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {//whichѡ�е�����ֵ
						//(1,��¼ѡ�е�����ֵ,2,�رնԻ���,3,��ʾѡ��ɫֵ����)
						SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
						dialog.dismiss();
						scv_toast_style.setDes(mToastStyleDes[which]);
					}
				});
				//������ť
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			}

		/**
		 * �Ƿ�򿪹����ز�ѯ
		 */
		private void initAddress() {

			
			//�Է����Ƿ񿪵�״̬����ʾ
			boolean isRunning = ServiceUtil.isRunning(this, "com.with.activity.Service.AddressService");
			siv_address.setCheck(isRunning);
			
			//���������,״̬(�Ƿ����绰���������)���л�����
			siv_address.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//���ص��ǰ��ѡ��״̬
					boolean isCheck = siv_address.isCheck();
					siv_address.setCheck(!isCheck);
					if(!isCheck){
						//��������,������˾
						startService(new Intent(getApplicationContext(),AddressService.class));
					}else{
						//�رշ���,����Ҫ��ʾ��˾
						stopService(new Intent(getApplicationContext(),AddressService.class));
					}
				}
			});
		}

		private void initUI() {
			siv_update = (SettingItemView) findViewById(R.id.siv_update);
			siv_address = (SettingItemView) findViewById(R.id.siv_address);
			scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
			scv_location = (SettingClickView) findViewById(R.id.scv_location);
		}

		private void initUpdate() {
			boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
			//�Ƿ�ѡ��,������һ�δ洢�Ľ��ȥ������
			siv_update.setCheck(open_update);

			siv_update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//���֮ǰ��ѡ�е�,�������,���δѡ��
					//���֮ǰ��δѡ�е�,�������,���ѡ��
					
					//��ȡ֮ǰ��ѡ��״̬
					boolean ischeck = siv_update.isCheck();
					//��ԭ��״̬ȡ��,��ͬ���ߵ���������
					siv_update.setCheck(!ischeck);
					SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE,!ischeck);
				}
			});
			
			
		}

		
		

}
