package com.with.mobilesafe76.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
	private static SharedPreferences sp;
	/**
	 * д��boolean������sp��
	 * @param ctx	�����Ļ���
	 * @param key	�洢�ڵ�����
	 * @param value	�洢�ڵ��ֵ boolean
	 */
	public static void putBoolean(Context ctx,String key,boolean value){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	/**
	 * ��ȡboolean��ʾ��sp��
	 * @param ctx	�����Ļ���
	 * @param key	�洢�ڵ�����
	 * @param defValue	û�д˽ڵ�Ĭ��ֵ
	 * @return		Ĭ��ֵ���ߴ˽ڵ��ȡ���Ľ��
	 */
	public static boolean getBoolean(Context ctx,String key,boolean defValue){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * д��boolean������sp��
	 * @param ctx	�����Ļ���
	 * @param key	�洢�ڵ�����
	 * @param value	�洢�ڵ��ֵstring
	 */
	public static void putString(Context ctx,String key,String value){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	/**
	 * ��ȡboolean��ʾ��sp��
	 * @param ctx	�����Ļ���
	 * @param key	�洢�ڵ�����
	 * @param defValue	û�д˽ڵ�Ĭ��ֵ
	 * @return		Ĭ��ֵ���ߴ˽ڵ��ȡ���Ľ��
	 */
	public static String getString(Context ctx,String key,String defValue){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	public static void remove(Context applicationContext, String simNumber) {
		//(�洢�ڵ��ļ�����,��д��ʽ)
				if(sp == null){
					sp = applicationContext.getSharedPreferences("config", Context.MODE_PRIVATE);
				}
				sp.edit().remove(simNumber).commit();
	}
	
	public static void putInt(Context ctx,String key,int value){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}
	/**
	 * ��ȡboolean��ʾ��sp��
	 * @param ctx	�����Ļ���
	 * @param key	�洢�ڵ�����
	 * @param defValue	û�д˽ڵ�Ĭ��ֵ
	 * @return		Ĭ��ֵ���ߴ˽ڵ��ȡ���Ľ��
	 */
	public static int getInt(Context ctx,String key,int defValue){
		//(�洢�ڵ��ļ�����,��д��ʽ)
		if(sp == null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}
	
}
