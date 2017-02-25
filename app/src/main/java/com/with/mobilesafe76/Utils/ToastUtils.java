package com.with.mobilesafe76.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void show(Context ctx,String msg){
		Toast.makeText(ctx, msg, 0).show();
	}

}
