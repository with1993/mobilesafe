package com.with.mobilesafe76.View;

import com.with.safe360.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private CheckBox cb_box;
	private TextView tv_des;
	private static final String tag = "SettingItemView";
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.with.safe360";
	private String mDestitle;
	private String mDesoff;
	private String mDeson;

	public SettingItemView(Context context) {
		this(context, null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.setting_item_view, this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);

		// ��ȡ�Զ���ؼ�
		initAttrs(attrs);
		
		tv_title.setText(mDestitle);
	}

	private void initAttrs(AttributeSet attrs) {
		//ͨ�����ռ�+�������ƻ�ȡ����ֵ
		mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
		
		Log.i(tag, mDestitle);
		Log.i(tag, mDesoff);
		Log.i(tag, mDeson);
	}

	/**
	 * �ж��Ƿ����ķ���
	 * 
	 * @return ���ص�ǰSettingItemView�Ƿ�ѡ��״̬ true����(checkBox����false)
	 *         false�ر�(checkBox����true)
	 */

	public boolean isCheck() {
		return cb_box.isChecked();
	}

	/**
	 * @param isCheck
	 *            �Ƿ���Ϊ�����ı���,�ɵ��������ȥ������
	 */
	public void setCheck(boolean isCheck) {
		cb_box.setChecked(isCheck);
		// ��ǰ��Ŀ��ѡ��Ĺ�����,cb_boxѡ��״̬Ҳ�ڸ���(isCheck)�仯
		if (isCheck) {
			tv_des.setText(mDeson);
		} else {
			tv_des.setText(mDesoff);
		}
	}

}
