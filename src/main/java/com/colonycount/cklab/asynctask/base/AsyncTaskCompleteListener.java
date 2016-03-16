package com.colonycount.cklab.asynctask.base;


public interface AsyncTaskCompleteListener<T> {
	public void onTaskComplete(AsyncTaskPayload result, String taskName);
}
