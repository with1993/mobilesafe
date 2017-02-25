package com.with.mobilesafe76.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.with.mobilesafe76.Utils.ConstantValue;
import com.with.mobilesafe76.Utils.SpUtil;
import com.with.mobilesafe76.Utils.StreamUtil;
import com.with.mobilesafe76.Utils.ToastUtils;
import com.with.safe360.R;

public class SplashActivity extends Activity {

	protected static final String tag = "SplashActivity";
	protected static final int UPDATE_VISION = 100;
	protected static final int ENTER_HOME = 101;
	protected static final int ERROR_E = 102;
	private TextView tv_version_name;
	private int mLocationVersionCode;
	private String mVersionDes;
	private String mDownloadUrl;
	private RelativeLayout rl;

	
	    @SuppressLint("HandlerLeak") Handler mHandler=new Handler(){
	    	public void handleMessage(android.os.Message msg) {
	    		switch (msg.what) {
				case UPDATE_VISION:
					showUpdateDownDialog();
					break;
				case ENTER_HOME:
					//进入应用程序主界面,activity跳转过程
					enterHome();
					break;
				case ERROR_E:
					ToastUtils.show(getApplicationContext(), "更新失败，请检查服务器是否开启！");
					enterHome();
					break;
				}
	    	};
	    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		//初始化UI
        initUI();
        //初始化数据
        initData();
        //开始动画
        initAnimation();
        //初始化数据库
        initDB();
        //快捷方式的生成
        if(!SpUtil.getBoolean(this, ConstantValue.HAS_SHORTCUT, false)){
        	//生成快捷方式
        	initShortCut();
        } 
     
	}


	/**
	 * 生成快捷方式的方法
	 */
	private void initShortCut() {
		//1,给intent维护图标,名称
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//维护图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		//名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "安全卫士快捷方式");
		//2,点击快捷方式后跳转到的activity
		//2.1维护开启的意图对象
		Intent shortCutIntent = new Intent("android.intent.action.HOME");
		shortCutIntent.addCategory("android.intent.category.DEFAULT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		//3.发送广播
		sendBroadcast(intent);
		//4.告知sp已经生成快捷方式
		SpUtil.putBoolean(this, ConstantValue.HAS_SHORTCUT, true);
	}


	private void initDB() {
		//1,归属地数据拷贝过程
				initAddressDB("address.db");
				initAddressDB("commonnum.db");
				//3,拷贝病毒数据库
				initAddressDB("antivirus.db");
	}


	private void initAddressDB(String dbName) {
		//1,在files文件夹下创建同名dbName数据库文件过程
				File files = getFilesDir();
				File file = new File(files, dbName);
				if(file.exists()){
					return;
				}
				InputStream stream = null;
				FileOutputStream fos = null;
				//2,输入流读取第三方资产目录下的文件
				try {
					stream = getAssets().open(dbName);
					//3,将读取的内容写入到指定文件夹的文件中去
					fos = new FileOutputStream(file);
					//4,每次的读取内容大小
					byte[] bs = new byte[1024];
					int temp = -1;
					while( (temp = stream.read(bs))!=-1){
						fos.write(bs, 0, temp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(stream!=null && fos!=null){
						try {
							stream.close();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
	}


	/**
	 * 欢迎界面的弹出动画
	 */
	private void initAnimation() {
		 AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1);
		 alphaAnimation.setDuration(4000);
		// alphaAnimation.setRepeatMode(Animation.RESTART);
		 rl.startAnimation(alphaAnimation);
	}


	/**
	 * 弹出对话框,提示用户更新
	 */
	protected void showUpdateDownDialog() {
	            //    Builder builder=new AlertDialog.Builder(this);
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新咯");
		builder.setMessage(mVersionDes);
		builder.setPositiveButton("立即更新",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk,apk链接地址,downloadUrl
				downloadApk();
			}
		});
		builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
		
				enterHome();
			}
		});
		//点击取消事件监听
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//即使用户点击取消,也需要让其进入应用程序主界面
				enterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 下载Apk的方法
	 */
	protected void downloadApk() {
	
		//apk下载链接地址,放置apk的所在路径
		
				//1,判断sd卡是否可用,是否挂在上
		   if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		   {
			   //获取路径
			   String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"mobilesafe76.apk";
			 //3,发送请求,获取apk,并且放置到指定路径
			   HttpUtils httpUtils=new HttpUtils();
			 //4,发送请求,传递参数(下载地址,下载应用放置位置)
			   httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
			
					//下载成功(下载过后的放置在sd卡中apk)
					Log.i(tag, "下载成功！");
					File file = responseInfo.result;
					//提示用户安装
					installApk(file);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
			
					Log.i(tag, "下载失败！");

				}
				@Override
				public void onStart() {
			
					Log.i(tag, "刚刚开始下载");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					Log.i(tag, "下载中........");
					Log.i(tag, "total = "+total);
					Log.i(tag, "current = "+current);
			
					super.onLoading(total, current, isUploading);
				}
			
			});
		   }
	}

	/**
	 * 安装对应apk
	 * @param file	安装文件
	 */
	protected void installApk(File file) {

		//系统应用界面,源码,安装apk入口
				Intent intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				/*//文件作为数据源
				intent.setData(Uri.fromFile(file));
				//设置安装的类型
				intent.setType("application/vnd.android.package-archive");*/
				intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//				startActivity(intent);
				startActivityForResult(intent, 0);
	}
  @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	  enterHome();
	super.onActivityResult(requestCode, resultCode, data);
}

	/**
	 * 进入应用程序主界面
	 */
	protected void enterHome() {
		
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		//在开启一个新的界面后,将导航界面关闭(导航界面只可见一次)
		finish();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		
		tv_version_name.setText("版本名称："+getVersionName());
		mLocationVersionCode = getVersionCode();
		//3,获取服务器版本号(客户端发请求,服务端给响应,(json,xml))
				//http://www.oxxx.com/update74.json?key=value  返回200 请求成功,流的方式将数据读取下来
				//json中内容包含:
				/* 更新版本的版本名称
				 * 新版本的描述信息
				 * 服务器版本号
				 * 新版本apk下载地址*/
		if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false)){
			checkVersion();
		}else
		{
			//直接进入应用程序主界面
//			enterHome();
			//消息机制
//			mHandler.sendMessageDelayed(msg, 4000);
			//在发送消息4秒后去处理,ENTER_HOME状态码指向的消息
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
	}

	/**
	 * 检测版本号
	 */
	private void checkVersion() {
		new Thread(){
			

			@Override
			public void run() {
				long starttime = System.currentTimeMillis();
				Message msg=Message.obtain();
				//发送请求获取数据,参数则为请求json的链接地址
				//http://117.178.112.32:8888/updownload.json	测试阶段不是最优
				//仅限于模拟器访问电脑tomcat
				try {
					//1,封装url地址39.177.95.114
					URL url = new URL("http://39.177.95.114/updownload.json");
					//2,开启一个链接
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,设置常见请求参数(请求头)
					
					//请求超时
					connection.setConnectTimeout(2000);
					//读取超时
					connection.setReadTimeout(2000);

					//默认就是get请求方式,
//					connection.setRequestMethod("POST");
					//4,获取请求成功响应码
					if(connection.getResponseCode()==200)
					{
						//5,以流的形式,将数据获取下来
						InputStream is = connection.getInputStream();
						//6,将流转换成字符串(工具类封装)
						String json = StreamUtil.streamToString(is);
						//Log.i(tag, json);
						//7,解析json
						JSONObject jsonObject = new JSONObject(json);
						//debug调试,解决问题

						@SuppressWarnings("unused")
						String versionName = jsonObject.getString("versionName");
						mVersionDes = jsonObject.getString("versionDes");
						String versionCode = jsonObject.getString("versionCode");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						
//						日志打印
//						Log.i(tag, versionName);
//						Log.i(tag, versionDes);
//						Log.i(tag, versionCode);
//						Log.i(tag, downloadUrl);
						
						//判断当前的版本与服务器的版本
						if(mLocationVersionCode < Integer.parseInt(versionCode))
						{
							//提示用户更新,弹出对话框(UI),消息机制
							msg.what=UPDATE_VISION;
						}else
						{
							msg.what=ENTER_HOME;
						}


					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					msg.what=ERROR_E;
				}
				finally{
					//指定睡眠时间,请求网络的时长超过4秒则不做处理
					//请求网络的时长小于4秒,强制让其睡眠满4秒钟
					long finallytime = System.currentTimeMillis();
					if(finallytime-starttime<4000){
						try {
							Thread.sleep(4000-(finallytime-starttime));
						} catch (InterruptedException e) {
					
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 返回版本号
	 * @return	
	 * 			非0 则代表获取成功
	 */
	private int getVersionCode() {
	
				try {
					PackageManager pm = getPackageManager();
					PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
					 return packageInfo.versionCode;
				} catch (Exception e) {
				
					e.printStackTrace();
				}
				return 0;
	}

	/**
	 * 获取版本名称:清单文件中
	 * @return	应用版本名称	返回null代表异常
	 */
	private String getVersionName() {

		try {
			PackageManager pm = getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			 return packageInfo.versionName;
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化控件数据
	 */
	private void initUI() {
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl = (RelativeLayout) findViewById(R.id.rl);
	}
        

}
