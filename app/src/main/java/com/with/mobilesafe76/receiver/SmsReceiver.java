package com.with.mobilesafe76.receiver;

import com.with.mobilesafe76.Service.LocationService;
import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.safe360.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//1,�ж��Ƿ����˷�������
		boolean open_security = SpUtil.getBoolean(context,ConstantValue.OPEN_SECURITY, false);
		if(open_security){
			//2,��ȡ��������
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			//3,ѭ���������Ź���
			for (Object object : objects) {
				//4,��ȡ���Ŷ���
				SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
				//5,��ȡ���Ŷ���Ļ�����Ϣ
				//String originatingAddress = sms.getOriginatingAddress();
				String messageBody = sms.getMessageBody();
				
				//6,�ж��Ƿ�����������ֵĹؼ���
				if(messageBody.contains("#*alarm*#")){
					//7,��������(׼������,MediaPlayer)
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
					mediaPlayer.setLooping(true);
					mediaPlayer.start();
				}
				
				if(messageBody.contains("#*location*#")){
					//8,������ȡλ�÷���
					context.startService(new Intent(context,LocationService.class));
				}
				
				if(messageBody.contains("#*lockscrenn*#")){
					
				}
				if(messageBody.contains("#*wipedate*#")){
					
				}
			}
		}
	}
}
