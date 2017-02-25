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
		//第一次开启服务以后,就需要去管理吐司的显示
				//电话状态的监听(服务开启的时候,需要去做监听,关闭的时候电话状态就不需要监听)
				//1,电话管理者对象
				mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				//2,监听电话状态
				myPhoneStateListener = new MyPhoneStateListener();
				mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
				//获取窗体对象
				mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
				
				mScreenHeight = mWM.getDefaultDisplay().getHeight();
			    mScreenWidth = mWM.getDefaultDisplay().getWidth();
				
				//去电监听
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
				//广播接收者
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
				//挂断电话的时候窗体需要移除吐司
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
		//取消对电话状态的监听(开启服务的时候监听电话的对象)
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
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司,和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        
        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+Gravity.TOP;
        
        //吐司显示效果(吐司布局文件),xml-->view(吐司),将吐司挂在到windowManager窗体上
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
        
        //从sp中获取色值文字的索引,匹配图片,用作展示
        mDrawableIds = new int[]{
        		R.drawable.call_locate_white,
        		R.drawable.call_locate_orange,
        		R.drawable.call_locate_blue,
        		R.drawable.call_locate_gray,
        		R.drawable.call_locate_green};
        int toastStyleIndex = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(mDrawableIds[toastStyleIndex]);
        
        //在窗体上挂在一个view(权限)
        mWM.addView(mViewToast, params);
        
        //获取到了来电号码以后,需要做来电号码查询
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
