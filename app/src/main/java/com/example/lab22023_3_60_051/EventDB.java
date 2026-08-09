package com.example.lab22023_3_60_051;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDB extends SQLiteOpenHelper {

	public EventDB(Context context) {
		super(context, "EventDB.db", null, 2);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlContacts = "CREATE TABLE contacts ("
				+ "email TEXT PRIMARY KEY,"
				+ "name TEXT,"
				+ "phone TEXT,"
				+ "dob TEXT,"
				+ "present_address TEXT,"
				+ "permanent_address TEXT,"
				+ "image_uri TEXT"
				+ ")";
		db.execSQL(sqlContacts);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS contacts");
		onCreate(db);
	}
	public void insertContact(String name, String email, String phone, String dob, String presentAddress, String permanentAddress, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("name", name);
		cols.put("email", email);
		cols.put("phone", phone);
		cols.put("dob", dob);
		cols.put("present_address", presentAddress);
		cols.put("permanent_address", permanentAddress);
		cols.put("image_uri", imageUri);
		db.insert("contacts", null, cols);
		db.close();
	}
	public void updateContact(String name, String email, String phone, String dob, String presentAddress, String permanentAddress, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("name", name);
		cols.put("phone", phone);
		cols.put("dob", dob);
		cols.put("present_address", presentAddress);
		cols.put("permanent_address", permanentAddress);
		cols.put("image_uri", imageUri);
		db.update("contacts", cols, "email=?", new String[]{email});
		db.close();
	}
	public void deleteContact(String email) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("contacts", "email=?", new String[]{email});
		db.close();
	}
	public Cursor getAllContacts() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM contacts", null);
	}
}