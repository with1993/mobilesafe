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
					//����Ӧ�ó���������,activity��ת����
					enterHome();
					break;
				case ERROR_E:
					ToastUtils.show(getApplicationContext(), "����ʧ�ܣ�����������Ƿ�����");
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
		//��ʼ��UI
        initUI();
        //��ʼ������
        initData();
        //��ʼ����
        initAnimation();
        //��ʼ�����ݿ�
        initDB();
        //��ݷ�ʽ������
        if(!SpUtil.getBoolean(this, ConstantValue.HAS_SHORTCUT, false)){
        	//���ɿ�ݷ�ʽ
        	initShortCut();
        } 
     
	}


	/**
	 * ���ɿ�ݷ�ʽ�ķ���
	 */
	private void initShortCut() {
		//1,��intentά��ͼ��,����
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//ά��ͼ��
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		//����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "��ȫ��ʿ��ݷ�ʽ");
		//2,�����ݷ�ʽ����ת����activity
		//2.1ά����������ͼ����
		Intent shortCutIntent = new Intent("android.intent.action.HOME");
		shortCutIntent.addCategory("android.intent.category.DEFAULT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		//3.���͹㲥
		sendBroadcast(intent);
		//4.��֪sp�Ѿ����ɿ�ݷ�ʽ
		SpUtil.putBoolean(this, ConstantValue.HAS_SHORTCUT, true);
	}


	private void initDB() {
		//1,���������ݿ�������
				initAddressDB("address.db");
				initAddressDB("commonnum.db");
				//3,�����������ݿ�
				initAddressDB("antivirus.db");
	}


	private void initAddressDB(String dbName) {
		//1,��files�ļ����´���ͬ��dbName���ݿ��ļ�����
				File files = getFilesDir();
				File file = new File(files, dbName);
				if(file.exists()){
					return;
				}
				InputStream stream = null;
				FileOutputStream fos = null;
				//2,��������ȡ�������ʲ�Ŀ¼�µ��ļ�
				try {
					stream = getAssets().open(dbName);
					//3,����ȡ������д�뵽ָ���ļ��е��ļ���ȥ
					fos = new FileOutputStream(file);
					//4,ÿ�εĶ�ȡ���ݴ�С
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
	 * ��ӭ����ĵ�������
	 */
	private void initAnimation() {
		 AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1);
		 alphaAnimation.setDuration(4000);
		// alphaAnimation.setRepeatMode(Animation.RESTART);
		 rl.startAnimation(alphaAnimation);
	}


	/**
	 * �����Ի���,��ʾ�û�����
	 */
	protected void showUpdateDownDialog() {
	            //    Builder builder=new AlertDialog.Builder(this);
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("�汾���¿�");
		builder.setMessage(mVersionDes);
		builder.setPositiveButton("��������",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//����apk,apk���ӵ�ַ,downloadUrl
				downloadApk();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
		
				enterHome();
			}
		});
		//���ȡ���¼�����
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//��ʹ�û����ȡ��,Ҳ��Ҫ�������Ӧ�ó���������
				enterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * ����Apk�ķ���
	 */
	protected void downloadApk() {
	
		//apk�������ӵ�ַ,����apk������·��
		
				//1,�ж�sd���Ƿ����,�Ƿ������
		   if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		   {
			   //��ȡ·��
			   String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"mobilesafe76.apk";
			 //3,��������,��ȡapk,���ҷ��õ�ָ��·��
			   HttpUtils httpUtils=new HttpUtils();
			 //4,��������,���ݲ���(���ص�ַ,����Ӧ�÷���λ��)
			   httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
			
					//���سɹ�(���ع���ķ�����sd����apk)
					Log.i(tag, "���سɹ���");
					File file = responseInfo.result;
					//��ʾ�û���װ
					installApk(file);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
			
					Log.i(tag, "����ʧ�ܣ�");

				}
				@Override
				public void onStart() {
			
					Log.i(tag, "�ոտ�ʼ����");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					Log.i(tag, "������........");
					Log.i(tag, "total = "+total);
					Log.i(tag, "current = "+current);
			
					super.onLoading(total, current, isUploading);
				}
			
			});
		   }
	}

	/**
	 * ��װ��Ӧapk
	 * @param file	��װ�ļ�
	 */
	protected void installApk(File file) {

		//ϵͳӦ�ý���,Դ��,��װapk���
				Intent intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				/*//�ļ���Ϊ����Դ
				intent.setData(Uri.fromFile(file));
				//���ð�װ������
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
	 * ����Ӧ�ó���������
	 */
	protected void enterHome() {
		
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		//�ڿ���һ���µĽ����,����������ر�(��������ֻ�ɼ�һ��)
		finish();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		
		tv_version_name.setText("�汾���ƣ�"+getVersionName());
		mLocationVersionCode = getVersionCode();
		//3,��ȡ�������汾��(�ͻ��˷�����,����˸���Ӧ,(json,xml))
				//http://www.oxxx.com/update74.json?key=value  ����200 ����ɹ�,���ķ�ʽ�����ݶ�ȡ����
				//json�����ݰ���:
				/* ���°汾�İ汾����
				 * �°汾��������Ϣ
				 * �������汾��
				 * �°汾apk���ص�ַ*/
		if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false)){
			checkVersion();
		}else
		{
			//ֱ�ӽ���Ӧ�ó���������
//			enterHome();
			//��Ϣ����
//			mHandler.sendMessageDelayed(msg, 4000);
			//�ڷ�����Ϣ4���ȥ����,ENTER_HOME״̬��ָ�����Ϣ
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
	}

	/**
	 * ���汾��
	 */
	private void checkVersion() {
		new Thread(){
			

			@Override
			public void run() {
				long starttime = System.currentTimeMillis();
				Message msg=Message.obtain();
				//���������ȡ����,������Ϊ����json�����ӵ�ַ
				//http://117.178.112.32:8888/updownload.json	���Խ׶β�������
				//������ģ�������ʵ���tomcat
				try {
					//1,��װurl��ַ39.177.95.114
					URL url = new URL("http://39.177.95.114/updownload.json");
					//2,����һ������
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,���ó����������(����ͷ)
					
					//����ʱ
					connection.setConnectTimeout(2000);
					//��ȡ��ʱ
					connection.setReadTimeout(2000);

					//Ĭ�Ͼ���get����ʽ,
//					connection.setRequestMethod("POST");
					//4,��ȡ����ɹ���Ӧ��
					if(connection.getResponseCode()==200)
					{
						//5,��������ʽ,�����ݻ�ȡ����
						InputStream is = connection.getInputStream();
						//6,����ת�����ַ���(�������װ)
						String json = StreamUtil.streamToString(is);
						//Log.i(tag, json);
						//7,����json
						JSONObject jsonObject = new JSONObject(json);
						//debug����,�������

						@SuppressWarnings("unused")
						String versionName = jsonObject.getString("versionName");
						mVersionDes = jsonObject.getString("versionDes");
						String versionCode = jsonObject.getString("versionCode");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						
//						��־��ӡ
//						Log.i(tag, versionName);
//						Log.i(tag, versionDes);
//						Log.i(tag, versionCode);
//						Log.i(tag, downloadUrl);
						
						//�жϵ�ǰ�İ汾��������İ汾
						if(mLocationVersionCode < Integer.parseInt(versionCode))
						{
							//��ʾ�û�����,�����Ի���(UI),��Ϣ����
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
					//ָ��˯��ʱ��,���������ʱ������4����������
					//���������ʱ��С��4��,ǿ������˯����4����
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
	 * ���ذ汾��
	 * @return	
	 * 			��0 ������ȡ�ɹ�
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
	 * ��ȡ�汾����:�嵥�ļ���
	 * @return	Ӧ�ð汾����	����null�����쳣
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
	 * ��ʼ���ؼ�����
	 */
	private void initUI() {
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl = (RelativeLayout) findViewById(R.id.rl);
	}
        

}
