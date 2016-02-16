package com.livingsoup.todolist.dataservices;

import android.os.AsyncTask;
import com.livingsoup.todolist.model.TaskVo;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 21/09/2013
 * Time: 10:40
 * Project: ToDoListApp
 */
public class ReadTasks extends AsyncTask<String, Void, ArrayList<TaskVo>>
{
	public final static String GET_UNCOMPLTED_TASKS = "getUncompletedTasks";
	public final static String GET_COMPLTED_TASKS = "getCompletedTasks";


	private DBHelper dbHelper;

	public ReadTasks(DBHelper dbHelper)
	{
		this.dbHelper = dbHelper;
	}

	@Override
	protected ArrayList<TaskVo> doInBackground(String... values)
	{
		ArrayList<TaskVo> data = null;
		if (values[0].equals(GET_UNCOMPLTED_TASKS))
		{
			data = dbHelper.getUncompletedTasks();
		}
		else if (values[0].equals(GET_COMPLTED_TASKS))
		{
			data = dbHelper.getCompletedTasks();
		}

		return data;
	}
}
