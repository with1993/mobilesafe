package com.with.mobilesafe76.db.dao;
import java.util.ArrayList;
import java.util.List;

import com.with.mobilesafe76.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AppLockDao {
	private AppLockOpenHelper appLockOpenHelper;
	//BlackNumberDao����ģʽ
	//1,˽�л����췽��
	private AppLockDao(Context context){
		//�������ݿ��Ѿ�������
		appLockOpenHelper = new AppLockOpenHelper(context);
	}
	//2,����һ����ǰ��Ķ���
	private static AppLockDao appLockDao = null;
	//3,�ṩһ����̬����,�����ǰ��Ķ���Ϊ��,����һ���µ�
	public static AppLockDao getInstance(Context context){
		if(appLockDao == null){
			appLockDao = new AppLockDao(context);
		}
		return appLockDao;
	}
	
	//���뷽��
	public void insert(String packagename){
		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put("packagename", packagename);
		
		db.insert("applock", null, contentValues);
		db.close();
	}
	//ɾ������
	public void delete(String packagename){
		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put("packagename", packagename);
		
		db.delete("applock", "packagename = ?", new String[]{packagename});
		
		db.close();
	}
	//��ѯ����
	public List<String> findAll(){
		SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("applock", new String[]{"packagename"}, null, null, null, null, null);
		List<String> lockPackageList = new ArrayList<String>();
		while(cursor.moveToNext()){
			lockPackageList.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return lockPackageList;
	}
}

