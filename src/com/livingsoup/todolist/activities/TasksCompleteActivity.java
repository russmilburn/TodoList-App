package com.livingsoup.todolist.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Toast;
import com.livingsoup.todolist.R;
import com.livingsoup.todolist.dataservices.DBHelper;
import com.livingsoup.todolist.dataservices.DeleteTask;
import com.livingsoup.todolist.dataservices.ReadTasks;
import com.livingsoup.todolist.dataservices.UpdateTask;
import com.livingsoup.todolist.model.TaskAdapter;
import com.livingsoup.todolist.model.TaskVo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 16/09/2013
 * Time: 17:18
 * Project: ToDoListApp
 */
public class TasksCompleteActivity extends ListActivity implements IUpdateTask
{
	private TaskAdapter adapter;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_tasks);


		dbHelper = new DBHelper(this);

		registerForContextMenu(getListView());

		getDataFromDB();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		getDataFromDB();
	}

	public void updateTask(int position, boolean isCompleted)
	{

		UpdateTask updateThread = new UpdateTask(dbHelper);

		try
		{
			updateThread.execute(adapter.getItem(position)).get();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		MenuItem item = menu.findItem(R.id.edit_mi);
		item.setEnabled(false);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.task_item_context_menu, menu);
		MenuItem item = menu.findItem(R.id.edit_mi);
		item.setEnabled(false);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		TaskVo vo = null;
		if (item.getMenuInfo() != null && item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo)
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			vo = (TaskVo) getListAdapter().getItem(info.position);
		}
		switch (item.getItemId())
		{
			case R.id.delete_mi:
				DeleteTask deleteThread = new DeleteTask(dbHelper);

				boolean deleteSuccessful = false;
				try
				{
					deleteSuccessful = deleteThread.execute(vo).get();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}

				if (deleteSuccessful)
				{
					adapter.remove(vo);
					adapter.notifyDataSetChanged();
				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(), "ERROR: Deleting Task", Toast.LENGTH_SHORT);
					toast.show();
				}
				break;
		}
		return true;
	}

	protected void getDataFromDB()
	{
		ReadTasks readThread = new ReadTasks(dbHelper);

		ArrayList<TaskVo> listData = null;
		try
		{
			listData = readThread.execute(ReadTasks.GET_COMPLTED_TASKS).get();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}

		if (listData.size() > 0)
		{
			adapter = new TaskAdapter(this, listData);
		}
		else
		{
			adapter = new TaskAdapter(this);
		}
		setListAdapter(adapter);
	}


}
