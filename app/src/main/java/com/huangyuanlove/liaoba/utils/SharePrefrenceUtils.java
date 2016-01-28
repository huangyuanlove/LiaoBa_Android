package com.huangyuanlove.liaoba.utils;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences存储数据类
 */
public class SharePrefrenceUtils {
	private static SharedPreferences mPreferences;
	private static Context mContext;
	private static String mShareName = "xuan";
	private static SharePrefrenceUtils mPreferencesutils;

	public static SharePrefrenceUtils getInstance(Context context) {
		if (mPreferencesutils == null) {
			mContext = context;
			mPreferencesutils = new SharePrefrenceUtils();
			mPreferences = mContext.getSharedPreferences(mShareName,
					Context.MODE_PRIVATE);
		}
		return mPreferencesutils;
	}

	/**
	 * 批量保存信息
	 */
	public void saveInfo(Map<String, String> info) {
		Editor editor = mPreferences.edit();
		Set<String> keys = info.keySet();
		for (String key : keys) {
			editor.putString(key, info.get(key));
		}
		editor.commit();
	}

	/**
	 * 根据key读取参数
	 */
	public String getInfo(String key) {
		return mPreferences.getString(key, "");
	}

	/**
	 * 保存string类型数据
	 */
	public void setString(String name, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(name, value);
		editor.commit();
	}

	/**
	 * 获取string类型数据
	 */
	public String getString(String name) {
		return mPreferences.getString(name, "");
	}

	/**
	 * 保存long类型数据
	 */
	public void setLong(String name, long value) {
		Editor editor = mPreferences.edit();
		editor.putLong(name, value);
		editor.commit();
	}

	/**
	 * 获取long类型数据
	 */
	public long getLong(String name) {
		return mPreferences.getLong(name, -1);
	}

	/**
	 * 保存int类型数据
	 */
	public void setInt(String name, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * 获取int类型数据
	 * @return
	 */
	public int getInt(String name) {
		return mPreferences.getInt(name.toString(), 0);
	}

	/**
	 * 读取状态
	 */
	public boolean getBoolean(String key, boolean def) {
		return mPreferences.getBoolean(key, def);
	}

	/**
	 * 保存状态
	 */
	public void setBoolean(String key, boolean val) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}
}
