package com.with.mobilesafe76.activity;

import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.Utils.ToastUtils;
import com.with.mobilesafe76.View.SettingItemView;
import com.with.safe360.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends BaseSetupActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_setup2);
	initDate();
}
private void initDate() {
	final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
	//1,����(��ȡ���еİ�״̬,������ʾ,sp���Ƿ�洢��sim�������к�)
	String sim_number = SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
	//2,�ж��Ƿ����п���Ϊ""
	if(TextUtils.isEmpty(sim_number)){
		siv_sim_bound.setCheck(false);
	}else{
		siv_sim_bound.setCheck(true);
	}
	siv_sim_bound.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//3,��ȡԭ�е�״̬
			boolean isCheck = siv_sim_bound.isCheck();
			//4,��ԭ��״̬ȡ��
			//5,״̬���ø���ǰ��Ŀ
			siv_sim_bound.setCheck(!isCheck);
			if(!isCheck){
				//6,�洢(���п���)
				//6.1��ȡsim�����к�TelephoneManager
				TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				//6.2��ȡsim�������п���
				String simSerialNumber = manager.getSimSerialNumber();
				//6.3�洢
				SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
				
			}else{
				//7,���洢���п��ŵĽڵ�,��sp��ɾ����
				SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
			}
		}
	});
}

@Override
protected void showNextPage() {
	String sim = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
	if(!TextUtils.isEmpty(sim)){
		Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
		startActivity(intent);	
		finish();
		overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
	}else{
		ToastUtils.show(getApplicationContext(), "���SIM����");
	}

}
@Override
protected void showPrePage() {
	Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
	startActivity(intent);
	
	finish();
	overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
}
}