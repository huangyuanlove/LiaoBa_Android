package com.huangyuanlove.liaoba.utils;



import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonTool {
	
	public static <T> T getObj(String jsonString,Class<T> cls)
	{ 
		T t = null;
		Gson gson = new Gson();
		t = gson.fromJson(jsonString, cls);
		return t;
	}
	
	public static <T> List<T> getObjs(String jsonString)
	{
		List<T> list = null;
		Gson gson = new Gson();
		list =gson.fromJson(jsonString, new TypeToken<List<T>>(){}.getType());
		return list;
	}
	
	public static List<Map<String,Object>> getListMap(String jsonString)
	{
		List<Map<String,Object>> list = null;
		Gson gson = new Gson();
		list = gson.fromJson(jsonString, new TypeToken<List<Map<String,Object>>>(){}.getType());
		return list;
	}
	

}
