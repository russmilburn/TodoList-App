package com.livingsoup.todolist.dataservices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.livingsoup.todolist.model.TaskVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 17/09/2013
 * Time: 18:53
 * Project: ToDoListApp
 */
public class DBHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "ThingzToDoDatabase.db";
	private static final int DB_VERSION = 1;

	public static final String TABLE_TASK = "Tasks";
	public static final String C_ID = "_id";
	public static final String C_TASK = "task";
	public static final String C_COMPLETED = "complete";
	public static final String C_DUE_DATE = "dueDate";
	public static final String C_PRIORITY = "priority";
	public static final String C_PRIORITY_LEVEL = "priorityLevel";

	public DBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		final String sqlCreateTablePeople = "" +
				"CREATE TABLE " + TABLE_TASK + "( "
				+ C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ C_TASK + " TEXT NOT NULL, "
				+ C_COMPLETED + " BOOLEAN, "
				+ C_DUE_DATE + " DATE, "
				+ C_PRIORITY + " CHARACTER(20), "
				+ C_PRIORITY_LEVEL + " INTEGER);";
		sqLiteDatabase.execSQL(sqlCreateTablePeople);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
	{
		final String sqlDropTable = "DROP TABLE IF EXISTS " + TABLE_TASK + ";";
		sqLiteDatabase.execSQL(sqlDropTable);
		onCreate(sqLiteDatabase);
	}

	public TaskVo insertTask(TaskVo vo)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = covertTaskVoToContentValues(vo);
		vo.id = db.insert(TABLE_TASK, null, cv);
		//Log.d("Debug", "DBHelper().insertTask()  rowId = [" + rowId + "]");
		db.close();
		return vo;

	}

	private ContentValues covertTaskVoToContentValues(TaskVo vo)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ContentValues cv = new ContentValues();
		cv.put(C_TASK, vo.task);
		cv.put(C_COMPLETED, vo.completed);
		cv.put(C_PRIORITY, String.valueOf(vo.priority));
		cv.put(C_DUE_DATE, dateFormat.format(vo.dueDate));
		cv.put(C_PRIORITY_LEVEL, vo.priorityLevel);
		return cv;
	}

	public boolean updateTask(TaskVo vo)
	{
		Boolean recordUpdated = false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = covertTaskVoToContentValues(vo);
		int recordsAffected = db.update(TABLE_TASK, cv, C_ID + "=?", new String[]{String.valueOf(vo.id)});
		if (recordsAffected > 0)
		{

			recordUpdated = true;
		}
		db.close();

		return recordUpdated;

	}

	public boolean deleteTask(TaskVo vo)
	{
		Boolean recordDeleted = false;
		SQLiteDatabase db = getWritableDatabase();
		int recordsAffected = db.delete(TABLE_TASK, C_ID + "=?", new String[]{String.valueOf(vo.id)});
		if (recordsAffected > 0)
		{

			recordDeleted = true;
		}
		db.close();

		return recordDeleted;


	}


	public ArrayList<TaskVo> getUncompletedTasks()
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASK, null, C_COMPLETED + "=?", new String[]{"0"}, null, null, null, null);

		ArrayList<TaskVo> listData = new ArrayList<TaskVo>();
		if (cursor.moveToFirst())
		{
			do
			{
				TaskVo vo = convertDBRecordToTaskVo(cursor);
				listData.add(vo);

			}
			while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return listData;
	}

	public ArrayList<TaskVo> getCompletedTasks()
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASK, null, C_COMPLETED + "=?", new String[]{"1"}, null, null, null, null);

		ArrayList<TaskVo> listData = new ArrayList<TaskVo>();
		if (cursor.moveToFirst())
		{
			do
			{
				TaskVo vo = convertDBRecordToTaskVo(cursor);
				listData.add(vo);

			}
			while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return listData;
	}

	private TaskVo convertDBRecordToTaskVo(Cursor cursor)
	{
		TaskVo vo = new TaskVo();
		vo.id = cursor.getInt(0);
		vo.task = cursor.getString(1);
		vo.completed = (cursor.getInt(2) != 0);

		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(3));
			vo.dueDate = date;
		}
		catch (ParseException e)
		{
			Log.e("ERROR", "DBHelper().getUncompletedTasks(): Cannot Convert Date: " + vo);
		}

		vo.priority = cursor.getString(4);
		vo.priorityLevel = cursor.getInt(5);


		/*
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(0) + ": " + cursor.getString(0));
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(1) + ": " + cursor.getString(1));
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(2) + ": " + cursor.getInt(2));
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(3) + ": " + cursor.getString(3));
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(4) + ": " + cursor.getString(4));
		Log.d("Debug", "DBHelper().getUncompletedTasks()  " + cursor.getColumnName(5) + ": " + cursor.getString(5));
        */

		return vo;
	}

}
