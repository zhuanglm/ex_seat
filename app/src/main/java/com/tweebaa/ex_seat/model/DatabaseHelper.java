package com.tweebaa.ex_seat.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	//"_id" have to setup for SimpleCursorAdapter
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table data(_id integer primary key autoincrement,date varchar(20) not null , " +
				"duration varchar(20) not null,max varchar(5),avg varchar(5) );";
        db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}


}
