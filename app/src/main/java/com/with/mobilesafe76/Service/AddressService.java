package com.with.mobilesafe76.Service;

import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.engine.AddressDao;
import com.with.safe360.R;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {

	private TelephonyManager mTM;
	private MyPhoneStateListener myPhoneStateListener;
	private WindowManager mWM;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mViewToast;
	private String mAddress;
	private TextView tv_toast;
	private int[] mDrawableIds;
	
	@SuppressWarnings("unused")
	private int mScreenHeight;
	@SuppressWarnings("unused")
	private int mScreenWidth;
	private OutCallReceiver outCallReceiver;
	@SuppressLint("HandlerLeak") private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			tv_toast.setText(mAddress);
		};
	};
	
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		//��һ�ο��������Ժ�,����Ҫȥ������˾����ʾ
				//�绰״̬�ļ���(��������ʱ��,��Ҫȥ������,�رյ�ʱ��绰״̬�Ͳ���Ҫ����)
				//1,�绰�����߶���
				mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				//2,�����绰״̬
				myPhoneStateListener = new MyPhoneStateListener();
				mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
				//��ȡ�������
				mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
				
				mScreenHeight = mWM.getDefaultDisplay().getHeight();
			    mScreenWidth = mWM.getDefaultDisplay().getWidth();
				
				//ȥ�����
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
				//�㲥������
				outCallReceiver = new OutCallReceiver();
				registerReceiver(outCallReceiver, intentFilter);
				
				super.onCreate();
	}
	
	class OutCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String resultData = getResultData();
			showToast(resultData);
		}
	}
	
	class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE :
				//�Ҷϵ绰��ʱ������Ҫ�Ƴ���˾
				if(mWM!=null && mViewToast!=null){
					mWM.removeView(mViewToast);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK :
				
				break;
			case TelephonyManager.CALL_STATE_RINGING :
				showToast(incomingNumber);
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	
	@Override
	public void onDestroy() {
		//ȡ���Ե绰״̬�ļ���(���������ʱ������绰�Ķ���)
				if(mTM!=null && myPhoneStateListener!=null){
					mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
				}
				if(outCallReceiver!=null){
				   unregisterReceiver(outCallReceiver);
				}
		super.onDestroy();
	}
	public void showToast(String incomingNumber) {
	    final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	Ĭ���ܹ�������
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //�������ʱ����ʾ��˾,�͵绰����һ��
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        
        //ָ����˾������λ��(����˾ָ�������Ͻ�)
        params.gravity = Gravity.LEFT+Gravity.TOP;
        
        //��˾��ʾЧ��(��˾�����ļ�),xml-->view(��˾),����˾���ڵ�windowManager������
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
        
        //��sp�л�ȡɫֵ���ֵ�����,ƥ��ͼƬ,����չʾ
        mDrawableIds = new int[]{
        		R.drawable.call_locate_white,
        		R.drawable.call_locate_orange,
        		R.drawable.call_locate_blue,
        		R.drawable.call_locate_gray,
        		R.drawable.call_locate_green};
        int toastStyleIndex = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(mDrawableIds[toastStyleIndex]);
        
        //�ڴ����Ϲ���һ��view(Ȩ��)
        mWM.addView(mViewToast, params);
        
        //��ȡ������������Ժ�,��Ҫ����������ѯ
        query(incomingNumber);
	}
	private void query(final String incomingNumber) {
		
		new Thread(){
			public void run() {
				mAddress = AddressDao.getAddress(incomingNumber);
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}
 
}
