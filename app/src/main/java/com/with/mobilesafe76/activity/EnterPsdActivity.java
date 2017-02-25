package com.with.mobilesafe76.activity;


import com.with.mobilesafe76.Utils.ToastUtils;
import com.with.safe360.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPsdActivity extends Activity {
	private String packagename;
	private TextView tv_app_name;
	private ImageView iv_app_icon;
	private EditText et_psd;
	private Button bt_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//��ȡ����
		packagename = getIntent().getStringExtra("packagename");
		setContentView(R.layout.activity_enter_psd);
		initUI();
		initData();
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		//ͨ�����ݹ����İ�����ȡ����Ӧ�õ�ͼ���Լ�����
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packagename,0);
			Drawable icon = applicationInfo.loadIcon(pm);
			iv_app_icon.setBackgroundDrawable(icon);
			tv_app_name.setText(applicationInfo.loadLabel(pm).toString());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String psd = et_psd.getText().toString();
				if(!TextUtils.isEmpty(psd)){
					if(psd.equals("123")){
						//����,����Ӧ��,��֪���ſڲ�Ҫ��ȥ�����Լ�������Ӧ��,���͹㲥
						Intent intent = new Intent("android.intent.action.SKIP");
						intent.putExtra("packagename",packagename);
						sendBroadcast(intent);
						
						finish();
					}else{
						ToastUtils.show(getApplicationContext(), "�������");
					}
				}else{
					ToastUtils.show(getApplicationContext(), "����������");
				}
			}
		});
	}

	private void initUI() {
		tv_app_name = (TextView) findViewById(R.id.tv_app_name);
		iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
		
		et_psd = (EditText) findViewById(R.id.et_psd);
		bt_submit = (Button) findViewById(R.id.bt_submit);
	}
	
	@Override
	public void onBackPressed() {
		//ͨ����ʽ��ͼ,��ת������
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		super.onBackPressed();
	}
}

