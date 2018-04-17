package com.example.classloaderstudy;

import android.app.Application;
import android.util.Log;

/** 
 * @author ningerlei@danale.com
 * @version 2016-4-26 上午9:32:48
 * @类说明
 */

public class MyApplication extends Application {

	public void onCreate() {
		Log.d(MyApplication.class.getSimpleName(), "application");
	};
}
