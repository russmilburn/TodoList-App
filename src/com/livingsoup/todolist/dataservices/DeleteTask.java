package com.livingsoup.todolist.dataservices;

import android.os.AsyncTask;
import com.livingsoup.todolist.model.TaskVo;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 21/09/2013
 * Time: 12:20
 * Project: ToDoListApp
 */
public class DeleteTask extends AsyncTask<TaskVo, Void, Boolean>
{
	DBHelper dbHelper;

	public DeleteTask(DBHelper dbHelper)
	{
		this.dbHelper = dbHelper;
	}

	@Override
	protected Boolean doInBackground(TaskVo... taskVos)
	{
		return dbHelper.deleteTask(taskVos[0]);
	}
}

