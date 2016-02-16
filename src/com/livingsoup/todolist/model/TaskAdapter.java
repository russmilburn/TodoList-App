package com.livingsoup.todolist.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.livingsoup.todolist.R;
import com.livingsoup.todolist.activities.IUpdateTask;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 15/09/2013
 * Time: 20:51
 * Project: ToDoListApp
 */
public class TaskAdapter extends ArrayAdapter<TaskVo>
{

	private LayoutInflater inflater;

	public TaskAdapter(Context context)
	{
		super(context, R.layout.list_item);
		inflater = LayoutInflater.from(context);
	}

	public TaskAdapter(Context context, ArrayList<TaskVo> data)
	{
		super(context, R.layout.list_item, data);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
			view = inflater.inflate(R.layout.list_item, null);
		}

		final TaskVo data = getItem(position);

		TextView taskText = (TextView) view.findViewById(R.id.task_txt);
		taskText.setText(data.task);

		if (data.priority.equals("A"))
		{
			taskText.setTextAppearance(getContext(), R.style.task_high);
		}
		else if (data.priority.equals("B"))
		{
			taskText.setTextAppearance(getContext(), R.style.task_mid);
		}
		else if (data.priority.equals("C"))
		{
			taskText.setTextAppearance(getContext(), R.style.task_low);
		}
		else
		{
			taskText.setTextAppearance(getContext(), R.style.task_none);
		}

		TextView dateText = (TextView) view.findViewById(R.id.list_item_due_date_txt);

		if (data.dueDate.getTime() < Calendar.getInstance().getTime().getTime() && data.completed == false)
		{
			dateText.setTextAppearance(getContext(), R.style.dateTextStyle_overdue);
		}
		else
		{
			dateText.setTextAppearance(getContext(), R.style.dateTextStyle);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
		dateText.setText(dateFormat.format(data.dueDate));

		CheckBox completedCb = (CheckBox) view.findViewById(R.id.completed_cb);
		completedCb.setChecked(data.completed);

		CheckBox.OnClickListener onClickListener = new View.OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				data.completed = ((CheckBox) view).isChecked();
				notifyDataSetChanged();
				((IUpdateTask) getContext()).updateTask(position, data.completed);
			}
		};

		completedCb.setOnClickListener(onClickListener);

		return view;
	}

	public TaskVo getTaskById(long id)
	{
		for (int i = 0; i < super.getCount(); i++)
		{
			if (getItem(i).id == id)
			{
				return getItem(i);
			}
		}
		return null;
	}
}
