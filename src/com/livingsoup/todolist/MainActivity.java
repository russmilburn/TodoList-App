package com.livingsoup.todolist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.livingsoup.todolist.activities.IUpdateTask;
import com.livingsoup.todolist.activities.TaskActivity;
import com.livingsoup.todolist.activities.TasksCompleteActivity;
import com.livingsoup.todolist.dataservices.*;
import com.livingsoup.todolist.model.TaskAdapter;
import com.livingsoup.todolist.model.TaskVo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ListActivity implements IUpdateTask
{

	private TaskAdapter adapter;
	private DBHelper dbHelper;
	private boolean refreshData = false;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dbHelper = new DBHelper(this);
		registerForContextMenu(getListView());

		getDataFromDB();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (refreshData == true)
		{
			getDataFromDB();
		}


	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.task_item_context_menu, menu);
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
			case R.id.edit_mi:
				launchEditTaskView(vo);
				break;
			case R.id.delete_mi:
				DeleteTask deleteThread = new DeleteTask(dbHelper);

				boolean deleteSuccessful = false;
				try
				{
					deleteSuccessful = deleteThread.execute(vo).get();
				}
				catch (InterruptedException ie)
				{
					ie.printStackTrace();
				}
				catch (ExecutionException ee)
				{
					ee.printStackTrace();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case TaskActivity.ADD_TASK_REQUEST:
				if (resultCode == RESULT_OK)
				{

					CreateTask create = new CreateTask(dbHelper);
					TaskVo vo = null;
					try
					{
						vo = create.execute((TaskVo) data.getSerializableExtra(TaskActivity.TASK_EXTRA)).get();
					}
					catch (InterruptedException ie)
					{
						ie.printStackTrace();
					}
					catch (ExecutionException ee)
					{
						ee.printStackTrace();
					}

					adapter.add(vo);
					adapter.notifyDataSetChanged();
				}
				break;
			case TaskActivity.EDIT_TASK_REQUEST:
				if (resultCode == RESULT_OK)
				{
					TaskVo vo = (TaskVo) data.getSerializableExtra(TaskActivity.TASK_EXTRA);

					UpdateTask updateThread = new UpdateTask(dbHelper);

					boolean recordUpdated = false;
					try
					{
						recordUpdated = updateThread.execute(vo).get();
					}
					catch (InterruptedException ie)
					{
						ie.printStackTrace();
					}
					catch (ExecutionException ee)
					{
						ee.printStackTrace();
					}

					if (recordUpdated)
					{
						TaskVo oldItem = adapter.getTaskById(vo.id);
						adapter.remove(oldItem);
						adapter.add(vo);
						adapter.notifyDataSetChanged();
					}
					else
					{
						Toast toast = Toast.makeText(getApplicationContext(), "ERROR: Updating Task", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				break;

		}
	}

	public void onTaskAddClick(View view)
	{
		Intent intent = new Intent(MainActivity.this, TaskActivity.class);
		startActivityForResult(intent, TaskActivity.ADD_TASK_REQUEST);
	}

	public void onViewCompletedTasksClick(View view)
	{
		Intent intent = new Intent(MainActivity.this, TasksCompleteActivity.class);
		refreshData = true;
		startActivity(intent);
	}

	public void updateTask(int position, boolean isCompleted)
	{
		UpdateTask updateThread = new UpdateTask(dbHelper);

		try
		{
			updateThread.execute(adapter.getItem(position)).get();
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		catch (ExecutionException ee)
		{
			ee.printStackTrace();
		}
	}

	private void launchEditTaskView(TaskVo vo)
	{
		Intent intent = new Intent(MainActivity.this, TaskActivity.class);
		intent.putExtra(TaskActivity.TASK_EXTRA, vo);
		startActivityForResult(intent, TaskActivity.EDIT_TASK_REQUEST);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		TaskVo vo = adapter.getItem(position);
		launchEditTaskView(vo);
	}

	protected void getDataFromDB()
	{
		ReadTasks readThread = new ReadTasks(dbHelper);

		ArrayList<TaskVo> listData = null;
		try
		{
			listData = readThread.execute(ReadTasks.GET_UNCOMPLTED_TASKS).get();
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
		refreshData = false;
	}
}
