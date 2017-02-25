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

		// 获取自定义控件
		initAttrs(attrs);
		
		tv_title.setText(mDestitle);
	}

	private void initAttrs(AttributeSet attrs) {
		//通过名空间+属性名称获取属性值
		mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
		
		Log.i(tag, mDestitle);
		Log.i(tag, mDesoff);
		Log.i(tag, mDeson);
	}

	/**
	 * 判断是否开启的方法
	 * 
	 * @return 返回当前SettingItemView是否选中状态 true开启(checkBox返回false)
	 *         false关闭(checkBox返回true)
	 */

	public boolean isCheck() {
		return cb_box.isChecked();
	}

	/**
	 * @param isCheck
	 *            是否作为开启的变量,由点击过程中去做传递
	 */
	public void setCheck(boolean isCheck) {
		cb_box.setChecked(isCheck);
		// 当前条目在选择的过程中,cb_box选中状态也在跟随(isCheck)变化
		if (isCheck) {
			tv_des.setText(mDeson);
		} else {
			tv_des.setText(mDesoff);
		}
	}

}
