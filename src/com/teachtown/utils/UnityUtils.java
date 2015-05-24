package com.teachtown.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


public class UnityUtils {
	private static ProgressDialog mProgress;
	public static void showToast(Context context,String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	public static void showProgress(Context context,String msg) {
		mProgress = ProgressDialog.show(context, "", msg);
	}
	
	public static void stopProgress() {
		mProgress.cancel();
	}
}
