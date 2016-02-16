package com.livingsoup.todolist.dataservices;

import android.os.AsyncTask;
import com.livingsoup.todolist.model.TaskVo;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 21/09/2013
 * Time: 10:30
 * Project: ToDoListApp
 */
public class CreateTask extends AsyncTask<TaskVo, Void, TaskVo>
{
	private DBHelper dbHelper;

	public CreateTask(DBHelper dbHelper)
	{
		this.dbHelper = dbHelper;
	}

	@Override
	protected TaskVo doInBackground(TaskVo... taskVos)
	{
		return dbHelper.insertTask(taskVos[0]);
	}


}
