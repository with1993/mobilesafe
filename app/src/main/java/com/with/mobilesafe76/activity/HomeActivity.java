package com.with.mobilesafe76.activity;


import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.Md5Util;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.Utils.ToastUtils;
import com.with.safe360.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private String[] mTitleStrs;
	private int[] mDrawableIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// ��ʼ���ؼ�
		initUI();
		// ��ʼ������
		initData();

	}

	private void initData() {
		mTitleStrs = new String[] { "�ֻ�����", "ͨ����ʿ", "�������", "���̹���", "����ͳ��",
				"�ֻ�ɱ��", "��������", "�߼�����", "��������" };
		mDrawableIds = new int[] { R.drawable.home_safe,
				R.drawable.home_callmsgsafe, R.drawable.home_apps,
				R.drawable.home_taskmanager, R.drawable.home_netmanager,
				R.drawable.home_trojan, R.drawable.home_sysoptimize,
				R.drawable.home_tools, R.drawable.home_settings };

		// �Ź���ؼ���������������(��ͬListView����������)
		gv_home.setAdapter(new MyAdapter());
		// ע��Ź��񵥸���Ŀ����¼�
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// �����Ի���
					showDialog();
					break;
				case 1:
					//��ת��ͨ����ʿ����
					startActivity(new Intent(getApplicationContext(), BlackNumberActivity.class));
					break;
				case 2:
					//��ת���������ģ��
					startActivity(new Intent(getApplicationContext(), AppManagerActivity.class));
					break;
				case 3:
					//��ת�����̹���ģ��
					startActivity(new Intent(getApplicationContext(), ProcessManagerActivity.class));
					break;
				case 4:
					//��ת���������ģ��
					startActivity(new Intent(getApplicationContext(), TrafficActivity.class));
					break;
				case 5:
					//��ת������ɨ��ģ��
					startActivity(new Intent(getApplicationContext(), AnitVirusActivity.class));
					break;
				case 6:
					//��ת����������ģ��
//					startActivity(new Intent(getApplicationContext(), CacheClearActivity.class));
					startActivity(new Intent(getApplicationContext(), BaseCacheClearActivity.class));
					break;
				case 7:
					//��ת���߼����߹����б����
					startActivity(new Intent(getApplicationContext(), AToolActivity.class));
					break;
				case 8:
					//��ת���������Ľ���
					Intent intent = new Intent(getApplicationContext(),
							SettingActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	/**
	 * �ж��Ƿ��һ�ν���
	 */
	protected void showDialog() {
		String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
		if (TextUtils.isEmpty(psd)) {
			// 1.��һ�ν��룬��������
			showSetPsdDialog();

		} else {
			// 2.ȷ���Ի�����������
			showConfirmPsdDialog();
		}
	}

	/**
	 * ȷ������Ի���
	 */
	private void showConfirmPsdDialog() {

		//��Ϊ��Ҫȥ�Լ�����Ի����չʾ��ʽ,������Ҫ����dialog.setView(view);
		//view�����Լ���д��xmlת���ɵ�view����xml----->view
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		
		final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
		//�öԻ�����ʾһ���Լ�����ĶԻ������Ч��
//		dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et_confirm_psd = (EditText)view.findViewById(R.id.et_confirm_psd);
				
				String confirmPsd = et_confirm_psd.getText().toString();
				
				if(!TextUtils.isEmpty(confirmPsd)){
					//���洢��sp��32λ������,��ȡ����,Ȼ�����������ͬ������md5,Ȼ����sp�д洢����ȶ�
					String psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
					
					if(psd.equals(Md5Util.encoder(confirmPsd))){
						//����Ӧ���ֻ�����ģ��,����һ���µ�activity
//						Intent intent = new Intent(getApplicationContext(), TestActivity.class);
						Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
						startActivity(intent);
						//��ת���µĽ����Ժ���Ҫȥ���ضԻ���
						dialog.dismiss();
					}else{
						ToastUtils.show(getApplicationContext(),"ȷ���������");
					}
				}else{
					//��ʾ�û�����������Ϊ�յ����
					ToastUtils.show(getApplicationContext(), "����������");
				}
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	
	}
	/**
	 * ��������Ի���
	 */
	private void showSetPsdDialog() {
		// ��Ϊ��Ҫȥ�Լ�����Ի����չʾ��ʽ,������Ҫ����dialog.setView(view);
		// view�����Լ���д��xmlת���ɵ�view����xml----->view
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		final View view = View.inflate(this, R.layout.dialog_set_psd, null);
		// �öԻ�����ʾһ���Լ�����ĶԻ������Ч��
		dialog.setView(view);
		dialog.show();
		
		Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView et_set_psd = (TextView) view.findViewById(R.id.et_set_psd);
				TextView et_confirm_psd = (TextView) view.findViewById(R.id.et_confirm_psd);
				
				String confirmPsd = et_confirm_psd.getText().toString();
				String psd = et_set_psd.getText().toString();
				
				if(!TextUtils.isEmpty(confirmPsd)&&!TextUtils.isEmpty(psd)){
					if(psd.equals(confirmPsd)){
						Intent intent=new Intent(getApplicationContext(),SetupOverActivity.class);
						startActivity(intent);
						dialog.dismiss();
						SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(confirmPsd));
					}else{
						ToastUtils.show(getApplicationContext(), "������������벻һ����");
					}
				}else{
					ToastUtils.show(getApplicationContext(), "��������벻��Ϊ�գ�");
				}
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	

	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mTitleStrs.length;
		}

		@Override
		public Object getItem(int position) {

			return mTitleStrs[position];
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.gridview_item, null);
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

			tv_title.setText(mTitleStrs[position]);
			iv_icon.setBackgroundResource(mDrawableIds[position]);
			return view;
		}
	}
}
