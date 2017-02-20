package com.mmazzarolo.dev.topgithub.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * @desc:简易的log打印类
  * @author：Arison on 2017/2/9
  */
public class LogUtil {
	
	private static final boolean DEBUG = true;
	
	/**
	  * @desc:只打印文件名和函数名
	  * @author：Arison on 2017/2/20
	  */
	public static void d(){
		Log.d(_FILE_(), "[" + getLineMethod() + "]" );
	}
	
	public static void d(String TAG, String method, String msg) {
		Log.d(TAG, "[" + method + "]" + msg);
	}
	
	public static void d(String TAG, String msg){
		if (DEBUG) {
			Log.d(TAG, "[" + getFileLineMethod() + "]" + msg);
		}
	}
	
	public static void d(String msg){
		if (DEBUG && !TextUtils.isEmpty(msg)) {
			Log.d(_FILE_(), "[" + getLineMethod() + "]" + msg);
		}
	}
	@SuppressWarnings("unused")
	public static void e(String msg){
		if (DEBUG) {
			Log.e(_FILE_(), getLineMethod() + msg);
		}
	}
	@SuppressWarnings("unused")
	public static void e(String TAG, String msg){
		if (DEBUG) {
			Log.e(TAG, getLineMethod() + msg);
		}
	}
    
	/**
	  * @desc:获取方法名
	  * @author：Arison on 2017/2/20
	  */
	public static String getFileLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[")
				.append(traceElement.getFileName()).append(" | ")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}
	
	public static String getLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[")
				.append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}

	public static String _FILE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		return traceElement.getFileName();
	}
    
	@SuppressWarnings("unused")
	public static String _FUNC_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getMethodName();
	}
	@SuppressWarnings("unused")
	public static int _LINE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getLineNumber();
	}
	@SuppressWarnings("unused")
	public static String _TIME_() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(now);
	}
	 
	/**
	  * @desc:分段打印log日志(打印长日志)
	  * @author：Arison on 2017/2/9
	  */
	@SuppressWarnings("unused")
	public static void prinLnLongMsg(String TAG,String responseInfo){
		int lenth=4000;
		if (responseInfo != null){
			if (responseInfo.length() >=lenth) {
				int chunkCount = responseInfo.length() /lenth;     // integer division
				int y=responseInfo.length()%lenth;
				if(y>0)chunkCount++;//余数大于0
				for (int i = 0; i <chunkCount; i++) {
					int max = lenth * (i + 1);
					if (max >= responseInfo.length()) {
						Log.v(TAG, "【chunk " + i + " of " + chunkCount + "】:" + responseInfo.substring(lenth * i));
					} else {
						Log.v(TAG, "【chunk " + i + " of " + chunkCount + "】:" + responseInfo.substring(lenth * i, max));
					}
				}
			} else {
				Log.v(TAG,responseInfo);
			}
		}
	}
}
