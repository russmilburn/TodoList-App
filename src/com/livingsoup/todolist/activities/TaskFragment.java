package com.livingsoup.todolist.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.livingsoup.todolist.R;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 19/09/2013
 * Time: 12:09
 * Project: ToDoListApp
 */
public class TaskFragment extends Fragment
{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.task_fragment, container, false);
	}
}
