package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

public class ActivityManager {
	private static Stack<Activity> activityStack;
	private static ActivityManager instance;

	private ActivityManager() {
		activityStack = new Stack<Activity>();
	}

	public static ActivityManager getScreenManager() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	// 退出栈顶Activity
	public void popActivity(Activity activity) {
		if (activity != null) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			activityStack.remove(activity);
			activity.finish();
		}
	}

	// 获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty())
			activity = activityStack.lastElement();
		Log.v("actvity",activity.getLocalClassName());
		return activity;
	}

	// 将当前Activity推入栈中
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	// 退出栈中所有Activity
	public void popAllActivity() {
		while (!activityStack.empty()) {
			Activity activity = currentActivity();
			if (activity != null) {
				popActivity(activity);
			}
		}
		if (activityStack != null) {
			activityStack.clear();
		}
	}

	// 退出除首页外的其他Activity
	public void popTopActivity() {
		for (int i = 0; i < activityStack.size(); i++) {
			Activity activity = currentActivity();
			if (activity != null && !(activity instanceof MainActivity)) {
				Log.v("actvity",activity.getLocalClassName());
				popActivity(activity);
			}
		}
	}

	// 退出所有页面
	public void ExitAll() {
		try {
			for (Activity activity : activityStack) {
				if (activity != null )
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}
