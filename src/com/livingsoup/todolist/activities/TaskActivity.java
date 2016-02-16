package com.livingsoup.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.livingsoup.todolist.R;
import com.livingsoup.todolist.model.TaskVo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 15/09/2013
 * Time: 20:43
 * Project: ToDoListApp
 */
public class TaskActivity extends FragmentActivity
{

	public static final String TASK_EXTRA = "TaskExtra";
	public static final String DATE_EXTRA = "DateExtra";

	public static final int ADD_TASK_REQUEST = 0;
	public static final int EDIT_TASK_REQUEST = 1;

	private EditText editText;
	private RadioGroup radioGroup;
	private Date dueDate;
	private TaskVo currentTask;

	private static String DATE_PICKER_DIALOG = "datePickerDialog";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_fragment);

		editText = (EditText) findViewById(R.id.task_ip);
		radioGroup = (RadioGroup) findViewById(R.id.priorityGroup);

		RadioButton rb = (RadioButton) radioGroup.findViewById(R.id.none_rb);
		rb.setChecked(true);


		Intent i = getIntent();
		if (i.hasExtra(TASK_EXTRA))
		{

			currentTask = (TaskVo) i.getSerializableExtra(TASK_EXTRA);

			editText.setText(currentTask.task);

			if (currentTask.priority.equals("A"))
			{
				rb = (RadioButton) radioGroup.findViewById(R.id.high_rb);
				rb.setChecked(true);
			}
			else if (currentTask.priority.equals("B"))
			{
				rb = (RadioButton) radioGroup.findViewById(R.id.mid_rb);
				rb.setChecked(true);
			}
			else if (currentTask.priority.equals("C"))
			{
				rb = (RadioButton) radioGroup.findViewById(R.id.low_rb);
				rb.setChecked(true);
			}
			updateDueDate(currentTask.dueDate);
		}
		else
		{
			Date date = Calendar.getInstance().getTime();
			updateDueDate(date);
			currentTask = null;
		}
	}


	public void onSaveClick(View view)
	{
		if (currentTask == null)
		{
			currentTask = new TaskVo();
		}


		String task = editText.getText().toString();
		currentTask.task = task;

		if (dueDate != null)
		{
			currentTask.dueDate = dueDate;
		}
		else
		{
			currentTask.dueDate = Calendar.getInstance().getTime();
		}

		int selectedID = radioGroup.getCheckedRadioButtonId();
		String priority = "";
		if (selectedID != -1)
		{
			if (selectedID == R.id.low_rb)
			{
				priority = "C";
			}
			else if (selectedID == R.id.mid_rb)
			{
				priority = "B";
			}
			else if (selectedID == R.id.high_rb)
			{
				priority = "A";
			}
			else
			{
				priority = "";
			}
			currentTask.priority = priority;
		}

		Intent intent = new Intent();
		intent.putExtra(TASK_EXTRA, currentTask);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void onChooseDateClick(View view)
	{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		DialogFragment df = new DataPickerDialog();

		Bundle bundle = new Bundle();
		bundle.putLong(DATE_EXTRA, dueDate.getTime());
		df.setArguments(bundle);
		df.show(ft, DATE_PICKER_DIALOG);
	}

	public void updateDueDate(Date date)
	{
		this.dueDate = date;
		TextView dueDateText = (TextView) findViewById(R.id.due_date_txt);
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
		dueDateText.setText(dateFormat.format(date));
	}


}
