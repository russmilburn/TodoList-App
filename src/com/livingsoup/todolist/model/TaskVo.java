package com.livingsoup.todolist.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: russell
 * Date: 15/09/2013
 * Time: 20:51
 * Project: ToDoListApp
 */
public class TaskVo implements Serializable
{
	public long id;
	public boolean completed;
	public String task;
	public Date dueDate;
	public String priority;
	public int priorityLevel;

	public TaskVo()
	{
		completed = false;
	}

	@Override
	public String toString()
	{
		return "[TaskVo:"
				+ " id: " + id
				+ ", completed: " + completed
				+ ", task: " + task
				+ ", dueDate: " + dueDate
				+ ", priority: " + priority
				+ ", priorityLevel: " + priorityLevel
				+ "]";
	}
}
