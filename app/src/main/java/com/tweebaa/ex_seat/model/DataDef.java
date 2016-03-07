package com.tweebaa.ex_seat.model;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

public class DataDef {
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
}
