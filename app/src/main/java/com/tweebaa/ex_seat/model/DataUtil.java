package com.tweebaa.ex_seat.model;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {
	public static int msgTYPE_YES = 1;
	public static int msgTYPE_NO = 0;
	public static List<Integer> dataFromDev = new ArrayList<Integer>();
	public static int MaxDataSize=0;
	public static int WinTabNum=4;
	public static int TimeInterver_BT=300;

	public static AccessToken accessToken;
	public static Profile profile;

	public static final String DB_NAME = "ex_seat.db"; //数据库名称
	public static final int version = 1; //数据库版本

	public static String Inputstr2Str_Reader(InputStream in, String encode)
	{

		String str = "";
		try
		{
			if (encode == null || encode.equals(""))
			{
				// 默认以utf-8形式
				encode = "utf-8";
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
			StringBuffer sb = new StringBuffer();

			while ((str = reader.readLine()) != null)
			{
				sb.append(str).append("\n");
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return str;
	}

	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
}
