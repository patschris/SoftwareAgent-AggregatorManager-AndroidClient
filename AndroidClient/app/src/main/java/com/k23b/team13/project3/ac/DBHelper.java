package com.k23b.team13.project3.ac;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.k23b.team13.project3.ac.classes.Job;
import com.k23b.team13.project3.ac.classes.KillAgent;
import com.k23b.team13.project3.ac.classes.StopPeriodic;
import com.k23b.team13.project3.ac.classes.Users;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
	/**
	 * database name.
	 */
	public static final String DATABASE_NAME = "AndroidDatabase.db";
	/**
	 * database version.
	 */
	public static final int DATABASE_VERSION = 45;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* Create a new database */
		db.execSQL("create table IF NOT EXISTS Users (username text primary key, active integer)");
		db.execSQL("create table IF NOT EXISTS KillAgent (id integer primary key autoincrement, hashkey integer,user text,FOREIGN KEY(user) REFERENCES Users(username))");
		db.execSQL("create table IF NOT EXISTS Job (id integer primary key autoincrement, command text, periodic integer, time integer, hashkey integer,user text,FOREIGN KEY(user) REFERENCES Users(username))");
		db.execSQL("create table IF NOT EXISTS StopPeriodic (id integer primary key autoincrement,jobid integer,hashkey integer, user text,FOREIGN KEY(user) REFERENCES Users(username))");
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* drop old version*/
		db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS KillAgent");
        db.execSQL("DROP TABLE IF EXISTS Job");
        db.execSQL("DROP TABLE IF EXISTS StopPeriodic");
        db.execSQL("DROP TABLE IF EXISTS SeeOutput");
		/* and create new version*/
        onCreate(db);
    }
	/**********************************************************************************************************************************************************************/
											/* Users */
	/**********************************************************************************************************************************************************************/
	/**
	 * @return user who is connected in the app.
	 */
	public Users getConnectedUser(){
		Users user=null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from Users where active='1'",null);
		if (res.moveToFirst()) {
			while (res.isAfterLast()==false) {
				String us = res.getString(res.getColumnIndex("username"));
				boolean b = res.getInt(res.getColumnIndex("active"))>0;
				user = new Users(us,b);
				res.moveToNext();
			}
		}
		res.close();
		db.close();
		return user;
	}
	/**
	 * Insert newly logged in user into the database.
	 * @param user user who logged in.
	 * @return true if user inserted successfully into database, false otherwise.
	 */
	public boolean insertIntoConnected(Users user) {
		int num;
		if (user.isActive()) num=1;
		else num=0;
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("username", user.getUsername());
	    contentValues.put("active", num);
	    long rows= db.insert("Users", null, contentValues);
	    db.close();
	    if (rows==-1) return false;
	    else return true;
	}
	/**
	 * Logout connected user.
	 * @param user user who disconnected.
	 * @return true if database updated successfully, false otherwise.
	 */
	public boolean logoutConnected(Users user) {
		int number=0;
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("active", number);
	    long rows= db.update("Users", contentValues, "username = ? ", new String[]{user.getUsername()});
	    db.close();
	    if (rows >0) return true;
	    else return false;
	}
	/**
	 * Make user active.
	 * @param user user who just logged in.
	 * @return true if database updated successfully, false otherwise.
	 */
	public boolean makeUserActive(Users user) {
		int number=1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("active", number);
		long rows= db.update("Users", contentValues, "username = ? ", new String[]{user.getUsername()});
		db.close();
		if (rows >0) return true;
		else return false;
	}
	/**
	 * Find if given user is stored in database.
	 * @param u user i want to search.
	 * @return true if user was found, false otherwise.
	 */
	public boolean findUser(Users u){
		SQLiteDatabase db = this.getReadableDatabase();
		boolean flag = false;
		Cursor res = db.rawQuery("select * from Users where username='"+u.getUsername()+"'",null);
		if (res.moveToFirst()) {
			while (res.isAfterLast()==false) {
				flag = true;
				res.moveToNext();
			}
		}
		res.close();
		db.close();
		return flag;
	}
	/**
	 * Delete all inactive users.
	 * @param user the active user.
	 */
	public void deleteAllInactiveData(Users user){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from KillAgent where user not in ('"+user.getUsername()+"')");
		db.execSQL("delete from Job where user not in ('" + user.getUsername() + "')");
		db.execSQL("delete from StopPeriodic where user not in ('" + user.getUsername() + "')");
		db.execSQL("delete from Users where active='0'");
		db.close();
	}
	/***********************************************************************************************************************************************************************/
												     /* Kill Agent */
	/***********************************************************************************************************************************************************************/
	/**
	 * Check if this stop agent request is already inside database.
	 * @param ka new stop agent request.
	 * @return true if it's already inside, otherwise false.
	 */
	public boolean checkForKillAgent(KillAgent ka){
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from KillAgent where hashkey='" + ka.getHashkey() + "' and user='" + ka.getUser() + "'", null);
		if (res!=null && res.getCount()>0){
			flag = true;
		}
		if (res != null) res.close();
		db.close();
		return flag;
	}
	/**
	 * Insert a new stop agent request into the database while there is no internet connection.
	 * @param ka newly created stop agent request.
	 * @return true if database updated successfully, false otherwise.
	 */
	public boolean insertIntoKillAgent(KillAgent ka) {
		if (checkForKillAgent(ka)) return true;
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("hashkey", ka.getHashkey());
	    contentValues.put("user", ka.getUser());
		long rows= db.insert("KillAgent", null, contentValues);
	    db.close();
	    if (rows==-1) return false;
	    else return true;
	}
	/**
	 * Delete all stop agent requests which belongs to a specific user.
	 * @param user connected user.
	 */
	public void deleteKillAgent(Users user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("KillAgent", "user = ?", new String[]{user.getUsername()});
		db.close();
	}
	/**
	 * @param user connected user.
	 * @return a string formatted json array which contains all stop agent requests of connected user.
	 */
	public String fetchKillAgent(Users user){
		String str = new String();
		JSONArray list = new JSONArray();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from KillAgent where user=\"" + user.getUsername() + "\"", null);
		if (res.moveToFirst()) {
			while (res.isAfterLast()==false) {
				JSONObject obj=new JSONObject();
				obj.put("hashkey", res.getInt(res.getColumnIndex("hashkey")));
				list.add(obj);
				res.moveToNext();
			}
			str = list.toString();
		}
		res.close();
		db.close();
		return str;
	}
	/**
	 * @param user connected user.
	 * @return arraylist containing all stop agent requests of connected user.
	 */
	public ArrayList<String> fetchRemainingAgents(Users user) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select hashkey from KillAgent where user=\"" + user.getUsername() + "\"", null);
		ArrayList<String> al = new ArrayList<String>();
		if (res.moveToFirst()) {
			while (res.isAfterLast() == false) {
				al.add( String.valueOf(res.getInt(res.getColumnIndex("hashkey"))) );
				res.moveToNext();
			}
		}
		return al;
	}

	/**********************************************************************************************************************************************************************/
													/* Job */
	/**********************************************************************************************************************************************************************/
	/**
	 * Insert a new job into the database while there is no internet connection.
	 * @param job newly created job.
	 * @return true if database updated successfully, false otherwise.
	 */
	public boolean insertIntoJob(Job job) {
		SQLiteDatabase db = this.getWritableDatabase();
		int flag=0;
		if (job.isPeriodic()) flag=1;
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("command",job.getCommand());
	    contentValues.put("periodic",flag);
		contentValues.put("time", job.getTime());
	    contentValues.put("hashkey", job.getHashkey());
		contentValues.put("user", job.getUser());
	    long rows= db.insert("Job",null,contentValues);
	    db.close();
	    if (rows==-1) return false;
	    else return true;
	}
	/**
	 * Delete jobs from database after receiving response from the server.
	 * @param user connected user.
	 */
	public void deleteJob(Users user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Job", "user = ?", new String[]{user.getUsername()});
		db.close();
	}
	/**
	 * @param user connected user.
	 * @return string formatted json array which contains all newly created jobs of connected user.
	 */
	public String fetchJob(Users user){
		String str = new String();
		JSONArray list = new JSONArray();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from Job where user=\"" + user.getUsername() + "\"", null);
		if (res.moveToFirst()) {
			while (res.isAfterLast()==false) {
				JSONObject obj=new JSONObject();
				obj.put("command",res.getString(res.getColumnIndex("command")));
				boolean per = res.getInt(res.getColumnIndex("periodic")) > 0;
				obj.put("periodic", per);
				obj.put("time", res.getInt(res.getColumnIndex("time")));
				obj.put("hashkey", res.getInt(res.getColumnIndex("hashkey")));
				list.add(obj);
				res.moveToNext();
			}
			if (!list.isEmpty()) str = list.toString();
		}
		res.close();
		db.close();
		return str;
	}
	/**********************************************************************************************************************************************************************/
													/* Stop Periodic */
	/**********************************************************************************************************************************************************************/
	/**
	 * Insert a newly created stop periodic request into the database while there is no internet connection.
	 * @param sp newly created stop periodic request.
	 * @return true if database updated successfully, false otherwise.
	 */
	public boolean insertIntoStopPeriodic(StopPeriodic sp) {
		if (checkForStopPeriodic(sp)) return true;
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
		contentValues.put("jobid", sp.getJobid());
		contentValues.put("user", sp.getUser());
        contentValues.put("hashkey", sp.getHashkey());
	    long rows= db.insert("StopPeriodic",null,contentValues);
	    db.close();
	    if (rows==-1) return false;
	    else return true;
	}
	/**
	 * Check if this stop periodic request is already inside database.
	 * @param sp new stop periodic request.
	 * @return true if it's already inside, otherwise false.
	 */
	public boolean checkForStopPeriodic(StopPeriodic sp){
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from StopPeriodic where hashkey='" + sp.getHashkey() + "' and jobid='" + sp.getJobid() + "' and user='" + sp.getUser() + "'", null);
		if (res!=null && res.getCount()>0){
			flag = true;
		}
		if (res != null) res.close();
		db.close();
		return flag;
	}
	/**
	 * Delete stop periodic requests from database after receiving response from the server.
	 * @param user connected user.
	 */
	public void deleteStopPeriodic(Users user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("StopPeriodic", "user = ?", new String[]{user.getUsername()});
		db.close();
	}
	/**
	 * @param user connected user.
	 * @return string formatted json array which contains all stop periodic requests of connected user stored in database.
	 */
	public String fetchStopPeriodic(Users user) {
		String str = new String();
		JSONArray list = new JSONArray();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from StopPeriodic where user=\"" + user.getUsername() + "\"", null);
		if (res.moveToFirst()) {
			while (res.isAfterLast()==false) {
				JSONObject obj=new JSONObject();
				obj.put("jobid",res.getInt(res.getColumnIndex("jobid")));
				obj.put("hashkey", res.getInt(res.getColumnIndex("hashkey")));
				list.add(obj);
				res.moveToNext();
			}
			str = list.toString();
		}
		res.close();
		db.close();
		return str;
	}
	/**
	 * @param user connected user.
	 * @return arraylist containing all stop periodic requests of connected user.
	 */
	public ArrayList<Integer> fetchRemainingPeriodics(Users user) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select jobid from StopPeriodic where user=\"" + user.getUsername() + "\"", null);
		ArrayList<Integer> al = new ArrayList<Integer>();
		if (res.moveToFirst()) {
			while (res.isAfterLast() == false) {
				al.add(res.getInt(res.getColumnIndex("jobid")));
				res.moveToNext();
			}
		}
		return al;
	}
	/**********************************************************************************************************************************************************************/
	/**
	 * Empty all tables.
	 */
	public void onDestroy(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Users", null, null);
		db.delete("KillAgent", null, null);
		db.delete("Job", null, null);
		db.delete("StopPeriodic", null, null);
		db.close();
	}
}