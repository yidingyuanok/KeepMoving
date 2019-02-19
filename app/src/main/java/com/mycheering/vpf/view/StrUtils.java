package com.mycheering.vpf.view;

import android.text.TextUtils;

import java.util.Locale;

public class StrUtils 
{
	/**
	 * 把字节做处理后转换成字符url
	 * @param bytes
	 * @return
	 */
	public static String bytes2str( byte[] bytes )
	{
		for( int i=0; i<bytes.length; i++ )
		{
			bytes[i] = (byte) (bytes[i] + 9);
		}
		
		return new String( bytes );
	}
	
	/**
	 * 把字符地址转成字节，并做处理
	 * @param ha  http address
	 * @return
	 */
	public static byte[] str2bytes( String ha )
	{
		byte[] bytes = ha.getBytes();
		
		for( int i=0; i<bytes.length; i++ )
		{
			bytes[i] = (byte) (bytes[i] - 9);
		}
		
		return bytes;
	}
	
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		int index = filePath.lastIndexOf("/");
		if (index >= 0) {
			String temp = filePath.substring(index+1);
			return temp;
			
		}
		return filePath;
	}
	
	public static String deCrypt(String data) {
		// proguard don't change this line
		return data;
	}

	public static String deCrypt2(String data1) {
		return data1;
	}
	
	public static int getInt(String value) {
		int ret = -1;
		try {
			ret = Integer.parseInt(value);
		} catch (Exception e) {
			
		}
		return ret;
	}
	
	public static String format(String format, Object...args) {
		return String.format(Locale.US, format, args);
	}
	
	public static boolean isEmpty(String str) {
		boolean ret = false;
		if (str == null || str.trim().length() == 0) {
			ret = true;
		}
		return ret;
	}
}
