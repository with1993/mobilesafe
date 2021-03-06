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
	//1,回显(读取已有的绑定状态,用作显示,sp中是否存储了sim卡的序列号)
	String sim_number = SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
	//2,判断是否序列卡号为""
	if(TextUtils.isEmpty(sim_number)){
		siv_sim_bound.setCheck(false);
	}else{
		siv_sim_bound.setCheck(true);
	}
	siv_sim_bound.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//3,获取原有的状态
			boolean isCheck = siv_sim_bound.isCheck();
			//4,将原有状态取反
			//5,状态设置给当前条目
			siv_sim_bound.setCheck(!isCheck);
			if(!isCheck){
				//6,存储(序列卡号)
				//6.1获取sim卡序列号TelephoneManager
				TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				//6.2获取sim卡的序列卡号
				String simSerialNumber = manager.getSimSerialNumber();
				//6.3存储
				SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
				
			}else{
				//7,将存储序列卡号的节点,从sp中删除掉
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
		ToastUtils.show(getApplicationContext(), "请绑定SIM卡！");
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
