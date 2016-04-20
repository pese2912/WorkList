package com.example.memo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MemoSharedpreference {

	final private static String PREFNAME_MEMO = "memo";
	//
	final private static String PREFKEY_STUDY = "study";
	
	public static String getSharedPrefStudy(Context context){
		SharedPreferences pref = context.getSharedPreferences(PREFNAME_MEMO, Context.MODE_PRIVATE);
		return pref.getString(PREFKEY_STUDY, "");
	}
	
	public static void setSharedPrefStudy(Context context, String value){
		SharedPreferences pref = context.getSharedPreferences(PREFNAME_MEMO, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(PREFKEY_STUDY, value);
		prefEditor.commit();
	}
	
	
	
	
}
