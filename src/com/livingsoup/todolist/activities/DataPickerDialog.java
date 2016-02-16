package com.livingsoup.todolist.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import com.livingsoup.todolist.R;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 19/09/2013
 * Time: 11:32
 * Project: ToDoListApp
 */
public class DataPickerDialog extends DialogFragment
{

	protected DatePicker datePicker;

	public DataPickerDialog()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.date_dialog, container, false);
		((Button) view.findViewById(R.id.date_ok_btn)).setOnClickListener(okListener);
		((Button) view.findViewById(R.id.date_cancel_btn)).setOnClickListener(cancelListener);
		datePicker = (DatePicker) view.findViewById(R.id.due_date_picker);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getArguments().getLong(TaskActivity.DATE_EXTRA));
		datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		return view;
	}

	private View.OnClickListener okListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View view)
		{
			TaskActivity activity = (TaskActivity) getActivity();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, datePicker.getYear());
			calendar.set(Calendar.MONTH, datePicker.getMonth());
			calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
			activity.updateDueDate(calendar.getTime());
			dismiss();
			//Log.d("Debug", "DataPickerDialog().onClick()  view = [" + String.valueOf(datePicker.getYear()) + "-" + String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getDayOfMonth()) +"]");
		}
	};

	private View.OnClickListener cancelListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View view)
		{
			dismiss();
		}
	};

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog d = super.onCreateDialog(savedInstanceState);
		d.setTitle(R.string.date_dialog_title_text);
		return d;

	}
}
