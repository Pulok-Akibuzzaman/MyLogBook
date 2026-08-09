package com.example.lab22023_3_60_051;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDB extends SQLiteOpenHelper {

	public EventDB(Context context) {
		super(context, "EventDB.db", null, 3);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlUsers = "CREATE TABLE users ("
				+ "userId TEXT PRIMARY KEY,"
				+ "userName TEXT,"
				+ "email TEXT,"
				+ "phone TEXT,"
				+ "password TEXT"
				+ ")";
		db.execSQL(sqlUsers);

		String sqlContacts = "CREATE TABLE contacts ("
				+ "email TEXT,"
				+ "name TEXT,"
				+ "phone TEXT,"
				+ "dob TEXT,"
				+ "present_address TEXT,"
				+ "permanent_address TEXT,"
				+ "image_uri TEXT,"
				+ "owner_id TEXT,"
				+ "PRIMARY KEY (email, owner_id)"
				+ ")";
		db.execSQL(sqlContacts);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS contacts");
		onCreate(db);
	}
	public void insertUser(String userId, String userName, String email, String phone, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("userId", userId);
		cols.put("userName", userName);
		cols.put("email", email);
		cols.put("phone", phone);
		cols.put("password", password);
		db.insertWithOnConflict("users", null, cols, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}
	public Cursor getUser(String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("SELECT * FROM users WHERE userId=?", new String[]{userId});
	}
	public void insertContact(String ownerId, String name, String email, String phone, String dob, String presentAddress, String permanentAddress, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("owner_id", ownerId);
		cols.put("name", name);
		cols.put("email", email);
		cols.put("phone", phone);
		cols.put("dob", dob);
		cols.put("present_address", presentAddress);
		cols.put("permanent_address", permanentAddress);
		cols.put("image_uri", imageUri);
		db.insertWithOnConflict("contacts", null, cols, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}
	public void updateContact(String ownerId, String name, String email, String phone, String dob, String presentAddress, String permanentAddress, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("name", name);
		cols.put("phone", phone);
		cols.put("dob", dob);
		cols.put("present_address", presentAddress);
		cols.put("permanent_address", permanentAddress);
		cols.put("image_uri", imageUri);
		db.update("contacts", cols, "email=? AND owner_id=?", new String[]{email, ownerId});
		db.close();
	}
	public void deleteContact(String ownerId, String email) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("contacts", "email=? AND owner_id=?", new String[]{email, ownerId});
		db.close();
	}
	public Cursor getAllContacts(String ownerId) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("SELECT * FROM contacts WHERE owner_id=?", new String[]{ownerId});
	}
}