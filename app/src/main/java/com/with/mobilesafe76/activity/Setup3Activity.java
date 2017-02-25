package com.with.mobilesafe76.activity;

import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.Utils.ToastUtils;
import com.with.safe360.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_phone_number;
	private Button bt_select_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		
		initUI();
	}
	private void initUI() {
		//��ʾ�绰����������
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		//��ȡ��ϵ�˵绰������Թ���
		String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);
		
		//���ѡ����ϵ�˵ĶԻ���
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			//1,���ص���ǰ�����ʱ��,���ܽ���ķ���
			String phone = data.getStringExtra("phone");
			//2,�������ַ�����(�л���ת���ɿ��ַ���)
			phone = phone.replace("-", "").replace(" ", "").trim();
			et_phone_number.setText(phone);
			
			//3,�洢��ϵ����sp��
			SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	//�жϷ��������Ƿ���,����ͼ��״̬
	@Override
	protected void showNextPage() {
		// TODO Auto-generated method stub
		//�����ť�Ժ�,��Ҫ��ȡ������е���ϵ��,������һҳ����
				String phone = et_phone_number.getText().toString();
				
				//��sp�洢�������ϵ���Ժ�ſ�����ת����һ������
//				String contact_phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
				if(!TextUtils.isEmpty(phone)){
					Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
					startActivity(intent);
					
					finish();
					
					//�������������绰����,����Ҫȥ����
					SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
					
				overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
				}else{
					ToastUtils.show(this,"������绰����");
				}
	}
	@Override
	protected void showPrePage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
		startActivity(intent);
		
		finish();
		
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
	}
		
}
